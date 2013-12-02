/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.v3.Listener;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class PropertyListenerTest {

    public PropertyListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of onSet method, of class PropertyListener.
     */
    @Test
    public void testListener() {
        try {
            System.out.println("onSet");
            String xml = "<Instance><number>737337</number></Instance>";
            Instance instance = new VObjectMarshaller<Instance>(Instance.class).unmarshall(VDocument.parseDocumentFromString(xml));
            int expected = 737337;
            int actual = instance.numberInt;
            assertEquals(expected, actual);
        } catch (VXMLBindingException ex) {
            Logger.getLogger(PropertyListenerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class Instance {

        @Listener(PropertyListenerImpl.class)
        private String number;
        @Transient
        private int numberInt;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getNumberInt() {
            return numberInt;
        }

        public void setNumberInt(int numberInt) {
            this.numberInt = numberInt;
        }

    }

    public static class PropertyListenerImpl implements PropertyListener<Instance, String> {

        @Override
        public void onSet(Instance object, String property) {
            object.numberInt = Integer.parseInt(property);
        }

    }

}
