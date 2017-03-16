package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.User;

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

    int type = (int) (long) roomSnapShot.child("type").getValue();
    room.setType(type);

    if (roomSnapShot.hasChild("name"))
      room.setName((String) roomSnapShot.child("name").getValue());
    room.setPhoto((String) roomSnapShot.child("photo").getValue());

    ArrayList<String> members = new ArrayList<>();
    for (DataSnapshot memberShot : roomSnapShot.child("members").getChildren()) {
      members.add(memberShot.getKey());
    }
    room.setMembers(members);

    if(roomSnapShot.hasChild("latest_message")){
      DataSnapshot latestMessageSnapShot = roomSnapShot.child("latest_message");
      room.setLatestMessageShowTime(TimeParser.getTimeStr(((String)latestMessageSnapShot.child("timestamp").getValue()),TimeFormat.CHAT_TIME_FORMAT));
      room.setLatestMessageShowText((((String) latestMessageSnapShot.child("show_text").getValue())));
    }

    if (type == Room.TYPE_USER) {
      for (String userId : members) {
        if (!userId.equals(Static.userId)) {
          User user = UserManager.getInstance().getUser(userId);
          room.setName(user.getName());
        }
      }
    }

    DebugLog.e("Baaa", "room name = " + room.getName());
    return room;
  }

}
