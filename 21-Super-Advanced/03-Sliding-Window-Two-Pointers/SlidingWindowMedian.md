# LC 480 — Sliding Window Median (Hard)

**Pattern:** two heaps with lazy deletion / two ordered multisets · **Target:** O(n log k) · **Code:** [SlidingWindowMedian.java](SlidingWindowMedian.java)

## Problem
For each window of size `k`, return its median (average of the two middle values when `k` is even).

`[1,3,-1,-3,5,3,6,7], k=3 → [1,-1,-1,3,5,6]`

## Why engineers fear it
The streaming median (LC 295) uses two heaps, but here elements also **leave**. Java's `PriorityQueue.remove(Object)` is O(k), and balancing heaps while lazily-deleted junk sits inside them is easy to get wrong.

## Approach 1 — Sort every window
**O(n · k log k)**. The oracle.

## Approach 2 — Keep the window sorted (insertion with binary search)
Maintain a sorted `ArrayList`: binary-search the position to insert `a[i]` and to delete `a[i−k]`. Lookups O(log k) but shifting is O(k) → **O(n · k)**. Surprisingly fast in practice for moderate k.

## Approach 3 — Two TreeSets of indices (clean O(n log k))
`low` (max side) and `high` (min side) are `TreeSet<Integer>` of **indices** ordered by `(value, index)` so duplicates are distinct. Invariant: `|low| = |high|` or `|low| = |high| + 1`, and every value in `low` ≤ every value in `high`.
- Insert: put into `low`, move `low.last()` to `high`, and if `high` got bigger move `high.first()` back.
- Remove `i−k`: delete from whichever set contains it, then rebalance.
- Median: `low.last()` (odd) or average with `high.first()`.
**O(n log k)**, real deletions, no lazy bookkeeping. The easiest to explain correctly.

## Approach 4 — Two heaps with lazy deletion (classic)
Same invariant as the streaming median, but removals are recorded in a `delayed` map and only physically removed when they reach a heap top (`prune`). Track **valid** sizes separately from heap sizes. **O(n log n)** worst, O(n log k) typical.

## Invariant to state in the interview
"Left half holds the smaller ⌈k/2⌉ values, right half the larger ⌊k/2⌋, and max(left) ≤ min(right)." Every operation restores it.

## Pitfalls
- Overflow: `(a + b) / 2.0` with ints near `Integer.MAX_VALUE` → cast to `double`/`long` first.
- Duplicates in TreeSet → order by index as a tiebreaker.
- In the lazy version, decide which heap an outgoing value belongs to by comparing with `low.peek()` **after** pruning.

## Follow-ups
- **Find median from data stream** (LC 295) — insert only.
- **Sliding window k-th smallest** — same TreeSet approach; or an order-statistic tree / Fenwick over compressed values (O(n log n)).
- **Minimise sum of |x − median|** over windows — needs sums of each half too.

## 30-second interview script
"I keep the window split into a lower and upper half, sizes balanced so the lower one has the extra element. With TreeSets of indices ordered by value-then-index I can insert, delete the outgoing index and rebalance in O(log k), and the median is the top of the lower half or the average of both tops."
