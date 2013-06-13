/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.schema.util.DerivationType;
import java.lang.annotation.*;

/**
 * When a class has been annotated by this element, and it extends or implements an interface with
 * properties that are marshallable, then the superclass or interface will be marshalled to a type
 * and the class will derive from it according to the derivation specified.
 *
 * @author marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Derives {

  DerivationType value() default DerivationType.EXTENSION;
}
