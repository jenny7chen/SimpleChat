package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.message.Message;

/**
 * Created by jennychen on 2017/1/24.
 */

public abstract class MessageView extends FrameLayout {
  protected boolean isSender;
  protected Message message;
  protected View rootView;
  protected View layout;

  public MessageView(Context context) {
    super(context);
    initRootView();
    init();
  }

  public MessageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setMessage(Message message) {
    this.message = message;
    setView();
    invalidate();
  }

  protected void initRootView() {
    rootView = LayoutInflater.from(getContext()).inflate(R.layout.message_view, this, true);
  }

  protected abstract void init();

  protected abstract void setView();
}
