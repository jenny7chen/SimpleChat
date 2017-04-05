package com.seveneow.simplechat.utils;

import com.google.firebase.database.DataSnapshot;
import com.seveneow.simplechat.model.Info;
import com.seveneow.simplechat.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by jennychen on 2017/3/10.
 */

public class RoomParser {
  public Info parse(DataSnapshot roomSnapShot) {
    if (roomSnapShot == null)
      return null;

    Info info = new Info();
    info.setId(roomSnapShot.getKey());

    int type = (int) (long) roomSnapShot.child("type").getValue();
    info.setType(type);

    if (roomSnapShot.hasChild("name"))
      info.setName((String) roomSnapShot.child("name").getValue());
    info.setPhoto((String) roomSnapShot.child("photo").getValue());

    ArrayList<String> members = new ArrayList<>();
    for (DataSnapshot memberShot : roomSnapShot.child("members").getChildren()) {
      members.add(memberShot.getKey());
    }
    info.setMembers(members);

    if (roomSnapShot.hasChild("latest_message")) {
      DataSnapshot latestMessageSnapShot = roomSnapShot.child("latest_message");
      info.setLatestMessageShowTime(TimeParser.getTimeStr(String.valueOf(latestMessageSnapShot.child("timestamp").getValue()), TimeFormat.CHAT_TIME_FORMAT));

      String showText = (String) latestMessageSnapShot.child("show_text").getValue();
      try {
        showText = URLDecoder.decode(showText, "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        DebugLog.printStackTrace(e);
      }
      info.setLatestMessageShowText(showText);
    }

    if (type == Info.TYPE_USER) {
      for (String userId : members) {
        if (!userId.equals(Static.userId)) {
          User user = UserManager.getInstance().getUser(userId);
          info.setName(user.getName());
        }
      }
    }

    DebugLog.e("Baaa", "room name = " + info.getName());
    return info;
  }

  public Info parse(Object[] data) {
    Info info = new Info();

    info.setId((String) data[0]);

    int type = (int) (long) data[5];
    info.setType(type);
    info.setName((String) data[2]);
    info.setPhoto((String) data[6]);

    String latestMessageTime = (String) data[3];
    info.setLatestMessageShowTime(TimeParser.getTimeStr(latestMessageTime, TimeFormat.CHAT_TIME_FORMAT));
    info.setLatestMessageShowText((String) data[4]);
    return info;
  }

}
