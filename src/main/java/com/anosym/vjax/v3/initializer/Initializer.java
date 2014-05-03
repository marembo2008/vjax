/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.initializer;

import com.anosym.vjax.xml.VElement;

/**
 *
 * @author marembo
 */
public interface Initializer<T> {

  /**
   * Defines the class representation from the xml element.
   *
   * The xml element can be null if the initializer can define the class without recourse to any xml
   * content.
   *
   * @param element
   * @return
   */
  Class<? extends T> define(VElement element);
}
