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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.adapter.MessageListAdapter;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.presenter.ChatPresenter;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_custom.MessageEditorView;
import com.seveneow.simplechat.view_interface.BasicListMvpView;
import com.seveneow.simplechat.view_interface.ChatListMvpView;

import java.util.List;
import java.util.Random;


public class ChatActivity extends BaseActivity<ChatListMvpView, ChatPresenter> implements ChatListMvpView {
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

  @Override
  public void onNewViewStateInstance() {
    loadData();
    //entering at the first time
  }

  @Override
  public void setTitle(String titleText) {
    getSupportActionBar().setTitle(titleText);
  }

  @NonNull
  @Override
  public ChatPresenter createPresenter() {
    return new ChatPresenter();
  }

  @Override
  protected void onPause() {
    hideSoftKeyboard();
    messageEditorView.removeFocus();
    super.onPause();
  }

  @Override
  public void showLoading() {
    //show progressbar here
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void showContent() {
    ChatViewState state = (ChatViewState) viewState;
    state.setData((List<Message>) (Object) getData());
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
  public boolean listIsAtBottom() {
    return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;
  }

  @Override
  public void scrollToBottom() {
    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
  }

  @Override
  public void showSnackBar(int messageId) {
    Snackbar snackbar = Snackbar
        .make(snackBarLayout, getString(messageId), Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(R.string.snack_check_got_message), (view) -> recyclerView.smoothScrollToPosition(0));
    snackbar.getView().setBackgroundColor(Color.DKGRAY);
    snackbar.show();
  }

  @Override
  public List<Object> getData() {
    return adapter == null ? null : (List<Object>) (Object) adapter.getData();
  }

  @Override
  public int getItemCount() {
    if (adapter == null)
      return 0;
    return adapter.getItemCount();
  }

  @Override
  public void setDataToList(List<Object> messages) {
    if (adapter != null)
      adapter.setData((List<Message>) (Object) messages);
  }

  @Override
  public void loadData() {
    presenter.setRoomId(getIntent());
    presenter.initRoomData();
  }

  @Override
  public ChatViewState createViewState() {
    return new ChatViewState();
  }

  public class ChatViewState implements ViewState<ChatListMvpView> {
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
    public void apply(ChatListMvpView view, boolean retained) {

      switch (state) {
      case STATE_SHOW_LOADING:
        view.showLoading();
        break;

      case STATE_SHOW_ERROR:
        view.showError();
        break;

      case STATE_SHOW_CONTENT:
        presenter.updateViewData(messageList, false, false);
        view.showContent();
        break;
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.chat_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    //TODO: for test use
    if (item.getItemId() == R.id.add_message) {
      presenter.sendMessage();
    }
    return super.onOptionsItemSelected(item);
  }

  private String getRandomMessage() {
    String[] message = {"Hello", "你好啊", "天氣真好", "你喜歡海底鴨嗎？", "走吧去吃牛排飯", "聽說那部電影很好看耶", "好熱喔", "你是豬嗎？", "我覺得我的鼻子癢癢的", "外面下大雨惹哭哭", "肚子好餓", "我想睡覺"};
    int ranInt = new Random().nextInt(message.length);
    return message[ranInt];
  }
}
