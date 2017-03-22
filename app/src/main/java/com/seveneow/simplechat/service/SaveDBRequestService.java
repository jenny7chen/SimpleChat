package com.seveneow.simplechat.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.seveneow.simplechat.model.DBRequest;

import java.util.Iterator;
import java.util.LinkedList;

public class SaveDBRequestService extends Service {
  private static final int PENDING_TIME = 1000;
  private static final int MAX_RUNNING_REQUEST_AMOUNT = 10000;
  private final Binder binder = new SaveDBBinder();
  private LinkedList<DBRequest> pendingQueue;
  private LinkedList<DBRequest> runningQueue;
  private Handler handler;
  private Runnable timerRunnable = null;

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
    handler = new Handler();

    return super.onStartCommand(intent, flags, startId);
  }

  private void initQueue() {
    runningQueue = new LinkedList<DBRequest>();
    pendingQueue = new LinkedList<DBRequest>();
  }

  public boolean transactionIsStarted() {
    return false;
  }

  public void addRequest(DBRequest request) {
    pendingQueue.add(request);
    checkPendingQueue();
  }

  private synchronized void checkPendingQueue() {
    if (transactionIsStarted()) {
      return;
    }
    if (pendingQueue.size() > 0) {
      startTransaction();
    }
    else {
      stopSelf();
    }
  }

  private void startTransaction() {
    //TODO: lock pending queue when doing this
    Iterator iterator = pendingQueue.iterator();
    while (iterator.hasNext()) {
      DBRequest request = (DBRequest) iterator.next();
      runningQueue.add(request);
    }
    pendingQueue.clear();

    //TODO: start DB transaction here


    if (runningQueue.size() > MAX_RUNNING_REQUEST_AMOUNT) {
      commitTransaction();
    }
    else {
      startTimer();
    }
  }


  private void startTimer() {
    timerRunnable = this::commitTransaction;
    handler.postDelayed(timerRunnable, PENDING_TIME);
  }

  private void commitTransaction() {
    if (timerRunnable != null)
      handler.removeCallbacks(timerRunnable);

    //TODO: do db transaction commit here

    runningQueue.clear();
    checkPendingQueue();
  }

  public int getRequestCount() {
    return runningQueue.size() + pendingQueue.size();
  }

}
