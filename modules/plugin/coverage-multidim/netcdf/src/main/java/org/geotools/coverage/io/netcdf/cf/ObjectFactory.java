/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference
// Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2015.03.04 at 12:42:54 PM CET
//

package org.geotools.coverage.io.netcdf.cf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the org.geotools.coverageio.netcdf.cf package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _EntryId_QNAME = new QName("", "entry_id");
    private static final QName _LastModified_QNAME = new QName("", "last_modified");
    private static final QName _CanonicalUnits_QNAME = new QName("", "canonical_units");
    private static final QName _Description_QNAME = new QName("", "description");
    private static final QName _VersionNumber_QNAME = new QName("", "version_number");
    private static final QName _Amip_QNAME = new QName("", "amip");
    private static final QName _Grib_QNAME = new QName("", "grib");
    private static final QName _Contact_QNAME = new QName("", "contact");
    private static final QName _Institution_QNAME = new QName("", "institution");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: org.geotools.coverageio.netcdf.cf
     */
    public ObjectFactory() {}

    /** Create an instance of {@link Alias } */
    public Alias createAlias() {
        return new Alias();
    }

    /** Create an instance of {@link Entry } */
    public Entry createEntry() {
        return new Entry();
    }

    /** Create an instance of {@link StandardNameTable } */
    public StandardNameTable createStandardNameTable() {
        return new StandardNameTable();
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}} */
    @XmlElementDecl(namespace = "", name = "entry_id")
    @XmlIDREF
    public JAXBElement<Object> createEntryId(Object value) {
        return new JAXBElement<>(_EntryId_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "last_modified")
    public JAXBElement<XMLGregorianCalendar> createLastModified(XMLGregorianCalendar value) {
        return new JAXBElement<>(_LastModified_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "canonical_units")
    public JAXBElement<String> createCanonicalUnits(String value) {
        return new JAXBElement<>(_CanonicalUnits_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<>(_Description_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "version_number")
    public JAXBElement<String> createVersionNumber(String value) {
        return new JAXBElement<>(_VersionNumber_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "amip")
    public JAXBElement<String> createAmip(String value) {
        return new JAXBElement<>(_Amip_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "grib")
    public JAXBElement<String> createGrib(String value) {
        return new JAXBElement<>(_Grib_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "contact")
    public JAXBElement<String> createContact(String value) {
        return new JAXBElement<>(_Contact_QNAME, String.class, null, value);
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "institution")
    public JAXBElement<String> createInstitution(String value) {
        return new JAXBElement<>(_Institution_QNAME, String.class, null, value);
    }
}
