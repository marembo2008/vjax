/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.converter.VConverter;
import java.lang.annotation.*;

/**
 * When this annotation appears on a class, then it implies that the object will be marshalled by a
 * conversion to a string disregarding all its properties
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Converter {

  /**
   * The converter class that must be an instance of {@link VConverter.class}
   *
   * @return
   */
  Class<? extends VConverter<?>> converter();
}
