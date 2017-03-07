package com.seveneow.simplechat.presenter;


import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.view_interface.BasicListMvpView;

import java.util.List;

public class MainPresenter extends BasePresenter<BasicListMvpView> {

  public void loadData() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //get data here using asynchttp lib
    getView().setDataToList((List<Object>) (Object) RoomManager.getInstance().getAllRooms());
    getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    getView().showContent();
  }

  @Override
  public void onEvent(RxEvent event) {

  }
}
