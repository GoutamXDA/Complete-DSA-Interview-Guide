# LC 632 — Smallest Range Covering Elements from K Lists (Hard)

**Pattern:** k-way merge heap + running maximum (or merge + sliding window) · **Target:** O(N log k) · **Code:** [SmallestRangeCoveringKLists.java](SmallestRangeCoveringKLists.java)

## Problem
`k` sorted lists. Find the smallest range `[a, b]` that contains at least one number from **each** list. Smaller means smaller `b − a`; ties → smaller `a`.

`[[4,10,15,24,26],[0,9,12,20],[5,18,22,30]] → [20,24]`

## Why engineers fear it
Two moving ends, k lists, and a heap that only gives you the minimum — you have to see that the maximum can be tracked separately.

## Approach 1 — Brute force over candidate ranges
Every pair of values `(a, b)` from the union, check if each list has an element in `[a, b]` (binary search). **O(N² · k log n)**.

## Approach 2 — Merge + sliding window (very clear)
Merge all `(value, listId)` pairs and sort: **O(N log N)**. Now it's "minimum window containing all k colours" — a classic variable sliding window with a per-list count. **O(N log N)** total.

## Approach 3 — Heap of current heads + running max (optimal)
Keep one pointer per list. The current range is `[min of heads, max of heads]`.
```
push (lists[i][0], i, 0) for all i; curMax = max of those
loop:
    (val, i, j) = pop min
    update best with [val, curMax]
    if j + 1 == len(lists[i]): stop          # can't cover list i anymore
    push (lists[i][j+1], i, j+1); curMax = max(curMax, lists[i][j+1])
```
**Why advancing the minimum is the only useful move:** the range is bounded below by the minimum head; advancing any other pointer can only increase the max without raising the min, which never shrinks the range.
**O(N log k)** time, **O(k)** space.

## Dry run (first steps)
Heads 4, 0, 5 → range [0, 5] (size 5). Pop 0 → push 9 → heads 4, 9, 5 → [4, 9]. Pop 4 → 10 → [5, 10]. … the best found is **[20, 24]**.

## Pitfalls
- Tie-breaking: update only when the new size is **strictly** smaller (then the earlier, smaller `a` wins).
- Stop as soon as **any** list is exhausted.
- Track `curMax` yourself — the min-heap doesn't know it.

## Follow-ups
- **Merge k sorted lists** (LC 23) — the heap part alone.
- **Minimum window substring** (LC 76) — the sliding-window version on characters.
- **Kth smallest pair sums** (LC 373) — heap frontier.

## 30-second interview script
"I keep one pointer per list in a min-heap and remember the maximum of the current heads, so the current range is [heap min, max]. The only move that can shrink it is advancing the list with the minimum, so I pop it, record the range, push that list's next element and update the max. When a list runs out I stop. O(N log k)."
