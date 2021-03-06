/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper.pojo;

import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.GenerateWrapper;
import com.anosym.vjax.converter.v3.impl.CalendarConverter;
import java.util.Calendar;

/**
 *
 * @author marembo
 */
@GenerateWrapper
public class Option {

  private int type;
  private String name;
  @Converter(CalendarConverter.class)
  private Calendar time;
  @Converter(CalendarConverter.class)
  private Calendar date;
  private Require required;

}
