package graph;

/**
 * Interface representing a graph data structure.
 */
public interface Graph {
  int nodeCount();
  int edgeCount();
  boolean isDirected();

  void addEdge(int u, int v);
  void addEdge(int u, int v, double weight);
  void removeEdge(int u, int v);

  int degree(int u);

  Iterable<Integer> nodes();
  Iterable<Edge> edges();
  Iterable<Edge> adjacency(int u);
}
