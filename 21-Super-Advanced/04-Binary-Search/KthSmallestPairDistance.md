# LC 719 — Find K-th Smallest Pair Distance (Hard)

**Pattern:** binary search on the value + two-pointer counting · **Target:** O(n log n + n log W) · **Code:** [KthSmallestPairDistance.java](KthSmallestPairDistance.java)

## Problem
The distance of a pair is `|a[i] − a[j]|`. Return the k-th smallest distance among all `n(n−1)/2` pairs.

`[1,3,1], k=1 → 0` · `[1,6,1], k=3 → 5`

## Why engineers fear it
There are O(n²) pairs — you can't list them for n = 10⁴. You must answer "how many pairs have distance ≤ d?" without enumerating them.

## Approach 1 — Enumerate and sort
All distances, sort, pick index k−1. **O(n² log n)** time, O(n²) space.

## Approach 2 — Bucket count over distances
Distances are bounded by `W = max − min`. Count each distance into `cnt[W+1]`, then prefix-scan to k. **O(n² + W)** time. Better constant, still quadratic.

## Approach 3 — Heap of (i, i+1) frontier
Sort; push pairs `(i, i+1)` for all i; pop k times, pushing `(i, j+1)` each time — like "k smallest pairs". **O((n + k) log n)**. Fails for large k (k up to n²/2).

## Approach 4 — Binary search on the distance (optimal)
1. Sort `a`.
2. `count(d)` = number of pairs with distance ≤ d, computed with a sliding window: for each right index `j`, advance `i` while `a[j] − a[i] > d`; add `j − i`. **O(n)**.
3. `count` is monotonic in `d` → binary search the smallest `d` with `count(d) ≥ k` over `[0, W]`.
**Time** O(n log n + n log W) · **Space** O(1) extra.

### Why the answer is an actual pair distance
The smallest `d` with `count(d) ≥ k` must be achieved by some pair: if no pair had distance exactly `d`, then `count(d − 1) = count(d) ≥ k`, contradicting minimality.

## Dry run — `[1,6,1]`, k=3
Sorted [1,1,6], W=5. count(2)=1 (1,1); count(5)=3 → binary search gives d=5.

## Pitfalls
- Forgetting to sort before the two-pointer count.
- `count` can reach ~5·10⁷ — fits in `int` for LC limits, but use `long` in general.
- Binary search boundaries: `lo = 0`, `hi = W`, standard "first true" template.

## Follow-ups (same "BS on value + count" idea)
- **Kth smallest in a multiplication table** (LC 668) — next file.
- **Kth smallest element in a sorted matrix** (LC 378) — staircase count.
- **K-th smallest prime fraction** (LC 786) — BS on a real value + two pointers.

## 30-second interview script
"I can't list n² pairs, but after sorting I can count pairs with distance ≤ d in O(n) using two pointers. That count is monotonic in d, so I binary search the smallest d whose count reaches k. Total O(n log n + n log W)."
