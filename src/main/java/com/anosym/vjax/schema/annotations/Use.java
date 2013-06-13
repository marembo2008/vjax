/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * The attribute specifies the options for an attribute use in an element
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Use {

  /**
   * Defines how attribute is used within its declaring element
   *
   * @return
   */
  UseType use() default UseType.REQUIRED;

  /**
   * Default value that can be specified when attributes use is default.
   *
   * @return
   */
  String defaultValue() default "";

  public static enum UseType {

    REQUIRED,
    OPTIONAL,
    PROHIBITED;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }
}
