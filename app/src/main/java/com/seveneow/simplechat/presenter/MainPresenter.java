package com.seveneow.simplechat.presenter;


import android.content.Intent;

import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.service.InitRoomListService;
import com.seveneow.simplechat.service.UpdateRoomService;
import com.seveneow.simplechat.utils.BasePresenter;
import com.seveneow.simplechat.utils.DebugLog;
import com.seveneow.simplechat.utils.FDBManager;
import com.seveneow.simplechat.utils.RoomManager;
import com.seveneow.simplechat.utils.RxEvent;
import com.seveneow.simplechat.utils.RxEventBus;
import com.seveneow.simplechat.utils.Static;
import com.seveneow.simplechat.view_interface.BasicListMvpView;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<BasicListMvpView> {
  private ArrayList<Room> roomList = null;

  public void loadData() {
    if (!isViewAttached())
      return;

    getView().showLoading();
    //    FDBManager.checkDataInit();

    if (!RoomManager.getInstance().hasRoomData()) {
      Intent intent = new Intent();
      intent.putExtra(InitRoomListService.PARAM_USER_ID, Static.userId);
      getView().startService(InitRoomListService.class, intent);
    }
    else {
      onRoomsUpdated();
    }
  }

  private void onRoomsInit() {
    if (!isViewAttached())
      return;
    roomList = RoomManager.getInstance().getAllRooms();
    if (roomList != null && roomList.size() > 0) {
      onRoomsUpdated();
    }
    Intent intent = new Intent();
    intent.putExtra(UpdateRoomService.PARAM_USER_ID, Static.userId);
    getView().startService(UpdateRoomService.class, intent);
  }

  private void onRoomsUpdated() {
    if (!isViewAttached())
      return;
    DebugLog.e("Baaa", "onRoom update");
    roomList = RoomManager.getInstance().getAllRooms();

    if (getView().getData() == null || getView().getData().size() == 0)
      getView().setDataToList((List<Object>) (Object) roomList);
    getView().notifyChanged(BasicListMvpView.NOTIFY_DATA_RANGE_CHANGED, 0, getView().getItemCount(), null);
    getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    getView().showContent();
  }

  public void updateData(List<Room> rooms) {
    this.roomList = (ArrayList<Room>) rooms;
  }

  @Override
  public void onEvent(RxEvent event) {
    if (event.id == RxEvent.EVENT_ROOM_LIST_UPDATE) {
      onRoomsUpdated();
    }
    else if (event.id == RxEvent.EVENT_ROOM_LIST_INITED) {
      onRoomsInit();
    }
  }
}
