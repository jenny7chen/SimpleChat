package com.seveneow.simplechat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.presenter.MainPresenter;

public class MainActivity extends MvpViewStateActivity<MvpView, MainPresenter> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button enterButton = (Button) findViewById(R.id.button);
    enterButton.setOnClickListener((View) -> startActivity(new Intent(this, ChatActivity.class)));
    setRetainInstance(true);//when using viewstate needs to turn this on
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

  }

  public class MainViewState implements ViewState<MvpView> {
    final int STATE_SHOW_CONTENT = 0;

    /**
     * Is called from Mosby to apply the view state to the view.
     * We do that by calling the methods from the View interface (like the presenter does)
     */
    @Override
    public void apply(MvpView view, boolean retained) {

    }
  }
}
