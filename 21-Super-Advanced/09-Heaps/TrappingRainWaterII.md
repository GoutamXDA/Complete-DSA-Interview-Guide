# LC 407 — Trapping Rain Water II (Hard)

**Pattern:** Dijkstra-like "flood from the boundary" with a min-heap · **Target:** O(RC log(RC)) · **Code:** [TrappingRainWaterII.java](TrappingRainWaterII.java)

## Problem
A 2D elevation map `h[r][c]`. How much water can it trap after raining? Water escapes over the border.

```
1 4 3 1 3 2
3 2 1 3 2 4   → 4
2 3 3 2 3 1
```

## Why engineers fear it
The 1D trick (`min(maxLeft, maxRight)`) does **not** generalise — water can escape diagonally around obstacles along any path. You need the right global view: the water level of a cell is determined by the **lowest boundary you must cross to escape**.

## Key insight
Water level at a cell = the minimum, over all paths from the cell to the border, of the **maximum height** along that path (a "minimax path"). Trapped water = level − height. Computing minimax distances from the border is exactly Dijkstra with `max` instead of `+`.

## Approach 1 — Iterative relaxation (Bellman-Ford style)
Start with `level = ∞` for interior cells, `level = h` on the border. Repeatedly set `level[c] = max(h[c], min(level[neighbours]))` until nothing changes. Correct, but **O((RC)²)** worst case.

## Approach 2 — Min-heap from the border (optimal)
```
push every border cell (height = h), mark visited
while heap:
    (r, c, level) = pop smallest
    for each unvisited neighbour (nr, nc):
        water += max(0, level − h[nr][nc])
        push (nr, nc, max(level, h[nr][nc])); mark visited
```
**Why popping the lowest wall first is correct:** the current lowest boundary cell is the weakest point of the "dam". Any unvisited neighbour can only escape through some boundary cell, and the lowest one is this one — so its water level is exactly `max(level, its own height)`. This is the Dijkstra greedy-choice argument for minimax paths.
**O(RC log(RC))** time, **O(RC)** space.

## Dry run (idea)
Border cells form the initial dam. The lowest border cell (height 1) is popped first; its interior neighbour of height 1... is not lower, so no water. Later the dam level rises to 3 around the basin containing heights 2, 1, 2 → water 1 + 2 + 1 = **4**.

## Pitfalls
- Mark cells visited **when pushed**, not when popped (avoids double counting).
- Push the neighbour with `max(level, h)`, not `h`: the water raises the effective wall.
- Grids with fewer than 3 rows or columns trap nothing.

## Follow-ups
- **Trapping rain water** 1D (LC 42) — two pointers (see `01-Arrays-Math`).
- **Swim in rising water** (LC 778), **path with minimum effort** (LC 1631) — the same minimax-Dijkstra.
- **Pour water** (LC 755) — simulation.

## 30-second interview script
"The water level of a cell is the lowest possible maximum height on any escape path to the border. So I run a Dijkstra-style flood from the border with a min-heap: always expand the lowest wall cell; each new neighbour holds max(0, wall − height) water and becomes part of the wall at height max(wall, its height). O(RC log RC)."
