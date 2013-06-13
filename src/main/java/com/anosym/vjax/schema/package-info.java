/**
 * The marshalling of java classes to xml schemas.
 * <p>
 * <b>Java Mapping to XML mapping</b>
 * <p>
 * The java primitives all map to their equivalent simple types in xml built in type.
 * The mapping of xml built-in type that are normally objects in java are mapped in two manners:
 * <ol>
 * <li>If the property/class is annotated with the Converter annotation, then the specified java object will be mapped to in built xml simple type</li>
 * <li>If the class/property is annotated with Alias annotation, the normall class xml schema generation will follow and there will be no mapping
 * between the class and its equivalent xml built in simple type</li>
 * </ol>
 * </p>
 * </p>
 * <p>
 * The following namespace prefixes are not to be used in declaring namespace prefixes
 * <ol>
 * <li>"xsd", for "http://www.w3.org/2001/XMLSchema"</li>
 * <li>"xi", for "http://www.w3.org/2001/XInclude"</li>
 * <li>"xsi", for "http://www.w3.org/2001/XMLSchema-instance"</li>
 * <li>"vjax", for "http://www.flemax.com/2011/vjax"</li>
 * </ol>
 * An exception will be raised if encountered in any schema definition
 * </p>
 * <p>
 * Two classes in the same package can be annotated with the schema annotation with the same uri value left the same or default.
 * What this implies is that the application will generate schema files for both instance and references the other member element definition
 * by the use of xml include syntax.
 * </p>
 * <p>
 * If the two classes have been annotated with the schema annotation which specify different namespace uri, and one of the schemas
 * requires the other, then their namespace prefices must be different or left to default
 * </p>
 * <p>
 * In defining the name of a new complexType or simpleType, the marshaller simply uses the unchanged simple name of the class
 * </p>
 * <p>
 * If annotation is defined on a property/field, then it will override similar annotation defined at the class
 * Level. however, if the overriden annotation definition at property/field does not affect the child elements,
 * while the class definition affects the child elements, the children will be processed as if there was not
 * overriden implication
 * </p>
 * <p>
 * When access type has been specified as method property, the marshaller will only consider, public methods that conform
 * to the java specification, unless otherwise annotated differently based on the marshaller annotation schemes
 * </p>
 * <p>
 * when field property has been specified, the following criteria is used.
 * <ol>
 * <li>All static fields are ignored</li>
 * <li>All final fields are ignored</li>
 * <li>All transient fields are ignored</li>
 * <li>Any field declared in the in any one of the interfaces the class implements will be ignored</li>
 * <li>If the class/member property of the class being marshalled restricts the fields by annotating itself with
 * {@link com.variance.vjax.annotations.SuppressSuperClass} then the super class fields will not be considered</li>
 * <li>The fields may lack information on the actual type of the class it is assigned to. The marshaller will always specify at least
 * one implementing class unless the field value is null, in which case the marshaller will not be able to determine a concrete type
 * for the abstract or interface specified on the field</li>
 * </ol>
 * </p>
 * <p>
 * By definition, it is impossible to do a object derivation in java by restriction. The java specification states that
 * sub-classes cannot alter property visbility or access modifiers in anyway.
 * However, to manage derivation by restriction, developers can decide to annotate overriden method properties
 * with the restriction annotation or pattern annotation to achieve the same effect
 * </p>
 */
package com.anosym.vjax.schema;
