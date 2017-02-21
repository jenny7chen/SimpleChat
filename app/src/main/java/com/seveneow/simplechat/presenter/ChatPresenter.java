package com.seveneow.simplechat.presenter;


import android.content.Intent;
import android.os.Handler;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.service.FetchMessageService;
import com.seveneow.simplechat.service.SendMessageService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.MessageGenerator;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.view_interface.ChatMvpView;

import java.util.ArrayList;

public class ChatPresenter extends BasePresenter<ChatMvpView> {
  ArrayList<Message> messageList = new ArrayList<>();
  private String roomId;

  public void setRoomData(Intent intent) {
    roomId = intent.getStringExtra("roomId");
  }

  public void sendMessage(String messageText) {
    if (!isViewAttached())
      return;

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      Message message = MessageGenerator.getPendingTextMessage(messageText);
      RoomManager.getInstance().addPendingMessage(roomId, message);
      messageList.add(0, message);
      getView().updateData(messageList, true, true);
      getView().showContent();


      //send message
      //TODO:bind service of sending message

      //test use
      getView().startService(SendMessageService.class, SendMessageService.generateDataIntent((TextMessage) message));

    }, 2); // set a delay for message sent
  }

  public void fetchMessages() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //get data here using asynchttp lib

    Intent intent = new Intent();
    intent.putExtra(FetchMessageService.PARAM_ROOM_ID, roomId);
    getView().startService(FetchMessageService.class, intent);
  }

  public void onMessagesAdded(String roomId, Message message) {
    if (!isViewAttached())
      return;

    if (!this.roomId.equals(roomId)) {
      return;
    }
    messageList.add(0, message);
    getView().updateData(messageList, true, true);
    getView().showContent();
  }

  public void onMessagesUpdated(String roomId, Message... messages) {
    if (!isViewAttached())
      return;

    if (!this.roomId.equals(roomId)) {
      return;
    }
    boolean isWholeListUpdate = messages == null || messages.length == 0;
    this.messageList = RoomManager.getInstance().getRoomById(roomId).getShowMessages();

    if (isWholeListUpdate) {
      DebugLog.e("baaa", "whole list");
      getView().updateData(messageList, false, false);
      for (Message message : messageList) {
        DebugLog.e("baa", "message = " + message.getMessageId());
      }
    }
    else {
      DebugLog.e("baaa", "single message");
      getView().updateData(messageList, true, false);
    }

    getView().showContent();
  }

  @Override
  public void onEvent(RxEvent event) {
    if (event.id == RxEvent.EVENT_NOTIFICATION) {
      onReceiveSocketMessage((String) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGES_UPDATED) {
      onMessagesUpdated((String) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_SINGLE_MESSAGES_UPDATED) {
      onMessagesUpdated((String) event.params[0], (Message) event.object);

    }
    else {
      onMessagesAdded((String) event.params[0], (Message) event.object);
    }
  }

  public void onReceiveSocketMessage(String notificationMessage) {
    if (!isViewAttached())
      return;

    MessageParser parser = new MessageParser();
    Message message = parser.parse(notificationMessage);

    if (message == null)
      return;

    if (message.getSenderId().isEmpty()) {
      RoomManager.getInstance().updateRoomSingleMessage(roomId, message);
    }
    else {
      RoomManager.getInstance().addRoomSingleMessage(roomId, message);
    }
  }
}
