package com.seveneow.simplechat.view_custom;

import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.seveneow.simplechat.R;


/* TODO: maybe use this for custom chat snack bar */
public class ChatSnackBar extends BaseTransientBottomBar<ChatSnackBar> {

  /**
   * Constructor for the transient bottom bar.
   *
   * @param parent   The parent for this transient bottom bar.
   * @param content  The content view for this transient bottom bar.
   * @param callback The content view callback for this transient bottom bar.
   */
  private ChatSnackBar(ViewGroup parent, View content, ContentViewCallback callback) {
    super(parent, content, callback);
  }

  public static ChatSnackBar make(@NonNull ViewGroup parent, @Duration int duration) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final View content = inflater.inflate(R.layout.chat_snack_bar_view, parent, false);
    final ContentViewCallback viewCallback = new ContentViewCallback(content);
    final ChatSnackBar customSnackbar = new ChatSnackBar(parent, content, viewCallback);
    customSnackbar.setDuration(duration);
    return customSnackbar;
  }

  public ChatSnackBar setText(CharSequence text) {
    TextView textView = (TextView) getView().findViewById(R.id.snackbar_text);
    textView.setText(text);
    return this;
  }

  public ChatSnackBar setAction(CharSequence text, final View.OnClickListener listener) {
    Button actionView = (Button) getView().findViewById(R.id.snackbar_action);
    actionView.setText(text);
    actionView.setVisibility(View.VISIBLE);
    actionView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        listener.onClick(view);
        // Now dismiss the Snackbar
        dismiss();
      }
    });
    return this;
  }

  private static class ContentViewCallback implements BaseTransientBottomBar.ContentViewCallback {

    private View content;

    public ContentViewCallback(View content) {
      this.content = content;
    }

    @Override
    public void animateContentIn(int delay, int duration) {
      ViewCompat.setScaleY(content, 0f);
      ViewCompat.animate(content).scaleY(1f).setDuration(duration).setStartDelay(delay);
    }

    @Override
    public void animateContentOut(int delay, int duration) {
      ViewCompat.setScaleY(content, 1f);
      ViewCompat.animate(content).scaleY(0f).setDuration(duration).setStartDelay(delay);
    }
  }
}