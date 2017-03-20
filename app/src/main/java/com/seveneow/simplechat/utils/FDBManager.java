package com.seveneow.simplechat.utils;

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
import com.seveneow.simplechat.presenter.ChatPresenter;

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

  public static void checkDataInit() {
    RoomManager.getInstance().hasRoomData();
    if (!RoomManager.getInstance().hasRoomData()) {
      getUserRoomList();
    }else{
      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_ROOM_LIST_UPDATE;
      RxEventBus.send(event);
    }
  }

  private static void getUserRoomList() {
    databaseRef.child("users").child(Static.userId).child("rooms").addListenerForSingleValueEvent(new UserRoomsEventListener());
  }

  public static void onGotUserRooms(ArrayList<String> roomIdList) {
    DebugLog.e("Baa", "roomList = " + roomIdList);

    for (String roomId : roomIdList) {
      Room room = new Room();
      room.setId(roomId);
      RoomManager.getInstance().addRoom(room);
    }

    for(String roomId : roomIdList){
      getRoomData(roomId);
    }
  }

  private static void getRoomData(String roomId) {
    DebugLog.e("baaa", "get room data = "+roomId);
    databaseRef.child("rooms").child(roomId).addListenerForSingleValueEvent(new RoomEventListener());
  }

  public static void onGotRoomData(Room room){
    DebugLog.e("baaa", "got room data = "+room);
    if (room == null) {
      return;
    }
    RoomManager.getInstance().addRoom(room);
  }

  public static void initRoomMessages(String roomId, ChatPresenter presenter) {
    ArrayList<Message> roomMessages = new ArrayList<>();
    databaseRef.child("messages").child(roomId).orderByChild("timestamp").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getChildren() == null)
          return;
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          Message message = new MessageParser(presenter).parse(postSnapshot);
          if (message == null)
            continue;
          roomMessages.add(message);
        }
        RoomManager.getInstance().updateRoomMessages(roomId, roomMessages);
        RxEvent event = new RxEvent();
        event.id = RxEvent.EVENT_ROOM_MESSAGE_INIT;
        event.object = roomId;
        RxEventBus.send(event);
        DebugLog.e("baaa", "message init");
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

  public static String getMessagePushKey(String roomId){
    return databaseRef.child("messages").child(roomId).push().getKey();
  }

  public static String sendMessage(String key, String roomId, Message message) {
    Message messageSend = MessageGenerator.copyMessage(message);

    Map<String, Object> roomPushValues = new HashMap<>();
    roomPushValues.put("show_text", message.getShowText());
    roomPushValues.put("timestamp", message.getTime());

    messageSend.setPending(false);
    messageSend.setPendingId("");
    Map<String, Object> pushValues = messageSend.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/messages/" + roomId + "/" + key, pushValues);
    childUpdates.put("/rooms/" + roomId + "/latest_message/", roomPushValues);
    databaseRef.updateChildren(childUpdates).addOnSuccessListener((Void) -> {
      //inform message sent, need pending id for layout update
      message.setPending(false);
      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_DATA_UPDATE_NOTIFICATION;
      event.params = new String[]{roomId};
      event.object = message;
      RxEventBus.send(event);

      Room room = RoomManager.getInstance().getRoomById(roomId);
      room.setLatestMessageShowText(message.getShowText());
      RxEvent roomEvent = new RxEvent();
      roomEvent.id = RxEvent.EVENT_ROOM_LIST_UPDATE;
      RxEventBus.send(event);

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
