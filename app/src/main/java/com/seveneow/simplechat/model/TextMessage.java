package com.seveneow.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by jennychen on 2017/1/24.
 */

public class TextMessage extends Message{
  private String message;

  public TextMessage() {
    super(TYPE_TEXT);
  }

  public TextMessage(Message message) {
    super(message);
    this.message = ((TextMessage) message).getMessage();
    this.setType(TYPE_TEXT);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("message", message);
    return map;
  }

  public static final Parcelable.Creator<TextMessage> CREATOR = new Parcelable.Creator<TextMessage>() {
    public TextMessage createFromParcel(Parcel in) {
      return new TextMessage(in);
    }

    public TextMessage[] newArray(int size) {
      return new TextMessage[size];
    }
  };

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeString(message);
  }

  private TextMessage(Parcel in) {
    super(in);
    this.message = in.readString();
  }
}
