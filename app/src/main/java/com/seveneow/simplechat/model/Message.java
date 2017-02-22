package com.seveneow.simplechat.model;

import com.seveneow.simplechat.utils.TimeFormat;
import com.seveneow.simplechat.utils.TimeParser;

import java.util.Comparator;

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
  private String pendingId = null;
  private String messageShowTime = "";
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

  public String getMessageShowTime() {
    if (messageShowTime != null && !messageShowTime.isEmpty())
      return messageShowTime;

    messageShowTime = TimeParser.getTimeStr(Long.valueOf(messageTime), TimeFormat.CHAT_TIME_FORMAT);
    return messageShowTime;
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

  public boolean isFromMe() {
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

  public String getPendingId() {
    return pendingId;
  }

  public void setPendingId(String pendingId) {
    this.pendingId = pendingId;
  }

  public static class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
      return o2.getMessageTime().compareTo(o1.getMessageTime());
    }
  }
}
