package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraphTask3Test {

  @Test
  void directedGraphHasPathRespectsDirectionality() {
    Graph graph = new DirectedGraph(3);
    graph.addEdge(0, 1);
    graph.addEdge(1, 2);

    assertTrue(graph.hasPath(0, 2));
    assertFalse(graph.hasPath(2, 0));
  }

  @Test
  void pathIterableReturnsShortestDirectedPath() {
    Graph graph = new DirectedGraph(4);
    graph.addEdge(0, 1);
    graph.addEdge(1, 2);
    graph.addEdge(0, 3);
    graph.addEdge(3, 2);

    List<Integer> path = collect(graph.path(0, 2));
    assertEquals(List.of(0, 1, 2), path);
  }

  @Test
  void pathIterableEmptyWhenUnreachable() {
    Graph graph = new DirectedGraph(3);
    graph.addEdge(0, 1);
    List<Integer> path = collect(graph.path(1, 0));
    assertTrue(path.isEmpty());
    assertFalse(graph.hasPath(1, 0));
  }

  @Test
  void undirectedGraphPathConnectsBothDirections() {
    Graph graph = new UndirectedGraph(3);
    graph.addEdge(0, 1);
    graph.addEdge(1, 2);

    assertTrue(graph.hasPath(2, 0));
    List<Integer> path = collect(graph.path(2, 0));
    assertEquals(List.of(2, 1, 0), path);
  }

  @Test
  void pathReturnsSingleNodeWhenSourceEqualsTarget() {
    Graph graph = new DirectedGraph(2);
    List<Integer> path = collect(graph.path(1, 1));
    assertEquals(List.of(1), path);
  }

  @Test
  void bfsProducesLevelOrderTraversal() {
    Graph graph = new DirectedGraph(5);
    graph.addEdge(0, 1);
    graph.addEdge(0, 2);
    graph.addEdge(1, 3);
    graph.addEdge(1, 4);
    graph.addEdge(2, 4);

    List<Integer> order = collect(graph.bfs(0));
    assertEquals(List.of(0, 1, 2, 3, 4), order);
  }

  @Test
  void dfsProducesDepthFirstTraversal() {
    Graph graph = new DirectedGraph(5);
    graph.addEdge(0, 1);
    graph.addEdge(0, 2);
    graph.addEdge(1, 3);
    graph.addEdge(1, 4);
    graph.addEdge(2, 4);

    List<Integer> order = collect(graph.dfs(0));
    assertEquals(List.of(0, 1, 3, 4, 2), order);
  }

  private static <T> List<T> collect(Iterable<T> values) {
    List<T> list = new ArrayList<>();
    for (T value : values) {
      list.add(value);
    }
    return list;
  }
}
