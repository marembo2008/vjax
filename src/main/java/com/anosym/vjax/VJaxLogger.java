/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author marembo
 */
public class VJaxLogger {

  private static final Logger LOG = Logger.getLogger(VJaxLogger.class.getName());

  public static void setFilter(Filter newFilter) throws SecurityException {
    LOG.setFilter(newFilter);
  }

  public static void log(LogRecord record) {
    LOG.log(record);
  }

  public static void log(Level level, String msg) {
    LOG.log(level, msg);
  }

  public static void log(Level level, String msg, Object param1) {
    LOG.log(level, msg, param1);
  }

  public static void log(Level level, String msg, Object[] params) {
    LOG.log(level, msg, params);
  }

  public static void log(Level level, String msg, Throwable thrown) {
    LOG.log(level, msg, thrown);
  }

  public static void logp(Level level, String sourceClass, String sourceMethod, String msg) {
    LOG.logp(level, sourceClass, sourceMethod, msg);
  }

  public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Object param1) {
    LOG.logp(level, sourceClass, sourceMethod, msg, param1);
  }

  public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Object[] params) {
    LOG.logp(level, sourceClass, sourceMethod, msg, params);
  }

  public static void logp(Level level, String sourceClass, String sourceMethod, String msg, Throwable thrown) {
    LOG.logp(level, sourceClass, sourceMethod, msg, thrown);
  }

  public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg) {
    LOG.logrb(level, sourceClass, sourceMethod, bundleName, msg);
  }

  public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object param1) {
    LOG.logrb(level, sourceClass, sourceMethod, bundleName, msg, param1);
  }

  public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Object[] params) {
    LOG.logrb(level, sourceClass, sourceMethod, bundleName, msg, params);
  }

  public static void logrb(Level level, String sourceClass, String sourceMethod, String bundleName, String msg, Throwable thrown) {
    LOG.logrb(level, sourceClass, sourceMethod, bundleName, msg, thrown);
  }

  public static void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
    LOG.throwing(sourceClass, sourceMethod, thrown);
  }

  public static void severe(String msg) {
    LOG.severe(msg);
  }

  public static void warning(String msg) {
    LOG.warning(msg);
  }

  public static void info(String msg) {
    LOG.info(msg);
  }

  public static void config(String msg) {
    LOG.config(msg);
  }

  public static void fine(String msg) {
    LOG.fine(msg);
  }

  public static void finer(String msg) {
    LOG.finer(msg);
  }

  public static void finest(String msg) {
    LOG.finest(msg);
  }

  public synchronized void addHandler(Handler handler) throws SecurityException {
    LOG.addHandler(handler);
  }

  public synchronized void removeHandler(Handler handler) throws SecurityException {
    LOG.removeHandler(handler);
  }

}
