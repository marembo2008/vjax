/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
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
public class MarshallableTest {

  public static class TestClass {

    @Marshallable(Marshallable.Option.MARSHALL)
    private String marshallOnly;
    @Marshallable(Marshallable.Option.UNMARSHALL)
    private String unmarshallOnly;
    @Marshallable(Marshallable.Option.BOTH)
    private String marshallAndUnmarshall;
    @Marshallable(Marshallable.Option.NONE)
    private String noMarshallNorUnmarshall;

    public TestClass() {
    }

    public TestClass(String marshallOnly, String unmarshallOnly, String marshallAndUnmarshall, String noMarshallNorUnmarshall) {
      this.marshallOnly = marshallOnly;
      this.unmarshallOnly = unmarshallOnly;
      this.marshallAndUnmarshall = marshallAndUnmarshall;
      this.noMarshallNorUnmarshall = noMarshallNorUnmarshall;
    }

  }

  public MarshallableTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testMarshallingOption() {
    TestClass tc = new TestClass("marshallOnly", "unmarshallOnly", "marshallAndUnmarshall", "norMarshallNorUnmarshall");
    VDocument doc = new VObjectMarshaller<TestClass>(TestClass.class).marshall(tc);
    System.out.println(doc.toXmlString());
    assertFalse("unmarshallOnly only field must be absent", doc.getRootElement().hasChild("unmarshallOnly"));
    assertFalse("noMarshallNorUnmarshall field must be absent", doc.getRootElement().hasChild("noMarshallNorUnmarshall"));
    assertTrue("marshallOnly only field must be present", doc.getRootElement().hasChild("marshallOnly"));
    assertTrue("marshallAndUnmarshall field must be present", doc.getRootElement().hasChild("marshallAndUnmarshall"));
  }

  @Test
  public void testUnmarshallingOptions() {
    try {
      String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
              + "<TestClass>\n"
              + "   <marshallOnly>marshallOnly</marshallOnly>\n"
              + "   <unmarshallOnly>unmarshallOnly</unmarshallOnly>\n"
              + "   <marshallAndUnmarshall>marshallAndUnmarshall</marshallAndUnmarshall>\n"
              + "   <noMarshallNorUnmarshall>noMarshallNorUnmarshall</noMarshallNorUnmarshall>\n"
              + "</TestClass>";
      TestClass tc = new VObjectMarshaller<TestClass>(TestClass.class).unmarshall(VDocument.parseDocumentFromString(xml));
      assertNotNull("unmarshallOnly must not be null", tc.unmarshallOnly);
      assertNotNull("marshallAndUnmarshall must not be null", tc.marshallAndUnmarshall);
      assertNull("marshallOnly must be null", tc.marshallOnly);
      assertNull("noMarshallNorUnmarshall must be null", tc.noMarshallNorUnmarshall);
    } catch (VXMLBindingException ex) {
      Logger.getLogger(MarshallableTest.class.getName()).log(Level.SEVERE, null, ex);
      assertFalse(true);
    }
  }

  @Test
  public void testUnmarshallAndMarshall() {
  }

  @Test
  public void testNoMarshallingOrUnmarshalling() {
  }

}
