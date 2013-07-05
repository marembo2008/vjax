/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import com.anosym.vjax.annotations.AsAttribute;
import com.anosym.vjax.annotations.AsAttributes;
import com.anosym.vjax.annotations.AsCollection;
import com.anosym.vjax.annotations.Generated;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.IgnoreGeneratedAttribute;
import com.anosym.vjax.annotations.NoNamespace;
import com.anosym.vjax.annotations.Position;
import com.anosym.vjax.annotations.XmlMarkup;
import com.anosym.vjax.id.generation.VGenerator;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class VMarshallerTest {

    @NoNamespace
    @IgnoreGeneratedAttribute
    public static interface DefaultSchema {
    }

    public static class AsCollectionExample implements DefaultSchema {

        private String name;
        private List<String> testCollections;

        public AsCollectionExample(String name, List<String> testCollections) {
            this.name = name;
            this.testCollections = testCollections;
        }

        @Position(index = 0)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @AsCollection(false)
        public List<String> getTestCollections() {
            return testCollections;
        }

        public void setTestCollections(List<String> testCollections) {
            this.testCollections = testCollections;
        }
    }

    @NoNamespace
    @IgnoreGeneratedAttribute
    public static class AsAttributeMapExample {

        private String name;
        private Map<String, BigDecimal> examples;

        public AsAttributeMapExample(String name, Map<String, BigDecimal> examples) {
            this.name = name;
            this.examples = examples;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @AsAttribute
        public Map<String, BigDecimal> getExamples() {
            return examples;
        }

        public void setExamples(Map<String, BigDecimal> examples) {
            this.examples = examples;
        }
    }

    @NoNamespace
    @IgnoreGeneratedAttribute
    public static class XmlMarkupExample {

        private String name;
        private String value;

        public XmlMarkupExample(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @XmlMarkup
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @NoNamespace
    @IgnoreGeneratedAttribute
    @AsAttributes(ignore = {XmlMarkupExample.class})
    public static class AsAttributesExample {

        private String data;
        private boolean state;
        private XmlMarkupExample example;

        public AsAttributesExample(String data, boolean state, XmlMarkupExample example) {
            this.data = data;
            this.state = state;
            this.example = example;
        }

        public AsAttributesExample() {
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public XmlMarkupExample getExample() {
            return example;
        }

        public void setExample(XmlMarkupExample example) {
            this.example = example;
        }
    }

    public static interface AsIdExampleInterface {

        @Id
        public String getId();

        public void setId(String id);
    }

    @NoNamespace
    @IgnoreGeneratedAttribute
    public static class AsIdExample implements AsIdExampleInterface {

        private String id;
        private String value;

        public AsIdExample() {
        }

        public AsIdExample(String id, String value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class AsAutoGeneratedExample extends AsIdExample {

        public AsAutoGeneratedExample() {
        }

        public AsAutoGeneratedExample(String id, String value) {
            super(id, value);
        }

        @Override
        @Generated
        public String getId() {
            return super.getId();
        }
    }

    public static class AsProviderGeneratedExample extends AsIdExample {

        public static class IdProvider implements VGenerator<String> {

            @Override
            public String generate() {
                return "PROVIDER_" + System.currentTimeMillis();
            }
        }

        public AsProviderGeneratedExample() {
        }

        public AsProviderGeneratedExample(String id, String value) {
            super(id, value);
        }

        @Override
        @Generated(provider = IdProvider.class)
        public String getId() {
            return super.getId();
        }
    }

    public VMarshallerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAsCollectionFalse() {
        try {
            VMarshaller<AsCollectionExample> m = new VMarshaller<VMarshallerTest.AsCollectionExample>();
            List<String> data = new ArrayList<String>();
            data.add("test1");
            data.add("test1");
            data.add("test3");
            AsCollectionExample ace = new AsCollectionExample("testing as collection", data);
            VDocument doc = m.marshallDocument(ace);
            doc.setDebug(true);
            String expDocStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"
                    + "<AsCollectionExample>"
                    + "   <name>testing as collection</name>"
                    + "   <string>test1</string>\n"
                    + "   <string>test1</string>\n"
                    + "   <string>test3</string>"
                    + "</AsCollectionExample>";
            VDocument expected = VDocument.parseDocumentFromString(expDocStr);
            expected.setDebug(true);
            assertEquals(expected, doc);
        } catch (VXMLBindingException ex) {
            Logger.getLogger(VMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testAsAttributeMapExample() {
        try {
            VMarshaller<AsAttributeMapExample> m = new VMarshaller<VMarshallerTest.AsAttributeMapExample>();
            Map<String, BigDecimal> cal = new HashMap<String, BigDecimal>();
            for (int i = 0; i < 10; i++) {
                cal.put("test" + i + "", BigDecimal.valueOf(i));
            }
            AsAttributeMapExample aame = new AsAttributeMapExample("AsAttribute map example", cal);
            VDocument result = m.marshallDocument(aame);
            String expStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"
                    + "<AsAttributeMapExample test1=\"1\" test0=\"0\" test8=\"8\" test9=\"9\" test6=\"6\" test7=\"7\" test4=\"4\" test5=\"5\" test2=\"2\" test3=\"3\">"
                    + "   <name>AsAttribute map example</name>"
                    + "</AsAttributeMapExample>";
            VDocument expected = VDocument.parseDocumentFromString(expStr);
            result.setDebug(true);
            expected.setDebug(true);
            assertEquals(expected, result);
        } catch (Exception ex) {
            Logger.getLogger(VMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testXmlMarkup() {
        try {
            VMarshaller<XmlMarkupExample> m = new VMarshaller<XmlMarkupExample>();
            XmlMarkupExample xme = new XmlMarkupExample("example", "example value");
            VDocument actual = m.marshallDocument(xme);
            actual.setDebug(true);
            String expStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"
                    + "<example>"
                    + "   <value>example value</value>"
                    + "</example>";
            VDocument expected = VDocument.parseDocumentFromString(expStr);
            expected.setDebug(true);
            assertEquals(expected, actual);
        } catch (Exception e) {
        }
    }

    @Test
    public void testAsAttributes() {
        try {
            VMarshaller<AsAttributesExample> m = new VMarshaller<AsAttributesExample>();
            XmlMarkupExample xme = new XmlMarkupExample("example_m", "example value");
            String data = "this is my data";
            boolean state = false;
            AsAttributesExample aae = new AsAttributesExample(data, state, xme);
            VDocument actual = m.marshallDocument(aae);
            actual.setDebug(true);
            String expStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
                    + "<AsAttributesExample data=\"this is my data\" state=\"false\">\n"
                    + "   <example_m>\n"
                    + "      <value>example value</value>\n"
                    + "   </example_m>\n"
                    + "</AsAttributesExample>";
            VDocument expected = VDocument.parseDocumentFromString(expStr);
            expected.setDebug(true);
            assertEquals(expected, actual);
        } catch (Exception e) {
        }
    }

    @Test
    public void testAsId() {
        try {
            VMarshaller<AsIdExample> m = new VMarshaller<AsIdExample>();
            String id = "387483493";
            String data = "an ided element";
            AsIdExample aie = new AsIdExample(id, data);
            VDocument actual = m.marshallDocument(aie);
            actual.setDebug(true);
            String expStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
                    + "<AsIdExample id=\"387483493\">\n"
                    + "   <value>an ided element</value>\n"
                    + "</AsIdExample>";
            VDocument expected = VDocument.parseDocumentFromString(expStr);
            expected.setDebug(true);
            assertEquals(expected, actual);
        } catch (Exception e) {
        }
    }

    @Test
    public void testAsAutoId() {
        try {
            VMarshaller<AsIdExample> m = new VMarshaller<AsIdExample>();
            String id = "387483493";
            String data = "an ided element";
            AsIdExample aie = new AsAutoGeneratedExample(id, data);
            VDocument actual = m.marshallDocument(aie);
            actual.setDebug(true);
            //since the id is generated, we simply need to check that the attribute exists and has value
            VAttribute idAttr = actual.getRootElement().getAttribute("id");
            assertNotNull(idAttr);
            assertNotNull(idAttr.getValue());
            assertFalse(idAttr.getValue().trim().isEmpty());
        } catch (Exception e) {
        }
    }

    @Test
    public void testAsProviderId() {
        try {
            VMarshaller<AsIdExample> m = new VMarshaller<AsIdExample>();
            String id = "387483493";
            String data = "an ided element";
            AsIdExample aie = new AsProviderGeneratedExample(id, data);
            VDocument actual = m.marshallDocument(aie);
            actual.setDebug(true);
            //since the id is generated, we simply need to check that the attribute exists and has value
            VAttribute idAttr = actual.getRootElement().getAttribute("id");
            assertNotNull(idAttr);
            assertNotNull(idAttr.getValue());
            assertTrue(idAttr.getValue().trim().startsWith("PROVIDER_"));
        } catch (Exception e) {
        }
    }

    /**
     * **********************************************************************************************
     * Testing unmarshalling
     * **********************************************************************************************
     */
    public static class UnmarshallExample {

        private String name;
        private String title;
        private int id;

        public UnmarshallExample() {
        }

        public UnmarshallExample(String name, String title, int id) {
            this.name = name;
            this.title = title;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 83 * hash + (this.title != null ? this.title.hashCode() : 0);
            hash = 83 * hash + this.id;
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
            final UnmarshallExample other = (UnmarshallExample) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
                return false;
            }
            if (this.id != other.id) {
                return false;
            }
            return true;
        }
    }

    @Test
    public void testBasicUnmarshall() {
        try {
            VMarshaller<UnmarshallExample> m = new VMarshaller<VMarshallerTest.UnmarshallExample>();
            UnmarshallExample expected = new UnmarshallExample("unmarshall example", "test example", 3555354);
            VDocument doc = m.marshallDocument(expected);
            System.out.println(doc.toXmlString());
            UnmarshallExample result = m.unmarshall(doc);
            assertEquals(expected, result);
        } catch (Exception ex) {
            Logger.getLogger(VMarshallerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
