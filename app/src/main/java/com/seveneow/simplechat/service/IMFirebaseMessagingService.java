package com.seveneow.simplechat.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.utils.RxEventBus;


public class IMFirebaseMessagingService extends FirebaseMessagingService {
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    // Check if message contains a data payload.
    if (remoteMessage.getData().get("message") != null) {
      DebugLog.e("baaa", "receive message = " + remoteMessage.getData().get("message"));

      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_NOTIFICATION;
      event.object = remoteMessage.getData().get("message");
      RxEventBus.send(event);
    }
    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      DebugLog.e("baaa", "receive message = " + remoteMessage.getNotification());

      DebugLog.e("Mess", "Message Notification Body: " + remoteMessage.getNotification().getBody());
      RxEvent event = new RxEvent();
      event.id = RxEvent.EVENT_NOTIFICATION;
      event.object = remoteMessage.getNotification().getBody();
      RxEventBus.send(event);
    }
  }
}
