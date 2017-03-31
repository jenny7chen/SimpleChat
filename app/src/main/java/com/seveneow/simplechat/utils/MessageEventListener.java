package com.seveneow.simplechat.utils;

import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.service.HandleNewMessageService;


public class MessageEventListener implements ChildEventListener {
  private String roomId;
  private BasePresenter presenter;

  public MessageEventListener(String roomId, BasePresenter presenter) {
    this.roomId = roomId;
    this.presenter = presenter;
  }

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    onGotNewMessage(dataSnapshot);
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    onGotNewMessage(dataSnapshot);
  }

  private synchronized void onGotNewMessage(DataSnapshot dataSnapshot) {
    Message message = new MessageParser(presenter).parse(dataSnapshot);

    if (message == null) {
      DebugLog.e("ba", "receive new message data is null");
      return;
    }
    Message localMessage = RoomManager.getInstance().getRoomById(roomId).getMessages().get(message.getId());
    if (localMessage != null && localMessage.isPending()) {
      //prevent message sent by this device from being called back instantly.
      //because firebase off-line database will call back to here immediately after we pushed data to it.
      //it makes us not able to get the true sent callback
      return;
    }

    Intent intent = new Intent();
    intent.putExtra(HandleNewMessageService.PARAM_ROOM_ID, roomId);
    intent.putExtra(HandleNewMessageService.PARAM_MESSAGE, message);
    presenter.startService(HandleNewMessageService.class, intent);
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