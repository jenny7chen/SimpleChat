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
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.MessageGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by jennychen on 2017/2/16.
 * <p>
 * TODO: Test use, remove this after using socket connection
 */

public class SendMessagesService extends IntentService {
  public static final String PARAM_SENDER_ID = "sender_id";
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_MESSAGE = "message";
  public static final String PARAM_MESSAGE_TYPE = "message_type";
  public static final String PARAM_MESSAGE_TEMP_ID = "message_temp_id";
  public static final String PARAM_MESSAGE_TIME = "message_time";

  public SendMessagesService() {
    super("SendMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String senderId = intent.getStringExtra(PARAM_SENDER_ID);
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    String message = intent.getStringExtra(PARAM_MESSAGE);
    String messageTempId = intent.getStringExtra(PARAM_MESSAGE_TEMP_ID);
    String messageTime = intent.getStringExtra(PARAM_MESSAGE_TIME);
    int messageType = intent.getIntExtra(PARAM_MESSAGE_TYPE, Message.TYPE_TEXT);
    JsonObject sendObject = getSendObject(senderId, message, messageTempId, messageTime, messageType);


  }

  public static Intent generateDataIntent(String senderId, TextMessage message) {
    Intent intent = new Intent();
    intent.putExtra(SendMessagesService.PARAM_SENDER_ID, senderId);
    intent.putExtra(SendMessagesService.PARAM_MESSAGE, message.getMessage());
    intent.putExtra(SendMessagesService.PARAM_MESSAGE_TEMP_ID, message.getPendingId());
    intent.putExtra(SendMessagesService.PARAM_MESSAGE_TIME, message.getMessageTime());
    return intent;
  }

  public static JsonObject getSendObject(String senderId, String message, String messageTempId, String messageTime, int messageType) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("to", "/topics/123");
    JsonObject messageObj = new JsonObject();
    String encodedString = message;
    try {
      encodedString = URLEncoder.encode(message, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      DebugLog.printStackTrace(e);
    }
    messageObj.addProperty("message", encodedString);
    messageObj.addProperty("message_type", messageType);
    messageObj.addProperty("message_id", messageTime);
    messageObj.addProperty("message_time", messageTime);
    messageObj.addProperty("message_sender_id", senderId);
    messageObj.addProperty("message_temp_id", messageTempId);
    JsonObject object = new JsonObject();
    object.add("message", messageObj);
    jsonObject.add("data", object);
    return jsonObject;
  }
}
