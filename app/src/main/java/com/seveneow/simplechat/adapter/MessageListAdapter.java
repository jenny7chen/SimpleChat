package com.seveneow.simplechat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.seveneow.simplechat.message.Message;
import com.seveneow.simplechat.view_custom.ImageMessageView;
import com.seveneow.simplechat.view_custom.MessageView;
import com.seveneow.simplechat.view_custom.StickerMessageView;
import com.seveneow.simplechat.view_custom.TextMessageView;

import java.util.List;


public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.Holder> {
  private List<Message> data = null;
  private LayoutInflater inflater;
  private Context context;

  public MessageListAdapter(Context context) {
    this.context = context;
    inflater = LayoutInflater.from(context);
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
    switch (viewType) {
    case Message.TYPE_TEXT:
      view = new TextMessageView(context);
      view.setLayoutParams(params);
      return new Holder(view);

    case Message.TYPE_IMAGE:
      view = new ImageMessageView(context);
      view.setLayoutParams(params);
      return new Holder(view);

    case Message.TYPE_STICKER:
      view = new StickerMessageView(context);
      view.setLayoutParams(params);
      return new Holder(view);

    default:
      view = new TextMessageView(context);
      view.setLayoutParams(params);
      return new Holder(view);
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
    return data.get(position).getType();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<Message> getData() {
    return data;
  }

  public void setData(List<Message> data) {
    this.data = data;
    notifyDataSetChanged();
  }


  public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public Message message;

    public Holder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      //onClick event

      //      Context context = v.getContext();
      //      Intent intent = new Intent(context, VideoActivity.class);
      //      intent.putExtra(VideoActivity.BUNDLE_VIDEO_SN, volume.videoSn);
      //      context.startActivity(intent);
      //
      //      int gaAction = R.string.ga_action_volume;
      //      Activity activity = scanForActivity(context);
      //      if (activity != null)
      //        ((BaseActivity) activity).gaSendEvent(R.string.ga_category_video, gaAction);
    }


    @Override
    public boolean onLongClick(View view) {
      Toast.makeText(context, "你按我", Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}