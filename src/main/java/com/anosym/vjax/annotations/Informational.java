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
 * providing the the getter field is information only
 * In this regard, the marshalling will not try to set the properties of the instance
 * even if it has informational decoration
 * However, if the property is decorated to be settable, them the unmarshalling engine will
 * try to set the values
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Informational {
}
