/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.url;

import com.anosym.vjax.annotation.IgnoreUrlParam;
import com.anosym.vjax.annotation.Url;
import com.anosym.vjax.annotation.UrlEncodeInheritedParam;
import com.anosym.vjax.annotation.UrlParam;
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
public class HttpUrlEncoderTest {

  public static class UrlEncodingTestParam {

    private String testParam1;
    @IgnoreUrlParam
    private String testParamIgnored2;
    @UrlParam("testurlparamrenamedby_urlparam_annotation")
    private String testParamRenamed3;
    @Url
    private String testUrl;

    public UrlEncodingTestParam(String testParam1, String testParamIgnored2, String testParamRenamed3, String testUrl) {
      this.testParam1 = testParam1;
      this.testParamIgnored2 = testParamIgnored2;
      this.testParamRenamed3 = testParamRenamed3;
      this.testUrl = testUrl;
    }
  }

  @UrlEncodeInheritedParam(encodeInheritedParam = true)
  public static class UrlEncodingTestParamInHerited extends UrlEncodingTestParam {

    private String testInheritedParam1;

    public UrlEncodingTestParamInHerited(String testInheritedParam1, String testParam1,
            String testParamIgnored2, String testParamRenamed3, String testUrl) {
      super(testParam1, testParamIgnored2, testParamRenamed3, testUrl);
      this.testInheritedParam1 = testInheritedParam1;
    }
  }

  public HttpUrlEncoderTest() {
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
   * Test of encodeUrl method, of class HttpUrlEncoder.
   */
  @org.junit.Test
  public void testEncodeUrl() throws Exception {
    System.out.println("encodeUrl");
    UrlEncodingTestParam object = new UrlEncodingTestParam("my param1", "my_param2_ignored", "my_param3_param_name_renamed", "http://url.com");
    HttpUrlEncoder<UrlEncodingTestParam> instance = new HttpUrlEncoder<UrlEncodingTestParam>();
    String expResult = "http://url.com?testParam1=my+param1&testurlparamrenamedby_urlparam_annotation=my_param3_param_name_renamed";
    String result = instance.encodeUrl(object);
    System.out.println("Result: " + result);
    assertEquals(expResult, result);
  }

  @Test
  public void testEncodeUrlWithIhneritField() throws Exception {
    System.out.println("encodeUrl");
    UrlEncodingTestParam object = new UrlEncodingTestParamInHerited("inherited_param", "my_param1", "my_param2_ignored",
            "my_param3_param_name_renamed", "http://url.com");
    HttpUrlEncoder<UrlEncodingTestParam> instance = new HttpUrlEncoder<UrlEncodingTestParam>();
    String expResult = "http://url.com?testInheritedParam1=inherited_param&testParam1=my_param1&testurlparamrenamedby_urlparam_annotation=my_param3_param_name_renamed";
    String result = instance.encodeUrl(object);
    System.out.println("Result: " + result);
    assertEquals(expResult, result);
  }
}
