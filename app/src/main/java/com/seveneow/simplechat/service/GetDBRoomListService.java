package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RoomParser;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;

import java.util.ArrayList;


public class GetDBRoomListService extends IntentService {
  public static final String PARAM_NOTIFY_INIT = "notify_init";

  public GetDBRoomListService() {
    super("GetDBRoomListService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    DebugLog.e("Baaa", "GetDBRoomListService start");

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveRoomService.class)) {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    //get new message list from database
    DBHelper helper = DBHelper.getInstance(this);
    ArrayList<Room> rooms = helper.getUserRoomList(new RoomParser(), Static.DB_PASS);
    if (rooms.size() > 0) {
      DebugLog.e("GetDBRoomListService", "has db rooms return, room list size = " + rooms.size());
      RoomManager.getInstance().addAllRooms(rooms);
      RxEventSender.notifyRoomListUpdated();
      RxEventSender.notifyRoomListInited();
      return;
    }

    RxEventSender.notifyRoomListInited();
  }

}
