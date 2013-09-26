/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.test;

import com.anosym.vjax.annotations.NoNamespace;
import com.anosym.vjax.annotations.v3.GenericCollectionType;
import com.anosym.vjax.converter.v3.Converter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Marembo
 */
public class VTester {

  public static class MainClass {

    private String str0;
    private String str1;

    public MainClass(String str0, String str1) {
      this.str0 = str0;
      this.str1 = str1;
    }

    public MainClass() {
    }

    public String getStr0() {
      return str0;
    }

    public void setStr0(String str0) {
      this.str0 = str0;
    }

    public String getStr1() {
      return str1;
    }

    public void setStr1(String str1) {
      this.str1 = str1;
    }

    @Override
    public String toString() {
      return "MainClass{" + "str0=" + str0 + ", str1=" + str1 + '}';
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 73 * hash + (this.str0 != null ? this.str0.hashCode() : 0);
      hash = 73 * hash + (this.str1 != null ? this.str1.hashCode() : 0);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final MainClass other = (MainClass) obj;
      if ((this.str0 == null) ? (other.str0 != null) : !this.str0.equals(other.str0)) {
        return false;
      }
      if ((this.str1 == null) ? (other.str1 != null) : !this.str1.equals(other.str1)) {
        return false;
      }
      return true;
    }
  }

  public static class BasicClass {

    private Long id0;
    private Long id1;

    public BasicClass(Long id0, Long id1) {
      this.id0 = id0;
      this.id1 = id1;
    }

    public BasicClass() {
    }

    public Long getId0() {
      return id0;
    }

    public void setId0(Long id0) {
      this.id0 = id0;
    }

    public Long getId1() {
      return id1;
    }

    public void setId1(Long id1) {
      this.id1 = id1;
    }
  }

  public static class MainToBasicConverter implements Converter<MainClass, BasicClass> {

    public static Map<Long, MainClass> mains = new HashMap<Long, MainClass>();

    private Long toLong(String s) {
      byte[] bb = s.getBytes();
      Long l = 0l;
      for (byte b : bb) {
        l += b;
      }
      return l;
    }

    @Override
    public BasicClass convertFrom(MainClass value) {
      BasicClass bc = new BasicClass(toLong(value.str0), toLong(value.str1));
      mains.put(bc.id0 + bc.id1, value);
      return bc;
    }

    @Override
    public MainClass convertTo(BasicClass value) {
      return mains.get(value.id0 + value.id1);
    }
  }

  @NoNamespace
  public interface Interface0 {

    String method();
  }

  public interface Interface1 extends Interface0 {

    @Override
    String method();
  }

  public static class CollectionMember {

    @com.anosym.vjax.annotations.v3.CollectionElementConverter(MainToBasicConverter.class)
    @GenericCollectionType(MainClass.class)
    private Set<MainClass> mains;

    public CollectionMember() {
      mains = new HashSet<MainClass>();
    }

    public void setMains(Set<MainClass> mains) {
      this.mains = mains;
    }

    public Set<MainClass> getMains() {
      return mains;
    }

    @Override
    public String toString() {
      return "CollectionMember{" + "mains=" + mains + '}';
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + (this.mains != null ? this.mains.hashCode() : 0);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final CollectionMember other = (CollectionMember) obj;
      if (this.mains != other.mains && (this.mains == null || !this.mains.equals(other.mains))) {
        return false;
      }
      return true;
    }
  }

  private static <T extends Annotation> T getInterfaceAnnotation(Class<?>[] cls, Class<T> annotationClass) {
    T t = null;
    for (Class<?> cl : cls) {
      t = cl.getAnnotation(annotationClass);
      if (t != null) {
        return t;
      } else {
        t = getInterfaceAnnotation(cl.getInterfaces(), annotationClass);
        if (t != null) {
          return t;
        }
      }
    }
    return t;
  }

  public static interface Interface2 extends Interface1 {
  }

  public static class Class0 implements Interface2 {

    @Override
    public String method() {
      throw new UnsupportedOperationException("Not supported yet.");
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

  public static void main(String[] args) throws Exception {
    NoNamespace nn = getInterfaceAnnotation(Class0.class.getInterfaces(), NoNamespace.class);
    System.out.println(nn);
  }
}
