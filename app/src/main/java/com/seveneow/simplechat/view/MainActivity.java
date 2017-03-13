package com.seveneow.simplechat.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.adapter.RoomListAdapter;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.presenter.MainPresenter;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_interface.BasicListMvpView;
import com.seveneow.simplechat.view_interface.BasicMvpView;

import java.util.List;

public class MainActivity extends BaseActivity<BasicListMvpView, MainPresenter> implements BasicListMvpView {
  private RecyclerView recyclerView;
  private RoomListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(linearLayoutManager);
    DefaultItemAnimator animator = new DefaultItemAnimator();
    animator.setAddDuration(250);
    recyclerView.setItemAnimator(animator);
    adapter = new RoomListAdapter(this);
    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);

    FirebaseMessaging.getInstance().subscribeToTopic("123");
  }

  @NonNull
  @Override
  public MainPresenter createPresenter() {
    return new MainPresenter();
  }

  @Override
  public ViewState createViewState() {
    return new MainViewState();
  }

  @Override
  public void onNewViewStateInstance() {
    //entering at the first time
    loadData();
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void showContent() {

  }

  @Override
  public void showError() {

  }

  @Override
  public void loadData() {
    presenter.loadData();
  }

  @Override
  public List<Message> getData() {
    return null;
  }

  @Override
  public boolean listIsAtBottom() {
    return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == getData().size() - 1;
  }

  @Override
  public void scrollToBottom() {
    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(getData().size() - 1, 0);
  }

  @Override
  public void setDataToList(List<Object> data) {
    if (adapter != null)
      adapter.setData((List<Room>) (Object) data);
  }

  @Override
  public void notifyChanged(int type, Object... params) {
    if (adapter != null)
      adapter.notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public class MainViewState implements ViewState<BasicMvpView> {
    final int STATE_SHOW_CONTENT = 0;

    /**
     * Is called from Mosby to apply the view state to the view.
     * We do that by calling the methods from the View interface (like the presenter does)
     */
    @Override
    public void apply(BasicMvpView view, boolean retained) {

    }
  }
}
