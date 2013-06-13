/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * This reader has been implemented for fast retrieval of indexed data
 * It is typically used where database query is common
 * @author marembo
 */
public class VXMLPersistenceReader implements XMLReader {

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEntityResolver(EntityResolver resolver) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EntityResolver getEntityResolver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDTDHandler(DTDHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DTDHandler getDTDHandler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setContentHandler(ContentHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ContentHandler getContentHandler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setErrorHandler(ErrorHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ErrorHandler getErrorHandler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parse(InputSource input) throws IOException, SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parse(String systemId) throws IOException, SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
