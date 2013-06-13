/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * True if the property is nillable
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nillable {
}
