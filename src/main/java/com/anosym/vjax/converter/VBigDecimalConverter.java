/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;
import java.math.BigDecimal;

/**
 *
 * @author Marembo
 */
public class VBigDecimalConverter extends VConverter<BigDecimal> {

    public BigDecimal convert(String value) throws VConverterBindingException {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public String convert(BigDecimal value) throws VConverterBindingException {
        try {
            return value.toString();
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<BigDecimal> clazz) {
        return clazz == BigDecimal.class;
    }
}
