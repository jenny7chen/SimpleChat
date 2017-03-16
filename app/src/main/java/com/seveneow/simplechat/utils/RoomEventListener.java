package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.seveneow.simplechat.model.Room;


public class RoomEventListener implements ValueEventListener {

  @Override
  public void onDataChange(DataSnapshot dataSnapshot) {
    Room room = new RoomParser().parse(dataSnapshot);
    FDBManager.onGotRoomData(room);
  }

  @Override
  public void onCancelled(DatabaseError databaseError) {

  }
}