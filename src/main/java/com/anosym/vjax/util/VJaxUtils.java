/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author marembo
 */
public class VJaxUtils {

  public static boolean isNullOrEmpty(Object str) {
    return str == null || str.toString().trim().length() == 0;
  }

  /**
   * Returns null if not field is annotated with the specified annotation.
   *
   * @param <A>
   * @param c
   * @param annot
   * @return
   */
  public static <A extends Annotation> Field getFieldAnnotated(Class c, Class<A> annot) {
    if (Object.class == c) {
      return null;
    }
    for (Field f : c.getDeclaredFields()) {
      if (f.isAnnotationPresent(annot)) {
        return f;
      }
    }
    return getFieldAnnotated(c, annot);
  }
}
