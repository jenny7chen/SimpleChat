package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.User;

/**
 * Created by jennychen on 2017/2/21.
 */

public class UserManager {
  private static UserManager userManager = new UserManager();

  public static UserManager getInstance() {
    return userManager;
  }

  public User getUser(String userId) {
    User user = new User();

    //TODO: test use
    if (userId.equals("123")) {
      user.setName("實驗體1號");
    }
    else {
      user.setName("實驗體2號");
    }
    return user;
  }

  public String getUserPhoto(String userId) {
    return "https://images.pexels.com/photos/160107/pexels-photo-160107.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
  }
}
