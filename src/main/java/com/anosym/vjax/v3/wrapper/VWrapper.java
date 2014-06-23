/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.v3.GenericCollectionType;
import com.anosym.vjax.annotations.v3.GenericCollectionType.Typer;
import com.anosym.vjax.util.VJaxUtils;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author marembo
 */
public final class VWrapper {

  /**
   * Two way method. Can be called to find the wrapper or to find the object value from the wrapper.
   *
   * @param <W> the object type to return as the wrapper
   * @param <T> the object type to wrap
   * @param value the actual value to wrap
   * @param wrapperClass the class of the wrapped object
   * @return
   * @throws com.anosym.vjax.v3.wrapper.VWrapperException
   */
  public static <W, T> W wrap(T value, Class<W> wrapperClass) throws VWrapperException {
    if (value == null) {
      return null;
    }
    if (wrapperClass == null) {
      throw new VWrapperException("Wrapper class must not be null");
    }
    try {
      VObjectMarshaller<T> vom = new VObjectMarshaller<T>((Class<? extends T>) value.getClass());
      VDocument doc = vom.marshall(value);
      VObjectMarshaller<W> vom_ = new VObjectMarshaller<W>(wrapperClass);
      return vom_.unmarshall(doc);
    } catch (Exception ex) {
      throw new VWrapperException(value.getClass(), wrapperClass, ex);
    }
  }

  /**
   * Two way method. Can be called to find the wrapper or to find the object value from the wrapper.
   *
   * @param <W> the object type to return as the wrapper
   * @param <T> the object type to wrap
   * @param value the actual value to wrap
   * @param wrapperClass the class of the wrapped object
   * @return
   * @throws com.anosym.vjax.v3.wrapper.VWrapperException
   */
  public static <W, T> List<W> wrapCollection(Collection<T> value, final Class<W> wrapperClass) throws VWrapperException {
    try {
      //id does not matter whether it is the same thread or not, it may have changed the wrapper class, so set it afresh.
      WRAPPER_CLASS.set(wrapperClass);
      ValueHolder<T> valueHolder = new ValueHolder<T>(value);
      return wrap(valueHolder, WrapperHolder.class).values;
    } catch (Exception ex) {
      throw new VWrapperException(VJaxUtils.isNullOrEmpty(value) ? Void.class : value.iterator().next().getClass(), wrapperClass, ex);
    }
  }
  private static ThreadLocal<Class> WRAPPER_CLASS = new ThreadLocal<Class>();

  public static class WrapperTyper implements Typer {

    @Override
    public Class typer() {
      return WRAPPER_CLASS.get();
    }

  }

  public static class ValueHolder<T> {

    private Collection<T> values;

    public ValueHolder() {
    }

    public ValueHolder(Collection<T> values) {
      this.values = values;
    }

  }

  public static class WrapperHolder<W> {

    @GenericCollectionType(typer = WrapperTyper.class)
    private List<W> values;

    public WrapperHolder() {
    }

  }
}
