/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.PrimitiveType;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.xml.VDocument;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    static final Map<Class, Class> PRIMITIVE_WRAPPER_MAPPING = loadPrimitiveMapping();
    @SuppressWarnings({"rawtypes"})
    static final Map<Class, PrimitiveType> PRIMITIVE_MAPPING_TYPES = loadPrimitiveMappingTypes();

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
    private final Marshaller<T> marshaller;
    private final Unmarshaller<T> unmarshaller;

    public VObjectMarshaller(Class<? extends T> instanceClass) {
        this(instanceClass, false);
    }

    public VObjectMarshaller(Class<? extends T> instanceClass,
            boolean ignoreUnmappedElements) {
        this.instanceClass = instanceClass;
        this.ignoreUnmappedElements = ignoreUnmappedElements;
        this.marshaller = new Marshaller<T>(instanceClass);
        this.unmarshaller = new Unmarshaller<T>(instanceClass);
    }

    public VDocument marshall(T object) {
        return marshaller.marshall(object);
    }

    public String doMarshall(T object) {
        return marshaller.doMarshall(object);
    }

    public T unmarshall(VDocument document) throws VXMLMemberNotFoundException,
            VXMLBindingException {
        return unmarshaller.unmarshall(document);
    }

    /**
     * Returns annotation of the specified class if its presents in the array
     *
     * @param <A>
     * @param annots
     * @param clazz
     * @return
     */
    static <A extends Annotation> A getAnnotation(Annotation[] annots,
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

    /**
     * Returns all declared fields excluding any field declared in
     * {@link Object} class.
     *
     * @param clazz
     * @param fields
     */
    static void getFields(Class clazz, List<Field> fields) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getAnnotation(Transient.class) == null) {
                fields.add(f);
            }
        }
        if (clazz.getSuperclass() != Object.class) {
            getFields(clazz.getSuperclass(), fields);
        }
    }
}
