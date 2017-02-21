package com.seveneow.simplechat.utils;

import com.seveneow.simplechat.model.User;

/**
 * Created by jennychen on 2017/2/21.
 */

public class UserManager {
  private static UserManager userManager = new UserManager();
  public static UserManager getInstance(){
    return userManager;
  }

  public User getUser(){
    return new User();
  }

  public String getUserPhoto(String userId){
    return "https://images.pexels.com/photos/160107/pexels-photo-160107.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
  }
}
