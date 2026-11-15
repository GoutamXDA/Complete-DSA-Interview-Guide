# LC 1192 — Critical Connections in a Network (Hard)

**Pattern:** Tarjan's bridge-finding with discovery times and low-links · **Target:** O(V + E) · **Code:** [CriticalConnections.java](CriticalConnections.java)

## Problem
An undirected connected graph of `n` servers. A **critical connection** (bridge) is an edge whose removal disconnects the graph. Return all of them.

`n=4, edges [[0,1],[1,2],[2,0],[1,3]] → [[1,3]]`

## Why engineers fear it
Tarjan's low-link algorithm is short but subtle: what exactly `low` means, why the parent edge is skipped, and why the bridge condition is `low[v] > tin[u]` (strict).

## Approach 1 — Remove each edge and test connectivity
For each edge, delete it and BFS/DFS to check if the graph stays connected. **O(E · (V + E))**. Simple and a perfect oracle.

## Approach 2 — Tarjan's algorithm (optimal)
Run one DFS, assigning `tin[u]` = discovery time. Define
`low[u]` = the smallest discovery time reachable from `u`'s DFS subtree using **at most one back edge**.
```
dfs(u, parent):
    tin[u] = low[u] = timer++
    for v in adj[u]:
        if v == parent: continue                  # don't use the tree edge back up
        if v unvisited:
            dfs(v, u)
            low[u] = min(low[u], low[v])
            if low[v] > tin[u]: bridge (u, v)     # v's subtree can't reach u or above without (u, v)
        else:
            low[u] = min(low[u], tin[v])          # back edge
```
**Why `low[v] > tin[u]`:** if the subtree of `v` could reach `u` or any ancestor of `u` through a back edge, removing `(u, v)` wouldn't disconnect it. `low[v] ≤ tin[u]` means it can; strictly greater means it can't → bridge.
**O(V + E)** time and space.

### Parallel edges
If the graph can contain duplicate edges, skip the parent **edge id**, not the parent vertex — two parallel edges between u and v are never bridges.

## Dry run — triangle 0-1-2 plus 1-3
DFS 0 (tin 0) → 1 (tin 1) → 2 (tin 2): back edge to 0 → low[2] = 0 → low[1] = 0. Then 1 → 3 (tin 3): low[3] = 3 > tin[1] = 1 → **bridge (1, 3)**. Edge (0,1): low[1] = 0 ≤ tin[0] = 0 → not a bridge.

## Pitfalls
- Using `low[v]` instead of `tin[v]` for back edges (works for bridges, breaks articulation points; use `tin[v]` for clarity).
- Recursion depth with n = 10⁵ in Java → iterative DFS or a bigger thread stack (the Java file includes an iterative version).
- Disconnected graphs → run DFS from every unvisited node.

## Follow-ups
- **Articulation points** — `low[v] ≥ tin[u]` for non-root `u`; root with ≥ 2 DFS children.
- **2-edge-connected components** — remove bridges, take components.
- **Strongly connected components** (Tarjan/Kosaraju) — directed version of low-links.

## 30-second interview script
"One DFS with discovery times; low[u] is the earliest discovery time reachable from u's subtree using one back edge. After visiting a child v I take low[u] = min(low[u], low[v]); if low[v] is still greater than tin[u], v's subtree has no way back above u except the edge (u, v), so it's a bridge. Back edges update low with tin[v], and I skip the edge to the parent. O(V + E)."
