/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.Position;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class AccessOptionTest {

  @Markup(name = "Access")
  @AccessOption(AccessOption.AccessType.FIELD)
  public static class AccessClassTest {

    @Position(index = 0)
    private String name;
    @Position(index = 1)
    private String id;

    public AccessClassTest(String name, String id) {
      this.name = name;
      this.id = id;
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

  }

  @Markup(name = "Access")
  @AccessOption(AccessOption.AccessType.METHOD)
  public static class AccessClassMethodTest {

    private String name;
    private String id;

    public AccessClassMethodTest(String name, String id) {
      this.name = name;
      this.id = id;
    }

    @Position(index = 0)
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Position(index = 1)
    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

  }

  public AccessOptionTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testFieldAccess() {
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
            + "<Access>\n"
            + "   <name>name</name>\n"
            + "   <id>id</id>\n"
            + "</Access>";
    AccessClassTest ac = new AccessClassTest("name", "id");
    VObjectMarshaller<AccessClassTest> vom = new VObjectMarshaller<AccessClassTest>(AccessClassTest.class);
    VDocument doc = vom.marshall(ac);
    System.out.println(doc.toXmlString());
    assertEquals(expected, doc.toXmlString());
  }

  @Test
  public void testMethodAccess() {
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
            + "<Access>\n"
            + "   <name>name</name>\n"
            + "   <id>id</id>\n"
            + "</Access>";
    AccessClassMethodTest ac = new AccessClassMethodTest("name", "id");
    VObjectMarshaller<AccessClassMethodTest> vom = new VObjectMarshaller<AccessClassMethodTest>(AccessClassMethodTest.class);
    VDocument doc = vom.marshall(ac);
    System.out.println(doc.toXmlString());
    assertEquals(expected, doc.toXmlString());
  }

}
