/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.arrays;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.util.Arrays;
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
public class ArrayComponentInitializerTest {

  public static interface Interface {
  }

  public static class Impl1 implements Interface {

    private String message;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 53 * hash + (this.message != null ? this.message.hashCode() : 0);
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
      final Impl1 other = (Impl1) obj;
      if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Impl1{" + "message=" + message + '}';
    }
  }

  public static class Impl2 implements Interface {

    private int order;

    public int getOrder() {
      return order;
    }

    public void setOrder(int order) {
      this.order = order;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 19 * hash + this.order;
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
      final Impl2 other = (Impl2) obj;
      if (this.order != other.order) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "Impl2{" + "order=" + order + '}';
    }
  }

  public static class InterfaceArrayComponentInitializer implements ArrayComponentInitializer<Interface> {

    @Override
    public Class<? extends Interface> define(VElement componentElement) {
      if (componentElement.hasChild("message")) {
        return Impl1.class;
      } else {
        return Impl2.class;
      }
    }
  }

  public static class ArrayComponentInitializerImplTest {

    @com.anosym.vjax.annotations.v3.ArrayComponentInitializer(InterfaceArrayComponentInitializer.class)
    @Markup(name = "arrays")
    @ArrayParented(componentMarkup = "interface")
    private Interface[] array;

    public ArrayComponentInitializerImplTest() {
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 73 * hash + Arrays.deepHashCode(this.array);
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
      final ArrayComponentInitializerImplTest other = (ArrayComponentInitializerImplTest) obj;
      return Arrays.deepEquals(this.array, other.array);
    }

  }

  public ArrayComponentInitializerTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testArrayInitializationMarshalling() {
    Impl1 _1 = new Impl1();
    _1.setMessage("Implementation one");
    Impl2 _2 = new Impl2();
    _2.setOrder(9393993);
    Interface[] arr = {_1, _2};
    ArrayComponentInitializerImplTest acit = new ArrayComponentInitializerImplTest();
    acit.array = arr;
    VObjectMarshaller<ArrayComponentInitializerImplTest> m = new VObjectMarshaller<ArrayComponentInitializerImplTest>(acit.getClass());
    String actual = m.doMarshall(acit);
    String expected = "<ArrayComponentInitializerImplTest><arrays><interface><message>Implementation one</message></interface><interface><order>9393993</order></interface></arrays></ArrayComponentInitializerImplTest>";
    assertEquals(expected, actual);
  }

  @Test
  public void testArrayInitializationUnmarshalling() {
    try {
      Impl1 _1 = new Impl1();
      _1.setMessage("Implementation one");
      Impl2 _2 = new Impl2();
      _2.setOrder(9393993);
      Interface[] arr = {_1, _2};
      ArrayComponentInitializerImplTest expected = new ArrayComponentInitializerImplTest();
      expected.array = arr;
      VObjectMarshaller<ArrayComponentInitializerImplTest> m = new VObjectMarshaller<ArrayComponentInitializerImplTest>(
              ArrayComponentInitializerImplTest.class);
      String xml = "<ArrayComponentInitializerImplTest><arrays><interface><message>Implementation one</message></interface><interface><order>9393993</order></interface></arrays></ArrayComponentInitializerImplTest>";
      ArrayComponentInitializerImplTest actual = m.unmarshall(VDocument.parseDocumentFromString(xml));
      assertEquals(expected, actual);
      Impl2 _22 = (Impl2) actual.array[1];
      assertEquals(_2.order, _22.order);
    } catch (VXMLMemberNotFoundException ex) {
      Logger.getLogger(ArrayComponentInitializerTest.class.getName()).log(Level.SEVERE, null, ex);
    } catch (VXMLBindingException ex) {
      Logger.getLogger(ArrayComponentInitializerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
