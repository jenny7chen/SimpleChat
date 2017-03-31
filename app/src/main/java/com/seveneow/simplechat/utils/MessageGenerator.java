package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.FileMessage;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;


public class MessageGenerator {
  public static Message getPendingTextMessage(String roomId, String message, int roomType) {
    TextMessage text = new TextMessage();
    text.setMessage(message);
    text.setPending(true);
    text.setTime(String.valueOf(TimeParser.getCurrentTimeString()));
    text.setSenderId(Static.userId);
    text.setRoomId(roomId);
    text.setShowSender(roomType != Room.TYPE_USER);
    try {
      text.setShowText(URLEncoder.encode(message, "UTF-8"));
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      text.setShowText(message);
    }
    return text;
  }

  public static Message getTestImageMessage(BasePresenter presenter, String roomId, int roomType) {
    ImageMessage image = new ImageMessage();
    image.setThumbnail(getImageUrl());
    image.setTime(String.valueOf(TimeParser.getCurrentTimeString()));
    image.setSenderId(Static.userId);
    image.setId(image.getTime());
    image.setRoomId(roomId);
    image.setPending(true);
    image.setShowSender(roomType != Room.TYPE_USER);

    String showText = presenter.getString(R.string.message_show_text_image, UserManager.getInstance().getUser(Static.userId).getName());
    try {
      image.setShowText(URLEncoder.encode(showText, "UTF-8"));
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      image.setShowText("");
    }
    return image;
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
