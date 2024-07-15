/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl;

import javax.xml.namespace.QName;
import org.geotools.xsd.Binding;
import org.geotools.xsd.BindingFactory;
import org.picocontainer.MutablePicoContainer;

public class BindingFactoryImpl implements BindingFactory {
    MutablePicoContainer context;
    BindingLoader loader;

    public BindingFactoryImpl(BindingLoader loader) {
        this.loader = loader;
    }

    @Override
    public Binding createBinding(QName name) {
        return loader.loadBinding(name, context);
    }

    public void setContext(MutablePicoContainer context) {
        this.context = context;
    }
}
