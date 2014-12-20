package com.anosym.vjax.v3.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;

/**
 *
 * @author marembo
 */
public class VObjectWrapperTest {

    public VObjectWrapperTest() {
    }

    @AfterClass
    public static void clear() {
        File file = new File("src/wrapper-test");
        if (file.exists()) {
            try {
                //delete all folders and their subfolder.
                System.out.println("Deleting resources: " + file.getAbsolutePath());
                FileUtils.deleteDirectory(file);
            } catch (IOException ex) {
                Logger.getLogger(VObjectWrapperTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Test
    public void testGeneration() {
        VObjectWrapper wrapper = new VObjectWrapper("src/wrapper-test/main");
        wrapper.process();
    }

}
