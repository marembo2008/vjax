/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flemax.vjax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * specifies the name of the url parameters.
 *
 * @author marembo
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlParam {

  /**
   * The name of the parameter value
   *
   * @return
   */
  String value() default "";
}
