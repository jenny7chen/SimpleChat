package com.seveneow.simplechat.utils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.seveneow.simplechat.model.Message;


public class MessageEventListener implements ChildEventListener {
  private String roomId;
  private BasePresenter presenter;

  public MessageEventListener(String roomId, BasePresenter presenter) {
    this.roomId = roomId;
    this.presenter = presenter;
  }

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    Message message = new MessageParser(presenter).parse(dataSnapshot);

    if (message == null) {
      DebugLog.e("ba", "message data is null");
      return;
    }

    if (Static.isMessageSentFromLocal(message))
      return;

    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_DATA_UPDATE_NOTIFICATION;
    event.params = new String[]{roomId};
    event.object = message;
    RxEventBus.send(event);
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    Message message = new MessageParser(presenter).parse(dataSnapshot);

    if (message == null) {
      DebugLog.e("la", "message data is null");
      return;
    }

    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_DATA_UPDATE_NOTIFICATION;
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