/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations;

import java.lang.annotation.*;

/**
 * If declared on an enum-class, then the value property is used as the element markup name.
 *
 * @author marembo
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface EnumMarkup {

  String value();
}
