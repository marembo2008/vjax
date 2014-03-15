/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

/**
 *
 * @author marembo
 */
public class VJaxUtils {

  public static boolean isNullOrEmpty(Object str) {
    return str == null || str.toString().trim().length() == 0;
  }
}
