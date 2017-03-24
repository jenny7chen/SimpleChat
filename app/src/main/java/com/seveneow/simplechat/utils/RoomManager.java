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

  private RoomManager() {

  }

  public static RoomManager getInstance() {
    return roomManager;
  }

  public void addAllRooms(ArrayList<Room> rooms) {
    for (Room room : rooms) {
      roomMap.put(room.getId(), room);
    }
  }

  public boolean hasRoomData() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    return roomMap.size() > 0;
  }

  public void addRoom(Room room) {
    roomMap.put(room.getId(), room);
    RxEventSender.notifyRoomListUpdated(room);
  }

  public Room getRoomById(String roomId) {
    return roomMap.get(roomId);
  }

  public ArrayList<Room> getAllRooms() {
    if (roomMap == null)
      roomMap = new LinkedHashMap<>();
    ArrayList<Room> roomList = new ArrayList<>(roomMap.values());
    return sort(roomList);
  }

  public void updateRoomMessages(String roomId, ArrayList<Message> messages) {
    Room room = getRoomById(roomId);
    if (messages != null)
      room.setMessages(messages);
  }

  public boolean addOrUpdateMessage(String roomId, Message message) {
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
    }

    event.params = new String[]{roomId};
    RxEventBus.send(event);
    return event.id == RxEvent.EVENT_ROOM_MESSAGE_ADDED;
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
