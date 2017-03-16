package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.FileMessage;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;


public class MessageGenerator {
  public static Message getPendingTextMessage(String message) {
    TextMessage text = new TextMessage();
    text.setMessage(message);
    text.setPending(true);
    text.setTime(String.valueOf(TimeParser.getCurrentTimeString()));
    text.setPendingId(text.getTime());
    text.setSenderId(Static.userId);
    return text;
  }

  public static Message copyMessage(Message message) {
    if (message.getType() == Message.TYPE_TEXT) {
      return new TextMessage(message);
    }
    else if (message.getType() == Message.TYPE_IMAGE) {
      return new ImageMessage(message);
    }
    else if (message.getType() == Message.TYPE_STICKER) {
      return new StickerMessage(message);
    }
    else if (message.getType() == Message.TYPE_FILE) {
      return new FileMessage(message);
    }
    return new Message(message);
  }
}
