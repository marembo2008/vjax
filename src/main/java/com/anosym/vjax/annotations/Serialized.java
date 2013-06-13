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
 * When a propety of field has been annotated with this annotation,
 * the marshaller will use java serialization mechanism to map the data to an element.
 * In this case, the element will not have any child elements
 * If this annotation appears on a class, then all annotations that appear on the class field/properties
 * will be ignored
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Serialized {
}
