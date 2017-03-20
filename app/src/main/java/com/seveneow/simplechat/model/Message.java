package com.seveneow.simplechat.model;

import android.support.annotation.CallSuper;

import com.seveneow.simplechat.utils.TimeFormat;
import com.seveneow.simplechat.utils.TimeParser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennychen on 2017/1/24.
 */

public class Message {
  public static final int TYPE_TEXT = 0;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_STICKER = 2;
  public static final int TYPE_FILE = 3;

  private int type = TYPE_TEXT;
  private String id = null;
  private String time = null;
  private String senderId = "";
  private String pendingId = null;
  private String showTime = "";
  private String roomId = null;
  private String showText = "";
  private boolean isPending = false;
  private boolean isShowSender = false;

  public Message() {
  }

  public Message(int type) {
    this.type = type;
  }

  public Message(Message message) {
    this.id = message.getId();
    this.type = message.getType();
    this.time = message.getTime();
    this.senderId = message.getSenderId();
    this.pendingId = message.getPendingId();
    this.roomId = message.getRoomId();
    this.isPending = message.isPending();
    this.isShowSender = message.isShowSender();
  }

  public void updateMessage(Message message){
    this.type = message.getType();
    this.time = message.getTime();
    this.senderId = message.getSenderId();
    this.pendingId = message.getPendingId();
    this.roomId = message.getRoomId();
    this.isPending = message.isPending();
    this.isShowSender = message.isShowSender();
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getTime() {
    return time;
  }

  public String getShowTime() {
    if (showTime != null && !showTime.isEmpty())
      return showTime;

    showTime = TimeParser.getTimeStr(Long.valueOf(time), TimeFormat.CHAT_TIME_FORMAT);
    return showTime;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
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


  public String getShowText() {
    return showText;
  }

  public void setShowText(String showText) {
    this.showText = showText;
  }


  public static class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
      return o2.getTime().compareTo(o1.getTime());
    }
  }

  @CallSuper
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("type", type);
    result.put("timestamp", Long.valueOf(time));
    result.put("sender_id", senderId);
    result.put("isPending", isPending);
    result.put("pendingId", pendingId);
    result.put("isShowSender", isShowSender);
    result.put("roomId", roomId);
    return result;
  }
}
