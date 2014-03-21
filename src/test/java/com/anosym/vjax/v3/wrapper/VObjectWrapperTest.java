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
public class VObjectWrapperTest {

  public VObjectWrapperTest() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testGeneration() {
    VObjectWrapper wrapper = new VObjectWrapper("src/wrapper-test/main");
    wrapper.init();
    wrapper.process();
  }

}
