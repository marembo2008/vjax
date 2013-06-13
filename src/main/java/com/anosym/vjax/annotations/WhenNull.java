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
 * This annotation, when it appears on a method or field property
 * determines how that property is handled when it is null
 * When this annotation appears on property field or method, it is recommended
 * that the class strictly follow the java beans specification and such inclusions
 * of converters and aliases must not be used, since during marshalling, such converters and aliases
 * cannot be determined.
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface WhenNull {

    /**
     * The Null Property mode behaviour.
     * By defualt, the marshaller ignores all null properties
     * @return
     */
    NullMode mode() default NullMode.IGNORE;

    public static enum NullMode {

        /**
         * Ignores the property and continue marshalling
         */
        IGNORE,
        /**
         * Marshalls the property to single element with no property children
         */
        MARSHALL_NULL,
        /**
         * Marshalls the property plus its properties, but set all properties to null as well
         */
        MARSHALL_NULL_PROPERTY;
    }
}
