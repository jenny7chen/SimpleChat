package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.Static;


public class GetServerRoomListService extends IntentService {
  public static final String PARAM_USER_ID = "user_id";

  public GetServerRoomListService() {
    super("GetServerRoomListService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "GetServerRoomListService start");
    String userId = intent.getStringExtra(PARAM_USER_ID);

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveRoomService.class) || Static.isMyServiceRunning(this, GetDBMessageListService.class)) {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    FDBManager.getServerRoomList(userId, this);
  }
}
