/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Signifies that the data is to be appended to the attribute of the annotated element. Mostly, this
 * is intended to be added to basic type, when the user does not need to necessarily created a class
 * to hold the object. It can also be important on rendering of the object for editing on an xml
 * based engine.
 *
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface DefinedAttribute {

  /**
   * The name of the attribute.
   *
   * @return
   */
  String name();

  /**
   * The value of the attribute
   *
   * @return
   */
  String value();
}
