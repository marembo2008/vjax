/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flemax.vjax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies how to handle empty or null param values. By default, the url encoder does not ignore
 * null or empty values.
 *
 * @author marembo
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlParamValue {

  boolean encodeNull() default true;

  boolean encodeEmpty() default true;
}
