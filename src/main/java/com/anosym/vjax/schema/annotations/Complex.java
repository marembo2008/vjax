/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * When this attribute appears on a class or property, then we intend to extend the content model of
 * a complex type. In essence all properties of the class or property instance must either be
 * primitive type or convertible to a string with a specified converter
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Complex {

  /**
   * The restricted base
   *
   * @return
   */
  String base();

  /**
   * Uses attribute group to group all the attributes (by default all specified properties)
   *
   * @return
   */
  boolean grouped() default true;

  /**
   * If grouped, then this value specifies the group id and cannot be null
   *
   * @return
   */
  String groupId() default "";
}
