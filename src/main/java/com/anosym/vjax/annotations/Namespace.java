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
 * Used to annotate a high-level class to be in a particular namespace When members of the instance
 * annotated with
 *
 * @Namespace appears, they will be treated based on their namespace, otherwise the current
 * namespace will be used and applied to the instance and all its members Note that a class
 * subjected to this namespace annotation will have a schema generated based on its definition
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Namespace {

  public String prefix();

  public String uri();

  public boolean elementFormDefault() default true;

  public boolean attributeFormDefault() default false;

  boolean includeFormDefaultAttributeOnFalse() default true;

  boolean hasSchema() default false;

  String schemaLocation() default "";
}
