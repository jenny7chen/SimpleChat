package com.seveneow.simplechat.model;


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
}
