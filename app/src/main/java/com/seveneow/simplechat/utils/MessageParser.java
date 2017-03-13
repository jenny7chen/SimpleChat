package com.seveneow.simplechat.utils;


import com.google.firebase.database.DataSnapshot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Parse Json string to Message object
 */

public class MessageParser {

  public MessageParser() {
  }

  public Message parse(String jsonStr) {
    JsonElement jsonElement = new JsonParser().parse(jsonStr);
    if (!jsonElement.isJsonObject())
      return null;

    JsonObject messageObject = jsonElement.getAsJsonObject();
    int messageType = getMessageType(messageObject);
    String messageSenderId = messageObject.get("message_sender_id").getAsString();

    //TODO:Test data, if sender id is from myself
    if (messageSenderId.equals(Static.userId)) {
      messageSenderId = "";
    }
    if (messageType == Message.TYPE_TEXT) {
      TextMessage message = new TextMessage();
      String messageText = messageObject.get("message").getAsString();
      try {
        messageText = URLDecoder.decode(messageText, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        DebugLog.printStackTrace(e);
      }
      message.setMessage(messageText);
      message.setMessageId(messageObject.get("message_id").getAsString());
      message.setMessageTime(messageObject.get("message_time").getAsString());
      message.setPendingId(messageObject.get("message_temp_id").getAsString());
      message.setSenderId(messageSenderId);
      return message;

    }
    else if (messageType == Message.TYPE_IMAGE) {
      ImageMessage message = new ImageMessage();
      message.setThumbnail(messageObject.get("message").getAsString());
      message.setMessageId(messageObject.get("message_id").getAsString());
      message.setPendingId(messageObject.get("message_temp_id").getAsString());
      message.setMessageTime(messageObject.get("message_time").getAsString());
      message.setSenderId(messageSenderId);
      return message;

    }
    else if (messageType == Message.TYPE_STICKER) {
      StickerMessage message = new StickerMessage();
      return message;
    }
    return null;
  }

  public Message parse(Message message) {
    //TODO:Test data, if sender id is from myself
    if (message.getSenderId().equals(Static.userId)) {
      message.setSenderId("");
    }
    return message;
  }

  private int getMessageType(JsonObject jsonObject) {
    return jsonObject.get("message_type").getAsInt();
  }

  private int getMessageType(DataSnapshot messageSnapShot) {
    return (int) ((long) messageSnapShot.child("type").getValue());
  }

  public Message parse(DataSnapshot messageSnapShot) {
    if (messageSnapShot == null)
      return null;

    int messageType = getMessageType(messageSnapShot);
    String senderId = (String) messageSnapShot.child("sender_id").getValue();

    if (senderId == null || Static.userId.equals(senderId)) {
      senderId = "";
    }

    Message message = null;

    if (messageType == Message.TYPE_TEXT) {
      message = new TextMessage();

      String messageText = (String) messageSnapShot.child("message").getValue();
      try {
        messageText = URLDecoder.decode(messageText, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        DebugLog.printStackTrace(e);
      }
      ((TextMessage) message).setMessage(messageText);

    }
    else if (messageType == Message.TYPE_IMAGE) {
      message = new ImageMessage();
      ((ImageMessage) message).setThumbnail((String) messageSnapShot.child("thumbnail").getValue());

    }
    else if (messageType == Message.TYPE_STICKER) {
      message = new StickerMessage();
      return message;
    }

    message.setMessageId(messageSnapShot.getKey());
    message.setType(messageType);
    message.setSenderId(senderId);
    message.setReceiverId((String) messageSnapShot.child("receiver_id").getValue());
    message.setMessageTime((String.valueOf((long) messageSnapShot.child("timestamp").getValue())));

    if (messageSnapShot.hasChild("isPending"))
      message.setPending((boolean) messageSnapShot.child("isPending").getValue());

    if (messageSnapShot.hasChild("pendingId"))
      message.setPendingId((String) messageSnapShot.child("pendingId").getValue());
    return message;
  }
}
