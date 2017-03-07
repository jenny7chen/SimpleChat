package com.seveneow.simplechat.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Post;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.User;

import java.util.HashMap;
import java.util.Map;


public class FDBActionManager {
  private static DatabaseReference databaseRef;

  public static void init() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.setPersistenceEnabled(true);
    databaseRef = database.getReference("database");
    databaseRef.keepSynced(true);
  }

  public static void initListeners(){

    eventListener = new RoomEventListener(roomId);
    FDBActionManager.addRoomEventListener(eventListener);
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
    databaseRef.updateChildren(childUpdates);
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

  public static void addRoomEventListener(ChildEventListener childEventListener) {
    databaseRef.addChildEventListener(childEventListener);
  }

  public static void removeRoomEventListener(ChildEventListener childEventListener) {
    databaseRef.removeEventListener(childEventListener);
  }
}
