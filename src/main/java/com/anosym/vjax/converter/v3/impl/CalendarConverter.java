/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.converter.v3.Converter;
import java.util.Calendar;

/**
 *
 * @author marembo
 */
public class CalendarConverter implements Converter<Calendar, Long> {

  @Override
  public Long convertFrom(Calendar value) {
    return value.getTimeInMillis();
  }

  @Override
  public Calendar convertTo(Long value) {
    Calendar now = Calendar.getInstance();
    now.setTimeInMillis(value);
    return now;
  }
}
