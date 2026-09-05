import java.util.*;

/**
* Shortest path in a graph where nodes are generated lazily.
* Simplest approach: BFS (guarantees shortest path in an unweighted graph,
* i.e. one where every edge/expansion step has equal cost).
*
* The key idea: you don't need the whole graph up front. You only need a
* way to "expand" a node into its neighbors on demand. BFS naturally
* expands nodes one layer at a time, so it works perfectly with lazy
* generation — you just call expand() the first time you visit a node.
  */
  public class LazyGraphShortestPath {

  /**
    * Represents a node in the graph. Nodes are only created when a
    * parent node is expanded (i.e. neighbors are generated on the fly).
      */
      interface Node {
      // Returns the neighbors of this node, creating them if needed.
      List<Node> expand();

      // Used to check if we've reached the target and to avoid revisiting.
      boolean equalsNode(Node other);
      }

  /**
    * Runs BFS from start until it finds a node equal to target (per
    * equalsNode), expanding nodes lazily as it goes.
    *
    * @return the path from start to target (inclusive), or null if not found.
      */
      public static List<Node> findShortestPath(Node start, Node target) {
      if (start.equalsNode(target)) {
      return List.of(start);
      }

      Queue<Node> queue = new LinkedList<>();
      Map<Node, Node> cameFrom = new HashMap<>(); // child -> parent
      Set<Node> visited = new HashSet<>();

      queue.add(start);
      visited.add(start);

      while (!queue.isEmpty()) {
      Node current = queue.poll();

           // Lazily generate neighbors only when this node is expanded.
           for (Node neighbor : current.expand()) {
               if (visited.contains(neighbor)) continue;

               cameFrom.put(neighbor, current);

               if (neighbor.equalsNode(target)) {
                   return reconstructPath(cameFrom, start, neighbor);
               }

               visited.add(neighbor);
               queue.add(neighbor);
           }
      }

      return null; // no path found
      }

  private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node start, Node end) {
  LinkedList<Node> path = new LinkedList<>();
  Node current = end;
  path.addFirst(current);
  while (!current.equalsNode(start)) {
  current = cameFrom.get(current);
  path.addFirst(current);
  }
  return path;
  }

  // ---------------------------------------------------------------
  // Example usage: a simple grid where nodes are generated on demand.
  // ---------------------------------------------------------------
  static class GridNode implements Node {
  final int x, y;
  final int maxX, maxY;
  final Set<String> blocked; // e.g. walls, using "x,y" keys

       GridNode(int x, int y, int maxX, int maxY, Set<String> blocked) {
           this.x = x;
           this.y = y;
           this.maxX = maxX;
           this.maxY = maxY;
           this.blocked = blocked;
       }

       @Override
       public List<Node> expand() {
           List<Node> neighbors = new ArrayList<>();
           int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
           for (int[] d : dirs) {
               int nx = x + d[0], ny = y + d[1];
               if (nx < 0 || ny < 0 || nx > maxX || ny > maxY) continue;
               if (blocked.contains(nx + "," + ny)) continue;
               neighbors.add(new GridNode(nx, ny, maxX, maxY, blocked));
           }
           return neighbors;
       }

       @Override
       public boolean equalsNode(Node other) {
           GridNode o = (GridNode) other;
           return this.x == o.x && this.y == o.y;
       }

       @Override
       public boolean equals(Object o) {
           if (!(o instanceof GridNode)) return false;
           GridNode g = (GridNode) o;
           return x == g.x && y == g.y;
       }

       @Override
       public int hashCode() {
           return Objects.hash(x, y);
       }

       @Override
       public String toString() {
           return "(" + x + "," + y + ")";
       }
  }

  public static void main(String[] args) {
  Set<String> blocked = new HashSet<>(); // no walls in this example
  Node start = new GridNode(0, 0, 5, 5, blocked);
  Node target = new GridNode(4, 4, 5, 5, blocked);

       List<Node> path = findShortestPath(start, target);
       if (path == null) {
           System.out.println("No path found.");
       } else {
           System.out.println("Path length: " + (path.size() - 1) + " steps");
           System.out.println(path);
       }
  }
  }
