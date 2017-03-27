package com.seveneow.simplechat.presenter;


import android.content.Intent;
import android.os.Handler;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.service.FetchLocalMessagesService;
import com.seveneow.simplechat.service.InitMessageService;
import com.seveneow.simplechat.service.SendMessagesService;
import com.seveneow.simplechat.service.UpdateMessageService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageEventListener;
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
    if(room != null)
    getView().setTitle(room.getName());
  }

  public void fetchMessages() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //get data here using asynchttp lib

    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room.hasMessages()) {
      onMessagesUpdated(roomId);
      onMessagesInit();
      getView().showContent();
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(InitMessageService.PARAM_ROOM_ID, roomId);
    getView().startService(InitMessageService.class, intent);
  }

  public void sendMessage(String messageText) {
    if (!isViewAttached())
      return;

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      Message message = MessageGenerator.getPendingTextMessage(roomId, messageText, RoomManager.getInstance().getRoomById(roomId).getType());
      message.setId(FDBManager.getMessagePushKey(roomId));

      //send message
      //TODO:bind service of sending message
      Intent intent = new Intent();
      intent.putExtra(SendMessagesService.PARAM_ROOM_ID, roomId);
      intent.putExtra(SendMessagesService.PARAM_MESSAGE, message);
      getView().startService(SendMessagesService.class, intent);

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
      FDBManager.addRoomEventListener(roomId, new MessageEventListener(roomId, this));
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
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_LIST_UPDATED) {
      onMessagesUpdated((String) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_UPDATED) {
      onMessagesUpdated((String) event.params[0], (Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_ADDED) {
      onMessagesAdded((String) event.params[0], (Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_INIT) {
      if (roomId.equals(event.object))
        onMessagesInit();
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_SAVED) {
      DebugLog.e("Baaa", "message saved fetch messags");
      if (roomId.equals(event.object))
        updateFromLocalDB();
    }
  }

  private void updateFromLocalDB() {
    if (!isViewAttached())
      return;
    Intent intent = new Intent();
    intent.putExtra(FetchLocalMessagesService.PARAM_ROOM_ID, roomId);
    getView().startService(FetchLocalMessagesService.class, intent);
  }

  private void onMessagesInit() {
    if (!isViewAttached())
      return;

    if (RoomManager.getInstance().getRoomById(roomId).hasMessages()) {
      getView().showContent();
      DebugLog.e("Baaaa", " onMessage init show content");
      FDBManager.addRoomEventListener(roomId, new MessageEventListener(roomId, this));
    }
    Intent intent = new Intent();
    intent.putExtra(UpdateMessageService.PARAM_ROOM_ID, roomId);
    getView().startService(UpdateMessageService.class, intent);
  }

  public void onReceiveMessage(String roomId, String notificationMessage) {
    MessageParser parser = new MessageParser(this);
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

    RoomManager.getInstance().addOrUpdateMessage(roomId, message);
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
      //check data is null, when set the list when in first time updating the data or just notify for the same list
      if (getView().getData() == null || getView().getData().size() == 0)
        getView().setDataToList((List<Object>) (Object) updatedData);
      getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    }
  }

}
