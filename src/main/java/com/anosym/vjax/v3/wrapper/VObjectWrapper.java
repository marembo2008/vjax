/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.v3.Converter;
import com.anosym.vjax.annotations.v3.CopyAnnotation;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.annotations.v3.Wrapped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 *
 * @author marembo
 */
public class VObjectWrapper {

  private class FieldInfo {

    private String name;
    private Class clazz;
    private Annotation[] annotations;

    public FieldInfo() {
    }

    public FieldInfo(String name, Class clazz, Annotation[] annotations) {
      this.name = name;
      this.clazz = clazz;
      this.annotations = annotations;
    }

  }

  private class Pair {

    String first;
    String second;
    List<Annotation> copiedAnnotations;

    public Pair() {
    }

    public Pair(String first, String second) {
      this.first = first;
      this.second = second;
    }

    public Pair(String first, String second, List<Annotation> copiedAnnotations) {
      this.first = first;
      this.second = second;
      this.copiedAnnotations = copiedAnnotations;
    }

  }
  private Reflections reflections;
  //folder where to generate the wrappers.
  private String srcFolder;
  //we keep a list of generated classes, so that we do not regenerate classes twice due to inheritance.
  private List<Class> generated;

  public VObjectWrapper(String srcFolder) {
    this.srcFolder = srcFolder;
  }

  public void init() {
    reflections = new Reflections(new ConfigurationBuilder()
            .filterInputsBy(new FilterBuilder()
                    .include(FilterBuilder.prefix("org"))
                    .include(FilterBuilder.prefix("com")))
            .setUrls(ClasspathHelper.forPackage("com"))
            .setScanners(new SubTypesScanner(),
                    new TypeAnnotationsScanner(),
                    new ResourcesScanner()));
    generated = new ArrayList<Class>();
  }

  public void process() {
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Wrapped.class);
    for (Class c : annotated) {
      generate(c);
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
    boolean extendSuper = super_.isAnnotationPresent(Wrapped.class);
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
    List<Pair> fieldDecls = new ArrayList<Pair>();
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
  private Pair getField(Field f) {
    Class type = f.getType();
    String name = f.getName();
    String fieldType;
    List<Annotation> copiedAnnotations = new ArrayList<Annotation>();
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
    if (type.isAnnotationPresent(Wrapped.class)) {
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
        if (returnType.isAnnotationPresent(Wrapped.class)) {
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
        if (arrType.isAnnotationPresent(Wrapped.class)) {
          fieldType = getGeneratedFullName(arrType);
        } else {
          fieldType = arrType.getName();
        }
        dimens += "[]";
      }
      fieldType += dimens;
    }
    return new Pair(fieldType, name, copiedAnnotations);
  }

  @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
  private void processSource(String simpleWrapperClassName, String classComment, String packageDecl, String classDecl, List<Pair> declFields) {
    FileOutputStream out = null;
    try {
      File sourceFolder = new File(srcFolder, packageDecl.replaceAll("\\.", File.separator));
      if (!sourceFolder.exists()) {
        //create the entire package hierarchy.
        sourceFolder.mkdirs();
      }
      File sourceFile = new File(sourceFolder, simpleWrapperClassName + ".java");
      out = new FileOutputStream(sourceFile);
      String source = generateSource(classComment, packageDecl, classDecl, declFields);
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
            if (!arr.isEmpty()) {
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
        if (!params.isEmpty()) {
          params += ", ";
        }
        params += vName;
      }
      return name + "(" + params + ")";
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private String generateSource(String classComment, String packageDecl, String classDecl, List<Pair> declFields) {
    StringBuilder sb = new StringBuilder(classComment);
    sb.append("\n");
    sb.append("package ");
    sb.append(packageDecl);
    sb.append(";\n\n");
    sb.append(classDecl);
    sb.append("{"); //start class body
    sb.append("\n\n");
    //add fields
    //all fields are private
    for (Pair p : declFields) {
      //annotations first.
      for (Annotation a : p.copiedAnnotations) {
        sb.append("\t");
        sb.append(generateAnnotation(a));
        sb.append("\n");
      }
      sb.append("\tprivate ");
      sb.append(p.first);
      sb.append(" ");
      sb.append(p.second);
      sb.append(";");
      sb.append("\n");
    }
    sb.append("\n");
    //add getters and setters
    for (Pair p : declFields) {
      //getter
      sb.append("\tpublic ");
      sb.append(p.first);
      sb.append(" get");
      sb.append(p.second.substring(0, 1).toUpperCase());
      sb.append(p.second.substring(1));
      sb.append("() {\n");
      sb.append("\t\t");
      sb.append("return this.");
      sb.append(p.second);
      sb.append(";\n");
      sb.append("\t}\n\n");
      //setters
      sb.append("\tpublic void ");
      sb.append(" set");
      sb.append(p.second.substring(0, 1).toUpperCase());
      sb.append(p.second.substring(1));
      sb.append("(");
      sb.append(p.first);
      sb.append(" ");
      sb.append(p.second);
      sb.append(")");
      sb.append(" {\n");
      sb.append("\t\tthis.");
      sb.append(p.second);
      sb.append(" = ");
      sb.append(p.second);
      sb.append(";\n");
      sb.append("\t}\n\n");
    }
    //close body.
    sb.append("}");
    return sb.toString();
  }
}
