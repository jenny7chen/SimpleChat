package com.seveneow.simplechat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seveneow.simplechat.R;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.view.ChatActivity;

import java.util.List;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.Holder> {
  private List<Room> data = null;
  private Context context;

  public RoomListAdapter(Context context) {
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
    LayoutInflater inflater = LayoutInflater.from(context);
    view = inflater.inflate(R.layout.room_list_view, parent, false);
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
      Room room = (Room) payloads.get(0);
      if (room.getId().equals(holder.room.getId())) {
        onBindViewHolder(holder, position);
      }
    }
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    Room room = data.get(position);
    holder.room = room;
    View view = holder.itemView;
    TextView textView = (TextView) view.findViewById(R.id.room_name_view);
    textView.setText(room.getName());
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
    return 0;
  }

  @Override
  public long getItemId(int position) {
    return Long.valueOf(data.get(position).getId());
  }

  public List<Room> getData() {
    return data;
  }

  public void setData(List<Room> data) {
    this.data = data;
  }


  public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public Room room;

    public Holder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Context context = v.getContext();
      Activity activity = scanForActivity(context);
      if (activity != null) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("roomId", room.getId());
        activity.startActivity(intent);
      }
    }


    @Override
    public boolean onLongClick(View view) {
      Toast.makeText(context, "你按我", Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}