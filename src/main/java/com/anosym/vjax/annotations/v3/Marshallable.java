/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Marshallable {

  public static enum Option {

    MARSHALL,
    UNMARSHALL,
    BOTH,
    /**
     * this is equivalent to annotating the property with {@link Transient}
     */
    NONE;
  }

  /**
   * Determines how the marshaller handles the property.
   *
   * If it should be marshalled to xml only without unmarshalling {@link Option#MARSHALL}, or
   * unmarshalled to object property without marshalling {@link Option#UNMARSHALL} or both
   * {@link Option#BOTH}, Or if it should not be marshalled at all.
   *
   * @return
   */
  Option value() default Option.BOTH;
}
