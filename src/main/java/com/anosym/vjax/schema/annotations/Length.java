/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * The length constraint specified on an object property
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {

  int minLength();

  int maxLenght();
}
