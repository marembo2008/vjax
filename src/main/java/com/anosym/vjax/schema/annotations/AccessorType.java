/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * The access type specifies how the marshaller access the object properties. Note that if FIELD
 * type is specified, all annotations on the method property will be ignored and vice-versa <p> When
 * access type is defined, then it applies to all members unless, overriden by a member
 * class/property </p>
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface AccessorType {

  public AccessType value() default AccessType.FIELD;

  public static enum AccessType {

    FIELD,
    METHOD;
  }
}
