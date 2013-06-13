/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.test;

import com.anosym.vjax.annotations.Markup;
import java.io.Serializable;

/**
 *
 * @author variance
 */
public class Single1 extends Single101Data<String> implements Serializable {

    public Single1() {
    }

    public Single1(String val) {
        super(val);
    }

    @Override
    @Markup(name = "data-field", property = "firstValue")
    public String getValue() {
        return super.getValue();
    }
}
