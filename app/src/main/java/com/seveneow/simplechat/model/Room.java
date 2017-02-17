package com.seveneow.simplechat.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;


public class Room {
  public static final int TYPE_GROUP = 1;
  public static final int TYPE_BOARD = 2;
  public static final int TYPE_USER = 3;

  //not in use
  private static final int TYPE_GUILD = 3;

  private int type = TYPE_GROUP;
  private String id;
  private String name;
  private String description;
  private String photo;
  private String photoMd5;
  private ArrayList<String> memberIds;
  private ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();

  public void setId(String id) {
    this.id = id;
  }

  public String getLatestMessageTime() {
    Message message = messages.get(0);
    return message.getMessageTime();
  }

  public ConcurrentHashMap<String, Message> getMessages() {
    return messages == null ? new ConcurrentHashMap<String, Message>() : messages;
  }

  public ArrayList<Message> getShowMessages() {
    return sortMessages(new ArrayList<>(getMessages().values()));
  }

  public void setMessages(ArrayList<Message> messageList) {
    Iterator iterator = messageList.iterator();
    while(iterator.hasNext()){
      Message message = (Message) iterator.next();
      this.getMessages().put(message.getMessageId(), message);
    }
  }

  public boolean hasMembers() {
    return !(memberIds == null || memberIds.isEmpty());
  }

  private ArrayList<Message> sortMessages(ArrayList<Message> roomList) {
    Collections.sort(roomList, new Message.MessageComparator());
    return roomList;
  }

}
