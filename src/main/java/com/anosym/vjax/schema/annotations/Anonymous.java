/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * when a property is annotated as anonymous, then it will not have a named type that can be
 * referenced. The complex type represented by the specified class will be defined inside its parent
 * element An element marked Anonymous cannot be {@link Global} neither can it be a {@link Schema}
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Anonymous {
}
