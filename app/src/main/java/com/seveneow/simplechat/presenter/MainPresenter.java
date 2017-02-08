package com.seveneow.simplechat.presenter;


import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.seveneow.simplechat.message.Message;

import java.util.ArrayList;

public class MainPresenter extends MvpBasePresenter<MvpView> {
  ArrayList<Message> messageList = new ArrayList<>();


}
