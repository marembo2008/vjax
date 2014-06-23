/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies which concrete class to be used as impementation of the field interface/abstract class.
 *
 * If specified on class level, the class must be an interface/abstract type.
 *
 * Can potentially replace the functionality of {@link Define} annotation in certain scenarios.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Implemented {

  Class value();
}
