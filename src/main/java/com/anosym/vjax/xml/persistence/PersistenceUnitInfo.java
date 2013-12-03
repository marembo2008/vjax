/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.ArrayParented;
import com.anosym.vjax.annotations.v3.Listener;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.v3.impl.PropertyListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marembo
 */
public class PersistenceUnitInfo {

  public static class PersistenceUnitInfoPropertyListener implements PropertyListener<PersistenceUnitInfo, PersistenceProperty[]> {

    @Override
    public void onSet(PersistenceUnitInfo object, PersistenceProperty[] property) {
      object.persistenceUnitProperties = new HashMap<String, PersistenceProperty>();
      for (PersistenceProperty pp : property) {
        object.persistenceUnitProperties.put(pp.getName(), pp);
      }
    }
  }
  @Attribute
  private String name;
  @Attribute
  private TransactionType transactionType;
  private String provider;
  @Markup(name = "class")
  private String[] entityClasses;
  @ArrayParented(componentMarkup = "property")
  @Markup(name = "properties")
  @Listener(PersistenceUnitInfoPropertyListener.class)
  private PersistenceProperty[] properties;
  @Transient
  private Map<String, PersistenceProperty> persistenceUnitProperties;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PersistenceProperty getPersistenceProperty(String name) {
    return persistenceUnitProperties.get(name);
  }
}
