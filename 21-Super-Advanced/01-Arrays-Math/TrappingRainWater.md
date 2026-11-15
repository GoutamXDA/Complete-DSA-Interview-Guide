# LC 42 — Trapping Rain Water (Hard)

**Pattern:** prefix maxima → two pointers / monotonic stack · **Target:** O(n) time, O(1) space · **Code:** [TrappingRainWater.java](TrappingRainWater.java)

## Problem
Bars of width 1 have heights `h[i]`. After rain, how many units of water are trapped between bars?

`[0,1,0,2,1,0,1,3,2,1,2,1] → 6` · `[4,2,0,3,2,5] → 9`

## Why engineers fear it
Thinking "find the containers" leads to messy case analysis. The breakthrough is to think **per column**, not per container.

## Key insight
Water above column `i` = `min(maxLeft(i), maxRight(i)) − h[i]` where both maxima include `i` itself. Everything else is about computing those maxima cheaply.

## Approach 1 — Brute force
For each `i`, scan left and right for the maxima. **O(n²)** time, O(1) space.

## Approach 2 — Prefix / suffix maximum arrays
`L[i] = max(L[i−1], h[i])`, `R[i] = max(R[i+1], h[i])`, sum `min(L, R) − h`. **O(n)** time, **O(n)** space. Easiest correct answer — say it first.

## Approach 3 — Monotonic decreasing stack (horizontal layers)
Keep indices with decreasing heights. When `h[i]` is taller than the top, the top is a "bottom" bounded by the new top (left wall) and `i` (right wall):
`water += (i − left − 1) × (min(h[left], h[i]) − h[bottom])`.
This fills water **layer by layer horizontally**. **O(n)** time, O(n) space. Useful because the same idea generalises to "next greater" problems.

## Approach 4 — Two pointers (optimal)
Keep `l`, `r`, `leftMax`, `rightMax`.
- If `h[l] < h[r]`: the right side already has a wall ≥ `h[r] > h[l]`, so the water at `l` is decided by `leftMax` alone → add `leftMax − h[l]`, move `l`.
- Otherwise symmetric on the right.

**Proof sketch:** let `leftMax = h[p]` for some `p ≤ l`. When `p` was processed, we were in the branch `h[p] < h[r']` for the right pointer `r'` at that moment. Since `r` only moves left, `r' ≥ r`, so `h[r']` lies in `h[r..n−1]` and is to the right of `l`. Hence `leftMax ≤ h[r'] ≤ maxRight(l)`. Therefore `min(maxLeft(l), maxRight(l)) = leftMax`, and the water at `l` is exactly `leftMax − h[l]`. The right side is symmetric. **O(n)** time, **O(1)** space.

## Dry run (two pointers) — `[4, 2, 0, 3, 2, 5]`
| l | r | leftMax | rightMax | added | total |
|---|---|---|---|---|---|
| 0 | 5 | 4 | – | 0 | 0 |
| 1 | 5 | 4 | – | 2 | 2 |
| 2 | 5 | 4 | – | 4 | 6 |
| 3 | 5 | 4 | – | 1 | 7 |
| 4 | 5 | 4 | – | 2 | 9 |
Answer **9**.

## Pitfalls
- Forgetting that maxima include the bar itself (avoids negative water).
- In the stack version, `break` if the stack becomes empty after popping the bottom (no left wall).
- Use `long` if heights × width can overflow `int`.

## Follow-ups
- **Trapping Rain Water II** (2D, LC 407) — min-heap from the border inward (see `09-Heaps`).
- **Container With Most Water** (LC 11) — two pointers but a different objective (one container, not sum).
- **Pour water** (LC 755) — simulation.

## 30-second interview script
"Water on a column is min(max to the left, max to the right) minus its height. Prefix/suffix max arrays give O(n) time and space. To get O(1) space I use two pointers: whichever side has the smaller current height, the other side is guaranteed to have a taller wall, so that column's water depends only on its own side's running max."
