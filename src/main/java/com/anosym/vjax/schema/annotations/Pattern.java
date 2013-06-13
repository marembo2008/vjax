/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.converter.VConverter;
import java.lang.annotation.*;

/**
 * This annotation specifies the accepted value of a String type using regular expression. Since
 * Strings map to the xml string type, then the annotated property/type must be a String or
 * convertible to a String.
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Pattern {

  /**
   * The regular expression for which the string value must adhere to
   *
   * @return
   */
  String regex();

  /**
   * Used to convert an object to String if it is annoted with this annotation and is not a String
   * instance
   *
   * @return
   */
  Class<? extends VConverter> converter() default VConverter.class;
}
