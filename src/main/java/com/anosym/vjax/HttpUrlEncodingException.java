/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

/**
 *
 * @author marembo
 */
public class HttpUrlEncodingException extends Exception {

  public HttpUrlEncodingException() {
  }

  public HttpUrlEncodingException(String message) {
    super(message);
  }

  public HttpUrlEncodingException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpUrlEncodingException(Throwable cause) {
    super(cause);
  }

  public HttpUrlEncodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
