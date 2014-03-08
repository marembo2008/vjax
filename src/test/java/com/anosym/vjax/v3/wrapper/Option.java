/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.v3.AsIs;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.Wrapped;
import com.anosym.vjax.converter.v3.impl.CalendarConverter;
import java.util.Calendar;

/**
 *
 * @author marembo
 */
@Wrapped
public class Option {

  private int type;
  private String name;
  @Converter(CalendarConverter.class)
  private Calendar time;
  @Converter(CalendarConverter.class)
  @AsIs
  private Calendar date;
  private Require required;

}
