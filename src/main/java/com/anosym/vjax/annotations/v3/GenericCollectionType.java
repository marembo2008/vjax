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
 * If this annotation is specified, the unmarshaller will not bother to determine the type of the collection, and hence
 * either the value or the typer of this annotation must be specified.
 *
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GenericCollectionType {

    public static interface Typer<T> {

        Class<T> typer();
    }

    /**
     * If not specified, we determine the type of the generic type at runtime.
     *
     * If the generic type cannot be marshalled, an exception will be thrown.
     *
     * We have deprecated this since it is no longer a requirement to specify the collection element type.
     *
     * @return
     */
    @Deprecated
    Class value() default Void.class;

    /**
     * A typer is a class that may decide to generate a different collection element type dynamically.
     *
     * @return
     */
    Class<? extends Typer> typer() default Typer.class;
}
