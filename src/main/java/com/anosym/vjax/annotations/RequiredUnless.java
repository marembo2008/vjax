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
 * An child element or property is required, unless a specified child element or property is not
 * null.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface RequiredUnless {

  /**
   * The property to check for nullity.
   *
   * @return
   */
  String property();

  /**
   * If true, the nullity of the property will be negated and the result used as the truth value.
   *
   * @return
   */
  boolean negate() default false;
}
