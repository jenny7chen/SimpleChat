package com.seveneow.simplechat.utils;

import android.os.SystemClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parse date string to display time
 */

public class TimeParser {
  public static String getTimeStr(long milliseconds, String template) {
    Date date = new Date();
    date.setTime(milliseconds);
    return convertDateToStr(date, template);
  }

  public static String getTimeStr(String timeStr, String template) {
    Date date = convertStrToDate(timeStr, template);
    return convertDateToStr(date, template);
  }

  public static String getTimeStr(Date date, String template){
    return convertDateToStr(date, template);
  }


  /**
   * convert between date and String
   **/

  private static Date convertStrToDate(String dateString, String template) {
    return convertStrToDate(dateString, template, Locale.US);
  }

  private static String convertDateToStr(Date date, String template) {
    return convertDateToStr(date, template, Locale.US);
  }

  private static Date convertStrToDate(String dateString, String template, Locale locale) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(template, locale);
    Date convertedDate = null;
    try {
      convertedDate = dateFormat.parse(dateString);
    }
    catch (ParseException e) {
      DebugLog.printStackTrace(e);
    }
    return convertedDate;
  }

  private static String convertDateToStr(Date date, String template, Locale locale) {
    if (date == null)
      return "";
    SimpleDateFormat formatter = new SimpleDateFormat(template, locale);

    //TODO: is this the only place need to set the time zone?
    formatter.setTimeZone(TimeZone.getDefault());
    return formatter.format(date);
  }

  public static String getCurrentTimeString(){
    long timeMillis = System.currentTimeMillis();
    return String.valueOf(timeMillis);
  }


}
