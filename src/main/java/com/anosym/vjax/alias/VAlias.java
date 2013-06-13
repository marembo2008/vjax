/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.alias;

import com.anosym.vjax.annotations.Alias;
import com.anosym.vjax.exceptions.VConverterBindingException;

/**
 * Conversion between two objects of different classes. The best common use case for an alias is
 * when a class contains several properties and annotation each of the property with the
 * Marshallable annotation will clutter most of the codes. An alias is the defined which is capable
 * of transforming the huge class to a smaller one without necessary loosing any information. The
 * alias must be able to convert back to the original object. Additionally, the alias must not
 * depend on a reference of object to be converted. Otherwise if it is the class that has been
 * annotated with the {@link Alias} annotation, even though circular reference will be resolved, the
 * result will be undefined
 *
 * @author marembo
 * @param <T> the object instance type
 * @param <A> the alias instance type
 */
public interface VAlias<T, A> {

  /**
   * Returns the alias representation of the specified instance object
   *
   * @param instance
   * @return
   */
  public A getAlias(T instance) throws VConverterBindingException;

  /**
   * Returns the object whose alias has been specified
   *
   * @param alias
   * @return
   */
  public T getInstance(A alias) throws VConverterBindingException;
}
