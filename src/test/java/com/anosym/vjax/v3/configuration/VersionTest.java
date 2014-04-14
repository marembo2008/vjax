/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.configuration;

import java.io.File;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class VersionTest {

  private static final String PATH = "test-version.xml";

  public static class VersionedClass {

    @Version
    private String version;
    private String data;

    public VersionedClass() {
    }

    public VersionedClass(String version, String data) {
      this.version = version;
      this.data = data;
    }

  }

  public VersionTest() {
  }

  @Before
  public void setUp() {
    System.setProperty(ConfigurationUtil.CONFIGURATION_FILE, PATH);
    System.setProperty(ConfigurationUtil.CONFIGURATION_PATH, System.getProperty("java.io.tmpdir"));
    VersionedClass vc = new VersionedClass("443", "data");
    ConfigurationUtil.getConfiguration(vc);
  }

  @After
  public void tearDown() {
    File file = new File(System.getProperty("java.io.tmpdir"), PATH);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void testVersioning() {
    VersionedClass vc = new VersionedClass("4554", "data");
    VersionedClass oldConfig = ConfigurationUtil.getConfiguration(vc);
    Assert.assertEquals("4554", oldConfig.version);
  }

}
