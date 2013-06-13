/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.test;

import com.anosym.vjax.annotations.WhenNull;

/**
 *
 * @author variance
 */
public class Single101Data<T> {

  private T value;

  public Single101Data() {
  }

  public Single101Data(T value) {
    this.value = value;
  }

  @WhenNull(mode = WhenNull.NullMode.MARSHALL_NULL_PROPERTY)
  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }
}
