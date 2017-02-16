package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.TextMessage;

/**
 * Created by jennychen on 2017/1/24.
 */

public class TextMessageView extends MessageView {

  public TextMessageView(Context context) {
    super(context);
    layoutId = R.layout.text_message_view;
    init();
  }

  public TextMessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TextMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }


  protected void setView() {
    TextView messageView = (TextView) layout.findViewById(R.id.text);
    messageView.setText(((TextMessage) message).getMessage());
    //    imageLayout.setOnClickListener(this);
  }
}
