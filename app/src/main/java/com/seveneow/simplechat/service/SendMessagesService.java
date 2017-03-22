package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by jennychen on 2017/2/16.
 * <p>
 * TODO: Test use, remove this after using socket connection
 */

public class SendMessagesService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_MESSAGE = "message";

  public SendMessagesService() {
    super("SendMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Message message = intent.getParcelableExtra(PARAM_MESSAGE);
    long insertKey = DBHelper.getInstance(this).insertMessage(message, Static.DB_PASS);
    RxEventSender.notifyNewMessageSaved(roomId);
    message.setDatabaseId(insertKey);
    FDBManager.sendMessage(FDBManager.getMessagePushKey(roomId), roomId, message, this);
  }
}
