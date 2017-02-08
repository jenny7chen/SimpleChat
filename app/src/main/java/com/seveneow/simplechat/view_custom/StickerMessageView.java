package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.seveneow.simplechat.R;

/**
 * Created by jennychen on 2017/1/24.
 */

public class StickerMessageView extends MessageView {

  public StickerMessageView(Context context) {
    super(context);
  }

  public StickerMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StickerMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected void init() {
    LinearLayout viewContainer = (LinearLayout) rootView.findViewById(R.id.message_view_container);
    layout = LayoutInflater.from(getContext()).inflate(R.layout.sticker_message_view, viewContainer, false);
    viewContainer.removeAllViews();
    viewContainer.addView(layout);
  }

  protected void setView() {
    //    TextView messageView = (TextView) layout.findViewById(R.id.xxx);
    //    messageView.setText(((TextMessage) message).getMessage());
    //    imageLayout.setOnClickListener(this);
  }
}
