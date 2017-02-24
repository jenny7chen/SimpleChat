package com.seveneow.simplechat.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.model.DBRequest;

import java.util.LinkedList;

public class SaveDBRequestService extends Service {
  private final Binder binder = new SaveDBBinder();
  private LinkedList requestQueue;

  public class SaveDBBinder extends Binder {
    SaveDBRequestService getService() {
      return SaveDBRequestService.this;
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    initQueue();


    return super.onStartCommand(intent, flags, startId);
  }

  private void initQueue() {
    requestQueue = new LinkedList();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  public boolean isTransactionEnded() {
    return false;
  }

  public void addRequest(DBRequest request) {

  }
}
