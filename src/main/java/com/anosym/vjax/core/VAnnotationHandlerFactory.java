/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core;

import java.lang.annotation.Annotation;

/**
 * An Annotation handler factory is responsible for creating specific annotation handlers for the
 * specified Annotations. In some cases, users may override the default handlers specified in the
 * Annotations' AnootationHandler
 *
 * @author marembo
 */
public interface VAnnotationHandlerFactory {

  /**
   * Returns the annotation handler for the specified annotation, optionally cacheing the handler
   * and the annotation for future requests
   *
   * @param annotation
   * @return
   */
  public VAnnotationHandler getAnnotationHandlerFor(Annotation annotation);

  /**
   * Registers the annotation handler with the annotation handler factory. A conveninet form for
   * which users are allowed to override the default handler for annotations.
   *
   * @param annotation
   * @param annotationHandler
   */
  public void registerAnnotationHandlerFor(Annotation annotation, VAnnotationHandler annotationHandler);

  /**
   * The marshaller explictly calls this method to release all the resources this factory may have
   * acquired, when all marshalling/unmarshalling has been done, regardless whether an error
   * occurred or not. If the implementation does not wish to release the resources because of reuse,
   * it may choose to simply ignore the call.
   */
  public void close();
}
