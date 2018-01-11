package directed_graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vertex class.
 *
 * Note that parallel edges are allowed, but not self-loops.
 * @author Ziang Lu
 */
class Vertex {

    /**
     * Vertex ID.
     */
    private final int vtxID;
    /**
     * Frequency of emissive neighbors.
     */
    private HashMap<Integer, Integer> freqOfEmissiveNeighbors;
    /**
     * Emissive edge of this vertex.
     */
    private ArrayList<Edge> emissiveEdges;
    /**
     * Frequency of incident neighbors.
     */
    private HashMap<Integer, Integer> freqOfIncidentNeighbors;
    /**
     * Incident of this vertex.
     */
    private ArrayList<Edge> incidentEdges;

    /**
     * Constructor with parameter.
     * @param vtxID vertex ID
     */
    Vertex(int vtxID) {
        this.vtxID = vtxID;
        freqOfEmissiveNeighbors = new HashMap<Integer, Integer>();
        emissiveEdges = new ArrayList<Edge>();
        freqOfIncidentNeighbors = new HashMap<Integer, Integer>();
        incidentEdges = new ArrayList<Edge>();
    }

    /**
     * Accessor of vtxID.
     * @return vtxID
     */
    int getID() {
        return vtxID;
    }

    /**
     * Returns the first emissive edge with the given head.
     * @param head given head
     * @return emissive edge if found, null if not found
     */
    Edge getEmissiveEdgeWithHead(Vertex head) {
        // Check whether the input head is null
        if (head == null) {
            throw new IllegalArgumentException("The input head should not be null.");
        }

        for (Edge emissiveEdge : emissiveEdges) {
            if (emissiveEdge.head == head) {
                return emissiveEdge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of emissiveEdges.
     * @return emissiveEdges
     */
    ArrayList<Edge> getEmissiveEdges() {
        return emissiveEdges;
    }

    /**
     * Returns the first incident edge with the given tail.
     * @param tail given tail
     * @return incident edge if found, null if not found
     */
    Edge getIncidentEdgeWithTail(Vertex tail) {
        // Check whether the input tail is null
        if (tail == null) {
            throw new IllegalArgumentException("The input tail should not be null.");
        }

        for (Edge incidentEdge : incidentEdges) {
            if (incidentEdge.tail == tail) {
                return incidentEdge;
            }
        }
        // Not found
        return null;
    }

    /**
     * Accessor of incidentEdges.
     * @return incidentEdges
     */
    ArrayList<Edge> getIncidentEdges() {
        return incidentEdges;
    }

    /**
     * Adds the given emissive edge to this vertex.
     * @param newEdge emissive edge to add
     */
    void addEmissiveEdge(Edge newEmissiveEdge) {
        // Check whether the input emissive edge is null
        if (newEmissiveEdge == null) {
            throw new IllegalArgumentException("The emissive edge to add should not be null.");
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (newEmissiveEdge.tail != this) {
            throw new IllegalArgumentException("The emissive edge to add should involve this vertex as the tail.");
        }

        emissiveEdges.add(newEmissiveEdge);

        // Find the emissive neighbor associated with the input emissive edge
        Vertex emissiveNeighbor = newEmissiveEdge.head;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfEmissiveNeighbors.getOrDefault(emissiveNeighbor.vtxID, 0);
        ++freq;
        freqOfEmissiveNeighbors.put(emissiveNeighbor.vtxID, freq);
    }

    /**
     * Adds the given incident edge to this vertex.
     * @param newIncidentEdge incident edge to add
     */
    void addIncidentEdge(Edge newIncidentEdge) {
        // Check whether the input incident edge is null
        if (newIncidentEdge == null) {
            throw new IllegalArgumentException("The incident edge to add should not be null.");
        }
        // Check whether the input incident edge involves this vertex as the head
        if (newIncidentEdge.head != this) {
            throw new IllegalArgumentException("The incident edge to add should involve this vertex as the head.");
        }

        incidentEdges.add(newIncidentEdge);

        // Find the incident neighbor associated with the input incident edge
        Vertex incidentNeighbor = newIncidentEdge.tail;
        // Update the frequency of the incident neighbor
        Integer freq = freqOfIncidentNeighbors.getOrDefault(incidentNeighbor.vtxID, 0);
        ++freq;
        freqOfIncidentNeighbors.put(incidentNeighbor.vtxID, freq);
    }

    /**
     * Removes the given emissive edge from this vertex
     * @param emissiveEdgeToRemove edge to remove
     */
    void removeEmissiveEdge(Edge emissiveEdgeToRemove) {
        // Check whether the input emissive edge is null
        if (emissiveEdgeToRemove == null) {
            throw new IllegalArgumentException("The emissive edge to remove should not be null.");
        }
        // Check whether the input emissive edge involves this vertex as the tail
        if (emissiveEdgeToRemove.tail != this) {
            throw new IllegalArgumentException("The emissive edge to remove should involve this vertex as the tail.");
        }

        emissiveEdges.remove(emissiveEdgeToRemove);

        // Find the emissive neighbor associated with the input emissive edge
        Vertex emissiveNeighbor = emissiveEdgeToRemove.head;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfEmissiveNeighbors.get(emissiveNeighbor.vtxID);
        if (freq == 1) {
            freqOfEmissiveNeighbors.remove(emissiveNeighbor.vtxID);
        } else {
            --freq;
            freqOfEmissiveNeighbors.put(emissiveNeighbor.vtxID, freq);
        }
    }

    /**
     * Removes the given incident edge from this vertex
     * @param incidentEdgeToRemove edge to remove
     */
    void removeIncidentEdge(Edge incidentEdgeToRemove) {
        // Check whether the input incident edge is null
        if (incidentEdgeToRemove == null) {
            throw new IllegalArgumentException("The incident edge to remove should not be null.");
        }
        // Check whether the input incident edge involves this vertex as the head
        if (incidentEdgeToRemove.head != this) {
            throw new IllegalArgumentException("The incident edge to remove should involve this vertex as the head.");
        }

        incidentEdges.remove(incidentEdgeToRemove);

        // Find the incident neighbor associated with the input incident edge
        Vertex incidentNeighbor = incidentEdgeToRemove.tail;
        // Update the frequency of the emissive neighbor
        Integer freq = freqOfIncidentNeighbors.get(incidentNeighbor.vtxID);
        if (freq == 1) {
            freqOfIncidentNeighbors.remove(incidentNeighbor.vtxID);
        } else {
            --freq;
            freqOfIncidentNeighbors.put(incidentNeighbor.vtxID, freq);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Vertex #").append(vtxID).append('\n');
        s.append("Its emissive neighbors and frequencyes: ").append(freqOfEmissiveNeighbors).append('\n');
        s.append("Its incident neighbors and frequencyes: ").append(freqOfIncidentNeighbors).append('\n');
        return s.toString();
    }

}
