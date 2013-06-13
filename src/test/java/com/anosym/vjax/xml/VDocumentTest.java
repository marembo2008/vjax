/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class VDocumentTest {

  public VDocumentTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getDocumentName method, of class VDocument.
   */
  @Test
  public void testGetDocumentName() {
    System.out.println("getDocumentName");
    VDocument instance = new VDocument();
    File expResult = null;
    File result = instance.getDocumentName();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDocumentName method, of class VDocument.
   */
  @Test
  public void testSetDocumentName() {
    System.out.println("setDocumentName");
    File documentName = null;
    VDocument instance = new VDocument();
    instance.setDocumentName(documentName);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getName method, of class VDocument.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    VDocument instance = new VDocument();
    String expResult = "";
    String result = instance.getName();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getRootElement method, of class VDocument.
   */
  @Test
  public void testGetRootElement() {
    System.out.println("getRootElement");
    VDocument instance = new VDocument();
    VElement expResult = null;
    VElement result = instance.getRootElement();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setRootElement method, of class VDocument.
   */
  @Test
  public void testSetRootElement() {
    System.out.println("setRootElement");
    VElement rootElement = null;
    VDocument instance = new VDocument();
    instance.setRootElement(rootElement);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of toString method, of class VDocument.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    VDocument instance = new VDocument();
    String expResult = "";
    String result = instance.toString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of toXmlString method, of class VDocument.
   */
  @Test
  public void testToXmlString() {
    System.out.println("toXmlString");
    VDocument instance = new VDocument();
    String expResult = "";
    String result = instance.toXmlString();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of parse method, of class VDocument.
   */
  @Test
  public void testParse() {
    System.out.println("parse");
    VDocument instance = new VDocument();
    instance.parse();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getNotationDeclaration method, of class VDocument.
   */
  @Test
  public void testGetNotationDeclaration() {
    System.out.println("getNotationDeclaration");
    VDocument instance = new VDocument();
    VElement expResult = null;
    VElement result = instance.getNotationDeclaration();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of removeElement method, of class VDocument.
   */
  @Test
  public void testRemoveElement() {
    System.out.println("removeElement");
    VElement elem = null;
    VDocument instance = new VDocument();
    instance.removeElement(elem);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of parseDocument method, of class VDocument.
   */
  @Test
  public void testParseDocument_InputStream() {
    System.out.println("parseDocument");
    InputStream inn = null;
    VDocument expResult = null;
    VDocument result = VDocument.parseDocument(inn);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of parseDocument method, of class VDocument.
   */
  @Test
  public void testParseDocument_String() {
    System.out.println("parseDocument");
    String filePath = "";
    VDocument expResult = null;
    VDocument result = VDocument.parseDocument(filePath);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of parseDocumentFromString method, of class VDocument.
   */
  @Test
  public void testParseDocumentFromString() {
    System.out.println("parseDocumentFromString");
    String xml = "";
    VDocument expResult = null;
    VDocument result = VDocument.parseDocumentFromString(xml);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of writeDocument method, of class VDocument.
   */
  @Test
  public void testWriteDocument() {
    System.out.println("writeDocument");
    VDocument instance = new VDocument();
    instance.writeDocument();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addInclude method, of class VDocument.
   */
  @Test
  public void testAddInclude() {
    System.out.println("addInclude");
    VDocument doc = null;
    VDocument instance = new VDocument();
    instance.addInclude(doc);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setIncludes method, of class VDocument.
   */
  @Test
  public void testSetIncludes() {
    System.out.println("setIncludes");
    List<VDocument> includes = null;
    VDocument instance = new VDocument();
    instance.setIncludes(includes);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getIncludes method, of class VDocument.
   */
  @Test
  public void testGetIncludes() {
    System.out.println("getIncludes");
    VDocument instance = new VDocument();
    List expResult = null;
    List result = instance.getIncludes();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
