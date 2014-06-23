/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * We no longer need to determine generic map type.
 *
 * @See {@link KeyValueMarkup}
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Deprecated
public @interface GenericMapType {

    Class key();

    Class value();

    String keyMarkup() default "key";

    String valueMarkup() default "value";

    String entryMarkup() default "entry";
}
