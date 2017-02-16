package com.seveneow.simplechat.model;

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

}
