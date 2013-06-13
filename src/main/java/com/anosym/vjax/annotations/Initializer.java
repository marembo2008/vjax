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
 * when a propety is marked with this annotation, then it is used as an initialization parameter
 * By default, a property annotated with Inializer annotation is not writable, even if explicitly specified
 * to be writable
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Initializer {

    /**
     * The index of this initializer in the declaration order of the constructor
     * @return
     */
    public int index() default 0;
}
