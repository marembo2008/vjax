package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.CopyAnnotation;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.annotations.v3.GenerateWrapper;
import com.anosym.vjax.util.VJaxUtils;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.atteo.classindex.ClassIndex;

/**
 *
 * @author marembo
 */
public class VObjectWrapper {

    private class FieldInformation {

        String fieldTypeName;
        String fieldName;
        List<Annotation> fieldAnnotations;
        boolean isEqualityField; //if it has the @Id annotation.
        boolean isPrimitive;
        boolean isBoolean;
        boolean isInt; //includes byte, short and int, and char
        boolean isLong;
        boolean isDouble;
        boolean isFloat;

        public FieldInformation() {
        }

        public FieldInformation(String first, String second) {
            this.fieldTypeName = first;
            this.fieldName = second;
        }

        public FieldInformation(String first, String second, List<Annotation> copiedAnnotations) {
            this.fieldTypeName = first;
            this.fieldName = second;
            this.fieldAnnotations = copiedAnnotations;
        }

        public FieldInformation(String first, String second, List<Annotation> copiedAnnotations, boolean equalityField) {
            this.fieldTypeName = first;
            this.fieldName = second;
            this.fieldAnnotations = copiedAnnotations;
            this.isEqualityField = equalityField;
        }

        public FieldInformation(String first, String second, List<Annotation> copiedAnnotations, boolean equalityField, boolean primitive) {
            this.fieldTypeName = first;
            this.fieldName = second;
            this.fieldAnnotations = copiedAnnotations;
            this.isEqualityField = equalityField;
            this.isPrimitive = primitive;
        }

    }
    //folder where to generate the wrappers.
    private final String srcFolder;
    //we keep a list of generated classes, so that we do not regenerate classes twice due to inheritance.
    private final List<Class> generated;

    public VObjectWrapper(String srcFolder) {
        this.srcFolder = srcFolder;
        this.generated = Lists.newArrayList();
    }

    public void process() {
        final Iterable<Class<?>> classForWrappers = ClassIndex.getAnnotated(GenerateWrapper.class);
        for (final Class generateWrapperForClass : classForWrappers) {
            generate(generateWrapperForClass);
        }
    }

    private String getGeneratedPackageName(Class c) {
        String simpleName = c.getSimpleName();
        String fullname = c.getName();
        int ix = fullname.indexOf(simpleName);
        String generatedPackageName = fullname.substring(0, ix) + "wrapper";
        return generatedPackageName;
    }

    private String getGeneratedSimpleName(Class c) {
        String simpleName = c.getSimpleName();
        String simpleWrapperName = "W" + simpleName;
        return simpleWrapperName;
    }

    private String getGeneratedFullName(Class c) {
        String simpleWrapperName = getGeneratedSimpleName(c);
        String generatedPackageName = getGeneratedPackageName(c);
        //if package name is java.lang, ignore
        return generatedPackageName + "." + simpleWrapperName;
    }

    private boolean generated(Class c) {
        return generated.contains(c);
    }

    private void generate(Class c) {
        if (generated(c)) {
            return;
        }
        Class super_ = c.getSuperclass();
        boolean extendSuper = super_.isAnnotationPresent(GenerateWrapper.class);
        if (extendSuper) {
            generate(super_);
        }
        System.out.println("Generating wrapper for: " + c);
        String wrapperPackageName = getGeneratedPackageName(c);
        String wrapperSimpleName = getGeneratedSimpleName(c);
        boolean isAbstract = Modifier.isAbstract(c.getModifiers());
        String wrapperClassComment
                = "/**\n"
                + " *This is a generated artifact for " + c.getName() + "\n"
                + " */";
        String wrapperClassDecl = "public " + (isAbstract ? "abstract" : "") + " class "
                + wrapperSimpleName + (extendSuper ? (" extends " + getGeneratedFullName(super_)) : "");
        if (((GenerateWrapper) c.getAnnotation(GenerateWrapper.class)).serializable()) {
            wrapperClassDecl += " implements java.io.Serializable ";
        }
        List<FieldInformation> fieldDecls = new ArrayList<FieldInformation>();
        //get the declared fields only.
        Field[] declFields = c.getDeclaredFields();
        for (Field f : declFields) {
            if (Modifier.isFinal(f.getModifiers())
                    || Modifier.isStatic(f.getModifiers())
                    || f.isAnnotationPresent(Transient.class)) {
                continue;
            }
            fieldDecls.add(getField(f));
        }
        processSource(wrapperSimpleName, wrapperClassComment, wrapperPackageName, wrapperClassDecl, fieldDecls);
    }

    private boolean isJavaLangPackage(Class c) {
        return c.getName().equals("java.lang." + c.getSimpleName());
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    private FieldInformation getField(Field f) {
        Class type = f.getType();
        String name = f.getName();
        String fieldType;
        List<Annotation> copiedAnnotations = new ArrayList<Annotation>();
        //by default, add @Id annotation to copied annotations if it is present.
        //this is is used in generating uniqueness and also for equality purposes.
        if (f.isAnnotationPresent(Id.class)) {
            copiedAnnotations.add(f.getAnnotation(Id.class));
        }
        if (f.isAnnotationPresent(CopyAnnotation.class)) {
            CopyAnnotation ca = f.getAnnotation(CopyAnnotation.class);
            //remove all excluded annotations.
            for (Class c : ca.value()) {
                for (Annotation a : f.getAnnotations()) {
                    Class aClass = a.annotationType();
                    if (aClass.equals(c) && !aClass.equals(CopyAnnotation.class)) {
                        copiedAnnotations.add(a);
                    }
                }
            }
        }
        if (type.isAnnotationPresent(GenerateWrapper.class)) {
            //use the wrapped type
            fieldType = getGeneratedFullName(type);
        } else if (f.isAnnotationPresent(Converter.class)
                && !copiedAnnotations.contains(f.getAnnotation(Converter.class))) {
            //if it is not copied
            try {
                //get the converter class, and find its convertFrom return type.
                Class convC = f.getAnnotation(Converter.class).value();
                Method convFrom = convC.getMethod("convertFrom", new Class[]{type});
                Class returnType = convFrom.getReturnType();
                if (returnType.isAnnotationPresent(GenerateWrapper.class)) {
                    generate(returnType);
                    fieldType = getGeneratedFullName(returnType);
                } else {
                    fieldType = isJavaLangPackage(returnType) ? returnType.getSimpleName() : returnType.getName();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            //we need to check if it is a converter
            fieldType = isJavaLangPackage(type) ? type.getSimpleName() : type.getName();
            //take care of arrays.
            Class arrType = type;
            String dimens = "";
            while (arrType.isArray()) {
                arrType = arrType.getComponentType();
                if (arrType.isAnnotationPresent(GenerateWrapper.class)) {
                    fieldType = getGeneratedFullName(arrType);
                } else {
                    fieldType = arrType.getName();
                }
                dimens += "[]";
            }
            fieldType += dimens;
        }
        FieldInformation fi = new FieldInformation(fieldType, name, copiedAnnotations, f.isAnnotationPresent(Id.class), f.getType().isPrimitive());
        if (fi.isPrimitive) {
            Class fc = f.getType();
            if (fc == int.class
                    || fc == char.class
                    || fc == byte.class
                    || fc == short.class) {
                fi.isInt = true;
            } else if (fc == boolean.class) {
                fi.isBoolean = true;
            } else if (fc == long.class) {
                fi.isLong = true;
            } else if (fc == float.class) {
                fi.isFloat = true;
            } else if (fc == double.class) {
                fi.isDouble = true;
            }
        }
        return fi;
    }

    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void processSource(String simpleWrapperClassName, String classComment, String packageDecl, String classDecl,
                               List<FieldInformation> declFields) {
        FileOutputStream out = null;
        try {
            File sourceFolder = new File(srcFolder, packageDecl.replaceAll("\\.", File.separator));
            if (!sourceFolder.exists()) {
                //create the entire package hierarchy.
                sourceFolder.mkdirs();
            }
            File sourceFile = new File(sourceFolder, simpleWrapperClassName + ".java");
            out = new FileOutputStream(sourceFile);
            String source = generateSource(simpleWrapperClassName, classComment, packageDecl, classDecl, declFields);
            //get the file name.
            out.write(source.getBytes());
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(VObjectWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @SuppressWarnings("UseSpecificCatch")
    private String generateAnnotation(Annotation annot) {
        try {
            String name = "@" + annot.annotationType().getCanonicalName();
            String params = "";
            //if annotation contains values.
            Method[] declMethods = annot.annotationType().getDeclaredMethods();
            for (Method m : declMethods) {
                //full name unless java,lang
                String vName = m.getName() + " = ";
                //get the value
                m.setAccessible(true);
                Object value = m.invoke(annot, new Object[]{});
                if (value.getClass().isPrimitive()) {
                    vName += value.toString();
                } else if (String.class.isAssignableFrom(value.getClass())) {
                    vName += "\"" + value.toString() + "\"";
                } else if (Class.class.isAssignableFrom(value.getClass())) {
                    vName += ((Class) value).getName() + ".class";
                } else if (value.getClass().isArray()) {
                    Class cmpType = value.getClass().getComponentType();
                    String arr = "";
                    int len = Array.getLength(value);
                    for (int i = 0; i < len; i++) {
                        if (!VJaxUtils.isNullOrEmpty(arr)) {
                            arr += ", ";
                        }
                        Object arrVal = Array.get(value, i);
                        if (cmpType.isPrimitive()) {
                            arr += arrVal.toString();
                        } else if (String.class.isAssignableFrom(cmpType)) {
                            arr += "\"" + arrVal.toString() + "\"";
                        } else if (Class.class.isAssignableFrom(cmpType)) {
                            arr += ((Class) arrVal).getName() + ".class";
                        }
                    }
                    vName += "{" + arr + "}";
                }
                if (!VJaxUtils.isNullOrEmpty(params)) {
                    params += ", ";
                }
                params += vName;
            }
            return name + "(" + params + ")";
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String generateSource(String className, String classComment, String packageDecl, String classDecl, List<FieldInformation> declFields) {
        StringBuilder hashcode = new StringBuilder();
        boolean addHashCodeAndEquals = false;
        hashcode.append("\t@Override\n\tpublic int hashCode() {\n")
                .append("\t\tint hash = ")
                .append(new Random(System.currentTimeMillis()).nextInt(1000))
                .append(";\n");
        StringBuilder equals = new StringBuilder();
        equals.append("\t@Override\n\tpublic boolean equals(Object obj) {\n")
                .append("\t\tif(obj == null) {\n\t\t\treturn false;\n\t\t}\n")
                .append("\t\tif(getClass() != obj.getClass()) {\n\t\t\treturn false;\n\t\t}\n")
                .append("\t\tfinal ")
                .append(className)
                .append(" other = (")
                .append(className)
                .append(")")
                .append("obj;\n");
        StringBuilder sb = new StringBuilder(classComment);
        sb.append("\n")
                .append("package ")
                .append(packageDecl)
                .append(";\n\n")
                .append(classDecl)
                .append("{") //start class body
                .append("\n\n");
        //add fields
        //all fields are private
        for (FieldInformation p : declFields) {
            //annotations first.
            for (Annotation a : p.fieldAnnotations) {
                sb.append("\t")
                        .append(generateAnnotation(a))
                        .append("\n");
            }
            sb.append("\tprivate ")
                    .append(p.fieldTypeName)
                    .append(" ")
                    .append(p.fieldName)
                    .append(";")
                    .append("\n");
            //hashcode and equals.
            if (p.isEqualityField) {
                addHashCodeAndEquals = true;
                hashcode.append("\t\thash = ")
                        .append(new Random(System.currentTimeMillis()).nextInt(100))
                        .append(" * hash + ");
                if (p.isPrimitive) {
                    if (p.isBoolean) {
                        hashcode.append("(this.")
                                .append(p.fieldName)
                                .append("? 1: 0 ); \n");
                        equals.append("\t\tif(this.")
                                .append(p.fieldName)
                                .append(" != other.")
                                .append(p.fieldName)
                                .append(") {\n\t\t\treturn false;\n\t\t}\n");
                    } else if (p.isInt) {
                        hashcode.append("this.")
                                .append(p.fieldName)
                                .append(";\n");
                        equals.append("\t\tif(this.")
                                .append(p.fieldName)
                                .append(" != other.")
                                .append(p.fieldName)
                                .append(") {\n\t\t\treturn false;\n\t\t}\n");
                    } else if (p.isLong) {
                        hashcode.append("(int) (this.")
                                .append(p.fieldName)
                                .append(" ^ (this.")
                                .append(p.fieldName)
                                .append(" >>> 32));\n");
                        equals.append("\t\tif(this.")
                                .append(p.fieldName)
                                .append(" != other.")
                                .append(p.fieldName)
                                .append(") {\n\t\t\treturn false;\n\t\t}\n");
                    } else if (p.isFloat) {
                        hashcode.append("Float.floatToIntBits(this.")
                                .append(p.fieldName)
                                .append(");\n");
                        equals.append("\t\tif (Float.floatToIntBits(this.")
                                .append(p.fieldName)
                                .append(") != Float.floatToIntBits(other.")
                                .append(p.fieldName)
                                .append(")) {\n\t\t\treturn false;\n\t\t}\n");
                    } else if (p.isDouble) {
                        hashcode.append("(int) (Double.doubleToLongBits(this.")
                                .append(p.fieldName)
                                .append(") ^ (Double.doubleToLongBits(this.")
                                .append(p.fieldName)
                                .append(") >>> 32));\n");
                        equals.append("\t\tif (Double.doubleToLongBits(this.")
                                .append(p.fieldName)
                                .append(") != Double.doubleToLongBits(other.")
                                .append(p.fieldName)
                                .append(")) {\n\t\t\treturn false;\n\t\t}\n");
                    }
                } else {
                    hashcode.append("(this.")
                            .append(p.fieldName)
                            .append(" != null ? this.")
                            .append(p.fieldName)
                            .append(".hashCode() : 0);\n");
                    equals.append("\t\tif (this.")
                            .append(p.fieldName)
                            .append(" != other.")
                            .append(p.fieldName)
                            .append(" && (this.")
                            .append(p.fieldName)
                            .append(" == null || !this.")
                            .append(p.fieldName)
                            .append(".equals(other.")
                            .append(p.fieldName)
                            .append("))) {\n\t\t\treturn false;\n\t\t}\n");
                }
            }
        }
        sb.append("\n");
        //add getters and setters
        for (FieldInformation p : declFields) {
            //getter
            sb.append("\tpublic ")
                    .append(p.fieldTypeName)
                    .append(" get")
                    .append(p.fieldName.substring(0, 1).toUpperCase())
                    .append(p.fieldName.substring(1))
                    .append("() {\n")
                    .append("\t\t")
                    .append("return this.")
                    .append(p.fieldName)
                    .append(";\n")
                    .append("\t}\n\n");
            //setters
            sb.append("\tpublic void ")
                    .append(" set")
                    .append(p.fieldName.substring(0, 1).toUpperCase())
                    .append(p.fieldName.substring(1))
                    .append("(")
                    .append(p.fieldTypeName)
                    .append(" ")
                    .append(p.fieldName)
                    .append(")")
                    .append(" {\n")
                    .append("\t\tthis.")
                    .append(p.fieldName)
                    .append(" = ")
                    .append(p.fieldName)
                    .append(";\n")
                    .append("\t}\n\n");
        }
        if (addHashCodeAndEquals) {
            //close hashcode and equals.
            hashcode.append("\t\treturn hash;\n")
                    .append("\t}\n\n");
            equals.append("\t\treturn true;\n")
                    .append("\t}\n\n");
            sb.append(hashcode)
                    .append(equals);
        }
        //close body.
        sb.append("}");
        return sb.toString();
    }
}
