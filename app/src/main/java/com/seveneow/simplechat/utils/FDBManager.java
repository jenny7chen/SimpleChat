package com.seveneow.simplechat.utils;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Post;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.User;
import com.seveneow.simplechat.service.SaveMessageService;
import com.seveneow.simplechat.service.SaveRoomService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FDBManager {
  private static DatabaseReference databaseRef;

  public static void init() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.setPersistenceEnabled(true);
    databaseRef = database.getReference("database");
    databaseRef.keepSynced(true);
  }

  public static void getServerRoomList(String userId, Context context) {
    databaseRef.child("users").child(userId).child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<String> roomIdList = new ArrayList<String>();
        for (DataSnapshot roomId : dataSnapshot.getChildren()) {
          roomIdList.add(roomId.getKey());
        }

        FDBManager.onGotRoomList(roomIdList, context);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  public static void onGotRoomList(ArrayList<String> roomIdList, Context context) {
    DebugLog.e("Baa", "roomList = " + roomIdList);

    for (String roomId : roomIdList) {
      Room room = new Room();
      room.setId(roomId);
      RoomManager.getInstance().addRoom(room);
    }

    for (String roomId : roomIdList) {
      getServerRoomData(roomId, context);
    }
  }

  private static void getServerRoomData(String roomId, Context context) {
    DebugLog.e("baaa", "get room data = " + roomId);
    databaseRef.child("rooms").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Room room = new RoomParser().parse(dataSnapshot);
        FDBManager.onGotRoomData(room, context);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  public static void onGotRoomData(Room room, Context context) {
    DebugLog.e("baaa", "got room data = " + room);
    if (room == null) {
      return;
    }
    RoomManager.getInstance().addRoom(room);
    Intent intent = new Intent(context, SaveRoomService.class);
    intent.putExtra(SaveRoomService.PARAM_ROOMS, room);
    context.startService(intent);
  }

  public static void getServerMessages(String roomId, Context context) {
    databaseRef.child("messages").child(roomId).orderByChild("timestamp").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<Message> roomMessages = new ArrayList<>();
        if (dataSnapshot.getChildren() == null)
          return;
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          Message message = new MessageParser(context).parse(postSnapshot);
          if (message == null)
            continue;
          message.setRoomId(roomId);
          roomMessages.add(message);
        }
        DebugLog.e("baa", "onServer message get");
        Intent intent = new Intent(context, SaveMessageService.class);
        intent.putExtra(SaveMessageService.PARAM_ROOM_ID, roomId);
        intent.putExtra(SaveMessageService.PARAM_MESSAGES, roomMessages);
        context.startService(intent);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        DebugLog.e("la", databaseError.getMessage());

      }
    });
  }

  public static void createRoom(Room room) {
    String roomId = databaseRef.child("rooms").push().getKey();
    Map<String, Object> pushValues = room.toMap();
    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/rooms/" + roomId, pushValues);
    databaseRef.updateChildren(childUpdates);
  }

  public static void createUser(User user) {
    String userId = databaseRef.child("users").push().getKey();
    databaseRef.child("users").child(userId).setValue(user);
  }

  public static String getMessagePushKey(String roomId) {
    return databaseRef.child("messages").child(roomId).push().getKey();
  }

  public static String sendMessage(String key, String roomId, Message message, Context context) {
    Message messageSend = MessageGenerator.copyMessage(message);

    Map<String, Object> roomPushValues = new HashMap<>();
    roomPushValues.put("show_text", message.getShowText());
    roomPushValues.put("timestamp", message.getTime());

    messageSend.setPending(false);
    Map<String, Object> pushValues = messageSend.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/messages/" + roomId + "/" + key, pushValues);
    childUpdates.put("/rooms/" + roomId + "/latest_message/", roomPushValues);
    databaseRef.updateChildren(childUpdates).addOnSuccessListener((Void) -> {
      //inform message sent, need pending id for layout update
      message.setPending(false);

      ArrayList<Message> messages = new ArrayList<Message>();
      messages.add(message);
      Intent intent = new Intent(context, SaveMessageService.class);
      intent.putExtra(SaveMessageService.PARAM_ROOM_ID, roomId);
      intent.putExtra(SaveMessageService.PARAM_NOTIFY_CHANGE, false);
      intent.putExtra(SaveMessageService.PARAM_MESSAGES, messages);
      context.startService(intent);

      RoomManager.getInstance().addOrUpdateMessage(roomId, message);

      //TODO: update room information in DB
      Room room = RoomManager.getInstance().getRoomById(roomId);

      String text = message.getShowText();
      try {
        text = URLDecoder.decode(text, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      room.setLatestMessageShowText(text);
      RxEventSender.notifyRoomListUpdated();

    }).addOnFailureListener((Exception) -> {
      //TODO: update failure status on list
    });
    return key;
  }

  /**
   * transaction working with data that could be corrupted by concurrent modifications
   */
  public static void updateGroupName(String roomId, String newName) {
    databaseRef.child("rooms").child(roomId).runTransaction(new Transaction.Handler() {
      @Override
      public Transaction.Result doTransaction(MutableData mutableData) {
        Room p = mutableData.getValue(Room.class);
        if (p == null) {
          return Transaction.success(mutableData);
        }
        p.setName(newName);
        mutableData.setValue(p);
        return Transaction.success(mutableData);
      }

      @Override
      public void onComplete(DatabaseError databaseError, boolean b,
                             DataSnapshot dataSnapshot) {
        DebugLog.d("ba", "updateGroupName:onComplete:" + databaseError);
      }
    });
  }

  public static void updateGroupPhoto(String roomId, String photoUrl) {
    databaseRef.child("rooms").child(roomId).runTransaction(new Transaction.Handler() {
      @Override
      public Transaction.Result doTransaction(MutableData mutableData) {
        Room p = mutableData.getValue(Room.class);
        if (p == null) {
          return Transaction.success(mutableData);
        }
        p.setPhoto(photoUrl);
        mutableData.setValue(p);
        return Transaction.success(mutableData);
      }

      @Override
      public void onComplete(DatabaseError databaseError, boolean b,
                             DataSnapshot dataSnapshot) {
        DebugLog.d("ba", "updateGroupName:onComplete:" + databaseError);
      }
    });
  }

  public static void joinGroup(String userId, String roomId) {
    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/users/" + userId + "/rooms/" + roomId, true);
    childUpdates.put("/rooms/" + roomId + "/members/" + userId, true);

    databaseRef.updateChildren(childUpdates);
  }

  public static void leaveGroup(String userId, String roomId) {
    databaseRef.child("users").child("rooms").child(roomId).removeValue();
    databaseRef.child("rooms").child("members").child(userId).removeValue();
  }

  public static void writeNewPost(String roomId, Post post) {
    // Create new post at /groups/$groupId/$posts/$postId and at
    // /posts/$postId simultaneously
    String key = databaseRef.child("posts").push().getKey();
    Map<String, Object> postValues = post.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/posts/" + key, postValues);
    childUpdates.put("/rooms/" + roomId + "/" + "posts/" + key, postValues);

    databaseRef.updateChildren(childUpdates);

  }

  public static void addRoomEventListener(String roomId, ChildEventListener childEventListener) {
    databaseRef.child("messages").child(roomId).addChildEventListener(childEventListener);
  }

  public static void removeRoomEventListener(String roomId, ChildEventListener childEventListener) {
    databaseRef.child("messages").child(roomId).removeEventListener(childEventListener);
  }
}
