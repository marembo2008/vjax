package com.anosym.vjax.schema;

import com.anosym.vjax.annotations.Converter;
import com.anosym.vjax.annotations.Informational;
import com.anosym.vjax.annotations.Marshallable;
import com.anosym.vjax.annotations.SuppressSuperClass;
import com.anosym.vjax.schema.annotations.AccessorType.AccessType;
import com.anosym.vjax.schema.annotations.Global;
import com.anosym.vjax.schema.annotations.ModelGroup;
import com.anosym.vjax.schema.annotations.Schema;
import com.anosym.vjax.schema.annotations.Simple;
import com.anosym.vjax.schema.annotations.Value;
import com.anosym.vjax.test.Single101Data;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import com.anosym.vjax.xml.VNamespace;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class creates a schema from the specified object
 *
 * @author Marembo
 */
public class VSchemaMarshaller<T> {

  final static Map<Class, String> primitiveToXmlMapping = loadPrimitiveMapping();
  final static Map<String, Class> xmlToPrimitiveMapping = loadXmlMapping();

  static Map<Class, String> loadPrimitiveMapping() {
    Map<Class, String> maps = new HashMap<Class, String>();
    maps.put(boolean.class, "xsd:boolean");
    maps.put(char.class, "vjax:char");
    maps.put(byte.class, "xsd:byte");
    maps.put(short.class, "xsd:short");
    maps.put(int.class, "xsd:int");
    maps.put(long.class, "xsd:long");
    maps.put(float.class, "xsd:float");
    maps.put(double.class, "xsd:double");
    maps.put(void.class, "vjax:void");
    return maps;
  }

  static Map<String, Class> loadXmlMapping() {
    Map<String, Class> maps = new HashMap<String, Class>();
    maps.put("xsd:boolean", boolean.class);
    maps.put("vjax:char", char.class);
    maps.put("xsd:byte", byte.class);
    maps.put("xsd:short", short.class);
    maps.put("xsd:int", int.class);
    maps.put("xsd:long", long.class);
    maps.put("xsd:float", float.class);
    maps.put("xsd:double", double.class);
    maps.put("vjax:void", void.class);
    return maps;
  }

  private enum PreservedPrefix {

    xsd,
    xi,
    xsi,
    vjax
  }

  private class ClassProperties {
    //the definition class

    Class propertyClass;
    //the property implementing class, can be the same as the
    //the property class
    Class implementingClass;
    String propertyName;
    Annotation[] annotations;
    T value;
  }
  private transient VDocument document;
  /**
   * New type definition
   */
  private Map<Class, VElement> definitions;
  /**
   * Global Element declarations
   */
  private Map<Class, VElement> declarations;

  /**
   * creates a document schema from the specified object
   *
   * @param instance
   * @return
   */
  public VDocument marshallSchema(T instance) {
    //create the root schema element
    return null;
  }

  private void marshallSchemaAnnotation(Class<T> clazz, T obj, VElement schemaElement, AccessType accessType)
          throws VSchemaException, VNamespaceConflictException {
    Schema schema = clazz.getAnnotation(Schema.class);
    if (schema != null) {
      VNamespace namespace = new VNamespace(schema.prefix(), schema.uri());
      namespace.setAttributeFormDefault(schema.attributeFormQualified());
      namespace.setElementFormDefault(schema.elementFormQualified());
      //make sure that the schema element does not already contain the namespace
      if (schemaElement.definesNamespace(namespace)) {
        throw new VNamespaceConflictException("Schema already defines namespace");
      }
      if (schema.prefix() != null && !schema.prefix().isEmpty()) {
        try {
          //make sure that the namespace prefix does not collide with the preserved prefices
          PreservedPrefix pp = PreservedPrefix.valueOf(schema.prefix().toLowerCase().trim());
          if (pp != null) {
            throw new VSchemaException("Preserved Schema Prefix: " + schema.prefix());
          }
        } catch (IllegalArgumentException ex) {
          //ignore if such prefix does not exists
        }
      }
      schemaElement.addNamespace(namespace);
      //add the other namespace as attributes
    }
    VElement elem = new VElement("element");
    schemaElement.addChild(elem, VNamespace.SCHEMA_NAMESPACE);
    elem.addAttribute(new VAttribute("type", clazz.getSimpleName()));
    VElement complex = new VElement("complexType");
    complex.addAttribute(new VAttribute("name", clazz.getSimpleName()));
    schemaElement.addChild(complex, VNamespace.SCHEMA_NAMESPACE);
    defineComplexType(obj, clazz, null, complex, schemaElement, accessType);
  }

  private void defineComplexType(T obj, Class<?> cls, Annotation[] annots,
          VElement complex, VElement schemaElement, AccessType accessType) throws VSchemaException {
    Class<?> clazz = (obj != null) ? obj.getClass() : cls;
    //if this is not global, then dont give it a name
    boolean global = false;
    if (annots != null) {
      for (Annotation a : annots) {
        if (a.getClass() == Global.class) {
          global = true;
          break;
        }
      }
    }
    if (global) {
      VAttribute nameAttr = new VAttribute("name", clazz.getSimpleName(), VNamespace.SCHEMA_NAMESPACE);
      complex.addAttribute(nameAttr);
      schemaElement.addChild(complex);
    }
    //lets add application information
    VElement annot = new VElement("annotation");
    complex.addChild(annot, VNamespace.SCHEMA_NAMESPACE);
    VElement appInfo = new VElement("appInfo");
    annot.addChild(appInfo, VNamespace.SCHEMA_NAMESPACE);
    //add the vjax specific element
    VElement vjax = new VElement("appInfo");
    appInfo.addChild(vjax, VNamespace.VJAX_NAMESPACE);
    if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
      //class information
      VElement defClass = new VElement("definition", vjax);
      VElement _clazz = new VElement("class", defClass);
      _clazz.setContent(cls.getSimpleName());
      if (cls.isInterface()) {
        _clazz.addAttribute(new VAttribute("interface", "true"));
      } else {
        _clazz.addAttribute(new VAttribute("abstract", "true"));
      }
      //outer class reference if it has been defined as  inner
      if (cls.isMemberClass()) {
        _clazz = new VElement("outerClass", _clazz);
        _clazz.setContent(cls.getEnclosingClass().getSimpleName());
        if (Modifier.isStatic(cls.getModifiers())) {
          defClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.STATIC.name()));
        } else if (cls.isAnonymousClass()) {
          defClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.ANONYMOUS.name()));
        } else {
          defClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.INNER.name()));
        }
      }
      //definition package
      _clazz = new VElement("package", defClass);
      _clazz.setContent(cls.getPackage().getName());
    }
    if (obj != null) {
      //do we have information in the implementing class
      //implementing class information
      VElement implClass = new VElement("implementation", vjax);
      VElement _clazz = new VElement("class", implClass);
      _clazz.setContent(clazz.getSimpleName());
      //outer class reference if it has been defined as  inner
      if (clazz.isMemberClass()) {
        _clazz = new VElement("outerClass", _clazz);
        _clazz.setContent(clazz.getEnclosingClass().getSimpleName());
        if (Modifier.isStatic(clazz.getModifiers())) {
          implClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.STATIC.name()));
        } else if (clazz.isAnonymousClass()) {
          implClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.ANONYMOUS.name()));
        } else {
          implClass.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.INNER.name()));
        }
      }
      //implementation package
      _clazz = new VElement("package", implClass);
      _clazz.setContent(clazz.getPackage().getName());
    }
    //lets define the child element
    defineTypeElement(obj, clazz, annots, complex, schemaElement, accessType);
  }

  private void defineTypeElement(T obj, Class<?> cls, Annotation[] annots,
          VElement complexType, VElement schemaElement, AccessType accessType) throws VSchemaException {
    Class<?> clazz = (obj != null) ? obj.getClass() : cls;
    //get all the properties
    List<ClassProperties> props = getClassProperties(obj, clazz, annots, accessType);
    if (!props.isEmpty()) {
      //lets get the model group if defined
      ModelGroup mGroup = clazz.getAnnotation(ModelGroup.class);
      //any field/method property should override the class annotation
      if (annots != null && annots.length > 0) {
        for (Annotation a : annots) {
          if (a.getClass() == ModelGroup.class) {
            mGroup = (ModelGroup) a;
            break;
          }
        }
      }
      //default grouping of child elements
      ModelGroup.GroupType gType = null;
      if (mGroup != null) {
        gType = mGroup.type();
      } else {
        gType = ModelGroup.GroupType.SEQUENCE;
      }
      VElement group = new VElement(gType.name().toLowerCase());
      complexType.addChild(group, VNamespace.SCHEMA_NAMESPACE);
      for (ClassProperties cp : props) {
        VElement elem = new VElement("element");
        group.addChild(elem, VNamespace.SCHEMA_NAMESPACE);
        elem.addAttribute(new VAttribute("name", cp.propertyName));
        //what type of access are we employing
        VElement annot = new VElement("annotation");
        elem.addChild(annot, VNamespace.SCHEMA_NAMESPACE);
        VElement appInfo = new VElement("appInfo");
        annot.addChild(appInfo, VNamespace.SCHEMA_NAMESPACE);
        VElement vjax = new VElement("appInfo");
        appInfo.addChild(vjax, VNamespace.VJAX_NAMESPACE);
        VElement propAcess = new VElement("propertyAccessor", vjax);
        propAcess.addAttribute(new VAttribute("accessType", accessType.name()));
        if (cp.propertyClass.isPrimitive()) {
          //find the xml mapping
          String type = findXmlMapping(cp.propertyClass);
          elem.addAttribute(new VAttribute("type", type));
        } else {
          //has we defined the simple type
          Simple simple = null;
          //is the field annotated with a converter
          Converter con = null;
          for (Annotation a : cp.annotations) {
            if (a.getClass() == Converter.class) {
              con = (Converter) a;
            } else if (a.getClass() == Simple.class) {
              simple = (Simple) a;
            }
          }
          if (simple != null) {
            //then the class must define at least one value or a converter
            if (con != null) {
              //then will have simple direct mapping
              elem.addAttribute(new VAttribute("type", "xsd:" + simple.base()));
              //add converter information
              VElement convElem = new VElement("converter", vjax);
              VElement cClass = new VElement("class", convElem);
              cClass.setContent(con.converter().getSimpleName());
              VElement pkg = new VElement("package", convElem);
              pkg.setContent(con.converter().getPackage().getName());
              //outer class reference if it has been defined as  inner
              if (con.converter().isMemberClass()) {
                VElement _clazz = new VElement("outerClass", convElem);
                _clazz.setContent(clazz.getEnclosingClass().getSimpleName());
                if (Modifier.isStatic(con.converter().getModifiers())) {
                  convElem.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.STATIC.name()));
                } else if (con.converter().isAnonymousClass()) {
                  convElem.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.ANONYMOUS.name()));
                } else {
                  convElem.findChild("class").addAttribute(new VAttribute("innerClassType", InnerClassType.INNER.name()));
                }
              }
            } else {
              //the @value annotation or the converter annotation in one of the fields
              //get the class properties
              List<ClassProperties> cps = getClassProperties(cp.value, cp.implementingClass, cp.annotations, accessType);
              for (ClassProperties c : cps) {
                Value v = null;
                Converter cv = null;
                for (Annotation a : c.annotations) {
                  if (a.getClass() == Value.class) {
                    v = (Value) a;
                    break;
                  } else if (a.getClass() == Converter.class) {
                    cv = (Converter) a;
                    break;
                  }
                }
              }
            }
          }
          if (simple != null && con == null) {
            throw new VSchemaException("Invalid XmlType Specification: There is no converter specified for=" + cp.propertyClass.getName());
          }
        }
        //
        VElement complex = new VElement("complexType");
        elem.addChild(complex, VNamespace.SCHEMA_NAMESPACE);
        defineComplexType(cp.value, cp.propertyClass, annots, complex, schemaElement, accessType);
        //
      }
    }
  }

  private String findXmlMapping(Class primitiveClass) {
    return primitiveToXmlMapping.get(primitiveClass);
  }

  private List<ClassProperties> getClassProperties(T obj, Class<?> cls, Annotation[] annots, AccessType accessType) throws VSchemaException {
    Class<?> clazz = (obj != null) ? obj.getClass() : cls;
    List<ClassProperties> list = new ArrayList<ClassProperties>();
    if (accessType == AccessType.METHOD) {
      List<Method> mms = new ArrayList<Method>(Arrays.asList(clazz.getMethods()));
      normalizeMethods(mms, false, clazz);
      //be sure that the list is not empty
      if (!mms.isEmpty()) {
        for (Method m : mms) {
          ClassProperties cp = new ClassProperties();
          cp.annotations = m.getAnnotations();
          cp.propertyClass = m.getReturnType();
          String name = m.getName();
          if (name.startsWith("get")) {
            name = name.substring(3);
          } else if (name.startsWith("is")) {
            name = name.substring(2);
          }
          name = name.toLowerCase().charAt(0) + name.substring(1);
          cp.propertyName = name;
          try {
            obj = (T) m.invoke(obj, new Object[]{});
            if (obj != null) {
              cp.implementingClass = obj.getClass();
            }
            cp.value = obj;
          } catch (Exception e) {
            throw new VSchemaException(e);
          }
          list.add(cp);
        }
      }
    } else {
      //by default use field properties
      List<Field> fields = new ArrayList<Field>();
      doNormalizeFields(clazz, annots, fields);
      normalizeFields(fields, clazz);
      for (Field f : fields) {
        ClassProperties cp = new ClassProperties();
        cp.annotations = f.getAnnotations();
        cp.propertyClass = f.getType();
        cp.propertyName = f.getName();
        try {
          Object value = f.get(obj);
          if (value != null) {
            cp.implementingClass = value.getClass();
          }
          cp.value = (T) value;
        } catch (Exception e) {
          throw new VSchemaException(e);
        }
        list.add(cp);
      }
    }
    return list;
  }

  private void doNormalizeFields(Class<?> clazz, Annotation[] annots, List<Field> fields) {
    if (clazz == null) {
      return;
    }
    fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    //are we allowed to marshall inherited fields
    SuppressSuperClass ssc = clazz.getAnnotation(SuppressSuperClass.class);
    if (ssc == null) {
      for (Annotation a : annots) {
        if (a.getClass() == Annotation.class) {
          ssc = (SuppressSuperClass) a;
          break;
        }
      }
      if (ssc == null) {
        doNormalizeFields(clazz.getSuperclass(), annots, fields);
      }
    }
  }

  private void normalizeFields(List<Field> fields, Class<?> clazz) {
    for (ListIterator<Field> it = fields.listIterator(); it.hasNext();) {
      Field f = it.next();
      int mod = f.getModifiers();
      if (Modifier.isTransient(mod) || Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
        it.remove();
      } else {
        for (Annotation a : f.getAnnotations()) {
          if (a.getClass() == Marshallable.class) {
            Marshallable m = (Marshallable) a;
            if (!m.marshal()) {
              it.remove();
            }
          } else if (a.getClass() == Deprecated.class) {
            it.remove();
          }
        }
      }
    }
  }

  private static Method getOverridenMethod(Method m, Class clazz) {
    Class clz = m.getDeclaringClass();
    if (clz.equals(clazz)) {
      //then get the superclass and determine if it declares the method
      Class sClazz = clz.getSuperclass();
      try {
        m = sClazz.getMethod(m.getName(), m.getParameterTypes());
        return getOverridenMethod(m, sClazz);
      } catch (Exception e) {
        return m;
      }
    } else {
      return getOverridenMethod(m, clz);
    }
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
          Method om = getOverridenMethod(m, clazz);
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

  public static void main(String[] args) throws VSchemaException {
    VElement e = new VElement("schema");
    e.addChild(e, VNamespace.VJAX_NAMESPACE);
  }
}
