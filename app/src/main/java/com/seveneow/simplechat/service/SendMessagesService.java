package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.Static;

/**
 * Created by jennychen on 2017/2/16.
 * save pending message to database and send message.
 */

public class SendMessagesService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_MESSAGE = "message";

  public SendMessagesService() {
    super("SendMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "SendMessagesService start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Message message = intent.getParcelableExtra(PARAM_MESSAGE);
    DBHelper helper = DBHelper.getInstance(this);

    long insertKey = helper.insertMessage(message, Static.DB_PASS);
    message.setDatabaseId(insertKey);

    RoomManager.getInstance().addOrUpdateRoomMessage(roomId, message);
    FDBManager.sendMessage(message.getId(), roomId, message, this);
  }
}
