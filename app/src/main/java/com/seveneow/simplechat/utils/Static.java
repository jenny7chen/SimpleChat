package com.seveneow.simplechat.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by jennychen on 2017/2/16.
 */

public class Static {
  private static DisplayImageOptions imageOptions;

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
}
