/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.xml.VDocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class VAttributeTest {

  public static class Option {

    private int option1;
    private String optionName;

    public Option(int option1, String optionName) {
      this.option1 = option1;
      this.optionName = optionName;
    }

    public Option() {
    }

    public int getOption1() {
      return option1;
    }

    public void setOption1(int option1) {
      this.option1 = option1;
    }

    public String getOptionName() {
      return optionName;
    }

    public void setOptionName(String optionName) {
      this.optionName = optionName;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 89 * hash + this.option1;
      hash = 89 * hash + (this.optionName != null ? this.optionName.hashCode() : 0);
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
      final Option other = (Option) obj;
      if (this.option1 != other.option1) {
        return false;
      }
      if ((this.optionName == null) ? (other.optionName != null) : !this.optionName.equals(other.optionName)) {
        return false;
      }
      return true;
    }

  }

  public static class Instance {

    private String name;
    @Attribute
    private String id;
    private Option option;

    public Instance() {
    }

    public Instance(String name, String id, Option option) {
      this.name = name;
      this.id = id;
      this.option = option;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public Option getOption() {
      return option;
    }

    public void setOption(Option option) {
      this.option = option;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
      hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
      hash = 37 * hash + (this.option != null ? this.option.hashCode() : 0);
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
      final Instance other = (Instance) obj;
      if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
        return false;
      }
      if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
        return false;
      }
      if (this.option != other.option && (this.option == null || !this.option.equals(other.option))) {
        return false;
      }
      return true;
    }

  }

  @Test
  public void testMarshall() {
    try {
      Option p = new Option(888, "option-name");
      Instance ai = new Instance("kalji833773", "344444", p);
      VObjectMarshaller<Instance> m = new VObjectMarshaller<Instance>(Instance.class);
      String expected = "<Instance id=\"344444\"><name>kalji833773</name><option><option1>888</option1><optionName>option-name</optionName></option></Instance>";
      String xml = m.doMarshall(ai);
      assertEquals(expected, xml);
    } catch (Exception ex) {
      Logger.getLogger(VObjectMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Test
  public void testUnmarshall() {
    try {
      Option p = new Option(888, "option-name");
      Instance expected = new Instance("kalji833773", "344444", p);
      VObjectMarshaller<Instance> m = new VObjectMarshaller<Instance>(Instance.class);
      String xml = "<Instance id=\"344444\"><name>kalji833773</name><option><option1>888</option1><optionName>option-name</optionName></option></Instance>";
      Instance actual = m.unmarshall(VDocument.parseDocumentFromString(xml));
      assertEquals(expected, actual);
    } catch (VXMLBindingException ex) {
      Logger.getLogger(VObjectMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
