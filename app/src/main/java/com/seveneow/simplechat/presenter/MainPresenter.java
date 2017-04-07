package com.seveneow.simplechat.presenter;


import android.content.Intent;

import com.seveneow.simplechat.model.Room;
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
  private ArrayList<Room> roomList = null;

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
    roomList = RoomManager.getInstance().getAllRooms();
    if (roomList != null && roomList.size() > 0) {
      onRoomsUpdated();
    }
    Intent intent = new Intent();
    intent.putExtra(GetServerRoomListService.PARAM_USER_ID, Static.userId);
    getView().startService(GetServerRoomListService.class, intent);
  }

  private void onRoomsUpdated(Room... room) {
    if (!isViewAttached())
      return;
    DebugLog.e("Baaa", "onRoom update");
    roomList = RoomManager.getInstance().getAllRooms();

    if (getView().getData() == null)
      getView().setDataToList((ArrayList<Object>) (Object) roomList);

    if (room != null && room.length > 0)
      getView().notifyChanged(BasicListMvpView.NOTIFY_DATA_RANGE_CHANGED, 0, roomList.size(), room[0]);
    else
      getView().notifyChanged(BasicListMvpView.NOTIFY_ALL_DATA_CHANGED);
    getView().showContent();
  }

  public void updateData(List<Room> rooms) {
    this.roomList = (ArrayList<Room>) rooms;
    getView().setDataToList((ArrayList<Object>) (Object) roomList);
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
      onRoomsUpdated((Room) event.object);
    }
  }
}
