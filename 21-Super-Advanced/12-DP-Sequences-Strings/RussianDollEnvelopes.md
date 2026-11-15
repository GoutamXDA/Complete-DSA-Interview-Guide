# LC 354 — Russian Doll Envelopes (Hard)

**Pattern:** 2D LIS → sort cleverly, then 1D LIS in O(n log n) · **Target:** O(n log n) · **Code:** [RussianDollEnvelopes.java](RussianDollEnvelopes.java)

## Problem
Envelope `(w, h)` fits into another if **both** width and height are strictly smaller. Return the maximum number of envelopes you can nest.

`[[5,4],[6,4],[6,7],[2,3]] → 3` ([2,3] → [5,4] → [6,7])

## Why engineers fear it
The O(n²) DP is easy; LC's limits (10⁵ envelopes) require O(n log n), and the sorting trick for equal widths is the part everyone forgets.

## Approach 1 — Sort + O(n²) LIS DP
Sort by width; `dp[i] = 1 + max dp[j]` over `j < i` with `w[j] < w[i]` and `h[j] < h[i]`. **O(n²)**.

## Approach 2 — Sort by (w asc, h desc) + patience sorting LIS (optimal)
1. Sort by width **ascending**, and for equal widths by height **descending**.
2. Run the O(n log n) strictly increasing LIS on the heights.

**Why descending heights for equal widths:** envelopes with the same width can't nest. Sorting their heights in decreasing order guarantees that a strictly increasing subsequence of heights picks **at most one** of them. Without it, `[3,3],[3,4]` would be counted as nested.

**Patience sorting:** `tails[k]` = smallest possible tail of an increasing subsequence of length `k+1`. For each `h`, binary-search the first `tails[k] ≥ h` (lower bound → strictly increasing) and replace it (or append). Length of `tails` = answer. **O(n log n)**.

## Approach 3 — Fenwick tree over compressed heights
After the same sort, `best(h) = 1 + max over heights < h` → a Fenwick tree of prefix maxima over compressed heights. **O(n log n)**; generalises when the "LIS" has weights.

## Dry run — `[[5,4],[6,4],[6,7],[2,3]]`
Sorted: `[2,3], [5,4], [6,7], [6,4]` → heights `3, 4, 7, 4`
tails: [3] → [3,4] → [3,4,7] → (4 replaces 4) [3,4,7] → **3**.

## Pitfalls
- Sorting heights ascending for equal widths (overcounts).
- Using upper bound (non-strict LIS) instead of lower bound.
- `tails` is **not** a valid nesting sequence — only its length is meaningful.

## Follow-ups
- **Longest increasing subsequence** (LC 300) — the 1D core.
- **Maximum height by stacking cuboids** (LC 1691) — 3D, sort dimensions + O(n²) DP.
- **Box stacking** — weighted variant → DP or Fenwick with max.
- Return the actual chain → store predecessor indices alongside `tails`.

## 30-second interview script
"Sort by width ascending and, for equal widths, height descending; then the answer is the longest strictly increasing subsequence of heights. The descending tie-break stops two envelopes of the same width from both being chosen. LIS with patience sorting and binary search makes it O(n log n)."
