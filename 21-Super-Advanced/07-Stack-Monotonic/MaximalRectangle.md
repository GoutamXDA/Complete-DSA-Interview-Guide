# LC 85 — Maximal Rectangle (Hard)

**Pattern:** reduce each row to a histogram, apply LC 84 · **Target:** O(R · C) · **Code:** [MaximalRectangle.java](MaximalRectangle.java)

## Problem
In a binary matrix of `'0'`/`'1'`, find the area of the largest rectangle containing only `'1'`s.

```
1 0 1 0 0
1 0 1 1 1      → 6
1 1 1 1 1
1 0 0 1 0
```

## Why engineers fear it
A 2D search over all rectangles is O(R²C²). The reduction to a 1D problem isn't obvious unless you've seen it.

## Approach 1 — Brute force with prefix sums
Every pair of corners, check "all ones" with a 2D prefix sum in O(1). **O(R²C²)**.

## Approach 2 — Row-pair compression
Fix the top and bottom rows; a column is usable if it's all ones in that band; find the longest run of usable columns. **O(R² · C)**.

## Approach 3 — Histogram per row (optimal)
`heights[c]` = number of consecutive `1`s ending at the current row in column `c` (reset to 0 on a `0`). Every all-ones rectangle has a bottom row; for that row it's a rectangle under the histogram `heights`. So:
```
for each row: update heights; best = max(best, largestRectangleInHistogram(heights))
```
Using the O(C) stack algorithm from LC 84 → **O(R · C)** time, **O(C)** space.

## Approach 4 — DP with left / right / height arrays
For each row maintain, per column:
- `height[c]` as above,
- `left[c]` = leftmost column the rectangle of that height can extend to (max of the previous row's left and the current run start),
- `right[c]` = rightmost (min of previous right and current run end).
Area = `height · (right − left)`. Also **O(R · C)**; avoids a stack and is popular in editorials.

## Dry run (row 3 of the example)
heights after row index 2 = `[3,1,3,2,2]` → largest rectangle = 2 × 3 = **6** (columns 2–4, height 2).

## Pitfalls
- Reset `heights[c]` to 0 on a `'0'` — don't keep the old count.
- Empty matrix / empty rows.
- The matrix is `char[][]`, compare with `'1'`.

## Follow-ups
- **Maximal square** (LC 221) — simpler DP `1 + min(top, left, diag)`.
- **Count submatrices with all ones** (LC 1504) — histogram + monotonic stack counting.
- **Largest rectangle with at most k zeros** — harder; sliding window per row band.

## 30-second interview script
"For each row I build a histogram where each column's height is the number of consecutive ones ending there. Any all-ones rectangle sits on some bottom row, so the answer is the best 'largest rectangle in histogram' over all rows. With the O(C) stack algorithm the total is O(R·C)."
