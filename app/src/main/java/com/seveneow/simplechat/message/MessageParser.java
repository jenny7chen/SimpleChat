package com.seveneow.simplechat.message;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Parse Json string to Message object
 */

public class MessageParser {

  public MessageParser() {
  }

  public Message parse(String jsonStr) {
    JsonElement jsonElement = new JsonParser().parse(jsonStr);
    int messageType = getMessageType(jsonElement);
    if (messageType == Message.TYPE_TEXT) {
      TextMessage message = new TextMessage();
      return message;

    }
    else if (messageType == Message.TYPE_IMAGE) {
      ImageMessage message = new ImageMessage();
      return message;

    }
    else if (messageType == Message.TYPE_STICKER) {
      StickerMessage message = new StickerMessage();
      return message;
    }
    return null;
  }

  public Message parse(){
    //parse database cursor to message object
    return new Message(Message.TYPE_TEXT);
  }

  private int getMessageType(JsonElement jsonElement) {
    return Message.TYPE_TEXT;
  }

}
