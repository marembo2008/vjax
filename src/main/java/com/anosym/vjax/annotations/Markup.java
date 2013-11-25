/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The value of this annotation is used as markup that associates an element and a property
 *
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Markup {

  public String name();

  /**
   * Used on unmarshalling when element can be mapped to different element markup, on different
   * context
   *
   * @return
   */
  public String[] optionalNames() default {};

  public String property() default "";

  /**
   * tells the marhsaller that the element markup should use the regex to compare between fields and
   * element markups for equality.
   *
   * This only applies during unmarshalling.
   *
   * @return
   */
  public boolean useRegex() default false;

  /**
   * Considered only if the {@link #useRegex() } is true.
   *
   * @return
   */
  public String regex() default "";
}
