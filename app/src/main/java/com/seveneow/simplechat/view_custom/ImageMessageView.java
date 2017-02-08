package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;

import com.seveneow.simplechat.R;

/**
 * Created by jennychen on 2017/1/24.
 */

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

  protected void setView() {
    //    TextView messageView = (TextView) messageView.findViewById(R.id.xxx);
    //    messageView.setText(((TextMessage) message).getMessage());
    //    imageLayout.setOnClickListener(this);
  }
}
