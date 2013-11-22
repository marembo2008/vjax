/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import com.anosym.vjax.v3.initializer.Initializer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To indicate to the marshaller that the type class of the object can only be initiated from an
 * external context.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Define {

  /**
   * The class to initialize the class object of the specified object.
   *
   * @return
   */
  Class<? extends Initializer> value();
}
