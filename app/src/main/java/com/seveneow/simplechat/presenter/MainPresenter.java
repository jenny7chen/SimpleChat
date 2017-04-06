package com.seveneow.simplechat.presenter;


import android.content.Intent;

import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.service.GetServerRoomListService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.view_interface.BasicListMvpView;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<BasicListMvpView> {
  private ArrayList<Info> infoList = null;

  public void loadData() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    if (RoomManager.getInstance().hasRoomData()) {
      onRoomsUpdated();
    }
  }

  private void onRoomsInit() {
    if (!isViewAttached())
      return;
    infoList = RoomManager.getInstance().getAllRooms();
    if (infoList != null && infoList.size() > 0) {
      onRoomsUpdated();
    }
    Intent intent = new Intent();
    intent.putExtra(GetServerRoomListService.PARAM_USER_ID, Static.userId);
    getView().startService(GetServerRoomListService.class, intent);
  }

  private void onRoomsUpdated(Info... info) {
    if (!isViewAttached())
      return;
    DebugLog.e("Baaa", "onRoom update");
    infoList = RoomManager.getInstance().getAllRooms();

    if (getView().getData() == null)
      getView().setDataToList((ArrayList<Object>) (Object) infoList);

    if (info != null && info.length > 0)
      getView().notifyChanged(BasicListMvpView.NOTIFY_DATA_RANGE_CHANGED, 0, infoList.size(), info[0]);
    else
      getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    getView().showContent();
  }

  public void updateData(List<Info> infos) {
    this.infoList = (ArrayList<Info>) infos;
    getView().setDataToList((ArrayList<Object>) (Object) infoList);
  }

  @Override
  public void onEvent(RxEvent event) {
    if (event.id == RxEvent.EVENT_ROOM_LIST_UPDATE || event.id == RxEvent.EVENT_ROOM_LATEST_MESSAGE_SHOW_TEXT_UPDATE) {
      onRoomsUpdated();
    }
    else if (event.id == RxEvent.EVENT_ROOM_LIST_INIT) {
      onRoomsInit();
    }
    else if (event.id == RxEvent.EVENT_ROOM_ADDED) {
      onRoomsUpdated((Info) event.object);
    }
  }
}
