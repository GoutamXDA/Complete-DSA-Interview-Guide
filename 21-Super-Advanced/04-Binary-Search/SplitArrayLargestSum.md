# LC 410 — Split Array Largest Sum (Hard)

**Pattern:** binary search on the answer + greedy feasibility (also: partition DP) · **Target:** O(n log Σ) · **Code:** [SplitArrayLargestSum.java](SplitArrayLargestSum.java)

## Problem
Split `nums` (non-negative) into `k` non-empty contiguous parts to **minimise the largest part sum**.

`[7,2,5,10,8], k=2 → 18` ([7,2,5] | [10,8])

Same problem in disguise: **Book Allocation**, **Painter's Partition**, **Capacity to Ship Packages within D Days** (LC 1011).

## Why engineers fear it
"Minimise the maximum" with `k` cuts looks like it needs DP over cut positions. Recognising the monotonic feasibility function is the leap.

## Approach 1 — Recursion over the first cut
`f(i, k)` = best answer for `nums[i..]` with `k` parts = `min over j of max(sum(i..j), f(j+1, k−1))`. Exponential without memo.

## Approach 2 — Partition DP
`dp[k][i]` = answer for the first `i` elements in `k` parts:
`dp[k][i] = min_{j<i} max(dp[k−1][j], P[i] − P[j])`. **O(k · n²)** time, O(k·n) space. Correct for any sign of numbers.

## Approach 3 — Binary search on the answer (optimal)
**Feasibility:** "can we split into ≤ k parts with every part ≤ `cap`?" Greedily extend the current part until adding the next number would exceed `cap`, then start a new part. Count parts; feasible if `parts ≤ k`.
- Monotonic: if `cap` works, every larger `cap` works.
- Range: `lo = max(nums)` (a part must hold the largest element), `hi = sum(nums)`.
```
while lo < hi:
    mid = (lo + hi) / 2
    if parts(mid) <= k: hi = mid else lo = mid + 1
```
**O(n log Σ)** time, O(1) space.

### Why greedy feasibility is exact
Packing each part as full as possible never uses more parts than any other valid split (exchange argument: moving a cut right while the part still fits never increases the number of parts).
"≤ k parts" is enough because a split into fewer parts can always be refined into exactly k non-empty parts without increasing the maximum (n ≥ k).

## Dry run — `[7,2,5,10,8]`, k=2
lo=10, hi=32 → mid=21: parts [7,2,5][10,8] = 2 ✓ → hi=21 → mid=15: [7,2,5][10][8] = 3 ✗ → lo=16 → mid=18: 2 ✓ → hi=18 → mid=17: [7,2,5][10][8] = 3 ✗ → lo=18. Answer **18**.

## Pitfalls
- `lo` must start at `max(nums)` (or feasibility must reject an element larger than cap).
- Sums can overflow `int` → use `long`.
- Binary search on the answer requires **non-negative** numbers; with negatives use the DP.

## Follow-ups
- **Minimise the maximum** / **maximise the minimum** family: Koko, ship packages, aggressive cows, magnetic force (topic 06).
- **Divide chocolate** (LC 1231) — maximise the minimum piece.
- **Minimise the sum of k parts' costs** → DP with Knuth/divide-and-conquer optimisation.

## 30-second interview script
"If a maximum part sum X is achievable, any larger X is too, so I binary search X between max(nums) and sum(nums). For each candidate I greedily pack elements into parts without exceeding X and check if I need at most k parts. That's O(n log sum). The alternative is an O(k n²) partition DP."
