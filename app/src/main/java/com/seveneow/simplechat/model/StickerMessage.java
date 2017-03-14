package com.seveneow.simplechat.model;

import java.util.Map;

/**
 * Created by jennychen on 2017/1/24.
 */

public class StickerMessage extends Message {
  private String stickerId = "";
  private String stickerGroupId;

  public StickerMessage() {
    super(TYPE_STICKER);
  }

  public StickerMessage(Message message) {
    super(message);
    this.stickerId = ((StickerMessage) message).getStickerId();
    this.stickerGroupId = ((StickerMessage) message).getStickerGroupId();
    this.setType(TYPE_STICKER);
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

  @Override
  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("stickerId", stickerId);
    map.put("stickerGroupId", stickerGroupId);
    return map;
  }
}
