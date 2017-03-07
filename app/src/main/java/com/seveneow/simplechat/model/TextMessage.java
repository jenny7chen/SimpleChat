package com.seveneow.simplechat.model;

import java.util.Map;

/**
 * Created by jennychen on 2017/1/24.
 */

public class TextMessage extends Message {
  private String message;

  public TextMessage() {
    super(TYPE_TEXT);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = super.toMap();
    map.put("message", message);
    return map;
  }
}
