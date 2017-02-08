package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.message.TextMessage;

/**
 * Created by jennychen on 2017/1/24.
 */

public class TextMessageView extends MessageView {

  public TextMessageView(Context context) {
    super(context);
  }

  public TextMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TextMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  protected void init() {
    LinearLayout viewContainer = (LinearLayout) rootView.findViewById(R.id.message_view_container);
    layout = LayoutInflater.from(getContext()).inflate(R.layout.text_message_view, viewContainer, false);
    viewContainer.removeAllViews();
    viewContainer.addView(layout);
  }

  protected void setView() {
    TextView messageView = (TextView) layout.findViewById(R.id.xxx);
    messageView.setText(((TextMessage) message).getMessage());
    //    imageLayout.setOnClickListener(this);
  }
}
