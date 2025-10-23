package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

class GraphTest {

  @Test
  void directedGraphAddEdgeMaintainsCountsAndAdjacency() {
    Graph graph = new DirectedGraph(3);

    graph.addEdge(0, 1, 2.5);
    graph.addEdge(1, 2); // implicit weight 1.0

    assertTrue(graph.isDirected());
    assertEquals(2, graph.edgeCount());
    assertEquals(3, graph.nodeCount());
    assertEquals(1, graph.degree(0));
    assertEquals(1, graph.degree(1));
    assertEquals(0, graph.degree(2));

    List<Edge> edges = collect(graph.edges());
    assertEquals(2, edges.size());
    assertTrue(edges.contains(new Edge(0, 1, 2.5)));
    assertTrue(edges.contains(new Edge(1, 2, 1.0)));

    List<Edge> fromZero = collect(graph.adjacency(0));
    assertEquals(1, fromZero.size());
    assertEquals(new Edge(0, 1, 2.5), fromZero.getFirst());
  }

  @Test
  void directedGraphRemoveEdgeUpdatesDegreeAndEdges() {
    Graph graph = new DirectedGraph(4);
    graph.addEdge(0, 1);
    graph.addEdge(0, 2);

    graph.removeEdge(0, 1);

    assertEquals(1, graph.edgeCount());
    assertEquals(1, graph.degree(0));
    assertEquals(0, graph.degree(1));

    // Removing a non-existing edge should not alter the count.
    graph.removeEdge(0, 1);
    assertEquals(1, graph.edgeCount());
  }

  @Test
  void undirectedGraphAddEdgeAddsSymmetricAdjacency() {
    Graph graph = new UndirectedGraph(3);

    graph.addEdge(0, 1, 3.0);

    assertFalse(graph.isDirected());
    assertEquals(1, graph.edgeCount());
    assertEquals(1, graph.degree(0));
    assertEquals(1, graph.degree(1));
    assertEquals(0, graph.degree(2));

    List<Edge> zeroNeighbors = collect(graph.adjacency(0));
    List<Edge> oneNeighbors = collect(graph.adjacency(1));

    assertEquals(new Edge(0, 1, 3.0), zeroNeighbors.getFirst());
    assertEquals(new Edge(1, 0, 3.0), oneNeighbors.getFirst());

    List<Edge> edges = collect(graph.edges());
    assertEquals(1, edges.size());
    assertEquals(new Edge(0, 1, 3.0), edges.getFirst());
  }

  @Test
  void undirectedGraphRemoveEdgeRemovesBothDirections() {
    Graph graph = new UndirectedGraph(4);
    graph.addEdge(0, 1);
    graph.addEdge(1, 2);

    graph.removeEdge(0, 1);

    assertEquals(1, graph.edgeCount());
    assertEquals(0, graph.degree(0));
    assertEquals(1, graph.degree(1));

    List<Edge> zeroNeighbors = collect(graph.adjacency(0));
    assertTrue(zeroNeighbors.isEmpty());

    List<Edge> oneNeighbors = collect(graph.adjacency(1));
    assertEquals(1, oneNeighbors.size());
    assertEquals(new Edge(1, 2, 1.0), oneNeighbors.getFirst());
  }

  @Test
  void undirectedGraphRejectsSelfLoops() {
    Graph graph = new UndirectedGraph(2);
    assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 0));
  }

  @Test
  void nodesIterableCoversAllVertices() {
    Graph graph = new DirectedGraph(5);
    Iterator<Integer> iterator = graph.nodes().iterator();
    for (int expected = 0; expected < graph.nodeCount(); expected++) {
      assertTrue(iterator.hasNext());
      assertEquals(expected, iterator.next());
    }
    assertFalse(iterator.hasNext());
  }

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
