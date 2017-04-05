package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.Static;


public class GetServerMessageListService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";

  public GetServerMessageListService() {
    super("GetServerMessageListService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "GetServerMessageListService start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Info info = RoomManager.getInstance().getRoomById(roomId);
    if (info == null)
      return;

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveMessageService.class) || Static.isMyServiceRunning(this, GetDBMessageListService.class)) {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    FDBManager.getServerMessages(roomId, this);
  }
}
