# LC 1368 — Minimum Cost to Make at Least One Valid Path in a Grid (Hard)

**Pattern:** 0-1 BFS (deque) on implicit edge weights 0/1 · **Target:** O(R · C) · **Code:** [MinimumCostToMakeValidPath.java](MinimumCostToMakeValidPath.java)

## Problem
Each cell has an arrow (1 right, 2 left, 3 down, 4 up). Following arrows is free; changing a cell's arrow costs 1 (once per cell). Return the minimum cost so that there is a valid path from (0,0) to (R−1,C−1).

`[[1,1,1,1],[2,2,2,2],[1,1,1,1],[2,2,2,2]] → 3`

## Why engineers fear it
It looks like a search over modifications. The reframe: moving **in** the arrow's direction costs 0, moving in any **other** direction costs 1 (you'd change that cell's arrow). Now it's a shortest path with weights 0 and 1.

## Approach 1 — Dijkstra
Edges of weight 0/1, standard Dijkstra with a heap. **O(RC log(RC))**.

## Approach 2 — 0-1 BFS (optimal)
Deque instead of heap: when relaxing a 0-weight edge, push the neighbour to the **front**; 1-weight edge → **back**. The deque always holds nodes with distance `d` at the front and `d + 1` at the back, so it behaves like Dijkstra without the log factor. **O(RC)**.

## Approach 3 — BFS layers with DFS "free spread"
From the current frontier, DFS along arrows marking every cell reachable at the **same cost** (cost-0 closure). Then expand all of them by one paid move to form the next layer. Same O(RC); intuitive "flood then pay" picture.

## Why changing a cell at most once is fine
A shortest path never visits a cell twice, so each cell's arrow only needs to point to the next cell of the path — at most one change per cell, and the edge-weight model counts exactly that.

## Pitfalls
- Direction indices: map arrow values 1..4 to `(0,1),(0,−1),(1,0),(−1,0)`.
- In 0-1 BFS, skip a popped node if its stored distance is smaller than the popped one (or use `dist` comparisons when pushing).
- Start = end (1×1 grid) → 0.

## Follow-ups
- **Minimum obstacle removal to reach corner** (LC 2290) — 0-1 BFS (obstacle = cost 1).
- **Shortest path in a grid with obstacles elimination** (LC 1293) — BFS over (cell, removals left).
- Any graph with weights in {0, 1} (or small integers → Dial's algorithm).

## 30-second interview script
"Think of each cell as a node with four outgoing edges: following its arrow costs 0, any other direction costs 1 because I'd have to change that arrow. That's a shortest path with 0/1 weights, so I use 0-1 BFS — a deque where free moves go to the front and paid moves to the back. Each cell is finalised once: O(R·C)."
