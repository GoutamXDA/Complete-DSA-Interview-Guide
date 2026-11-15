# LC 992 — Subarrays with K Different Integers (Hard)

**Pattern:** "exactly K" = atMost(K) − atMost(K−1), or a three-pointer window · **Target:** O(n) · **Code:** [SubarraysWithKDistinct.java](SubarraysWithKDistinct.java)

## Problem
Count contiguous subarrays containing **exactly** `K` distinct integers.

`[1,2,1,2,3], K=2 → 7` · `[1,2,1,3,4], K=3 → 3`

## Why engineers fear it
"Exactly K" isn't monotonic: extending a valid window can make it invalid, and shrinking can make it valid again. A single sliding window can't count it directly.

## Approach 1 — Brute force
For each start, extend while tracking a set; count ends where size == K; break when size > K. **O(n²)**.

## Approach 2 — atMost(K) − atMost(K−1) (optimal, standard)
`atMost(K)` **is** monotonic: if a window has ≤ K distinct, every sub-window does too. So a standard window works:
```
atMost(K): l = 0, count = 0
  for r: add a[r]; while distinct > K: remove a[l++]
         count += r - l + 1          # every start in [l, r] gives a valid subarray ending at r
```
Then `exactly(K) = atMost(K) − atMost(K−1)`. **O(n)** time (two passes), **O(n)** space for counts (an `int[n+1]` array works since values ≤ n).

## Approach 3 — One pass with two left pointers
For each `r`, maintain:
- `l1` = smallest start such that `[l1, r]` has ≤ K distinct,
- `l2` = smallest start such that `[l2, r]` has ≤ K−1 distinct.
Starts in `[l1, l2)` have **exactly** K distinct → add `l2 − l1`. Two independent count maps, one pass. Same complexity, shows deeper understanding.

## Why "count += r − l + 1" is right
For a fixed `r`, the valid starts for "at most K" form a contiguous range `[l, r]` (monotonicity), so there are `r − l + 1` subarrays ending at `r`.

## Dry run — `[1,2,1,2,3]`, K = 2
atMost(2): r=0→1, r=1→2, r=2→3, r=3→4, r=4 (window shrinks to [2,3]... start 3) → 2 → total 12.
atMost(1): 1+1+1+1+1 = 5. Answer **12 − 5 = 7**.

## Pitfalls
- `atMost(0)` must return 0 (handle K−1 = 0).
- Decrement distinct only when a count drops to 0.
- Use `long` counts for big n (n(n+1)/2 can exceed int).

## Follow-ups (same trick)
- **Binary subarrays with sum** (LC 930), **count nice subarrays** (LC 1248).
- **Count substrings with exactly K distinct characters**.
- **Longest substring with at most K distinct** (LC 340) — the "atMost" window alone.

## 30-second interview script
"Exactly-K isn't monotonic but at-most-K is: shrinking a window never adds distinct values. So I count subarrays with at most K distinct using a window — each r contributes r−l+1 — and subtract the count for at most K−1. Two linear passes; or one pass with two left pointers."
