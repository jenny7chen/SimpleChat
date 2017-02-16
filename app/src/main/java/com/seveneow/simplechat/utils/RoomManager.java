package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class RoomManager {
  private static RoomManager roomManager = new RoomManager();
  private LinkedHashMap<String, Room> roomMap = new LinkedHashMap<>();

  private RoomManager() {

  }

  public static RoomManager getInstance() {
    return roomManager;
  }

  public void addTestData() {
    Room room = new Room();
    room.setId("123");
    roomMap.put("123", room);
  }


  public Room getRoomById(String roomId) {
    return roomMap.get(roomId);
  }

  public ArrayList<Room> getAllRooms() {
    ArrayList<Room> roomList = new ArrayList<>(roomMap.values());
    return sort(roomList);
  }

  public void updateRoomMessages(String roomId, ArrayList<Message> messages) {
    Room room = getRoomById(roomId);
    if (messages != null)
      room.setMessages(messages);
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_MESSAGES_UPDATE;
    event.object = roomId;
    RxEventBus.send(event);
  }

  private ArrayList<Room> sort(ArrayList<Room> roomList) {
    Collections.sort(roomList, new RoomComparator());
    return roomList;
  }

  private class RoomComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
      return o1.getLatestMessageTime().compareTo(o2.getLatestMessageTime());
    }
  }
}