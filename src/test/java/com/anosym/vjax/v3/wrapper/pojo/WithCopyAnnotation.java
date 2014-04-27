/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper.pojo;

import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.CopyAnnotation;
import com.anosym.vjax.annotations.v3.Wrapped;
import com.anosym.vjax.converter.v3.impl.CalendarConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Calendar;

/**
 *
 * @author marembo
 */
@Wrapped
public class WithCopyAnnotation {

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public static @interface Options {

    int[] options();
  }
  private String name;
  private int id;
  @Converter(CalendarConverter.class)
  @CopyAnnotation(value = {Converter.class})
  private Calendar time;
  @Options(options = {9, 84, 7374, 88384})
  @CopyAnnotation(value = {Options.class})
  private int options;
}
