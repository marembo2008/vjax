/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.arrays;

import com.anosym.vjax.xml.VElement;

/**
 *
 * @author marembo
 */
public interface ArrayComponentInitializer<T> {

  /**
   * Returns the array component class based on the velement instance.
   *
   * @param componentElement
   * @return
   */
  Class<? extends T> define(VElement componentElement);
}
