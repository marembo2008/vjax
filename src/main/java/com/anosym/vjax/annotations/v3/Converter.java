package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Converts one value to another
 *
 * @author marembo
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Converter {

  @SuppressWarnings("rawtypes")
  Class<? extends com.anosym.vjax.converter.v3.Converter> value();
}
