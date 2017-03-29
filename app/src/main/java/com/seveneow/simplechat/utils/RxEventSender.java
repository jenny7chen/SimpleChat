package com.seveneow.simplechat.utils;


import com.seveneow.simplechat.model.Room;

public class RxEventSender {
  public static void notifyRoomMessagesUpdated(String roomId){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_MESSAGE_LIST_UPDATED;
    event.object = roomId;
    RxEventBus.send(event);
  }

  public static void notifyRoomMessagesInited(String roomId){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_MESSAGE_INIT;
    event.object = roomId;
    RxEventBus.send(event);
  }

  public static void notifyRoomListUpdated(){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_LIST_UPDATE;
    RxEventBus.send(event);
  }

  public static void notifyRoomInserted(Room room){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_INSERTED;
    event.object = room;
    RxEventBus.send(event);
  }

  public static void notifyRoomListInited(){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_LIST_INITED;
    RxEventBus.send(event);
  }

  public static void notifyRoomShowTextUpdate(){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_SHOWTEXT_UPDATE;
    RxEventBus.send(event);
  }

  public static void notifyNewMessageSaved(String roomId){
    RxEvent event = new RxEvent();
    event.id = RxEvent.EVENT_ROOM_MESSAGE_SAVED;
    event.object = roomId;
    RxEventBus.send(event);
  }

}
