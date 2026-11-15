# LC 295 — Find Median from Data Stream (Hard)

**Pattern:** two heaps (max-heap for the lower half, min-heap for the upper half) · **Target:** add O(log n), median O(1) · **Code:** [FindMedianFromDataStream.java](FindMedianFromDataStream.java)

## Problem
Support `addNum(x)` and `findMedian()` over a growing stream of integers.

`add 1, add 2 → 1.5; add 3 → 2.0`

## Why engineers fear it
Knowing "two heaps" is half of it; keeping the **ordering invariant** and the **size invariant** correct on every insert is where bugs live. Follow-ups (bounded values, 99% in a range) test depth.

## Approach 1 — Sort on every query
Append, sort when asked. add O(1), median **O(n log n)**.

## Approach 2 — Keep a sorted list (insertion sort)
Binary-search the position and insert. add **O(n)** (shifting), median O(1).

## Approach 3 — Two heaps (optimal)
- `low`: max-heap with the smaller half; `high`: min-heap with the larger half.
- Invariants: every element of `low` ≤ every element of `high`; `|low| = |high|` or `|low| = |high| + 1`.
```
addNum(x):
    low.offer(x)
    high.offer(low.poll())            # move the largest of the low half up → ordering OK
    if high.size() > low.size():
        low.offer(high.poll())        # rebalance sizes
findMedian():
    odd → low.peek(); even → (low.peek() + high.peek()) / 2.0
```
The "always pass through the other heap" trick makes the ordering invariant hold without case analysis. add **O(log n)** (≤ 3 heap ops), median **O(1)**.

## Approach 4 — Order-statistic tree / TreeMap with counts
A balanced BST with subtree sizes (or two TreeMaps acting as multisets) supports insert **and delete** in O(log n) — necessary for the sliding-window version (LC 480).

## Follow-ups (classic)
- **All values in [0, 100]:** keep `count[101]` and a total; median by scanning 101 buckets → O(1) add, O(100) median.
- **99% of values in [0, 100]:** buckets for the range plus two small heaps/lists for outliers below and above.
- **Sliding window median** (LC 480) → deletions needed (see `03-Sliding-Window-Two-Pointers`).
- **Distributed stream** → approximate quantiles (t-digest, GK sketch).

## Pitfalls
- Integer overflow in `(a + b) / 2` → divide as doubles.
- `Collections.reverseOrder()` for the max-heap.
- Checking sizes before the ordering fix leaves elements on the wrong side.

## 30-second interview script
"A max-heap holds the lower half and a min-heap the upper half, with the lower one allowed one extra element. Every new number goes into the lower heap, its largest element moves to the upper heap, and if the upper heap becomes bigger I move its smallest back. The median is the lower top or the average of both tops: O(log n) insert, O(1) query."
