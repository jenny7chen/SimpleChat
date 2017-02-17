package com.seveneow.simplechat.presenter;


import android.content.Intent;
import android.os.Handler;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.service.FetchMessageService;
import com.seveneow.simplechat.service.SendMessageService;
import com.seveneow.simplechat.utils.BasePresenter;
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
      getView().updatePendingData(messageList, true);
      getView().showContent();

      //send message
      //TODO:bind service of sending message

      //test use
      getView().startService(SendMessageService.class, SendMessageService.generateDataIntent((TextMessage) message));

    }, 2);
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

  public void onReceiveMessage(String notificationMessage) {
    if (!isViewAttached())
      return;

    MessageParser parser = new MessageParser();
    Message message = parser.parse(notificationMessage);

    if (message.getSenderId().isEmpty()) {
      RoomManager.getInstance().updateRoomSingleMessage(roomId, message);
    }
    else {
      messageList.add(0, message);
      getView().updateData(messageList, true);
      getView().showContent();
    }
  }

  public void onUpdatedMessages(String roomId) {
    if (!isViewAttached())
      return;

    if (!this.roomId.equals(roomId)) {
      return;
    }
    this.messageList = RoomManager.getInstance().getRoomById(roomId).getShowMessages();
    getView().updateData(messageList, false);
    getView().showContent();
  }

  @Override
  public void onEvent(RxEvent event) {
    if (event.id == RxEvent.EVENT_NOTIFICATION) {
      onReceiveMessage((String) event.object);
    }
    else {
      onUpdatedMessages((String) event.object);
    }
  }
}
