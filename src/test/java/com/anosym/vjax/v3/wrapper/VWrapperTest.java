/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class VWrapperTest {

  public static class Normal {

    private String desc;
    private int id;
    private double value;

    public Normal() {
    }

    public Normal(String desc, int id, double value) {
      this.desc = desc;
      this.id = id;
      this.value = value;
    }

  }

  public static class Wrapped {

    private String desc;
    private int id;

    public Wrapped() {
    }

    public Wrapped(String desc, int id) {
      this.desc = desc;
      this.id = id;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 23 * hash + (this.desc != null ? this.desc.hashCode() : 0);
      hash = 23 * hash + this.id;
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
      final Wrapped other = (Wrapped) obj;
      if ((this.desc == null) ? (other.desc != null) : !this.desc.equals(other.desc)) {
        return false;
      }
      if (this.id != other.id) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Wrapped{" + "desc=" + desc + ", id=" + id + '}';
    }

  }

  public VWrapperTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testWrapped() {
    try {
      Normal n = new Normal("description", 3434, 78.900);
      Wrapped expected = new Wrapped("description", 3434);
      Wrapped actual = VWrapper.wrap(n, Wrapped.class);
      assertEquals(expected, actual);
    } catch (VWrapperException ex) {
      Logger.getLogger(VWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Test
  public void testWrappedCollection() {
    try {
      List<Normal> normals = new ArrayList<Normal>();
      List<Wrapped> expecteds = new ArrayList<Wrapped>();
      for (int i = 0; i < 10; i++) {
        normals.add(new Normal("description-" + i, 3434 + i, 78.900));
        expecteds.add(new Wrapped("description-" + i, 3434 + i));
      }
      List<Wrapped> actuals = VWrapper.wrapCollection(normals, Wrapped.class);
      assertEquals(expecteds, actuals);
    } catch (VWrapperException ex) {
      Logger.getLogger(VWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Test
  public void testWrappedCollectionInMultipleThreads() {
    try {
      final List<Data> data = new ArrayList<Data>();
      fill(data);
      for (final Data d : data) {
        new Thread(new Runnable() {

          @Override
          public void run() {
            try {
              List actuals = VWrapper.wrapCollection(d.normalList, d.wrapperClass);
              assertEquals(d.wrappedExpectedList, actuals);
              d.passed = true;
            } catch (Exception ex) {
              Logger.getLogger(VWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            d.complete = true;
          }
        }).start();
      }
      while (!complete(data)) {
        synchronized (this) {
          this.wait(100);
        }
      }
      assertTrue(passed(data));
    } catch (InterruptedException ex) {
      Logger.getLogger(VWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static boolean complete(List<Data> l) {
    for (Data d : l) {
      if (!d.complete) {
        return false;
      }
    }
    return true;
  }

  private static boolean passed(List<Data> l) {
    for (Data d : l) {
      if (!d.passed) {
        return false;
      }
    }
    return true;
  }

  private static class Data {

    Class wrapperClass;
    List normalList;
    List wrappedExpectedList;
    boolean passed;
    boolean complete;

    public Data(Class wrapperClass, List normalList, List wrappedExpectedList) {
      this.wrapperClass = wrapperClass;
      this.normalList = normalList;
      this.wrappedExpectedList = wrappedExpectedList;
    }

  }

  private static void fill(List<Data> l) {
    List<Normal> n = new ArrayList<Normal>();
    List<Normal1> n1 = new ArrayList<Normal1>();
    List<Normal2> n2 = new ArrayList<Normal2>();
    List<Normal3> n3 = new ArrayList<Normal3>();

    List<Wrapped> w = new ArrayList<Wrapped>();
    List<Wrapped1> w1 = new ArrayList<Wrapped1>();
    List<Wrapped2> w2 = new ArrayList<Wrapped2>();
    List<Wrapped3> w3 = new ArrayList<Wrapped3>();
    for (int i = 0; i < 20; i++) {
      n.add(new Normal("desc" + i, i, i));
      w.add(new Wrapped("desc" + i, i));
      n1.add(new Normal1());
      w1.add(new Wrapped1());

      n2.add(new Normal2());
      w2.add(new Wrapped2());

      n3.add(new Normal3());
      w3.add(new Wrapped3());
    }
    l.add(new Data(Wrapped.class, n, w));
    l.add(new Data(Wrapped1.class, n1, w1));

    l.add(new Data(Wrapped2.class, n2, w2));
    l.add(new Data(Wrapped3.class, n3, w3));
  }

  public static class Normal1 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 59 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 59 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 59 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Normal1 other = (Normal1) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

  }

  public static class Normal2 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 59 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 59 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 59 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Normal2 other = (Normal2) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

  }

  public static class Normal3 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 59 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 59 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 59 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Normal3 other = (Normal3) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

  }

  public static class Wrapped1 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 29 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 29 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 29 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Wrapped1 other = (Wrapped1) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Wrapped1{" + "iii0=" + iii0 + ", iii1=" + iii1 + ", iii2=" + iii2 + '}';
    }
  }

  public static class Wrapped2 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 29 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 29 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 29 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Wrapped2 other = (Wrapped2) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Wrapped2{" + "iii0=" + iii0 + ", iii1=" + iii1 + ", iii2=" + iii2 + '}';
    }
  }

  public static class Wrapped3 {

    private long iii0 = 8384378343433l;
    private long iii1 = 8384378343433l;
    private long iii2 = 8384378343433l;

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 29 * hash + (int) (this.iii0 ^ (this.iii0 >>> 32));
      hash = 29 * hash + (int) (this.iii1 ^ (this.iii1 >>> 32));
      hash = 29 * hash + (int) (this.iii2 ^ (this.iii2 >>> 32));
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
      final Wrapped3 other = (Wrapped3) obj;
      if (this.iii0 != other.iii0) {
        return false;
      }
      if (this.iii1 != other.iii1) {
        return false;
      }
      if (this.iii2 != other.iii2) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Wrapped3{" + "iii0=" + iii0 + ", iii1=" + iii1 + ", iii2=" + iii2 + '}';
    }

  }
}
