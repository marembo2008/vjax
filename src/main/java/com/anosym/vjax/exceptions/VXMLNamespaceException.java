/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.exceptions;

/**
 *
 * @author Marembo
 */
public class VXMLNamespaceException extends IllegalArgumentException {

    public VXMLNamespaceException(Throwable cause) {
        super(cause);
    }

    public VXMLNamespaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public VXMLNamespaceException(String s) {
        super(s);
    }

    public VXMLNamespaceException() {
    }
}
