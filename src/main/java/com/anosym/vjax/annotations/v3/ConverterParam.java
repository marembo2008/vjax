/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

/**
 * Used to pass converter specific parameter values to the specified converter.
 *
 * @author marembo
 */
public @interface ConverterParam {

  /**
   * The parameter key
   *
   * @return
   */
  String key();

  /**
   * The parameter value
   *
   * @return
   */
  String value();
}
