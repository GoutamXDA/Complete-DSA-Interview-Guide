# LC 862 — Shortest Subarray with Sum at Least K (Hard)

**Pattern:** prefix sums + monotonic increasing deque · **Target:** O(n) · **Code:** [ShortestSubarrayWithSumAtLeastK.java](ShortestSubarrayWithSumAtLeastK.java)

## Problem
Return the length of the shortest non-empty subarray with sum **≥ K**; −1 if none. **Values can be negative.**

`[2,-1,2], K=3 → 3` · `[1,2], K=4 → −1` · `[84,-37,32,40,95], K=167 → 3`

## Why engineers fear it
With only positive numbers this is the easy LC 209 sliding window. **Negatives break the window** (shrinking can increase the sum), and a binary search on prefix sums doesn't work either because prefix sums aren't monotonic.

## Reformulation
With prefix sums `P[0] = 0`, `P[i+1] = P[i] + a[i]`, find `i < j` with `P[j] − P[i] ≥ K` minimising `j − i`.

## Approach 1 — Brute force
All pairs `(i, j)`. **O(n²)**.

## Approach 2 — Sorted structure of earlier prefixes
For each `j`, we want the **largest** `i` with `P[i] ≤ P[j] − K`. A TreeMap from prefix value → latest index, queried with `floorKey`, doesn't directly give "largest index among values ≤ x". You can make it work by keeping only useful candidates (see below), which leads straight to the deque. O(n log n) variants exist; the deque is simpler.

## Approach 3 — Monotonic deque of candidate starts (optimal)
Keep a deque of indices `i` with **strictly increasing** `P[i]`.
```
for j in 0..n:
    while dq not empty and P[j] - P[dq.front] >= K:
        best = min(best, j - dq.front); pop front      # (1) front is used up
    while dq not empty and P[dq.back] >= P[j]:
        pop back                                         # (2) dominated start
    push j
```
**Rule (1):** once front `i` satisfies `P[j] − P[i] ≥ K`, any later `j' > j` would give a *longer* subarray from `i`, so `i` will never do better — pop it.
**Rule (2):** if `P[back] ≥ P[j]` with `back < j`, then `j` is a better start than `back` for every future end (smaller prefix and closer) — `back` is dominated.
Each index enters/leaves once → **O(n)** time, **O(n)** space.

## Dry run — `[84,-37,32,40,95]`, K = 167
P = [0, 84, 47, 79, 119, 214]
- j=0..4: no front satisfies; back pops: at j=2 (47), pop 84. Deque values [0, 47, 79, 119].
- j=5 (214): 214−0 ≥167 → len 5, pop; 214−47=167 → len 3, pop; 214−79=135 stop. Answer **3**.

## Pitfalls
- Use `long` prefix sums (values up to 10⁵ × 10⁵).
- Order of the two loops: check the front (answers) before pruning the back.
- The two-pointer window from LC 209 silently fails on negatives — mention why.

## Follow-ups
- **Minimum size subarray sum** (LC 209) — positives only, plain window.
- **Longest subarray with sum ≥ K** — monotonic stack of prefix minima + scan from the right.
- **Constrained subsequence sum** (LC 1425) — DP + deque max.

## 30-second interview script
"Negatives kill the normal sliding window, so I switch to prefix sums: find i<j with P[j]−P[i] ≥ K and minimal j−i. I keep a deque of start indices with increasing prefix values. For each j I pop fronts that already satisfy the condition — later ends can't beat them — and I pop backs whose prefix is ≥ P[j], since j dominates them. Linear time."
