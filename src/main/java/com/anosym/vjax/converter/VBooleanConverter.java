/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;

/**
 *
 * @author Marembo
 */
public class VBooleanConverter extends VConverter<Boolean> {

    public Boolean convert(String value) throws VConverterBindingException {
        //convert it based on yes/no, true/false, 0/1,
        value = value.trim();
        if ("yes".equalsIgnoreCase(value.trim()) || "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
            return true;
        } else if ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
            return false;
        } else {
            throw new VConverterBindingException("Invalid boolean representation");
        }
    }

    public String convert(Boolean value) throws VConverterBindingException {
        try {
            return (value) ? "true" : "false";
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<Boolean> clazz) {
        return clazz == boolean.class || clazz == Boolean.class;
    }
}
