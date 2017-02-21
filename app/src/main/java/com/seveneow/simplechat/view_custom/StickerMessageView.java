package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;

import com.seveneow.simplechat.R;

/**
 * Created by jennychen on 2017/1/24.
 */

public class StickerMessageView extends MessageView {

  public StickerMessageView(Context context) {
    super(context);
    layoutId = R.layout.sticker_message_view;
    init();
  }

  public StickerMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StickerMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected void setTypeView() {
    //    TextView messageView = (TextView) messageView.findViewById(R.id.xxx);
    //    messageView.setText(((TextMessage) message).getMessage());
    //    imageLayout.setOnClickListener(this);
  }
}
