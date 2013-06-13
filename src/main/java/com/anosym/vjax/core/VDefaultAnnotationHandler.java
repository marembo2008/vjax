/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core;

import com.anosym.vjax.VMarshaller;
import com.anosym.vjax.xml.VElement;

/**
 * <p> The following implementation cannot have parent handler. It is singleton, and all
 * AnnotationHandlers, who wish to get an instance need to simply reference
 * {@link #DEFAULT_ANNOTATION_HANDLER} </p> <p> The {@link #getParentAnnotationHandler() } method
 * returns <b style="color:red;">this</b> as its parent and never returns <b
 * color="style:blue">null</b>. </p>
 *
 * @author marembo
 */
public enum VDefaultAnnotationHandler implements VAnnotationHandler {

  DEFAULT_ANNOTATION_HANDLER;

  /**
   * {@inheritDoc }
   *
   * @throws IllegalArgumentException always.
   * @param annotationHandler
   */
  @Override
  public void setParentAnnotationHandler(VAnnotationHandler annotationHandler) {
    throw new IllegalArgumentException("Default Annotation Handler cannot have parent handler");
  }

  /**
   * {@inheritDoc } <p>We set this to always return itself as the parent handler in order to avoid,
   * any one trying to set a parent handler for the Default Annotation handler.</p>
   *
   * @return
   */
  @Override
  public VAnnotationHandler getParentAnnotationHandler() {
    return this;
  }

  @Override
  public <T> VElement marshallElement(VMarshaller<?> marshaller, T object) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
