package com.seveneow.simplechat.presenter;


import android.content.Intent;
import android.os.Handler;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.service.GetDBMessageListService;
import com.seveneow.simplechat.service.GetServerMessageListService;
import com.seveneow.simplechat.service.SendMessagesService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageEventListener;
import com.seveneow.simplechat.utils.MessageGenerator;
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
  private static final boolean IS_INIT = true;
  private String roomId;

  public void setRoomId(Intent intent) {
    roomId = intent.getStringExtra("roomId");
  }

  public void initRoomData() {
    if (!isViewAttached())
      return;

    Room room = RoomManager.getInstance().getRoomById(roomId);

    if (room == null) {
      return;
    }

    DebugLog.e("baaa", "enter room : " + roomId);
    getView().setTitle(room.getName());
    initMessages();
  }

  private void initMessages() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //get data here using asynchttp lib

    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room.hasMessages()) {
      onMessagesUpdated();
      onMessagesInit();
      getView().showContent();
      return;
    }
    getDataFromDB(IS_INIT);
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

  @Override
  public void onEvent(RxEvent event) {
    String roomId = event.roomId;
    if (event.id == RxEvent.EVENT_ROOM_LIST_UPDATE) {
      initRoomData();

    }
    else if (!this.roomId.equals(roomId)) {
      return;
    }

    if (event.id == RxEvent.EVENT_ROOM_MESSAGE_LIST_UPDATED) {
      onMessagesUpdated();
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_UPDATED) {
      onMessagesUpdated((Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_ADDED) {
      onMessagesAdded((Message) event.object);
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_INIT) {
      onMessagesInit();
    }
    else if (event.id == RxEvent.EVENT_ROOM_MESSAGE_IS_SAVED_TO_DB) {
      DebugLog.e("Baaa", "message saved fetch messags");
      getDataFromDB(!IS_INIT);
    }
  }

  private void getDataFromDB(boolean notifyInit) {
    if (!isViewAttached())
      return;
    Intent intent = new Intent();
    intent.putExtra(GetDBMessageListService.PARAM_ROOM_ID, roomId);
    intent.putExtra(GetDBMessageListService.PARAM_IS_INIT, notifyInit);
    getView().startService(GetDBMessageListService.class, intent);
  }

  private void onMessagesInit() {
    if (!isViewAttached())
      return;

    Intent intent = new Intent();
    intent.putExtra(GetServerMessageListService.PARAM_ROOM_ID, roomId);
    getView().startService(GetServerMessageListService.class, intent);

    if (RoomManager.getInstance().getRoomById(roomId).hasMessages()) {
      FDBManager.addRoomEventListener(roomId, new MessageEventListener(roomId, this));
    }
  }

  public void onMessagesAdded(Message message) {
    if (!isViewAttached())
      return;

    messageList.add(0, message);
    updateViewData(messageList, true, true);
    getView().showContent();
  }

  public void onMessagesUpdated(Message... messages) {
    if (!isViewAttached())
      return;

    boolean isWholeListUpdate = messages == null || messages.length == 0;
    this.messageList = RoomManager.getInstance().getRoomById(roomId).getShowMessages();

    if (isWholeListUpdate) {
      updateViewData(messageList, false, false);
      FDBManager.addRoomEventListener(roomId, new MessageEventListener(roomId, this));
    }
    else {
      List<Message> updatedMessages = Arrays.asList(messages);
      updateViewData(updatedMessages, true, false);
    }

    getView().showContent();
  }

  public synchronized void updateViewData(List<Message> updatedData, boolean isSingleMessage, boolean isInsert) {
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
      if (getView().getData() == null)
        getView().setDataToList((List<Object>) (Object) updatedData);
      getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    }
  }

}
