/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import com.anosym.vjax.v3.defaulter.Defaulter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author marembo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Default {

  /**
   * If the type of the property is a simple type, the default value will be converted
   * appropriately.
   *
   * Simple type includes:
   *
   * 1. Primitive types and their wrappers
   *
   * 2. String
   *
   * 3. BigDecimal
   *
   * @return
   */
  String value() default "";

  /**
   * Used to create a default type of a property.
   *
   * Note that the default type applies only to simple objects.
   *
   * A list will use the created type as its element, rather than as the list definition itself.
   *
   * @return
   */
  Class<?> defaulter() default Defaulter.class;
}
