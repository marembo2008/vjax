/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When this annotation appears on the top level node, then no attributes pertaining to the
 * unmarshalling of the document will be generated. What this implies is that after generation, the
 * document cannot be used to create a java tree object. This annotation must appear only on the top
 * level node, otherwise it will be ignored When attributes are ignored, then recursive reference
 * may result and the application may fail.
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface IgnoreGeneratedAttribute {
}
