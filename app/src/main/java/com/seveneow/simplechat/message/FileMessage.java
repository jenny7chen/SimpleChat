package com.seveneow.simplechat.message;

public class FileMessage extends Message {
  private String url;
  private String md5;
  private String name;
  private String size;

  public FileMessage(){
    super(Message.TYPE_FILE);
  }

  public FileMessage(int type) {
    super(type);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

}
