/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.converter.VConverter;
import java.lang.annotation.*;

/**
 * The fixed annotation alludes to the schema generator that the the property of the instance (which
 * must be annotated as an attribute) will have a fixed value if it appears on the element,
 * otherwise it must not appear on the specified element
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Fixed {

  /**
   * The default value of the attribute. By default this is the empty string
   *
   * @return
   */
  String value() default "";

  /**
   * The class to instantiate to derive the default value of an attribute This class must be
   * assignable to {@link VConverter} and the returned value must be convertible to String by
   * default, the class is specified as the
   * {@link VConverter.class} and thus must be overriden if the value in String has not been
   * specified, otherwise an exception will be raised
   *
   * @return
   */
  Class<? extends VConverter<?>> converter();
}
