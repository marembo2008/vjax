/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

/**
 *
 * @author Marembo
 */
public class VNamespaceConflictException extends VNamespaceException {

    public VNamespaceConflictException() {
    }

    public VNamespaceConflictException(String s) {
        super(s);
    }

    public VNamespaceConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public VNamespaceConflictException(Throwable cause) {
        super(cause);
    }
}
