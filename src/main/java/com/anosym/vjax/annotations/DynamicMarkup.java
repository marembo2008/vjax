/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.util.VMarkupGenerator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation, appearing on a field or method is to be used for representing a dynamically
 * assigned markup When this annotation appears on properties, the the generator will be called for
 * every instance in the class
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface DynamicMarkup {

  /**
   * The class that is instantiated to dynamically generate the markup This class must be an
   * instance of {@link VMarkupGenerator}
   *
   * @return a class extending the {@link VMarkupGenerator} interface
   */
  Class markupGenerator();
}
