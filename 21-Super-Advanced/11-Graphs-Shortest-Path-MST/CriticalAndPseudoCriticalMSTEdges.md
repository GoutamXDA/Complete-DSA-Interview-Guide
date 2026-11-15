# LC 1489 — Find Critical and Pseudo-Critical Edges in MST (Hard)

**Pattern:** Kruskal with an edge **excluded** / **forced** · **Target:** O(E² α(V)) (E ≤ 200) · **Code:** [CriticalAndPseudoCriticalMSTEdges.java](CriticalAndPseudoCriticalMSTEdges.java)

## Problem
Weighted undirected connected graph. An edge is **critical** if removing it increases the MST weight (it's in every MST). It's **pseudo-critical** if it appears in some MST but not all. Return both lists of edge indices.

`n=5, edges=[[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]] → [[0,1],[2,3,4,5]]`

## Why engineers fear it
You must reason about **all** minimum spanning trees, not just the one Kruskal produces — and recognise that two cheap Kruskal variants answer both questions.

## Key facts
Let `W` = MST weight.
- Edge `e` is **critical** ⇔ MST weight **without** `e` is > `W` (or the graph becomes disconnected).
- Edge `e` is **pseudo-critical** ⇔ not critical **and** the MST weight when `e` is **forced in first** equals `W`.

## Approach 1 — Enumerate all spanning trees (oracle)
Try every subset of `V − 1` edges; keep spanning trees of minimum weight; an edge in all of them is critical, in some is pseudo-critical. Exponential — tests only.

## Approach 2 — Kruskal twice per edge (optimal for constraints)
1. Sort edges by weight (keep original indices). Compute `W` with Kruskal.
2. For each edge `e`:
   - `kruskal(skip = e)`: if the result is disconnected or > `W` → **critical**.
   - else `kruskal(force = e)`: union `e`'s endpoints first, add its weight, then run Kruskal on the rest. If the result == `W` → **pseudo-critical**.
**O(E · E α(V))** — about 40,000 operations for E = 200.

### Why forcing works
If some MST contains `e`, then starting Kruskal with `e` already chosen still yields total `W` (the cut/exchange property). If no MST contains `e`, any spanning tree containing it weighs more.

## Approach 3 — Group by equal weights + bridges (O(E log E))
Process edges in increasing weight groups. Inside a group, contract the components formed by lighter edges; an edge joining the same component is useless; among the rest, edges that are **bridges** in the group's contracted multigraph are critical and the others pseudo-critical. Faster, but much harder to code — mention as the theoretical optimum.

## Pitfalls
- Sorting loses the original indices → sort an index array.
- A skipped edge can disconnect the graph → treat as critical (weight = ∞).
- An edge that's critical must not also be reported as pseudo-critical.

## Follow-ups
- **Min cost to connect all points** (LC 1584) — plain MST (Prim O(V²) on dense graphs).
- **Second-best MST** — best swap: add a non-tree edge, remove the max edge on the cycle (LCA + binary lifting).
- **MST with one forced/forbidden edge** — exactly the two Kruskal variants here.

## 30-second interview script
"Compute the MST weight W once. For each edge, run Kruskal without it — if the tree gets heavier or the graph disconnects, it's critical. Otherwise run Kruskal with that edge forced in first; if the weight is still W, it belongs to some MST, so it's pseudo-critical. With E ≤ 200 that's O(E²·α), well within limits."
