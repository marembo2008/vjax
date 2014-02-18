/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import java.io.File;
import java.util.logging.Level;

/**
 * Manages xml application configurations of loading and saving.
 *
 * @author marembo
 */
public class ConfigurationUtil {

  public static final String CONFIGURATION_PATH = "com.anosym.vjax.configuration-path";
  public static final String CONFIGURATION_FILE = "com.anosym.vjax.configuration-file";

  private static File getPath() {
    String configPath = System.getProperty(CONFIGURATION_PATH, System.getProperty("user.home"));
    String configFile = System.getProperty(CONFIGURATION_FILE, "configuration.xml");
    VJaxLogger.fine("Configuration Path: " + configPath);
    VJaxLogger.fine("Configuration File: " + configFile);
    return new File(configPath, configFile);
  }

  public static <T> T getConfiguration(T defaultConfig) {
    try {
      VObjectMarshaller<T> m = new VObjectMarshaller<T>((Class<? extends T>) defaultConfig.getClass());
      File configPath = getPath();
      VDocument doc;
      T ac = defaultConfig;
      if (configPath.exists() && configPath.length() > 100) {
        doc = VDocument.parseDocument(configPath);
        ac = m.unmarshall(doc);
        VJaxLogger.info("Loaded Configurations: " + configPath.getAbsolutePath());
        VJaxLogger.fine(doc.toXmlString());
      } else {
        //we save the default config
        saveConfiguration(ac);
      }
      return ac;
    } catch (VXMLMemberNotFoundException ex) {
      VJaxLogger.log(Level.SEVERE, null, ex);
    } catch (VXMLBindingException ex) {
      VJaxLogger.log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public static <T> void saveConfiguration(T config) {
    VObjectMarshaller<T> m = new VObjectMarshaller<T>((Class<? extends T>) config.getClass());
    File file = getPath();
    VDocument doc = m.marshall(config);
    doc.setDocumentName(file);
    doc.writeDocument();
    VJaxLogger.info("Saving configurations: " + file.getAbsolutePath());
    VJaxLogger.fine(doc.toXmlString());
  }
}
