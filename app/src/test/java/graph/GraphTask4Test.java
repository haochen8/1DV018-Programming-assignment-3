package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraphTask4Test {

  @Test
  void shortestDistancesDirectedGraphComputesWeightedRoutes() {
    Graph graph = new DirectedGraph(4);
    graph.addEdge(0, 1, 1.0);
    graph.addEdge(0, 2, 4.0);
    graph.addEdge(1, 2, 2.0);
    graph.addEdge(1, 3, 5.0);
    graph.addEdge(2, 3, 1.0);

    double[] distances = graph.shortestDistances(0);
    assertArrayEquals(new double[] {0.0, 1.0, 3.0, 4.0}, distances, 1e-9);

    List<Integer> path = collect(graph.shortestPath(0, 3));
    assertEquals(List.of(0, 1, 2, 3), path);
  }

  @Test
  void shortestPathReturnsEmptyIfTargetUnreachable() {
    Graph graph = new DirectedGraph(3);
    graph.addEdge(0, 1, 2.0);

    assertEquals(List.of(), collect(graph.shortestPath(0, 2)));
    double[] distances = graph.shortestDistances(0);
    assertEquals(Double.POSITIVE_INFINITY, distances[2]);
  }

  @Test
  void undirectedGraphShortestPathHonorsWeights() {
    Graph graph = new UndirectedGraph(5);
    graph.addEdge(0, 1, 2.0);
    graph.addEdge(1, 2, 2.0);
    graph.addEdge(2, 3, 2.0);
    graph.addEdge(0, 4, 1.0);
    graph.addEdge(4, 3, 1.0);

    List<Integer> path = collect(graph.shortestPath(0, 3));
    assertEquals(List.of(0, 4, 3), path);

    double[] distances = graph.shortestDistances(0);
    assertArrayEquals(new double[] {0.0, 2.0, 4.0, 2.0, 1.0}, distances, 1e-9);
  }

  @Test
  void shortestDistancesRejectsNegativeWeights() {
    Graph graph = new DirectedGraph(2);
    graph.addEdge(0, 1, -1.0);
    assertThrows(IllegalArgumentException.class, () -> graph.shortestDistances(0));
  }

  private static <T> List<T> collect(Iterable<T> values) {
    List<T> list = new ArrayList<>();
    for (T value : values) {
      list.add(value);
    }
    return list;
  }
}
