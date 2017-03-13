package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.seveneow.simplechat.model.Room;

import java.util.ArrayList;

/**
 * Created by jennychen on 2017/3/10.
 */

public class RoomParser {
  public Room parse(DataSnapshot roomSnapShot) {
    if (roomSnapShot == null)
      return null;

    Room room = new Room();
    room.setId(roomSnapShot.getKey());
    room.setName((String) roomSnapShot.child("name").getValue());
    room.setPhoto((String) roomSnapShot.child("photo").getValue());

    ArrayList<String> members = new ArrayList<>();
    for (DataSnapshot memberShot : roomSnapShot.child("members").getChildren()) {
      members.add(memberShot.getKey());
    }
    room.setMembers(members);

    DebugLog.e("Baaa", "room name = " + room.getName());
    return room;
  }

}
