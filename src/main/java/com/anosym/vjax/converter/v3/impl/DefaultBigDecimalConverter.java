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

  public String convertFrom(BigDecimal value) {
    return value.toString();
  }

  public BigDecimal convertTo(String value) {
    return new BigDecimal(value);
  }
}
