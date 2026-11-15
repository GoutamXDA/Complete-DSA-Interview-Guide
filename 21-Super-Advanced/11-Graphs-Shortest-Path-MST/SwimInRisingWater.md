# LC 778 — Swim in Rising Water (Hard)

**Pattern:** minimax path — Dijkstra on max, binary search + BFS, or Kruskal/DSU · **Target:** O(n² log n) · **Code:** [SwimInRisingWater.java](SwimInRisingWater.java)

## Problem
`n × n` grid of distinct elevations (a permutation of 0..n²−1). At time `t` you can be on any cell with elevation ≤ `t`, moving 4-directionally instantly. Minimum `t` to get from (0,0) to (n−1,n−1)?

`[[0,2],[1,3]] → 3`

## Why engineers fear it
It's not a sum-of-weights shortest path; it's "minimise the **maximum** cell on the path". Three different classic techniques all solve it — the interview tests whether you see the minimax structure.

## Approach 1 — Binary search on t + BFS/DFS
`reachable(t)`: flood from (0,0) through cells ≤ t. Monotonic in `t` → binary search over `[grid[0][0], n²−1]`. **O(n² log n²)**.

## Approach 2 — Dijkstra with max instead of +
`cost(path) = max elevation on the path`. Dijkstra's greedy works for any **monotone** path cost (adding a cell never decreases it). Pop the cell with the smallest `cost`; relax neighbours with `max(cost, h[nb])`. **O(n² log n)**.

## Approach 3 — Kruskal / DSU by increasing elevation
Process cells in increasing elevation (it's a permutation, so `pos[v]` gives the cell with elevation v). Activate each cell and union with active neighbours. The first time (0,0) and (n−1,n−1) are connected, the current elevation is the answer. **O(n² α(n²))** — the minimum spanning tree view of minimax paths.

## Why the three agree
The minimax path value between two nodes equals the bottleneck in the **minimum spanning tree** — Dijkstra-on-max, binary search on the threshold, and Kruskal all compute that bottleneck.

## Pitfalls
- The start cell's height counts: answer ≥ `grid[0][0]`.
- Mark visited when popped (Dijkstra) or when pushed (BFS) consistently.
- In the DSU version, `pos[]` needs the permutation property (otherwise sort cells).

## Follow-ups
- **Path with minimum effort** (LC 1631) — minimax over edge differences.
- **Trapping rain water II** (LC 407) — minimax from the border.
- **Checking existence of edge length limited paths** (LC 1697) — offline queries + DSU sorted by weight.

## 30-second interview script
"The time needed is the maximum elevation along the best path, so it's a minimax path problem. Dijkstra still works if the path cost is the running max instead of a sum, since it's monotone: pop the lowest current cost and relax neighbours with max(cost, height). Equivalently I could binary search the time with a flood fill, or add cells in increasing height with union-find until start and end connect."
