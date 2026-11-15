# LC 23 — Merge k Sorted Lists (Hard)

**Pattern:** k-way merge with a min-heap, or divide & conquer pairing · **Target:** O(N log k) · **Code:** [MergeKSortedLists.java](MergeKSortedLists.java)

## Problem
Merge `k` sorted linked lists (N nodes in total) into one sorted list.

`[[1,4,5],[1,3,4],[2,6]] → 1→1→2→3→4→4→5→6`

## Why engineers fear it
Not hard to get *working*; hard to get *optimal* and to explain why the naive merge is O(kN).

## Approach 1 — Collect and sort
Dump all values, sort, rebuild. **O(N log N)** time, O(N) space. Ignores the sortedness.

## Approach 2 — Merge one list at a time
`result = merge(result, list_i)` for each i. The growing result is re-scanned every time: cost ≈ n + 2n + … + kn = **O(k · N)** (n = average list length).

## Approach 3 — Compare all k heads each step
Pick the minimum of k heads by scanning: **O(N · k)**.

## Approach 4 — Min-heap of heads (optimal)
Put each non-null head in a min-heap. Repeatedly pop the smallest, append it, and push its `next`. The heap never holds more than k nodes → **O(N log k)** time, **O(k)** space.

## Approach 5 — Divide & conquer pairing (optimal, no heap)
Merge lists in pairs: (0,1), (2,3), … then pairs of results, like merge sort's merge phase. There are `log k` rounds and each round touches all N nodes → **O(N log k)** time, **O(1)** extra (iterative) or O(log k) recursion.

## Why O(N log k) and not O(N log N)
Each node is compared against only the other ≤ k candidates (heap size k), or participates in `log k` merge rounds.

## Pitfalls
- Null lists in the input (skip them when seeding the heap).
- Comparator: `Comparator.comparingInt(n -> n.val)` — don't subtract values (overflow).
- Reusing nodes (no allocation) is expected.

## Follow-ups
- **Kth smallest in k sorted lists / matrix** — stop after k pops.
- **Smallest range covering k lists** (LC 632) — the same heap plus a running max (see `09-Heaps`).
- External sorting of huge files — k-way merge of sorted runs is exactly this.

## 30-second interview script
"Merging one at a time is O(kN) because the result keeps getting rescanned. Instead I keep the current head of every list in a min-heap of size k: pop the smallest, append it, push its successor — O(N log k). Pairwise divide-and-conquer merging gives the same bound without a heap."
