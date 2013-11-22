/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class CalendarConverterTest {

  public CalendarConverterTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testWithFormat() {
    Calendar date = Calendar.getInstance();//FormattedCalendar.parseISODate("2011-09-27 23:00:00");
    date.setTimeInMillis(0);
    date.set(2008, 5, 14, 23, 00, 00);
    String format = "d MMM, yyyy HH:mm:ss";
    String expected = "14 Jun, 2008 23:00:00";
    Map<String, String[]> params = new HashMap<String, String[]>();
    params.put(CalendarConverter.CALENDAR_FORMAT_PARAM, new String[]{format});
    CalendarConverter cc = new CalendarConverter(params);
    String actual = cc.convertFrom(date);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testWithDateOnlyFormat() {
    Calendar date = Calendar.getInstance();//FormattedCalendar.parseISODate("2011-09-27 23:00:00");
    date.setTimeInMillis(0);
    date.set(2008, 5, 4, 23, 00, 00);
    String format = "d MMM yyyy";
    String expected = "4 Jun 2008";
    Map<String, String[]> params = new HashMap<String, String[]>();
    params.put(CalendarConverter.CALENDAR_FORMAT_PARAM, new String[]{format});
    CalendarConverter cc = new CalendarConverter(params);
    String actual = cc.convertFrom(date);
    Assert.assertEquals(expected, actual);
  }
}