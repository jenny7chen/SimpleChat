package com.seveneow.simplechat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.seveneow.simplechat.BR;
import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Message;
import com.seveneow.simplechat.utils.BaseActivity;
import com.seveneow.simplechat.viewmodel.MessageViewModel;

import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.Holder> {
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
    ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
        .from(parent.getContext()), R.layout.message_view, parent, false);
    Holder holder = new Holder(binding.getRoot());
    binding.getRoot().setLayoutParams(params);
    holder.dataBinding = binding;
    return holder;
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
      Message positionData = data.get(position);
      Message message = (Message) payloads.get(0);
      if (positionData.getId().equals(message.getId())) {
        bindData(message, holder, position);
      }
    }
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    Message message = data.get(position);
    bindData(message, holder, position);
  }

  private void bindData(Message message, Holder holder, int position) {
    Message nextMessage = null;
    Message lastMessage = null;
    if (position > 0) {
      nextMessage = data.get(position - 1);
    }
    if (position < getItemCount() - 1) {
      lastMessage = data.get(position + 1);
    }
    holder.dataBinding.setVariable(BR.MessageViewModel, new MessageViewModel(message, context, nextMessage, lastMessage));
    holder.dataBinding.executePendingBindings();
  }

  @Override
  public int getItemCount() {
    if (data == null) {
      return 0;
    }
    return data.size();
  }

  @Override
  public long getItemId(int position) {
    if (data.get(position).getDatabaseId() != -1)
      return data.get(position).getDatabaseId();
    else
      return data.get(position).getId().hashCode();
  }

  public List<Message> getData() {
    return data;
  }

  public void setData(List<Message> data) {
    this.data = data;
  }


  public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public ViewDataBinding dataBinding;

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