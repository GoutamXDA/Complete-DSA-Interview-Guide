# LC 1499 — Max Value of Equation (Hard)

**Pattern:** algebraic split + monotonic deque over a constrained window · **Target:** O(n) · **Code:** [MaxValueOfEquation.java](MaxValueOfEquation.java)

## Problem
Points `(x, y)` sorted by strictly increasing `x`. Maximise `yi + yj + |xi − xj|` over pairs `i < j` with `|xi − xj| ≤ k`. At least one valid pair exists.

`[[1,3],[2,0],[5,10],[6,-10]], k = 1 → 4` (points 0 and 1: 3 + 0 + 1)

## Why engineers fear it
It looks like a pairwise O(n²) geometry problem. The trick is an algebraic rewrite that separates `i` and `j`.

## Key insight — split the expression
Since `xj > xi`: `yi + yj + xj − xi = (yj + xj) + (yi − xi)`.
For each `j`, we need the **maximum of `yi − xi`** over earlier `i` with `xj − xi ≤ k` — a **sliding-window maximum** where the window is defined by `x`, not by count.

## Approach 1 — Brute force
All pairs with the distance check. **O(n²)**; with sorted x you can `break` once `xj − xi > k`, but the worst case stays quadratic.

## Approach 2 — Max-heap of (yi − xi, xi)
For each `j`: pop heap tops whose `x` is too far (`xj − x > k`) — lazy expiry — then use the top. Push `(yj − xj, xj)`. **O(n log n)**.

## Approach 3 — Monotonic deque (optimal)
Deque of indices with **decreasing `y − x`**:
```
for j:
    while dq and x[j] - x[dq.front] > k: pop front         # out of range forever (x increases)
    if dq: best = max(best, y[j] + x[j] + (y[front] - x[front]))
    while dq and y[back] - x[back] <= y[j] - x[j]: pop back  # dominated: j is newer AND better
    push j
```
**Why expiry is permanent:** `x` strictly increases, so once `i` is too far from `j` it's too far from every later point.
**Why domination is safe:** a newer index with larger `y − x` stays in range longer and contributes more.
**O(n)** time, **O(n)** space.

## Dry run — `[[1,3],[2,0],[5,10],[6,-10]]`, k = 1
| j | deque (y−x) after expiry | candidate | push |
|---|---|---|---|
| 0 | – | – | [2] |
| 1 | [2] | 0+2+2 = 4 | [2, −2] |
| 2 | expire both (5−1, 5−2 > 1) | – | [5] |
| 3 | [5] | −10+6+5 = 1 | … |
Answer **4**.

## Pitfalls
- Query **before** pushing `j` (i must be strictly before j).
- Use `long` if coordinates are large.
- Don't forget the expression uses `xj − xi` (not absolute after the rewrite) because x is sorted.

## Follow-ups
- Same split trick: **best sightseeing pair** (LC 1014) — `(a[i] + i) + (a[j] − j)` with no window.
- **Maximum of |arr1[i] − arr1[j]| + |arr2[i] − arr2[j]| + |i − j|** (LC 1131) — expand absolute values into 4 sign cases.
- Any `f(i) + g(j)` objective under a sliding constraint → deque or heap.

## 30-second interview script
"Because x is sorted, the objective is (yj + xj) + (yi − xi). So for each j I need the max of yi − xi among points within distance k behind it — a sliding window maximum keyed by x. A monotonic deque does it in O(n): expire points too far left, read the front, then push j after popping dominated entries."
