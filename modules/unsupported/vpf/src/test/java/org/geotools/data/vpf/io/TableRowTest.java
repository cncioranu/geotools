/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * File is generated by 'Unit Tests Generator' developed under
 * 'Web Test Tools' project at http://sf.net/projects/wttools/
 * Copyright (C) 2001 "Artur Hefczyc" <kobit@users.sourceforge.net>
 * to all 'Web Test Tools' subprojects.
 *
 * No rights to files and no responsibility for code generated
 * by this tool are belonged to author of 'unittestsgen' utility.
 *
 */
package org.geotools.data.vpf.io;

import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.geotools.data.vpf.ifc.DataTypesDefinition;

/**
 * Test VPF Row access.
 *
 * @source $URL$
 */
public class TableRowTest extends TestCase implements DataTypesDefinition {
    /** Instance of tested class. */
    protected TableRow varTableRow;

    /** Public constructor for creating testing class. */
    public TableRowTest(String name) {
        super(name);
    } // end of TableRowTest(String name)

    /** This main method is used for run tests for this class only from command line. */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    } // end of main(Stringp[] args)

    public static final RowField[] TEST_FIELDS = {
        new RowField(Float.valueOf(1f), DATA_SHORT_FLOAT),
        new RowField(Short.valueOf((short) 2), DATA_SHORT_INTEGER),
        new RowField(new VPFDate("200301301149.00000"), DATA_DATE_TIME)
    };

    /**
     * This method is called every time before particular test execution. It creates new instance of
     * tested class and it can perform some more actions which are necessary for performs tests.
     */
    protected void setUp() {
        HashMap<String, RowField> map = new HashMap<>();
        map.put("first", TEST_FIELDS[0]);
        map.put("second", TEST_FIELDS[1]);
        map.put("third", TEST_FIELDS[2]);
        varTableRow = new TableRow(TEST_FIELDS, map);
    }

    /**
     * Returns all tests which should be performed for testing class. By default it returns only
     * name of testing class. Instance of this is then created with its constructor.
     */
    public static Test suite() {
        return new TestSuite(TableRowTest.class);
    } // end of suite()

    /** Method for testing original source method: int fieldsCount() from tested class */
    public void testFieldsCount() {
        assertEquals("Checking row size.", TEST_FIELDS.length, varTableRow.fieldsCount());
    } // end of testFieldsCount()

    /**
     * Method for testing original source method: org.geotools.vpf.RowField get(java.lang.String)
     * from tested class
     */
    public void testGet1195259493() {
        assertSame("Checking method get field by name.", TEST_FIELDS[0], varTableRow.get("first"));
        assertSame("Checking method get field by name.", TEST_FIELDS[1], varTableRow.get("second"));
        assertSame("Checking method get field by name.", TEST_FIELDS[2], varTableRow.get("third"));
    } // end of testGet1195259493(java.lang.String)

    /**
     * Method for testing original source method: org.geotools.vpf.RowField get(int) from tested
     * class
     */
    public void testGet104431() {
        assertSame("Checking method get field by index.", TEST_FIELDS[0], varTableRow.get(0));
        assertSame("Checking method get field by index.", TEST_FIELDS[1], varTableRow.get(1));
        assertSame("Checking method get field by index.", TEST_FIELDS[2], varTableRow.get(2));
    } // end of testGet104431(int)
} // end of TableRowTest
