# LC 84 — Largest Rectangle in Histogram (Hard)

**Pattern:** monotonic increasing stack → previous/next smaller element · **Target:** O(n) · **Code:** [LargestRectangleInHistogram.java](LargestRectangleInHistogram.java)

## Problem
Bars of width 1 with heights `h`. Find the area of the largest rectangle that fits entirely under the histogram.

`[2,1,5,6,2,3] → 10` (bars 5 and 6, height 5, width 2)

## Why engineers fear it
The one-pass stack solution is short but subtle: when you pop, you compute the area for the **popped** bar using the **new top** and the **current index** as boundaries.

## Key insight
In the optimal rectangle, some bar `i` is the shortest, and the rectangle extends as far as possible while bars are ≥ `h[i]`:
`area(i) = h[i] × (nextSmaller(i) − prevSmaller(i) − 1)`. The answer is the max over i.

## Approach 1 — Brute force over pairs
For every `(l, r)`, track the running min height. **O(n²)**.

## Approach 2 — Divide and conquer
The minimum bar of a range splits it: `best = max(min · width, left part, right part)`. **O(n log n)** average, **O(n²)** on sorted input (use a segment tree for range-min to guarantee O(n log n)).

## Approach 3 — Two passes: previous & next smaller (clear)
Compute `prevSmaller[]` and `nextSmaller[]` with a monotonic stack each, then take the max of `h[i] × (next − prev − 1)`. **O(n)**, three passes, very easy to explain.

## Approach 4 — One-pass stack (optimal)
Keep indices with **increasing** heights. Append a sentinel height 0 at the end.
```
for i in 0..n:                     # i == n uses height 0
    while stack and h[top] > h[i]:
        height = h[pop()]
        left = stack.top if stack else -1        # previous smaller
        area = height × (i − left − 1)           # i is the next smaller
    push i
```
When a bar is popped, the current `i` is the first bar to its right that's smaller, and the new top is the last bar to its left that's smaller — so its maximal width is known exactly. **O(n)** time, **O(n)** space.

## Dry run — `[2,1,5,6,2,3]`
| i | h[i] | pops (height × width) | stack (heights) |
|---|---|---|---|
| 0 | 2 | – | [2] |
| 1 | 1 | 2×1=2 | [1] |
| 2 | 5 | – | [1,5] |
| 3 | 6 | – | [1,5,6] |
| 4 | 2 | 6×1=6, 5×2=**10** | [1,2] |
| 5 | 3 | – | [1,2,3] |
| 6 | 0 | 3×1, 2×4=8, 1×6=6 | [] |
Answer **10**.

## Pitfalls
- Forgetting the sentinel → bars left in the stack are never evaluated.
- Width uses `i − left − 1` where `left` is the **new** top after popping.
- Equal heights: `>` or `>=` both work for the maximum (one of the equal bars gets the full width).

## Follow-ups
- **Maximal rectangle** in a binary matrix (LC 85) — histogram per row (next file).
- **Sum of subarray minimums** (LC 907) — same prev/next-smaller contributions.
- **Maximum score of a good subarray** (LC 1793) — rectangle must contain index k → two pointers.

## 30-second interview script
"The best rectangle uses some bar as its shortest and extends to the nearest smaller bar on each side. A stack of increasing heights finds those boundaries: when a bar is popped because the current bar is smaller, the current index is its right boundary and the new stack top is its left boundary, so I compute its area right there. With a zero-height sentinel it's one O(n) pass."
