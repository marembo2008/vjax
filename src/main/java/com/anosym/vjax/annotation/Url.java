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
 * If found on an instance member during url encoding, the field will be treated as the base url.
 * The order in which fields are treated as base urls is undetermined. All other fields annotated
 * with this annotation will be ignored completely and not considered in any encoding
 *
 * @author marembo
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Url {
}
