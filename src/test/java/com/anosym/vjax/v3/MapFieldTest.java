/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.v3.GenericMapType;
import com.anosym.vjax.xml.VDocument;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class MapFieldTest {

  public static class MapTest {

    @GenericMapType(key = String.class, value = String.class, entryMarkup = "extra", keyMarkup = "id", valueMarkup = "value")
    private Map<String, String> extras;

    @Override
    public String toString() {
      return extras.toString();
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 31 * hash + (this.extras != null ? this.extras.hashCode() : 0);
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
      final MapTest other = (MapTest) obj;
      if (this.extras != other.extras && (this.extras == null || !this.extras.equals(other.extras))) {
        return false;
      }
      return true;
    }

  }

  @Test
  public void testMapField() {
    try {
      String xml = "<response><extras><extra><id>business_name</id><value>Anosym Corporation</value></extra><extra><id>is_business_admin</id><value>true</value></extra></extras><status><enum-value>SUCCESS</enum-value></status><message>true</message></response>";
      VObjectMarshaller<MapTest> m = new VObjectMarshaller<MapFieldTest.MapTest>(MapTest.class);
      VDocument doc = VDocument.parseDocumentFromString(xml);
      MapTest mt = m.unmarshall(doc);
      MapTest expected = new MapTest();
      expected.extras = new HashMap<String, String>();
      expected.extras.put("business_name", "Anosym Corporation");
      expected.extras.put("is_business_admin", "true");
      Assert.assertEquals(expected, mt);
    } catch (VXMLBindingException ex) {
      Logger.getLogger(MapFieldTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
