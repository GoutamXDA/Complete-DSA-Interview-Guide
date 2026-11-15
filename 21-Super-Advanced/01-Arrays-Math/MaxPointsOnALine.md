# LC 149 — Max Points on a Line (Hard)

**Pattern:** geometry + hashing with exact (reduced-fraction) slopes · **Target:** O(n²) · **Code:** [MaxPointsOnALine.java](MaxPointsOnALine.java)

## Problem
Given points on a 2D plane, return the maximum number of points that lie on one straight line.

`[[1,1],[2,2],[3,3]] → 3` · `[[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]] → 4`

## Why engineers fear it
It looks simple, but **floating-point slopes are wrong** (precision collisions like `1/3` vs `0.33333334`), vertical lines divide by zero, and duplicate points (in variants) break naive counting.

## Key insight
Fix an anchor point `p`. Every line through `p` is identified by its **direction**. Represent the direction exactly as a reduced fraction `(dy/g, dx/g)` with a **canonical sign**. Points with the same key are collinear with `p`.

## Approach 1 — Brute force over pairs (O(n³))
For every pair `(i, j)` define a line, then count every `k` with zero cross product:
`(xj − xi)(yk − yi) − (yj − yi)(xk − xi) == 0`. Integer arithmetic, no division, always correct.
**Time** O(n³) · **Space** O(1). Fine for n ≤ 300 and a great correctness oracle.

## Approach 2 — Anchor + slope hash map (optimal O(n²))
```
for each i:
    map.clear(); duplicates = 0; best_i = 0
    for each j > i:
        dx = xj - xi, dy = yj - yi
        if dx == 0 and dy == 0: duplicates++; continue
        g = gcd(dx, dy); dx /= g; dy /= g
        if dx < 0 or (dx == 0 and dy < 0): dx = -dx; dy = -dy   # canonical sign
        best_i = max(best_i, ++map[(dx, dy)])
    answer = max(answer, best_i + duplicates + 1)
```
**Why only `j > i`:** a line's best count is found when the anchor is its first point.
**Time** O(n² log C) (gcd) · **Space** O(n).

### Why a canonical sign matters
`(1, 2)` and `(−1, −2)` describe the same direction. Without normalising, collinear points on opposite sides of the anchor would be split into two buckets.

### Key encoding
Use a `long` key `dx * K + dy` (or a string `"dx/dy"`). With coordinates ≤ 10⁴, differences are ≤ 2·10⁴, so `K = 100003` is collision-free.

## Pitfalls
- `double` slopes → wrong answers on large coordinates.
- Vertical lines (`dx = 0`) — handled naturally by the gcd form: `(0, 1)`.
- Horizontal lines → `(1, 0)`.
- Duplicate points (not in LC 149 constraints, but common in variants) must be added to every line through the anchor.
- n ≤ 2 → answer is n.

## Follow-ups
- **Line reflection** (LC 356) — symmetric about a vertical line.
- **Minimum lines to cover all points** (LC 2152) — bitmask DP on top of this collinearity test.
- **Check if it is a straight line** (LC 1232) — the cross-product test.

## 30-second interview script
"I fix each point as an anchor and group every later point by the direction vector to it, reduced by gcd and sign-normalised so the key is exact — no floating point. The largest group plus the anchor (and any duplicates) is the best line through that anchor. That's O(n²) with a hash map; the cross-product O(n³) version is my brute force."
