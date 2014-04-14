/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class VJaxUtilsTest {

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  public static @interface FieldAnnot {

  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.FIELD})
  public static @interface FieldNotAnnot {

  }

  public static class AnnotatedClass {

    @FieldAnnot
    private String fieldAnnotated;
  }

  public VJaxUtilsTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testFieldAnnotated() {
    Field annotField = VJaxUtils.getFieldAnnotated(AnnotatedClass.class, FieldAnnot.class);
    assertNotNull("Field must not be null", annotField);
  }

  @Test
  public void testFieldNotAnnotated() {
    Field annotField = VJaxUtils.getFieldAnnotated(AnnotatedClass.class, FieldNotAnnot.class);
    assertNull("Field must be null", annotField);
  }

}
