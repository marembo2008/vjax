/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core;

import com.anosym.vjax.core.annotations.AnnotationHandler;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * A handler factory for determining the appropriate annotation handler for each annotation
 *
 * @author marembo
 */
public enum VDefaultAnnotationHandlerFactory implements VAnnotationHandlerFactory {

  ANNOTATION_HANDLER_FACTORY;
  private final Map<Annotation, VAnnotationHandler> registry;

  private VDefaultAnnotationHandlerFactory() {
    registry = Collections.synchronizedMap(new HashMap<Annotation, VAnnotationHandler>());
  }

  @Override
  public VAnnotationHandler getAnnotationHandlerFor(Annotation annotation) {
    boolean annotationRegistered;
    synchronized (registry) {
      annotationRegistered = registry.containsKey(annotation);
    }
    if (!annotationRegistered) {
      //check if it has been annotated with AnnotationHandler annotation
      //And register it for subsequent lookups
      AnnotationHandler annotHandler = annotation.getClass().getAnnotation(AnnotationHandler.class);
      if (annotHandler != null) {
        try {
          Class<? extends VAnnotationHandler> handlerClass = annotHandler.handler();
          if (handlerClass == null || handlerClass == VDefaultAnnotationHandler.class) {
            // TODO(marembo) : Whats the space cost implication of maintaining several
            // references of this in a map?
            registerAnnotationHandlerFor(annotation, VDefaultAnnotationHandler.DEFAULT_ANNOTATION_HANDLER);
          } else {
            VAnnotationHandler handler = handlerClass.newInstance();
            registerAnnotationHandlerFor(annotation, handler);
            // We dont return the handler at this point, so that if Handler had been added
            // by another thread, and used, we return the current state of the handler, rather
            // than replacing it.
          }
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
      }
    }
    synchronized (registry) {
      return registry.get(annotation);
    }
  }

  @Override
  public void registerAnnotationHandlerFor(Annotation annototation, VAnnotationHandler annotationHandler) {
    synchronized (registry) {
      if (!registry.containsKey(annototation)) {
        // We dont replace since the annotation handler may contain states that the user
        // would like to remain persisted across invocations.
        /*
         * We set the parentage of the handler at this point, if it has none, in order that if it is
         * not added, it be garbage collected soonest.
         */
        if (annotationHandler.getParentAnnotationHandler() == null) {
          annotationHandler.setParentAnnotationHandler(VDefaultAnnotationHandler.DEFAULT_ANNOTATION_HANDLER);
        }
        registry.put(annototation, annotationHandler);
      }
    }
  }

  @Override
  public void close() {
    registry.clear();
  }
}
