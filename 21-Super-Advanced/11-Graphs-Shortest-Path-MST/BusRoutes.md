# LC 815 — Bus Routes (Hard)

**Pattern:** BFS on the right graph — routes as nodes, not stops · **Target:** O(Σ route lengths) · **Code:** [BusRoutes.java](BusRoutes.java)

## Problem
`routes[i]` lists the stops bus `i` loops through forever. Starting at stop `source`, find the minimum number of **buses** to reach `target` (−1 if impossible).

`routes=[[1,2,7],[3,6,7]], source=1, target=6 → 2`

## Why engineers fear it
The obvious graph (stops as nodes, edges between consecutive stops) has the wrong cost — riding many stops on one bus costs 1. Building "stop → stop if same route" edges is O(Σ len²) and times out (routes have up to 10⁵ stops).

## Approach 1 — Stop graph with all pairwise edges
Connect every pair of stops on the same route (cost 1) and BFS. **O(Σ len²)** edges — too many for big routes.

## Approach 2 — BFS over routes (optimal)
Build `stopToRoutes`. BFS where each **level is one more bus**:
```
queue = all routes containing source (level 1)
while queue:
    for route in level:
        if target in route: return level
        for stop in route:
            for nextRoute in stopToRoutes[stop]: if unseen → enqueue
    level++
```
Each route is expanded once and each stop's route list is scanned once (mark stops visited too) → **O(Σ route lengths)** time and space.

## Approach 3 — Bipartite BFS (stops and routes as two node types)
Alternate stop → route (cost 1) → stop (cost 0). Same complexity; a nice general modelling trick for "hub" problems. 0-1 BFS can handle the mixed weights directly.

## Pitfalls
- `source == target` → 0 buses.
- Mark **routes** as used (and ideally stops) or the BFS revisits big routes repeatedly.
- Membership test `target in route` — precompute with a set per route, or check stop by stop while expanding.

## Follow-ups
- **Cheapest flights within K stops** (LC 787) — bounded edges Bellman-Ford.
- **Word ladder** (LC 127) — another "build the right neighbour relation" BFS.
- **Minimum transfers** with weights → Dijkstra on the bipartite graph.

## 30-second interview script
"The cost is buses, not stops, so I BFS over routes: the first level is every route through the source, and from a route I can switch to any route sharing one of its stops. A map from stop to routes gives the neighbours, and marking routes and stops visited means every stop list is scanned once. The level at which a route contains the target is the answer. Linear in the total route length."
