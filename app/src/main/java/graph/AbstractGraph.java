package graph;

import java.util.*;

/**
 * An abstract base class for graph implementations.
 */
public abstract class AbstractGraph implements Graph {
  
  protected final int nodes;
  protected int edges;
  protected final List<List<Edge>> adjList;

  protected AbstractGraph(int nodes) {
    this.nodes = nodes;
    this.edges = 0;
    this.adjList = new ArrayList<>(nodes);
    for (int i = 0; i < nodes; i++) {
      adjList.add(new ArrayList<>());
    }
  }

  @Override
  public int nodeCount() {
    return nodes;
  }

  @Override
  public int edgeCount() {
    return edges;
  }

  /**
   * Returns an iterable over all nodes in the graph.
   */
  @Override
  public Iterable<Integer> nodes() {
    return () -> new Iterator<Integer>() {
      int current = 0;
      @Override
      public boolean hasNext() {
        return current < nodes;
      }

      @Override
      public Integer next() {
        if (!hasNext()) throw new NoSuchElementException();
        return current++;
      }
    };
  }

  @Override
  public Iterable<Edge> adjacency(int u) {
    checkNode(u);
    return Collections.unmodifiableList(adjList.get(u));
  }

  @Override
  public Iterable<Edge> edges() {
    return () -> new Iterator<Edge>() {
      private int bucketIndex = 0;
      private Iterator<Edge> current = Collections.emptyIterator();

      {
        advance();
      }

      @Override
      public boolean hasNext() {
        return current.hasNext();
      }

      @Override
      public Edge next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Edge edge = current.next();
        if (!current.hasNext()) {
          advance();
        }
        return edge;
      }

      private void advance() {
        while (bucketIndex < adjList.size()) {
          current = adjList.get(bucketIndex).iterator();
          bucketIndex++;
          if (current.hasNext()) {
            return;
          }
        }
        current = Collections.emptyIterator();
      }
    };
  }

  protected void checkNode(int u) {
    if (u < 0 || u >= nodes) {
      throw new IllegalArgumentException("Node " + u + " is out of bounds.");
    }
  }

  protected void addAdjEdge(int u, int v, double weight) {
    checkNode(u);
    checkNode(v);
    adjList.get(u).add(new Edge(u, v, weight));
  }

  protected boolean removeAdjEdge(int u, int v) {
    checkNode(u);
    checkNode(v);
    Iterator<Edge> iterator = adjList.get(u).iterator();
    while (iterator.hasNext()) {
      if (iterator.next().to() == v) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  protected void incrementEdgeCount() {
    edges++;
  }

  protected void decrementEdgeCount() {
    if (edges == 0) {
      throw new IllegalStateException("Cannot decrement edge count below zero.");
    }
    edges--;
  }

  @Override
  public void addEdge(int u, int v) {
    addEdge(u, v, 1.0);
  }

  @Override
  public int degree(int u) {
    checkNode(u);
    return adjList.get(u).size();
  }
}
