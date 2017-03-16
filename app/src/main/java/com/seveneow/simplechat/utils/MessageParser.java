package com.seveneow.simplechat.utils;


import com.google.firebase.database.DataSnapshot;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Parse Json string to Message object
 */

public class MessageParser {
  BasePresenter presenter;

  public MessageParser(BasePresenter presenter) {
    this.presenter = presenter;
  }

  public Message parse(String jsonStr) {
    JsonElement jsonElement = new JsonParser().parse(jsonStr);
    if (!jsonElement.isJsonObject())
      return null;

    JsonObject messageObject = jsonElement.getAsJsonObject();
    int messageType = getMessageType(messageObject);
    String messageSenderId = messageObject.get("message_sender_id").getAsString();

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
      message.setId(messageObject.get("message_id").getAsString());
      message.setTime(messageObject.get("message_time").getAsString());
      message.setPendingId(messageObject.get("message_temp_id").getAsString());
      message.setSenderId(messageSenderId);
      return message;

    }
    else if (messageType == Message.TYPE_IMAGE) {
      ImageMessage message = new ImageMessage();
      message.setThumbnail(messageObject.get("message").getAsString());
      message.setId(messageObject.get("message_id").getAsString());
      message.setPendingId(messageObject.get("message_temp_id").getAsString());
      message.setTime(messageObject.get("message_time").getAsString());
      message.setSenderId(messageSenderId);
      return message;

    }
    else if (messageType == Message.TYPE_STICKER) {
      StickerMessage message = new StickerMessage();
      return message;
    }
    return null;
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
    User sender = UserManager.getInstance().getUser(senderId);

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
      message.setShowText(messageText);

    }
    else if (messageType == Message.TYPE_IMAGE) {
      message = new ImageMessage();
      ((ImageMessage) message).setThumbnail((String) messageSnapShot.child("thumbnail").getValue());
      message.setShowText(presenter.getString(R.string.message_show_text_image, sender.getName()));

    }
    else if (messageType == Message.TYPE_STICKER) {
      message = new StickerMessage();
      message.setShowText(presenter.getString(R.string.message_show_text_sticker, sender.getName()));

      return message;
    }

    message.setId(messageSnapShot.getKey());
    message.setType(messageType);
    message.setSenderId(senderId);
    message.setTime((String.valueOf((long) messageSnapShot.child("timestamp").getValue())));

    if (messageSnapShot.hasChild("isPending"))
      message.setPending((boolean) messageSnapShot.child("isPending").getValue());

    if (messageSnapShot.hasChild("pendingId"))
      message.setPendingId((String) messageSnapShot.child("pendingId").getValue());
    return message;
  }
}
