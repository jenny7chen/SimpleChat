package com.seveneow.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Info implements Parcelable{
  public static final int TYPE_NONE = 0;
  public static final int TYPE_GROUP = 1;
  public static final int TYPE_BOARD = 2;
  public static final int TYPE_USER = 3;

  //not in use
  private static final int TYPE_GUILD = 3;

  private int type = TYPE_GROUP;
  private String id;
  private String name;
  private String photo;
  private ArrayList<String> members;
  private ConcurrentHashMap<String, Message> messages = new ConcurrentHashMap<>();
  private ArrayList<Message> showMessages = new ArrayList<>();
  private ArrayList<Post> posts = new ArrayList<>();
  private String latestMessageShowText = "";
  private String latestMessageShowTime = "";
  private boolean isFavorite;
  private boolean hasChat;

  public Info() {

  }

  public Info(String name, String photo, ArrayList<String> members, ArrayList<Post> posts) {
    this.name = name;
    this.photo = photo;
    this.members = members;
    this.posts = posts;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLatestMessageTime() {
    if (getShowMessages().size() == 0)
      return "0";
    Message message = getShowMessages().get(0);
    if (message == null)
      return "0";
    return message.getTime();
  }

  public String getLatestMessageShowTime() {
    return latestMessageShowTime;
  }

  public String getLatestMessageShowText() {
    return latestMessageShowText;
  }


  public void setLatestMessageShowText(String latestMessageShowText) {
    this.latestMessageShowText = latestMessageShowText;
  }

  public void setLatestMessageShowTime(String latestMessageShowTime) {
    this.latestMessageShowTime = latestMessageShowTime;
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
      this.getMessages().put(message.getId(), message);
    }
  }

  public boolean hasMembers() {
    return !(members == null || members.isEmpty());
  }

  public boolean hasMessages() {
    return !(messages == null || messages.isEmpty());
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

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
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

  public boolean isFavorite() {
    return isFavorite;
  }

  public void setFavorite(boolean favorite) {
    isFavorite = favorite;
  }

  public boolean hasChat() {
    return hasChat;
  }

  public void setHasChat(boolean hasChat) {
    this.hasChat = hasChat;
  }

  public Info(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.type = in.readInt();
    this.photo = in.readString();
    if(this.members == null)
      this.members = new ArrayList<>();
    in.readStringList(this.members);
    this.latestMessageShowText = in.readString();
    this.latestMessageShowTime = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeInt(this.type);
    dest.writeString(this.photo);
    dest.writeStringList(this.members);
    dest.writeString(this.latestMessageShowText);
    dest.writeString(this.latestMessageShowTime);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Info createFromParcel(Parcel in) {
      return new Info(in);
    }

    public Info[] newArray(int size) {
      return new Info[size];
    }
  };

  @CallSuper
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("type", type);
    result.put("name", name);
    result.put("photo", photo);
    HashMap<String, Boolean> memberMap = new HashMap<String, Boolean>();
    if (members != null)
      for (String memberId : members) {
        memberMap.put(memberId, true);
      }
    HashMap<String, Boolean> postMap = new HashMap<String, Boolean>();
    if (posts != null)
      for (Post post : posts) {
        postMap.put(post.getId(), true);
      }
    result.put("members", memberMap);
    result.put("posts", postMap);
    return result;
  }
}
