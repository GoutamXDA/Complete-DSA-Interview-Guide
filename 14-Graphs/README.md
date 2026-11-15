# 14 — Graphs

## Notes
- Graph `G = (V, E)`: directed/undirected, weighted/unweighted, cyclic/acyclic (DAG), connected/disconnected.
- **Representations**
  | | Space | Edge check | Iterate neighbours |
  |---|---|---|---|
  | Adjacency matrix | O(V²) | O(1) | O(V) |
  | Adjacency list `List<List<Integer>>` | O(V+E) | O(deg) | O(deg) |
  | Edge list | O(E) | O(E) | — (Kruskal, Bellman-Ford) |
- Grids are implicit graphs: neighbours via `int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}}`.

## Algorithm chooser
| Problem | Algorithm | Complexity |
|---|---|---|
| Traverse / components / flood fill | BFS or DFS | O(V+E) |
| Shortest path, unweighted | BFS | O(V+E) |
| Shortest path, 0/1 weights | 0-1 BFS (deque) | O(V+E) |
| Shortest path, non-negative weights | Dijkstra (heap) | O((V+E) log V) |
| Shortest path, negative weights / detect negative cycle | Bellman-Ford | O(V·E) |
| All-pairs shortest path | Floyd–Warshall | O(V³) |
| Shortest path in DAG | Topological order + relax | O(V+E) |
| Ordering with dependencies | Topological sort (Kahn / DFS) | O(V+E) |
| Cycle detection | undirected: parent check / DSU; directed: 3-colour DFS or Kahn | O(V+E) |
| Two-colouring | Bipartite BFS/DFS | O(V+E) |
| Minimum spanning tree | Kruskal (sort + DSU) / Prim (heap) | O(E log E) |
| Strongly connected components | Kosaraju / Tarjan | O(V+E) |
| Bridges / articulation points | Tarjan low-link | O(V+E) |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | BFS / DFS of graph | Easy | queue / recursion | [GraphTraversals](GraphTraversals.java) |
| 2 | Number of provinces (LC 547) | Medium | DFS components | [GraphTraversals](GraphTraversals.java) |
| 3 | Number of islands (LC 200) | Medium | grid DFS/BFS | [GraphTraversals](GraphTraversals.java) |
| 4 | Flood fill (LC 733) | Easy | DFS | [GraphTraversals](GraphTraversals.java) |
| 5 | Rotting oranges (LC 994) | Medium | multi-source BFS | [GraphTraversals](GraphTraversals.java) |
| 6 | 01 Matrix (LC 542) | Medium | multi-source BFS from zeros | [GraphTraversals](GraphTraversals.java) |
| 7 | Surrounded regions (LC 130) | Medium | DFS from border | [GraphTraversals](GraphTraversals.java) |
| 8 | Clone graph (LC 133) | Medium | DFS + map | [GraphTraversals](GraphTraversals.java) |
| 9 | Cycle in undirected graph | Medium | BFS/DFS with parent | [CycleTopoSort](CycleTopoSort.java) |
| 10 | Cycle in directed graph | Medium | 3-colour DFS | [CycleTopoSort](CycleTopoSort.java) |
| 11 | Topological sort | Medium | DFS stack / Kahn BFS | [CycleTopoSort](CycleTopoSort.java) |
| 12 | Course schedule I/II (LC 207/210) | Medium | Kahn | [CycleTopoSort](CycleTopoSort.java) |
| 13 | Is graph bipartite (LC 785) | Medium | BFS colouring | [CycleTopoSort](CycleTopoSort.java) |
| 14 | Alien dictionary (LC 269) | Hard | build edges from adjacent words + topo | [CycleTopoSort](CycleTopoSort.java) |
| 15 | Find eventual safe states (LC 802) | Medium | reverse graph + Kahn | [CycleTopoSort](CycleTopoSort.java) |
| 16 | Shortest path unweighted | Easy | BFS | [ShortestPaths](ShortestPaths.java) |
| 17 | Dijkstra / Network delay time (LC 743) | Medium | heap | [ShortestPaths](ShortestPaths.java) |
| 18 | Bellman-Ford + negative cycle | Medium | relax V−1 times | [ShortestPaths](ShortestPaths.java) |
| 19 | Floyd–Warshall | Medium | via k | [ShortestPaths](ShortestPaths.java) |
| 20 | Cheapest flights within K stops (LC 787) | Medium | BFS by stops / Bellman-Ford K+1 rounds | [ShortestPaths](ShortestPaths.java) |
| 21 | Path with minimum effort (LC 1631) | Medium | Dijkstra on max edge | [ShortestPaths](ShortestPaths.java) |
| 22 | Shortest path in DAG | Medium | topo + relax | [ShortestPaths](ShortestPaths.java) |
| 23 | Word ladder (LC 127) | Hard | BFS over word mutations | [ShortestPaths](ShortestPaths.java) |
| 24 | MST – Prim & Kruskal | Medium | heap / DSU | [MinimumSpanningTree](MinimumSpanningTree.java) |
| 25 | Min cost to connect all points (LC 1584) | Medium | Prim O(V²) | [MinimumSpanningTree](MinimumSpanningTree.java) |
| 26 | Number of operations to make network connected (LC 1319) | Medium | DSU | [MinimumSpanningTree](MinimumSpanningTree.java) |
| 27 | Kosaraju SCC | Hard | 2 DFS passes | [AdvancedGraphs](AdvancedGraphs.java) |
| 28 | Critical connections / bridges (LC 1192) | Hard | Tarjan low-link | [AdvancedGraphs](AdvancedGraphs.java) |
| 29 | Articulation points | Hard | Tarjan | [AdvancedGraphs](AdvancedGraphs.java) |
| 30 | Accounts merge (LC 721) | Medium | DSU on emails | see 19 |
| 31 | Number of islands II (LC 305) | Hard | DSU online | [GraphProblemsIII](GraphProblemsIII.java) |
| 32 | Swim in rising water (LC 778) | Hard | Dijkstra / binary search + BFS | [GraphProblemsIII](GraphProblemsIII.java) |
| 33 | Reconstruct itinerary (LC 332) | Hard | Hierholzer Euler path | [GraphProblemsIII](GraphProblemsIII.java) |
| 34 | Max area of island (LC 695) | Medium | DFS returning size | [GraphProblemsII](GraphProblemsII.java) |
| 35 | Number of enclaves (LC 1020) | Medium | flood from border, count rest | [GraphProblemsII](GraphProblemsII.java) |
| 36 | Pacific Atlantic water flow (LC 417) | Medium | reverse flow from both oceans | [GraphProblemsII](GraphProblemsII.java) |
| 37 | Walls and gates (LC 286) | Medium | multi-source BFS from gates | [GraphProblemsII](GraphProblemsII.java) |
| 38 | Number of distinct islands (LC 694) | Medium | shape signature (relative coords) in a set | [GraphProblemsII](GraphProblemsII.java) |
| 39 | Number of connected components (LC 323) | Medium | DFS / DSU | [GraphProblemsII](GraphProblemsII.java) |
| 40 | Graph valid tree (LC 261) | Medium | n−1 edges + connected (DSU) | [GraphProblemsII](GraphProblemsII.java) |
| 41 | Shortest path in binary matrix (LC 1091) | Medium | BFS with 8 directions | [GraphProblemsII](GraphProblemsII.java) |
| 42 | Evaluate division (LC 399) | Medium | weighted graph DFS / weighted DSU | [GraphProblemsII](GraphProblemsII.java) |
| 43 | Minimum multiplications to reach end (GFG) | Medium | BFS over states mod 100000 | [GraphProblemsII](GraphProblemsII.java) |
| 44 | Find the city with the smallest number of neighbours (LC 1334) | Medium | Floyd–Warshall | [GraphProblemsII](GraphProblemsII.java) |
| 45 | Number of ways to arrive at destination (LC 1976) | Medium | Dijkstra + count ways mod | [GraphProblemsII](GraphProblemsII.java) |
| 46 | Making a large island (LC 827) | Hard | label islands with sizes, try each 0 | [GraphProblemsIII](GraphProblemsIII.java) |
| 47 | Bus routes (LC 815) | Hard | BFS over routes, stop → routes map | [GraphProblemsIII](GraphProblemsIII.java) |
| 48 | Word ladder II (LC 126) | Hard | BFS levels + backtrack paths | [GraphProblemsIII](GraphProblemsIII.java) |
