package com.seveneow.simplechat.service;

import android.app.IntentService;
import android.content.Intent;


public class FetchMessageService extends IntentService{

  public FetchMessageService(){
    super("FetchMessageService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //do queries here
  }
}
