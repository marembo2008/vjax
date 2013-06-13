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
 * Determines the relative position of the element
 * in its parent children hierarchy
 * Specifying position on informational field is redundant
 * @author Administrator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Position {

    /**
     * The index to place the element.
     * This starts from 0
     * @return
     */
    public int index();
}
