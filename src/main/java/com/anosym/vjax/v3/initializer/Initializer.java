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

  Class<? extends T> define(VElement element);
}
