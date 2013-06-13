/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;
import java.math.BigInteger;

/**
 *
 * @author Marembo
 */
public class VBigIntegerConverter extends VConverter<BigInteger> {

    public BigInteger convert(String value) throws VConverterBindingException {
        try {
            return new BigInteger(value);
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public String convert(BigInteger value) throws VConverterBindingException {
        try {
            return value.toString();
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<BigInteger> clazz) {
        return clazz == BigInteger.class;
    }
}
