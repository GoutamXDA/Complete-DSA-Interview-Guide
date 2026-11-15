# LC 4 — Median of Two Sorted Arrays (Hard)

**Pattern:** binary search on a **partition**, not on values · **Target:** O(log min(m, n)) · **Code:** [MedianOfTwoSortedArrays.java](MedianOfTwoSortedArrays.java)

## Problem
Two sorted arrays `A` (size m) and `B` (size n). Return the median of their union in **O(log(m+n))**.

`[1,3] + [2] → 2.0` · `[1,2] + [3,4] → 2.5`

## Why engineers fear it
Index arithmetic with four boundary values and sentinels, plus the leap from "binary search a value" to "binary search a cut".

## Approach 1 — Merge
Merge into one array and take the middle. **O(m+n)** time and space.

## Approach 2 — Merge virtually
Two pointers, stop after `(m+n)/2 + 1` steps, remembering the last two values. **O(m+n)** time, O(1) space.

## Approach 3 — k-th element by discarding k/2 (O(log(m+n)))
`kth(A, B, k)`: compare `A[k/2−1]` and `B[k/2−1]`; the smaller one's first `k/2` elements can't contain the k-th, so discard them and recurse with `k − k/2`. Median = kth for the middle one or two. Elegant and generalises to "k-th smallest of two sorted arrays".

## Approach 4 — Partition binary search (optimal, O(log min(m, n)))
Cut `A` after `i` elements and `B` after `j = (m + n + 1)/2 − i` elements so the **left half** has ⌈(m+n)/2⌉ elements.
```
Aleft = A[i-1] (or -∞), Aright = A[i] (or +∞)
Bleft = B[j-1] (or -∞), Bright = B[j] (or +∞)
valid  ⇔  Aleft ≤ Bright  and  Bleft ≤ Aright
```
- If `Aleft > Bright`, the cut in A is too far right → `hi = i − 1`.
- Else if `Bleft > Aright`, it's too far left → `lo = i + 1`.
- When valid: odd total → `max(Aleft, Bleft)`; even → `(max(Aleft, Bleft) + min(Aright, Bright)) / 2`.
Binary search over `i ∈ [0, m]` on the **shorter** array → **O(log min(m, n))**, O(1) space.

### Why searching the shorter array matters
It guarantees `j = half − i` stays within `[0, n]` for every `i ∈ [0, m]`.

## Dry run — A = [1, 3, 8, 9, 15], B = [7, 11, 18, 19, 21, 25]
Search the shorter (A, m=5), half = 6.
- i=2, j=4: Aleft=3, Aright=8, Bleft=19, Bright=21 → Bleft > Aright → lo=3
- i=4, j=2: Aleft=9, Aright=15, Bleft=11, Bright=18 → Bleft > Aright? 11 > 15 no; Aleft ≤ Bright ✓ → valid
Total 11 (odd) → median = max(9, 11) = **11**.

## Pitfalls
- Integer overflow when averaging: use `(a + (double) b) / 2`.
- Sentinels: `Integer.MIN_VALUE`/`MAX_VALUE` when a side is empty.
- One array empty → still works if you search the shorter (empty) one.

## Follow-ups
- **k-th smallest of two sorted arrays** — Approach 3, or partition with `j = k − i`.
- **Median of k sorted arrays** → binary search on the value + counting (or heap).
- **Median of a row-wise sorted matrix** — value binary search with `upper_bound` per row (see topic 06).

## 30-second interview script
"I binary search where to cut the shorter array; the cut in the other array is forced so the left side has half the elements. A cut is correct when the max of the left side of each array is ≤ the min of the right side of the other. Then the median comes from those four boundary values. O(log min(m,n))."
