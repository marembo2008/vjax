/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.util.VConditional;
import java.lang.annotation.*;

/**
 * If found on a field or method, then the current instance must define an implementation of
 * VConditional that will be passed the current instance if the field or method should be
 * marshalled. The class annotation overrides property annotation
 *
 * @author Administrator
 * @see VConditional for use case
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Condition {

  /**
   * The conditional instance which will be passed the instance to see if it accepts the field or
   * object returned by the method call
   *
   * @return
   */
  Class<? extends VConditional> condition() default VConditional.class;

  /**
   * If this annotation is defined on a class level and the onProperty is true, then the condition
   * will be called on all property instances accessed for marshalling
   *
   * @return
   */
  boolean onProperty() default false;
}
