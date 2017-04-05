package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class RoomManager {
  private static RoomManager roomManager = new RoomManager();
  private static LinkedHashMap<String, Info> roomMap = new LinkedHashMap<>();
  private static ArrayList<Info> showInfoList = new ArrayList<>();

  private RoomManager() {

  }

  public static RoomManager getInstance() {
    return roomManager;
  }

  public void addAllRooms(ArrayList<Info> infos) {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    for (Info info : infos) {
      roomMap.put(info.getId(), info);
    }
  }

  public boolean hasRoomData() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    return roomMap.size() > 0;
  }

  public void addOrUpdateRoom(Info info) {
    roomMap.put(info.getId(), info);
    RxEventSender.notifyRoomInserted(info);
  }

  public Info getRoomById(String roomId) {
    return roomMap.get(roomId);
  }

  public ArrayList<Info> getAllRooms() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    if (showInfoList == null)
      showInfoList = new ArrayList<>();
    showInfoList.clear();
    showInfoList.addAll(sort(new ArrayList<>(roomMap.values())));
    return showInfoList;
  }

  public void updateRoomMessages(String roomId, ArrayList<Message> messages) {
    Info info = getRoomById(roomId);
    if (messages != null)
      info.setMessages(messages);
    RxEventSender.notifyRoomMessagesUpdated(roomId);
  }

  public boolean addOrUpdateRoomMessage(String roomId, Message message) {
    Info info = getRoomById(roomId);
    RxEvent event = new RxEvent();
    if (message.getId() != null) {
      if (info.getMessages().containsKey(message.getId())) {
        event.id = RxEvent.EVENT_ROOM_MESSAGE_UPDATED;
        event.object = message;
        info.getMessages().get(message.getId()).updateMessage(message);
      }
      else {
        event.id = RxEvent.EVENT_ROOM_MESSAGE_ADDED;
        event.object = message;
        info.getMessages().put(message.getId(), message);
      }
      event.roomId = roomId;
      RxEventBus.send(event);
      return event.id == RxEvent.EVENT_ROOM_MESSAGE_ADDED;
    }
    return false;
  }

  private ArrayList<Info> sort(ArrayList<Info> infoList) {
    Collections.sort(infoList, new RoomComparator());
    return infoList;
  }

  private class RoomComparator implements Comparator<Info> {
    @Override
    public int compare(Info o1, Info o2) {
      return o2.getLatestMessageTime().compareTo(o1.getLatestMessageTime());
    }
  }
}
