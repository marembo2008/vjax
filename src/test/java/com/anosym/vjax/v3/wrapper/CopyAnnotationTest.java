/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class CopyAnnotationTest {

  @Before
  public void before() {
  }

  @After
  public void after() {
  }

  @Test
  public void testCopyConverter() {
    VObjectWrapper vow = new VObjectWrapper("src/test/java");
    vow.init();
    vow.process();
  }
}
