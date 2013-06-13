/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author Marembo
 */
public class VDateConverter extends VConverter<Date> {

    public static String ISOString(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
            calendar.set(1972, 0, 1);
        }
        String year = calendar.get(Calendar.YEAR) + "",
                mon = (calendar.get(Calendar.MONTH) + 1) + "",
                day = calendar.get(Calendar.DATE) + "",
                hrs = calendar.get(Calendar.HOUR_OF_DAY) + "",
                mins = calendar.get(Calendar.MINUTE) + "",
                secs = calendar.get(Calendar.SECOND) + "";
        int millis = calendar.get(Calendar.MILLISECOND);
        if (year.length() != 4) {
            year = "20" + year;
        }
        if (mon.length() != 2) {
            mon = "0" + mon;
        }
        if (day.length() != 2) {
            day = "0" + day;
        }
        if (hrs.length() != 2) {
            hrs = "0" + hrs;
        }
        if (mins.length() != 2) {
            mins = "0" + mins;
        }
        if (secs.length() != 2) {
            secs = "0" + secs;
        }
        String str = year + "-" + mon + "-" + day + " " + hrs + ":" + mins + ":" + secs;
        if (millis > 0) {
            str += "." + millis;
        }
        return str;
    }

    public Date convert(String value) throws VConverterBindingException {
        //parse the date
        String[] date_time = value.split(" "); //date and time separated by spaces
        if (date_time.length != 2) {
            throw new VConverterBindingException("Invalid Date Style:  " + value);
        }
        String date = date_time[0];
        String time = date_time[1];
        String[] date_array = date.split("-");
        if (date_array.length != 3) {
            throw new VConverterBindingException("invalid date format: " + date);
        }
        int yr, mn, dy;
        try {
            yr = Integer.parseInt(date_array[0]);
            mn = Integer.parseInt(date_array[1]);
            dy = Integer.parseInt(date_array[2]);
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
        String[] time_array = time.split(":");
        if (time_array.length != 3) {
            throw new VConverterBindingException("invalid time component for date: " + time);
        }
        int hr, min, sec, millis = 0;
        try {
            hr = Integer.parseInt(time_array[0]);
            min = Integer.parseInt(time_array[1]);
            String secs = time_array[2];
            if (secs.contains(".")) {
                String[] s_millis = secs.split(Pattern.quote("."));
                sec = Integer.parseInt(s_millis[0]);
                millis = Integer.parseInt(s_millis[1]);
            } else {
                sec = Integer.parseInt(secs);
            }
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.set(yr, mn - 1, dy, hr, min, sec);
        cal.set(Calendar.MILLISECOND, millis);
        return cal.getTime();
    }

    public String convert(Date value) throws VConverterBindingException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(value);
            return ISOString(cal);
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<Date> clazz) {
        //ensure that the clazz is a date
        return clazz == Date.class;
    }
}
