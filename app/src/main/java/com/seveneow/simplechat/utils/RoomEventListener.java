package com.seveneow.simplechat.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.seveneow.simplechat.model.Message;


public class RoomEventListener implements ChildEventListener {
  private String roomId;

  public RoomEventListener(String roomId) {
    this.roomId = roomId;
  }

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);

    if (message == null) {
      DebugLog.e("ba", "message data is null");
      return;
    }
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_MESSAGES_ADDED;
    event.params = new String[]{roomId};
    event.object = message;
    RxEventBus.send(event);
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);

    if (message == null) {
      DebugLog.e("la", "message data is null");
      return;
    }
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_SINGLE_MESSAGES_UPDATED;
    event.params = new String[]{roomId};
    event.object = message;
    RxEventBus.send(event);
  }

  @Override
  public void onChildRemoved(DataSnapshot dataSnapshot) {

  }

  @Override
  public void onChildMoved(DataSnapshot dataSnapshot, String s) {

  }

  @Override
  public void onCancelled(DatabaseError databaseError) {

  }
}