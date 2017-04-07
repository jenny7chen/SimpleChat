package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.seveneow.simplechat.model.Room;
import com.seveneow.simplechat.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    if (roomSnapShot.hasChild("latest_message")) {
      DataSnapshot latestMessageSnapShot = roomSnapShot.child("latest_message");
      room.setLatestMessageShowTime(TimeParser.getTimeStr(String.valueOf(latestMessageSnapShot.child("timestamp").getValue()), TimeFormat.CHAT_TIME_FORMAT));

      String showText = (String) latestMessageSnapShot.child("show_text").getValue();
      try {
        showText = URLDecoder.decode(showText, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        DebugLog.printStackTrace(e);
      }
      room.setLatestMessageShowText(showText);
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

  public Room parse(Object[] data) {
    Room room = new Room();

    room.setId((String) data[0]);

    int type = (int) (long) data[5];
    room.setType(type);
    room.setName((String) data[2]);
    room.setPhoto((String) data[6]);

    String latestMessageTime = (String) data[3];
    room.setLatestMessageShowTime(TimeParser.getTimeStr(latestMessageTime, TimeFormat.CHAT_TIME_FORMAT));
    room.setLatestMessageShowText((String) data[4]);
    return room;
  }

}
