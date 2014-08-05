/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.PrimitiveType;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Position;
import com.anosym.vjax.annotations.v3.AccessSuper;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.xml.VDocument;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class maps html element directly to object member instances. no schema, or class information is required on the
 * document.
 *
 * The marshaller may operate, by ignoring all elements that cannot be mapped to member instances of the specified
 * class. this must be explicitly specified, otherwise a MemberNotFoundException will be raised.
 *
 * @author marembo
 * @param <T> The object type to unmarshall.
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
        try {
            return marshaller.marshall(object);
        } catch (VXMLBindingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String doMarshall(T object) {
        try {
            return marshaller.doMarshall(object);
        } catch (VXMLBindingException ex) {
            throw new RuntimeException(ex);
        }
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

    @SuppressWarnings("rawtypes")
    public static boolean isPrimitiveOrPrimitiveWrapper(Class cls) {
        return PRIMITIVE_WRAPPER_MAPPING.containsKey(cls)
                || PRIMITIVE_WRAPPER_MAPPING.containsValue(cls);
    }

    public static Class getPrimitiveClass(Class clsPrimitiveOrPrimitiveWrapper) {
        if (clsPrimitiveOrPrimitiveWrapper.isPrimitive()) {
            return clsPrimitiveOrPrimitiveWrapper;
        }
        return PRIMITIVE_WRAPPER_MAPPING.get(clsPrimitiveOrPrimitiveWrapper);
    }

    public static Class findCollectionGenericType(Field f) {
        Type genericType = f.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type typeArg = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (typeArg instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) typeArg).getRawType();
            }
            return (Class) typeArg;
        }
        return null;
    }

    public static Class findGenericType(Method f) {
        Type genericType = f.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            Type typeArg = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (typeArg instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) typeArg).getRawType();
            }
            return (Class) typeArg;
        }
        return null;
    }

    public static Class[] findMapGenericType(Field f) {
        Type genericType = f.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type typeArg0 = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            Type typeArg1 = ((ParameterizedType) genericType).getActualTypeArguments()[1];
            return new Class[]{(Class) typeArg0, (Class) typeArg1};
        }
        return new Class[0];
    }

    public static Class[] findMapGenericType(Method me) {
        Type genericType = me.getGenericReturnType();
        if (genericType instanceof ParameterizedType) {
            Type typeArg0 = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            Type typeArg1 = ((ParameterizedType) genericType).getActualTypeArguments()[1];
            return new Class[]{(Class) typeArg0, (Class) typeArg1};
        }
        return new Class[0];
    }

    /**
     * Returns all declared fields excluding any field declared in {@link Object} class.
     *
     * @param clazz
     * @param fields
     */
    static void getFields(Class clazz, List<Field> fields) throws VXMLBindingException {
        List<FieldPropertyInfo> infos = new ArrayList<FieldPropertyInfo>();
        getFields0(clazz, infos);
        Collections.sort(infos);
        for (FieldPropertyInfo info : infos) {
            fields.add(info.field);
        }
    }

    /**
     * Returns all declared fields excluding any field declared in {@link Object} class.
     *
     * @param clazz
     * @param fields
     */
    private static void getFields0(Class clazz, List<FieldPropertyInfo> fields) throws VXMLBindingException {
        int pos = Integer.MAX_VALUE;
        Field idField = null;
        for (Field f : clazz.getDeclaredFields()) {
            int modifier = f.getModifiers();
            //ensure it is not static, nor final
            if (!Modifier.isFinal(modifier)
                    && !Modifier.isStatic(modifier)
                    && !f.isAnnotationPresent(Transient.class)) {
                //if it is an id field, add it as the first.
                if (f.isAnnotationPresent(Id.class)) {
                    if (idField != null) {
                        throw new VXMLBindingException(
                                "Multiple ID fields specified: " + idField.toString() + " and " + f.toString());
                    }
                    idField = f;
                    //check if the field at 0 is also an id field
                    pos = 0;
                }
                Position p = f.getAnnotation(Position.class);
                if (p != null) {
                    pos = p.index();
                }
                fields.add(new FieldPropertyInfo(f, pos));
            }
        }
        AccessSuper accessSuper = (AccessSuper) clazz.getAnnotation(AccessSuper.class);
        if (clazz.getSuperclass() != Object.class && (accessSuper == null || accessSuper.value())) {
            getFields0(clazz.getSuperclass(), fields);
        }
    }

    /**
     * Returns all declared fields excluding any field declared in {@link Object} class.
     *
     * @param clazz
     * @param methods
     */
    static void getMethods(Class clazz, List<Method> methods, boolean getters) throws VXMLBindingException {
        Pattern pGet = Pattern.compile("\\b(is|get)\\w+"); //starts with is/get and at least one more character
        Pattern pSet = Pattern.compile("\\b(set)\\w+"); //starts with set and at least one more character
        Map<String, MethodPropertyInfo> infos = new HashMap<String, MethodPropertyInfo>();
        getMethods0(clazz, infos, getters);
        List<MethodPropertyInfo> sortedList = new ArrayList<MethodPropertyInfo>(infos.values());
        Collections.sort(sortedList);
        for (MethodPropertyInfo info : sortedList) {
            Method m = null;
            if (getters && info.getter != null) {
                m = info.getter;
            } else if (!getters && info.setter != null) {
                m = info.setter;
            }
            if (m != null) {
                methods.add(m);
            }
        }
    }

    /**
     * Returns all declared fields excluding any field declared in {@link Object} class.
     *
     * @param clazz
     * @param methods
     */
    private static void getMethods0(Class clazz, Map<String, MethodPropertyInfo> infos, boolean getters) throws
            VXMLBindingException {
        Pattern pGet = Pattern.compile("\\b(is|get)\\w+"); //starts with is/get and at least one more character
        Pattern pSet = Pattern.compile("\\b(set)\\w+"); //starts with set and at least one more character
        Method idProperty = null;
        for (Method m : clazz.getDeclaredMethods()) {
            int modifier = m.getModifiers();
            //ensure it is not static, nor final
            //only public methods.
            if (!Modifier.isFinal(modifier)
                    && !Modifier.isStatic(modifier)
                    && !m.isAnnotationPresent(Transient.class)
                    && Modifier.isPublic(modifier)) {
                String property = getProperty(m);
                MethodPropertyInfo info = infos.get(property);
                if (info == null) {
                    info = new MethodPropertyInfo();
                    infos.put(property, info);
                }
                String mName = m.getName();
                Matcher mGet = pGet.matcher(mName);
                if (mGet.find()) {
                    //if this is a getter and an id is specified?
                    info.getter = m;
                    //must start with is/get
                    //if it is an id property, add it as the first.
                    if (m.isAnnotationPresent(Id.class)) {
                        //check if the property at 0 is also an id propertys
                        if (idProperty != null) {
                            //Id had already been specified, throw an IllegalArgumentException.
                            throw new VXMLBindingException(
                                    "Multiple ID properties specified: " + idProperty.toString() + " and " + m
                                    .toString());
                        }
                        idProperty = m;
                        info.propertyIndex = 0;
                    }
                    Position p = m.getAnnotation(Position.class);
                    if (p != null) {
                        info.propertyIndex = p.index();
                    }
                } else if (!getters) {
                    Matcher mSet = pSet.matcher(mName);
                    if (mSet.find()) {
                        //ensure that the parameter to the method is only one.
                        if (m.getParameterTypes().length != 1) {
                            continue;
                        }
                        //get the getter equivalence. If it is already added, we may have
                        info.setter = m;
                    }
                }
            }
        }
        AccessSuper accessSuper = (AccessSuper) clazz.getAnnotation(AccessSuper.class);
        if (clazz.getSuperclass() != Object.class && (accessSuper == null || accessSuper.value())) {
            getMethods0(clazz.getSuperclass(), infos, getters);
        }
    }

    private static String getProperty(Method m) {
        final Pattern p = Pattern.compile("\\b(is|get|set)");
        String markup = m.getName();
        Matcher mm = p.matcher(markup);
        if (mm.find()) {
            markup = mm.replaceAll("");
        }
        return markup.substring(0, 1).toLowerCase() + markup.substring(1);
    }

    private static final class MethodPropertyInfo implements Comparable<MethodPropertyInfo> {

        private Method getter;
        private Method setter;
        private int propertyIndex = Integer.MAX_VALUE;

        @Override
        public int compareTo(MethodPropertyInfo o) {
            return Integer.valueOf(propertyIndex).compareTo(o.propertyIndex);
        }

    }

    private static final class FieldPropertyInfo implements Comparable<FieldPropertyInfo> {

        private Field field;
        private int propertyIndex = Integer.MAX_VALUE;

        public FieldPropertyInfo(Field field, int propertyIndex) {
            this.field = field;
            this.propertyIndex = propertyIndex;
        }

        @Override
        public int compareTo(FieldPropertyInfo o) {
            return Integer.valueOf(propertyIndex).compareTo(o.propertyIndex);
        }

    }
}
