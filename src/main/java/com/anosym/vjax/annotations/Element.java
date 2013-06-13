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
 * By default, all properties of an object is considered to be child elements of their containing
 * parent. However, if the parent is annotated by {@link AsAttributes} then any property that needs
 * to be considered as an element should be annotated with this annotation in order to consider the
 * property as child element.
 *
 * This property will not have effect if the annotation is specified on type, and the object
 * instance is part of a collection.
 *
 * This annotation overrides any class specified in the {@link AsAttributes#ignore() } value.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Element {
}
