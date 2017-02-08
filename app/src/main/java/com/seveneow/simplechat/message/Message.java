package com.seveneow.simplechat.message;

/**
 * Created by jennychen on 2017/1/24.
 */

public class Message {
  public static final int TYPE_TEXT = 0;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_STICKER = 2;
  public static final int TYPE_FILE = 3;

  private int type = TYPE_TEXT;
  private String messageId;
  private String messageTime;
  private String senderId;
  private String receiverId;

  public Message(int type){
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getMessageTime() {
    return messageTime;
  }

  public void setMessageTime(String messageTime) {
    this.messageTime = messageTime;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }
}
