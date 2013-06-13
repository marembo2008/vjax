/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The contents generated will be encapsulated inside a CDATA section.
 *
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CData {

  public static enum Option {

    /**
     * Should the parse still parse the object tree, but encapsulate it inside a CDATA section.
     * Since there cannot be nested CDATA sections, this may end up throwing an
     * illegalargumentexception.
     */
    XML,
    /**
     * The parser will automatically call the toString method of the current object, unless there is
     * a converter, for which the result toString method will be used.
     */
    TEXT;
  }

  /**
   * Option for this CDATA section. Default is text, implying that the toString method will be
   * called automatically.
   *
   * @return
   */
  Option option() default Option.TEXT;
}
