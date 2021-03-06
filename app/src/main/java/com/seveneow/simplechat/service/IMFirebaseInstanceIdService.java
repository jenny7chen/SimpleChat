package com.seveneow.simplechat.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.seveneow.simplechat.utils.DebugLog;

/**
 * Created by jennychen on 2017/2/7.
 */

public class IMFirebaseInstanceIdService extends FirebaseInstanceIdService{
  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    // Get updated InstanceID token.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    //  Log.d(TAG, "Refreshed token: " + refreshedToken);

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // Instance ID token to your app server.
    //  sendRegistrationToServer(refreshedToken);

    DebugLog.e("Baaa", "refreshedToken = " + refreshedToken);
  }
}

