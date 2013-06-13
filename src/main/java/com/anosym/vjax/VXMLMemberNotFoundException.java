/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

/**
 *
 * @author marembo
 */
public class VXMLMemberNotFoundException extends VXMLBindingException {

  public VXMLMemberNotFoundException() {
  }

  public VXMLMemberNotFoundException(String message) {
    super(message);
  }

  public VXMLMemberNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VXMLMemberNotFoundException(Throwable cause) {
    super(cause);
  }
}
