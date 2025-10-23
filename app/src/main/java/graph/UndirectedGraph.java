package graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An undirected graph implementation.
 */
public class UndirectedGraph extends AbstractGraph {

  public UndirectedGraph(int nodes) {
    super(nodes);
  }

  @Override
  public boolean isDirected() {
    return false;
  }

  @Override
  public void addEdge(int u, int v, double weight) {
    if (u == v) {
      throw new IllegalArgumentException("Self-loops are not supported in undirected graphs.");
    }
    addAdjEdge(u, v, weight);
    addAdjEdge(v, u, weight);
    incrementEdgeCount();
  }

  @Override
  public void removeEdge(int u, int v) {
    boolean removedUV = removeAdjEdge(u, v);
    boolean removedVU = removeAdjEdge(v, u);
    if (removedUV || removedVU) {
      if (removedUV != removedVU) {
        throw new IllegalStateException("Undirected edge is missing its symmetric counterpart.");
      }
      decrementEdgeCount();
    }
  }

  @Override
  public Iterable<Edge> edges() {
    return () -> new Iterator<Edge>() {
      private int bucketIndex = 0;
      private Iterator<Edge> current = Collections.emptyIterator();
      private Edge nextEdge;

      {
        advance();
      }

      @Override
      public boolean hasNext() {
        return nextEdge != null;
      }

      @Override
      public Edge next() {
        if (nextEdge == null) {
          throw new NoSuchElementException();
        }
        Edge result = nextEdge;
        advance();
        return result;
      }

      private void advance() {
        nextEdge = null;
        while (true) {
          while (current.hasNext()) {
            Edge candidate = current.next();
            int from = candidate.from();
            int to = candidate.to();
            if (from <= to) {
              nextEdge = candidate;
              return;
            }
          }
          if (bucketIndex >= adjList.size()) {
            return;
          }
          List<Edge> bucket = adjList.get(bucketIndex);
          current = bucket.iterator();
          bucketIndex++;
        }
      }
    };
  }
}
