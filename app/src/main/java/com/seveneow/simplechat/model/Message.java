package com.seveneow.simplechat.model;

/**
 * Created by jennychen on 2017/1/24.
 */

public class Message {
  public static final int TYPE_TEXT = 0;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_STICKER = 2;
  public static final int TYPE_FILE = 3;

  private int type = TYPE_TEXT;
  private String messageId = null;
  private String messageTime = null;
  private String senderId = "";
  private String receiverId = null;
  private boolean isPending = false;
  private boolean isShowSender = false;

  public Message(int type) {
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

  public boolean isMe() {
    return senderId == null || senderId.isEmpty();
  }

  public boolean isPending() {
    return isPending;
  }

  public void setPending(boolean isPending) {
    this.isPending = isPending;
  }

  public boolean isShowSender() {
    return isShowSender;
  }

  public void setShowSender(boolean isShowSender) {
    this.isShowSender = isShowSender;
  }
}
