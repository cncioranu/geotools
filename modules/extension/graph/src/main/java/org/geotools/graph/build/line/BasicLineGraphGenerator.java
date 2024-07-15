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
package org.geotools.graph.build.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.index.bintree.Bintree;
import org.locationtech.jts.index.bintree.Interval;

/**
 * An implementation of GraphGenerator used to generate a graph representing a line network. Graphs
 * are generated by supplying the generator with objects of type LineSegment via the add(Object)
 * method. <br>
 * <br>
 * For each line segment added, an edge in the graph is created. The builder records the end
 * coordinates of each line added, and maintains a map of coordinates to nodes, creating nodes when
 * neccessary.<br>
 * <br>
 * If a tolerance distance is set, the end coordinates matched to nodes with a tolerance distance
 * (using a spatial index). <br>
 * <br>
 * Edges created by the generator are of type BasicEdge and contain an object of type LineSegment.
 * <br>
 * Nodes created by the generator are of type BasicXYNode and contain an object of type Coordinate.
 *
 * @see org.geotools.graph.structure.line.BasicXYNode
 * @see org.geotools.graph.structure.basic.BasicEdge
 * @see org.locationtech.jts.geom.LineSegment
 * @see org.locationtech.jts.geom.Coordinate
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @author Anders Bakkevold, Bouvet AS, bakkedev@gmail.com
 */
public class BasicLineGraphGenerator implements LineGraphGenerator {

    /** coordinate to node map * */
    private HashMap<Coordinate, Node> m_coord2node;

    /** underlying builder * */
    private GraphBuilder m_builder;

    /** tolerance distance * */
    private double tolerance = 0.0;

    /** used when tolerance is greater than 0.0 */
    private Bintree spatialIndex;

    /**
     * Constructs a new BasicLineGraphGenerator.
     *
     * <p>Tolerance is 0.0 as default, meaning coordinates must be equal for lines to connect at a
     * node.
     */
    public BasicLineGraphGenerator() {
        m_coord2node = new HashMap<>();
        setGraphBuilder(new BasicGraphBuilder());
    }

    /**
     * Constructs a new BasicLineGraphGenerator.
     *
     * <p>If two coordinates are considered equal (and should be snapped to the same Node), the
     * distance between them must be less than the tolerance value.
     *
     * @param tolerance threshold distance value for coordinates to be considered equal
     */
    public BasicLineGraphGenerator(double tolerance) {
        this.tolerance = tolerance;
        spatialIndex = new Bintree();
        m_coord2node = new HashMap<>();
        setGraphBuilder(new BasicGraphBuilder());
    }

    /**
     * Adds a line to the graph.
     *
     * @param obj An instance of LineSegment.
     * @return A BasicEdge.
     * @see LineSegment
     * @see GraphGenerator#add(Object)
     */
    @Override
    public Graphable add(Object obj) {
        LineSegment line = (LineSegment) obj;

        // check first coordinate
        Coordinate first = line.p0;
        Node n1 = retrieveNode(first);
        if (n1 == null) {
            n1 = createNode(first);
        }

        // check second coordinate
        Coordinate last = line.p1;
        Node n2 = retrieveNode(last);
        if (n2 == null) {
            n2 = createNode(last);
        }

        // build the edge setting underlying object to line
        Edge e = getGraphBuilder().buildEdge(n1, n2);

        getGraphBuilder().addEdge(e);

        if (useTolerance()) {
            line = alterLine(line, n1, n2);
        }

        setObject(e, line);

        // return the created edge
        return (e);
    }

    protected LineSegment alterLine(LineSegment line, Node n1, Node n2) {
        Coordinate c1added = ((Coordinate) n1.getObject());
        Coordinate c2added = ((Coordinate) n2.getObject());
        if (!c1added.equals2D(line.p0) || c2added.equals2D(line.p1)) {
            return new LineSegment(c1added, c2added);
        }
        return line;
    }

    /**
     * Returns the edge which represents a line. Note that if the exact same line has been added to
     * the graph multiple times, then only one of the edges that represents it will be returned. It
     * is undefined which edge will be returned.
     *
     * @param obj An instance of LineSegment.
     * @return Edge that represents the line.
     * @see GraphGenerator#get(Object)
     */
    @Override
    public Graphable get(Object obj) {
        LineSegment line = (LineSegment) obj;

        // get nodes representing coordinate
        Node n1 = retrieveNode(line.p0);
        Node n2 = retrieveNode(line.p1);

        if (n1 == null || n2 == null) return (null);

        // return edge shared between them
        return (n1.getEdge(n2));

        // note: if there are identical lines in the graph then it is undefined
        // which of them will be returned
    }

    /**
     * Removes the edge from the graph that represents a line.
     *
     * @return Edge that represents the line.
     * @see GraphGenerator#remove(Object)
     */
    @Override
    public Graphable remove(Object obj) {
        LineSegment line = (LineSegment) obj;
        Node n1 = retrieveNode(line.p0);
        Node n2 = retrieveNode(line.p1);

        if (n1 == null || n2 == null) return (null);

        Edge e = n1.getEdge(n2);
        getGraphBuilder().removeEdge(e);

        return (e);
    }

    /** @see GraphGenerator#setGraphBuilder(GraphBuilder) */
    @Override
    public void setGraphBuilder(GraphBuilder builder) {
        m_builder = builder;
    }

    /** @see GraphGenerator#getGraphBuilder() */
    @Override
    public GraphBuilder getGraphBuilder() {
        return (m_builder);
    }

    /** @see GraphGenerator#getGraph() */
    @Override
    public Graph getGraph() {
        return (getGraphBuilder().getGraph());
    }

    /**
     * Returns the coordinate to node map used to build nodes representing line endpoint
     * coordinates.
     *
     * @return coordinate to node map.
     */
    public Map getNodeMap() {
        return (m_coord2node);
    }

    // TODO COMMENT ME!
    @Override
    public Node getNode(Coordinate c) {
        return retrieveNode(c);
    }

    @Override
    public Edge getEdge(Coordinate c1, Coordinate c2) {
        Node n1 = retrieveNode(c1);
        Node n2 = retrieveNode(c2);

        return (n1.getEdge(n2));
    }

    protected void setObject(Edge e, Object obj) {
        e.setObject(obj);
    }

    protected void setObject(Node n, Object obj) {
        n.setObject(obj);
    }

    private Node createNode(Coordinate c) {
        Node node = getGraphBuilder().buildNode();
        setObject(node, c);
        getGraphBuilder().addNode(node);
        m_coord2node.put(c, node);
        if (useTolerance()) {
            spatialIndex.insert(new Interval(c.y, c.y), c);
        }
        return node;
    }

    private Node retrieveNode(Coordinate c) {
        Node node = m_coord2node.get(c);
        if (node == null && useTolerance()) {
            node = findClosestNodeWithinTolerance(c);
        }
        return node;
    }

    protected boolean useTolerance() {
        return tolerance > 0.0;
    }

    // spatial search with tolerance
    private Node findClosestNodeWithinTolerance(Coordinate inCoord) {
        double closestDistance = Double.MAX_VALUE;
        Coordinate closestCoordinate = null;
        @SuppressWarnings("unchecked")
        List<Coordinate> list =
                (List<Coordinate>)
                        spatialIndex.query(
                                new Interval(inCoord.y - tolerance, inCoord.y + tolerance));
        for (Coordinate c : list) {
            double distance = inCoord.distance(c);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCoordinate = c;
            }
        }
        if (closestCoordinate != null && closestCoordinate.distance(inCoord) < tolerance) {
            return m_coord2node.get(closestCoordinate);
        }
        return null;
    }
}
