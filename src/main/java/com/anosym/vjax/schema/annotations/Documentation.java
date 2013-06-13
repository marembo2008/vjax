/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.annotations.Comment;
import java.lang.annotation.*;

/**
 * The documentation annotation refers to the description of the property type This defers
 * significantly from {@link Comment} which describes the content of or meaning of the property in
 * terms of usage and value specification
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PACKAGE})
public @interface Documentation {

  /**
   * The description of the element
   *
   * @return
   */
  String value();
}
