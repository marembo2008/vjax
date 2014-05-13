/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.defaulter;

/**
 * Creates a default value of the specified type.
 *
 * @author marembo
 * @param <T> the type to create its default value.
 */
public interface Defaulter<T> {

  /**
   *
   * @param type the type of the object to construct its default value.
   * @return
   */
  T getDefault(Class<? extends T> type);
}
