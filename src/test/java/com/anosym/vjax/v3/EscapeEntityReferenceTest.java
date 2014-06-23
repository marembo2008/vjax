/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class EscapeEntityReferenceTest {

    public static class ObjectWithEntityRef {

        private String ref;

        public ObjectWithEntityRef() {
        }

        public ObjectWithEntityRef(String ref) {
            this.ref = ref;
        }

    }

    @After
    public void after() {
    }

    @Before
    public void brefore() {
    }

    @Test
    public void testEscape() {
        String str = "This is a test for \"entity\" references escape &&& having > and < as xml data";
        ObjectWithEntityRef ref = new ObjectWithEntityRef(str);
        VObjectMarshaller<ObjectWithEntityRef> vom = new VObjectMarshaller<EscapeEntityReferenceTest.ObjectWithEntityRef>(
                ObjectWithEntityRef.class);
        String actual = vom.marshall(ref).getRootElement().toContent();
        String expected = "This is a test for &quot;entity&quot; references escape &amp;&amp;&amp; having &gt; and &lt; as xml data";
        Assert.assertEquals(expected, actual);
    }
}
