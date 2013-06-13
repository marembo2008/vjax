/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

/**
 *
 * @author Marembo
 */
public enum VModifier {

    PUBLIC,
    PROTECTED,
    PRIVATE,
    FINAL,
    PACKAGE_PRIVATE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
