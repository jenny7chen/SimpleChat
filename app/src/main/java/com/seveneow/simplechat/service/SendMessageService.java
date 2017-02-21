package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.seveneow.simplechat.BuildConfig;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.DebugLog;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by jennychen on 2017/2/16.
 * <p>
 * TODO: Test use, remove this after using socket connection
 */

public class SendMessageService extends IntentService {
  public static final String PARAM_SENDER_ID = "sender_id";
  public static final String PARAM_MESSAGE = "message";
  public static final String PARAM_MESSAGE_TYPE = "message_type";
  public static final String PARAM_MESSAGE_TEMP_ID = "message_temp_id";
  public static final String PARAM_MESSAGE_TIME = "message_time";

  public SendMessageService() {
    super("SendMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String senderId = intent.getStringExtra(PARAM_SENDER_ID);
    String message = intent.getStringExtra(PARAM_MESSAGE);
    String messageTempId = intent.getStringExtra(PARAM_MESSAGE_TEMP_ID);
    String messageTime = intent.getStringExtra(PARAM_MESSAGE_TIME);
    int messageType = intent.getIntExtra(PARAM_MESSAGE_TYPE, Message.TYPE_TEXT);
    JsonObject sendObject = getSendObject(senderId, message, messageTempId, messageTime, messageType);

    SyncHttpClient client = new SyncHttpClient();
    client.setEnableRedirects(true);

    if (BuildConfig.DEBUG) {
      client.setLoggingLevel(Log.ERROR);
    }
    else {
      client.setLoggingEnabled(false);
    }
    Header header = new BasicHeader("Authorization", "key=AAAAITet3RY:APA91bF1EOwJlTQ88tQCog4Z2ARSRc9aTR4JwNzGHn7Zt4zqkf097rICiWoTPK_nzneoTO4yb018grE2diFydA5BsR8TXIXoGH4H649MWGUJYlxLwS5x8sAdcvZnOkWbYvx477GvSspC");
    Header[] headers = {header};
    AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Log.e("baaa", "meesga send");

      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        Log.e("baaa", "onFailure statuscode = " + statusCode);

      }
    };

    StringEntity entity = null;
    try {
      entity = new StringEntity(sendObject.toString());
    }
    catch (UnsupportedEncodingException e) {
      DebugLog.printStackTrace(e);
    }
    client.post(this, "https://fcm.googleapis.com/fcm/send", headers, entity, "application/json", responseHandler);
  }

  public static Intent generateDataIntent(TextMessage message) {
    Intent intent = new Intent();
    intent.putExtra(SendMessageService.PARAM_SENDER_ID, "456");
    intent.putExtra(SendMessageService.PARAM_MESSAGE, message.getMessage());
    intent.putExtra(SendMessageService.PARAM_MESSAGE_TEMP_ID, message.getPendingId());
    intent.putExtra(SendMessageService.PARAM_MESSAGE_TIME, message.getMessageTime());
    return intent;
  }

  public static JsonObject getSendObject(String senderId, String message, String messageTempId, String messageTime, int messageType) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("to", "/topics/123");
    JsonObject messageObj = new JsonObject();
    messageObj.addProperty("message", message);
    messageObj.addProperty("message_type", messageType);
    messageObj.addProperty("message_id", messageTime);
    messageObj.addProperty("message_time", messageTime);
    messageObj.addProperty("message_sender_id", "456");
    messageObj.addProperty("message_temp_id", messageTempId);
    JsonObject object = new JsonObject();
    object.addProperty("message", messageObj.toString());
    jsonObject.add("data", object);
    jsonObject.addProperty("from", senderId);
    return jsonObject;
  }
}
