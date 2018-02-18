package undirected_graph;

import java.util.ArrayList;
import java.util.HashMap;

import graph.AbstractVertex;
import union_find.UnionFindObj;

/**
 * Vertex class.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
class Vertex extends AbstractVertex implements Comparable<Vertex>, UnionFindObj {

    /**
     * Default minimum cost of the incident edge from the spanned vertices (X).
     */
    private static final int DEFAULT_MIN_INCIDENT_COST = Integer.MAX_VALUE;

    /**
     * Frequency of neighbors.
     */
    private final HashMap<Integer, Integer> freqOfNeighbors;
    /**
     * Edges of this vertex.
     */
    private final ArrayList<UndirectedEdge> edges;
    /**
     * Reference to the incident edge with minimum cost from the spanned vertices (X).
     */
    private UndirectedEdge minCostIncidentEdge;
    /**
     * Minimum cost of the incident edges from the spanned vertices (X).
     */
    private double minIncidentCost;
    /**
     * Leader of this object.
     */
    private UnionFindObj leader;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        super(vtxID);
        freqOfNeighbors = new HashMap<Integer, Integer>();
        edges = new ArrayList<UndirectedEdge>();
        minCostIncidentEdge = null;
        minIncidentCost = DEFAULT_MIN_INCIDENT_COST;
        leader = this;
    }

    /**
     * Returns the first edge with the given neighbor.
     * @param neighbor given neighbor
     * @return edge if found, null if not found
     */
    UndirectedEdge getEdgeWithNeighbor(Vertex neighbor) {
        // Check whether the input neighbor is null
        if (neighbor == null) {
            throw new IllegalArgumentException("The input neighbor should not be null.");
        }

        for (UndirectedEdge edge : edges) {
            if (((edge.end1() == this) && (edge.end2() == neighbor))
                    || ((edge.end1() == neighbor) && (edge.end2() == this))) {
                return edge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of edges.
     * @return edges
     */
    ArrayList<UndirectedEdge> edges() {
        return edges;
    }

    /**
     * Accessor of minCostIncidentEdge.
     * @return minCostIncidentEdge
     */
    UndirectedEdge minCostIncidentEdge() {
        return minCostIncidentEdge;
    }

    /**
     * Accessor of minIncidentCost.
     * @return minIncidentCost
     */
    double minIncidentCost() {
        return minIncidentCost;
    }

    @Override
    public String name() {
        return String.valueOf(vtxID);
    }

    @Override
    public UnionFindObj leader() {
        return leader;
    }

    /**
     * Adds the given edge to this vertex.
     * @param newEdge edge to add
     */
    void addEdge(UndirectedEdge newEdge) {
        // Check whether the input edge is null
        if (newEdge == null) {
            throw new IllegalArgumentException("The edge to add should not be null.");
        }
        // Check whether the input edge involves this vertex
        if ((newEdge.end1() != this) && (newEdge.end2() != this)) {
            throw new IllegalArgumentException("The edge to add should involve this vertex.");
        }

        edges.add(newEdge);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (newEdge.end1() == this) { // endpoint2 is the neighbor.
            neighbor = newEdge.end2();
        } else { // endpoint1 is the neighbor.
            neighbor = newEdge.end1();
        }
        // Update the frequency of the neighbor
        Integer freq = freqOfNeighbors.getOrDefault(neighbor.vtxID, 0);
        ++freq;
        freqOfNeighbors.put(neighbor.vtxID, freq);
    }

    /**
     * Removes the given edge from this vertex.
     * @param edgeToRemove edge to remove
     */
    void removeEdge(UndirectedEdge edgeToRemove) {
        // Check whether the input edge is null
        if (edgeToRemove == null) {
            throw new IllegalArgumentException("The edge to remove should not be null.");
        }
        // Check whether the input edge involves this vertex
        if ((edgeToRemove.end1()!= this) && (edgeToRemove.end2() != this)) {
            throw new IllegalArgumentException("The edge to remove should involve this vertex.");
        }

        edges.remove(edgeToRemove);

        // Find the neighbor associated with the input edge
        Vertex neighbor = null;
        if (edgeToRemove.end1() == this) { // endpoint2 is the neighbor.
            neighbor = edgeToRemove.end2();
        } else { // endpoint1 is the neighbor.
            neighbor = edgeToRemove.end1();
        }
        // Update the frequency of the neighbor
        Integer freq = freqOfNeighbors.get(neighbor.vtxID);
        if (freq == 1) {
            freqOfNeighbors.remove(neighbor.vtxID);
        } else {
            --freq;
            freqOfNeighbors.put(neighbor.vtxID, freq);
        }
    }

    /**
     * Mutator of minCostIncidentEdge
     * @param minCostIncidentEdge minCostIncidentEdge
     */
    void setMinCostIncidentEdge(UndirectedEdge minCostIncidentEdge) {
        this.minCostIncidentEdge = minCostIncidentEdge;
    }

    /**
     * Mutator of minIncidentCost.
     * @param minIncidentCost minIncidentCost
     */
    void setMinIncidentCost(double minIncidentCost) {
        this.minIncidentCost = minIncidentCost;
    }

    @Override
    public void setLeader(UnionFindObj leader) {
        this.leader = leader;
    }

    @Override
    public int compareTo(Vertex o) {
        return Double.compare(minIncidentCost, o.minIncidentCost);
    }

    @Override
    public String toString() {
        return String.format("Vertex #%d, Its neighbors and frequencies: %s", vtxID, freqOfNeighbors);
    }

}
