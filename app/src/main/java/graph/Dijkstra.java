package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Utility class providing Dijkstra's single-source shortest path algorithm.
 */
public final class Dijkstra {

  private Dijkstra() {
    // Utility class
  }

  public static Result compute(Graph graph, int source) {
    int n = graph.nodeCount();
    validateNode(source, n);

    double[] distances = new double[n];
    int[] parents = new int[n];
    boolean[] settled = new boolean[n];

    Arrays.fill(distances, Double.POSITIVE_INFINITY);
    Arrays.fill(parents, -1);

    PriorityQueue<NodeDistance> queue = new PriorityQueue<>();
    distances[source] = 0.0;
    queue.add(new NodeDistance(source, 0.0));

    while (!queue.isEmpty()) {
      NodeDistance current = queue.poll();
      int u = current.node();
      if (settled[u]) {
        continue;
      }
      settled[u] = true;

      for (Edge edge : graph.adjacency(u)) {
        if (edge.weight() < 0) {
          throw new IllegalArgumentException("Negative edge weights are not supported by Dijkstra's algorithm.");
        }
        int v = edge.to();
        double candidate = distances[u] + edge.weight();
        if (candidate < distances[v]) {
          distances[v] = candidate;
          parents[v] = u;
          queue.add(new NodeDistance(v, candidate));
        }
      }
    }

    return new Result(distances, parents);
  }

  public static List<Integer> reconstructPath(int source, int target, Result result) {
    int[] parents = result.parents();
    validateNode(target, result.distances().length);
    if (result.distances()[target] == Double.POSITIVE_INFINITY) {
      return List.of();
    }

    Deque<Integer> stack = new ArrayDeque<>();
    int current = target;
    while (current != -1) {
      stack.push(current);
      if (current == source) {
        break;
      }
      current = parents[current];
    }

    if (stack.peekFirst() == null || stack.peekFirst() != source) {
      return List.of();
    }

    List<Integer> path = new ArrayList<>(stack.size());
    while (!stack.isEmpty()) {
      path.add(stack.pop());
    }
    return path;
  }

  public record Result(double[] distances, int[] parents) {
  }

  private static void validateNode(int node, int size) {
    if (node < 0 || node >= size) {
      throw new IllegalArgumentException("Node " + node + " is out of bounds.");
    }
  }

  private record NodeDistance(int node, double distance) implements Comparable<NodeDistance> {
    @Override
    public int compareTo(NodeDistance other) {
      return Double.compare(this.distance, other.distance);
    }
  }
}
