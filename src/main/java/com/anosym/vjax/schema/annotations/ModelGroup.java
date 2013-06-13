/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * Determines the nature of the generated element from a list, array or any collection structure
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface ModelGroup {

  GroupType type() default GroupType.SEQUENCE;

  public static enum GroupType {

    /**
     * The elements match the particles in a sequential order
     */
    SEQUENCE,
    /**
     * The element information match the particles in any order
     */
    CONJUNCTION,
    /**
     * The element information match one of the particles
     */
    DISJUNCTION;
  }
}
