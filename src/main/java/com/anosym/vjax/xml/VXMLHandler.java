/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 *
 * @author Marembo
 */
public class VXMLHandler extends org.xml.sax.helpers.DefaultHandler implements LexicalHandler, DeclHandler {

  private Deque<VElement> elements;
  private VElement element;
  private StringBuffer contentBuffer;
  private StringBuffer commentBuffer;
  private List<String[]> notationDeclaration;

  public VXMLHandler() {
    this.elements = new ArrayDeque<VElement>();
    notationDeclaration = new ArrayList<String[]>();
    this.element = null;
    this.contentBuffer = new StringBuffer();
    this.commentBuffer = new StringBuffer();
  }

  @Override
  public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    String[] decls = {name, publicId, systemId};
    notationDeclaration.add(decls);
  }

  @Override
  public void endElement(String uri, String localName, String qName)
          throws SAXException {
    // no op
    this.element = this.elements.pop();
    if (this.element != null) {
      String content = this.contentBuffer.toString().trim();
      if (!content.isEmpty()) {
        this.element.addChild(new VContent(content));
      }
      String comment = this.commentBuffer.toString().trim();
      if (!comment.isEmpty()) {
        this.element.setComment(comment);
      }
      this.contentBuffer.delete(0, this.contentBuffer.length());
      this.commentBuffer.delete(0, this.commentBuffer.length());
      if (!this.element.getMarkup().equals(localName)) {
        throw new SAXException("Invalid Element matching tags");
      }
    }
  }

  @Override
  public void startElement(String uri, String localName,
          String qName, Attributes attributes)
          throws SAXException {
    // no op
    VElement elem = new VElement(localName);
    if (!this.elements.isEmpty()) {
      VElement p = this.elements.peek();
      //we may have mixed elements. So we add content if there is before a new element
      if (this.contentBuffer.length() > 0) {
        //we have mixed sequence
        String content = this.contentBuffer.toString().trim();
        if (!content.isEmpty()) {
          p.addChild(new VContent(content));
        }
        this.contentBuffer.delete(0, this.contentBuffer.length());
      }
      p.addChild(elem);
    }
    this.elements.push(elem);
    int num = attributes.getLength();
    VNamespace namespace = null;
    if (uri != null && !uri.isEmpty()) {
      namespace = new VNamespace("", uri);
      if (qName != null && !qName.isEmpty() && qName.contains(":")) {
        String prf = qName.substring(0, qName.indexOf(":"));
        namespace.setPrefix(prf);
      }
      VNamespace assocNm = elem.getAssociatedNamespace();
      if (assocNm == null || !assocNm.equals(namespace)) {
        assocNm = namespace;
      }
      elem.setAssociatedNamespace(assocNm);
    }
    for (int i = 0; i < num; i++) {
      String name = attributes.getLocalName(i);
      String value = attributes.getValue(i);
      String aQname = attributes.getQName(i);
      if (aQname != null && !aQname.isEmpty()) {
        if ("elementFormDefault".equalsIgnoreCase(name) && namespace != null) {
          namespace.setElementFormDefault("qualified".equalsIgnoreCase(value));
        } else if ("attributeFormDefault".equalsIgnoreCase(name) && namespace != null) {
          namespace.setAttributeFormDefault("qualified".equalsIgnoreCase(value));
        } else {
          VAttribute a = new VAttribute(name, value);
          a.setAttributeNamespace(namespace);
          elem.addAttribute(a);
        }
      }
    }
  }

  @Override
  public void characters(char ch[], int start, int length)
          throws SAXException {
    // no op
    char[] aa = java.util.Arrays.copyOfRange(ch, start, start + length);
    String value = String.copyValueOf(aa);
    this.contentBuffer.append(value);
  }

  public VElement getRootElement() {
    return this.element;
  }

  public List<String[]> getNotationDeclaration() {
    return notationDeclaration;
  }

  public void startDTD(String name, String publicId, String systemId) throws SAXException {
    //do nothing
    System.out.println(name);
    System.out.println(publicId);
    System.out.println(systemId);
  }

  public void endDTD() throws SAXException {
    //do nothing
  }

  public void startEntity(String name) throws SAXException {
    //do nothing
    System.out.println(name);
  }

  public void endEntity(String name) throws SAXException {
    //do nothing
    System.out.println(name);
  }

  public void startCDATA() throws SAXException {
    //do nothing
  }

  public void endCDATA() throws SAXException {
    //do nothing
  }

  public void comment(char[] ch, int start, int length) throws SAXException {
    // no op
    char[] aa = java.util.Arrays.copyOfRange(ch, start, start + length);
    String value = String.copyValueOf(aa);
    //add with a new line annotation
    if (this.commentBuffer.length() > 0) {
      this.commentBuffer.append("\n").append(value);
    } else {
      this.commentBuffer.append(value);
    }
  }

  public void elementDecl(String name, String model) throws SAXException {
    System.out.println(name + " " + model);
  }

  public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
    System.out.println(eName);
    System.out.println(aName);
    System.out.println(type);
    System.out.println(mode);
    System.out.println(value);
  }

  public void internalEntityDecl(String name, String value) throws SAXException {
    System.out.println(name);
    System.out.println(value);
  }

  public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    System.out.println(name);
    System.out.println(publicId);
    System.out.println(systemId);
  }
}
