package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class RoomManager {
  private static RoomManager roomManager = new RoomManager();
  private static LinkedHashMap<String, Room> roomMap = new LinkedHashMap<>();
  private static ArrayList<Room> showRoomList = new ArrayList<>();

  private RoomManager() {

  }

  public static RoomManager getInstance() {
    return roomManager;
  }

  public void addAllRooms(ArrayList<Room> rooms) {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    for (Room room : rooms) {
      roomMap.put(room.getId(), room);
    }
  }

  public boolean hasRoomData() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    return roomMap.size() > 0;
  }

  public void addOrUpdateRoom(Room room) {
    roomMap.put(room.getId(), room);
    RxEventSender.notifyRoomInserted(room);
  }

  public Room getRoomById(String roomId) {
    return roomMap.get(roomId);
  }

  public ArrayList<Room> getAllRooms() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    if (showRoomList == null)
      showRoomList = new ArrayList<>();
    showRoomList.clear();
    showRoomList.addAll(sort(new ArrayList<>(roomMap.values())));
    return showRoomList;
  }

  public void updateRoomMessages(String roomId, ArrayList<Message> messages) {
    Room room = getRoomById(roomId);
    if (messages != null)
      room.setMessages(messages);
    RxEventSender.notifyRoomMessagesUpdated(roomId);
  }

  public boolean addOrUpdateRoomMessage(String roomId, Message message) {
    Room room = getRoomById(roomId);
    RxEvent event = new RxEvent();
    if (message.getId() != null) {
      if (room.getMessages().containsKey(message.getId())) {
        event.id = RxEvent.EVENT_ROOM_MESSAGE_UPDATED;
        event.object = message;
        room.getMessages().get(message.getId()).updateMessage(message);
      }
      else {
        event.id = RxEvent.EVENT_ROOM_MESSAGE_ADDED;
        event.object = message;
        room.getMessages().put(message.getId(), message);
      }
      event.roomId = roomId;
      RxEventBus.send(event);
      return event.id == RxEvent.EVENT_ROOM_MESSAGE_ADDED;
    }
    return false;
  }

  private ArrayList<Room> sort(ArrayList<Room> roomList) {
    Collections.sort(roomList, new RoomComparator());
    return roomList;
  }

  private class RoomComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
      return o2.getLatestMessageTime().compareTo(o1.getLatestMessageTime());
    }
  }
}
