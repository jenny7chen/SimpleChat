package com.seveneow.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;

import com.seveneow.simplechat.utils.TimeFormat;
import com.seveneow.simplechat.utils.TimeParser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennychen on 2017/1/24.
 */

public class Message implements Parcelable {
  public static final int TYPE_TEXT = 0;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_STICKER = 2;
  public static final int TYPE_FILE = 3;

  private int type = TYPE_TEXT;
  private long databaseId = -1;
  private String id = null;
  private String time = null;
  private String senderId = "";
  private String showTime = "";
  private String roomId = null;
  private String showText = "";
  private boolean isPending = false;
  private boolean isShowSender = false;

  public Message() {
  }

  public Message(int type) {
    this.type = type;
  }

  public Message(Message message) {
    this.id = message.getId();
    this.databaseId = message.getDatabaseId();
    this.type = message.getType();
    this.time = message.getTime();
    this.senderId = message.getSenderId();
    this.roomId = message.getRoomId();
    this.isPending = message.isPending();
    this.isShowSender = message.isShowSender();
    this.showText = message.getShowText();
  }

  public void updateMessage(Message message) {
    this.type = message.getType();
    this.databaseId = message.getDatabaseId();
    this.time = message.getTime();
    this.senderId = message.getSenderId();
    this.roomId = message.getRoomId();
    this.isPending = message.isPending();
    this.isShowSender = message.isShowSender();
    this.showText = message.getShowText();
  }

  public long getDatabaseId() {
    return databaseId;
  }

  public void setDatabaseId(long databaseId) {
    this.databaseId = databaseId;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getTime() {
    return time;
  }

  public String getShowTime() {
    if (showTime != null && !showTime.isEmpty())
      return showTime;

    showTime = TimeParser.getTimeStr(Long.valueOf(time), TimeFormat.CHAT_TIME_FORMAT);
    return showTime;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public boolean isPending() {
    return isPending;
  }

  public void setPending(boolean isPending) {
    this.isPending = isPending;
  }

  public boolean isShowSender() {
    return isShowSender;
  }

  public void setShowSender(boolean isShowSender) {
    this.isShowSender = isShowSender;
  }

  public String getShowText() {
    return showText;
  }

  public void setShowText(String showText) {
    this.showText = showText;
  }

  public Message(Parcel in) {
    this.id = in.readString();
    this.databaseId = in.readLong();
    this.type = in.readInt();
    this.time = in.readString();
    this.senderId = in.readString();
    this.roomId = in.readString();
    this.isPending = in.readInt() == 1;
    this.isShowSender = in.readInt() == 1;
    this.showText = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeLong(this.databaseId);
    dest.writeInt(this.type);
    dest.writeString(this.time);
    dest.writeString(this.senderId);
    dest.writeString(this.roomId);
    dest.writeInt(this.isPending ? 1 : 0);
    dest.writeInt(this.isShowSender ? 1 : 0);
    dest.writeString(this.showText);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Message createFromParcel(Parcel in) {
      return new Message(in);
    }

    public Message[] newArray(int size) {
      return new Message[size];
    }
  };

  public static class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
      return o2.getTime().compareTo(o1.getTime());
    }
  }

  @CallSuper
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("type", type);
    result.put("timestamp", Long.valueOf(time));
    result.put("sender_id", senderId);
    result.put("isShowSender", isShowSender);
    result.put("roomId", roomId);
    return result;
  }
}
