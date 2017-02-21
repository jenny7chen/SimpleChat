package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.utils.UserManager;

import de.hdodenhof.circleimageview.CircleImageView;

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
    initRootView();
  }

  public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initRootView();
  }

  public void setMessage(Message message) {
    this.message = message;
    setViewWithAlignment();
    setRootView();
    setTypeView();
    invalidate();
  }

  protected void initRootView() {
    removeAllViews();
    messageView = LayoutInflater.from(getContext()).inflate(R.layout.message_view, this, true);
  }

  private void setViewWithAlignment() {
    if (message.isMe()) {
      setGravity(Gravity.END);
      messageView.findViewById(R.id.message_left_time).setVisibility(VISIBLE);
      messageView.findViewById(R.id.message_right_time).setVisibility(GONE);
      messageView.findViewById(R.id.avatar).setVisibility(GONE);
      messageView.findViewById(R.id.message_sender).setVisibility(GONE);
      messageView.findViewById(R.id.message_view_container).setBackgroundColor(Color.parseColor("#f6f6f6"));
    }
    else {
      setGravity(Gravity.START);
      messageView.findViewById(R.id.message_left_time).setVisibility(GONE);
      messageView.findViewById(R.id.message_right_time).setVisibility(VISIBLE);
      messageView.findViewById(R.id.avatar).setVisibility(VISIBLE);
      messageView.findViewById(R.id.message_sender).setVisibility(VISIBLE);
      messageView.findViewById(R.id.message_view_container).setBackgroundColor(Color.parseColor("#8ca3e1"));
    }
  }

  protected void init() {
    ViewGroup viewContainer = (ViewGroup) messageView.findViewById(R.id.message_view_container);
    layout = LayoutInflater.from(getContext()).inflate(layoutId, viewContainer, true);
  }

  protected void setRootView() {
    TextView senderView = (TextView) messageView.findViewById(R.id.message_sender);
    senderView.setText(message.getSenderId());
    senderView.setVisibility(message.isShowSender() ? VISIBLE : GONE);

    TextView leftTimeView = (TextView) messageView.findViewById(R.id.message_left_time);
    TextView rightTimeView = (TextView) messageView.findViewById(R.id.message_right_time);
    leftTimeView.setText(message.getMessageShowTime());
    rightTimeView.setText(message.getMessageShowTime());

    CircleImageView circleImageView = (CircleImageView) messageView.findViewById(R.id.avatar);
    ImageLoader.getInstance().displayImage(UserManager.getInstance().getUserPhoto(message.getSenderId()), circleImageView, Static.defaultDisplayImageOptions(R.mipmap.ic_launcher, true));
  }

  protected abstract void setTypeView();
}
