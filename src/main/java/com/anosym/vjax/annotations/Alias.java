/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.alias.VAlias;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation appears on a class or field/method property, then the specified {@link #alias()
 * } class will be used to convert the specified object to a marshallable instance.
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Alias {

  /**
   * The alias Class. Note that the Alias class must have a no-arg constructor and must implement {@link VAlias}
   *
   * @return
   */
  Class<? extends VAlias> alias() default VAlias.class;
}
