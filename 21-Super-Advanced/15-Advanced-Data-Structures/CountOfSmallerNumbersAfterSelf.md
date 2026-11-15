# LC 315 — Count of Smaller Numbers After Self (Hard)

**Pattern:** inversion counting per element — merge sort with indices, Fenwick tree, or order-statistic BST · **Target:** O(n log n) · **Code:** [CountOfSmallerNumbersAfterSelf.java](CountOfSmallerNumbersAfterSelf.java)

## Problem
For each `i`, count how many `j > i` have `nums[j] < nums[i]`.

`[5,2,6,1] → [2,1,1,0]`

## Why engineers fear it
It's the per-element version of inversion counting. You need a data structure that answers "how many smaller values have I seen?" in O(log n), or a merge sort that tracks **original indices** through the merges.

## Approach 1 — Brute force
Double loop. **O(n²)**.

## Approach 2 — Sorted list + binary search (insertion from the right)
Scan from the right; the answer for `i` is the insertion position (lower bound) of `nums[i]` in the sorted list of elements to its right; then insert. Binary search is O(log n) but `ArrayList.add(index)` shifts → **O(n²)** worst, fast in practice.

## Approach 3 — Fenwick tree over compressed values (optimal, simplest)
Compress values to ranks `1..m`. Scan from the right: `ans[i] = prefix(rank[i] − 1)`, then `add(rank[i], 1)`. **O(n log n)** time, O(n) space.

## Approach 4 — Merge sort with index array (optimal, no extra structure)
Sort **indices** by value with merge sort. While merging left half `L` and right half `R`, every time an element from `L` is placed, all right-half elements already placed (`rightTaken`) are smaller and were originally to its right → `ans[idx] += rightTaken`. **O(n log n)**.

## Approach 5 — Segment tree / BST with subtree sizes
Equivalent to Fenwick; a balanced BST (or treap) with sizes supports the same "count less than x" in O(log n) without coordinate compression.

## Why the merge-sort count is right
In the merge step, right-half elements taken before a left-half element `x` are strictly smaller than `x` (use `≤` when choosing from the left so equal values aren't counted) and they all came from positions after `x` in the original array.

## Pitfalls
- Duplicates: count strictly smaller → Fenwick query `rank − 1`; merge takes left on ties.
- Negative values → coordinate compression (or offset).
- Sorting values, not indices, in merge sort loses track of where counts go.

## Follow-ups
- **Count of range sum** (LC 327), **reverse pairs** (LC 493) — merge sort counting with different conditions.
- **Global inversions** — sum of this array.
- **Create sorted array through instructions** (LC 1649) — Fenwick counting both sides.

## 30-second interview script
"Scanning from the right, I need for each element the number of smaller values already seen. I compress the values to ranks and keep a Fenwick tree of counts: query the prefix below my rank, then add myself — O(n log n). Alternatively a merge sort over indices adds, for each left element, how many right-half elements were placed before it."
