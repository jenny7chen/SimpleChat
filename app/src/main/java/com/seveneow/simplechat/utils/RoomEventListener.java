package com.seveneow.simplechat.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.seveneow.simplechat.model.Message;


public class MessageChangeListener implements ChildEventListener {

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    Message message = dataSnapshot.getValue(Message.class);

    // Check for null
    if (message == null) {
      DebugLog.e("ba", "message data is null");
      return;
    }
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_NOTIFICATION;
    event.object = remoteMessage.getNotification().getBody();
    RxEventBus.send(event);
    onReceiveMessage(new MessageParser().parse(message));
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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