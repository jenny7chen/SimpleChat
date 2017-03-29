package com.seveneow.simplechat.service;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SaveMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_NOTIFY_CHANGE = "notify_change";
  public static final String PARAM_MESSAGES = "messages";

  public SaveMessageService() {
    super("SaveMessageService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    DebugLog.e("Baaa", "SaveMessageService start");

    if (intent == null)
      return;

    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    ArrayList<Message> messageData = intent.getParcelableArrayListExtra(PARAM_MESSAGES);
    DBHelper helper = DBHelper.getInstance(this);
    helper.insertMessageList(messageData, Static.DB_PASS);
    if (intent.getBooleanExtra(PARAM_NOTIFY_CHANGE, true) && hasNewMessage(roomId, messageData))
      RxEventSender.notifyNewMessageSaved(roomId);
    helper.close();
  }

  //TODO: remove this if need to notify when messages been updated such as "Remove"
  private boolean hasNewMessage(String roomId, ArrayList<Message> messages) {
    ConcurrentHashMap<String, Message> localMessages = RoomManager.getInstance().getRoomById(roomId).getMessages();
    boolean hasNewMessage = false;
    if (messages.size() == 0)
      return true;

    for (Message message : messages) {
      if (localMessages.containsKey(message.getId())) {
        continue;
      }
      hasNewMessage = true;
    }
    return hasNewMessage;
  }
}
