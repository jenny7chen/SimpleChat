package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.database.DBHelper;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageParser;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEventSender;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.utils.TimeParser;

import java.util.ArrayList;
import java.util.Random;


public class FetchMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";

  public FetchMessageService() {
    super("FetchMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room == null)
      return;

    //check if save to DB is in processing, if it's running, need to wait until data saving finished.
    while (Static.isMyServiceRunning(this, SaveDBRequestService.class)) {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    //get new message list from database
    ArrayList<Message> messages = DBHelper.getInstance(this).getRoomMessages(new MessageParser(this), roomId, Static.DB_PASS);
    if (messages.size() > 0) {
      DebugLog.e("FetchMessageService", "has db message return ");
      RoomManager.getInstance().updateRoomMessages(roomId, messages);
      RxEventSender.notifyRoomMessagesUpdated(roomId);
      RxEventSender.notifyRoomMessagesInited(roomId);
      return;
    }

    FDBManager.initRoomMessages(roomId, this);
  }

  private ImageMessage getTestImageMessage() {
    try {
      Thread.sleep(200);
    }
    catch (InterruptedException e) {
      DebugLog.printStackTrace(e);
    }
    ImageMessage image = new ImageMessage();
    image.setThumbnail(getImageUrl());
    image.setTime(TimeParser.getCurrentTimeString());
    image.setId(image.getTime());
    return image;
  }

  private String getImageUrl() {
    Random r = new Random();
    int i1 = r.nextInt(4);
    String[] urls = {
        "https://images.pexels.com/photos/286426/pexels-photo-286426.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
        "https://images.pexels.com/photos/25585/pexels-photo-25585.jpg?w=1260&h=750&auto=compress&cs=tinysrgb",
        "https://images.pexels.com/photos/247470/pexels-photo-247470.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
        "https://images.pexels.com/photos/250389/pexels-photo-250389.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb",
        "https://images.pexels.com/photos/29682/pexels-photo-29682.jpg?w=1260&h=750&auto=compress&cs=tinysrgb"
    };
    return urls[i1];

  }
}
