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
 *
 * @author marembo
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlEncodeInheritedParam {

  /**
   * If true, the superclass fields will not be considered for encoding.
   *
   * @return
   */
  boolean encodeInheritedParam() default false;

  /**
   * Specifies how far up the superclass hierarchy, we should encode for parameters. The default is
   * the immediate superclass.
   *
   * @return
   */
  int encodeInheritedLevel() default 1;
}
