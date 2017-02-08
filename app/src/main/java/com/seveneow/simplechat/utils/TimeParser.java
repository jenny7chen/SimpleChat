package com.seveneow.simplechat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Parse date string to display time
 */

public class TimeParser {
  public String getTimeStr(long milliseconds, String template) {
    Date date = new Date();
    date.setTime(milliseconds);
    return convertDateToStr(date, template);
  }

  public String getTimeStr(String timeStr, String template) {
    Date date = convertStrToDate(timeStr, template);
    return convertDateToStr(date, template);
  }

  public String getTimeStr(Date date, String template){
    return convertDateToStr(date, template);
  }


  /**
   * convert between date <-> String
   **/

  private Date convertStrToDate(String dateString, String template) {
    return convertStrToDate(dateString, template, Locale.US);
  }

  private String convertDateToStr(Date date, String template) {
    return convertDateToStr(date, template, Locale.US);
  }

  private Date convertStrToDate(String dateString, String template, Locale locale) {
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

  private String convertDateToStr(Date date, String template, Locale locale) {
    if (date == null)
      return "";
    SimpleDateFormat formatter = new SimpleDateFormat(template, locale);
    return formatter.format(date);
  }
}
