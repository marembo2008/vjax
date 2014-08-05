/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.VXMLMemberNotFoundException;
import com.anosym.vjax.xml.VDocument;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marembo
 */
public class VObjectMarshallerTest {

    public static class Super {

        private int one;
        private String info;

        public Super() {
        }

        public Super(int one, String info) {
            this.one = one;
            this.info = info;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + this.one;
            hash = 43 * hash + (this.info != null ? this.info.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Super other = (Super) obj;
            if (this.one != other.one) {
                return false;
            }
            if ((this.info == null) ? (other.info != null) : !this.info.equals(other.info)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Super{" + "one=" + one + ", info=" + info + '}';
        }
    }

    public static class Sub extends Super {

        private int two;
        private String subInfo;

        public Sub() {
        }

        public Sub(int two, String subInfo, int one, String info) {
            super(one, info);
            this.two = two;
            this.subInfo = subInfo;
        }

        @Override
        public int hashCode() {
            int hash = super.hashCode();
            hash = 31 * hash + this.two;
            hash = 31 * hash + (this.subInfo != null ? this.subInfo.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) {
                return false;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Sub other = (Sub) obj;
            if (this.two != other.two) {
                return false;
            }
            if ((this.subInfo == null) ? (other.subInfo != null) : !this.subInfo.equals(other.subInfo)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return super.toString() + "Sub{" + "two=" + two + ", subInfo=" + subInfo + '}';
        }
    }

    public VObjectMarshallerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * This
     */
    @Test
    public void testMarshall() {
        try {
            Sub sub = new Sub(2, "sub-info", 1, "super-info");
            VObjectMarshaller<Sub> m = new VObjectMarshaller<VObjectMarshallerTest.Sub>(Sub.class);
            String expected = "<Sub>\n"
                    + "   <two>2</two>\n"
                    + "   <subInfo>sub-info</subInfo>\n"
                    + "   <one>1</one>\n"
                    + "   <info>super-info</info>\n"
                    + "</Sub>";
            VDocument doc = VDocument.parseDocumentFromString(expected);
            System.out.println(doc.toXmlString());
            Sub actualSub = m.unmarshall(doc);
            assertEquals(sub, actualSub);
        } catch (VXMLMemberNotFoundException ex) {
            Logger.getLogger(VObjectMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (VXMLBindingException ex) {
            Logger.getLogger(VObjectMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
