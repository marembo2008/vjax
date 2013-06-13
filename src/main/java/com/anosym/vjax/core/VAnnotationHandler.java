/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core;

import com.anosym.vjax.VMarshaller;
import com.anosym.vjax.xml.VElement;

/**
 * Handles the marshalling/unmarshalling of the object in an annotation specific way.
 *
 * When the marshaller has to marshall/unmarshall specific object, it will delegate any marshalling/
 * unmarshalling based on specific annotations. <p> The marshaller deals with annotations as below:
 * <pre>
 *    Annotation[] annots = object.getClass().getAnnotations();
 *    for (Annotation a : annots) {
 *      VAnnotationHandler handler = annotationHandlerFactory.getAnnotationHandlerFor(a);
 *      if (handler != null) {
 *        VElement elem = handler.marshallElement(this, object);
 *      }
 *    }
 * In order to avoid endless recursion, subclasses need to specify a parent handler, without calling
 * the {@link VMarshaller#marshall(java.lang.Object) } directly. By default, default registered
 * AnnotationHandler's through the use of AnnotationHandler annotation have {@link VDefaultAnnotationHandler}
 * specified as parent, and it is guaranteed that only one instance of this {@link VDefaultAnnotationHandler}
 * will ever exist.
 *
 * A Recursive call to {@link VAnnotationHandler#getParentAnnotationHandler() } must terminate at
 * the point where {@link VAnnotationHandler#getParentAnnotationHandler() } == {@link VDefaultAnnotationHandler#DEFAULT_ANNOTATION_HANDLER}.
 * Moreover, if a subclasses cannot effectively marshall ab attribute, or element from the specified
 * object, it should delegate the marshalling to the parent handler.
 *
 * @author marembo
 */
public interface VAnnotationHandler {

  /**
   * Sets the parent annotation handler for this annotation handler.
   *
   * @param annotationHandler
   */
  public void setParentAnnotationHandler(VAnnotationHandler annotationHandler);

  /**
   * Returns the parent annotation handler for this Annotation handler if any.
   *
   * @return
   */
  public VAnnotationHandler getParentAnnotationHandler();

  /**
   * When given the object, the annotation handler must be able to marshall the object, using the
   * specified marshaller and returns an element for the specified object
   *
   * @param <T>
   * @param marshaller
   * @param object
   * @return
   */
  <T> VElement marshallElement(VMarshaller<?> marshaller, T object);
}
