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
 *
 * @author marembo
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhenEmpty {

  /**
   * Should the collection or map property be marshaled when empty.
   *
   * @return
   */
  boolean marshall() default true;

  /**
   * Should the element types be marshaled when the map or collection is empty
   *
   * @return
   */
  boolean marshallType() default false;

  /**
   * Returns the classes for the generic types of the collection or map
   *
   * @return
   */
  Class<?>[] genericClasses() default {Object.class};
}
