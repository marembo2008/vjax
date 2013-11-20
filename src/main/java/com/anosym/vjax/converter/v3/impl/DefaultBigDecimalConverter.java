/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.converter.v3.Converter;
import java.math.BigDecimal;

/**
 *
 * @author marembo
 */
public class DefaultBigDecimalConverter implements Converter<BigDecimal, String> {

  @Override
  public String convertFrom(BigDecimal value) {
    if (value == null) {
      return null;
    }
    return value.toString();
  }

  @Override
  public BigDecimal convertTo(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    return new BigDecimal(value);
  }
}
