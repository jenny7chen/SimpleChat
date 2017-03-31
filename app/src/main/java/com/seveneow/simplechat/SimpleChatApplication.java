package com.seveneow.simplechat;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.seveneow.simplechat.service.GetDBRoomListService;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.Static;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

public class SimpleChatApplication extends Application implements Application.ActivityLifecycleCallbacks {
  @Override
  public void onCreate() {
    super.onCreate();
    // initialize ImageLoader
    File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

    ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext());
    configBuilder.defaultDisplayImageOptions(Static.defaultDisplayImageOptions(R.mipmap.ic_launcher, true));
    configBuilder.memoryCache(new WeakMemoryCache());
    configBuilder.diskCache(new LimitedAgeDiskCache(cacheDir, 86400 /* 1 day */));

    SQLiteDatabase.loadLibs(this);

    ImageLoader.getInstance().init(configBuilder.build());
    FDBManager.init();
    registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);

  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    if (!RoomManager.getInstance().hasRoomData()) {
      Intent intent = new Intent(this, GetDBRoomListService.class);
      startService(intent);
    }
  }

  @Override
  public void onActivityStarted(Activity activity) {

  }

  @Override
  public void onActivityResumed(Activity activity) {

  }

  @Override
  public void onActivityPaused(Activity activity) {

  }

  @Override
  public void onActivityStopped(Activity activity) {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override
  public void onActivityDestroyed(Activity activity) {

  }
}
