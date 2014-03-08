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
 * If found on a class level, indicates to the wrapper that the class should generate a wrapper for
 * the class;
 *
 * @author marembo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Wrapped {

  /**
   * The name of the wrapper class. Default preappends 'W' to the name of the original class.
   *
   * @return
   */
  String name() default "";

}
