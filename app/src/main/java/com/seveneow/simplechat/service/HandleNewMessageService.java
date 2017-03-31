package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEventSender;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;


public class HandleNewMessageService extends IntentService {
  public static final String PARAM_ROOM_ID = "room_id";
  public static final String PARAM_MESSAGE = "message";

  public HandleNewMessageService() {
    super("HandleNewMessageService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent == null)
      return;

    String roomId = intent.getStringExtra(PARAM_ROOM_ID);
    Message message = intent.getParcelableExtra(PARAM_MESSAGE);
    ArrayList<Message> messageToBeSaved = new ArrayList<Message>();
    messageToBeSaved.add(message);

    Room room = RoomManager.getInstance().getRoomById(roomId);
    boolean isAdd = !room.getMessages().containsKey(message.getId());

    Intent saveIntent = new Intent(this, SaveMessageService.class);
    saveIntent.putExtra(SaveMessageService.PARAM_ROOM_ID, roomId);
    saveIntent.putExtra(SaveMessageService.PARAM_NOTIFY_ADD, true);
    saveIntent.putExtra(SaveMessageService.PARAM_NOTIFY_LIST_CHANGE, false);
    saveIntent.putExtra(SaveMessageService.PARAM_MESSAGES, messageToBeSaved);
    startService(saveIntent);

    //TODO: need to update room DB data, need to adjust image show text
    String text = message.getShowText();
    try {
      text = URLDecoder.decode(text, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    room.setLatestMessageShowText(text);
    room.setLatestMessageShowTime(message.getShowTime());
    RxEventSender.notifyRoomShowTextUpdate();
  }
}
