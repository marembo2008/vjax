/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

/**
 *
 * @author marembo
 */
public class VObjectMarshallerAndroidAdaptedTest  {

  static {
    System.setProperty("org.xml.sax.driver",
            "org.apache.xerces.parsers.SAXParser");
    System.setProperty("com.anosym.xml.sax.parser.adapted", "true");
  }
}
