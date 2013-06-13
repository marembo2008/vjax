/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * The annotation signals to the schema generator that the annotated element or attribute will be a
 * global element or attribute appearing as the child of the <schema> element By default all public
 * classes element definitions are global except inner classes.
 *
 * There is a difference when a class is annotated as a schema and global
 *
 * @see
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Global {

  boolean value() default true;
}
