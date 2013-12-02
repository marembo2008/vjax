/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.PrimitiveType;
import static com.anosym.vjax.PrimitiveType.BOOLEAN;
import static com.anosym.vjax.PrimitiveType.BYTE;
import static com.anosym.vjax.PrimitiveType.CHAR;
import static com.anosym.vjax.PrimitiveType.DOUBLE;
import static com.anosym.vjax.PrimitiveType.FLOAT;
import static com.anosym.vjax.PrimitiveType.INT;
import static com.anosym.vjax.PrimitiveType.LONG;
import static com.anosym.vjax.PrimitiveType.SHORT;
import static com.anosym.vjax.PrimitiveType.VOID;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayComponentInitializer;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.annotations.v3.CollectionElementConverter;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.ConverterParam;
import com.anosym.vjax.annotations.v3.Define;
import com.anosym.vjax.annotations.v3.GenericCollectionType;
import com.anosym.vjax.annotations.v3.GenericMapType;
import com.anosym.vjax.annotations.v3.Listener;
import com.anosym.vjax.converter.VBigDecimalConverter;
import com.anosym.vjax.converter.v3.impl.PropertyListener;
import com.anosym.vjax.util.VConditional;
import static com.anosym.vjax.v3.VObjectMarshaller.PRIMITIVE_WRAPPER_MAPPING;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author marembo
 */
public class Unmarshaller<T> {

  private final Class<? extends T> instanceClass;

  public Unmarshaller(Class<? extends T> instanceClass) {
    this.instanceClass = instanceClass;
  }

  public T unmarshall(VDocument document) throws VXMLMemberNotFoundException,
          VXMLBindingException {
    return unmarshal(document.getRootElement(), instanceClass, null);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private T unmarshal(VElement element, Class clazz, Annotation[] annots)
          throws VXMLBindingException {
    try {
      Converter converter = VObjectMarshaller.getAnnotation(annots, Converter.class);
      if (converter != null) {
        return unmarshallConverter(element, clazz, converter);
      }
      Define define = VObjectMarshaller.getAnnotation(annots, Define.class);
      if (define != null) {
        //the class is defined using a different class, probably the current class represents and interface or abstract class.
        Class<com.anosym.vjax.v3.initializer.Initializer> definer = (Class<com.anosym.vjax.v3.initializer.Initializer>) define.value();
        com.anosym.vjax.v3.initializer.Initializer initializer = definer.newInstance();
        clazz = initializer.define(element);
      }
      if (clazz.isAssignableFrom(BigDecimal.class) && VObjectMarshaller.getAnnotation(annots, Converter.class) == null) {
        VBigDecimalConverter bdConverter = new VBigDecimalConverter();
        return (T) bdConverter.convert(element.toContent());
      }
      if (clazz.isPrimitive()
              || PRIMITIVE_WRAPPER_MAPPING.get(clazz) != null) {
        return unmarshallPrimitive(element, clazz, annots);
      }
      if (clazz.equals(String.class)) {
        return (T) element.toContent();
      }
      T instance = null;
      if (clazz.isEnum()) {
        //do we have a converter
        instance = (T) Enum.valueOf(clazz, element.toContent());
      }
      if (clazz.isArray()) {
        return unmarshallArray(element, clazz, annots);
      }
      if (instance == null) {
        // check if we have a collection converter
        CollectionElementConverter cec = VObjectMarshaller.getAnnotation(annots, CollectionElementConverter.class);
        Class cls = cec != null ? cec.value() : null;
        if (cls != null) {
          // find the converted to value
          com.anosym.vjax.converter.v3.Converter<T, Object> cnvs = (com.anosym.vjax.converter.v3.Converter<T, Object>) cls
                  .newInstance();
          // get the return type of a method
          Method m = cls.getDeclaredMethod("convertFrom",
                  new Class[]{clazz});
          Class returnType = m.getReturnType();
          // marshall against this class
          Object convertedValue = unmarshal(element, returnType, null);
          return cnvs.convertTo(convertedValue);
        } else {
          instance = (T) clazz.newInstance();
        }
      }
      if (element.hasChildren()) {
        handleFields(element, clazz, instance);
      }
      return instance;
    } catch (Exception ex) {
      throw new VXMLBindingException(ex);
    }
  }

  private T unmarshallArray(VElement element, Class clazz, Annotation[] annots) throws Exception {
    int children = element.getChildren().size();
    T[] arr = (T[]) new Object[children];
    int i = 0;
    ArrayComponentInitializer aci = VObjectMarshaller.getAnnotation(annots, ArrayComponentInitializer.class);
    for (VElement e : element.getChildren()) {
      if (aci != null) {
        Class<com.anosym.vjax.v3.arrays.ArrayComponentInitializer> initializerClass = aci.value();
        com.anosym.vjax.v3.arrays.ArrayComponentInitializer initializer = initializerClass.newInstance();
        Class cmpClass = initializer.define(e);
        if (cmpClass != null) {
          arr[i++] = unmarshal(e, cmpClass, null);
        }
      } else {
        arr[i++] = unmarshal(e, clazz.getComponentType(), null);
      }
    }
    return (T) arr;
  }

  private T unmarshallConverter(VElement element, Class clazz, Converter converter) throws Exception {
    Class cls = converter.value();
    com.anosym.vjax.converter.v3.Converter<T, Object> cnvs;
    if (converter.params().length > 0) {
      ConverterParam[] params = converter.params();
      Map<String, String[]> converterParams = new HashMap<String, String[]>();
      for (ConverterParam p : params) {
        converterParams.put(p.key(), p.value());
      }
      Constructor<com.anosym.vjax.converter.v3.Converter<T, Object>> cns = cls.getConstructor(new Class[]{Map.class});
      cnvs = cns.newInstance(new Object[]{converterParams});
    } else {
      cnvs = (com.anosym.vjax.converter.v3.Converter<T, Object>) cls
              .newInstance();
    }
    // get the return type of a method
    Method m = cls.getDeclaredMethod("convertFrom",
            new Class[]{clazz});
    Class returnType = m.getReturnType();
    // marshall against this class
    Object convertedValue = unmarshal(element, returnType, null);
    return cnvs.convertTo(convertedValue);
  }

  private List<VElement> getFieldMapping(final Field f, VElement element) {
    return element.getChildren(new VConditional<VElement>() {
      final Markup mm = f.getAnnotation(Markup.class);
      final String name = f.getName();

      @Override
      public boolean accept(VElement instance) {
        if (mm == null) {
          return instance.getMarkup().equals(name);
        } else if (!mm.useRegex()) {
          return instance.getMarkup().equals(mm.name())
                  || (mm.optionalNames().length > 0 && Arrays.asList(mm.optionalNames()).contains(instance.getMarkup()));
        } else {
          Pattern p = Pattern.compile(mm.regex());
          Matcher m = p.matcher(instance.getMarkup());
          return m.find();
        }
      }

      @Override
      public boolean acceptProperty(Object prop) {
        return false;
      }
    });
  }

  private Object unmarshallFieldArray(List<VElement> elems, Field f) throws Exception {
    if (elems.size() > 0) {
      Class<?> typeClass = f.getType();
      ArrayParented parented = f
              .getAnnotation(ArrayParented.class);
      if (parented != null) {
        elems = elems.get(0).getChildren();
      }
      // The array can be specified through individial
      // elements, or through one parent element
      ArrayComponentInitializer aci = f.getAnnotation(ArrayComponentInitializer.class);
      Class cmpType = typeClass.getComponentType();
      int length = elems.size();
      Object arr = Array.newInstance(cmpType, length);
      int i = 0;
      for (VElement c : elems) {
        Object o = null;
        if (aci != null) {
          Class<com.anosym.vjax.v3.arrays.ArrayComponentInitializer> initializerClass = aci.value();
          com.anosym.vjax.v3.arrays.ArrayComponentInitializer initializer = initializerClass.newInstance();
          Class cmpClass = initializer.define(c);
          if (cmpClass != null) {
            o = unmarshal(c, cmpClass,
                    f.getAnnotations());
          }
        } else {
          o = unmarshal(c, cmpType,
                  f.getAnnotations());
        }
        Array.set(arr, i++, o);
      }
      return arr;
    }
    return null;
  }

  private Object unmarshallFieldCollections(List<VElement> elems, Field f) throws Exception {
    if (elems.size() == 1) {
      Class<?> typeClass = f.getType();
      VElement listElem = elems.get(0);
      Collection<T> l = (List.class
              .isAssignableFrom(typeClass)) ? new ArrayList<T>()
              : (Set.class.isAssignableFrom(typeClass)) ? new HashSet<T>()
              : (Queue.class
              .isAssignableFrom(typeClass)) ? new LinkedList<T>()
              : (SortedSet.class
              .isAssignableFrom(typeClass)) ? new TreeSet<T>()
              : null;
      if (l == null) {
        throw new VXMLBindingException(
                "Could not map collection element to appropriate implementation");
      }
      // get the field associated annotation for generic
      // type
      GenericCollectionType type = f
              .getAnnotation(GenericCollectionType.class);
      if (type == null) {
        return null;
      }
      for (VElement c : listElem.getChildren()) {
        l.add(unmarshal(c, type.value(),
                f.getAnnotations()));
      }
      return l;
    }
    return null;
  }

  private Object unmarshallFieldMaps(List<VElement> elems, Field f) throws Exception {
    if (elems.size() == 1) {
      Map<Object, T> m = new HashMap<Object, T>();
      GenericMapType type = f
              .getAnnotation(GenericMapType.class);
      if (type == null) {
        return null;
      }
      VElement MapElem = elems.get(0);
      for (VElement c : MapElem.getChildren()) {
        if (c.hasChildren()
                && c.getChildren().size() == 2) {
          VElement key = c.getChild(type.keyMarkup());
          VElement value = c.getChild(type.valueMarkup());
          Object key_ = unmarshal(key, type.key(),
                  f.getAnnotations());
          T value_ = unmarshal(value, type.value(),
                  f.getAnnotations());
          m.put(key_, value_);
        }
      }
      return m;
    }
    return null;
  }

  private void handleFields(VElement element, Class clazz, T instance) throws Exception {
    List<Field> fields = new ArrayList<Field>();
    VObjectMarshaller.getFields(clazz, fields);
    for (final Field f : fields) {
      Class<?> typeClass = f.getType();
      Object propety = null;
      List<VElement> elems = getFieldMapping(f, element);
      if (elems.isEmpty()) {
        Attribute a = f.getAnnotation(Attribute.class);
        if (a != null) {
          String name = f.getName();
          Markup mm = f.getAnnotation(Markup.class);
          if (mm != null) {
            name = mm.name();
          }
          propety = unmarshallAttribute(element, name, f.getAnnotations());
        }
      } else if (typeClass.isArray()) {
        propety = unmarshallFieldArray(elems, f);
      } else if (Collection.class.isAssignableFrom(typeClass)) {
        propety = unmarshallFieldCollections(elems, f);
      } else if (Map.class.isAssignableFrom(typeClass)) {
        propety = unmarshallFieldMaps(elems, f);
      } else {
        propety = unmarshal(elems.get(0),
                typeClass,
                f.getAnnotations());
      }
      if (propety != null) {
        f.setAccessible(true);
        f.set(instance, propety);
        Listener l = f.getAnnotation(Listener.class);
        if (l != null) {
          Class<? extends PropertyListener> plClass = l.value();
          PropertyListener pl = plClass.newInstance();
          pl.onSet(instance, propety);
        }
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private T unmarshallPrimitive(VElement elem, Class<?> clazz, Annotation[] annotations)
          throws VXMLBindingException {
    String value = elem.toContent();
    if (value == null) {
      return null;
    }
    Class primitiveClass = clazz.isPrimitive() ? clazz
            : PRIMITIVE_WRAPPER_MAPPING.get(clazz);
    if (primitiveClass == null) {
      throw new VXMLBindingException("Unknown primitive mapping: "
              + clazz.getName());
    }
    PrimitiveType primitiveType = VObjectMarshaller.PRIMITIVE_MAPPING_TYPES
            .get(primitiveClass);
    if (primitiveType == null) {
      throw new VXMLBindingException(
              "Unknown primitive mapping for primitve class: "
              + primitiveClass.getName());
    }
    try {
      switch (primitiveType) {
        //throw an exception or set the default.
        case BOOLEAN:
          return (T) Boolean.valueOf(value);
        case BYTE:
          return (T) Byte.valueOf(value);
        case CHAR:
          return (T) Character.valueOf(value.charAt(0));
        case DOUBLE:
          return (T) Double.valueOf(value);
        case FLOAT:
          return (T) Float.valueOf(value);
        case INT:
          return (T) Integer.valueOf(value);
        case LONG:
          return (T) Long.valueOf(value);
        case SHORT:
          return (T) Short.valueOf(value);
        case VOID:
        default:
          return null;
      }
    } catch (Exception ex) {
      return (T) new Integer(0);
    }
  }

  private T unmarshallAttribute(VElement element, String attribute, Annotation[] annots) {
    VAttribute at = element.getAttribute(attribute);
    if (at != null) {
      String value = at.getValue();
      Converter cn = VObjectMarshaller.getAnnotation(annots, Converter.class);
      if (cn != null) {
        try {
          com.anosym.vjax.converter.v3.Converter<T, String> cnv = cn.value().newInstance();
          return cnv.convertTo(value);
        } catch (InstantiationException ex) {
          Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
        return (T) value;
      }
    }
    return null;
  }
}
