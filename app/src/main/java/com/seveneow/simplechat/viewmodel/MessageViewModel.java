package com.seveneow.simplechat.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.ImageMessage;
import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.TextMessage;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.utils.UserManager;

public class MessageViewModel extends BaseObservable {
  private Message message;
  private Message nextMessage;
  private Message lastMessage;
  private Context context;

  public MessageViewModel(Message message, Context context, Message nextMessage, Message lastMessage) {
    this.message = message;
    this.nextMessage = nextMessage;
    this.lastMessage = lastMessage;
    this.context = context;
  }

  public int getMessageTimeVisibility() {
    return nextMessage != null && nextMessage.getSenderId().equals(message.getSenderId()) ? View.GONE : View.VISIBLE;
  }

  public int getMessageAlignment() {
    int rightAlignment = Gravity.END | Gravity.BOTTOM;
    int leftAlignment = Gravity.START | Gravity.BOTTOM;
    return Static.isMessageFromMe(message) ? rightAlignment : leftAlignment;
  }

  public int getMessageTimeAlign() {
    return Static.isMessageFromMe(message) ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT;
  }

  public Drawable getTextMessageBgResourceId() {
    int drawableId = Static.isMessageFromMe(message) ? R.drawable.chat_bubble_gray : R.drawable.chat_bubble_purple;
    return ContextCompat.getDrawable(context, drawableId);
  }

  public int getSenderVisibility() {
    boolean visible;
    Info room = RoomManager.getInstance().getRoomById(message.getRoomId());
    visible = room != null && room.getType() != Info.TYPE_USER;
    if (!visible)
      return View.GONE;

    visible = !Static.isMessageFromMe(message);
    if (!visible)
      return View.GONE;

    visible = lastMessage == null || !lastMessage.getSenderId().equals(message.getSenderId());
    return visible ? View.VISIBLE : View.GONE;
  }

  public String getMessageTime() {
    return message.getShowTime();
  }

  public String getMessageSender() {
    return message.getSenderId();
  }

  public int getAvatarVisibility() {
    if (Static.isMessageFromMe(message)) {
      return View.GONE;
    }
    return nextMessage != null && nextMessage.getSenderId().equals(message.getSenderId()) ? View.GONE : View.VISIBLE;
  }

  public int getAvatarAlternativeVisibility() {
    return Static.isMessageFromMe(message) ? View.GONE : View.VISIBLE;
  }

  public String getAvatarUrl() {
    if (Static.isMessageFromMe(message)) {
      return "";
    }
    boolean showAvatar = !(nextMessage != null && nextMessage.getSenderId().equals(message.getSenderId()));
    return showAvatar ? UserManager.getInstance().getUserPhoto(message.getSenderId()) : "";
  }

  public int getTextMessageVisibility() {
    return message.getType() == Message.TYPE_TEXT ? View.VISIBLE : View.GONE;
  }

  public int getImageMessageVisibility() {
    return message.getType() == Message.TYPE_IMAGE ? View.VISIBLE : View.GONE;
  }

  public int getStickerMessageVisibility() {
    return message.getType() == Message.TYPE_STICKER ? View.VISIBLE : View.GONE;
  }

  public String getTextMessage() {
    if (message.getType() == Message.TYPE_TEXT) {
      return ((TextMessage) message).getMessage();
    }
    return "";
  }

  public int getTextColor() {
    if (message.getType() == Message.TYPE_TEXT) {
      if (message.isPending()) {
        return Color.RED;
      }
      else if (Static.isMessageFromMe(message)) {
        return Color.BLACK;
      }
      else {
        return Color.WHITE;
      }
    }
    return Color.BLACK;
  }

  public String getImageMessageThumbnail() {
    if (message.getType() == Message.TYPE_IMAGE) {
      return ((ImageMessage) message).getThumbnail();
    }
    return "";
  }
}
