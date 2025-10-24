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
  public Iterable<Integer> bfs(int start) {
    checkNode(start);
    List<Integer> order = new ArrayList<>();
    boolean[] visited = new boolean[nodes];
    Deque<Integer> queue = new ArrayDeque<>();
    visited[start] = true;
    queue.add(start);
    while (!queue.isEmpty()) {
      int current = queue.remove();
      order.add(current);
      for (Edge edge : adjList.get(current)) {
        int neighbor = edge.to();
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          queue.add(neighbor);
        }
      }
    }
    return Collections.unmodifiableList(order);
  }

  @Override
  public Iterable<Integer> dfs(int start) {
    checkNode(start);
    List<Integer> order = new ArrayList<>();
    boolean[] visited = new boolean[nodes];
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(start);
    visited[start] = true;
    while (!stack.isEmpty()) {
      int current = stack.pop();
      order.add(current);
      List<Edge> neighbors = adjList.get(current);
      for (int i = neighbors.size() - 1; i >= 0; i--) {
        int neighbor = neighbors.get(i).to();
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          stack.push(neighbor);
        }
      }
    }
    return Collections.unmodifiableList(order);
  }

  @Override
  public boolean hasPath(int source, int target) {
    checkNode(source);
    checkNode(target);
    if (source == target) {
      return true;
    }
    return !findPath(source, target).isEmpty();
  }

  @Override
  public Iterable<Integer> path(int source, int target) {
    checkNode(source);
    checkNode(target);
    List<Integer> path = findPath(source, target);
    if (path.isEmpty()) {
      return List.of();
    }
    return Collections.unmodifiableList(path);
  }
  
  @Override
  public double[] shortestDistances(int source) {
    checkNode(source);
    Dijkstra.Result result = Dijkstra.compute(this, source);
    return result.distances().clone();
  }

  @Override
  public Iterable<Integer> shortestPath(int source, int target) {
    checkNode(source);
    checkNode(target);
    Dijkstra.Result result = Dijkstra.compute(this, source);
    List<Integer> path = Dijkstra.reconstructPath(source, target, result);
    if (path.isEmpty()) {
      return List.of();
    }
    return Collections.unmodifiableList(path);
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

  private List<Integer> findPath(int source, int target) {
    if (source == target) {
      return new ArrayList<>(List.of(source));
    }

    boolean[] visited = new boolean[nodes];
    int[] parent = new int[nodes];
    Arrays.fill(parent, -1);
    Deque<Integer> queue = new ArrayDeque<>();

    visited[source] = true;
    queue.add(source);

    while (!queue.isEmpty()) {
      int current = queue.remove();
      for (Edge edge : adjList.get(current)) {
        int neighbor = edge.to();
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          parent[neighbor] = current;
          if (neighbor == target) {
            return buildPath(source, target, parent);
          }
          queue.add(neighbor);
        }
      }
    }

    return Collections.emptyList();
  }

  private List<Integer> buildPath(int source, int target, int[] parent) {
    List<Integer> path = new ArrayList<>();
    int current = target;
    path.add(target);
    while (current != source) {
      current = parent[current];
      if (current == -1) {
        return Collections.emptyList();
      }
      path.add(current);
    }
    Collections.reverse(path);
    return path;
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
