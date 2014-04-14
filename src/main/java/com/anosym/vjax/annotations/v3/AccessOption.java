/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * How property of the type is accessed: {@link AccessType#FIELD} and {@link AccessType#METHOD}.
 *
 * Default access type is {@link AccessType#FIELD}
 *
 * @author marembo
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AccessOption {

  public static enum AccessType {

    FIELD,
    /**
     * Currently not supported for unmarshalling.
     */
    METHOD;
  }

  AccessType value() default AccessType.FIELD;

}
