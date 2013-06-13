/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

/**
 *
 * @author marembo
 */
public final class VContent extends VElement {

  public VContent(String content) {
    super("content");
    setContent(content);
  }

  @Override
  public String toString() {
    return getContent();
  }
}
