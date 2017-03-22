package com.seveneow.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

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

  public static final Parcelable.Creator<StickerMessage> CREATOR = new Parcelable.Creator<StickerMessage>() {
    public StickerMessage createFromParcel(Parcel in) {
      return new StickerMessage(in);
    }

    public StickerMessage[] newArray(int size) {
      return new StickerMessage[size];
    }
  };

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    super.writeToParcel(out, flags);
    out.writeString(stickerId);
    out.writeString(stickerGroupId);
  }

  public StickerMessage(Parcel in) {
    super(in);
    this.stickerId = in.readString();
    this.stickerGroupId = in.readString();
  }
}
