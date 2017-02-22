package com.seveneow.simplechat.view;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.adapter.MessageListAdapter;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.presenter.ChatPresenter;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_custom.MessageEditorView;
import com.seveneow.simplechat.view_interface.ChatMvpView;

import java.util.List;


public class ChatActivity extends BaseActivity<ChatMvpView, ChatPresenter> implements ChatMvpView {
  private CoordinatorLayout snackBarLayout;
  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private MessageEditorView messageEditorView;
  private MessageListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    findViews();
    initViews();
    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    int memoryClass = am.getMemoryClass();
    //    Log.v("onCreate", "memoryClass:" + Integer.toString(memoryClass));
  }

  private void findViews() {
    snackBarLayout = (CoordinatorLayout) findViewById(R.id.snackbar_layout);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    messageEditorView = (MessageEditorView) findViewById(R.id.message_editor_view);
  }

  private void initViews() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
    recyclerView.setLayoutManager(linearLayoutManager);
    DefaultItemAnimator animator = new DefaultItemAnimator();
    animator.setAddDuration(250);
    recyclerView.setItemAnimator(animator);
    adapter = new MessageListAdapter(this);
    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);
    messageEditorView.setListener((message) -> {
      presenter.sendMessage(message);
    });
  }

  @NonNull
  @Override
  public ChatPresenter createPresenter() {
    return new ChatPresenter();
  }

  @Override
  public void showLoading() {
    //show progressbar here
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void showContent() {
    ChatViewState state = (ChatViewState) viewState;
    state.setData(getData());
    //hide error view here
    progressBar.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void showError() {
    //show error view here
    progressBar.setVisibility(View.GONE);
    recyclerView.setVisibility(View.GONE);
  }

  @Override
  //combine the two TODO: move some logic to presenter
  public synchronized void updateData(List<Message> updatedData, boolean isSingleMessage, boolean isInsert) {
    if (isSingleMessage) {
      boolean isAtBottom = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;

      if (isInsert) {
        adapter.notifyItemInserted(0);
        if (isAtBottom || updatedData.get(0).isFromMe())
          ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        else if (!updatedData.get(0).isFromMe()) {
          showSnackBar(getString(R.string.snack_got_new_message));
        }

      }
      else {
        adapter.notifyItemRangeChanged(0, adapter.getItemCount(), updatedData.get(0));
        if (isAtBottom) {
          ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
        }
      }
    }
    else {
      if (adapter.getData() == null)
        adapter.setData(updatedData);
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void showSnackBar(String message) {
    Snackbar snackbar = Snackbar
        .make(snackBarLayout, message, Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(R.string.snack_check_got_message), (view) -> recyclerView.smoothScrollToPosition(0));
    snackbar.getView().setBackgroundColor(Color.DKGRAY);
    snackbar.show();
  }

  @Override
  public List<Message> getData() {
    return adapter == null ? null : adapter.getData();
  }

  @Override
  public void loadData() {
    presenter.fetchMessages();
  }

  @Override
  public ChatViewState createViewState() {
    return new ChatViewState();
  }

  @Override
  public void onNewViewStateInstance() {
    presenter.setRoomData(getIntent());
    loadData();
    //entering at the first time
  }

  public class ChatViewState implements ViewState<ChatMvpView> {
    final int STATE_SHOW_CONTENT = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;

    int state = STATE_SHOW_CONTENT;
    List<Message> messageList = null;

    public void setData(List<Message> messageList) {
      this.messageList = messageList;
    }

    /**
     * Is called from Mosby to apply the view state to the view.
     * We do that by calling the methods from the View interface (like the presenter does)
     */
    @Override
    public void apply(ChatMvpView view, boolean retained) {

      switch (state) {
      case STATE_SHOW_LOADING:
        view.showLoading();
        break;

      case STATE_SHOW_ERROR:
        view.showError();
        break;

      case STATE_SHOW_CONTENT:
        view.updateData(messageList, false, false);
        view.showContent();
        break;
      }
    }
  }

}
