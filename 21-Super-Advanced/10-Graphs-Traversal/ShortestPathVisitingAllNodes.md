# LC 847 — Shortest Path Visiting All Nodes (Hard)

**Pattern:** BFS over states `(node, visitedMask)` — multi-source · **Target:** O(2ⁿ · n²) with n ≤ 12 · **Code:** [ShortestPathVisitingAllNodes.java](ShortestPathVisitingAllNodes.java)

## Problem
An undirected connected graph with `n ≤ 12` nodes. Return the length of the shortest walk that visits every node. You may start and end anywhere, revisit nodes and reuse edges.

`[[1,2,3],[0],[0],[0]] → 4` (1 → 0 → 2 → 0 → 3)

## Why engineers fear it
It's a TSP-like problem: plain BFS on nodes is wrong (revisits are allowed and needed), and plain DFS explodes. The key is to BFS on an **expanded state space**.

## Key insight
The state must include **which nodes have been visited**, because being at node 0 having seen {0,1} is different from being at node 0 having seen {0,1,2}. With n ≤ 12, there are n · 2ⁿ ≈ 49k states — small.

## Approach 1 — Bitmask DP via all-pairs distances (Held–Karp)
1. BFS from every node → `dist[i][j]` (unweighted shortest paths).
2. `dp[mask][i]` = shortest walk that has visited exactly the nodes in `mask` (as targets) and ends at `i`; transition `dp[mask | 1<<j][j] = min(dp[mask][i] + dist[i][j])`.
Answer `min_i dp[full][i]`. **O(2ⁿ · n²)** time, O(2ⁿ · n) space.

## Approach 2 — Multi-source BFS over (node, mask) states (optimal & simplest)
- Start from **every** node at once: states `(i, 1 << i)` with distance 0 (because the start is free).
- BFS: from `(u, mask)` move to `(v, mask | 1 << v)` for each neighbour `v`.
- The first time any state has `mask == full`, its distance is the answer.
All edges have weight 1, so BFS gives shortest distances. **O(2ⁿ · E)** time, **O(2ⁿ · n)** space.

### Why multi-source
Starting anywhere is the same as adding a virtual super-source connected to every `(i, {i})` with cost 0 — BFS from all of them simultaneously.

## Approach 3 — A* / bidirectional (talking point)
Heuristic = number of unvisited nodes (admissible since each needs ≥ 1 step). Faster in practice; same worst case.

## Pitfalls
- Visited set must be over **(node, mask)**, not node.
- n = 1 → answer 0.
- Use `int[n][1 << n]` (or a boolean array) instead of `HashSet<String>` for speed.

## Follow-ups
- **Find the shortest superstring** (LC 943) — Held–Karp over strings with overlap weights.
- **Minimum cost to visit every node** with weights → Dijkstra/Held–Karp over the same states.
- **Shortest path to get all keys** (LC 864) — BFS over (cell, keysMask).

## 30-second interview script
"Because revisits are allowed, the state has to be (current node, set of visited nodes). With n ≤ 12 that's about 49k states. I run a BFS starting from every (i, {i}) at distance 0, moving along edges and OR-ing the new node into the mask; the first state whose mask is full gives the answer. O(2^n · E). Held–Karp DP over all-pairs distances is the equivalent DP formulation."
