package com.seveneow.simplechat.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.adapter.RoomListAdapter;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.presenter.MainPresenter;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_interface.BasicListMvpView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<BasicListMvpView, MainPresenter> implements BasicListMvpView {
  private RecyclerView recyclerView;
  private RoomListAdapter adapter;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(linearLayoutManager);
    DefaultItemAnimator animator = new DefaultItemAnimator();
    animator.setAddDuration(250);
    recyclerView.setItemAnimator(animator);
    adapter = new RoomListAdapter(this);
    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);

    //TODO: test
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
    //show progressbar here
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void showContent() {
    MainViewState state = (MainViewState) viewState;
    state.setData((List<Room>) (Object) getData());
    //hide error view here
    progressBar.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showError() {

  }

  @Override
  public void loadData() {
    presenter.loadData();
  }

  @Override
  public List<Object> getData() {
    return adapter == null ? null : (List<Object>) (Object) adapter.getData();
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
  public void setDataToList(ArrayList<Object> data) {
    if (adapter != null)
      adapter.setData((List<Room>) (Object) data);
  }

  @Override
  public void notifyChanged(int type, Object... params) {
    if (adapter == null)
      return;

    if (type == BasicListMvpView.NOTIFY_ALL_DATA_CHANGED) {
      adapter.notifyDataSetChanged();
    }
    else if (type == BasicListMvpView.NOTIFY_DATA_INSERT) {
      adapter.notifyItemInserted((int) params[0]);
    }
    else if (type == BasicListMvpView.NOTIFY_DATA_RANGE_CHANGED) {
      adapter.notifyItemRangeChanged((int) params[0], (int) params[1], params[2]);
    }
  }

  @Override
  public int getItemCount() {
    return getData() == null ? 0 : getData().size();
  }

  public class MainViewState implements ViewState<BasicListMvpView> {
    final int STATE_SHOW_CONTENT = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;

    int state = STATE_SHOW_CONTENT;
    List<Room> rooms = null;

    public void setData(List<Room> rooms) {
      this.rooms = rooms;
    }

    /**
     * Is called from Mosby to apply the view state to the view.
     * We do that by calling the methods from the View interface (like the presenter does)
     */
    @Override
    public void apply(BasicListMvpView view, boolean retained) {

      switch (state) {
      case STATE_SHOW_LOADING:
        view.showLoading();
        break;

      case STATE_SHOW_ERROR:
        view.showError();
        break;

      case STATE_SHOW_CONTENT:
        presenter.updateData(rooms);
        view.showContent();
        break;
      }
    }
  }
}
