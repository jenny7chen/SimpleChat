package com.seveneow.simplechat.model;

import java.util.ArrayList;


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
  private ArrayList<Message> messages;

  public void setId(String id){
    this.id = id;
  }

  public String getLatestMessageTime() {
    Message message = messages.get(0);
    return message.getMessageTime();
  }

  public ArrayList<Message> getMessages() {
    return messages == null ? new ArrayList<Message>() : messages;
  }

  public void setMessages(ArrayList<Message> messages) {
    this.messages = messages;
  }

  public boolean hasMembers() {
    return !(memberIds == null || memberIds.isEmpty());
  }
}
