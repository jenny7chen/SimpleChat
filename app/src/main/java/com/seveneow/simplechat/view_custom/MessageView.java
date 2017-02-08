package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.message.Message;

/**
 * Created by jennychen on 2017/1/24.
 */

public abstract class MessageView extends RelativeLayout {
  protected Message message;
  protected View messageView;
  protected int layoutId;
  protected View layout;

  public MessageView(Context context) {
    super(context);
    initRootView();
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
    setViewAlignment();
  }

  protected void initRootView() {
    removeAllViews();
    messageView = LayoutInflater.from(getContext()).inflate(R.layout.message_view, this, true);
  }

  private void setViewAlignment() {
    if (message.isMe()) {
      setGravity(Gravity.END);
      messageView.findViewById(R.id.message_left_time).setVisibility(VISIBLE);
      messageView.findViewById(R.id.message_right_time).setVisibility(GONE);
      messageView.findViewById(R.id.avatar).setVisibility(GONE);
    }
    else {
      setGravity(Gravity.START);
      messageView.findViewById(R.id.message_left_time).setVisibility(GONE);
      messageView.findViewById(R.id.message_right_time).setVisibility(VISIBLE);
      messageView.findViewById(R.id.avatar).setVisibility(VISIBLE);
    }
  }

  protected void init() {
    ViewGroup viewContainer = (ViewGroup) messageView.findViewById(R.id.message_view_container);
    layout = LayoutInflater.from(getContext()).inflate(layoutId, viewContainer, true);
  }

  protected abstract void setView();
}
