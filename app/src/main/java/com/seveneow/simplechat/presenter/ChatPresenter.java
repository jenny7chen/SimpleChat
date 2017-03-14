package com.seveneow.simplechat.presenter;


import android.content.Intent;
import android.os.Handler;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.service.FetchMessageService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageGenerator;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.view_interface.BasicListMvpView;
import com.seveneow.simplechat.view_interface.ChatListMvpView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatPresenter extends BasePresenter<ChatListMvpView> {
  ArrayList<Message> messageList = new ArrayList<>();
  private String roomId;

  public void setRoomData(Intent intent) {
    roomId = intent.getStringExtra("roomId");
    DebugLog.e("baaa", "enter room : " + roomId);
    Room room = RoomManager.getInstance().getRoomById(roomId);
    getView().setTitle(room.getName());
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

  public void sendMessage(String messageText) {
    if (!isViewAttached())
      return;

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      Message message = MessageGenerator.getPendingTextMessage(messageText);
      RoomManager.getInstance().addPendingMessage(roomId, message);

      //send message
      //TODO:bind service of sending message

      //test use
      FDBManager.sendMessage(roomId, message);
      //      getView().startService(SendMessageService.class, SendMessageService.generateDataIntent("456", (TextMessage) message));

    }, 2); // set a delay for message sent
  }

  public void onMessagesAdded(String roomId, Message message) {
    if (!isViewAttached())
      return;

    if (!this.roomId.equals(roomId)) {
      return;
    }

    messageList.add(0, message);
    updateData(messageList, true, true);
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
      updateData(messageList, false, false);
    }
    else {
      List<Message> updatedMessages = Arrays.asList(messages);
      updateData(updatedMessages, true, false);
    }

    getView().showContent();
  }

  @Override
  public void onEvent(RxEvent event) {
    if (event.id == RxEvent.EVENT_NOTIFICATION) {

      onReceiveMessage((String) event.params[0], (String) event.object);
    }
    else if (event.id == RxEvent.EVENT_DATA_UPDATE_NOTIFICATION) {
      onReceiveMessage((String) event.params[0], (Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGES_UPDATED) {
      onMessagesUpdated((String) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_SINGLE_MESSAGES_UPDATED) {
      onMessagesUpdated((String) event.params[0], (Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGES_ADDED) {
      onMessagesAdded((String) event.params[0], (Message) event.object);
    }
  }

  public void onReceiveMessage(String roomId, String notificationMessage) {
    MessageParser parser = new MessageParser();
    Message message = parser.parse(notificationMessage);

    onReceiveMessage(roomId, message);
  }

  /**
   * when received message from web or other users client or got messages by init listener
   */
  public void onReceiveMessage(String roomId, Message message) {
    if (!isViewAttached())
      return;

    if (!roomId.equals(this.roomId))
      return;

    if (message == null)
      return;

    RoomManager.getInstance().addMessage(roomId, message);
  }

  public synchronized void updateData(List<Message> updatedData, boolean isSingleMessage, boolean isInsert) {
    if (!isViewAttached())
      return;

    if (isSingleMessage) {
      boolean isAtBottom = getView().listIsAtBottom();
      if (isInsert) {
        getView().notifyChanged(ChatListMvpView.NOTIFY_DATA_INSERT, 0);
        if (isAtBottom || Static.isMessageFromMe(updatedData.get(0)))
          getView().scrollToBottom();
        else if (!Static.isMessageFromMe(updatedData.get(0))) {
          getView().showSnackBar(R.string.snack_got_new_message);
        }
      }
      else {
        getView().notifyChanged(BasicListMvpView.NOTIFY_DATA_RANGE_CHANGED, 0, getView().getItemCount(), updatedData.get(0));
        if (isAtBottom) {
          getView().scrollToBottom();
        }
      }
    }
    else {
      if (getView().getData() == null)
        getView().setDataToList((List<Object>) (Object) updatedData);
      getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    }
  }

}
