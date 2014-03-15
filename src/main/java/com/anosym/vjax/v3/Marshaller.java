/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VJaxLogger;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.annotations.v3.CollectionElement;
import com.anosym.vjax.annotations.v3.CollectionElementConverter;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.GenericMapType;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.VBigDecimalConverter;
import com.anosym.vjax.exceptions.VConverterBindingException;
import com.anosym.vjax.util.VJaxUtils;
import static com.anosym.vjax.v3.VObjectMarshaller.PRIMITIVE_WRAPPER_MAPPING;
import static com.anosym.vjax.v3.VObjectMarshaller.getAnnotation;
import com.anosym.vjax.xml.VDocument;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.htmlcleaner.Utils;

/**
 *
 * @author marembo
 */
public class Marshaller<T> {

  private final Class<? extends T> instanceClass;
  private static final Map<String, String> ENTITY_REFERENCES = new HashMap<String, String>();

  static {
    ENTITY_REFERENCES.put("&", "&amp;");
    ENTITY_REFERENCES.put("\"", "&quot;");
    ENTITY_REFERENCES.put("'", "&pos;");
    ENTITY_REFERENCES.put("<", "&lt;");
    ENTITY_REFERENCES.put(">", "&gt;");
  }
  //the key is the object, and the value is its id referenced
  private final Map<Object, String> REFERENCES = new HashMap<Object, String>();

  public Marshaller(Class<? extends T> instanceClass) {
    this.instanceClass = instanceClass;
  }

  public VDocument marshall(T object) {
    try {
      return VDocument.parseDocumentFromString(doMarshall(object));
    } finally {
      REFERENCES.clear();;
    }
  }

  public String doMarshall(T object) {
    try {
      String name = object.getClass().getSimpleName();
      return marshall(object, name, null);
    } finally {
      REFERENCES.clear();;
    }
  }

  @SuppressWarnings("rawtypes")
  private boolean isPrimitiveOrPrimitiveWrapper(Class cls) {
    return PRIMITIVE_WRAPPER_MAPPING.containsKey(cls)
            || PRIMITIVE_WRAPPER_MAPPING.containsValue(cls);
  }

  private String put(String id, Object data) {
    StringBuilder sb = new StringBuilder();
    sb.append("<").append(id);
    if (VJaxUtils.isNullOrEmpty(data)) {
      sb.append("/>");
    } else {
      sb.append(">");
      sb.append(data);
      sb.append("</")
              .append(id)
              .append(">");
    }
    return sb.toString();
  }

  private String putWithAttributes(String id, Object data, Map<String, String> attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append("<").append(id);
    for (Map.Entry<String, String> e : attributes.entrySet()) {
      sb.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
    }
    if (VJaxUtils.isNullOrEmpty(data)) {
      sb.append("/>");
    } else {
      sb.append(">");
      sb.append(data);
      sb.append("</").append(id).append(">");
    }
    return sb.toString();
  }

  @SuppressWarnings("UseSpecificCatch")
  private String marshallFields(T object, String markupName, String data, Annotation[] annotations) {
    List<Field> fields = new ArrayList<Field>();
    VObjectMarshaller.getFields(object.getClass(), fields);
    Map<String, String> attributes = new HashMap<String, String>();
    for (Field f : fields) {
      int mod = f.getModifiers();
      if ((mod & Modifier.STATIC) == 0 && (mod & Modifier.FINAL) == 0
              && f.getAnnotation(Transient.class) == null) {
        // check if it is primitive or string.
        Class cl = f.getType();
        f.setAccessible(true);
        Object value;
        try {
          value = f.get(object);
          if (value == null) {
            continue;
          }
          Converter cn = f.getAnnotation(Converter.class);
          if (cn != null) {
            Class<? extends com.anosym.vjax.converter.v3.Converter> cnv = cn
                    .value();
            if (cnv != null) {
              com.anosym.vjax.converter.v3.Converter<Object, Object> converter = cnv
                      .newInstance();
              value = converter.convertFrom(value);
              cl = value.getClass();
            }
          }
          String markup = f.getName();
          Markup m = f.getAnnotation(Markup.class);
          if (m != null) {
            markup = m.name();
          }
          if (f.getAnnotation(Attribute.class) != null || f.getAnnotation(Id.class) != null) {
            attributes.put(markup, value.toString());
            if (f.getAnnotation(Id.class) != null) {
              //then use references.
              REFERENCES.put(object, value.toString());
            }
          } else if (isPrimitiveOrPrimitiveWrapper(cl)) {
            data += put(markup, value);
          } else if (cl.equals(String.class)) {
            data += put(markup, escapeEntityReference(value.toString()));
          } else {
            data += marshall((T) value, markup,
                    f.getDeclaredAnnotations());
          }
        } catch (Exception e) {
          VJaxLogger.log(Level.SEVERE, data, e);
        }
      }
    }
    if (markupName != null) {
      data = putWithAttributes(markupName, data, attributes);
    }
    return data;
  }

  private String marshallMap(T object, String markupName, Annotation[] annotations) {
    Map m = (Map) object;
    Class c = object.getClass();
    Set entries = m.entrySet();
    String mapEntries = "";
    GenericMapType genericMapType = getAnnotation(annotations, GenericMapType.class);
    String keyMarkup = genericMapType != null ? genericMapType.keyMarkup() : "key";
    String valueMarkup = genericMapType != null ? genericMapType.valueMarkup() : "value";
    String entryMarkup = genericMapType != null ? genericMapType.entryMarkup() : "entry";
    for (Object o : entries) {
      Map.Entry e = (Map.Entry) o;
      Object key = e.getKey();
      Object value = e.getValue();
      String entry = marshall((T) key, keyMarkup, annotations);
      entry += marshall((T) value, valueMarkup, annotations);
      mapEntries += put(entryMarkup, entry);
    }
    if (markupName == null) {
      markupName = c.getSimpleName();
    }
    return put(markupName, mapEntries);
  }

  private String marshallCollection(T object, String markupName, String data, Annotation[] annotations) {
    CollectionElement colElem = null;
    if (annotations != null) {
      colElem = getAnnotation(annotations, CollectionElement.class);
    }
    String elementMarkups = markupName + "_value";
    if (colElem != null) {
      elementMarkups = colElem.elementMarkup();
    }
    String elems = "";
    Collection cols = (Collection) object;
    //do we have a collectionelementconverter
    CollectionElementConverter cec = getAnnotation(annotations, CollectionElementConverter.class);
    com.anosym.vjax.converter.v3.Converter<Object, Object> cnv = null;
    if (cec != null) {
      try {
        cnv = cec.value().newInstance();
      } catch (InstantiationException ex) {
        Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    for (Object o : cols) {
      if (cnv != null) {
        o = cnv.convertFrom(o);
      }
      elems += marshall((T) o, elementMarkups, null);
    }
    data += put(markupName, elems);
    return data;
  }

  @SuppressWarnings("null")
  private String marshallArray(T object, String markupName, String data, Annotation[] annotations) {
    Class c = object.getClass();
    Class cmpClass = c.getComponentType();
    ArrayParented ap = getAnnotation(annotations, ArrayParented.class);
    boolean parented = ap != null
            && ap.componentMarkup() != null
            && ap.componentMarkup().length() != 0;
    String arrayMarkup = markupName;
    if (parented) {
      markupName = ap.componentMarkup();
    }
    int arrLength = Array.getLength(object);
    for (int i = 0; i < arrLength; i++) {
      Object o = Array.get(object, i);
      if (isPrimitiveOrPrimitiveWrapper(cmpClass)
              || cmpClass.equals(String.class)) {
        try {
          data += put(markupName, o);
        } catch (Exception e) {
          Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, data, e);
        }
      } else {
        try {
          data += put(markupName, marshall((T) o, null, null));
        } catch (Exception e) {
          Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, data, e);
        }
      }
    }
    if (parented) {
      data = put(arrayMarkup, data);
    }
    return data;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private String marshall(T object, String markupName,
          Annotation[] annotations) {
    String data = "";
    Class<? extends Object> c = object.getClass();
    //if the object has already been marshalled.
    if (REFERENCES.containsKey(object)) {
      //add a a ref attribute and return.
      String refId = REFERENCES.get(object);
      markupName = markupName == null ? c.getSimpleName() : markupName;
      Map<String, String> attrs = new HashMap<String, String>();
      attrs.put("ref-id", refId);
      data += putWithAttributes(markupName, "", attrs);
      return data;
    }
    if (isPrimitiveOrPrimitiveWrapper(c)) {
      markupName = markupName == null ? c.getSimpleName() : markupName;
      data += put(markupName, object.toString());
    } else if (c.equals(String.class)) {
      markupName = markupName == null ? c.getSimpleName() : markupName;
      data += put(markupName, escapeEntityReference(object.toString()));
    } else if (c.isEnum()) {
      markupName = markupName == null ? c.getSimpleName() : markupName;
      data += put(markupName, ((Enum) object).name());
    } else if (c.isAssignableFrom(BigDecimal.class) && getAnnotation(annotations, Converter.class) == null) {
      VBigDecimalConverter converter = new VBigDecimalConverter();
      markupName = markupName == null ? c.getSimpleName() : markupName;
      try {
        data += put(markupName, converter.convert((BigDecimal) object));
      } catch (VConverterBindingException ex) {
        Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else if (c.isArray()) {
      data = marshallArray(object, markupName, data, annotations);
    } else if (Collection.class.isAssignableFrom(c)) {
      data = marshallCollection(object, markupName, data, annotations);
    } else if (Map.class.isAssignableFrom(c)) {
      data = marshallMap(object, markupName, annotations);
    } else {
      data = marshallFields(object, markupName, data, annotations);
    }
    return data;
  }

  private String getRegex(String value) {
    String regex = "";
    for (String repl : ENTITY_REFERENCES.values()) {
      if (!regex.isEmpty()) {
        regex += "|";
      }
      regex += "(" + value + "[!" + repl.substring(1) + "])";
    }
    return regex;
  }

  private String escapeEntityReference(String str) {
    if (str.contains("&")) {
      str = str.replaceAll("&", "&amp;");
    }
    for (Map.Entry<String, String> e : ENTITY_REFERENCES.entrySet()) {
      if (e.getKey().equals("&")) {
        continue;
      }
      str = str.replaceAll(e.getKey(), e.getValue());
    }
    return str;
  }
}
