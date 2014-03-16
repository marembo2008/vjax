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

}
