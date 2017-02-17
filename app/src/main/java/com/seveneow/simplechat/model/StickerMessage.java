package com.seveneow.simplechat.model;

/**
 * Created by jennychen on 2017/1/24.
 */

public class StickerMessage extends Message {
  private String stickerId = "";
  private String stickerGroupId;

  public StickerMessage() {
    super(TYPE_STICKER);
  }

  public String getStickerId() {
    return stickerId;
  }

  public void setStickerId(String stickerId) {
    this.stickerId = stickerId;
  }

  public String getStickerGroupId() {
    return stickerGroupId;
  }

  public void setStickerGroupId(String stickerGroupId) {
    this.stickerGroupId = stickerGroupId;
  }

}
