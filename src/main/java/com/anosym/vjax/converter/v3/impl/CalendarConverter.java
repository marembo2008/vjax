/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.converter.v3.Converter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marembo
 */
public class CalendarConverter implements Converter<Calendar, String> {

    public final static String CALENDAR_FORMAT_PARAM = "format";
    private final String[] formats;

    ;

  public CalendarConverter(Map<String, String[]> params) {
        this.formats = params.get(CALENDAR_FORMAT_PARAM);
    }

    public CalendarConverter() {
        this.formats = new String[]{"yyyy-mm-dd"};
    }

    @Override
    public String convertFrom(Calendar value) {
        for (String format : this.formats) {
            try {
                System.out.println("Format: " + format);
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.format(value.getTime());
            } catch (Exception ex) {
                Logger.getLogger(CalendarConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public Calendar convertTo(String value) {
        if (value.trim().equalsIgnoreCase("NA")) {
            return null;
        }
        for (String format : this.formats) {
            try {
                System.out.println("Format: " + format);
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                Date date = dateFormat.parse(value);
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(0);
                instance.setTime(date);
                return instance;
            } catch (ParseException ex) {
//                Logger.getLogger(CalendarConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
