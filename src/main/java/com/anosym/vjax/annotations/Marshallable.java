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
 * If false, then the element will not be marshalled
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface Marshallable {

    /**
     * If true then the annotated element will be marshalled.
     * If false, then the write property will not be considered
     * @return
     */
    public boolean marshal() default true;

    /**
     * If true, and it is marshallable, then the property specifies if it can be written into
     * @return
     */
    public boolean write() default true;
}
