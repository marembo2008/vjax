/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.converter.v3.Converter;
import java.util.Calendar;

/**
 *
 * @author marembo
 */
public class TimeMillisCalendarConverter implements Converter<Calendar, Long> {

  @Override
  public Long convertFrom(Calendar value) {
    return value != null ? value.getTimeInMillis() : 0;
  }

  @Override
  public Calendar convertTo(Long value) {
    if (value == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(value);
    return cal;
  }

}
