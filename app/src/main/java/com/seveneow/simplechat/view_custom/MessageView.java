package com.seveneow.simplechat.view_custom;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.utils.UserManager;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jennychen on 2017/1/24.
 */

public class MessageView extends RelativeLayout {
  protected Message message;

  public MessageView(Context context, boolean isMe) {
    super(context);
    initRootView(isMe);
  }

  public void setMessage(Message message) {
    this.message = message;
    setRootView();
    setTypeView();
  }

  protected void initRootView(boolean isMe) {
    removeAllViews();
    LayoutInflater.from(getContext()).inflate(R.layout.message_view, this, true);
    setViewWithAlignment(isMe);
  }

  private void setViewWithAlignment(boolean isMe) {
    findViewById(R.id.message_left_time).setVisibility(GONE);
    findViewById(R.id.message_right_time).setVisibility(GONE);
    findViewById(R.id.avatar).setVisibility(GONE);
    findViewById(R.id.message_sender).setVisibility(GONE);

    if (isMe) {
      setGravity(Gravity.END);
      findViewById(R.id.message_left_time).setVisibility(VISIBLE);
      findViewById(R.id.message_view_container).setBackgroundColor(Color.parseColor("#f2f2f2"));
    }
    else {
      setGravity(Gravity.START);
      findViewById(R.id.message_right_time).setVisibility(VISIBLE);
      findViewById(R.id.avatar).setVisibility(VISIBLE);
      findViewById(R.id.message_sender).setVisibility(VISIBLE);
      findViewById(R.id.message_view_container).setBackgroundColor(Color.parseColor("#8ca3e1"));
    }
  }

  protected void setRootView() {
    TextView senderView = (TextView) findViewById(R.id.message_sender);
    senderView.setText(message.getSenderId());
    senderView.setVisibility(message.isShowSender() ? VISIBLE : GONE);

    TextView leftTimeView = (TextView) findViewById(R.id.message_left_time);
    TextView rightTimeView = (TextView) findViewById(R.id.message_right_time);
    leftTimeView.setText(message.getMessageShowTime());
    rightTimeView.setText(message.getMessageShowTime());

    CircleImageView circleImageView = (CircleImageView) findViewById(R.id.avatar);
    ImageLoader.getInstance().displayImage(UserManager.getInstance().getUserPhoto(message.getSenderId()), circleImageView, Static.defaultDisplayImageOptions(R.mipmap.ic_launcher, true));
  }

  public void setTypeView() {
    RelativeLayout textLayout = (RelativeLayout) findViewById(R.id.text_layout);
    RelativeLayout imageLayout = (RelativeLayout) findViewById(R.id.image_layout);
    RelativeLayout stickerLayout = (RelativeLayout) findViewById(R.id.sticker_layout);
    if (message.getType() == Message.TYPE_TEXT) {
      textLayout.setVisibility(VISIBLE);
      imageLayout.setVisibility(GONE);
      stickerLayout.setVisibility(GONE);
      TextView textView = (TextView) findViewById(R.id.text_view);
      textView.setText(((TextMessage) message).getMessage());
      if (message.isPending()) {
        textView.setTextColor(Color.RED);
      }
      else if (message.isMe()) {
        textView.setTextColor(Color.BLACK);
      }
      else {
        textView.setTextColor(Color.WHITE);
      }

    }
    else if (message.getType() == Message.TYPE_IMAGE) {
      imageLayout.setVisibility(VISIBLE);
      textLayout.setVisibility(GONE);
      stickerLayout.setVisibility(GONE);
      ImageView imageView = (ImageView) findViewById(R.id.image_view);
      ImageLoader.getInstance().displayImage(((ImageMessage) message).getThumbnail(), imageView, Static.defaultDisplayImageOptions(R.mipmap.ic_launcher, true));

    }
    else if (message.getType() == Message.TYPE_STICKER) {
      stickerLayout.setVisibility(VISIBLE);
      textLayout.setVisibility(GONE);
      imageLayout.setVisibility(GONE);

    }
  }
}
