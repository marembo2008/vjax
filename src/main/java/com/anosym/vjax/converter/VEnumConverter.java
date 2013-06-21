/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;

/**
 * The default conversion is to return the lowercase name of the enum instance.
 *
 * @author marembo
 */
public class VEnumConverter extends VConverter<Enum<?>> {

  private Class enumClass;

  @Override
  public Enum convert(String value) throws VConverterBindingException {
    return Enum.valueOf(enumClass, value.toUpperCase());
  }

  @Override
  public String convert(Enum<?> value) throws VConverterBindingException {
    return value.name().toLowerCase();
  }

  @Override
  public boolean isConvertCapable(Class clazz) {
    enumClass = clazz;
    return Enum.class.isAssignableFrom(clazz);
  }
}
