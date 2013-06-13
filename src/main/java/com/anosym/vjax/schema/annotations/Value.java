/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * Should only appear on a class annotated as Simple It specifies the value content of the property
 * which will be derived from a simple type If this value cannot be mapped to a simple type and
 * there is no defined converter, an exception will be raised
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
}
