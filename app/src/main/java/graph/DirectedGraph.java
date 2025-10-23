package graph;

/**
 * A directed graph implementation.
 */
public class DirectedGraph extends AbstractGraph {

  public DirectedGraph(int nodes) {
    super(nodes);
  }

  @Override
  public boolean isDirected() {
    return true;
  }

  @Override
  public void addEdge(int u, int v, double weight) {
    addAdjEdge(u, v, weight);
    incrementEdgeCount();
  }

  @Override
  public void removeEdge(int u, int v) {
    if (removeAdjEdge(u, v)) {
      decrementEdgeCount();
    }
  }
}
