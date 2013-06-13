/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.exceptions.VConverterBindingException;
import java.awt.Color;

/**
 *
 * @author Marembo
 */
public class VColorConverter extends VConverter<Color> {

    private static class ColorAlias {

        private int blue;
        private int green;
        private int red;
        private int alpha;

        public ColorAlias(Color color) {
            this.blue = color.getBlue();
            this.green = color.getGreen();
            this.red = color.getRed();
            this.alpha = color.getAlpha();
        }

        public ColorAlias() {
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }
    }

    public Color convert(String value) throws VConverterBindingException {
        try {

            return new Color(Integer.parseInt(value));
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public String convert(Color value) throws VConverterBindingException {
        try {
            return value.getRGB() + "";
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<Color> clazz) {
        return clazz == Color.class;
    }

    @Override
    public <A> A convertToAlias(Color value) throws VConverterBindingException, VXMLBindingException {
        try {
            return (A) new ColorAlias(value);
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    @Override
    public <A> Color convertToInstance(A value) throws VConverterBindingException {
        try {
            if (!(value instanceof ColorAlias)) {
                throw new VConverterBindingException("Unsupported alias type. Required " + ColorAlias.class);
            }
            ColorAlias alias = (ColorAlias) value;
            return new Color(alias.getRed(), alias.getGreen(), alias.getBlue(), alias.getAlpha());
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }
}
