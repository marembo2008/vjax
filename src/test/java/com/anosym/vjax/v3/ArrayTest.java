/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.xml.VDocument;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class ArrayTest {

  public static class MyObject {

    private String name;
    private int[] bytes;

    public MyObject() {
    }

    public MyObject(String name, int[] bytes) {
      this.name = name;
      this.bytes = bytes;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int[] getBytes() {
      return bytes;
    }

    public void setBytes(int[] bytes) {
      this.bytes = bytes;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
      hash = 67 * hash + Arrays.hashCode(this.bytes);
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
      final MyObject other = (MyObject) obj;
      if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
        return false;
      }
      return Arrays.equals(this.bytes, other.bytes);
    }

  }

  @Test
  public void testUnmarshall() {
    try {
      MyObject ob = new MyObject("name", new int[]{8, 9, 9});
      VObjectMarshaller<MyObject> vom = new VObjectMarshaller<ArrayTest.MyObject>(MyObject.class);
      VDocument doc = VDocument.parseDocumentFromString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
              + "<MyObject>\n"
              + "   <name>name</name>\n"
              + "   <bytes>8</bytes>\n"
              + "   <bytes>9</bytes>\n"
              + "   <bytes>9</bytes>\n"
              + "</MyObject>");
      MyObject actual = vom.unmarshall(doc);
      Assert.assertEquals(ob, actual);
    } catch (VXMLBindingException ex) {
      Logger.getLogger(ArrayTest.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
}
