/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

import com.anosym.vjax.xml.VXMLHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author marembo
 */
public class VXMLPersistenceHandler extends VXMLHandler {

    private String elementName;
    private String elementIdName;
    private String elementId;
    private String namespaceUri;
    private boolean decodingElement;

    public VXMLPersistenceHandler(String elementName, String elementIdName, String elementId, String namespaceUri) {
        super();
        this.elementName = elementName;
        this.elementIdName = elementIdName;
        this.elementId = elementId;
        this.namespaceUri = namespaceUri;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (decodingElement) {
            super.endElement(uri, localName, qName);
            //check if we are ended
            if (localName.equals(elementName) && ((uri == null && namespaceUri == null) || (uri != null && namespaceUri != null && uri.equals(namespaceUri)))) {
                decodingElement = false;
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //we are to decode the element which has only the specifc id specified
        //we must make sure that we have the right element in its entirety
        if (localName.equals(elementName) && ((uri == null && namespaceUri == null) || (uri != null && namespaceUri != null && uri.equals(namespaceUri)))) {
            int num = attributes.getLength();
            for (int i = 0; i < num; i++) {
                String name = attributes.getLocalName(i);
                String value = attributes.getValue(i);
                String aQname = attributes.getQName(i);
                if ((aQname != null && !aQname.isEmpty()) && (name.equals(elementIdName) && value.equals(elementId))) {
                    super.startElement(uri, localName, qName, attributes);
                    decodingElement = true;
                    break;
                }
            }
        }
    }
}
