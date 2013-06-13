/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * When the annotation appears on a type, then the element cannot be derived from. By default, all
 * final classes cannot be derived from.
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Final {
}
