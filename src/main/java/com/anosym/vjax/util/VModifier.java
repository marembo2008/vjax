/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

/**
 *
 * @author marembo
 */
public interface VModifier<T> {

  void modify(T instance);

  boolean canModify(T instance);
}
