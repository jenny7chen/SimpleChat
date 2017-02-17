package com.seveneow.simplechat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.presenter.MainPresenter;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_interface.BasicMvpView;

import java.util.List;

public class MainActivity extends BaseActivity<BasicMvpView, MainPresenter> implements BasicMvpView {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button enterButton = (Button) findViewById(R.id.button);
    enterButton.setOnClickListener((View) -> {
      Intent intent = new Intent();
      intent.putExtra("roomId", "123");
      startActivity(ChatActivity.class, intent);
    });

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
