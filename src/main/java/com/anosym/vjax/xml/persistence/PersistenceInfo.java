/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.Listener;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.v3.impl.PropertyListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marembo
 */
public class PersistenceInfo {

    public static final class PersistenceUnitInfoMapConverter implements PropertyListener<PersistenceInfo, PersistenceUnitInfo[]> {

        @Override
        public void onSet(PersistenceInfo persistenceInfo, PersistenceUnitInfo[] persistenceUnitInfos) {
            Map<String, PersistenceUnitInfo> map = new HashMap<String, PersistenceUnitInfo>();
            for (PersistenceUnitInfo pui : persistenceUnitInfos) {
                map.put(pui.getName(), pui);
            }
            persistenceInfo.persistenceUnits = map;
        }

    }
    @Transient
    private Map<String, PersistenceUnitInfo> persistenceUnits;
    @Markup(name = "persistence-unit")
    @Listener(PropertyListener.class)
    private PersistenceUnitInfo[] persistenceUnitInformation;

    public PersistenceUnitInfo getPersistenceUnitInfo(String unitName) {
        return persistenceUnits.get(unitName);
    }

}
