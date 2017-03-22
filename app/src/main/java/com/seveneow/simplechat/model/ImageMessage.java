package com.seveneow.simplechat.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;


public class ImageMessage extends FileMessage {
  private String thumbnail;

  public ImageMessage() {
    super(TYPE_IMAGE);
  }


  public ImageMessage(Message message) {
    super(message);
    this.thumbnail = ((ImageMessage)message).getThumbnail();
    this.setType(TYPE_IMAGE);
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("thumbnail", thumbnail);
    return map;
  }

  public static final Parcelable.Creator<ImageMessage> CREATOR = new Parcelable.Creator<ImageMessage>() {
    public ImageMessage createFromParcel(Parcel in) {
      return new ImageMessage(in);
    }

    public ImageMessage[] newArray(int size) {
      return new ImageMessage[size];
    }
  };

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeString(thumbnail);
  }

  private ImageMessage(Parcel in) {
    super(in);
    this.thumbnail = in.readString();
  }
}
