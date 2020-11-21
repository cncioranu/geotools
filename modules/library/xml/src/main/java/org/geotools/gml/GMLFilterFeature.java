/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml;

import java.util.Vector;
import java.util.logging.Logger;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * LEVEL3 GML filter: translates JTS elements and attribute data into features.
 *
 * <p>This filter simply reads in the events and coordinates passed to it by its GMLFilterDocument
 * child and converts them into JTS objects. Note that it passes through anything not specifically
 * sent to it by GMLFilterDocument (i.e. more or less everything not in geometry.xsd). The parent of
 * this filter must implement GMLHandlerJTS in order to receive the JTS objects passed by this
 * filter.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class GMLFilterFeature extends XMLFilterImpl implements GMLHandlerJTS {
    // private FeatureSchema metadata = new FeatureSchema();
    // Static Globals to handle some expected elements

    /** The logger for the GML module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GMLFilterFeature.class);

    /** GML namespace string. */

    // private static final String GML_NAMESPACE = "http://www.opengis.net/gml";

    /** Handler for the JTS elements generated by this filter. */
    private GMLHandlerFeature parent;

    // private boolean defaultGeom = false;

    /** Factory for the JTS geometries. */

    // private Feature currentFeature; // = new FeatureFlat();

    /** Stores current feature attributes. */
    private Vector<Object> attributes = new Vector<>();

    private Vector<String> attributeNames = new Vector<>();
    private String fid = null;

    /** Stores current feature attributes. */
    private boolean insideAttribute = false;

    /** Stores current feature attributes. */
    private boolean insideFeature = false;

    /** Stores current feature attributes. */

    // private boolean insideGeometry = false;

    /** Stores current feature attributes. */
    private Object tempValue = null;

    private String attName = "";

    /** Some sort of feature name. */

    // private String FEATURE_MEMBER_NAME = "featureMember";
    private String typeName = "GenericFeature";

    /**
     * Collects string chunks in {@link #characters(char[], int, int)} callback to be handled at the
     * beggining of {@link #endElement(String, String, String)}
     */
    private StringBuffer characters = new StringBuffer();

    /**
     * Constructor with parent, which must implement GMLHandlerJTS.
     *
     * @param parent The parent of this filter.
     */
    public GMLFilterFeature(GMLHandlerFeature parent) {
        super();
        this.parent = parent;
    }

    public void setSchema(String uri) {}

    /**
     * Manages the start of a new main or sub geometry. This method looks at the status of the
     * current handler and either returns a new sub-handler (if the last one was successfully
     * returned already) or passes the element start notification along to the current handler as a
     * sub geometry notice.
     *
     * @param geometry The geometry from the child.
     */
    public void geometry(Geometry geometry) {
        // insideGeometry = true;
        // _log.debug("adding geometry with name "+attName);
        if (insideFeature) {
            if (attName.equals("")) {
                attributeNames.addElement("geometry");
            } else {
                attributeNames.addElement(attName);
            }

            attributes.addElement(geometry);
            endAttribute();

            // currentFeature.setGeometry(geometry);
        }
        // else {

        // parent.geometry(geometry);
        // }
    }

    /**
     * Checks for GML element start and - if not a coordinates element - sends it directly on down
     * the chain to the appropriate parent handler. If it is a coordinates (or coord) element, it
     * uses internal methods to set the current state of the coordinates reader appropriately.
     *
     * @param namespaceURI The namespace of the element.
     * @param localName The local name of the element.
     * @param qName The full name of the element, including namespace prefix.
     * @param atts The element attributes.
     * @throws SAXException Some parsing error occured while reading coordinates.
     * @task HACK:The method for determining if something is a feature or not is too crude.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        characters.setLength(0);

        if (localName.endsWith("Collection")) {
            // _log.debug("starting a collection with namespace " + NAMESPACE + " and Name " +
            // localName);
            return;
        }

        // if it ends with Member we'll assume it's a feature for the time being
        // nasty hack to fix members of multi lines and polygons
        if (isFeatureMember(localName)) {
            attributes = new Vector<>();
            attributeNames = new Vector<>();

            // currentFeature = new FeatureFlat();
            insideFeature = true;
            tempValue = null;

            // _log.debug("Starting a feature " + localName);
        } else if (insideFeature) {
            // _log.debug("inside feature " + localName);
            for (int i = 0; i < atts.getLength(); i++) {
                String name = atts.getLocalName(i);

                if (name.equalsIgnoreCase("fid")) {
                    // currentFeature.setTypeName(localName);
                    typeName = localName;

                    // _log.debug("set type name " + localName);
                    fid = atts.getValue(i);
                } else {
                    attributes.add(atts.getValue(i));
                    attributeNames.add(name);
                }
            }

            if (!typeName.equalsIgnoreCase(localName)) {
                if (attName.equals("")) {
                    // _log.debug("setting attName to " + localName);
                    attName = localName;
                } else {
                    // _log.debug("adding " + localName + " to " + attName);
                    attName = attName + "/" + localName;
                }

                // _log.debug("attName now equals " + attName);
            }

            insideAttribute = true;

            return;
        } else if (!insideAttribute) {
            parent.startElement(namespaceURI, localName, qName, atts);
        }
    }

    private boolean isFeatureMember(String localName) {
        return localName.endsWith("Member")
                && !localName.endsWith("StringMember")
                && !localName.endsWith("polygonMember")
                && !localName.endsWith("pointMember");
    }

    /**
     * Reads the only internal characters read by pure GML parsers, which are coordinates. These
     * coordinates are sent to the coordinates reader class which interprets them appropriately,
     * depending on the its current state.
     *
     * @param ch Raw coordinate string from the GML document.
     * @param start Beginning character position of raw coordinate string.
     * @param length Length of the character string.
     * @throws SAXException Some parsing error occurred while reading coordinates.
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }

    /** Handles the string chunks collected in {@link #characters}. */
    private void handleCharacters() throws SAXException {

        // the methods here read in both coordinates and coords and take the
        // grunt-work out of this task for geometry handlers.
        // See the documentation for CoordinatesReader to see what this entails
        String rawAttribute = characters.toString();
        characters.setLength(0);

        if (insideAttribute) {
            LOGGER.info("raw att = " + rawAttribute);
            if (tempValue instanceof StringBuffer) {
                ((StringBuffer) tempValue).append(" " + rawAttribute);
            } else {
                tempValue = new StringBuffer(rawAttribute);
            }
        } else {
            parent.characters(rawAttribute.toCharArray(), 0, rawAttribute.length());
        }
    }

    /**
     * Checks for GML element end and - if not a coordinates element - sends it directly on down the
     * chain to the appropriate parent handler. If it is a coordinates (or coord) element, it uses
     * internal methods to set the current state of the coordinates reader appropriately.
     *
     * @param namespaceURI Namespace of the element.
     * @param localName Local name of the element.
     * @param qName Full name of the element, including namespace prefix.
     * @throws SAXException Parsing error occurred while reading coordinates.
     */
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        handleCharacters();
        if (isFeatureMember(localName)) {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName(typeName);
            tb.setNamespaceURI(namespaceURI);

            //            AttributeType attDef[] = new AttributeTypeDefault[attributes.size()];
            //            for (int i = 0; i < attributes.size(); i++){
            //                attDef[i] = new AttributeTypeDefault((String)
            // attributeNames.get(i),attributes.get(i).getClass());
            //            }
            //            try {
            //                FeatureType schema =
            // FeatureTypeFactory.create(attDef).setTypeName(typeName);
            //                schema.setNamespace(namespaceURI);
            //                FlatFeatureFactory fac = new FlatFeatureFactory(schema);
            //                Feature feature = fac.create((Object []) attributes.toArray(),fid);
            //                //currentFeature.setAttributes((Object []) attributes.toArray());
            //                parent.feature(feature);
            //                //_log.debug("resetting attName at end of feature");
            //                attName = "";
            //            }
            //            catch (org.geotools.feature.SchemaException sve){
            //                //TODO: work out what to do in this case!
            //                //_log.error("Unable to create valid schema",sve);
            //            }
            //            catch (org.geotools.feature.IllegalFeatureException ife){
            //                //TODO: work out what to do in this case!
            //                //_log.error("Unable to build feature",ife);
            //            }
            //            insideFeature = false;
            for (int i = 0, ii = attributes.size(); i < ii; i++) {
                String name = (String) attributeNames.get(i);
                Class clazz = attributes.get(i).getClass();
                tb.add(name, clazz);
            }

            SimpleFeatureType featureType = tb.buildFeatureType();

            try {
                SimpleFeature feature = SimpleFeatureBuilder.build(featureType, attributes, fid);
                parent.feature(feature);
            } catch (IllegalAttributeException ife) {
                // TODO: work out what to do in this case!
                // _log.error("Unable to build feature",ife);
                // UNBELIEVABLE !!!!!!!!!!!!!!!!!!!!!!!!!!! - IanS
            }

            attName = "";
            insideFeature = false;
        } else if (insideAttribute) {
            // _log.debug("end - inside attribute [" + tempValue + "]");
            if ((tempValue != null) && !tempValue.toString().trim().equals("")) {
                if (tempValue instanceof StringBuffer) {
                    tempValue = tempValue.toString();
                }

                attributes.add(tempValue);
                attributeNames.add(attName);
            }

            endAttribute();
        } else {
            parent.endElement(namespaceURI, localName, qName);

            // _log.debug("end - inside feature");
            // insideFeature = false;
        }
    }

    /** Ends an attribute, by resetting the attribute name and setting insideAttribute to false. */
    private void endAttribute() {
        int index = attName.lastIndexOf('/');

        if (index > -1) {
            // _log.debug("removing " + attName.substring(index+1));
            attName = attName.substring(0, index);
        } else {
            attName = "";
        }

        // _log.debug("attName now equals " + attName);
        tempValue = null;
        insideAttribute = false;
    }
}
