package com.seveneow.simplechat.view_custom;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seveneow.simplechat.R;

public class MessageEditorView extends RelativeLayout {
  private MessageEditorListener listener = null;
  private EditText editText;
  private RelativeLayout sendButton;

  public MessageEditorView(Context context) {
    super(context);
    initRootView();
  }

  public MessageEditorView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initRootView();
  }

  public MessageEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initRootView();
  }

  private void initRootView() {
    removeAllViews();
    LayoutInflater.from(getContext()).inflate(R.layout.message_editor_view, this, true);
    initListeners();
  }

  private void initListeners() {
    sendButton = (RelativeLayout) findViewById(R.id.send_button);
    editText = (EditText) findViewById(R.id.editor_text);
    sendButton.setOnClickListener((view) -> {
      if (listener != null) {
        String message = editText.getText().toString();
        if (message.isEmpty())
          return;
        listener.onSendButtonPressed(message);
        editText.setText("");
      }
    });
  }

  public void setListener(MessageEditorListener listener) {
    this.listener = listener;
  }

  public void setHint(String hintText) {
    editText = (EditText) findViewById(R.id.editor_text);
    editText.setHint(hintText);
  }

  //  public void setSendButtonImage(Bitmap bitmap) {
  //    RelativeLayout sendButton = (RelativeLayout) findViewById(R.id.send_buton);
  //    sendButton.setImageBitmap(bitmap);
  //  }

  public void setSendButtonColor(int color) {
    sendButton.setBackgroundColor(color);
  }

  public interface MessageEditorListener {
    public void onSendButtonPressed(String message);

  }

  public void removeFocus() {
    editText.clearFocus();
  }

  public void giveFocus() {
    editText.requestFocus();
  }

}
