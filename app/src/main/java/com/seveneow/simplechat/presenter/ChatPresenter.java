package com.seveneow.simplechat.presenter;


import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.seveneow.simplechat.message.ImageMessage;
import com.seveneow.simplechat.message.Message;
import com.seveneow.simplechat.message.StickerMessage;
import com.seveneow.simplechat.message.TextMessage;
import com.seveneow.simplechat.view_interface.ChatMvpView;

import java.util.ArrayList;

public class ChatPresenter extends MvpBasePresenter<ChatMvpView> {
  ArrayList<Message> messageList = new ArrayList<>();

  public void updateList() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //get data here using asynchttp lib

    //depends on condition
    if (getView().getData() != null)
      return;

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      //test data onSuccess
      TextMessage text = new TextMessage();
      text.setMessage("123");
      text.setSenderId("1");

      ImageMessage image = new ImageMessage();
      StickerMessage sticker = new StickerMessage();

      messageList.add(text);
      messageList.add(image);
      messageList.add(sticker);

      getView().setData(messageList, false);
      getView().showContent();

      //test data onFail
      // getView().showError()
    }, 1000);

  }

  public void receiveMessage(String notificationMessage) {
    if (!isViewAttached())
      return;

    TextMessage text = new TextMessage();
    text.setMessage(notificationMessage);
    text.setSenderId("123");
    messageList.add(0, text);
    getView().setData(messageList, true);
    getView().showContent();
  }
}
