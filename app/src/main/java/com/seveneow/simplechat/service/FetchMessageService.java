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

//  private void getMessagesFromServer(String roomId) {
//    FDBManager.initRoomMessages(roomId);
//
//    String url = "https://" + Static.FIREBASE_PROJECT_ID + ".firebaseio-demo.com/messages/" + roomId + ".json";
//    SyncHttpClient client = new SyncHttpClient();
//    client.setEnableRedirects(true);
//
//    if (BuildConfig.DEBUG) {
//      client.setLoggingLevel(Log.ERROR);
//    }
//    else {
//      client.setLoggingEnabled(false);
//    }
//    Header header = new BasicHeader("Authorization", "key=AAAAITet3RY:APA91bF1EOwJlTQ88tQCog4Z2ARSRc9aTR4JwNzGHn7Zt4zqkf097rICiWoTPK_nzneoTO4yb018grE2diFydA5BsR8TXIXoGH4H649MWGUJYlxLwS5x8sAdcvZnOkWbYvx477GvSspC");
//    Header[] headers = {header};
//    RequestParams requestParams = new RequestParams();
//    requestParams.setContentEncoding("UTF-8");
//    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
//      @Override
//      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//        JsonElement jsonElement = new JsonParser().parse(new String(responseBody));
//        DebugLog.e("baaa", "result = " + jsonElement.toString());
//      }
//
//      @Override
//      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//        Log.e("baaa", "onFailure status code = " + statusCode);
//      }
//    };
//    client.get(this, url, requestParams, responseHandler);
//  }


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
