package com.seveneow.simplechat.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennychen on 2017/3/7.
 */

public class Post {

  private String id;
  private String authorId;
  private String content;
  private ArrayList<String> comments;

  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("id", id);
    result.put("author", authorId);
    result.put("content", content);
    result.put("comments", comments);

    return result;
  }
}
