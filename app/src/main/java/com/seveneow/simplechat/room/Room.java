package com.seveneow.simplechat.room;

import com.seveneow.simplechat.message.Message;

import java.util.ArrayList;


public class Room {
  public static final int TYPE_PUBLIC = 0;
  public static final int TYPE_PRIVATE = 1;
  public static final int TYPE_GROUP = 2;

  //not in use
  private static final int TYPE_GUILD = 3;

  private int type = TYPE_PUBLIC;
  private String id;
  private String name;
  private String description;
  private String photo;
  private String photoMd5;
  private ArrayList<String> memberIds;
  private ArrayList<Message> messages;
}
