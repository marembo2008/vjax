/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.annotations.Converter;
import java.lang.annotation.*;

/**
 * When a property has been annotated as simple then it will be derived from a simple type The
 * following scenario applies: <ol> <li>The annotation appears on a field/method property. The
 * property must be annotated with a converter annotation to convert between the value content and
 * the object instance. In this case, the {@link #base() } will specify the direct mapping</li>
 * <li>The annotation appears on a class scope. The property class must define at least one property
 * annotated with the
 * {@link Value} annotation or {@link Converter} annotation. In this case all other properties of
 * the elements will be ignored.</li> </ol>
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Simple {

  /**
   * The base from which the type extends, or for which the type is.
   *
   * @return
   */
  String base();
}
