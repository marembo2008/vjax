/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import com.anosym.vjax.id.generation.VGenerator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Only used with {@link Id} annotation to specify that an Id is automatically generated.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface Generated {

  /**
   * If provided, will be used as the default id generation.
   *
   * @return
   */
  Class<? extends VGenerator> provider() default VGenerator.class;
}
