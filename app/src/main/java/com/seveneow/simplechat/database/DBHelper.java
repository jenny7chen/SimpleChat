package com.seveneow.simplechat.database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by jennychen on 2017/2/14.
 */

public class DBHelper extends SQLiteOpenHelper {
  private final static int _DBVersion = 1;
  private final static String _DBName = "SampleList.db";

  public DBHelper(Context context) {
    super(context, _DBName, null, _DBVersion);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    initTable(db);
  }

  private void initTable(SQLiteDatabase db) {
    db.execSQL(Queries.CREATE_USER_TABLE);
    db.execSQL(Queries.CREATE_MESSAGE_TABLE);
    db.execSQL(Queries.CREATE_ROOM_TABLE);
  }

  private void insert() {
    //    PreparedStatement stm = c.prepareStatement("UPDATE user_table SET name=? WHERE id=?");
    //    stm.setString(1, "the name");
    //    stm.setInt(2, 345);
    //    stm.executeUpdate();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    //TODO: onUpgrade what to do?
    db.execSQL(Queries.DROP_USER_TABLE);
    db.execSQL(Queries.DROP_MESSAGE_TABLE);
    db.execSQL(Queries.DROP_ROOM_TABLE);
  }
}
