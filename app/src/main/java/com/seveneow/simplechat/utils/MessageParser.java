package com.seveneow.simplechat.utils;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;

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
    if (messageSenderId.equals("456")) {
      messageSenderId = "";
    }
    if (messageType == Message.TYPE_TEXT) {
      TextMessage message = new TextMessage();
      message.setMessage(messageObject.get("message").getAsString());
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

  public Message parse() {
    //parse database cursor to message object
    return new Message(Message.TYPE_TEXT);
  }

  private int getMessageType(JsonObject jsonObject) {
    return jsonObject.get("message_type").getAsInt();
  }

}
