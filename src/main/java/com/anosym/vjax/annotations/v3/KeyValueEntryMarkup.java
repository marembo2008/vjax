package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * markup for a map entry and its key-value.
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface KeyValueEntryMarkup {

    /**
     * The markup for the key.
     *
     * @return
     */
    String key() default "key";

    /**
     * The markup for the value.
     *
     * @return
     */
    String value() default "value";

    /**
     * The markup for the entry element that encapsulates the key=value pair.
     *
     * @return
     */
    String entry() default "entry";
}
