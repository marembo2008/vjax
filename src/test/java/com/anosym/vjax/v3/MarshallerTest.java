package com.anosym.vjax.v3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.annotations.v3.Default;
import com.anosym.vjax.annotations.v3.DefinedAttribute;
import com.anosym.vjax.annotations.v3.ElementMarkup;
import com.anosym.vjax.annotations.v3.Implemented;
import com.anosym.vjax.annotations.v3.KeyValueEntryMarkup;
import com.anosym.vjax.annotations.v3.Marshallable;
import com.anosym.vjax.annotations.v3.Nullable;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.converter.v3.Converter;
import com.anosym.vjax.v3.defaulter.Defaulter;
import com.anosym.vjax.xml.VAttribute;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

/**
 * We test all possible annotations.
 *
 * @author mochieng
 */
public class MarshallerTest {

    @Markup(name = "markup-on-class")
    public static class TestMarkupAnnotationOnClass {

        private String name;
    }

    public static class TestMarkupAnnotationOnField {

        @Markup(name = "markup-on-field")
        private String name;

        public TestMarkupAnnotationOnField() {
        }

        public TestMarkupAnnotationOnField(String name) {
            this.name = name;
        }

    }

    public static class TestAttribute {

        @Attribute
        private String name;

        public TestAttribute(String name) {
            this.name = name;
        }

        public TestAttribute() {
        }

    }

    public static class TestId {

        @Id
        private String id;

        public TestId() {
        }

        public TestId(String id) {
            this.id = id;
        }

    }

    public static class TestReferenceAndId {

        @Id
        private String id;
        private TestReferenceAndId testReferenceAndId;

        public TestReferenceAndId() {
        }

        public TestReferenceAndId(String id) {
            this.id = id;
        }

        public void setTestReferenceAndId(TestReferenceAndId testReferenceAndId) {
            this.testReferenceAndId = testReferenceAndId;
        }

    }

    public static class TestMarshallable {

        private String marshallable;
        //should not be marshalled, hence no element.
        @Marshallable(Marshallable.Option.UNMARSHALL)
        private String notMarshallable;

        public TestMarshallable() {
        }

        public TestMarshallable(String marshallable, String notMarshallable) {
            this.marshallable = marshallable;
            this.notMarshallable = notMarshallable;
        }

    }

    public static class TestTransient {

        @Transient
        private String transientValue;

        public TestTransient(String transientValue) {
            this.transientValue = transientValue;
        }

    }

    public static interface TestInterface {

    }

    public static class TestInterfaceImpl {

        @Nullable
        private String value;
    }

    public static class TestNoImplemented {

        @Nullable
        private TestInterface testInterface;
    }

    public static class TestImplemented {

        @Nullable
        @Implemented(TestInterfaceImpl.class)
        private TestInterface testInterface;
    }

    public static class TestNullable {

        @Nullable
        private String nullable;
        private String notNullable;

        public TestNullable() {
        }

    }

    public static class MyTestObject {

        private String name;
        private String value;

        public MyTestObject() {
        }

        public MyTestObject(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public static MyTestObject from(String s) {
            String[] vals = s.split("=");
            return new MyTestObject(vals[0], vals[1]);
        }

        @Override
        public String toString() {
            return name + "=" + value;
        }

    }

    public static class MyConverter implements Converter<MyTestObject, String> {

        @Override
        public String convertFrom(MyTestObject value) {
            return value == null ? null : value.toString();
        }

        @Override
        public MyTestObject convertTo(String value) {
            return Strings.isNullOrEmpty(value) ? null : MyTestObject.from(value);
        }

    }

    public static class TestConverter {

        @com.anosym.vjax.annotations.v3.Converter(MyConverter.class)
        private MyTestObject myTestObject;

        public TestConverter() {
        }

        public TestConverter(MyTestObject myTestObject) {
            this.myTestObject = myTestObject;
        }

    }

    public MarshallerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMarkupOnClass() throws VXMLBindingException {
        Marshaller<TestMarkupAnnotationOnClass> m = new Marshaller<MarshallerTest.TestMarkupAnnotationOnClass>(
                TestMarkupAnnotationOnClass.class);
        VDocument doc = m.marshall(new TestMarkupAnnotationOnClass());
        VElement root = doc.getRootElement();
        String expected = "markup-on-class";
        String actual = root.getMarkup();
        assertEquals(expected, actual);
    }

    @Test
    public void testMarkupOnField() throws VXMLBindingException {
        Marshaller<TestMarkupAnnotationOnField> m = new Marshaller<MarshallerTest.TestMarkupAnnotationOnField>(
                TestMarkupAnnotationOnField.class);
        VDocument doc = m.marshall(new TestMarkupAnnotationOnField("test"));
        VElement root = doc.getRootElement();
        assertEquals(1, root.childrenCount());
        VElement nameElem = root.getChildren().get(0);
        String expected = "markup-on-field";
        String actual = nameElem.getMarkup();
        assertEquals(expected, actual);
    }

    public void testAttribute() throws VXMLBindingException {
        String expected = "attribute000";
        String actual = getMarshaller(TestAttribute.class)
                .marshall(new TestAttribute(expected))
                .getRootElement()
                .getAttribute("name")
                .getValue();
        assertEquals(expected, actual);
    }

    @Test(expected = VXMLBindingException.class)
    public void testNullId() throws VXMLBindingException {
        VDocument m = getMarshaller(TestId.class).marshall(new TestId());
    }

    @Test
    public void testId() throws VXMLBindingException {
        String expected = "123456789";
        VDocument m = getMarshaller(TestId.class).marshall(new TestId("123456789"));
        VElement root = m.getRootElement();
        assertTrue(root.hasAttribute("id"));
        assertEquals(expected, root.getAttribute("id").getValue());
    }

    @Test
    public void testReferenceAndId() throws VXMLBindingException {
        Marshaller<TestReferenceAndId> m = new Marshaller<MarshallerTest.TestReferenceAndId>(TestReferenceAndId.class);
        TestReferenceAndId andId = new TestReferenceAndId("12345");
        TestReferenceAndId newAndId = new TestReferenceAndId("12345");
        String expected = "12345";
        andId.setTestReferenceAndId(newAndId);
        VDocument doc = m.marshall(andId);
        doc.setDebug(true);
        System.out.println(doc);
        VElement root = doc.getRootElement();
        assertTrue("expected id attribute", root.hasAttribute("id"));
        assertTrue("expected ref-id attribute on child element", root.getChildren().get(0).hasAttribute("ref-id"));
        VAttribute attr = root.getChildren().get(0).getAttribute("ref-id");
        String actual = attr.getValue();
        assertEquals("", expected, actual);
    }

    @Test
    public void testMarshallable() throws VXMLBindingException {
        VElement elem = getMarshaller(TestMarshallable.class)
                .marshall(new TestMarshallable("marshallable", "norMarshallable")).getRootElement();
        assertTrue("expects only one child", elem.childrenCount() == 1);
        assertEquals("expects only the marshallable element as child", "marshallable", elem.getChildren().get(0)
                     .getMarkup());
    }

    @Test
    public void testTransient() throws VXMLBindingException {
        VDocument doc = getMarshaller(TestTransient.class).marshall(new TestTransient("value"));
        assertTrue("exepcts no children", doc.getRootElement().isEmpty());
    }

    @Test
    public void testNullable() throws VXMLBindingException {
        VDocument doc = getMarshaller(TestNullable.class).marshall(new TestNullable());
        VElement root = doc.getRootElement();
        assertTrue("expects only one child", root.childrenCount() == 1);
        assertEquals("Expects only the nullable element", "nullable", root.getChildren().get(0).getMarkup());
    }

    @Test(expected = VXMLBindingException.class)
    public void testNotImplemented() throws VXMLBindingException {
        getMarshaller(TestNoImplemented.class).marshall(new TestNoImplemented());
    }

    @Test
    public void testImplemented() throws VXMLBindingException {
        VElement root = getMarshaller(TestImplemented.class).marshall(new TestImplemented()).getRootElement();
        assertEquals("expected value element", "value", root.getChildren().get(0).getChildren().get(0).getMarkup());
    }

    @Test
    public void testConverter() throws VXMLBindingException {
        VElement root = getMarshaller(TestConverter.class)
                .marshall(new TestConverter(new MyTestObject("id", "7373773"))).getRootElement();
        System.out.println(root.toXmlString());
        VElement mo = root.getChildren().get(0);
        assertEquals("id=7373773", mo.toContent());
    }

    public static class TestDefinedAttribute {

        @DefinedAttribute(name = "namespace", value = "http://namespace.com")
        private String issue;

        public TestDefinedAttribute(String issue) {
            this.issue = issue;
        }

    }

    @Test
    public void testDefinedAttribute() throws VXMLBindingException {
        VElement root = getMarshaller(TestDefinedAttribute.class).marshall(new TestDefinedAttribute("resolved"))
                .getRootElement();
        System.out.println(root.toXmlString());
        VElement issue = root.getFirstChild();
        assertTrue("contains defined attribute", issue.hasAttribute("namespace"));
        assertEquals("contains defined attribute", "http://namespace.com", issue.getAttribute("namespace").getValue());
    }

    public static class TestElementMarkup {

        @ElementMarkup("el")
        private List<String> elements;

        public TestElementMarkup(List<String> elements) {
            this.elements = elements;
        }

    }

    @Test
    public void testElementMarkup() throws VXMLBindingException {
        TestElementMarkup tem = new TestElementMarkup(Lists.newArrayList("one", "two"));
        VDocument doc = getMarshaller(TestElementMarkup.class).marshall(tem);
        VElement root = doc.getRootElement();
        VElement elements = root.getFirstChild();
        System.out.println(doc.toXmlString());
        assertEquals("Must have two children element", 2, elements.childrenCount());
        VElement child = elements.getFirstChild();
        assertEquals("Expected element with markup: el", "el", child.getMarkup());
    }

    public static class TestKeyValueEntryMarkup {

        @KeyValueEntryMarkup(key = "str", value = "int", entry = "value")
        private Map<String, Integer> values;

        public TestKeyValueEntryMarkup(Map<String, Integer> values) {
            this.values = values;
        }

    }

    @Test
    public void testKeyValueEntryMarkup() throws VXMLBindingException {
        TestKeyValueEntryMarkup markup = new TestKeyValueEntryMarkup(ImmutableMap.of("id", 39394343));
        VElement root = getMarshaller(TestKeyValueEntryMarkup.class).marshall(markup).getRootElement();
        System.out.println(root.toXmlString());
        VElement map = root.getFirstChild();
        VElement entry = map.getFirstChild();
        assertEquals("expected value ofr entry markup", "value", entry.getMarkup());
        VElement key = entry.getFirstChild();
        assertEquals("expected str for key markup", "str", key.getMarkup());
        VElement value = entry.getChildren().get(1);
        assertEquals("expected int for value markup", "int", value.getMarkup());
    }

    public static class TestCircularReference {

        private MyTestObject myTestObject;
        private TestCircularReference myCircularReference;

        public TestCircularReference() {
        }

        public TestCircularReference(MyTestObject myTestObject) {
            this.myTestObject = myTestObject;
        }

        public void setMyCircularReference(TestCircularReference myCircularReference) {
            this.myCircularReference = myCircularReference;
        }

    }

    @Test
    public void testCircularReference() throws VXMLBindingException {
        TestCircularReference reference = new TestCircularReference(new MyTestObject("name", "value"));
        reference.setMyCircularReference(reference);
        VElement elem = getMarshaller(TestCircularReference.class).marshall(reference).getRootElement();
        System.out.println(elem.toXmlString());
        VElement circularRef = elem.getChild("myCircularReference");
        assertTrue("Reference has no child", circularRef.childrenCount() == 0);
    }

    public static interface DefaultObject {
    }

    public static class DefaultObjectImpl implements DefaultObject {

        private String name;

        public DefaultObjectImpl(String name) {
            this.name = name;
        }

    }

    public static class TestDefault {

        public static class TestDefaultDefaulter implements Defaulter<DefaultObject> {

            @Override
            public DefaultObject getDefault(Class<? extends DefaultObject> type) {
                return new DefaultObjectImpl("defaulted-name");
            }
        }
        @Default(defaulter = TestDefaultDefaulter.class)
        private DefaultObject defaultObject;
        @Default("100")
        private Integer defaultInt;
    }

    @Test
    public void testDefault() throws VXMLBindingException {
        TestDefault td = new TestDefault();
        VElement root = getMarshaller(TestDefault.class).marshall(td).getRootElement();
        System.out.println(root.toXmlString());
        VElement intElem = root.getChild("defaultInt");
        String intValue = intElem.toContent();
        assertEquals("100", intValue);
        VElement defaulterElem = root.getChild("defaultObject");
        VElement nameElem = defaulterElem.getFirstChild();
        assertEquals("defaulted-name", nameElem.toContent());
    }

    //test marshll array, collections and maps.
    private <T> Marshaller<T> getMarshaller(Class<T> clazz) {
        return new Marshaller<T>(clazz);
    }

}
