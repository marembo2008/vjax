/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

/**
 *
 * @author Marembo
 */
public enum VJaxPrimitive {

    BOOLEAN,
    INTEGER,
    LONG,
    CHARACTER,
    BYTE,
    SHORT,
    DOUBLE,
    FLOAT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
