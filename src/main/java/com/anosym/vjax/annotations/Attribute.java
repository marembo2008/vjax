/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.util.VAttributeKeyNormalizer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation appears on a class member property, then it will be considered as an element
 * attribute rather than ana element child To unmarshall this component, an xml schema must be used.
 * An application that specifies a member property as attribute cannot generate a standalone
 * document Moreover, the element must be a simple type (primitive types/or primitive wrappers,
 * String) or a Converter must be defined
 * <pre>
 * Even if the root element class has been marked with {@link IgnoreGeneratedAttribute} annotation,
 * it will not affect the inclusion of user defined attributes
 * </pre>
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Attribute {

  /**
   * The default attribute name
   *
   * @return
   */
  public String name() default "";

  /**
   * Class used to normalize the attribute key
   *
   * @return
   */
  public Class<? extends VAttributeKeyNormalizer> attributeKeyNormalizer() default VAttributeKeyNormalizer.class;

  /**
   * The default value of this attribute if the property value is null.
   *
   * @return
   */
  public String value() default "";
}
