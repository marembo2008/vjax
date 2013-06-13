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

  @Override
  public boolean equals(Object obj) {
    if (getClass() != obj.getClass()) {
      return false;
    }
    VElement other = (VElement) obj;
    return getContent() == null ? other.getContent() == null : getContent().equals(other.getContent());
  }

  @Override
  public int hashCode() {
    int hash = 89 * ((getContent() != null) ? getContent().hashCode() : 0);
    return hash;
  }
}
