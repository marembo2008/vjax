/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.*;

/**
 * If the element contents should be displayed
 *
 * @author marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Displayable {

  boolean value() default true;
}
