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
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Iterable {

    /**
     * When a method is qualified with an iterable annotation
     * and this method returns true, then the marshaller will stop iterating on the object until
     * the method throws an exception.
     * If this method returns false, then the marshaller will stop iterating only when the method returns null
     * @return
     */
    boolean throwsException() default false;
}
