package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;

import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.StickerMessage;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.utils.RxEventBus;
import com.seveneow.simplechat.utils.TimeParser;

import java.util.ArrayList;
import java.util.Random;


public class FetchMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";

  public FetchMessageService() {
    super("FetchMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //do queries here
    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Room room = RoomManager.getInstance().getRoomById(roomId);
    if (room == null)
      return;

    if (room.getMessages().size() > 0) {
      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_ROOM_MESSAGES_UPDATED;
      event.object = roomId;
      RxEventBus.send(event);
      return;
    }

    //get new message list from database or server
    try {
      Thread.sleep(800);
      //test data onSuccess
      ArrayList<Message> messageList = new ArrayList<Message>();
      TextMessage text = new TextMessage();
      text.setMessage("Things base and vile, holding no quantity, love can transpose to from and dignity: love looks not with the eyes, but with mind. (A Midsummer Night’s Dream 1.1)\n" +
          "卑賤和劣行在愛情看來都不算數，都可以被轉化成美滿和莊嚴：愛情不用眼睛辨別，而是用心靈來判斷/愛用的不是眼睛，而是心。——《仲夏夜之夢》\n" +
          "Lord, what fools these mortals be! (A Midsummer Night’s Dream 3.2)\n" +
          "上帝呀，這些凡人怎麼都是十足的傻瓜！——《仲夏夜之夢》\n" +
          "The lunatic, the lover and the poet are of imagination all compact. (A Midsummer Night’s Dream 5.1)\n" +
          "瘋子、情人、詩人都是想像的產兒。——《仲夏夜之夢》");
      text.setMessage("hello1");
      text.setTime(TimeParser.getCurrentTimeString());
      text.setId(text.getTime());

      Thread.sleep(10);
      TextMessage text2 = new TextMessage();
      text2.setMessage("Hello");
      text2.setSenderId("haha");
      text2.setTime(TimeParser.getCurrentTimeString());
      text2.setId(text2.getTime());
      Thread.sleep(10);

      StickerMessage sticker = new StickerMessage();
      sticker.setTime(TimeParser.getCurrentTimeString());
      sticker.setId(sticker.getTime());
      Thread.sleep(10);

      messageList.add(text);
      messageList.add(text2);
      messageList.add(sticker);
      messageList.add(getTestImageMessage());
      messageList.add(getTestImageMessage());
      RoomManager.getInstance().updateRoomMessages(roomId, messageList);
    }
    catch (InterruptedException e) {

    }

  }

  private ImageMessage getTestImageMessage() {
    try {
      Thread.sleep(200);
    }
    catch (InterruptedException e) {
      DebugLog.printStackTrace(e);
    }
    ImageMessage image = new ImageMessage();
    image.setThumbnail(getImageUrl());
    image.setTime(TimeParser.getCurrentTimeString());
    image.setId(image.getTime());
    return image;
  }

  private String getImageUrl() {
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
}
