/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.PrimitiveType;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayComponentInitializer;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.annotations.v3.CollectionElement;
import com.anosym.vjax.annotations.v3.CollectionElementConverter;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.ConverterParam;
import com.anosym.vjax.annotations.v3.Define;
import com.anosym.vjax.annotations.v3.GenericCollectionType;
import com.anosym.vjax.annotations.v3.GenericMapType;
import com.anosym.vjax.annotations.v3.Listener;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.VBigDecimalConverter;
import com.anosym.vjax.converter.v3.impl.PropertyListener;
import com.anosym.vjax.exceptions.VConverterBindingException;
import com.anosym.vjax.util.VConditional;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
 * The class maps html element directly to object member instances. no schema,
 * or class information is required on the document.
 *
 * The marshaller may operate, by ignoring all elements that cannot be mapped to
 * member instances of the specified class. this must be explicitly specified,
 * otherwise a MemberNotFoundException will be raised.
 *
 * @author marembo
 */
public class VObjectMarshaller<T> {

    @SuppressWarnings("rawtypes")
    private static final Map<Class, Class> PRIMITIVE_WRAPPER_MAPPING = loadPrimitiveMapping();
    @SuppressWarnings({"rawtypes"})
    private static final Map<Class, PrimitiveType> PRIMITIVE_MAPPING_TYPES = loadPrimitiveMappingTypes();

    @SuppressWarnings("rawtypes")
    static Map<Class, Class> loadPrimitiveMapping() {
        Map<Class, Class> maps = new HashMap<Class, Class>();
        maps.put(Boolean.class, boolean.class);
        maps.put(Character.class, char.class);
        maps.put(Byte.class, byte.class);
        maps.put(Short.class, short.class);
        maps.put(Integer.class, int.class);
        maps.put(Long.class, long.class);
        maps.put(Float.class, float.class);
        maps.put(Double.class, double.class);
        maps.put(Void.class, void.class);
        return maps;
    }

    @SuppressWarnings("rawtypes")
    static Map<Class, PrimitiveType> loadPrimitiveMappingTypes() {
        Map<Class, PrimitiveType> maps = new HashMap<Class, PrimitiveType>();
        maps.put(boolean.class, PrimitiveType.BOOLEAN);
        maps.put(char.class, PrimitiveType.CHAR);
        maps.put(byte.class, PrimitiveType.BYTE);
        maps.put(short.class, PrimitiveType.SHORT);
        maps.put(int.class, PrimitiveType.INT);
        maps.put(long.class, PrimitiveType.LONG);
        maps.put(float.class, PrimitiveType.FLOAT);
        maps.put(double.class, PrimitiveType.DOUBLE);
        maps.put(void.class, PrimitiveType.VOID);
        return maps;
    }
    private final Class<? extends T> instanceClass;
    @SuppressWarnings("unused")
    private boolean ignoreUnmappedElements;

    public VObjectMarshaller(Class<? extends T> instanceClass) {
        this.instanceClass = instanceClass;
    }

    public VObjectMarshaller(Class<? extends T> instanceClass,
            boolean ignoreUnmappedElements) {
        this.instanceClass = instanceClass;
        this.ignoreUnmappedElements = ignoreUnmappedElements;
    }

    @SuppressWarnings("rawtypes")
    private boolean isPrimitiveOrPrimitiveWrapper(Class cls) {
        return PRIMITIVE_WRAPPER_MAPPING.containsKey(cls)
                || PRIMITIVE_WRAPPER_MAPPING.containsValue(cls);
    }

    private String put(String id, Object data) {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(id).append(">");
        sb.append(data);
        sb.append("</").append(id).append(">");
        return sb.toString();
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

    public VDocument marshall(T object) {
        return VDocument.parseDocumentFromString(doMarshall(object));
    }

    public String doMarshall(T object) {
        String name = object.getClass().getSimpleName();
        return marshall(object, name, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String marshall(T object, String markupName,
            Annotation[] annotations) {
        String data = "";
        Class<? extends Object> c = object.getClass();
        if (isPrimitiveOrPrimitiveWrapper(c) || c.equals(String.class)) {
            markupName = markupName == null ? c.getSimpleName() : markupName;
            data += put(markupName, object.toString());
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
            Object[] arr = (Object[]) object;
            Class cmpClass = c.getComponentType();
            ArrayParented ap = getAnnotation(annotations, ArrayParented.class);
            boolean parented = ap != null
                    && ap.componentMarkup() != null
                    && ap.componentMarkup().length() != 0;
            String arrayMarkup = markupName;
            if (parented) {
                markupName = ap.componentMarkup();
            }
            for (Object o : arr) {
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
        } else if (Collection.class.isAssignableFrom(c)) {
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
        } else if (Map.class.isAssignableFrom(c)) {
            Map m = (Map) object;
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
                markupName = c.getClass().getSimpleName();
            }
            return put(markupName, mapEntries);
        } else {
            List<Field> fields = new ArrayList<Field>();
            getFields(c, fields);
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
                        if (isPrimitiveOrPrimitiveWrapper(cl)
                                || cl.equals(String.class)) {
                            data += put(markup, value);
                        } else {
                            data += marshall((T) value, markup,
                                    f.getDeclaredAnnotations());
                        }
                    } catch (Exception e) {
                        Logger.getLogger(VObjectMarshaller.class.getName()).log(Level.SEVERE, data, e);
                    }
                }
            }
            if (markupName != null) {
                data = put(markupName, data);
            }
        }
        return data;
    }

    public T unmarshall(VDocument document) throws VXMLMemberNotFoundException,
            VXMLBindingException {
        return unmarshal(document.getRootElement(), instanceClass, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public T unmarshal(VElement element, Class clazz, Annotation[] annots)
            throws VXMLBindingException {
        try {
            Converter converter = getAnnotation(annots, Converter.class);
            if (converter != null) {
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
            Define define = getAnnotation(annots, Define.class);
            if (define != null) {
                //the class is defined using a different class, probably the current class represents and interface or abstract class.
                Class<com.anosym.vjax.v3.initializer.Initializer> definer = (Class<com.anosym.vjax.v3.initializer.Initializer>) define.value();
                com.anosym.vjax.v3.initializer.Initializer initializer = definer.newInstance();
                clazz = initializer.define(element);
            }
            if (clazz.isAssignableFrom(BigDecimal.class) && getAnnotation(annots, Converter.class) == null) {
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
                int children = element.getChildren().size();
                T[] arr = (T[]) new Object[children];
                int i = 0;
                ArrayComponentInitializer aci = getAnnotation(annots, ArrayComponentInitializer.class);
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
                instance = (T) arr;
                return instance;
            }
            if (instance == null) {
                // check if we have a converter or a collection converter
                Converter annot = getAnnotation(annots, Converter.class);
                CollectionElementConverter cec = getAnnotation(annots, CollectionElementConverter.class);
                Class cls = annot != null ? annot.value() : cec != null ? cec.value() : null;
                if (cls != null) {
                    // find the converted to value
                    com.anosym.vjax.converter.v3.Converter<T, Object> cnvs = null;
                    if (annot != null && annot.params().length > 0) {
                        ConverterParam[] params = annot.params();
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
                } else {
                    instance = (T) clazz.newInstance();
                }
            }
            if (element.hasChildren()) {
                List<Field> fields = new ArrayList<Field>();
                getFields(clazz, fields);
                for (final Field f : fields) {
                    Class<?> typeClass = f.getType();
                    Object propety = null;
                    List<VElement> elems = element.getChildren(new VConditional<VElement>() {
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
                    if (typeClass.isArray()) {
                        if (elems.size() > 0) {
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
                            propety = arr;
                        }
                    } else if (Collection.class.isAssignableFrom(typeClass)) {
                        if (elems.size() == 1) {
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
                                continue;
                            }
                            for (VElement c : listElem.getChildren()) {
                                l.add(unmarshal(c, type.value(),
                                        f.getAnnotations()));
                            }
                            propety = l;
                        }
                    } else if (Map.class.isAssignableFrom(typeClass)) {
                        if (elems.size() == 1) {
                            Map<Object, T> m = new HashMap<Object, T>();
                            GenericMapType type = f
                                    .getAnnotation(GenericMapType.class);
                            if (type == null) {
                                continue;
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
                            propety = m;
                        }
                    } else if (!elems.isEmpty()) {
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
            return instance;
        } catch (Exception ex) {
            throw new VXMLBindingException(ex);
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
        PrimitiveType primitiveType = PRIMITIVE_MAPPING_TYPES
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

    /**
     * Returns all declared fields excluding any field declared in
     * {@link Object} class.
     *
     * @param clazz
     * @param fields
     */
    private static void getFields(Class clazz, List<Field> fields) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != Object.class) {
            getFields(clazz.getSuperclass(), fields);
        }
    }
}
