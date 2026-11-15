# LC 305 — Number of Islands II (Hard)

**Pattern:** online connectivity with Union-Find (path compression + union by rank/size) · **Target:** O(k · α(mn)) · **Code:** [NumberOfIslandsII.java](NumberOfIslandsII.java)

## Problem
An `m × n` grid starts as water. `positions` lists cells turned into land, one by one. After each operation, report the number of islands (4-directional).

`m=3, n=3, positions=[[0,0],[0,1],[1,2],[2,1]] → [1,1,2,3]`

## Why engineers fear it
Re-running a BFS/DFS after each addition is O(k · mn). The online version needs Union-Find, plus care with **duplicate positions** and the island-count bookkeeping.

## Approach 1 — Recount after every addition
Flood-fill count after each update. **O(k · m · n)**.

## Approach 2 — Incremental BFS merge (labels)
Keep a label per land cell; on addition, look at neighbouring labels; relabel the smaller islands into the largest (small-to-large). **O(k log k)** amortised relabelling — works but is fiddly.

## Approach 3 — Union-Find (optimal)
- Map cell `(r, c)` → `r·n + c`; `parent = −1` means water.
- On adding a cell: if already land (duplicate) → count unchanged. Otherwise make it its own set, `count++`, then for each land neighbour: if `find(cell) != find(neighbour)`, union them and `count−−`.
- With path compression + union by size/rank: **O(α(mn))** amortised per operation → effectively O(k).

## Why each union decreases the count by exactly one
A new cell starts as its own island (+1). Each successful union merges two different islands into one (−1). Neighbours already in the same island don't change the count, which the `find` check detects.

## Pitfalls
- Duplicate positions → don't increment the count again.
- Initialising the DSU as "every cell is land" (wrong) → use a water sentinel.
- Recursion in `find` for 10⁴ × 10⁴ grids → iterative path halving.

## Follow-ups
- **Number of islands** (LC 200) — static version (DFS).
- **Bricks falling when hit** (LC 803) — reverse time + DSU (removals become additions).
- **Making a large island** (LC 827) — labels + sizes.
- Supporting **removals** online needs offline reversal or dynamic connectivity structures.

## 30-second interview script
"Each added land cell becomes its own set and increases the island count; then for each of its four land neighbours I union them, and every union between two different sets decreases the count by one. With path compression and union by size each operation is nearly constant, so the whole sequence is about O(k). Duplicates are detected by checking whether the cell is already land."
