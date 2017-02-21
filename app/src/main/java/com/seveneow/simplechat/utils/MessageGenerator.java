package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;


public class MessageGenerator {
  public static Message getPendingTextMessage(String message) {
    TextMessage text = new TextMessage();
    text.setMessage(message);
    text.setPending(true);
    text.setMessageTime(String.valueOf(TimeParser.getCurrentTimeString()));
    text.setPendingId(text.getMessageTime());
    text.setMessageId(text.getMessageTime());
    return text;
  }
}
