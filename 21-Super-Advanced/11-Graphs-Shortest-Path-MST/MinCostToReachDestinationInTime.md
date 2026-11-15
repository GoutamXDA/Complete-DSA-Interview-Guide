# LC 1928 — Minimum Cost to Reach Destination in Time (Hard)

**Pattern:** constrained shortest path — DP over (time, node) or Dijkstra on cost with time pruning · **Target:** O(maxTime · E) · **Code:** [MinCostToReachDestinationInTime.java](MinCostToReachDestinationInTime.java)

## Problem
Cities `0..n−1`, roads `[u, v, time]` (undirected, may be parallel), and a passing fee `fee[i]` paid every time you're in city `i` (including start and end). Reach city `n−1` from `0` within `maxTime`, minimising total fees. Return −1 if impossible.

`maxTime=30, edges=[[0,1,10],[1,2,10],[2,5,10],[0,3,1],[3,4,10],[4,5,15]], fee=[5,1,2,20,20,3] → 11`

## Why engineers fear it
Two metrics (time and cost). Plain Dijkstra on cost is wrong: the cheapest way to reach a node might be too slow, while a more expensive but faster arrival could lead to a feasible answer later.

## Approach 1 — DP over time (Bellman-Ford style)
`dp[t][v]` = minimum fee to be at `v` having used exactly `t` time.
`dp[t + w][v] = min(dp[t + w][v], dp[t][u] + fee[v])` for each edge `(u, v, w)` with `t + w ≤ maxTime`.
Answer `min_t dp[t][n−1]`. **O(maxTime · E)** time, O(maxTime · n) space. Deterministic and easy to prove.

## Approach 2 — Dijkstra on cost with "best time" pruning (fast in practice)
Priority = cost. State = (cost, time, node). Keep `bestTime[v]` = the smallest time with which `v` has been popped.
- Pop `(c, t, u)`. If `u == n−1` → answer `c` (first pop is cheapest).
- Skip if `t ≥ bestTime[u]` — an earlier pop reached `u` both **cheaper** (heap order) and **faster**, so this state is dominated.
- Otherwise set `bestTime[u] = t` and push neighbours with `t + w ≤ maxTime`.
**Why it's correct:** a state is only useful if it's not dominated in both metrics (Pareto front). Popping in cost order means every later state at `u` is at least as expensive; only a strictly faster one can still help.
Worst-case bounded by O(maxTime · E log), typically much faster.

## Approach 3 — Dijkstra on time first (pruning helper)
Precompute the shortest **time** from every node to `n−1` (Dijkstra from the destination). Prune any state where `t + minTimeToEnd[u] > maxTime`. Combine with Approach 2 for big speed-ups on hard tests.

## Pitfalls
- The start city's fee counts: initial cost = `fee[0]`.
- Parallel edges: keep the minimum time per pair or just keep all.
- Don't use the standard `dist[u]` pruning on cost alone — it discards fast-but-expensive states.

## Follow-ups
- **Cheapest flights within K stops** (LC 787) — constraint on the number of edges instead of time.
- **Shortest path with fuel / battery** — state (node, fuel), same idea.
- **Resource-constrained shortest path** in general is NP-hard; pseudo-polynomial DP over the resource is the standard technique.

## 30-second interview script
"Two criteria, so plain Dijkstra on cost can throw away a faster route. The clean solution is a DP over time: dp[t][v] is the cheapest way to be at v after exactly t minutes, relaxing every edge — O(maxTime·E). In practice I'd run Dijkstra by cost and only expand a node again if I arrive strictly faster than any earlier, cheaper visit, since cheaper-and-faster dominates."
