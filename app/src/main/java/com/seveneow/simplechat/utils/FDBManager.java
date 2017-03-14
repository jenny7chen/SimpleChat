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
    initRoomList();
  }

  public static void checkDataInit() {
    RoomManager.getInstance().hasRoomData();
    if (!RoomManager.getInstance().hasRoomData()) {
      initRoomList();
    }
  }

  private static void initRoomList() {
    databaseRef.child("users").child(Static.userId).child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<String> roomIdList = new ArrayList<String>();
        for (DataSnapshot roomId : dataSnapshot.getChildren()) {
          roomIdList.add(roomId.getKey());
        }
        DebugLog.e("Baa", "roomList = " + roomIdList);
        for (String roomId : roomIdList) {
          addRoom(roomId);
          FDBManager.addRoomEventListener(roomId, new RoomEventListener(roomId));
          initRoomMessages(roomId);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  private static void addRoom(String roomId) {
    databaseRef.child("rooms").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Room room = new RoomParser().parse(dataSnapshot);
        if (room == null) {
          return;
        }
        RoomManager.getInstance().addRoom(room);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        DebugLog.e("ba", databaseError.getMessage());

      }
    });
  }

  private static void initRoomMessages(String roomId) {
    ArrayList<Message> roomMessages = new ArrayList<>();
    databaseRef.child("messages").child(roomId).orderByChild("timestamp").limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getChildren() == null)
          return;
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          Message message = new MessageParser().parse(postSnapshot);
          if (message == null)
            continue;
          roomMessages.add(message);
        }
        RoomManager.getInstance().updateRoomMessages(roomId, roomMessages);
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

  public static void sendMessage(String roomId, Message message) {
    String key = databaseRef.child("messages").child(roomId).push().getKey();
    Map<String, Object> pushValues = message.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/messages/" + roomId + "/" + key, pushValues);
    databaseRef.updateChildren(childUpdates).addOnSuccessListener((Void) -> {
      //inform message sent, need pending id for layout update
      message.setPending(false);
      message.setMessageId(key);
      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_DATA_UPDATE_NOTIFICATION;
      event.params = new String[]{roomId};
      event.object = message;
      RxEventBus.send(event);

      //update message pending status
      Message messageSend = MessageGenerator.copyMessage(message);
      messageSend.setPendingId("");
      FDBManager.updateMessageSentStatus(roomId, messageSend);

    }).addOnFailureListener((Exception) -> {
      //TODO: update failure status on list
    });
  }

  public static void updateMessageSentStatus(String roomId, Message message) {
    Map<String, Object> pushValues = message.toMap();

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/messages/" + roomId + "/" + message.getMessageId(), pushValues);
    databaseRef.updateChildren(childUpdates).addOnFailureListener((Exception) -> updateMessageSentStatus(roomId, message));
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
