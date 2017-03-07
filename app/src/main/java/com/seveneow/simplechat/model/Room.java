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
  private ArrayList<String> members;
  private ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();
  private ArrayList<Message> showMessages = new ArrayList<>();
  private ArrayList<Post> posts = new ArrayList<>();

  public Room() {

  }

  public Room(String name, String photo, ArrayList<String> members, ArrayList<Post> posts) {
    this.name = name;
    this.photo = photo;
    this.members = members;
    this.posts = posts;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLatestMessageTime() {
    Message message = messages.get(0);
    return message.getMessageTime();
  }

  public ConcurrentHashMap<String, Message> getMessages() {
    return messages == null ? new ConcurrentHashMap<>() : messages;
  }

  public ArrayList<Message> getShowMessages() {
    showMessages.clear();
    showMessages.addAll(sortMessages(new ArrayList<>(getMessages().values())));
    return showMessages;
  }

  public void setMessages(ArrayList<Message> messageList) {
    Iterator iterator = messageList.iterator();
    while (iterator.hasNext()) {
      Message message = (Message) iterator.next();
      this.getMessages().put(message.getMessageId(), message);
    }
  }

  public boolean hasMembers() {
    return !(members == null || members.isEmpty());
  }

  private ArrayList<Message> sortMessages(ArrayList<Message> roomList) {
    Collections.sort(roomList, new Message.MessageComparator());
    return roomList;
  }


  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getPhotoMd5() {
    return photoMd5;
  }

  public void setPhotoMd5(String photoMd5) {
    this.photoMd5 = photoMd5;
  }

  public ArrayList<String> getMembers() {
    return members;
  }

  public void setMembers(ArrayList<String> members) {
    this.members = members;
  }

  public void setMessages(ConcurrentHashMap<String, Message> messages) {
    this.messages = messages;
  }

  public void setShowMessages(ArrayList<Message> showMessages) {
    this.showMessages = showMessages;
  }

}
