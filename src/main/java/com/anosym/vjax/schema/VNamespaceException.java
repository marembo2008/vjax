/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

/**
 *
 * @author Marembo
 */
public class VNamespaceException extends IllegalArgumentException {

    public VNamespaceException(Throwable cause) {
        super(cause);
    }

    public VNamespaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public VNamespaceException(String s) {
        super(s);
    }

    public VNamespaceException() {
    }
}
