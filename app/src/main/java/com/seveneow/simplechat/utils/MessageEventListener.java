package com.seveneow.simplechat.utils;

import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.service.SaveMessageService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;


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
    DebugLog.e("baaa", "message from message event listener : " + message.getShowText()+" id = " + message.getId());

    if (Static.isMessageSentFromLocal(message))
      return;

    addNewMessage(message);
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    Message message = new MessageParser(presenter).parse(dataSnapshot);

    if (message == null) {
      DebugLog.e("la", "message data is null");
      return;
    }
    DebugLog.e("baaa", "message from message event listener : onChildChanged" + message.getShowText()+" id = " + message.getId());

    if (Static.isMessageSentFromLocal(message))
      return;

    addNewMessage(message);
  }

  private synchronized void addNewMessage(Message message) {
    ArrayList<Message> messages = new ArrayList<Message>();
    messages.add(message);

    boolean isAdd = RoomManager.getInstance().addOrUpdateMessage(roomId, message);

    //TODO: check data base data
    if(isAdd) {
      Intent intent = new Intent();
      intent.putExtra(SaveMessageService.PARAM_ROOM_ID, roomId);
      intent.putExtra(SaveMessageService.PARAM_NOTIFY_CHANGE, false);
      intent.putExtra(SaveMessageService.PARAM_MESSAGES, messages);
      presenter.startService(SaveMessageService.class, intent);

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
      RxEventSender.notifyRoomListUpdated(room);
    }
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