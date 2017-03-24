package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.Static;


public class UpdateMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";

  public UpdateMessageService() {
    super("UpdateMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "UpdateMessageService start");
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room == null)
      return;

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveDBRequestService.class) || Static.isMyServiceRunning(this, FetchLocalMessagesService.class)) {
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
