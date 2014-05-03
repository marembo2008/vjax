/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class ImplementedTest {

  public static interface MyInterface {

    String getString();
  }

  public static class MyClass implements MyInterface {

    private String myString;

    @Override
    public String getString() {
      return myString;
    }

  }

  public static class MyData {

    private String name;
    @Implemented(MyClass.class)
    private MyInterface info;

    public MyData() {
    }

    public MyData(String name, MyInterface info) {
      this.name = name;
      this.info = info;
    }

  }

  @Test
  public void testImplemented() throws VXMLBindingException {
    String xml = "<MyData><name>Kalina</name><info><myString>My daughter</myString></info></MyData>";
    MyData data = new VObjectMarshaller<MyData>(MyData.class).unmarshall(VDocument.parseDocumentFromString(xml));
    String expected = "My daughter";
    String actual = data.info.getString();
    Assert.assertEquals("Testing of implemented interface on a field attribute", expected, actual);
  }
}
