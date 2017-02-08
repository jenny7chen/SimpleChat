package com.seveneow.simplechat.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RxEventBus;


public class IMFirebaseMessagingService extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      DebugLog.e("Mess", "Message data payload: " + remoteMessage.getData());
    }
    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      DebugLog.e("Mess", "Message Notification Body: " + remoteMessage.getNotification().getBody());
      RxEventBus.send(remoteMessage.getNotification().getBody());
    }
  }
}
