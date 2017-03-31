package com.seveneow.simplechat.service;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

public class SaveRoomService extends IntentService {
  public static final String PARAM_ROOMS = "room";

  public SaveRoomService() {
    super("SaveRoomService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    DebugLog.e("Baaa", "SaveRoomService start");

    if (intent == null)
      return;

    Room roomData = intent.getParcelableExtra(PARAM_ROOMS);
    DBHelper helper = DBHelper.getInstance(this);
    long insertId = helper.insertRoom(roomData, Static.DB_PASS);
    RoomManager.getInstance().addOrUpdateRoom(roomData);
    helper.close();
  }
}
