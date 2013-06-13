/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

import com.anosym.vjax.VXMLBindingException;

/**
 *
 * @author Marembo
 */
public class VSchemaException extends VXMLBindingException {

    public VSchemaException() {
    }

    public VSchemaException(String message) {
        super(message);
    }

    public VSchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public VSchemaException(Throwable cause) {
        super(cause);
    }
}
