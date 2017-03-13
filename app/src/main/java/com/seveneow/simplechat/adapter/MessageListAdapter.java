package com.seveneow.simplechat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.view_custom.MessageView;

import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.Holder> {
  private static final int MESSAGE_FROM_ME = 0;
  private static final int MESSAGE_FROM_OTHERS = 1;

  private List<Message> data = null;
  private Context context;

  public MessageListAdapter(Context context) {
    this.context = context;
  }

  private static Activity scanForActivity(Context cont) {
    if (cont == null)
      return null;
    else if (cont instanceof Activity)
      return (Activity) cont;
    else if (cont instanceof ContextWrapper)
      return scanForActivity(((ContextWrapper) cont).getBaseContext());

    return null;
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    View view;
    view = new MessageView(context, viewType == MESSAGE_FROM_ME);
    view.setLayoutParams(params);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(Holder holder, int position, List<Object> payloads) {
    if (payloads.isEmpty()) {
      // payloads is empty, update the whole ViewHolder
      onBindViewHolder(holder, position);
    }
    else {
      //TODO: add partial update here
      // when payloads is not empty, update view
      Message message = (Message) payloads.get(0);
      if (message.getMessageId().equals(holder.message.getMessageId())) {
        onBindViewHolder(holder, position);
      }
    }
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    Message message = data.get(position);
    holder.message = message;
    MessageView view = (MessageView) holder.itemView;
    view.setMessage(message);
  }

  @Override
  public int getItemCount() {
    if (data == null) {
      return 0;
    }
    return data.size();
  }

  @Override
  public int getItemViewType(int position) {
    return data.get(position).isFromMe() ? MESSAGE_FROM_ME : MESSAGE_FROM_OTHERS;
  }

  @Override
  public long getItemId(int position) {
    return data.get(position).getMessageId().hashCode();
  }

  public List<Message> getData() {
    return data;
  }

  public void setData(List<Message> data) {
    this.data = data;
  }


  public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public Message message;

    public Holder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Context context = v.getContext();
      Activity activity = scanForActivity(context);
      if (activity != null)
        ((BaseActivity) activity).hideSoftKeyboard();
    }


    @Override
    public boolean onLongClick(View view) {
      Toast.makeText(context, "你按我", Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}