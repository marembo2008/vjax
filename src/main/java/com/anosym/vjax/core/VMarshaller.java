/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.core;

import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;

/**
 * The interface to which marshalling/unmarshalling is delegated
 *
 * @author marembo
 */
public interface VMarshaller<T> {

  /**
   * Marshalls the specified Object to an xml element
   *
   * @param obj
   * @return
   */
  VElement marshall(T obj);

  /**
   * Marshalls the specified object to xml document
   *
   * @param obj
   * @return
   */
  VDocument marshallObject(T obj);

  /**
   * Unmarshalls the xml element to an instance of an object of specified type
   *
   * @param element
   * @return
   */
  T unmarshall(VElement element);

  /**
   * Unmarshalls the xml document to an instance of an object of the specified type
   *
   * @param document
   * @return
   */
  T unmarshallDocument(VDocument document);
}
