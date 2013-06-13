/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.exceptions.VConverterBindingException;

/**
 * The interface is the basis for converting objects to String If a class has been annotated with
 * the Converter annotation, then it will not be represented as a sequence of member elements in any
 * hierarchical order
 * <pre>
 *      All Converters must have a no-arg constructor
 * </pre>
 *
 * @author Marembo
 */
public abstract class VConverter<T> {

  /**
   * Converters the specified string representation to an object
   *
   * @param value the string representation of the object
   * @return an object converted from the string
   * @throws VConverterBindingException when the converter is unable to convert the string value to
   * the specified object
   */
  public abstract T convert(String value) throws VConverterBindingException;

  /**
   * Converts the specified Object to its String representation The String value must be in such a
   * way that the marshaller can use it to reconstruct the object
   *
   * @param value the object to convert to its string equivalence
   * @return a string representing the object
   * @throws VConverterBindingException when the converter is unable to convert the object to its
   * string equivalence
   */
  public abstract String convert(T value) throws VConverterBindingException;

  /**
   * Returns true if the converter is capable of converting an object of specified class
   *
   * @param clazz the class to determine its convertibility
   * @return true if the the converter can convert an object of the specified class, otherwise
   * returns false
   */
  public abstract boolean isConvertCapable(Class<T> clazz);

  /**
   * Convertes the specified object to an alias in a specified manner
   * <code>
   * </code>
   *
   * @param value
   * @return
   * @throws VConverterBindingException
   * @throws VXMLBindingException
   */
  public <A> A convertToAlias(T value) throws VConverterBindingException, VXMLBindingException {
    return null;
  }

  /**
   * Converts the alias to the actual object To recreate the alias, see the documentation of the
   * marshaller
   *
   * @param <A>
   * @param value
   * @return
   * @throws VConverterBindingException
   */
  public <A> T convertToInstance(A value) throws VConverterBindingException {
    return null;
  }
}
