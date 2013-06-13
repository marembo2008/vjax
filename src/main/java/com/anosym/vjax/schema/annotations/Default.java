/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.schema.VSchemaDefault;
import java.lang.annotation.*;

/**
 * When this element appears on the property of an object, the generated schema will use the
 * specified instance class to generate a default value. Note that the generated default value must
 * be of the same type as the field type or return type of the method property The specified class
 * must implement the {@link VSchemaDefault} interface The default value of the defaultClass is the
 * interface class {@link VSchemaDefault} thus if the default value is to be generated an
 * implementing class must be specified, otherwise the default string value will be used. this may
 * throw an exception in cases of different types and complex type elements
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Default {

  /**
   * The class which must not be annonymous, neither member instance and must implement the
   * {@link VSchemaDefault} interface
   *
   * @return
   */
  Class defaultClass() default VSchemaDefault.class;

  /**
   * The default value for the attribute or element
   *
   * @return
   */
  String defaultValue() default "";
}
