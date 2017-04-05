package com.seveneow.simplechat.model;

import android.os.Parcel;

import java.util.Map;


public class SystemMessage extends Message {
  private String systemMessage = "";
  public static final int JOIN_GROUP = 1;
  public static final int LEAVE_GROUP = 2;
  public static final int CHANGE_GROUP_NAME = 3;
  public static final int CHANGE_GROUP_PHOTO = 4;
  private String actorName = "";

  public SystemMessage() {
    super(TYPE_SYSTEM_MESSAGE);
  }

  public SystemMessage(Message message) {
    super(message);
    this.systemMessage = ((SystemMessage) message).getSystemMessage();
    this.setType(TYPE_SYSTEM_MESSAGE);
  }

  public String getSystemMessage() {
    return systemMessage;
  }

  public void setSystemMessage(String systemMessage) {
    this.systemMessage = systemMessage;
  }


  @Override
  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("system_message", systemMessage);
    return map;
  }

  public static final Creator<SystemMessage> CREATOR = new Creator<SystemMessage>() {
    public SystemMessage createFromParcel(Parcel in) {
      return new SystemMessage(in);
    }

    public SystemMessage[] newArray(int size) {
      return new SystemMessage[size];
    }
  };

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeString(systemMessage);
  }

  public SystemMessage(Parcel in) {
    super(in);
    this.systemMessage = in.readString();
  }

  @Override
  public String getShowText() {
    return super.getShowText();
  }
}
