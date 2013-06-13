/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.converter.VConverter;
import java.lang.annotation.*;

/**
 * Specifies the markups for the key and value pair of a map Optionally, specify the markup for the
 * entry element
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface MapElement {

  /**
   * Markup for the key element
   *
   * @return
   */
  public String key();

  /**
   * Markup for the value element
   *
   * @return
   */
  public String value();

  /**
   * Markup for the entry element for key-value pair. TODO(marembo): After Marshaller refactoring,
   * consider the inclusion of this entry markup
   *
   * @return
   */
  public String entry() default "";

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
