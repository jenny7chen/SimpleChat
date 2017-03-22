package com.seveneow.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class FileMessage extends Message {
  private String url;
  private String md5;
  private String name;
  private String size;

  public FileMessage() {
    super(Message.TYPE_FILE);
  }

  public FileMessage(Message message) {
    super(message);
    this.url = ((FileMessage) message).getUrl();
    this.md5 = ((FileMessage) message).getMd5();
    this.name = ((FileMessage) message).getName();
    this.size = ((FileMessage) message).getSize();
    this.setType(TYPE_IMAGE);
  }

  public FileMessage(int type) {
    super(type);
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("url", url);
    map.put("md5", md5);
    map.put("name", name);
    map.put("size", size);
    return map;
  }

  public static final Parcelable.Creator<FileMessage> CREATOR = new Parcelable.Creator<FileMessage>() {
    public FileMessage createFromParcel(Parcel in) {
      return new FileMessage(in);
    }

    public FileMessage[] newArray(int size) {
      return new FileMessage[size];
    }
  };

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeString(url);
    out.writeString(md5);
    out.writeString(name);
    out.writeString(size);
  }

  public FileMessage(Parcel in) {
    super(in);
    this.url = in.readString();
    this.md5 = in.readString();
    this.name = in.readString();
    this.size = in.readString();
  }
}
