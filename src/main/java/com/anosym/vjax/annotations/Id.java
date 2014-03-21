/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implies that a property is an id of current element.
 *
 * Implicitly this declares that the property is an attribute and that it is required. It becomes
 * useless, if the element is annotated by the {@link Required} attribute.
 *
 * If the property is not a primitive type, primitive wrapper or string, then a converter must be
 * provided.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface Id {
}
