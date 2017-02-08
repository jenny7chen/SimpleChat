package com.seveneow.simplechat.utils;

import android.util.Log;

import com.seveneow.simplechat.BuildConfig;

public class DebugLog {
  public static void e(String tag, String message){
    if(BuildConfig.DEBUG){
      Log.e(tag, message);
    }
  }

  public static void printStackTrace(Exception e){
    if(BuildConfig.DEBUG){
      e.printStackTrace();
    }
  }

  public static void d(String tag, String message){
    if(BuildConfig.DEBUG){
      Log.d(tag, message);
    }
  }
}
