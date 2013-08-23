/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import com.anosym.vjax.annotations.XmlMarkup;
import com.anosym.vjax.annotations.AsAttribute;
import com.anosym.vjax.annotations.NoNamespace;
import com.anosym.vjax.annotations.DynamicMarkup;
import com.anosym.vjax.annotations.Marshallable;
import com.anosym.vjax.annotations.Namespaces;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Serialized;
import com.anosym.vjax.annotations.Displayable;
import com.anosym.vjax.annotations.AsAttributes;
import com.anosym.vjax.annotations.Factory;
import com.anosym.vjax.annotations.Constant;
import com.anosym.vjax.annotations.Rescind;
import com.anosym.vjax.annotations.EnumMarkup;
import com.anosym.vjax.annotations.Namespace;
import com.anosym.vjax.annotations.Content;
import com.anosym.vjax.annotations.Comment;
import com.anosym.vjax.annotations.Constructible;
import com.anosym.vjax.annotations.CollectionElement;
import com.anosym.vjax.annotations.Informational;
import com.anosym.vjax.annotations.Element;
import com.anosym.vjax.annotations.Initializer;
import com.anosym.vjax.annotations.Converter;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Position;
import com.anosym.vjax.annotations.WhenEmpty;
import com.anosym.vjax.annotations.CData;
import com.anosym.vjax.annotations.Condition;
import com.anosym.vjax.annotations.Required;
import com.anosym.vjax.annotations.WhenNull;
import com.anosym.vjax.annotations.AsCollection;
import com.anosym.vjax.annotations.IgnoreGeneratedAttribute;
import com.anosym.vjax.annotations.SuppressSuperClass;
import com.anosym.vjax.annotations.Generated;
import com.anosym.vjax.annotations.MapElement;
import com.anosym.vjax.annotations.Alias;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.alias.VAlias;
import com.anosym.vjax.annotations.v3.DefinedAttribute;
import com.anosym.vjax.converter.VCalendarConverter;
import com.anosym.vjax.converter.VConverter;
import com.anosym.vjax.converter.VDateConverter;
import com.anosym.vjax.id.generation.IdGenerator;
import com.anosym.vjax.id.generation.VGenerator;
import com.anosym.vjax.util.VAttributeKeyNormalizer;
import com.anosym.vjax.util.VConditional;
import com.anosym.vjax.util.VMarkupGenerator;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VContent;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import com.anosym.vjax.xml.VNamespace;
import java.awt.Color;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.Permission;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * Marshalls a java object to an xml element The supported types for the object properties include
 * all objects that obey the java bean specification plus Color, Date, Time and Timestamp objects
 * Arrays, lists and maps whose components obey the above rules are also supported <p> The
 * unmarshalling of inner class dependecies has the following issue: If the top-level object is an
 * inner class instance, and any other outer class instance object has a reference to this inner
 * class instance, then a reference to the top-level inner class instance will be specified in the
 * reference attribute for the inner class that is a member of the outer class instance member. thus
 * if the inner-class has marshallable member properties, they will be references to the marshalled
 * properties of the instance member for the member instance of the outer class Example: Consider A
 * being outer class B inner class and C member instance of A and has an instance of B
 * <pre>
 * class A{
 *      private C c;
 *      class B{
 *          private Object value;
 *      }
 * }
 * class C{
 *      private B b;
 * }
 * </pre> //then the marshalling will be as follows: //considering B as the top-level object <p>
 * <pre>
 * &lt;B [attributes]>
 * &lt:referenced-outer-class [attributes]>
 *      &lt;c [attributes]>
 *          &lt;B [attributes]>
 *             &lt;value [attributes] ref-id="inner-instance-id">content&lt/value>
 *          &lt/B>
 *      &lt/c>
 * &lt;/referenced-outer-class>
 * &lt;value references="inner-instance-id"/>
 * &lt/B>
 * Note about enum maps: The unmarshalling will return an instance of EnumMap or VEnumMap, and will
 * ignore any other derived classes.
 * </pre> </p> <p> To avoid the stoppage of unmarshalling of the object tree when a node cannot be
 * set on the unmarshalled instance or when the node instance referred to does not exist in the
 * currently loaded java packages, the {@link #UNMARSHALL_ON_BINDING_EXCEPTION} can be set to true
 * in order to ignore this errors </p>
 *
 * @author Marembo
 * @version 2.0
 */
public final class VMarshaller<T> implements java.io.Serializable {

  /**
   * If this property is set to true, the the unmarshaller will no throw an exception if
   * unmarshalled object cannot be set as a property of its parent. it will silently ignore and
   * continue processing. By default this property is enabled
   */
  public static final String UNMARSHALL_ON_BINDING_EXCEPTION = "com.flemax.vjax.unmarshall.bounded";
  private static final Map<Class, Class> PRIMITIVE_WARPPER_MAPPING = loadPrimitiveMapping();
  private static final boolean unmarshallOnBindingException;

  static {
    unmarshallOnBindingException = "true".equalsIgnoreCase(System.getProperty(UNMARSHALL_ON_BINDING_EXCEPTION, "true").trim());
    String defualtNamespace = System.getProperty(VNamespace.DEFUALT_NAMESPACE_PROPERTY_BINDING, "true");
    System.setProperty(VNamespace.DEFUALT_NAMESPACE_PROPERTY_BINDING, defualtNamespace);
  }

  static Map<Class, Class> loadPrimitiveMapping() {
    Map<Class, Class> maps = new HashMap<Class, Class>();
    maps.put(Boolean.class, boolean.class);
    maps.put(Character.class, char.class);
    maps.put(Byte.class, byte.class);
    maps.put(Short.class, short.class);
    maps.put(Integer.class, int.class);
    maps.put(Long.class, long.class);
    maps.put(Float.class, Float.class);
    maps.put(Double.class, double.class);
    maps.put(Void.class, void.class);
    return maps;
  }
  /**
   * Used for marshalling and unmarshalling During marshalling, the system simply goes through to
   * determine the ids and object mappings
   */
  private Map<Integer, Object> idMapping;
  private boolean enableDefaultConversion;
  private VElement marshallElement;
  private boolean ignoreGeneratedAttributes;
  private boolean noNamespace;
  private List<VDocument> includes;

  /**
   * By default, ignore-property-setting is set to off, and marshalling uses method property values
   */
  public VMarshaller() {
    idMapping = new HashMap<Integer, Object>();
    includes = new ArrayList<VDocument>();
  }

  /**
   * By enabling defualt conversion, the marshaller will be able to convert common objects to
   * simpler xml representation and where alias are defined the marshaller will use the defined
   * aliases
   */
  public VMarshaller(boolean enableDefaultConversion) {
    this();
    this.enableDefaultConversion = enableDefaultConversion;
  }

  public VElement marshall(T o) throws VXMLBindingException {
    String markup = o.getClass().getSimpleName();
    if ((markup == null || markup.isEmpty()) && o.getClass().isAnonymousClass()) {
      markup = o.getClass().getName();
      int ix1 = markup.lastIndexOf("."),
              ix2 = markup.lastIndexOf("$");
      markup = markup.substring(ix1 + 1, ix2);
    }
    VElement el = marshall(o, markup);
    return el;
  }

  public VDocument marshallDocument(T o) throws VXMLBindingException {
    try {
      VDocument doc = new VDocument();
      doc.setRootElement(marshall(o));
      //are there includes
      if (!includes.isEmpty()) {
        doc.setIncludes(includes);
      }
      return doc;
    } finally {
      includes.clear();
      this.idMapping.clear();
    }
  }

  public VElement marshall(T o, String markup) throws VXMLBindingException {
    try {
      //marshall based on the mapping
      Class clazz = o.getClass();
      VElement e = new VElement(VMarshallerConstants.DEFAULT_NAME);
      if (markup != null) {
        e.setMarkup(markup);
      }
      ignoreGeneratedAttributes = getAnnotation(clazz, IgnoreGeneratedAttribute.class) != null;
      noNamespace = getAnnotation(clazz, NoNamespace.class) != null;
      System.out.println("NoNamespace: " + noNamespace);
      addAttribute(e, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, clazz.getName()));
      doMarshall(e, o);
      return e;
    } finally {
      idMapping.clear();
      ignoreGeneratedAttributes = false;
      noNamespace = false;
    }
  }

  public T unmarshall(VDocument doc) throws VXMLBindingException {
    try {
      VElement elem = doc.getRootElement();
      this.marshallElement = elem;
      T t = (T) unmarshallObject(elem);
      return t;
    } finally {
      this.marshallElement = null;
      idMapping.clear();
    }
  }

  /**
   * Unmarshalls the specified document based on the specified document schema This method should be
   * called when the document specifically refers to its schema file
   *
   * @param doc
   * @param documentSchema
   * @return
   * @throws VXMLBindingException
   */
  public T unmarshall(VDocument doc, VDocument documentSchema) throws VXMLBindingException {
    try {
      VElement elem = doc.getRootElement();
      this.marshallElement = elem;
      T t = (T) unmarshallObject(elem);
      return t;
    } finally {
      this.marshallElement = null;
      idMapping.clear();
    }
  }

  /**
   * Unmarshalls an xml document to a java object Note that if the element has namespace definition,
   * it will be lost unless this is defined as annotation description in the class
   *
   * @param elem
   * @return
   * @throws VXMLBindingException
   */
  public T unmarshall(VElement elem) throws VXMLBindingException {
    SecurityManager man = System.getSecurityManager();
    if (man != null) {
      System.setSecurityManager(new SecurityManager() {
        @Override
        public void checkPermission(Permission perm) {
          if (perm instanceof ReflectPermission) {
            return;
          }
          super.checkPermission(perm);
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
          if (perm instanceof ReflectPermission) {
            return;
          }
          super.checkPermission(perm, context);
        }
      });
    }
    try {
      this.marshallElement = elem;
      T t = (T) unmarshallObject(elem);
      return t;
    } finally {
      this.marshallElement = null;
      idMapping.clear();
      //return the state of the manager
      System.setSecurityManager(man);
    }
  }

  private T unmarshallArray(VElement elem) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, VXMLBindingException {
    T o;
    //create the array
    String ct = elem.getAttribute(VMarshallerConstants.ARRAY_COMPONENT_CLASS_ATTRIBUTE).getValue();
    String l = elem.getAttribute(VMarshallerConstants.ARRAY_LENGTH_ATTRIBUTE).getValue();
    Class cl = null;
    VAttribute va = elem.getAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE);
    if (va != null) {
      String p = va.getValue();
      if (p.equalsIgnoreCase("true")) {
        String wr = elem.getAttribute(VMarshallerConstants.PRIMITIVE_WRAPPER_ATTRIBUTE).getValue();
        Object ob = Class.forName(wr).getConstructor(new Class[]{String.class}).newInstance(new Object[]{"0"});
        if (ob instanceof Integer) {
          cl = int.class;
        }
        if (ob instanceof Long) {
          cl = long.class;
        }
        if (ob instanceof Double) {
          cl = double.class;
        }
        if (ob instanceof Float) {
          cl = float.class;
        }
        if (ob instanceof Byte) {
          cl = byte.class;
        }
        if (ob instanceof Short) {
          cl = short.class;
        }
        if (ob instanceof Character) {
          cl = char.class;
        }
      }
    } else {
      cl = Class.forName(ct);
    }
    o = (T) Array.newInstance(cl, Integer.parseInt(l));
    List<VElement> elems = elem.getChildren();
    int i = 0;
    for (VElement v : elems) {
      Object ob = unmarshallObject(v);
      Array.set(o, i++, ob);
    }
    return o;
  }

  private T unmarshallPrimitive(VElement elem) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    String wr = elem.getAttribute(VMarshallerConstants.PRIMITIVE_WRAPPER_ATTRIBUTE).getValue();
    String value = elem.toContent();
    Object o = null;
    if (!value.isEmpty()) {
      Object ob = Class.forName(wr).getConstructor(new Class[]{String.class}).newInstance(new Object[]{"0"});
      if (ob instanceof Integer) {
        o = Integer.parseInt(value);
      } else if (ob instanceof Long) {
        o = Long.parseLong(value);
      } else if (ob instanceof Double) {
        o = Double.parseDouble(value);
      } else if (ob instanceof Float) {
        o = Float.parseFloat(value);
      } else if (ob instanceof Byte) {
        o = Byte.parseByte(value);
      } else if (ob instanceof Short) {
        o = Short.parseShort(value);
      } else if (ob instanceof Character) {
        value = value.trim();
        o = value.charAt(0);
      }
    }
    VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
    if (at != null) {
      String val = at.getValue();
      int id = Integer.parseInt(val);
      idMapping.put(id, o);
    }
    return (T) o;
  }

  private T unmarshallCollection(VElement elem, List<VElement> elems, Class clazz, VAttribute initAttr, T o) throws VXMLBindingException {
    List<VElement> elems_;
    VElement _el = null;
    //for compatibility purposes with the collections without elements markup
    //the elements markup is to be removed in the future
    try {
      _el = elem.getChild(VMarshallerConstants.COLLECTION_ELEMENT_MARKUP);
    } catch (Exception e) {
    }
    if (_el != null) {
      elems_ = _el.getChildren();
      elems.remove(_el);
    } else {
      elems_ = elems;
    }
    if (o == null) {
      o = newInstance(initAttr, clazz);
    }
    for (Iterator<VElement> it = elems_.iterator(); it.hasNext();) {
      /**
       * We remove the collection children elements as we marshall the collection. If this
       * collection has properties, other than its children content, the children must have been
       * wrapped into a collection_element_markup, and hence this removal will not affect the
       * children. This is therefore prone to ClassCastException when different type of objects are
       * added to the collection, when the children and properties are mixed up.
       */
      VElement v = it.next();
      it.remove();
      //be sure that the child element is not an empty entry markup
      VAttribute a = v.getAttribute(VMarshallerConstants.COLLECTION_EMPTY_MARKUP);
      if (a == null) {
        Object ob = unmarshallObject(v);
        ((Collection) o).add(ob);
      }
    }
    if (elems.isEmpty()) {
      VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
      if (at != null) {
        String val = at.getValue();
        int id = Integer.parseInt(val);
        idMapping.put(id, o);
      }
      return (T) o;
    }
    return o;
  }

  private T unmarshallMap(VElement elem, List<VElement> elems, Class clazz, VAttribute initAttr, T o) throws VXMLBindingException {
    if (o == null) {
      //special case for handling enum maps
      if (EnumMap.class.isAssignableFrom(clazz)) {
        try {
          // retrieve the first entry in the map list
          VElement entry = elems.get(0);
          // get the key class, which is an enum
          VElement key = entry.findChild(new VAttribute(VMarshallerConstants.KEY_ATTRIBUTE, "true"));
          Class keyClass = Class.forName(key.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE).getValue());
          o = newInstance(initAttr, clazz, keyClass);
        } catch (Exception ex) {
          throw new VXMLBindingException(ex);
        }
      } else {
        o = newInstance(initAttr, clazz);
      }
    }
    //create the map
    for (ListIterator<VElement> it = elems.listIterator(); it.hasNext();) {
      VElement v = it.next();
      it.remove();//remove the instance
      List<VElement> cv = v.getChildren();
      VAttribute a = v.getAttribute(VMarshallerConstants.MAP_EMPTY_MARKUP);
      if (cv.size() == 2 && a == null) { //unmarshall only if not an empty entry markup
        //which we know
        VElement ke = cv.get(0);
        VElement ve = cv.get(1);
        Object key = null;
        Object value = null;
        if (ke.getAttribute(VMarshallerConstants.KEY_ATTRIBUTE) != null) {
          key = unmarshallObject(ke);
        } else if (ke.getAttribute(VMarshallerConstants.VALUE_ATTRIBUTE) != null) {
          value = unmarshallObject(ke);
        }
        if (ve.getAttribute(VMarshallerConstants.KEY_ATTRIBUTE) != null) {
          key = unmarshallObject(ve);
        } else if (ve.getAttribute(VMarshallerConstants.VALUE_ATTRIBUTE) != null) {
          value = unmarshallObject(ve);
        }
        if (key != null && value != null) {
          ((Map) o).put(key, value);
        }
      }
    }
    VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
    if (at != null) {
      String val = at.getValue();
      int id = Integer.parseInt(val);
      idMapping.put(id, o);
    }
    return (T) o;
  }

  private T unmarshallBasic(VElement elem, VAttribute initAttr, Object o, Class clazz) throws VXMLBindingException {
    try {
      String val = elem.toContent();
      if (val == null) {
        o = newInstance(initAttr, clazz);
      } else {
        if (o == null) {
          if (clazz.equals(String.class)) {
            o = val;
          } else if (clazz.isPrimitive() || isWrapperType(clazz)) {
            Method mm = clazz.getMethod("valueOf", String.class);
            mm.setAccessible(true);
            o = mm.invoke(null, val);
          } else if (clazz == BigDecimal.class || clazz == BigInteger.class) {
            Constructor con = clazz.getConstructor(String.class);
            o = con.newInstance(val);
          } else {
            //check if it is instance of a calendar or date
            //get superclass until it becomes a calendar or object
            Class spr = clazz;
            while (spr != Object.class && spr != Calendar.class && spr != Date.class) {
              spr = spr.getSuperclass();
            }
            if (spr == Calendar.class) {
              //create basic converter
            } else if (spr == Date.class) {
              //create basic converter
            } else {
              //we consider if it can be an object with empty elements
              o = newInstance(initAttr, clazz);
            }
          }
        }
      }
      return (T) o;
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      }
      throw new VXMLBindingException(e);
    }
  }

  private T unmarshallEnum(VElement elem, Class clazz) throws VXMLBindingException {
    String name;
    //for compatibility issues, we need to check if the instance has enum_value specified
    List<VElement> list = elem.getChildren();
    //the class may define enum-value markup
    EnumMarkup em = (EnumMarkup) clazz.getAnnotation(EnumMarkup.class);
    if (em != null && elem.isParent(em.value())) {
      VElement enumValue = elem.getChild(em.value());
      name = enumValue.toContent();
      list.remove(enumValue);
    } else if (elem.isParent(VMarshallerConstants.ENUM_VALUE_ELEMENT)) {
      VElement e = elem.getChild(VMarshallerConstants.ENUM_VALUE_ELEMENT);
      name = e.toContent();
      list.remove(e);
    } else {
      VAttribute va = elem.getAttribute(VMarshallerConstants.VALUE_ATTRIBUTE);
      if (va != null) {
        name = va.getValue();
      } else {
        name = elem.toContent();
      }
    }
    Object eo = Enum.valueOf(clazz, name);
    //check if it has properties
    if (!list.isEmpty()) {
      //then it has properties
      this.unmarshallProperties(list, clazz, eo);
      return (T) eo;
    }
    VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
    if (at != null) {
      String val = at.getValue();
      int id = Integer.parseInt(val);
      idMapping.put(id, eo);
    }
    return (T) eo;
  }

  private T unmarshallBeanOrBasicObject(VElement elem, List<VElement> elems, VAttribute initAttr, Class clazz, Object o) throws VXMLBindingException {
    try {
      if (o == null) {
        if (clazz.equals(Color.class)) {
          o = clazz.getConstructor(new Class[]{int.class}).newInstance(new Object[]{0});
        } else {
          //we determine if it is date or time
          if (clazz.equals(java.sql.Date.class)
                  || clazz.equals(java.sql.Time.class)
                  || clazz.equals(java.sql.Timestamp.class)
                  || clazz.equals(java.util.Date.class)) {
            o = clazz.getConstructor(new Class[]{long.class}).newInstance(new Object[]{0});
          } else if (clazz == MathContext.class) {
            //we need to unmarshall the properties and then instantiate it as initializers attribute
            //we are sure there are only two properties because the class is immutable and cannot be overidden
            if (elems.size() == 2) {
              VElement precisionEleme = null;
              VElement roundingModeElem = null;
              //assign them from the list
              for (VElement ve : elems) {
                if (ve.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE).getValue().equals(Integer.class.getName())) {
                  precisionEleme = ve;
                }
                if (ve.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE).getValue().equals(RoundingMode.class.getName())) {
                  roundingModeElem = ve;
                }
              }
              //then unmarshall the integer and the rounding mode
              unmarshallObject(precisionEleme);
              unmarshallObject(roundingModeElem);
              String params = "[" + precisionEleme.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE).getValue() + "," + roundingModeElem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE).getValue() + "]";
              VAttribute initializers = new VAttribute(VMarshallerConstants.INITIALIZERS_ATTRIBUTE, params);
              o = newInstance(initializers, clazz);
            }
          } else {
            o = newInstance(initAttr, clazz);
          }
        }
      }
      VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
      if (at != null) {
        String val_ = at.getValue();
        int id = Integer.parseInt(val_);
        idMapping.put(id, o);
      }
      this.unmarshallProperties(elems, clazz, o);
      return (T) o;
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      }
      throw new VXMLBindingException(o.getClass().getName(), e);
    }
  }

  private T unmarshallSerializable(VElement elem) throws VXMLBindingException {
    ByteArrayInputStream inn = null;
    ObjectInputStream objInn = null;
    try {
      //get the content
      //at this point we believe that the element has been determined to be serialized
      VAttribute isAliased = elem.getAttribute(VMarshallerConstants.ALIAS_ATTRIBUTE);
      String content = elem.toContent();
      HexBinaryAdapter adpter = new HexBinaryAdapter();
//            //since this is in array form, convert it to byte
//            content = content.substring(1, content.length() - 1); //remove []
//            String[] bytes = content.split(",\\s"); //split at the boundary of comma-space separated list
      byte[] data = adpter.unmarshal(content);
//            int i = 0;
//            for (String bStr : bytes) {
//                byte b = Byte.parseByte(bStr);
//                data[i++] = b;
//            }
      inn = new ByteArrayInputStream(data);
      objInn = new ObjectInputStream(inn);
      Object instance = objInn.readObject();
      //is it an alias
      if (isAliased != null) {
        //get tha alias
        VAlias<T, Object> alias = (VAlias<T, Object>) Class.forName(isAliased.getValue()).newInstance();
        return alias.getInstance(instance);
      }
      return (T) instance;
    } catch (Exception ex) {
      if (ex instanceof VXMLBindingException) {
        throw (VXMLBindingException) ex;
      }
      throw new VXMLBindingException(ex);
    } finally {
      if (inn != null) {
        try {
          inn.close();
        } catch (IOException ex) {
          Logger.getLogger(VMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (objInn != null) {
        try {
          objInn.close();
        } catch (IOException ex) {
          Logger.getLogger(VMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  private T unmarshallOnConverter(VElement elem, VAttribute conAttr) throws VXMLBindingException {
    try {
      //we have a converter
      Class conClass = Class.forName(conAttr.getValue());
      VConverter converter = (VConverter) conClass.newInstance();
      return (T) converter.convert(elem.toContent());
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      }
      throw new VXMLBindingException(e);
    }
  }

  private T unmarshallOnAlias(VElement elem, Class clazz, VAttribute aliasAttr) throws VXMLBindingException {
    try {
      //lets marshall the alias
      Object alias = this.unmarshallObject(elem, clazz);
      //initiate the alias
      Class aliasClass = Class.forName(aliasAttr.getValue());
      VAlias<T, Object> valias = (VAlias<T, Object>) aliasClass.newInstance();
      return (T) valias.getInstance(alias);
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      }
      throw new VXMLBindingException(e);
    }
  }

  private T unmarshallOnReference(VElement elem, VAttribute _at__) throws VXMLBindingException {
    String val_ = _at__.getValue();
    int id = Integer.parseInt(val_);
    Object tmp = idMapping.get(id);
    //since this is a reference we simply return the reference we have
    if (tmp != null) {
      return (T) tmp;
    } else {
      //find an element with the specified reference attribute
      VElement el;
      try {
        el = elem.findChild(new VAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE, val_));
      } catch (Exception e) {
        el = marshallElement.findChild(new VAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE, val_));
      }
      if (el == null) {
        throw new VXMLBindingException("Broken XML Document: Circular Reference Missing=" + id);
      }
      return (T) unmarshall(el);
    }
  }

  private void unmarshallOnInitialization(VElement elem, VAttribute initAttr, Object o) throws Exception {
    //get the elements specified and unmarshall them if they are not yet marshalled
    String inits = initAttr.getValue();
    inits = inits.substring(1, inits.length() - 1);
    String vals[] = inits.split(",");
    List<VElement> params = new ArrayList<VElement>();
    for (String v : vals) {
      VElement vElem = elem.findChild(new VAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE, v.trim()));
      params.add(vElem);
    }
    unmarshallProperties(params, null, o);
  }

  private T unmarshallOnInnerClass(VElement elem, VAttribute at_, VAttribute initAttr) throws ClassNotFoundException, VXMLBindingException {
    Class cl = Class.forName(elem.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE).getValue());
    //lets determine if the outer class is already instantiated
    VAttribute _at_ = elem.getAttribute(VMarshallerConstants.OUTER_CLASS_REFERENCE_ATTRIBUTE);
    if (_at_ == null) {
      throw new VXMLBindingException("Inner class without outer reference instance");
    }
    int refId = Integer.parseInt(_at_.getValue());
    Object ref = idMapping.get(refId);
    if (ref == null) {
      //then we must find the next element that has a reference to it
      VElement el = elem.findChild(VMarshallerConstants.OUTER_CLASS_REFERENCE_ELEMENT_MARKUP);
      if (el == null) {
        //lets look from the root parent
        el = marshallElement.findChild(VMarshallerConstants.OUTER_CLASS_REFERENCE_ELEMENT_MARKUP);
      }
      //remove this child from being unmarshalled again
      elem.removeChild(el);
      ref = unmarshallObject(el);
    }
    return newInstance(initAttr, cl, ref);
  }

  private T unmarshallObject(VElement elem, Class clazz) throws VXMLBindingException {
    try {
      //first things first, are we serialized?
      VAttribute serialAttr = elem.getAttribute(VMarshallerConstants.SERIALIZED_ATTRIBUTE);
      if (serialAttr != null && "true".equalsIgnoreCase(serialAttr.getValue())) {
        return unmarshallSerializable(elem);
      }
      Object o = null;
      List<VElement> elems = elem.getInstanceChildren();
      //check if we are referencing another object
      VAttribute _at__ = elem.getAttribute(VMarshallerConstants.OBJECT_CIRCULAR_REFERENCE_ATTRIBUTE);
      if (_at__ != null) {
        return unmarshallOnReference(elem, _at__);
      }
      //are we working with converters and aliases
      VAttribute conAttr = elem.getAttribute(VMarshallerConstants.CONVERTER_ATTRIBUTE);
      VAttribute aliasAttr = elem.getAttribute(VMarshallerConstants.ALIAS_ATTRIBUTE);
      if (conAttr != null) {
        return unmarshallOnConverter(elem, conAttr);
      } else if (aliasAttr != null) {
        //be sure we do not recursively call this method
        Class aliasClass = Class.forName(aliasAttr.getValue());
        if (aliasClass != clazz) {
          return unmarshallOnAlias(elem, aliasClass, aliasAttr);
        }
      }
      //does the class need some initializers?
      VAttribute initAttr = elem.getAttribute(VMarshallerConstants.INITIALIZERS_ATTRIBUTE);
      if (initAttr != null) {
        unmarshallOnInitialization(elem, initAttr, o);
      }
      //we need to unmarshall an inner class over here
      VAttribute at_ = elem.getAttribute(VMarshallerConstants.INNER_CLASS_ATTRIBUTE);
      if (at_ != null) {
        o = unmarshallOnInnerClass(elem, at_, initAttr);
      }
      //check if it is an array
      VAttribute a = elem.getAttribute(VMarshallerConstants.ARRAY_ATTRIBUTE);
      if (a != null && a.getValue().equalsIgnoreCase("true")) {
        Object tmp = unmarshallArray(elem);
        VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
        if (at != null) {
          String val = at.getValue();
          int id = Integer.parseInt(val);
          idMapping.put(id, tmp);
        }
        return (T) tmp;
      }
      //otherwise it is an object
      //check if it is primitive
      VAttribute va = elem.getAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE);
      if (va != null && va.getValue().equalsIgnoreCase("true")) {
        return unmarshallPrimitive(elem);
      }
      //check if it is an enum
      va = elem.getAttribute(VMarshallerConstants.ENUM_ATTRIBUTE);
      if (va != null && va.getValue().equalsIgnoreCase("true")) {
        return unmarshallEnum(elem, clazz);
      }
      //check if it is a collection
      VAttribute aa = elem.getAttribute(VMarshallerConstants.COLLECTION_ATTRIBUTE);
      if (aa != null && aa.getValue().contains("true")) {
        o = unmarshallCollection(elem, elems, clazz, initAttr, (T) o);
        //if the collection contained only child elements
        //return, otherwise we may continue to marshall the properties
        if (elems.isEmpty()) {
          return (T) o;
        }
      } else {
        //check if it is a map
        aa = elem.getAttribute(VMarshallerConstants.MAP_ATTRIBUTE);
        if (aa != null && aa.getValue().trim().equalsIgnoreCase("true")) {
          o = unmarshallMap(elem, elems, clazz, initAttr, (T) o);
          //if the mapcontains no other properties apart from the map members
          //then return
          if (elems.isEmpty()) {
            return (T) o;
          }
        }
      }
      //if the element has children, then it must comply with the
      //java bean specification
      if (elems.isEmpty() && o == null) {
        //then we have either primitive wrapper or a string or simply an object without values
        VAttribute converter = elem.getAttribute(VMarshallerConstants.CONVERTER_ATTRIBUTE);
        if (converter != null) {
          o = unmarshallOnConverter(elem, converter);
        } else {
          o = unmarshallBasic(elem, initAttr, o, clazz);
        }
        VAttribute at = elem.getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
        if (at != null) {
          String val_ = at.getValue();
          int id = Integer.parseInt(val_);
          idMapping.put(id, o);
        }
        return (T) o;
      } else {
        return unmarshallBeanOrBasicObject(elem, elems, initAttr, clazz, o);
      }
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      } else {
        throw new VXMLBindingException(e);
      }
    }
  }

  private T unmarshallObject(VElement elem) throws VXMLBindingException {
    try {
      VAttribute a = elem.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE);
      Class clazz = null;
      if (a != null) {
        String val = a.getValue();
        VAttribute primAttr = elem.getAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE);
        //we must not be working with primitives
        if (primAttr != null && "true".equalsIgnoreCase(primAttr.getValue().trim())) {
          clazz = null; //just parse null
        } else if (val != null && !val.isEmpty()) {
          clazz = Class.forName(val);
        }
      }
      return unmarshallObject(elem, clazz);
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      } else {
        throw new VXMLBindingException(e);
      }
    }
  }

  private T newInstance(VAttribute initAttr, Class clazz) throws VXMLBindingException {
    return newInstance(initAttr, clazz, null);
  }

  private T newInstance(VAttribute initAttr, Class<?> clazz, Object zeroOrder) throws VXMLBindingException {
    try {
      Class[] paramCls;
      Object[] params;
      if (initAttr != null) {
        //then we have inputs
        String val = initAttr.getValue();
        //we have demarcations
        val = val.substring(1, val.length() - 1); //remove []
        String[] inits = val.split(",");
        int i = 0;
        int j = 0;
        int length = inits.length;
        if (zeroOrder != null) {
          length++;
          i = 1;
          j = 1;
        }
        paramCls = new Class[length];
        params = new Object[length];
        if (zeroOrder != null) {
          paramCls[0] = zeroOrder.getClass();
          params[0] = zeroOrder;
        }
        for (; i < paramCls.length; i++) {
          String idStr = inits[i - j].trim();
          int id = Integer.parseInt(idStr);
          Object _o_ = idMapping.get(id);
          params[i] = _o_;
          paramCls[i] = _o_.getClass();
        }
      } else {
        if (zeroOrder != null) {
          paramCls = new Class[]{zeroOrder.getClass()};
          params = new Object[]{zeroOrder};
        } else {
          paramCls = new Class[0];
          params = new Object[0];
        }
      }
      //ensure that the class is not a factory class
      Factory ff = clazz.getAnnotation(Factory.class);
      if (ff != null) {
        //find the factory method
        for (Method m : clazz.getDeclaredMethods()) {
          int mod = m.getModifiers();
          if (Modifier.isStatic(mod) && m.getAnnotation(Factory.class) != null) {
            return (T) m.invoke(null, new Object[]{});
          }
        }
      }
      Constructor[] cons = clazz.getDeclaredConstructors();
      //we iterate through the constructors and find the right one.
      //it may be taking super class intance or interfaces as parameters
      Constructor con = null;
      SEARCH:
      for (Constructor c : cons) {
        Class[] paramTypes = c.getParameterTypes();
        if (paramTypes.length == 0 && paramCls.length == 0) {
          con = c;
          break;
        }
        if (paramTypes.length == paramCls.length) {
          //posibility that it is the construtor we want
          for (int j = 0; j < paramTypes.length; j++) {
            Class paramClass = paramTypes[j];
            Class expectedClass = paramCls[j];
            //these two must correlate,
            if (!paramClass.equals(expectedClass) && !paramClass.isAssignableFrom(expectedClass)) {
              //it may be a primitive
              if (isWrapperType(expectedClass)) {
                //then check if it is a wrapper for this class
                Class prim = PRIMITIVE_WARPPER_MAPPING.get(expectedClass);
                if (prim != paramClass) {
                  continue SEARCH;
                }
              } else if (isWrapperType(paramClass)) {
                //then check if it is a wrapper for this class
                Class prim = PRIMITIVE_WARPPER_MAPPING.get(paramClass);
                if (prim != expectedClass) {
                  continue SEARCH;
                }
              } else {
                continue SEARCH;
              }
            }
          }
          //if we reached here, we found what we were looking for
          con = c;
          break;
        }
      }
      con.setAccessible(true);
      return (T) con.newInstance(params);
    } catch (Exception ex) {
      if (unmarshallOnBindingException) {
        return null;
      }
      throw new VXMLBindingException(ex);
    }
  }

  private void unmarshallProperties(List<VElement> elems, Class clazz, Object o) throws VXMLBindingException {
    try {
      //it must be a java bean component with set and getter methods
      for (VElement v : elems) {
        //for now ignore the xi:include elements
        VNamespace namespace = v.getAssociatedNamespace();
        if (namespace != null && namespace.equals(VNamespace.XINCLUDE_NAMESPACE)) {
          continue;
        }
        //did it contain a markup annotation
        VAttribute prop = v.getAttribute(VMarshallerConstants.OBJECT_PROPERTY_ATTRIBUTE);
        String s = v.getMarkup();
        if (prop != null) {
          s = prop.getValue();
        } else {
          //did we define an escape sequence
          VAttribute escapeSequence = v.getAttribute(VElement.ESCAPE_SEQUENCE);
          if (escapeSequence != null) {
            String sequence = escapeSequence.getValue();
            String[] seqs = sequence.split(";");
            char[] charSeqs = s.toCharArray();
            for (String ss : seqs) {
              String[] rep = ss.split(":");
              int index = Integer.parseInt(rep[0]);
              char c = rep[1].charAt(0);
              charSeqs[index] = c;
            }
            s = new String(charSeqs);
          }
        }
        Class cl = Class.forName(v.getAttribute(VMarshallerConstants.CLASS_ATTRIBUTE).getValue());
        String props = s.substring(0, 1).toUpperCase() + s.substring(1);
        s = "set" + props;
        //lets find the equivalent getter method. it may specify information attribute
        String getter = "get" + props;
        if (cl.getName().equals("boolean") || cl.getName().equals("java.lang.Boolean")) {
          getter = "is" + props;
        }
        Method mm = null;
        try {
          Method gM = null;
          try {
            gM = clazz.getMethod(getter, new Class[]{});
          } catch (Exception ee) {
          }
          if (gM != null) {
            Informational info = gM.getAnnotation(Informational.class);
            Marshallable ms = gM.getAnnotation(Marshallable.class);
            if (info == null && (ms == null || (ms.marshal() && ms.write())) && v.getAttribute(VMarshallerConstants.INFORMATIONAL_ATTRIBUTE) == null) {
              mm = getMethod(clazz, s, cl);
              if (mm == null) {
                //try primitives
                if (cl.equals(Integer.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{int.class});
                } else if (cl.equals(Long.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{long.class});
                } else if (cl.equals(Character.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{char.class});
                } else if (cl.equals(Double.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{double.class});
                } else if (cl.equals(Float.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{float.class});
                } else if (cl.equals(Byte.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{byte.class});
                } else if (cl.equals(Short.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{short.class});
                } else if (cl.equals(Boolean.class)) {
                  mm = clazz.getDeclaredMethod(s, new Class[]{boolean.class});
                }
              }
            }
          }

        } catch (NoSuchMethodException e) {
          if (!unmarshallOnBindingException) {
            throw e;
          }
        }
        //was this property null?
        VAttribute nullAttr = v.getAttribute(VMarshallerConstants.NULL_PROPERTY);
        Object ob = null;
        //is the property null
        //technically, even the property of this object that were marshalled for informational purposes will be
        //ignored
        //we check the false status since it may have been changed by adding
        //the property values and thus liable for instantiation
        if (nullAttr == null || "false".equalsIgnoreCase(nullAttr.getValue())) {
          ob = unmarshallObject(v);
        }
        if (mm != null) {
          //we do this at this point after we are sure we have managed to fully understand the property
          mm.setAccessible(true);
          mm.invoke(o, new Object[]{ob});
        }
      }
    } catch (Exception ee) {
      if (ee instanceof VXMLBindingException) {
        throw (VXMLBindingException) ee;
      }
      throw new VXMLBindingException(o.getClass().getName(), ee);
    }
  }

  private Method getMethod(Class clazz, String name, Class param) {
    Method m = null;
    try {
      m = clazz.getMethod(name, new Class[]{param});
    } catch (Exception ee) {
      //then lets iterate
      List<Class> superCz = new ArrayList<Class>(Arrays.asList(param.getInterfaces()));
      Class sc = param.getSuperclass();
      if (sc != null) {
        superCz.add(sc);
      }
      for (Class c : superCz) {
        Method m_ = getMethod(clazz, name, c);
        if (m_ != null) {
          return m_;
        }
      }
    }
    return m;
  }

  private void doMarshall(VElement elem, T o) throws VXMLBindingException {
    VNamespace[] vNamespace = new VNamespace[0];
    if (!noNamespace) {
      Namespaces namespaces = o.getClass().getAnnotation(Namespaces.class);
      if (namespaces != null && namespaces.namespaces().length > 0) {
        int i = 0;
        vNamespace = new VNamespace[namespaces.namespaces().length];
        for (Namespace namespace : namespaces.namespaces()) {
          vNamespace[i++] = new VNamespace(namespace.prefix(), namespace.uri(),
                  namespace.elementFormDefault(), namespace.attributeFormDefault(),
                  namespace.includeFormDefaultAttributeOnFalse());
        }
      }
      Namespace namespace = o.getClass().getAnnotation(Namespace.class);
      if (namespace != null) {
        vNamespace = Arrays.copyOf(vNamespace, vNamespace.length + 1);
        vNamespace[vNamespace.length - 1] = new VNamespace(namespace.prefix(), namespace.uri(),
                namespace.elementFormDefault(), namespace.attributeFormDefault(),
                namespace.includeFormDefaultAttributeOnFalse());
      } else if (vNamespace.length == 0) {
        //did we define the default namespace
        //Leave the property for compatibility issues
        String defValue = System.getProperty(VNamespace.DEFUALT_NAMESPACE_PROPERTY_BINDING, "false");
        if (defValue.trim().equalsIgnoreCase("true")) {
          vNamespace = new VNamespace[]{VNamespace.VJAX_NAMESPACE};
        }
      }
    }
    doMarshall(elem, o, vNamespace, null, null, null, null, false, false, null);

  }

  private void doMarshallArray(VElement elem, T o, Class clazz, VNamespace namespace[], String elementMarkup) throws VXMLBindingException {
    if (elementMarkup == null || elementMarkup.isEmpty()) {
      elementMarkup = VMarshallerConstants.ELEMENT_ELEMENT_MARKUP;
    }
    addAttribute(elem, new VAttribute(VMarshallerConstants.ARRAY_ATTRIBUTE, "true"));
    addAttribute(elem, new VAttribute(VMarshallerConstants.ARRAY_LENGTH_ATTRIBUTE, Array.getLength(o) + ""));
    addAttribute(elem, new VAttribute(VMarshallerConstants.ARRAY_COMPONENT_CLASS_ATTRIBUTE, clazz.getComponentType().getName()));
    if (clazz.getComponentType().isPrimitive()) {
      addAttribute(elem, new VAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE, clazz.getComponentType().isPrimitive() + ""));
      addAttribute(elem, new VAttribute(VMarshallerConstants.PRIMITIVE_WRAPPER_ATTRIBUTE, Array.get(o, 0).getClass().getName()));
      int len = Array.getLength(o);
      for (int i = 0; i < len; i++) {
        Object ob = Array.get(o, i);
        VElement vb = new VElement(elementMarkup);
        addAttribute(vb, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, clazz.getComponentType().getName()));
        addAttribute(vb, new VAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE, clazz.getComponentType().isPrimitive() + ""));
        addAttribute(vb, new VAttribute(VMarshallerConstants.PRIMITIVE_WRAPPER_ATTRIBUTE, Array.get(o, 0).getClass().getName()));
        elem.addChild(vb);
        doMarshall(vb, (T) ob, namespace, null, null, null, null, false, false, null);
      }
    } else {
      Object[] ob = (Object[]) o;
      for (Object b : ob) {
        if (b != null) {
          String name = elementMarkup;
          if (b.getClass().isArray()) {
            name += "-array";
          }
          VElement vb = new VElement(name);
          addAttribute(vb, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, b.getClass().getName()));
          elem.addChild(vb);
          doMarshall(vb, (T) b, namespace, null, null, null, null, false, false, null);
        }
      }
    }
  }

  private void addAttribute(VElement elem, VAttribute attr) {
    if (!ignoreGeneratedAttributes) {
      elem.addAttribute(attr);
    }
  }

  private void doMarshallSerializable(VElement elem, T o) throws VXMLBindingException {
    ByteArrayOutputStream out = null;
    ObjectOutputStream objOut = null;
    try {
      //here we are to only serialize the object
      out = new ByteArrayOutputStream();
      objOut = new ObjectOutputStream(out);
      objOut.writeObject(o);
      HexBinaryAdapter adapter = new HexBinaryAdapter();
      byte[] data = out.toByteArray();
      String content = adapter.marshal(data);
      elem.addChild(new VContent(content));
    } catch (Exception ex) {
      if (ex instanceof VXMLBindingException) {
        throw (VXMLBindingException) ex;
      }
      throw new VXMLBindingException(ex);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException ex) {
          Logger.getLogger(VMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      if (objOut != null) {
        try {
          objOut.close();
        } catch (IOException ex) {
          Logger.getLogger(VMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  private <A extends Annotation> A getAnnotation(Annotation[] annots,
          Class<A> clazz) {
    if (annots == null) {
      return null;
    }
    for (Annotation a : annots) {
      if (clazz.isAssignableFrom(a.getClass())) {
        return clazz.cast(a);
      }
    }
    return null;
  }

  private T checkAlias(VElement elem, Class<?> clazz, Method m, T o) throws Exception {
    //check if we are aliased
    Alias alias = clazz != null ? clazz.getAnnotation(Alias.class) : m.getAnnotation(Alias.class);
    if (alias != null) {
      Class aliasClass = alias.alias();
      if (!VAlias.class.isAssignableFrom(aliasClass)) {
        throw new VXMLBindingException("Invalid Alias assignment on class: " + aliasClass);
      }
      VAlias<T, Object> valias = (VAlias<T, Object>) aliasClass.newInstance();
      o = (T) valias.getAlias(o);
      clazz = o.getClass();
      //add alias data
      elem.addAttribute(new VAttribute(VMarshallerConstants.ALIAS_ATTRIBUTE, clazz.getName()));
      elem.addAttribute(new VAttribute(VMarshallerConstants.ALIAS_CLASS_ATTRIBUTE, aliasClass.getName()));
    }

    return o;
  }

  /**
   * Returns true if this object has been successfully converted
   *
   * @param elem
   * @param clazz
   * @param o
   * @return
   * @throws Exception
   */
  private boolean checkConverter(VElement elem, Class<?> clazz, Method m, T o) throws Exception {
    //check if we have a converter
    Converter con = clazz != null ? clazz.getAnnotation(Converter.class) : m.getAnnotation(Converter.class);
    if (con != null) {
      Class conClass = con.converter();
      if (!VConverter.class.isAssignableFrom(conClass)) {
        throw new VXMLBindingException("Invalid Converter assignment on class: " + conClass);
      }
      VConverter vcon = (VConverter) conClass.newInstance();
      if (!vcon.isConvertCapable(o.getClass())) {
        throw new VXMLBindingException("Invalid Converter Specified for class type: " + o.getClass());
      }
      //add the converter
      addAttribute(elem, new VAttribute(VMarshallerConstants.CONVERTER_ATTRIBUTE, conClass.getName()));
      String value = vcon.convert(o);
      elem.addChild(new VContent(value));
      return true;
    }
    return false;
  }

  private VMarkupGenerator<T> checkDynamicMarkup(VElement elem, Class<?> clazz, T o) throws Exception {
    //find the markup generator if any
    DynamicMarkup markup = clazz.getAnnotation(DynamicMarkup.class);
    VMarkupGenerator<T> generator = null;
    if (markup != null) {
      Class gClass = markup.markupGenerator();
      generator = (VMarkupGenerator) gClass.newInstance();
      String dMarkup = generator.markup(o);
      elem.setMarkup(dMarkup);
    } else {
      //check if markup is available
      Markup mk = clazz.getAnnotation(Markup.class);
      if (mk != null) {
        //for a class, we ignore the property thing
        elem.setMarkup(mk.name());
      }
    }
    return generator;
  }

  private boolean checkSerializable(VElement elem, Class<?> clazz, Method m, T o) throws Exception {
    //at this point, we need to check if we are working with binary data
    Serialized bin = (Serialized) (clazz != null ? clazz.getAnnotation(Serialized.class) : m.getAnnotation(Serialized.class));
    if (bin != null) {
      addAttribute(elem, new VAttribute(VMarshallerConstants.SERIALIZED_ATTRIBUTE, "true"));
      //marshall as binary and  then return
      doMarshallSerializable(elem, o);
      return true; //we have finished with the marshalling at this point
    }
    return false;
  }

  private boolean checkCircularReference(VElement elem, Class<?> clazz, T o) throws Exception {
    boolean isInner = isInner(o.getClass());
    int refid = System.identityHashCode(o);
    //we cannot create a reference to primitive types, String and Enums. It does not make sense
    if (idMapping.containsKey(refid) && !isInner && !(o instanceof String) && !o.getClass().isEnum()) {
      addAttribute(elem, new VAttribute(VMarshallerConstants.OBJECT_CIRCULAR_REFERENCE_ATTRIBUTE, refid + ""));
      return true;
    } else {
      //add the id
      addAttribute(elem, new VAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE, "" + System.identityHashCode(o)));
      idMapping.put(refid, o);
    }
    return false;
  }

  private boolean checkInner(VElement elem, Class<?> clazz, T o, VNamespace[] namespace, String elementParam1) throws Exception {
    //is this an inner class
    boolean isInner = isInner(o.getClass());
    if (isInner) {
      //since this might
      try {
        Field ff = o.getClass().getDeclaredField("this$0");
        ff.setAccessible(true);
        Object ref = ff.get(o);
        int id = System.identityHashCode(ref);
        //find the object
        Object refObj = idMapping.get(id);
        if (refObj == null) {
          //decode it
          VElement _this = new VElement(VMarshallerConstants.OUTER_CLASS_REFERENCE_ELEMENT_MARKUP, elem);
          addAttribute(_this, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, ref.getClass().getName()));
          doMarshall(_this, (T) ref, namespace, elementParam1, null, null, null, false, false, null);
        }
        addAttribute(elem, new VAttribute(VMarshallerConstants.OUTER_CLASS_REFERENCE_ATTRIBUTE, id + ""));
        addAttribute(elem, new VAttribute(VMarshallerConstants.INNER_CLASS_ATTRIBUTE, "true"));
      } catch (Exception ex) {
        throw new VXMLBindingException(ex);
      }
    }
    return true;
  }

  private boolean checkEnum(VElement elem, Class<?> clazz, T o, VNamespace[] namespace, VMarkupGenerator<T> generator) throws Exception {
    addAttribute(elem, new VAttribute(VMarshallerConstants.ENUM_ATTRIBUTE, clazz.isEnum() + ""));
    Method[] mts = clazz.getDeclaredMethods();
    List<Method> mms = new ArrayList<Method>(Arrays.asList(mts));
    normalizeMethods(mms, false, clazz);
    EnumMarkup em = clazz.getAnnotation(EnumMarkup.class);
    String enumMarkup = (em != null) ? em.value() : VMarshallerConstants.ENUM_VALUE_ELEMENT;
    VElement en = new VElement(enumMarkup, elem);
    // Enum values are constants by default
    addAttribute(en, new VAttribute(VMarshallerConstants.CONSTANT_ATTRIBUTE, "true"));
    en.addChild(new VContent(((Enum) o).name()));
    if (!mms.isEmpty()) {
      this.marshallProperties(elem, o, namespace, generator);
    }
    return true;
  }

  private boolean checkCollection(VElement elem, T o, VNamespace[] namespace,
          String elementParam1, String elementParam2, Class<?> elementClass1,
          boolean onCollectionOrMapEmpty, boolean wrapCollectionElements, Annotation[] annotations) throws Exception {
    Collection c = (Collection) o;
    addAttribute(elem, new VAttribute(VMarshallerConstants.COLLECTION_ATTRIBUTE, "true"));
    VElement _elem_;
    //reassign the element here if we are not to wrap elements in the COLLECTION_ELEMENT_MARKUP
    if (!wrapCollectionElements) {
      _elem_ = elem;
    } else {
      _elem_ = new VElement(VMarshallerConstants.COLLECTION_ELEMENT_MARKUP, elem);
    }
    if (c.isEmpty()) {
      if (onCollectionOrMapEmpty) {
        //elemparam1 name of element
        String elemName = VMarshallerConstants.ELEMENT_ELEMENT_MARKUP;
        if (elementParam1 != null) {
          elemName = elementParam1;
        }
        VElement fe = new VElement(elemName);
        //elementClass1 for collection generic class
        addAttribute(fe, new VAttribute(VMarshallerConstants.COLLECTION_EMPTY_MARKUP, elementParam2));
        _elem_.addChild(fe);
        marshallNullProperties(fe, namespace, elementClass1, null);
        addAttribute(fe, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, elementParam2));
      }
    } else {
      //check if we are supposed to ignore collections here, and add the children to the parent
      AsCollection ac = getAnnotation(annotations, AsCollection.class);
      if (ac != null && !ac.value()) {
        //then we are not supposed to add the collection element.
        //At this point, the collection element had been added to the parent.
        //Remove it
        VElement parent = _elem_.getParent();
        parent.removeChild(_elem_);
        _elem_ = parent;
      }
      for (Object o1 : c) {
        String elemName;
        if (elementParam1 != null && !elementParam1.isEmpty()) {
          elemName = elementParam1;
        } else {
          //use the o1 class name
          String name = o1.getClass().getSimpleName();
          elemName = name.toLowerCase().charAt(0) + name.substring(1);
        }
        VElement fe = new VElement(elemName);
        _elem_.addChild(fe);
        addAttribute(fe, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, o1.getClass().getName()));
        doMarshall(fe, (T) o1, namespace, null, null, null, null, false, false, null);
      }
    }
    return true;
  }

  private boolean checkMap(VElement elem, T o, VNamespace namespace[], String elementParam1,
          String elementParam2, Class<?> elementClass1, Class<?> elementClass2,
          boolean onCollectionOrMapEmpty, boolean wrapCollectionElements, Annotation[] annotations) throws Exception {
    //is it a map
    Map m = (Map) o;
    //then we need to save this as map-entry pairs
    addAttribute(elem, new VAttribute(VMarshallerConstants.MAP_ATTRIBUTE, "true"));
    if (m.isEmpty()) {
      if (onCollectionOrMapEmpty) {
        VElement ee = new VElement(VMarshallerConstants.MAP_ENTRY_ELEMENT_MARKUP, elem);
        VElement kElem = new VElement((elementParam1 != null && elementParam2 != null) ? elementParam1 : VMarshallerConstants.KEY_ATTRIBUTE);
        VElement vElem = new VElement((elementParam1 != null && elementParam2 != null) ? elementParam2 : VMarshallerConstants.VALUE_ATTRIBUTE);
        //elementClass1 for key class
        addAttribute(ee, new VAttribute(VMarshallerConstants.MAP_EMPTY_MARKUP, elementParam2));
        marshallNullProperties(kElem, namespace, elementClass1, null);
        marshallNullProperties(vElem, namespace, elementClass2, null);
        ee.addChild(kElem);
        ee.addChild(vElem);
      }
    } else {
      //has the map been specified as attributes
      AsAttribute attrs = getAnnotation(annotations, AsAttribute.class);
      if (attrs != null) {
        //remove this from parent
        VElement parent = elem.getParent();
        parent.removeChild(elem);
        elem = parent;
        VConverter keyConverter = (VConverter) (attrs.keyConverter() != VConverter.class ? attrs.keyConverter().newInstance() : null);
        VConverter valueConverter = (VConverter) (attrs.valueConverter() != VConverter.class ? attrs.valueConverter().newInstance() : null);
        for (Object k : m.keySet()) {
          Object v = m.get(k);
          if (v != null) {
            String atName = keyConverter != null ? keyConverter.convert(k) : k.toString();
            String atValue = valueConverter != null ? valueConverter.convert(v) : v.toString();
            elem.addAttribute(new VAttribute(atName, atValue));
          }
        }
        /**
         * We return at this point because we have done everything that needs to be done.
         */
        return true;
      } else {
        for (Object k : m.keySet()) {
          Object v = m.get(k);
          VElement ee = new VElement(VMarshallerConstants.MAP_ENTRY_ELEMENT_MARKUP);
          VElement kElem = new VElement((elementParam1 != null && elementParam2 != null) ? elementParam1 : VMarshallerConstants.KEY_ATTRIBUTE);
          addAttribute(kElem, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, k.getClass().getName()));
          addAttribute(kElem, new VAttribute(VMarshallerConstants.KEY_ATTRIBUTE, "true"));
          VElement vElem = new VElement((elementParam1 != null && elementParam2 != null) ? elementParam2 : VMarshallerConstants.VALUE_ATTRIBUTE);
          addAttribute(vElem, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, v.getClass().getName()));
          addAttribute(vElem, new VAttribute(VMarshallerConstants.VALUE_ATTRIBUTE, "true"));
          ee.addChild(kElem);
          ee.addChild(vElem);
          elem.addChild(ee);
          doMarshall(kElem, (T) k, namespace, null, null, null, null, false, false, null);
          doMarshall(vElem, (T) v, namespace, null, null, null, null, false, false, null);
        }
      }
    }
    return false;
  }

  private void doMarshall(VElement elem, T o, VNamespace namespace[], String elementParam1,
          String elementParam2, Class<?> elementClass1, Class<?> elementClass2,
          boolean onCollectionOrMapEmpty, boolean wrapCollectionElements, Annotation[] annotations) throws VXMLBindingException {
    //check if the element has a namespace attributes
    Namespace _ns = getAnnotation(o.getClass(), Namespace.class);
    if (_ns != null) {
      //then we override the current namespace
      namespace = new VNamespace[]{new VNamespace(_ns.prefix(), _ns.uri())};
    }
    //set the element associated values before marshalling the children
    if (namespace != null) {
      for (VNamespace namespace1 : namespace) {
        elem.setAssociatedNamespace(namespace1);
      }
    }
    SecurityManager man = System.getSecurityManager();
    if (man != null) {
      System.setSecurityManager(new SecurityManager() {
        @Override
        public void checkPermission(Permission perm) {
          if (perm instanceof ReflectPermission) {
            return;
          }
          super.checkPermission(perm);
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
          if (perm instanceof ReflectPermission) {
            return;
          }
          super.checkPermission(perm, context);
        }
      });
    }
    try {
      Class<?> clazz = o.getClass();
      //is it displayable
      Displayable d = clazz.getAnnotation(Displayable.class);
      if (d != null) {
        addAttribute(elem, new VAttribute(VMarshallerConstants.DISPLAYABLE_ATTRIBUTE, "" + d.value()));
      }
      VMarkupGenerator<T> generator = checkDynamicMarkup(elem, clazz, o);
      //does this class have comments
      Comment cm = (Comment) clazz.getAnnotation(Comment.class);
      if (cm != null) {
        //get the comment
        elem.setComment(cm.value());
      }
      o = checkAlias(elem, clazz, null, o);
      if (checkConverter(elem, clazz, null, o)) {
        return;
      }
      if (checkSerializable(elem, clazz, null, o)) {
        return;
      }
      if (checkCircularReference(elem, clazz, o)) {
        return;
      }
      checkInner(elem, clazz, o, namespace, elementParam1);
      if (clazz.isPrimitive()) {
        addAttribute(elem, new VAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE, clazz.isPrimitive() + ""));
        elem.addChild(new VContent(o.toString()));
      } else if (clazz.isEnum()) {
        checkEnum(elem, clazz, o, namespace, generator);
      } else if (o instanceof Number) {
        elem.addChild(new VContent(o.toString()));
      } else if (o instanceof Character) {
        elem.addChild(new VContent(o.toString()));
      } else if (o instanceof Boolean) {
        elem.addChild(new VContent(o.toString()));
      } else if (o instanceof String) {
        String ss = (String) o;
        if (!ss.isEmpty()) {
          elem.addChild(new VContent(o.toString()));
        }
      } else if (o instanceof Calendar) {
        String iso = VDateConverter.ISOString((Calendar) o);
        elem.addChild(new VContent(iso));
        elem.addAttribute(new VAttribute(VMarshallerConstants.CONVERTER_ATTRIBUTE, VCalendarConverter.class.getName()));
      } else if (o instanceof Date) {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) o);
        String iso = VDateConverter.ISOString(c);
        elem.addChild(new VContent(iso));
        elem.addAttribute(new VAttribute(VMarshallerConstants.CONVERTER_ATTRIBUTE, VDateConverter.class.getName()));
      } else if (clazz.isArray()) {
        doMarshallArray(elem, o, clazz, namespace, elementParam1);
      } else {
        if (Collection.class.isAssignableFrom(o.getClass())) {
          //determine if we are working with generic collections
          checkCollection(elem, o, namespace, elementParam1, elementParam2, elementClass1, onCollectionOrMapEmpty, wrapCollectionElements, annotations);
        } else if (Map.class.isAssignableFrom(o.getClass())) {
          if (checkMap(elem, o, namespace, elementParam1, elementParam2, elementClass1, elementClass2,
                  onCollectionOrMapEmpty, wrapCollectionElements, annotations)) {
            return;
          }
        }
        //we may have collections with properties
        //we have reached here, we marchall the properties
        this.marshallProperties(elem, o, namespace, generator);
      }
    } catch (Exception e) {
      if (e instanceof VXMLBindingException) {
        throw (VXMLBindingException) e;
      }
      throw new VXMLBindingException(e);
    } finally {
      System.setSecurityManager(man);
    }
  }

  private boolean isInner(Class clazz) {
    boolean isMember = clazz.isMemberClass();
    boolean isInner = isMember && !Modifier.isStatic(clazz.getModifiers());
    if (!isInner) {
      isInner = clazz.isAnonymousClass();
    }
    return isInner;
  }

  /**
   * Returns the method declared in an interface or superclass. The returned method is the first to
   * be declared or defined. Method declaration in an interface takes precedence over method
   * definition in a superclass.
   *
   * @param m
   * @param clazz
   * @return
   */
  private static Method getOverriddenOrImplementedMethod(Method m, Class clazz) {
    Method overridden = getOverriddenMethod(m, clazz);
    Method implemented = getImplementedMethod(m, clazz);
    return implemented == null ? overridden : implemented;
  }

  private static Method getOverriddenMethod(Method m, Class clazz) {
    Class clz = m.getDeclaringClass();
    if (clz.equals(clazz)) {
      //then get the superclass and determine if it declares the method
      Class sClazz = clz.getSuperclass();
      try {
        m = sClazz.getMethod(m.getName(), m.getParameterTypes());
        return getOverriddenOrImplementedMethod(m, sClazz);
      } catch (Exception e) {
        //no method found, try interfaces
        return m;
      }
    } else {
      return getOverriddenOrImplementedMethod(m, clz);
    }
  }

  private static Method getImplementedMethod(Method m, Class clazz) {
    Class[] interfs = clazz.getInterfaces();
    Method decM = null;
    for (Class ifs : interfs) {
      try {
        decM = ifs.getMethod(m.getName(), m.getParameterTypes());
        Method mk = getImplementedMethod(decM, decM.getDeclaringClass());
        decM = mk != null ? mk : decM;
      } catch (Exception e) {
        //no method found, try superinterfaces
        decM = getImplementedMethod(m, ifs);
      }
    }
    return decM;
  }

  /**
   * Returns the annotation as follows: If this class declares the annotation, it is returned,
   * otherwise superclasses of this class is checked as specified by the {@link Inherited}
   * annotation. The interfaces of this class and superinterfaces, are also checked for this
   * annotation. The interface annotations are only considered if they are inherited and the default
   * check for annotation returns null.
   *
   *
   * @param <T>
   * @param clazz
   * @param annotationClass
   * @return
   */
  private <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
    T t = clazz.getAnnotation(annotationClass);
    if (t == null) {
      while (clazz != Object.class) {
        t = getInterfaceAnnotation(clazz, annotationClass);
        if (t != null) {
          break;
        }
        clazz = clazz.getSuperclass();
      }
    }
    return t;
  }

  private <T extends Annotation> T getInterfaceAnnotation(Class<?> clazz, Class<T> annotationClass) {
    T t = null;
    Class<?>[] cls = clazz.getInterfaces();
    for (Class<?> cl : cls) {
      t = cl.getAnnotation(annotationClass);
      if (t != null) {
        //check if it is inherited
        Inherited i = t.annotationType().getAnnotation(Inherited.class);
        return i != null ? t : null;
      } else {
        t = getInterfaceAnnotation(cl, annotationClass);
        if (t != null) {
          Inherited i = t.annotationType().getAnnotation(Inherited.class);
          return i != null ? t : null;
        }
      }
    }
    return t;
  }

  private <T extends Annotation> T getAnnotation(Method m, Class<T> annotationClass) {
    T t = m.getAnnotation(annotationClass);
    if (t == null) {
      m = getOverriddenOrImplementedMethod(m, m.getDeclaringClass());
      t = m.getAnnotation(annotationClass);
      //make sure that the annotation is inherited, otherwise forget about it.
      if (t != null) {
        Inherited i = t.annotationType().getAnnotation(Inherited.class);
        if (i == null) {
          return null;
        }
      }
    }
    return t;
  }

  /**
   * Returns true if the method has been removed
   *
   * @param m
   * @param it
   * @param clazz
   * @return
   */
  private static boolean normalizeMethod(Method m, ListIterator<Method> it, Class clazz) {
    //if the method is deprecated, remove it
    Deprecated dd = m.getAnnotation(Deprecated.class);
    if (dd != null) {
      it.remove();
      return true;
    }
    //it must be a getter method, otherwise ignore it
    String mName = m.getName();
    if (((!mName.startsWith("is") || mName.length() < 3) && (!mName.startsWith("get") || mName.length() < 4)) || mName.equals("getClass")) {
      it.remove();
      return true;
    } else {
      //we only need public methods
      int mod = m.getModifiers();
      if (!Modifier.isPublic(mod) || Modifier.isStatic(mod)) {
        it.remove();
        return true;
      }
      //this methods must not have parameters
      if (m.getParameterTypes().length > 0) {
        it.remove();
        return true;
      }
    }
    Marshallable ms = m.getAnnotation(Marshallable.class);
    if (ms != null && !ms.marshal()) {
      it.remove();
      return true;
    }
    if (m.isSynthetic()) {
      it.remove();
      return true;
    }
    //we only need java bean properties
    //the properties must have a setter and getter methods, unless it is marked as marshallable
    if (ms == null) {
      //it must be marked as marshallable if it does not have corresponding setter methods
      //check if it is informational
      Informational info = m.getAnnotation(Informational.class);
      if (info != null) {
        return true;
      }
      if (mName.startsWith("get")) {
        mName = mName.substring(3);
      } else if (mName.startsWith("is")) {
        mName = mName.substring(2);
      } else {
        it.remove();
        return true;
      }
      //lets handle MathContext
      if (clazz == MathContext.class) {
        return true;
      }
      String setName = "set" + mName;
      //get return type class
      Class retType = m.getReturnType();
      //since we may have synthetic methods we may be carefull not to remove
      try {
        Method sM = clazz.getMethod(setName, new Class[]{retType});
        //no exception, just return
      } catch (Exception e) {
        try {
          //lets see if there is synthetic or overriden generic type
          Method sM = clazz.getMethod(setName, new Class[]{Object.class});
        } catch (Exception ex) {
          //then we do not have any setter equivalent, remove it
          it.remove();
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Normalizes method for marshalling and unmarshalling purposes when method property has been set
   * as the default property
   *
   * @param mms
   * @param setter
   * @param clazz
   */
  private static void normalizeMethods(List<Method> mms, boolean setter, Class clazz) {
    if (!setter) {
      for (ListIterator<Method> it = mms.listIterator(); it.hasNext();) {
        Method m = it.next();
        if (!normalizeMethod(m, it, clazz)) {
          Method om = getOverriddenOrImplementedMethod(m, clazz);
          if (om != null) {
            normalizeMethod(om, it, clazz);
          }
        }
      }
    } else {
      for (ListIterator<Method> it = mms.listIterator(); it.hasNext();) {
        Method m = it.next();
        //if the method is deprecated, remove it
        Deprecated dd = m.getAnnotation(Deprecated.class);
        if (dd != null) {
          it.remove();
          continue;
        }
        //it must be a getter method, otherwise ignore it
        String mName = m.getName();
        if (!mName.startsWith(
                "set") || mName.length() < 4) {
          it.remove();
          continue;
        } else {
          //we only need public methods
          int mod = m.getModifiers();
          if (!Modifier.isPublic(mod) || Modifier.isStatic(mod)) {
            it.remove();
            continue;
          }
        }
        if (m.isSynthetic()) {
          it.remove();
        }
      }
    }
  }

  /**
   * Returns null if not valid.
   *
   * @param m
   * @return
   */
  private String getName(Method f) {
    String name = f.getName();
    int i1 = -1, i2 = -1;
    if (name != null && ((i1 = name.indexOf("get")) == 0 || (i2 = name.indexOf("is"))
            == 0) && name.length() > 3 && f.getParameterTypes().length == 0) {
      String mName;
      if (i1 == 0) {
        mName = name.substring("get".length());
      } else if (i2 == 0) {
        mName = name.substring("is".length());
      } else {
        return null;
      }
      return (mName.charAt(0) + "").toLowerCase() + mName.substring(1);
    }
    return null;
  }

  private boolean checkXmlMarkup(Method m, VElement elem, T o) throws Exception {
    //could the value of this property be specified as the markup of the parent
    XmlMarkup markup = m.getAnnotation(XmlMarkup.class);
    if (markup != null) {
      Class cls = m.getReturnType();
      String value;
      if (!cls.isPrimitive()
              && !isWrapperType(cls)
              && cls != String.class) {
        //otherwise check if it defines a converter
        Converter con = m.getAnnotation(Converter.class);
        if (con == null) {
          throw new VXMLBindingException("XmlMarkup Specification is invalid. The following XmlMarkup class is not accepted:" + cls.getName());
        }
        VConverter vcon = (VConverter) con.converter().newInstance();
        if (!vcon.isConvertCapable(cls)) {
          throw new VXMLBindingException("XmlMarkup Specification is invalid. The following XmlMarkup class: "
                  + "" + cls.getName() + " could not be converted with the specified converter: " + vcon.getClass());
        }
        value = vcon.convert(m.invoke(o, new Object[]{}));
      } else {
        Object resValue = m.invoke(o, new Object[]{});
        if (resValue == null) {
          return true;
        }
        value = resValue.toString();
      }
      if (value == null) {
        throw new VXMLBindingException("Error on XmlMarkup Binding: " + cls.getName());
      }
      elem.setMarkup(value);
      return true;
    }
    return false;
  }

  /**
   * Marshalls the property returned by the method of the specified object as attribute.
   *
   * @param elem the element for this attribute
   * @param o the object marshalled to the specified element
   * @param m the method to return the objects attribute
   * @param attr not null if the method was annotated as an attribute.
   * @return
   * @throws Exception
   */
  private boolean marshallAttribute(VElement elem, T o, Method m) throws Exception {
    //add it as attribute
    //this can be an Id or an attribute element without being annotated as Attribute
    Class cls = m.getReturnType();
    String value = null;
    String mName = getName(m);
    Attribute attr = getAnnotation(m, Attribute.class);
    if (attr != null) {
      //does it have a markup
      String attrName = attr.name();
      if (!attrName.isEmpty()) {
        mName = attrName;
      }
      //does it have an attribute normalizer?
      Class<? extends VAttributeKeyNormalizer> vaknClass = attr.attributeKeyNormalizer();
      try {
        VAttributeKeyNormalizer vakn = vaknClass.newInstance();
        mName = vakn.normalizeKey(mName);
      } catch (Exception e) {
      }
      value = attr.value();
    }
    if (!cls.isPrimitive()
            && !isWrapperType(cls)
            && cls != String.class) {
      //otherwise check if it defines a converter
      Converter con = getAnnotation(m, Converter.class);
      if (con == null) {
        throw new VXMLBindingException("Attribute Specification is invalid. The following Attribute class is not accepted:" + cls.getName());
      }
      VConverter vcon = (VConverter) con.converter().newInstance();
      if (!vcon.isConvertCapable(cls)) {
        throw new VXMLBindingException("Attribute Specification is invalid. The following Attribute class: "
                + "" + cls.getName() + " could not be converted with the specified converter: " + vcon.getClass());
      }
      value = vcon.convert(m.invoke(o, new Object[]{}));
    } else {
      Object resValue = m.invoke(o, new Object[]{});
      if (resValue == null && (value == null || value.trim().isEmpty())) {
        return true;
      }
      if (resValue != null) {
        value = resValue.toString();
      }
    }
    if (value == null) {
      throw new VXMLBindingException("Error on Attribute Binding: " + cls.getName());
    }
    //check if this attribute has been positioned.
    Position pos = getAnnotation(m, Position.class);
    if (pos != null) {
      elem.addAttribute(new VAttribute(mName, value), pos.index());
    } else {
      elem.addAttribute(new VAttribute(mName, value));
    }
    return true;
  }

  private boolean checkAttribute(VElement elem, T o, Method m) throws Exception {
    Attribute attr = getAnnotation(m, Attribute.class);
    if (attr != null) {
      return marshallAttribute(elem, o, m);
    }
    return false;
  }

  private boolean checkContent(VElement elem, T o, Method m) throws Exception {
    if (getAnnotation(m, Content.class) == null) {
      return false;
    }
    Class cls = m.getReturnType();
    String value = null;
    if (!cls.isPrimitive()
            && !isWrapperType(cls)
            && cls != String.class) {
      //otherwise check if it defines a converter
      Converter con = getAnnotation(m, Converter.class);
      if (con == null) {
        throw new VXMLBindingException("Content Specification is invalid. The following Content class is not accepted:" + cls.getName());
      }
      VConverter vcon = (VConverter) con.converter().newInstance();
      if (!vcon.isConvertCapable(cls)) {
        throw new VXMLBindingException("Content Specification is invalid. The following Content class: "
                + "" + cls.getName() + " could not be converted with the specified converter: " + vcon.getClass());
      }
      value = vcon.convert(m.invoke(o, new Object[]{}));
    } else {
      Object resValue = m.invoke(o, new Object[]{});
      if (resValue != null) {
        value = resValue.toString();
      }
    }
    if (value != null) {
      //is the value content supposed to be CDATA
      CData cData = getAnnotation(m, CData.class);
      if (cData != null) {
        value = "<![CDATA[" + value + "]]>";
      }
      elem.addChild(new VContent(value));
    }
    return true;
  }

  private boolean checkId(VElement elem, T o, Method m) throws Exception {
    Id id = getAnnotation(m, Id.class);
    if (id != null) {
      //check if we are to generate an id
      Generated g = getAnnotation(m, Generated.class);
      if (g != null) {
        Object genId = new IdGenerator().generateId(m.getReturnType());
        if (g.provider() != VGenerator.class) {
          VGenerator vg = g.provider().newInstance();
          genId = vg.generate();
        }
        //we need to get the setter
        String property = getName(m);
        String sName = "set" + property.toUpperCase().charAt(0) + property.substring(1);
        Method setter = o.getClass().getMethod(sName, new Class[]{m.getReturnType()});
        setter.invoke(o, new Object[]{genId});
      }
      marshallAttribute(elem, o, m);
      return true;
    }
    return false;
  }

  private void marshallProperties(VElement elem, T o, VNamespace namespace[], VMarkupGenerator<T> generator) throws VXMLBindingException {
    //add elements by getting its property methods
    try {
      Class<?> clazz = o.getClass();
      Method[] methods = o.getClass().getMethods();
      List<Method> getterMethods = new ArrayList<Method>(Arrays.asList(methods));
      normalizeMethods(getterMethods, false, clazz);
      Condition classCondition = (Condition) clazz.getAnnotation(Condition.class);
      //if this element has no properties, set its content with the toString
      if (getterMethods.isEmpty()) {
        //be sure it is not a collection or map
        if (!(o instanceof Collection) && !(o instanceof Map)) {
          elem.addChild(new VContent(o.toString()));
          return;
        }
      }
      //check to see if these properties have been specified as attributes
      AsAttributes aa = clazz.getAnnotation(AsAttributes.class);
      List<Class<?>> ignored = new ArrayList<Class<?>>();
      if (aa != null) {
        ignored.addAll(Arrays.asList(aa.ignore()));
      }
      for (Method f : getterMethods) {
        Class<?> returnType = f.getReturnType();
        String mName = getName(f);
        if (mName == null) {
          continue;
        }
        /**
         * Marshall this property if its a valid property, it is not an xml markup for the current
         * element, its not an attribute if the current element, and it is not specified as a
         * content for the current element.
         *
         * If the class specifies that all its fields are attributes,
         */
        /**
         * Is this instance an id?
         */
        if (!checkId(elem, o, f)) {
          if (aa != null
                  && !ignored.contains(returnType)
                  && getAnnotation(f, Element.class) == null
                  && returnType.getAnnotation(Element.class) == null) {
            marshallAttribute(elem, o, f);
          } else if (!checkXmlMarkup(f, elem, o)
                  && !checkAttribute(elem, o, f)
                  && !checkContent(elem, o, f)) {
            marshallProperty(elem, o, namespace, generator, mName, f, classCondition);
          }
        }
      }

    } catch (Exception ex) {
      if (ex instanceof VXMLBindingException) {
        throw (VXMLBindingException) ex;
      }
      throw new VXMLBindingException(ex);
    }
  }

  private boolean checkCondition(Method m, Condition classCondition, T o1) throws Exception {
    if (classCondition != null) {
      Class condClass = classCondition.condition();
      VConditional<T> vCond = (VConditional<T>) condClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
      if (!vCond.accept((T) o1)) {
        return true;
      }
    } else {
      //check if it is to be marshalled
      //does it have a conditional annotation?
      Condition cond = m.getAnnotation(Condition.class);
      if (cond != null) {
        Class condClass = cond.condition();
        VConditional<T> vCond = (VConditional<T>) condClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
        if (!vCond.accept((T) o1)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean checkInitializer(VElement elem, Method m, T o1) {
    //is it an inializer element
    Initializer init = m.getAnnotation(Initializer.class);
    if (init != null) {
      //get the attribute for initialization
      VAttribute initAttr = elem.getAttribute(VMarshallerConstants.INITIALIZERS_ATTRIBUTE);
      if (initAttr == null) {
        initAttr = new VAttribute(VMarshallerConstants.INITIALIZERS_ATTRIBUTE, "[" + System.identityHashCode(o1) + "]");
        addAttribute(elem, initAttr);
      } else {
        String value = initAttr.getValue();
        value = value.substring(1, value.length() - 1);
        int index = init.index();
        String inits[] = value.split(",");
        if (index >= inits.length) {
          inits = Arrays.copyOf(inits, inits.length + 1);
          inits[inits.length - 1] = System.identityHashCode(o1) + "";
        } else {
          //we find the right index
          String[] lower = Arrays.copyOfRange(inits, 0, index);
          String[] upper = Arrays.copyOfRange(inits, index, inits.length);
          if (lower.length == 0) {
            lower = new String[]{System.identityHashCode(o1) + ""};
            index++;
          }
          //merge them together
          inits = new String[lower.length + upper.length];
          System.arraycopy(lower, 0, inits, 0, lower.length);
          System.arraycopy(upper, 0, inits, index, upper.length);
        }
        value = Arrays.toString(inits);
        initAttr.setValue(value);
      }
    }
    return true;
  }

  private void marshallProperty(VElement elem, T o, VNamespace namespace[],
          VMarkupGenerator<T> generator, String mName, Method m, Condition classCondition) throws VXMLBindingException {
    try {
      m.setAccessible(true);
      //is this method iterable
      com.anosym.vjax.annotations.Iterable it = m.getAnnotation(com.anosym.vjax.annotations.Iterable.class);
      T o1 = null;
      do {
        try {
          o1 = (T) m.invoke(o, new Object[]{});
        } catch (Exception ee) {
          if (it != null && it.throwsException()) {
            break;
          } else {
            throw ee;
          }
        }
        if (o1 != null) {
          if (checkCondition(m, classCondition, o)) {
            continue;
          }
          VElement fe = new VElement(mName);
          //do we have specific attributes to be added to this element
          DefinedAttribute da = m.getAnnotation(DefinedAttribute.class);
          if (da != null) {
            //we do not use the VMarshaller.addAttribute since this is not a generated attribute by the system.
            fe.addAttribute(new VAttribute(da.name(), da.value()));
          }
          Displayable d = m.getAnnotation(Displayable.class);
          if (d != null) {
            addAttribute(fe, new VAttribute(VMarshallerConstants.DISPLAYABLE_ATTRIBUTE, "" + d.value()));
          }
          Constant ct = m.getAnnotation(Constant.class);
          if (ct != null) {
            addAttribute(fe, new VAttribute(VMarshallerConstants.CONSTANT_ATTRIBUTE, "true"));
          }
          //add the class attribute
          addAttribute(fe, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, o1.getClass().getName()));
          //if the method has been marked with constructible, then we generate a document include
          //for the class schema
          Constructible _const_ = m.getAnnotation(Constructible.class);
          if (_const_ != null) {
            VDocument doc = new VClassMarshaller().marshallClass(m.getReturnType());
            includes.add(doc);
          }
          //if a markup generator is defined ensure that it has not been rescinded in any of the
          //method properties
          Rescind rescind = m.getAnnotation(Rescind.class);
          if (rescind == null) {
            //ok check if the method property defines its on generator
            //this generator does extend to the definition of the object method properties
            DynamicMarkup dynMarkp = m.getAnnotation(DynamicMarkup.class);
            if (dynMarkp != null) {
              Class genClass = dynMarkp.markupGenerator();
              //is it ann inner class
              if (isInner(genClass)) {
                generator = (VMarkupGenerator) newInstance(null, genClass, o);
              } else {
                generator = (VMarkupGenerator) newInstance(null, genClass);
              }
              if (generator != null) {
                String markup = generator.markup((T) o1);
                if (markup != null) {
                  fe.setMarkup(markup);
                  addAttribute(fe, new VAttribute(VMarshallerConstants.OBJECT_PROPERTY_ATTRIBUTE, mName));
                }
              }
            } else if (generator != null) {
              String markup = generator.generateMarkup(o1);
              if (markup != null) {
                fe.setMarkup(markup);
                addAttribute(fe, new VAttribute(VMarshallerConstants.OBJECT_PROPERTY_ATTRIBUTE, mName));
              }
            }
          }
          //does it have informational attribute, then we set it as informational before the first parent
          Informational info = m.getAnnotation(Informational.class);
          if (info != null) {
            addAttribute(elem, new VAttribute(VMarshallerConstants.INFORMATIONAL_ATTRIBUTE, "true"));
            //be sure that the current element is the top level element
            //otherwise throw an exception
            if (elem.getParent() != null) {
              throw new VXMLBindingException("Invalid Information field attribute. Information Field must be in the main instance.");
            }
            elem.addChildAsFirst(fe);
          } else {
            Position pp = m.getAnnotation(Position.class);
            if (pp != null) {
              elem.addChild(fe, pp.index());
            } else {
              elem.addChild(fe);
            }
          }
          //does it have a comment annotation
          Comment comment = m.getAnnotation(Comment.class);
          if (comment != null) {
            fe.setComment(comment.value());
          }
          if (generator == null) {
            //check on static markup only if the generator is null
            //does it have a markup annotation
            Markup markup = m.getAnnotation(Markup.class);
            if (markup != null) {
              fe.setMarkup(markup.name());
              String prop = markup.property();
              if (prop.isEmpty()) {
                prop = mName;
              }
              addAttribute(fe, new VAttribute(VMarshallerConstants.OBJECT_PROPERTY_ATTRIBUTE, prop));
            }
          }
          //do we have a namespace declaration on methods
          Namespace _ns = m.getAnnotation(Namespace.class);
          if (_ns != null) {
            //we will override the class declaration
            namespace = new VNamespace[]{new VNamespace(_ns.prefix(), _ns.uri())};
            for (VNamespace namespace1 : namespace) {
              fe.addNamespace(namespace1);
            }
          }
          //check if we are aliased
          o1 = checkAlias(elem, null, m, o1);
          if (checkConverter(elem, null, m, o1)) {
            continue;
          }
          if (checkSerializable(elem, null, m, o1)) {
            continue;
          }
          checkInitializer(elem, m, o1);
          //supply the collectionelement value if it exist
          /**
           * TODO(marembo) We need to refactor this code and stop supplying the CollectionElement in
           * parameter method, rather we need to supply the annotation in the anootations array.
           */
          CollectionElement colElement = m.getAnnotation(CollectionElement.class);
          String elementParam1 = null;
          String elementParam2 = null;
          boolean wrapCollectionElements = true;
          if (colElement != null) {
            elementParam1 = colElement.value();
            wrapCollectionElements = colElement.wrapElements();
          } else {
            MapElement mapElem = m.getAnnotation(MapElement.class);
            if (mapElem != null) {
              elementParam1 = mapElem.key();
              elementParam2 = mapElem.value();
            }
          }
          //supply WhenEmpty properties
          WhenEmpty empty = m.getAnnotation(WhenEmpty.class);
          if (Collection.class.isAssignableFrom(o1.getClass())) {
            if (empty != null && ((Collection) o1).isEmpty()) {
              if (empty.marshall()) {
                if (empty.marshallType()) {
                  Class<?> cls[] = empty.genericClasses();
                  Class<?> c1 = cls.length > 0 ? cls[0] : null;
                  Class<?> c2 = cls.length > 1 ? cls[1] : null;
                  doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, c1, c2, true, wrapCollectionElements, m.getAnnotations());
                } else {
                  doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, null, null, false, false, m.getAnnotations());
                }
              } else if (elem != null) {
                //remove current element from parent
                elem.removeChild(fe);
              }
            } else {
              doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, null, null, false, false, m.getAnnotations());
            }
          } else if (Map.class.isAssignableFrom(o1.getClass())) {
            if (empty != null && ((Map) o1).isEmpty()) {
              if (empty.marshall()) {
                if (empty.marshallType()) {
                  Class<?> cls[] = empty.genericClasses();
                  Class<?> c1 = cls.length > 0 ? cls[0] : null;
                  Class<?> c2 = cls.length > 1 ? cls[1] : null;
                  doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, c1, c2, true, false, m.getAnnotations());
                } else {
                  doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, null, null, false, false, m.getAnnotations());
                }
              }
            } else {
              doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, null, null, false, false, m.getAnnotations());
            }
          } else {
            doMarshall(fe, (T) o1, namespace, elementParam1, elementParam2, null, null, false, false, m.getAnnotations());
          }
        } else {
          //confirm that it is required or not
          Required r = m.getAnnotation(Required.class);
          if (r != null && !r.relaxed()) {
            throw new VXMLBindingException("The following property(" + mName + ") is mandatory");
          }
          //get null handling procedure
          WhenNull whenNull = m.getAnnotation(WhenNull.class);
          if (whenNull == null || whenNull.mode() == WhenNull.NullMode.IGNORE) {
            return;
          }
          marshallNullProperty(elem, namespace, mName, m, whenNull.mode());
        }
      } while (it != null && o1 != null);
    } catch (Exception ee) {
      if (ee instanceof VXMLBindingException) {
        throw (VXMLBindingException) ee;
      }
      throw new VXMLBindingException(ee);
    }
  }

  private void marshallNullProperty(VElement elem, VNamespace namespace[],
          String mName, Method m, WhenNull.NullMode mode) throws VXMLBindingException {
    //otherwise lets marshall to empty elements
    Class<?> returnType = m.getReturnType();
    VElement fe = new VElement(mName);
    //add the class attribute
    addAttribute(fe, new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, returnType.getName()));
    //this is a null property, set the indicator for the unmarshalling procedure
    addAttribute(fe, new VAttribute(VMarshallerConstants.NULL_PROPERTY, "true"));
    //if the method has been marked with constructible, then we generate a document include
    //for the class schema
    Constructible _const_ = m.getAnnotation(Constructible.class);
    if (_const_ != null) {
      VDocument doc = new VClassMarshaller().marshallClass(m.getReturnType());
      includes.add(doc);
    }
    //since this value is null, we do not check on dynamic markup generators
    //since this is a null value, we expect it simply not to be a an informational property
    //we thus ignore informational property attribute if it exists
    Position pp = m.getAnnotation(Position.class);
    if (pp != null) {
      elem.addChild(fe, pp.index());
    } else {
      elem.addChild(fe);
    }
    //does it have a comment annotation
    Comment comment = m.getAnnotation(Comment.class);
    if (comment != null) {
      fe.setComment(comment.value());
    }
    //even if the generator is specified, we cannot use the dynamic generator because the
    //property is null
    //we therefore check by defualt, the Markup annotation
    Markup markup = m.getAnnotation(Markup.class);
    if (markup != null) {
      fe.setMarkup(markup.name());
      addAttribute(fe, new VAttribute(VMarshallerConstants.OBJECT_PROPERTY_ATTRIBUTE, markup.property()));
    }
    //do we have a namespace declaration on methods
    Namespace _ns = m.getAnnotation(Namespace.class);
    if (_ns != null) {
      //we will override the class declaration
      namespace = new VNamespace[]{new VNamespace(_ns.prefix(), _ns.uri())};
      fe.addNamespace(namespace[0]);
    }
    //lets get the property and marshall them
    //this depends on the whenNull property
    if (mode == WhenNull.NullMode.MARSHALL_NULL_PROPERTY) {
      marshallNullProperties(fe, namespace, returnType, mode);
    }
  }

  private void marshallNullProperties(VElement elem, VNamespace namespace[], Class<?> clazz, WhenNull.NullMode mode) throws VXMLBindingException {
    //do we suppress super class properties
    SuppressSuperClass sp = clazz.getAnnotation(SuppressSuperClass.class);
    Method[] methods = (sp == null) ? clazz.getMethods() : clazz.getDeclaredMethods();
    List<Method> getterMethods = new ArrayList<Method>(Arrays.asList(methods));
    normalizeMethods(getterMethods, false, clazz);
    if (getterMethods.isEmpty()) {
      //since we are marshalling a null object we simply return
      return;
    }
    for (Method f : getterMethods) {
      String name = f.getName();
      int i1 = -1, i2 = -1;
      if (name != null && ((i1 = name.indexOf("get")) == 0 || (i2 = name.indexOf("is"))
              == 0) && name.length() > 3 && f.getParameterTypes().length == 0) {
        String mName;
        if (i1 == 0) {
          mName = name.substring("get".length());
        } else if (i2 == 0) {
          mName = name.substring("is".length());
        } else {
          continue;
        }
        mName = (mName.charAt(0) + "").toLowerCase() + mName.substring(1);
        //since we are marshalling a null property, we simply call the
        //marshallNullProperty at this point
        marshallNullProperty(elem, namespace, mName, f, mode);
      }
    }
  }

  public static boolean isWrapperType(Class<?> clazz) {
    return PRIMITIVE_WARPPER_MAPPING.containsKey(clazz);
  }

  public static <T> String toXmlString(T t) {
    try {
      VMarshaller<T> m = new VMarshaller<T>();
      VDocument doc = m.marshallDocument(t);
      return doc.toXmlString();
    } catch (VXMLBindingException ex) {
      Logger.getLogger(VMarshaller.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}