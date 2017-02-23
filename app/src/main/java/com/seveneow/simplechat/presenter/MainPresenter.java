package com.seveneow.simplechat.presenter;


import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.view_interface.BasicListMvpView;

public class MainPresenter extends BasePresenter<BasicListMvpView> {

  public void loadData() {
    RoomManager.getInstance().addTestData();

  }

  @Override
  public void onEvent(RxEvent event) {

  }
}
