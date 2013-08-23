/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.url;

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
}
