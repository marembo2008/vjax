/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.converter.VConverter;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only allowed on map fields or getter methods. The map key value pairs are considered as
 * attributes of the current element.
 *
 * If the map key_value pair are not primitive, nor primitive wrappers nor String, then a converter
 * must be specified.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AsAttribute {

  /**
   * Converter class for key
   *
   * @return
   */
  Class keyConverter() default VConverter.class;

  /**
   * Converter class for value
   *
   * @return
   */
  Class valueConverter() default VConverter.class;
}
