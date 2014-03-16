/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GenericCollectionType {

  public static interface Typer {

    Class typer();
  }

  /**
   * The type of the generic collection element. If not specified, the typer must be specified.
   *
   * @return
   */
  Class value() default Void.class;

  /**
   * A typer is a class that may decide to generate a different collection element type dynamically.
   *
   * @return
   */
  Class<? extends Typer> typer() default Typer.class;
}
