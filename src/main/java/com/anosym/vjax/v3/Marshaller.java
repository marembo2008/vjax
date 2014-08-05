/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.AccessOption;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.annotations.v3.ElementMarkup;
import com.anosym.vjax.annotations.v3.CollectionElementConverter;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.Default;
import com.anosym.vjax.annotations.v3.DefinedAttribute;
import com.anosym.vjax.annotations.v3.DefinedAttribute.DefinedAttributes;
import com.anosym.vjax.annotations.v3.Implemented;
import com.anosym.vjax.annotations.v3.KeyValueEntryMarkup;
import com.anosym.vjax.annotations.v3.Marshallable;
import com.anosym.vjax.annotations.v3.Nullable;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.VBigDecimalConverter;
import com.anosym.vjax.exceptions.VConverterBindingException;
import com.anosym.vjax.v3.defaulter.Defaulter;

import static com.anosym.vjax.v3.VObjectMarshaller.getAnnotation;

import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VContent;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.anosym.vjax.v3.VObjectMarshaller.isPrimitiveOrPrimitiveWrapper;

/**
 *
 * @author marembo
 */
class Marshaller<T> {

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
    private final List<String> ID_REFERENCES = new ArrayList<String>();
    private final Map<Integer, List<Object>> CIRCULAR_REFERENCES = new HashMap<Integer, List<Object>>();

    public Marshaller(Class<? extends T> instanceClass) {
        this.instanceClass = instanceClass;
    }

    public VDocument marshall(T object) throws VXMLBindingException {
        try {
            String name = instanceClass.getSimpleName();
            Markup m = instanceClass.getAnnotation(Markup.class);
            if (m != null) {
                name = m.name();
            }
            VElement root = marshall0(object, name, null, null);
            VDocument doc = new VDocument();
            doc.setRootElement(root);
            return doc;
        } finally {
            REFERENCES.clear();
        }
    }

    public String doMarshall(T object) throws VXMLBindingException {
        return marshall(object).toXmlString();
    }

    private VElement put(String id, VElement child) {
        VElement newElem = new VElement(id);
        newElem.addChild(child);
        return newElem;
    }

    private VElement putWithAttributes(String id, VElement child, Map<String, String> attributes) {
        VElement newElem = new VElement(id);
        newElem.addChild(child);
        for (Map.Entry<String, String> e : attributes.entrySet()) {
            newElem.addAttribute(new VAttribute(e.getKey(), e.getValue()));
        }
        return newElem;
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private VElement marshallFields(T object, VElement parent, Annotation[] annotations) throws VXMLBindingException {
        List<Field> fields = new ArrayList<Field>();
        VObjectMarshaller.getFields(object.getClass(), fields);
        Map<String, String> attributes = new HashMap<String, String>();
        for (Field fProperty : fields) {
            try {
                Class elementClass = fProperty.getType();
                fProperty.setAccessible(true);
                Object value = fProperty.get(object);
                marshallProperty(parent, annotations, fProperty, fProperty.getName(), elementClass, value, attributes);
            } catch (Exception e) {
                Throwables.propagateIfPossible(e, VXMLBindingException.class);
                throw new VXMLBindingException(e);
            }
        }
        if (!attributes.isEmpty()) {
            for (Map.Entry<String, String> e : attributes.entrySet()) {
                parent.addAttribute(new VAttribute(e.getKey(), e.getValue()));
            }
        }
        return parent;
    }

    private String getIdReference(Object obj) throws VXMLBindingException {
        //get field annotated with @Id
        List<Field> fields = new ArrayList<Field>();
        VObjectMarshaller.getFields(obj.getClass(), fields);
        for (Field f : fields) {
            if (f.isAnnotationPresent(Id.class)) {
                f.setAccessible(true);
                try {
                    Object value = f.get(obj);
                    return String.valueOf(value);
                } catch (Exception ex) {
                    throw new VXMLBindingException(ex);
                }
            }
        }
        return null;
    }

    private Object marshallNullProperty(
            String markup, VElement parent, Annotation[] annotations, AccessibleObject mProperty,
            Class<?> elementClass, Object value, Map<String, String> attributes) throws VXMLBindingException,
            InstantiationException, IllegalAccessException {

        if (mProperty.isAnnotationPresent(Id.class)) {
            throw new VXMLBindingException("Id field cannot be null. Ignoring any defaults defined for this property");
        }
        //do we have default defined for this field?
        Default def = mProperty.getAnnotation(Default.class);
        if (def != null) {
            String defValue = def.value();
            if (!Strings.isNullOrEmpty(defValue)) {
                //get type
                if (isPrimitiveOrPrimitiveWrapper(elementClass)) {
                    value = Unmarshaller.unmarshallPrimitive(defValue, elementClass, null);
                } else if (String.class.isAssignableFrom(elementClass)) {
                    value = defValue;
                } else if (BigDecimal.class.isAssignableFrom(elementClass)) {
                    value = new BigDecimal(defValue);
                } else {
                    throw new VXMLBindingException(
                            "Unsupported default value without a Defaulter implementation");
                }
            } else {
                //check defaulter.
                Class<?> defaulterClass = def.defaulter();
                Defaulter defaulter = (Defaulter) defaulterClass.newInstance();
                value = defaulter.getDefault(elementClass);
                //this may be an interface, redefine the elementclass
            }
        } else if (mProperty.isAnnotationPresent(Nullable.class)) {
            if (mProperty.isAnnotationPresent(Attribute.class)) {
                attributes.put(markup, "");
                return null;
            } else {
                //what if the instance is not a primitive type?
                //we tacitly assume that the type of the field can be instantiated using default constructor.
                if (isPrimitiveOrPrimitiveWrapper(elementClass)
                        || String.class.isAssignableFrom(elementClass)
                        || BigDecimal.class.isAssignableFrom(elementClass)) {
                    VElement newElem = put(markup, VContent.valueOf(""));
                    parent.addChild(newElem);
                    return null;
                } else {
                    //probably the class is an interface
                    //we cannot use the @Define annotation here as it is primarily used when xml element is to be unmarshalled.
                    if (mProperty.isAnnotationPresent(Implemented.class)) {
                        Implemented impl = mProperty.getAnnotation(Implemented.class);
                        if (impl == null && elementClass.isAnnotationPresent(Implemented.class)) {
                            impl = (Implemented) elementClass.getAnnotation(Implemented.class);
                        }
                        if (impl != null) {
                            elementClass = impl.value();
                        }
                    }
                    //in any if we try to invoke new instance on an interface/asbtract class we are going to have exception thrown.
                    //but to be clear
                    if (elementClass.isInterface()) {
                        String message = "the element: " + markup + " "
                                + "has been declared as @Nullable, but no @Implemented annotation present for the interface: "
                                + "" + elementClass;
                        throw new VXMLBindingException(message);
                    } else if (Modifier.isAbstract(elementClass.getModifiers())) {
                        String message = "the element: " + markup + " "
                                + "has been declared as @Nullable, but no @Implemented annotation present for the abstract class: "
                                + "" + elementClass;
                        throw new VXMLBindingException(message);
                    }
                    value = (T) elementClass.newInstance();
                    //the value may be requiring converters, so proceed with normal marshalling.
                }
            }
        }
        return value;
    }

    private void marshallProperty(
            VElement parent, Annotation[] annotations, AccessibleObject property, String markup,
            Class<?> elementClass, Object value, Map<String, String> attributes) throws VXMLBindingException {
        Member member = (Member) property;
        int mod = member.getModifiers();
        if ((mod & Modifier.STATIC) == 0 && (mod & Modifier.FINAL) == 0
                && property.getAnnotation(Transient.class) == null) {
            Marshallable marshallable = property.getAnnotation(Marshallable.class);
            if (marshallable != null
                    && marshallable.value() != Marshallable.Option.BOTH
                    && marshallable.value() != Marshallable.Option.MARSHALL) {
                return;
            }
            try {
                Markup m = property.getAnnotation(Markup.class);
                if (m != null) {
                    markup = m.name();
                }
                if (value == null) {
                    value = marshallNullProperty(markup, parent, annotations, property, elementClass, value, attributes);
                    if (value == null) {
                        return;
                    }
                }
                Converter cn = property.getAnnotation(Converter.class);
                if (cn != null) {
                    Class<? extends com.anosym.vjax.converter.v3.Converter> cnv = cn
                            .value();
                    if (cnv != null) {
                        com.anosym.vjax.converter.v3.Converter<Object, Object> converter = cnv
                                .newInstance();
                        value = converter.convertFrom(value);
                    }
                }
                if (property.isAnnotationPresent(Attribute.class) || property.isAnnotationPresent(Id.class)) {
                    attributes.put(markup, value.toString());
                    if (property.isAnnotationPresent(Id.class)) {
                        //then use references.
                        ID_REFERENCES.add(String.valueOf(value));
                    }
                } else {
                    parent.addChild(marshall0((T) value, markup, property.getDeclaredAnnotations(), parent));
                }
            } catch (Exception e) {
                Throwables.propagateIfPossible(e, VXMLBindingException.class);
                throw new VXMLBindingException(e);
            }
        }
    }

    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    private VElement marshallMethods(T object, VElement parent, Annotation[] annotations) throws VXMLBindingException {
        List<Method> methods = new ArrayList<Method>();
        VObjectMarshaller.getMethods(object.getClass(), methods, true);
        Map<String, String> attributes = new HashMap<String, String>();
        for (Method mProperty : methods) {
            try {
                Class elementClass = mProperty.getReturnType();
                mProperty.setAccessible(true);
                Object value = mProperty.invoke(object, new Object[]{});
                String markup = mProperty.getName();
                String regex = "(^is)|(^get)";
                markup = markup.replaceAll(regex, "");
                markup = markup.substring(0, 1).toLowerCase() + markup.substring(1);
                marshallProperty(parent, annotations, mProperty, markup, elementClass, value, attributes);
            } catch (Exception e) {
                Throwables.propagateIfPossible(e, VXMLBindingException.class);
                throw new VXMLBindingException(e);
            }
        }
        if (attributes.isEmpty()) {
            for (Map.Entry<String, String> e : attributes.entrySet()) {
                parent.addAttribute(new VAttribute(e.getKey(), e.getValue()));
            }
        }
        return parent;
    }

    private VElement marshallMap0(T object, String markupName, Annotation[] annotations) throws VXMLBindingException {
        Map m = (Map) object;
        Class c = object.getClass();
        Set entries = m.entrySet();
        if (markupName == null) {
            markupName = c.getSimpleName();
        }
        VElement mapElem = new VElement(markupName);

        KeyValueEntryMarkup kvMarkup = getAnnotation(annotations, KeyValueEntryMarkup.class);
        String keyMarkup = kvMarkup != null ? kvMarkup.key() : "key";
        String valueMarkup = kvMarkup != null ? kvMarkup.value() : "value";
        String entryMarkup = kvMarkup != null ? kvMarkup.entry() : "entry";

        for (Object o : entries) {
            Map.Entry e = (Map.Entry) o;
            Object key = e.getKey();
            Object value = e.getValue();
            VElement entryElem = new VElement(entryMarkup, mapElem);
            VElement keyElem = marshall0((T) key, keyMarkup, annotations, entryElem);
            VElement valueElem = marshall0((T) value, valueMarkup, annotations, entryElem);
            entryElem.addChild(keyElem, 0);
            entryElem.addChild(valueElem, 1);
        }
        return mapElem;
    }

    private VElement marshallCollection(T object, String markupName, Annotation[] annotations) throws
            VXMLBindingException {
        VElement collectionElem = new VElement(markupName);
        String elementMarkups = markupName + "_value";
        ElementMarkup elementMarkup = null;
        if (annotations != null) {
            elementMarkup = getAnnotation(annotations, ElementMarkup.class);
        }
        if (elementMarkup != null) {
            elementMarkups = elementMarkup.value();
        }
        Collection cols = (Collection) object;
        //do we have a collectionelementconverter
        CollectionElementConverter cec = getAnnotation(annotations, CollectionElementConverter.class);
        com.anosym.vjax.converter.v3.Converter<Object, Object> cnv = null;
        if (cec != null) {
            try {
                cnv = cec.value().newInstance();
            } catch (IllegalAccessException ex) {
                throw new VXMLBindingException(ex);
            } catch (InstantiationException ex) {
                throw new VXMLBindingException(ex);
            }
        }
        for (Object o : cols) {
            if (cnv != null) {
                o = cnv.convertFrom(o);
            }
            collectionElem.addChild(marshall0((T) o, elementMarkups, null, collectionElem));
        }
        return collectionElem;
    }

    @SuppressWarnings("null")
    private VElement marshallArray(
            T object, String markupName, VElement parent, Annotation[] annotations) throws VXMLBindingException {
        ArrayParented ap = getAnnotation(annotations, ArrayParented.class);
        boolean parented = ap != null;
        String arrayMarkup = markupName;
        if (parented) {
            markupName = ap.componentMarkup();
            if (Strings.isNullOrEmpty(markupName)) {
                markupName += "_array";
            }
            parent = new VElement(arrayMarkup, parent);
        }
        int arrLength = Array.getLength(object);
        for (int i = 0; i < arrLength; i++) {
            Object o = Array.get(object, i);
            parent.addChild(put(markupName, marshall0((T) o, markupName, annotations, parent)));
        }
        return parent;
    }

    private void checkDefinedAttributes(VElement elem, Annotation[] annotations) {
        DefinedAttribute definedAttribute = getAnnotation(annotations, DefinedAttribute.class);
        if (definedAttribute != null) {
            elem.addAttribute(new VAttribute(definedAttribute.name(), definedAttribute.value()));
        }
        DefinedAttributes das = getAnnotation(annotations, DefinedAttributes.class);
        if (das != null) {
            for (DefinedAttribute da : das.value()) {
                elem.addAttribute(new VAttribute(da.name(), da.value()));
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private VElement marshall0(T object, String markupName, Annotation[] annotations, VElement parent) throws
            VXMLBindingException {
        Class<? extends Object> c = object.getClass();
        VElement element;
        //if the object has already been marshalled.
        if (CIRCULAR_REFERENCES.containsKey(System.identityHashCode(object))) {
            //we have marshalled this entity, add its circular reference lement only
            CIRCULAR_REFERENCES.get(System.identityHashCode(object)).add(object);
            element = new VElement(markupName);
            element.addAttribute(new VAttribute("references", object.hashCode()));
        } else {
            CIRCULAR_REFERENCES.put(System.identityHashCode(object), new ArrayList<Object>() {

                @Override
                public boolean add(Object e) {
                    //just add one of the references, no need of adding the same object twice.
                    return isEmpty() ? super.add(e) : true;
                }

            });
            String idRef = getIdReference(object);
            if (idRef != null && ID_REFERENCES.contains(idRef)) {
                //add a a ref attribute and return.
                markupName = markupName == null ? c.getSimpleName() : markupName;
                element = putWithAttributes(markupName, VContent.empty(), ImmutableMap.of("ref-id", idRef));
            } else if (isPrimitiveOrPrimitiveWrapper(c)) {
                markupName = markupName == null ? c.getSimpleName() : markupName;
                element = put(markupName, VContent.valueOf(object));
            } else if (c.equals(String.class)) {
                markupName = markupName == null ? c.getSimpleName() : markupName;
                element = put(markupName, VContent.valueOf(escapeEntityReference(object.toString())));
            } else if (c.isEnum()) {
                markupName = markupName == null ? c.getSimpleName() : markupName;
                element = put(markupName, VContent.valueOf(((Enum) object).name()));
            } else if (c.isAssignableFrom(BigDecimal.class) && getAnnotation(annotations, Converter.class) == null) {
                VBigDecimalConverter converter = new VBigDecimalConverter();
                markupName = markupName == null ? c.getSimpleName() : markupName;
                try {
                    element = put(markupName, VContent.valueOf(converter.convert((BigDecimal) object)));
                } catch (VConverterBindingException ex) {
                    throw new VXMLBindingException(ex);
                }
            } else if (c.isArray()) {
                element = marshallArray(object, markupName, parent, annotations);
            } else if (Collection.class.isAssignableFrom(c)) {
                element = marshallCollection(object, markupName, annotations);
            } else if (Map.class.isAssignableFrom(c)) {
                element = marshallMap0(object, markupName, annotations);
            } else {
                //check access options
                VElement elem = new VElement(markupName);
                AccessOption ac = c.getAnnotation(AccessOption.class);
                if (ac == null || ac.value() == AccessOption.AccessType.FIELD) {
                    element = marshallFields(object, elem, annotations);
                } else {
                    element = marshallMethods(object, elem, annotations);
                }
            }
            //now lets see if circular_references were found.
            if (!CIRCULAR_REFERENCES.get(System.identityHashCode(object)).isEmpty()) {
                element.addAttribute(new VAttribute("c-ref", System.identityHashCode(object)));
            }
        }
        checkDefinedAttributes(element, annotations);
        return element;
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
