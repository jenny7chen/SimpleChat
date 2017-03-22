package com.seveneow.simplechat.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.seveneow.simplechat.model.Message;

/**
 * Created by jennychen on 2017/2/16.
 */

public class Static {
  private static DisplayImageOptions imageOptions;

  //TODO: test use
  public static final String userId = "456";
  public static final String DB_PASS = "123";
  public static final String FIREBASE_PROJECT_ID = "test-f0908";

  public static DisplayImageOptions defaultDisplayImageOptions(int loadingImageRes, boolean fadeInDisplayer) {
    if (imageOptions != null)
      return imageOptions;

    DisplayImageOptions.Builder optionsBuilder = new DisplayImageOptions.Builder();
    optionsBuilder.cacheInMemory(true);
    optionsBuilder.cacheOnDisk(true);
    optionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
    optionsBuilder.displayer(new RoundedBitmapDisplayer(50));

    if (loadingImageRes != 0) {
      optionsBuilder.showImageOnLoading(loadingImageRes);
    }

    if (fadeInDisplayer) {
      optionsBuilder.displayer(new FadeInBitmapDisplayer(200));
    }
    imageOptions = optionsBuilder.build();
    return imageOptions;
  }

  public static boolean isMessageFromMe(Message message){
    return message.getSenderId() == null || message.getSenderId().isEmpty() || userId.equals(message.getSenderId());
  }

  public static boolean isMessageSentFromLocal(Message message){
    return message.getDatabaseId() == -1;
  }

  public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }
}
