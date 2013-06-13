/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * When this annotation is found on a type/property, then it specifies that a new type definition
 * for the class must not be defined on a global scale but must be anonymous By defaUlt all objects
 * are but the primitives are treated as complex type definitions
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface OverrideDefinition {
}
