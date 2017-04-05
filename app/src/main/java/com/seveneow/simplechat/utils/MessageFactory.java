package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.FileMessage;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;


public class MessageFactory {

  public static Message create(String roomId, String messageText, int roomType){
    return create(roomId, messageText, roomType, Message.TYPE_TEXT, null);
  }

  public static Message create(String roomId, int roomType, String thumbnail){
    return create(roomId, null, roomType, Message.TYPE_IMAGE, thumbnail);
  }

  public static Message createTestImage(String roomId, int roomType){
    return create(roomId, null, roomType, Message.TYPE_IMAGE, getImageUrl());
  }

  public static Message create(String roomId, String messageText, int roomType, int messageType, String thumbnail) {
    Message message = new Message();
    if (messageType == Message.TYPE_TEXT) {
      message = new TextMessage();
      ((TextMessage) message).setMessage(messageText);
      try {
        message.setShowText(URLEncoder.encode(messageText, "UTF-8"));
      }
      catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        message.setShowText(messageText);
      }
    }
    else if (messageType == Message.TYPE_IMAGE) {
      message = new ImageMessage();
      ((ImageMessage) message).setThumbnail(thumbnail);
    }
    message.setPending(true);
    message.setTime(String.valueOf(TimeParser.getCurrentTimeString()));
    message.setSenderId(Static.userId);
    message.setRoomId(roomId);
    message.setShowSender(roomType != Info.TYPE_USER);

    return message;
  }

  private static String getImageUrl() {
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
