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
 * When a property is marked with this annotation, it means that the marshaller will try find properties of the object
 * that can be used to initialize the object during unmarshalling.
 * If the marshaller fails to find any compatible properties, then
 * an exception may be thrown
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Constructible {
}
