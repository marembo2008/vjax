/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class marshalls the constrcutor definitions of a class.
 * The schema derived can be referenced while recreating a new object
 * @author Marembo
 */
public class VClassMarshaller {

    /**
     * References the class-element pair id for which the marshalling of the constructor has been done
     */
    private Map<Integer, Class> constructorReferences;
    private Map<Class, VDocument> marshalledDocuments;

    public VClassMarshaller() {
        this.constructorReferences = new HashMap<Integer, Class>();
        this.marshalledDocuments = new HashMap<Class, VDocument>();
    }

    public VDocument marshallClass(Class clazz) {
        try {
            return doMarshallClass(clazz);
        } finally {
            constructorReferences.clear();
            marshalledDocuments.clear();
        }
    }

    private VDocument doMarshallClass(Class clazz) {
        VElement elem = new VElement(clazz.getSimpleName());
        String name = clazz.getName();
        name = name.replace('.', File.separatorChar) + ".xml";
        VDocument doc = new VDocument(name);
        doc.setRootElement(elem);
        doMarshallConstructor(elem, clazz);
        //clear the constructor references at this point coz we do not want other marshalled ones to  refer to them since
        //they are in different documents
        constructorReferences.clear();
        doMarshallDeclaredFields(elem, clazz);
        doMarshallDeclaredMethods(elem, clazz);
        //at this point add the document ot the map so that it can be referenced
        marshalledDocuments.put(clazz, doc);
        //at this point we need to find all the implemented interfaces, and extended class hierachy
        Class[] interfaces = clazz.getInterfaces();
        for (Class intf : interfaces) {
            VElement ielem = new VElement("implements");
            elem.addChildAsFirst(ielem);
            ielem.addAttribute(new VAttribute(VMarshallerConstants.INTERFACE_ATTRIBUTE, intf.getName()));
            VDocument _doc = marshalledDocuments.get(intf);
            VDocument iDoc = (_doc == null) ? doMarshallClass(intf) : _doc;
            doc.addInclude(iDoc);
        }
        //we recursively include the documents xml from the superclasses
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            VElement sElem = new VElement("extends");
            elem.addChildAsFirst(sElem);
            sElem.addAttribute(new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, superClass.getName()));
            VDocument _doc = marshalledDocuments.get(superClass);
            VDocument superDoc = (_doc != null) ? _doc : marshallClass(superClass);
            doc.addInclude(superDoc);
        }
        return doc;
    }

    private void doMarshallDeclaredMethods(VElement elem, Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            VElement methodElem = new VElement("methods", elem);
            for (Method m : methods) {
                VElement felem = new VElement(m.getName(), methodElem);
                //get the type
                felem.addAttribute(new VAttribute(VMarshallerConstants.RETURN_TYPE_ATTRIBUTE, m.getReturnType().getName()));
                //get paramerts
                //type parameters
                Class[] cls = m.getParameterTypes();
                if (cls.length > 0) {
                    VElement param = new VElement("parameters", felem);
                    for (Class c : cls) {
                        VElement p = new VElement("parameter", param);
                        p.addAttribute(new VAttribute(VMarshallerConstants.PARAMETER_TYPE_ATTRIBUTE, c.getName()));
                    }
                }
                //does it declare to throw exceptions
                Class excepts[] = m.getExceptionTypes();
                if (excepts != null && excepts.length > 0) {
                    VElement elExcepts = new VElement("throws", felem);
                    for (Class ex : excepts) {
                        VElement exElem = new VElement("throwable", elExcepts);
                        exElem.addAttribute(new VAttribute(VMarshallerConstants.THROWS_ATTRIBUTE, ex.getName()));
                    }
                }
                //handle modifiers
                handleModifiers(felem, m.getModifiers());
                //check if we are overriden
                Method mm = getOverridenMethod(m, clazz);
                if (mm != null) {
                    felem.addAttribute(new VAttribute(VMarshallerConstants.OVERRIDES_ATTRIBUTE, "true"));
                }
            }
        }
    }

    private void doMarshallDeclaredFields(VElement elem, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            VElement fieldElem = new VElement("fields", elem);
            for (Field f : fields) {
                VElement felem = new VElement(f.getName(), fieldElem);
                //get the type
                felem.addAttribute(new VAttribute(VMarshallerConstants.TYPE_ATTRIBUTE, f.getType().getName()));
                //handle modifiers
                handleModifiers(felem, f.getModifiers());
            }
        }
    }

    /**
     * Currently does attempts to derive a constructor definition schema from a class
     * @param elem
     * @param clazz
     * @param exc
     */
    private void doMarshallConstructor(VElement elem, Class clazz) {
        elem.addAttribute(new VAttribute(VMarshallerConstants.CLASS_ATTRIBUTE, clazz.getName()));
        if (clazz.isPrimitive()) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.PRIMITIVE_ATTRIBUTE, true + ""));
            return; //nothing to do much about primitive types,no references to check or reference ids
        }
        //add the id of class
        int id = System.identityHashCode(clazz);
        //does the class already reference another marshalled instance
        Class cl = constructorReferences.get(id);
        if (cl != null) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.OBJECT_CIRCULAR_REFERENCE_ATTRIBUTE, id + ""));
            return;
        } else {
            elem.addAttribute(new VAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE, id + ""));
            constructorReferences.put(id, clazz);
        }
        //if this class is in interface, then we do not have a constructor
        if (Modifier.isInterface(clazz.getModifiers())) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.INTERFACE_ATTRIBUTE, "true"));
            return;
        }
        //is the class abstract? this is important for us not to instatiate the and abstract class class
        boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
        if (isAbstract) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.ABSTRACT_ATTRIBUTE, "true"));
        }
        if (clazz.isArray()) {
            Class cp = clazz.getComponentType();
            elem.setMarkup(cp.getSimpleName());
            elem.addAttribute(new VAttribute(VMarshallerConstants.ARRAY_ATTRIBUTE, true + ""));
            elem.addAttribute(new VAttribute(VMarshallerConstants.ARRAY_COMPONENT_CLASS_ATTRIBUTE, cp.getName()));
            VElement cmp = new VElement("array-component", elem);
            doMarshallConstructor(cmp, cp);
        } else {
            Constructor[] cons = clazz.getDeclaredConstructors();
            if (cons.length > 0) {
                VElement cElem = new VElement("constructors", elem);
                for (Constructor con : cons) {
                    VElement cEl = new VElement("constructor", cElem);
                    //is the constructor private, protected or public
                    int modfier = con.getModifiers();
                    handleModifiers(cEl, modfier);
                    //type parameters
                    Class[] cls = con.getParameterTypes();
                    if (cls.length > 0) {
                        VElement param = new VElement("parameters", cEl);
                        for (Class c : cls) {
                            VElement p = new VElement("parameter", param);
                            doMarshallConstructor(p, c);
                        }
                    }
                    //does it declare to throw exceptions
                    Class excepts[] = con.getExceptionTypes();
                    if (excepts != null && excepts.length > 0) {
                        VElement elExcepts = new VElement("throws", cEl);
                        for (Class ex : excepts) {
                            VElement exElem = new VElement("throwable", elExcepts);
                            doMarshallConstructor(exElem, ex);
                        }
                    }
                }
            }
        }
    }

    private void handleModifiers(VElement elem, int mod) {
        //get visibility
        if (Modifier.isPrivate(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.VISIBILITY_ATTRIBUTE, VModifier.PRIVATE.toString()));
        } else if (Modifier.isPublic(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.VISIBILITY_ATTRIBUTE, VModifier.PUBLIC.toString()));
        } else if (Modifier.isProtected(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.VISIBILITY_ATTRIBUTE, VModifier.PROTECTED.toString()));
        }
        //get other types of modifiers
        if (Modifier.isFinal(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.FINAL_ATTRIBUTE, "true"));
        }
        //is it transient
        if (Modifier.isTransient(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.TRANSIENT_ATTRIBUTE, "true"));
        }
        //is it volatile
        if (Modifier.isVolatile(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.VOLATILE_ATTRIBUTE, "true"));
        }
        //is it static
        if (Modifier.isStatic(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.STATIC_ATTRIBUTE, "true"));
        }
        //is it abstract
        if (Modifier.isAbstract(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.ABSTRACT_ATTRIBUTE, "true"));
        }
        //is it interface
        if (Modifier.isInterface(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.INTERFACE_ATTRIBUTE, "true"));
        }
        //is it native
        if (Modifier.isNative(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.NATIVE_ATTRIBUTE, "true"));
        }
        //is it strict
        if (Modifier.isStrict(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.STRICT_ATTRIBUTE, "true"));
        }
        //is it synchronized
        if (Modifier.isSynchronized(mod)) {
            elem.addAttribute(new VAttribute(VMarshallerConstants.SYNCHRONIZED_ATTRIBUTE, "true"));
        }
    }

    private static Method getOverridenMethod(Method m, Class clazz) {
        if (clazz == null || clazz.getSuperclass() == null) {
            return null;
        }
        Class clz = m.getDeclaringClass();
        if (clz.equals(clazz)) {
            //then get the superclass and determine if it declares the method
            Class sClazz = clz.getSuperclass();
            try {
                m = sClazz.getMethod(m.getName(), m.getParameterTypes());
                return m;
            } catch (Exception e) {
                return getOverridenMethod(m, sClazz.getSuperclass());
            }
        } else {
            try {
                m = clazz.getMethod(m.getName(), m.getParameterTypes());
                return m;
            } catch (NoSuchMethodException ex) {
                return getOverridenMethod(m, clazz.getSuperclass());
            } catch (SecurityException ex) {
                Logger.getLogger(VClassMarshaller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}