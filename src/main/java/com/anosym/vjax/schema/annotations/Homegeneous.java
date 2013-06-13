/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * To define an element where some sub-element occurs repeatedly, we make use of the optional
 * attributes minOccurs and maxOccurs. Various combinations are possible, permitting the definition
 * of a list that may be empty, may contain any number of elements, or a fixed number.
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Homegeneous {

  /**
   * The minimum occurence of the specified element By default this is zero
   *
   * @return
   */
  int minOccurs() default 0;

  /**
   * The maximum occurence of the element By default, this is -1 to specify that the element
   * occurence is unbounded
   *
   * @return
   */
  int maxOccurs() default -1;
}
