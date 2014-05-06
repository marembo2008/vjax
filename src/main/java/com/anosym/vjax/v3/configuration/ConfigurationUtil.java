/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.configuration;

import com.anosym.vjax.VJaxLogger;
import com.anosym.vjax.util.VJaxUtils;
import com.anosym.vjax.v3.VObjectMarshaller;
import com.anosym.vjax.xml.VDocument;
import java.io.File;
import java.lang.reflect.Field;
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
      boolean loadedConfig;
      if (loadedConfig = (configPath.exists() && configPath.length() > 100)) {
        doc = VDocument.parseDocument(configPath);
        ac = m.unmarshall(doc);
        VJaxLogger.info("Loaded Configurations: " + configPath.getAbsolutePath());
        VJaxLogger.fine(doc.toXmlString());
      }
      //we save the default config regardless, so that we update the underlying config xml with any new fields.
      if (isUpdateable(defaultConfig, ac) || !loadedConfig) {
        updateVersion(defaultConfig, ac);
        saveConfiguration(ac);
      }
      return ac;
    } catch (Exception ex) {
      VJaxLogger.log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Returns true if the old config should be persisted.
   *
   * @param <T>
   * @param newConfig
   * @param oldConfig
   * @return
   * @throws Exception
   */
  private static <T> boolean isUpdateable(T newConfig, T oldConfig) throws Exception {
    String version0 = getVersion(oldConfig);
    String version1 = getVersion(newConfig);
    return !version0.equals(version1);
  }

  /**
   * Replaces the version for the old config with the new config version.
   *
   * @param <T>
   * @param newConfig
   * @param oldConfig
   * @throws Exception
   */
  private static <T> void updateVersion(T newConfig, T oldConfig) throws Exception {
    String newVersion = getVersion(newConfig);
    Field versionField = VJaxUtils.getFieldAnnotated(oldConfig.getClass(), Version.class);
    versionField.setAccessible(true);
    versionField.set(oldConfig, newVersion);
  }

  /**
   * Return empty string if version field is not present.
   *
   * @param <T>
   * @param config
   * @return
   */
  private static <T> String getVersion(T config) throws Exception {
    Field versionField = VJaxUtils.getFieldAnnotated(config.getClass(), Version.class);
    if (versionField == null) {
      return "";
    }
    versionField.setAccessible(true);
    Object val = versionField.get(config);
    if (val != null) {
      return val.toString();
    }
    return "";
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
