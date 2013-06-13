/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

import com.anosym.vjax.VMarshallerConstants;
import com.anosym.vjax.util.VConditional;
import com.anosym.vjax.util.VModifier;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some methods depend on their parents. To ensure that appropriate parentage hierarchy is
 * maintained on behaviour, a parent should add a child only if it belongs to an existing hierarchy.
 * the exception is when the parent is the root element. However, the hierarchy may not be disrupted
 * but some consistent namespace behaviours may not be guranteed
 *
 * @author Marembo
 */
public class VElement implements Cloneable, Serializable {

  /**
   * When characters that are reserved in the xml specification appears on an element tag, then the
   * specified characters will be replaced by an underscore and then an attribute will be
   */
  public static final String ESCAPE_SEQUENCE = "escapeSequence";
  private static int elementIds = 0;
  /**
   * A unique id of the element that should identify it among the rest
   */
  private Long id = System.currentTimeMillis();
  private String markup;
  private String content;
  private List<VAttribute> attributes;
  private Map<Integer, VElement> children;
  private boolean empty;
  private VElement parent;
  private String comment;
  private transient int indexing = 0;
  private VNamespace associatedNamespace;
  private int elementId;
  /**
   * We use this property to determine whether to render this element or not
   */
  private transient boolean renderable = true;

  public VElement(String markup, VElement parent) {
    this(markup);
    this.SetParent(parent);
  }

  public VElement(String markup) {
    elementId = elementIds++;
    this.attributes = new ArrayList<VAttribute>();
    this.SetTag(markup);
    this.content = null;
    this.children = new TreeMap<Integer, VElement>();
    this.parent = null;
    this.empty = true;
  }

  private VElement(VElement elem) {
    this(elem.markup);
    this.content = elem.content;
    this.empty = elem.empty;
    this.comment = elem.comment;
    this.parent = elem.parent;
    this.indexing = elem.indexing;
    if (parent != null) {
      parent.addChild(this);
    }
    if (elem.getAssociatedNamespace() != null) {
      this.associatedNamespace = new VNamespace(elem.getAssociatedNamespace().getPrefix(), elem.getAssociatedNamespace().getValue());
    }
    for (VAttribute vv : elem.attributes) {
      addAttribute(new VAttribute(vv.getName(), vv.getValue(), vv.getAttributeNamespace()));
    }
    VAttribute refId = getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
    if (refId != null) {
      refId.setValue("" + (System.currentTimeMillis() & 0xffffff));
    }
    for (VElement el : elem.getChildren()) {
      try {
        VElement e = el.clone();
        addChild(e);
      } catch (CloneNotSupportedException ex) {
        Logger.getLogger(VElement.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private VElement(VElement elem, VElement parent) {
    this(elem.markup);
    this.content = elem.content;
    this.empty = elem.empty;
    this.comment = elem.comment;
    this.parent = parent;
    this.indexing = elem.indexing;
    if (parent != null) {
      parent.addChild(this);
    }
    if (elem.getAssociatedNamespace() != null) {
      this.associatedNamespace = new VNamespace(elem.getAssociatedNamespace().getPrefix(), elem.getAssociatedNamespace().getValue());
    }
    for (VAttribute vv : elem.attributes) {
      addAttribute(new VAttribute(vv.getName(), vv.getValue(), vv.getAttributeNamespace()));
    }
    VAttribute refId = getAttribute(VMarshallerConstants.OBJECT_REFERENCE_ID_ATTRIBUTE);
    if (refId != null) {
      refId.setValue("" + (System.currentTimeMillis() & 0xffffff));
    }
    for (VElement el : elem.getChildren()) {
      new VElement(el, this);
    }
  }

  /**
   * Adds a child to this element if this element is an array or a collection.
   *
   * @return
   */
  public VElement addSimilarChild() {
    try {
      boolean canAddChild = booleanAttributeValue(VMarshallerConstants.COLLECTION_ATTRIBUTE);
      if (!canAddChild && booleanAttributeValue(VMarshallerConstants.ARRAY_ATTRIBUTE)) {
        VAttribute at = getAttribute(VMarshallerConstants.ARRAY_LENGTH_ATTRIBUTE);
        if (at != null) {
          String value = at.getValue();
          if (value != null) {
            int len = Integer.parseInt(value);
            len++;
            at.setValue(len + "");
            canAddChild = true;
          }
        }
      }
      if (canAddChild) {
        //we can now safely add the child.
        VElement child = getChildren().get(0);
        VElement newElem = new VElement(child, this);
        if (newElem.hasChildren()) {
          for (VElement e : newElem.getChildren()) {
            e.setContent(null);
          }
        } else {
          newElem.setContent(null);
        }
        return newElem;
      }
    } catch (Exception ex) {
      Logger.getLogger(VElement.class.getName()).log(Level.SEVERE, "Adding Similar child to: " + toXmlString(), ex);
    }
    return null;
  }

  public Long getId() {
    return id;
  }

  /**
   * A unique integer that represents the id of the element in the current application context
   *
   * @return
   */
  public int getElementId() {
    return elementId;
  }

  public VElement getParent() {
    return parent;
  }

  public boolean isConstantValue() {
    VAttribute va = this.getAttribute(VMarshallerConstants.CONSTANT_ATTRIBUTE);
    return va != null && va.getBoolenValue();
  }

  public String getToString() {
    return toString();
  }

  public boolean isRenderable() {
    return renderable;
  }

  /**
   * Finds an element of the specified name and whose child element specified, has the content value
   * specified.
   *
   * @param name
   * @param childName
   * @param childContent
   * @return
   */
  public VElement findChild(String name, String childName, String childContent) {
    for (VElement elem : this.children.values()) {
      if (elem.getMarkup().equalsIgnoreCase(name)) {
        VElement child = elem.findChild(childName);
        if (child != null && child.getContent().equals(childContent)) {
          return elem;
        }
      } else if (elem.hasChildren()) {
        VElement e = elem.findChild(name, childName, childContent);
        if (e != null) {
          return e;
        }
      }
    }
    return null;
  }

  public void setRenderable(boolean renderable) {
    this.renderable = renderable;
  }

  /**
   * Adds a namespace definition to the namespaces defined by this element
   *
   * @param namespace
   */
  public void addNamespace(VNamespace namespace) {
    //must be added at the begining of the list
    //be sure that the namespace has right synatx
    VXMLVerifier.verifyNamespace(namespace);
    //ensure that we do not have namespaces already declared
    if (!attributes.contains(namespace)) {
      attributes.add(0, namespace);
    }
  }

  /**
   * Adds a namespace definition to the namespaces defined by this element
   *
   * @param namespace
   */
  private void doAddNamespace(VNamespace namespace) {
    if (this == this.parent) {
      if (this.parent.parent != null) {
        this.parent.parent.doAddNamespace(namespace);
      } else {
        this.parent.addNamespace(namespace);
      }
    }
    if (this.parent != null) {
      this.parent.doAddNamespace(namespace);
    } else {
      addNamespace(namespace);
    }
  }

  /**
   * Returns the current namespace associated with this element If none is specified but the element
   * defines namespaces, the returned value will be the first encountered namespace in the
   * definition list, If no namespace definition is found the associated namespace is not defined a
   * defualt namespace is returned if this is enabled in the environment property
   * com.flemax.vjax.defualtnamespace, otherwise returns null
   * <pre>
   *      The returned value is searched as follows:
   *      1. If the element defines an associated namespace, that particular namespace is returned
   *      2. If the element defines namespaces, the first namespace encountered is returned
   *      3. If the parent defines an associated namespace, the parents associated namespace is returned
   *      4. If the system property VNamespace.DEFUALT_NAMESPACE_PROPERTY_BINDING is defined to true, the default vjax namespace is returned
   *      5. Otherwise null is returned
   * <pre>
   *
   * @return the current associated namespace
   */
  public VNamespace getAssociatedNamespace() {
    if (associatedNamespace != null) {
      return associatedNamespace;
    }
    List<VNamespace> nms = this.getDefinedNamespaces();
    if (!nms.isEmpty()) {
      return nms.get(0);
    }
    //check if the parent defines an associated namespace
    VNamespace pAssoc = (parent == null) ? null : parent.getAssociatedNamespace();
    if (pAssoc != null) {
      this.setAssociatedNamespace(pAssoc);
      return pAssoc;
    }
//    String prop = System.getProperty(VNamespace.DEFUALT_NAMESPACE_PROPERTY_BINDING, "false");
//    if (prop.toLowerCase().trim().equals("true")) {
//      //get the default namespace set is as associated to
//      VNamespace defNamespace = VNamespace.VJAX_NAMESPACE;
//      this.addNamespace(defNamespace);
//      this.setAssociatedNamespace(defNamespace);
//      return defNamespace;
//    }
    return null;
  }

  public void setAssociatedNamespace(VNamespace associatedNamespace) {
    //if it has parent that does define this namespace, ignore, otherwise it must be the one who defines the names
    if (!isNamespaceDefined(associatedNamespace)) {
      this.addNamespace(associatedNamespace);
    }
    this.associatedNamespace = associatedNamespace;
    if (this.associatedNamespace.isAttributeFormDefault()) {
      for (VAttribute atr : attributes) {
        if (!(atr instanceof VNamespace)) {
          atr.setAttributeNamespace(associatedNamespace);
        }
      }
    }
  }

  private boolean isNamespaceDefined(VNamespace namespace) {
    return this.getNamespaces().contains(namespace);
  }

  /**
   * Returns the namespace associated with the current elements. If the current elements does not
   * define namespace, its parent element is searched for any namespace definition, otherwise, the
   * current element overrides namespace definition of its parents
   * <pre>
   * <b>The returned list of namespaces cannot be used to remove namespaces from this element
   * It will be needless calling {@literal List.remove(java.lang.Object)} on the returned list</b>
   * </pre>
   *
   * @return
   */
  public List<VNamespace> getNamespaces() {
    List<VNamespace> namespaces = getDefinedNamespaces();
    if (namespaces.isEmpty() && this.parent != null) {
      return parent.getNamespaces();
    }
    return namespaces;
  }

  /**
   * Return namespaces defined only by this element
   *
   * @return
   */
  private List<VNamespace> getDefinedNamespaces() {
    List<VNamespace> namespaces = new ArrayList<VNamespace>();
    for (VAttribute att : attributes) {
      if (att instanceof VNamespace) {
        namespaces.add(((VNamespace) att));
      }
    }
    return namespaces;
  }

  /**
   * Returns true only if the namespace is defined by this element
   *
   * @param namespace
   * @return
   */
  public boolean definesNamespace(VNamespace namespace) {
    return getDefinedNamespaces().contains(namespace);
  }

  public void setParent(VElement parent) {
    //we are changing the parentage of this child, so we need to set it regardless.
    this.parent = parent;
  }

  public List<VAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<VAttribute> attributes) {
    this.attributes = attributes;
  }

  /**
   * This method returns the references to the children of this element It is noted that removing
   * any child from the returned list does not affect the children of this element
   *
   * @return
   */
  public List<VElement> getChildren() {
    return new ArrayList<VElement>(children.values());
  }

  public int childrenCount() {
    return children.size();
  }

  public void setChildren(List<VElement> children) {
    for (; this.indexing < children.size(); indexing++) {
      this.children.put(indexing, this);
    }
  }

  public boolean isParent(String childName) {
    try {
      return this.findChild(childName) != null;
    } catch (Exception e) {
      return false;
    }
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    if (content != null && content.trim().isEmpty()) {
      int debug = 0;
    }
    this.content = unescape(content);
    if (this.content != null) {
      this.removeAttribute(this.getAttribute(VMarshallerConstants.NULL_PROPERTY));
    }
  }

  public String getMarkup() {
    return markup;
  }

  /**
   * By defualt this method escapes characters that are not recommended for the xml
   * element/attribute tags
   *
   * @param markup
   */
  public void setMarkup(String markup) {
    SetTag(markup);
  }

  private void SetTag(String elementTag) {
    this.markup = elementTag.trim();
    //we must escape it with the correct syntax
    String escapeSequence = "";
    char c = this.markup.charAt(0);
    char[] ccs = this.markup.toCharArray();
    for (int i = 0; i < ccs.length; i++) {
      c = this.markup.charAt(i);
      if (!Character.isLetterOrDigit(c) && c != '_' && c != '-') {
        escapeSequence += (i + ":" + c + ";");
        ccs[i] = '%';
      }
    }
    if (!escapeSequence.isEmpty()) {
      //remove the last ;
      escapeSequence = escapeSequence.substring(0, escapeSequence.length() - 1);
      this.addAttribute(new VAttribute(ESCAPE_SEQUENCE, escapeSequence));
      String tag = new String(ccs);
      this.markup = tag.replaceAll("%", "_");
    }
  }

  public boolean isElementParent() {
    return this.hasChildren();
  }

  public boolean hasAttribute(String attributeId) {
    for (VAttribute a : attributes) {
      if (a.getName().equals(attributeId)) {
        return true;
      }
    }
    return false;
  }

  public boolean booleanAttributeValue(String attributeId) {
    for (VAttribute a : attributes) {
      if (a.getName().equals(attributeId)) {
        return a.getBoolenValue();
      }
    }
    return false;
  }

  /**
   * True if the element has children
   *
   * @return
   */
  public boolean isParent() {
    return this.hasChildren();
  }

  public boolean isEmpty() {
    return (this.content != null ? (this.content.isEmpty() && this.children.isEmpty()) : this.children.isEmpty());
  }

  public void addAttribute(VAttribute attr) {
    for (VAttribute va : attributes) {
      if (va.getName().equalsIgnoreCase(attr.getName())) {
        va.setValue(attr.getValue());
        return;
      }
    }
    if (!this.attributes.contains(attr)) {
      if (this.associatedNamespace != null) {
        attr.setAttributeNamespace(associatedNamespace);
      }
      this.attributes.add(attr);
    }
  }

  public void addAttribute(VAttribute attr, int position) {
    for (VAttribute va : attributes) {
      if (va.getName().equalsIgnoreCase(attr.getName())) {
        va.setValue(attr.getValue());
        return;
      }
    }
    if (!this.attributes.contains(attr)) {
      if (this.associatedNamespace != null) {
        attr.setAttributeNamespace(associatedNamespace);
      }
      this.attributes.add(position, attr);
    }
  }

  private void SetParent(VElement parent) {
    parent.doAddChild(this);
    this.setParent(parent);
  }

  /**
   * Returns the index of the child If the child does not exist, returns -1 This will in most cases
   * return the first child encountered, if this element allows duplicate
   *
   * @param child
   * @return
   */
  protected int getChildIndex(VElement child) {
    if (children.containsValue(child)) {
      for (int i : children.keySet()) {
        if (children.get(i).equals(child)) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Adds the child element within the specified namespace defined by the parent If the namespace
   * does not exist, it will be added to the parent. The definition of the namespace will be at the
   * top most parent. If the child equals the parent (this==child) the child is simply associated
   * with the namespace and the method returns.
   *
   * @param child the child element to add
   * @param childNamespace the namespace to associate with the child element
   */
  public void addChild(VElement child, VNamespace childNamespace) {
    if (this == child) {
      child.setAssociatedNamespace(childNamespace);
    } else {
      this.addChild(child);
      this.doAddNamespace(childNamespace);
      child.setAssociatedNamespace(childNamespace);
    }
  }

  public void addChild(VElement child) {
    doAddChild(child);
    child.setParent(this);
  }

  public final void doAddChild(VElement child) {
    while (this.children.containsKey(indexing)) {
      //then copy the value
      indexing++;
    }
    if (!this.children.containsValue(child)) {
      this.children.put(indexing, child);
      indexing++;
    }
  }

  public final void addChild(VElement child, int index) {
    //does it exists
    VElement val = children.get(index);
    children.put(index, child);
    if (val != null) {
      addChild(val, index + 1);
    }
    child.setParent(this);
  }

  public void addChildAsFirst(VElement child) {
    this.addChild(child, 0);
  }

  /**
   * Returns an immediate child of this element
   *
   * @param name
   * @return
   */
  public VElement findImmediateChild(String name) {
    for (VElement elem : this.children.values()) {
      if (elem.getMarkup().equalsIgnoreCase(name)) {
        return elem;
      }
    }
    return null;
  }

  /**
   * Returns an immediate child of this element
   *
   * @param name
   * @return
   */
  public VElement findImmediateChild(String name, VAttribute at) {
    for (VElement elem : this.children.values()) {
      if (elem.getMarkup().equalsIgnoreCase(name)) {
        List<VAttribute> atts = elem.getAttributes();
        if (atts.contains(at)) {
          return elem;
        }
      }
    }
    return null;
  }

  public boolean hasAttributes() {
    return !this.attributes.isEmpty();
  }

  public boolean hasChildren() {
    return this.children == null || !this.children.isEmpty();
  }

  /**
   * Returns any child, whether grandchild or not of this element
   *
   * @param name the name tag of the element
   * @return null if no child is found with the specified name
   */
  public VElement findChild(String name) {
    for (VElement elem : this.children.values()) {
      if (elem.getMarkup().equalsIgnoreCase(name)) {
        return elem;
      } else if (elem.hasChildren()) {
        VElement e = elem.findChild(name);
        if (e != null) {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Finds the index of the content by regular expression. If this method finds two element that are
   * equal, only one will be returned.
   *
   * @param contentRegex
   * @return
   */
  public List<VElement> findChildrenByContentRegex(String contentRegex) {
    Pattern p = Pattern.compile(contentRegex);
    final List<VElement> foundElements = new ArrayList<VElement>() {
      @Override
      public boolean add(VElement e) {
        if (!contains(e)) {
          return super.add(e);
        }
        return false;
      }
    };
    for (VElement ve : children.values()) {
      if (contentRegex == null) {
        if (ve.getContent() == null) {
          foundElements.add(ve);
        }
        continue;
      }
      Matcher m = p.matcher(ve.getContent());
      if (m.find()) {
        foundElements.add(ve);
      }
    }

    return foundElements;
  }

//  public VElement findNextChild(Integer index){
//
//  }
  /**
   * Returns any child, whether grandchild or not of this element
   *
   * @param name
   * @return
   */
  public VElement findChild(String name, VAttribute at) {
    for (VElement elem : this.children.values()) {
      if (elem.getMarkup().equalsIgnoreCase(name)) {
        List<VAttribute> atts = elem.getAttributes();
        if (atts.contains(at)) {
          return elem;
        }
      } else if (elem.hasChildren()) {
        VElement e = elem.findChild(name, at);
        if (e != null) {
          return e;
        }
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final VElement other = (VElement) obj;
    if ((this.markup == null) ? (other.markup != null) : !this.markup.equals(other.markup)) {
      return false;
    }
    if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
      return false;
    }
    if (this.attributes != other.attributes && (this.attributes == null || !this.attributes.equals(other.attributes))) {
      return false;
    }
    if (this.children != other.children && (this.children == null || !this.children.equals(other.children))) {
      return false;
    }
    if (this.empty != other.empty) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 83 * hash + (this.markup != null ? this.markup.hashCode() : 0);
    hash = 83 * hash + (this.content != null ? this.content.hashCode() : 0);
    hash = 83 * hash + (this.attributes != null ? this.attributes.hashCode() : 0);
    hash = 83 * hash + (this.children != null ? this.children.hashCode() : 0);
    hash = 83 * hash + (this.empty ? 1 : 0);
    return hash;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = unescape(comment);
  }

  private static String escape(String str) {
    str = str.replace("&", "&amp;");
    str = str.replace("<", "&lt;");
    str = str.replace(">", "&gt;");
    str = str.replace("'", "&apos;");
    str = str.replace("\"", "&quot;");
    return str;
  }

  private static String unescape(String str) {
    str = str.replace("&amp;", "&");
    str = str.replace("&alt;", "<");
    str = str.replace("&gt;", ">");
    str = str.replace("&apos;", "'");
    str = str.replace("&quot;", "\"");
    return str;
  }

  private static String toString(VElement element, int tab) {
    String str = "";
    if (element instanceof VContent) {
      //we have content simply return the value
      String cont = element.toString();
      //replace all new lines with tab, otherwise we will have very bad omen.
      String tab_ = gTab(tab);
      cont = cont.replaceAll("\n", tab_ + "\n");
      return cont;
    }
    //write comments if we have
    if (element.comment != null) {
      String vTab = VElement.gTab(tab);
      String[] comments = element.getComment().split("\n");
      if (comments.length > 1) {
        for (String s : comments) {
          str += "\n";
          str += vTab;
          str += "<!--";
          str += escape(s);
          str += "-->";
        }
      } else {
        str += "\n";
        str += vTab;
        str += "<!--";
        str += escape(element.comment);
        str += "-->";
      }
    }
    //write markup
    if (element.markup == null) {
      throw new IllegalArgumentException("Markup is null");
    }
    str += "\n";
    str += VElement.gTab(tab);
    str += "<";
    //do we have a namespace
    VNamespace nm = element.getAssociatedNamespace();
    if (nm != null && nm.isElementFormDefault()) {
      str += nm.getPrefix() + ":" + element.markup;
    } else {
      str += element.markup;
    }
    if (element.definesNamespace(nm) && nm.isIncludeFormDefaultAttributeOnFalse()) {
      //then add the attributes of namespace
      VAttribute elemForm = nm.getElementFormDefaultAttribute();
      VAttribute attrForm = nm.getAttributeFormDefaultAttribute();
      str += " ";
      str += elemForm;
      str += " ";
      str += attrForm;
    }
    if (element.hasAttributes()) {
      for (VAttribute a : element.attributes) {
        str += " ";
        str += a;
      }
    }
    if (element.isEmpty()) {
      str += "/>";
    } else {
      //close mark
      str += ">";
      //then write contents and children
      if (element.content != null) {
        String elem_content = element.content;
        if (element.hasChildren()) {
          elem_content = "\n" + elem_content;
        }
        String[] conts = elem_content.split("\n");
        //tabify it appropriately
        if (conts.length > 1) {
          String vTab = VElement.gTab(tab);
          str += "\n  " + vTab;
          for (String s : conts) {
            str += escape(s);
            str += "\n";
            str += (vTab + "  ");
          }
          //remove the last two spaces
          str = str.substring(0, str.length() - 2);
        } else {
          str += escape(elem_content);
        }
      }
      if (element.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(element.children.keySet());
        for (int indx : keys) {
          VElement elem = element.children.get(indx);
          str += toString(elem, tab + 1);
        }
        str += "\n";
        str += VElement.gTab(tab);
      }
      str += "</";
      if (nm != null && nm.isElementFormDefault()) {
        str += nm.getPrefix() + ":" + element.markup;
      } else {
        str += element.markup;
      }
      str += ">";
    }
    return str;
  }

  protected static String gTab(int tabs) {
    String s = "";
    for (int i = 0; i < tabs; i++) {
      s += "   ";
    }
    return s;
  }

  @Override
  public String toString() {
    String mk = this.getMarkup();
    String str = (mk.charAt(0) + "").toUpperCase();
    boolean isDigit = false;
    boolean cap = false;
    for (int i = 1; i < mk.length(); i++) {
      if (Character.isDigit(mk.charAt(i))) {
        if (!isDigit) {
          str += " ";
          isDigit = true;
        }
      } else if ((mk.charAt(i) + "").toUpperCase().equals(mk.charAt(i) + "")) {
        if (!cap && i != 1) {
          str += " ";
          cap = true;
        }
      } else {
        isDigit = false;
        cap = false;
      }

      str += mk.charAt(i);
    }
    return str;
  }

  String elemString() {
    return VElement.toString(this, 0);
  }

  public String toXmlString() {
    return elemString();
  }

  public VAttribute getAttribute(String string) {
    for (VAttribute a : this.attributes) {
      if (a.getName().equalsIgnoreCase(string)) {
        return a;
      }
    }
    return null;
  }

  /**
   * Looks the entire ancestry to find the element and remove it. The comparison here is object
   * reference equality, that is it must be the same object.
   *
   * @param elem
   */
  public void removeChild(VElement elem) {
    if (elem != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        for (Integer i : keys) {
          VElement c = children.get(i);
          if (c == elem) {
            children.remove(i);
            elem.parent = null; //set parent to null
            //if this key is the last indexing, decrease it.
            if (i == (indexing - 1)) {
              indexing--;
            }
            return;
          } else {
            c.removeChild(elem);
          }
        }
      }
    }
  }

  /**
   * Removes only the first hierarchy child, without traversing the inner ancestry tree.
   *
   * @param elem
   */
  public void removeImmediateChild(VElement elem) {
    if (elem != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        for (Integer i : keys) {
          VElement c = children.get(i);
          if (c == elem) {
            children.remove(i);
            elem.parent = null; //set parent to null
            return;
          }
        }
      }
    }
  }

  /**
   * Removes the current elements children that meets the specified condition.
   *
   * @param conditional
   */
  public void removeChildren(VConditional<VElement> conditional) {
    if (conditional != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        for (Integer i : keys) {
          VElement c = children.get(i);
          if (conditional.accept(c)) {
            children.remove(i);
          }
        }
      }
    }
  }

  /**
   * Removes all element in the ancestry tree that meets the condition. The elements will be
   * recursively traversed. If condition returns false, the system stops.
   *
   * @param conditional
   */
  public void removeAncestry(VConditional<VElement> conditional) {
    if (conditional != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        for (Integer i : keys) {
          VElement c = children.get(i);
          if (c != null) {
            if (c.hasChildren()) {
              c.removeAncestry(conditional);
            }
            if (conditional.accept(c)) {
              children.remove(i);
            } else {
              return;
            }
          }
        }
      }
    }
  }

  /**
   * Removes all element in the ancestry tree that meets the condition. The elements will be
   * recursively traversed. The systems only if {@link VPredicate#stopOnAccept() } is true and at
   * least one value has been accepted.
   *
   * @param conditional
   */
  public void removeAncestry(VPredicate<VElement> predicate) {
    if (this.hasChildren()) {
      List<Integer> keys = new ArrayList<Integer>(children.keySet());
      for (Integer i : keys) {
        VElement c = children.get(i);
        if (c != null) {
          if (c.hasChildren()) {
            c.removeAncestry(predicate);
          }
          if (predicate.accept(c)) {
            children.remove(i);
            if (predicate.stopOnAccept()) {
              return;
            }
          }
        }
      }
    }
  }

  /**
   * Iterates through the entire ancestry of this element, calling the condition on each element
   * only if the child has no children.
   *
   * @param conditional
   */
  public void findChildlessAncestry(VConditional<VElement> conditional) {
    if (this.hasChildren()) {
      for (Integer key : children.keySet()) {
        VElement elem = children.get(key);
        if (elem.hasChildren()) {
          elem.findChildlessAncestry(conditional);
        } else {
          conditional.accept(elem);
        }
      }
    }
  }

  /**
   * We modify the structure of this element completely based on the following rules. If the child
   * element is accepted by the condition, then all its attributes are transferred to the parent,
   * and all its content, appended to the parent, and then it is removed from the parent.
   *
   * @param conditional
   */
  public void modifyAncestry(VConditional<VElement> conditional) {
    if (conditional != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        String childrenContent = "";
        for (Integer i : keys) {
          VElement c = children.get(i);
          /**
           * We modify the current element if it has children before passing it to the condition,
           */
          if (c.hasChildren()) {
            c.modifyAncestry(conditional);
          }
          if (conditional.accept(c)) {
            removeImmediateChild(c);
            this.getAttributes().addAll(c.getAttributes());
            childrenContent += (" " + c.getContent() != null ? c.getContent().trim() : "");
          }
        }
        if (childrenContent != null && !childrenContent.trim().isEmpty()) {
          String currentContent = this.getContent();
          if (currentContent != null) {
            currentContent = childrenContent + currentContent;
          }
          setContent(currentContent);
        }
      }
    }
    if (getContent() != null) {
      this.setContent(getContent().trim());
    }
  }

  /**
   * Modifies the immediate child of this element, according to the modifier specified.
   *
   * @param conditional
   */
  public void modifyChildren(VModifier<VElement> modifier) {
    if (modifier != null) {
      if (this.hasChildren()) {
        List<Integer> keys = new ArrayList<Integer>(children.keySet());
        for (Integer i : keys) {
          VElement c = children.get(i);
          if (modifier.canModify(c)) {
            modifier.modify(c);
          }
        }
      }
    }
  }

  public void removeChild(String name) {
    removeChild(getChild(name));
  }

  public void removeAttribute(VAttribute a) {
    this.attributes.remove(a);
  }

  /**
   * Returns the child element with parent hierarchy specified by the markups if no markup is
   * specified, returns this element. The first markup in the array represents a child of this
   * element, and subsequent markups represents a child of its parent.
   *
   * @param markups
   * @return
   */
  public VElement getChild(String... markups) {
    if (markups == null || markups.length == 0) {
      return this;
    }
    String str = markups[0];
    VElement elem = new VElement(str);
    for (VElement ve : this.children.values()) {
      if (ve.getMarkup().equals(elem.getMarkup())) {
        if (markups.length == 1) {
          return ve;
        }
        if (ve.hasChildren()) {
          String[] nextMarkups = new String[markups.length - 1];
          System.arraycopy(markups, 1, nextMarkups, 0, nextMarkups.length);
          return ve.getChild(nextMarkups);
        }
      }
    }
    return null;
  }

  /**
   * Returns the child element with parent hierarchy specified by the markups if no markup is
   * specified, returns this element. The first markup in the array represents a child of this
   * element, and subsequent markups represents a child of its parent.
   *
   * @param markups
   * @return
   */
  public VElement getChild(VConditional<VElement> conditional, String... markups) {
    if (markups == null || markups.length == 0) {
      return this;
    }
    String str = markups[0];
    VElement elem = new VElement(str);
    for (VElement ve : this.children.values()) {
      if (ve.getMarkup().equals(elem.getMarkup())) {
        if (markups.length == 1) {
          if (conditional.accept(ve)) {
            return ve;
          }
        } else if (ve.hasChildren()) {
          String[] nextMarkups = new String[markups.length - 1];
          System.arraycopy(markups, 1, nextMarkups, 0, nextMarkups.length);
          return ve.getChild(nextMarkups);
        }
      }
    }
    return null;
  }

  /**
   * Returns the child element with parent hierarchy specified by the markups if no markup is
   * specified, returns this element. The first markup in the array represents a child of this
   * element, and subsequent markups represents a child of its parent.
   *
   * @param markups
   * @return
   */
  public VElement getChild(boolean mustHaveNoChildren, String... markups) {
    if (markups == null || markups.length == 0) {
      return this;
    }
    String str = markups[0];
    VElement elem = new VElement(str);
    for (VElement ve : this.children.values()) {
      if (ve.getMarkup().equals(elem.getMarkup())) {
        if (markups.length == 1 && mustHaveNoChildren && !ve.hasChildren()) {
          return ve;
        } else if (ve.hasChildren()) {
          String[] nextMarkups = new String[markups.length - 1];
          System.arraycopy(markups, 1, nextMarkups, 0, nextMarkups.length);
          return ve.getChild(nextMarkups);
        }
      }
    }
    return null;
  }

  /**
   * Returns the first child that has an attribute equal to the specified attribute
   *
   * @param at
   * @return
   */
  public VElement findChild(VAttribute at) {
    for (VElement ee : this.children.values()) {
      for (VAttribute a : ee.attributes) {
        if (a.equals(at)) {
          return ee;

        }
      }
    }
    throw new NoSuchElementException("No such child: Attribute=" + at);
  }

  @Override
  public VElement clone() throws CloneNotSupportedException {
    VElement elem = (VElement) super.clone();
    return new VElement(elem);
  }

  public boolean hasChild(String markup) {
    for (VElement e : getChildren()) {
      if (e.getMarkup().equals(markup)) {
        return true;
      }
    }
    return false;
  }

  public List<VElement> getChildren(String markup) {
    List<VElement> el = getChildren();
    for (ListIterator<VElement> it = el.listIterator(); it.hasNext();) {
      VElement e = it.next();
      if (!e.getMarkup().equals(markup)) {
        it.remove();
      }
    }
    return el;
  }

  /**
   * Get children based on the specified condition.
   *
   * @param markup
   * @param conditional
   * @return
   */
  public List<VElement> getChildren(String markup, VConditional<VElement> conditional) {
    List<VElement> el = getChildren();
    for (ListIterator<VElement> it = el.listIterator(); it.hasNext();) {
      VElement e = it.next();
      if (!e.getMarkup().equals(markup) || !conditional.accept(e)) {
        it.remove();
      }
    }
    return el;
  }

  public List<VElement> getChildren(String markup, String childMarkup) {
    List<VElement> el = getChildren();
    for (ListIterator<VElement> it = el.listIterator(); it.hasNext();) {
      VElement e = it.next();
      if (!e.getMarkup().equals(markup)) {
        it.remove();
      } else {
        try {
          VElement c = e.getChild(childMarkup);
          if (c == null) {
            it.remove();
          }
        } catch (Exception ex) {
          it.remove();
        }
      }
    }
    return el;
  }

  public List<VElement> getChildren(String markup, VAttribute a) {
    List<VElement> el = getChildren();
    for (ListIterator<VElement> it = el.listIterator(); it.hasNext();) {
      VElement e = it.next();
      if (!e.getMarkup().equals(markup)) {
        it.remove();
      } else {
        try {
          if (!e.hasAttributes()) {
            it.remove();
          } else {
            if (!e.getAttributes().contains(a)) {
              it.remove();
            }
          }
        } catch (Exception ex) {
          it.remove();
        }
      }
    }
    return el;
  }

  public VElement getChild(String name, int index) {
    if (index >= childrenCount()) {
      throw new IndexOutOfBoundsException("No such element at index: " + index);
    }
    VElement e = new VElement(name);
    int i = 0;
    for (VElement ee : this.children.values()) {
      boolean equals = e.getMarkup().equals(ee.getMarkup());
      if (equals && i == index) {
        return ee;
      }
      i++;
    }
    throw new NoSuchElementException("No such child: " + name + " at index: " + index);
  }

  /**
   * Searched, from the end, the child of this element that meets the specified criteria.
   *
   * @param conditional
   * @return
   */
  public VElement getLastChild(VConditional<VElement> conditional) {
    List<Integer> keys = new ArrayList<Integer>(children.keySet());
    for (ListIterator<Integer> it = keys.listIterator(keys.size()); it.hasPrevious();) {
      Integer key = it.previous();
      VElement elem = children.get(key);
      if (conditional.accept(elem)) {
        return elem;
      }
    }
    return null;
  }

  /**
   * Returns the last child in the hierarchy, with the specified markup.
   *
   * @param conditional
   * @return
   */
  public VElement getLastChild(String markup) {
    List<Integer> keys = new ArrayList<Integer>(children.keySet());
    for (ListIterator<Integer> it = keys.listIterator(keys.size()); it.hasPrevious();) {
      Integer key = it.previous();
      VElement elem = children.get(key);
      if (elem.getMarkup().equalsIgnoreCase(markup)) {
        return elem;
      }
    }
    return null;
  }

  /**
   * Returns a list of child element which is sandwitched between two elements that satisfy the
   * start and the end conditions. By default, the system looks from the beginning. Thats is, the
   * start condition will be evaluated before the end condition. If the boolean values are true, the
   * start and end element will be included.
   *
   * @param startConditional
   * @param endcoConditional
   * @return
   */
  public List<VElement> findChildren(VConditional<VElement> startConditional, VConditional<VElement> endConditional, boolean addStart, boolean addEnd) {
    List<Integer> keys = new ArrayList<Integer>(children.keySet());
    List<VElement> swChildren = new ArrayList<VElement>();
    boolean startFound = false;
    for (ListIterator<Integer> it = keys.listIterator(); it.hasNext();) {
      Integer i = it.next();
      VElement elem = children.get(i);
      if (startFound) {
        if (endConditional.accept(elem)) {
          if (addEnd) {
            swChildren.add(elem);
          }
          break;
        }
        swChildren.add(elem);
      } else {
        startFound = startConditional.accept(elem);
        if (startFound && addStart) {
          swChildren.add(elem);
        }
      }
    }
    return swChildren;
  }

  /**
   * Returns a list of child element which is sandwitched between two elements that satisfy the
   * start and the end conditions. The search starts from the end, but the index of the element that
   * satisfy the start condition will always be less than the index of the element that satisfy the
   * end condition.
   *
   * @param startConditional
   * @param endcoConditional
   * @return
   */
  public List<VElement> findLastChildren(VConditional<VElement> startConditional, VConditional<VElement> endConditional) {
    List<Integer> keys = new ArrayList<Integer>(children.keySet());
    List<VElement> swChildren = new ArrayList<VElement>();
    boolean endFound = false;
    for (ListIterator<Integer> it = keys.listIterator(keys.size()); it.hasPrevious();) {
      Integer i = it.previous();
      VElement elem = children.get(i);
      if (endFound) {
        if (startConditional.accept(elem)) {
          break;
        }
        swChildren.add(0, elem);
      } else {
        endFound = endConditional.accept(elem);
      }
    }
    return swChildren;
  }

  /**
   * Returns a list of child element which is sandwitched between two elements that satisfy the
   * start and the end conditions. The search starts from the end, but the index of the element that
   * satisfy the start condition will always be less than the index of the element that satisfy the
   * end condition.
   *
   * @param startConditional
   * @param endcoConditional
   * @return
   */
  public List<VElement> findLastChildren(VConditional<VElement> startConditional,
          VConditional<VElement> endConditional, boolean addStart, boolean addEnd) {
    List<Integer> keys = new ArrayList<Integer>(children.keySet());
    List<VElement> swChildren = new ArrayList<VElement>();
    boolean endFound = false;
    for (ListIterator<Integer> it = keys.listIterator(keys.size()); it.hasPrevious();) {
      Integer i = it.previous();
      VElement elem = children.get(i);
      if (endFound) {
        if (startConditional.accept(elem)) {
          if (addStart) {
            swChildren.add(elem);
          }
          break;
        }
        swChildren.add(0, elem);
      } else {
        endFound = endConditional.accept(elem);
        if (endFound && addEnd) {
          swChildren.add(elem);
        }
      }
    }
    return swChildren;
  }

  /**
   * Removes all the specified children. This removes the same reference only.
   *
   * @param children
   */
  public void removeChildren(List<VElement> children) {
    for (VElement v : children) {
      List<Integer> keys = new ArrayList<Integer>(this.children.keySet());
      for (Integer i : keys) {
        VElement ve = this.children.get(i);
        if (ve == v) {
          this.children.remove(i);
          break;
        }
      }
    }
  }

  /**
   * Removes all the specified children. This removes children any child that meets the condition
   * first.
   *
   * @param children
   */
  public void removeAllChildren(List<VElement> children) {
    for (VElement v : children) {
      List<Integer> keys = new ArrayList<Integer>(this.children.keySet());
      for (Integer i : keys) {
        VElement ve = this.children.get(i);
        if (ve == v) {
          this.children.remove(i);
          break;
        }
      }
    }
  }

  /**
   * The method concatenates all contents of this element into a string. It works like this: If the
   * child content is an instance of a VContent, then its content method is returned, else if its an
   * element, its children contents are returned.
   *
   * @return
   */
  public String toContent() {
    String strContent = "";
    for (VElement c : getChildren()) {
      if (c instanceof VContent) {
        strContent += c.toString();
      } else {
        strContent += c.toContent();
      }
    }
    return strContent;
  }
}