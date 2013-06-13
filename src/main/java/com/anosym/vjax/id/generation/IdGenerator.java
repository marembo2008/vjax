/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.id.generation;

/**
 *
 * @author marembo
 */
public class IdGenerator {

  /**
   * Generates an id of the specified type. Currently supported are primitives types, primitive
   * wrappers and String
   *
   * @param clazz
   * @return
   */
  public Object generateId(Class<?> clazz) {
    return System.nanoTime() + "";
  }
}
