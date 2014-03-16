/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.util.VJaxUtils;

/**
 *
 * @author marembo
 */
public class VWrapperException extends Exception {

  private Class instanceClass;
  private Class wrapperClass;

  public VWrapperException() {
  }

  public VWrapperException(String message) {
    super(message);
  }

  public VWrapperException(String message, Throwable cause) {
    super(message, cause);
  }

  public VWrapperException(Throwable cause) {
    super(cause);
  }

  public VWrapperException(Class instanceClass, Class wrapperClass) {
    this.instanceClass = instanceClass;
    this.wrapperClass = wrapperClass;
  }

  public VWrapperException(Class instanceClass, Class wrapperClass, String message) {
    super(message);
    this.instanceClass = instanceClass;
    this.wrapperClass = wrapperClass;
  }

  public VWrapperException(Class instanceClass, Class wrapperClass, String message, Throwable cause) {
    super(message, cause);
    this.instanceClass = instanceClass;
    this.wrapperClass = wrapperClass;
  }

  public VWrapperException(Class instanceClass, Class wrapperClass, Throwable cause) {
    super(cause);
    this.instanceClass = instanceClass;
    this.wrapperClass = wrapperClass;
  }

  public Class getInstanceClass() {
    return instanceClass;
  }

  public void setInstanceClass(Class instanceClass) {
    this.instanceClass = instanceClass;
  }

  public Class getWrapperClass() {
    return wrapperClass;
  }

  public void setWrapperClass(Class wrapperClass) {
    this.wrapperClass = wrapperClass;
  }

  @Override
  public String getLocalizedMessage() {
    String localizedMessage = super.getLocalizedMessage();
    String message = "Instance Class=" + instanceClass + ", Wrapper Class=" + wrapperClass;
    return VJaxUtils.isNullOrEmpty(localizedMessage) ? message : (localizedMessage + " " + message);
  }

}
