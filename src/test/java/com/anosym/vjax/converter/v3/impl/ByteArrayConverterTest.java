/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class ByteArrayConverterTest {

  public ByteArrayConverterTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testEncodeDecode() {
    ByteArrayConverter bac = new ByteArrayConverter();
    String expectedValue = "This is a test for simple byte array base64 encode decode";
    String expected = "VGhpcyBpcyBhIHRlc3QgZm9yIHNpbXBsZSBieXRlIGFycmF5IGJhc2U2NCBlbmNvZGUgZGVjb2Rl";
    byte[] data = expectedValue.getBytes();
    String actual = bac.convertFrom(data);
    assertEquals(expected, actual);
    byte[] actualData = bac.convertTo(expected);
    String actualValue = new String(actualData);
    assertEquals(expectedValue, actualValue);
  }

}
