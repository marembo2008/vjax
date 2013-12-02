/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayParented;

/**
 *
 * @author marembo
 */
public class PersistenceUnitInfo {

    @Attribute
    private String name;
    @Attribute
    private TransactionType transactionType;
    private String provider;
    @Markup(name = "class")
    private String[] entityClasses;
    @ArrayParented(componentMarkup = "property")
    @Markup(name = "properties")
    private PersistenceProperty[] properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
