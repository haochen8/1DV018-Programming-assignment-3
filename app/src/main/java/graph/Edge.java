package graph;

/**
 * Represents an edge in a graph with a source node, destination node, and weight.
 *
 * @param from   the source node of the edge
 * @param to     the destination node of the edge
 * @param weight the weight of the edge
 */
public record Edge(int from, int to, double weight) {}
