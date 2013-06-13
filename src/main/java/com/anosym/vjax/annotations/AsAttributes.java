/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that all properties of this class are to be considered as attributes. This is a
 * shorthand for specifying each individual property as an attribute, where most properties of the
 * object are to be considered as attribute.
 *
 * You can exclude some properties by annotating them with {@link Element} or specifying their
 * classes in the {@link AsAttributes#ignore() } values.
 *
 * @author marembo
 * @see Element
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface AsAttributes {

  /**
   * This is intended for a subclass to override as Attributes definition if the subclass has more
   * element children than attributes.
   *
   * @return
   */
  boolean value() default true;

  /**
   * The classes to ignore. By default, no classes are ignored. Any class or subclass of these
   * classes, unless overriden by {@link Attribute} annotation, will be ignored and considered as
   * child elements.
   *
   * @return
   */
  Class<?>[] ignore() default {};
}
