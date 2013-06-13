/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When one or more fields are specified as exclusive, then only one can have a value. However, if a
 * priority is used on each field, the one with the highest priority will be considered and the rest
 * ignored. If no field has specified priority and more than one fields specified in exclusive value
 * has a value, an exception will be raised.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Exclusive {

  /**
   * The fields to consider for exclusion.
   *
   * @return
   */
  String[] fields();
}
