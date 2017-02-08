package com.seveneow.simplechat.message;

/**
 * Created by jennychen on 2017/1/24.
 */

public class ImageMessage extends FileMessage {
  private String thumbnail;

  public ImageMessage() {
    super(TYPE_IMAGE);
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }
}
