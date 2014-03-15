/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.xml.VDocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class IDReferenceTest {

  public static class IDType {

    @Id
    private String id;
    private String data;
    private IncludeIDType includeIDType;

    public IDType() {
    }

    public IDType(String id, String data) {
      this.id = id;
      this.data = data;
    }

    public void setIncludeIDType(IncludeIDType includeIDType) {
      this.includeIDType = includeIDType;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
      hash = 97 * hash + (this.data != null ? this.data.hashCode() : 0);
      hash = 97 * hash + (this.includeIDType != null ? this.includeIDType.hashCode() : 0);
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
      final IDType other = (IDType) obj;
      if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
        return false;
      }
      if ((this.data == null) ? (other.data != null) : !this.data.equals(other.data)) {
        return false;
      }
      if (this.includeIDType != other.includeIDType && (this.includeIDType == null || !this.includeIDType.equals(other.includeIDType))) {
        return false;
      }
      return true;
    }

  }

  public static class IncludeIDType {

    private int mydata;
    private IDType type;

    public IncludeIDType() {
    }

    public IncludeIDType(int mydata, IDType type) {
      this.mydata = mydata;
      this.type = type;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 41 * hash + this.mydata;
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
      final IncludeIDType other = (IncludeIDType) obj;
      if (this.mydata != other.mydata) {
        return false;
      }
      return true;
    }

  }

  @Test
  public void testIDType() {
    IDType type = new IDType("77777777", "This is the main ud type");
    IncludeIDType dType = new IncludeIDType(83777, type);
    type.setIncludeIDType(dType);
    VObjectMarshaller<IDType> vom = new VObjectMarshaller<IDReferenceTest.IDType>(IDType.class);
    String actual = vom.doMarshall(type);
    String expected = "<IDType id=\"77777777\"><data>This is the main ud type</data><includeIDType><mydata>83777</mydata><type ref-id=\"77777777\"/></includeIDType></IDType>";
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testUnmarshallIDType() {
    try {
      String data = "<IDType id=\"77777777\"><data>This is the main ud type</data><includeIDType><mydata>83777</mydata><type ref-id=\"77777777\"/></includeIDType></IDType>";
      VDocument doc = VDocument.parseDocumentFromString(data);
      VObjectMarshaller<IDType> vom = new VObjectMarshaller<IDReferenceTest.IDType>(IDType.class);
      IDType id = vom.unmarshall(doc);
      Assert.assertEquals(id.hashCode(), id.includeIDType.type.hashCode());
    } catch (VXMLBindingException ex) {
      Logger.getLogger(IDReferenceTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
