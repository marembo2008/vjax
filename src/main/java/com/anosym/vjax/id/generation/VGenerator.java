/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.id.generation;

/**
 * Id generator provider to be used to generate id.
 *
 * @author marembo
 */
public interface VGenerator<T> {

  T generate();
}
