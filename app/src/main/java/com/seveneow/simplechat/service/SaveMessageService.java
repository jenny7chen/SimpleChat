package com.seveneow.simplechat.service;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

import java.util.ArrayList;

public class SaveMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_MESSAGES = "messages";

  public SaveMessageService() {
    super("SaveMessageService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent == null)
      return;
    DebugLog.e("baa", "save start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    ArrayList<Message> messageData = intent.getParcelableArrayListExtra(PARAM_MESSAGES);
    DBHelper.getInstance(this).insertMessageList(messageData, Static.DB_PASS);
    DebugLog.e("baa", "save end");
    RxEventSender.notifyNewMessageSaved(roomId);
  }
}
