/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core.annotations;

import java.lang.annotation.*;

/**
 * Used to annotate Annotations by specifying the annotation handlers
 *
 * @author marembo
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationHandler {

  /**
   * The handler class for the annotated annotation
   *
   * @return
   */
  Class handler();
}
