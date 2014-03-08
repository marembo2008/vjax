/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;

/**
 *
 * @author marembo
 */
public final class VWrapper {

  public static <W, T> W findWrapper(T instance, Class<W> wrapper) {
    try {
      VObjectMarshaller<T> vom = new VObjectMarshaller<T>((Class<? extends T>) instance.getClass());
      VDocument doc = vom.marshall(instance);
      VObjectMarshaller<W> vom_ = new VObjectMarshaller<W>(wrapper);
      return vom_.unmarshall(doc);
    } catch (VXMLBindingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
