package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.utils.Static;


public class ImageMessageView extends MessageView {

  public ImageMessageView(Context context) {
    super(context);
    layoutId = R.layout.image_message_view;
    init();
  }

  public ImageMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ImageMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected void setTypeView() {
    ImageView imageView = (ImageView) messageView.findViewById(R.id.image_view);
    ImageLoader.getInstance().displayImage(((ImageMessage) message).getThumbnail(), imageView, Static.defaultDisplayImageOptions(R.mipmap.ic_launcher, true));
  }
}
