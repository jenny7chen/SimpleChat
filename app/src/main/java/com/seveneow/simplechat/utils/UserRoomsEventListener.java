package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserRoomsEventListener implements ValueEventListener {

  @Override
  public void onDataChange(DataSnapshot dataSnapshot) {
    ArrayList<String> roomIdList = new ArrayList<String>();
    for (DataSnapshot roomId : dataSnapshot.getChildren()) {
      roomIdList.add(roomId.getKey());
    }

    FDBManager.onGotUserRooms(roomIdList);

  }

  @Override
  public void onCancelled(DatabaseError databaseError) {

  }
}