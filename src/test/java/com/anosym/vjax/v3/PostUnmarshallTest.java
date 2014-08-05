package com.anosym.vjax.v3;

import static org.hamcrest.core.Is.is;

import com.anosym.vjax.VXMLBindingException;
import org.junit.Test;

import com.anosym.vjax.annotations.v3.PostUnmarshall;
import com.anosym.vjax.annotations.v3.Transient;
import com.anosym.vjax.xml.VDocument;
import java.util.List;
import org.junit.Assert;

/**
 *
 * @author mochieng
 */
public class PostUnmarshallTest {

    public PostUnmarshallTest() {
    }

    public static class TestInvalidPostUnmarshall {

        private int index;

        @PostUnmarshall
        public void onPostUnmarshall0() {
        }

        @PostUnmarshall
        public void onPostUnmarshall1() {
        }
    }

    public static class TestPostUnmarshall {

        private int index;
        @Transient
        private int dynamic;

        @PostUnmarshall
        public void onPostUnmarshall0() {
            dynamic = 100;
        }

    }

    public static class TestPostUnmarshallWithPostUnmarshallProperty {

        private int index;
        private TestPostUnmarshall testPostUnmarshall;

    }

    public static class TestPostUnmarshallWithListPostUnmarshallProperty {

        private int index;
        private List<TestPostUnmarshall> testPostUnmarshall;

    }

    @Test(expected = VXMLBindingException.class)
    public void testInvalidPostUnmarshall() throws VXMLBindingException {
        String xml = "<TestInvalidPostUnmarshall><index>45</index></TestInvalidPostUnmarshall>";

        VDocument doc = VDocument.parseDocumentFromString(xml);
        Unmarshaller<TestInvalidPostUnmarshall> m = new Unmarshaller<TestInvalidPostUnmarshall>(TestInvalidPostUnmarshall.class);
        TestInvalidPostUnmarshall val = m.unmarshall(doc);
    }

    @Test
    public void testPostUnmarshall() throws VXMLBindingException {
        String xml = "<TestPostUnmarshall><index>45</index></TestPostUnmarshall>";

        VDocument doc = VDocument.parseDocumentFromString(xml);
        Unmarshaller<TestPostUnmarshall> m = new Unmarshaller<TestPostUnmarshall>(TestPostUnmarshall.class);
        TestPostUnmarshall val = m.unmarshall(doc);
        Assert.assertThat(val.dynamic, is(100));
    }

    @Test
    public void testPostUnmarshallWithPostUnmarshallProperty() throws VXMLBindingException {
        String xml = "<TestPostUnmarshallWithPostUnmarshallProperty>"
                + "<index>34</index>"
                + "<testPostUnmarshall><index>45</index></testPostUnmarshall>"
                + "</TestPostUnmarshallWithPostUnmarshallProperty>";

        VDocument doc = VDocument.parseDocumentFromString(xml);
        Unmarshaller<TestPostUnmarshallWithPostUnmarshallProperty> m = new Unmarshaller<TestPostUnmarshallWithPostUnmarshallProperty>(TestPostUnmarshallWithPostUnmarshallProperty.class);
        TestPostUnmarshallWithPostUnmarshallProperty val = m.unmarshall(doc);
        Assert.assertThat(val.testPostUnmarshall.dynamic, is(100));
    }

    @Test
    public void testPostUnmarshallWithListPostUnmarshallProperty() throws VXMLBindingException {
        String xml = "<TestPostUnmarshallWithListPostUnmarshallProperty>"
                + "<index>34</index>"
                + "<testPostUnmarshall><testPostUnmarshall><index>45</index></testPostUnmarshall></testPostUnmarshall>"
                + "</TestPostUnmarshallWithListPostUnmarshallProperty>";

        VDocument doc = VDocument.parseDocumentFromString(xml);
        Unmarshaller<TestPostUnmarshallWithListPostUnmarshallProperty> m = new Unmarshaller<TestPostUnmarshallWithListPostUnmarshallProperty>(TestPostUnmarshallWithListPostUnmarshallProperty.class);
        TestPostUnmarshallWithListPostUnmarshallProperty val = m.unmarshall(doc);
        Assert.assertThat(val.testPostUnmarshall.get(0).dynamic, is(100));
    }

}
