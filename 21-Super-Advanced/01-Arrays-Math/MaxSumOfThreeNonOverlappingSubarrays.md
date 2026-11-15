# LC 689 — Maximum Sum of 3 Non-Overlapping Subarrays (Hard)

**Pattern:** window sums + best-left / best-right precomputation (or k-segment DP) · **Target:** O(n) · **Code:** [MaxSumOfThreeNonOverlappingSubarrays.java](MaxSumOfThreeNonOverlappingSubarrays.java)

## Problem
Choose three non-overlapping subarrays, each of length `k`, with the maximum total sum. Return their starting indices; if several answers tie, return the **lexicographically smallest** index triple.

`nums = [1,2,1,2,6,7,5,1], k = 2 → [0, 3, 5]` (sums 3 + 8 + 12 = 23)

## Why engineers fear it
Three interacting choices + a tie-breaking rule. Most people get the sum right but return the wrong indices.

## Step 0 — window sums
`W[i] = nums[i] + … + nums[i+k−1]` for every start `i` (sliding window, O(n)). Now the problem is: pick `i < j < l` with `j ≥ i + k`, `l ≥ j + k`, maximising `W[i] + W[j] + W[l]`.

## Approach 1 — Brute force over starts
Three nested loops over valid starts. **O(n³)**. Lexicographic order comes for free if you only replace on a strictly larger sum while looping in increasing order.

## Approach 2 — Fix the middle (optimal, O(n))
For a fixed middle start `j`, the best left window is the best `W[i]` with `i ≤ j − k`, and the best right window is the best `W[l]` with `l ≥ j + k`. Precompute:
- `left[i]` = index of max `W` in `[0, i]` — take the **earliest** on ties (`>` comparison going left→right).
- `right[i]` = index of max `W` in `[i, end]` — take the **earliest** on ties (`>=` comparison going right→left).
Then loop `j` from `k` to `len − 2k` and keep the best `W[left[j−k]] + W[j] + W[right[j+k]]`, replacing only on strictly greater.

**Time** O(n) · **Space** O(n).

## Approach 3 — General DP for m subarrays
`dp[t][i]` = best sum using `t` windows within the first `i` positions:
`dp[t][i] = max(dp[t][i−1], dp[t−1][i−k] + W[i−k])`. Reconstruct indices backwards. **O(m·n)** — the answer to the follow-up "what about m subarrays?"

## Why the tie rules work
For a fixed middle `j`, the best total is `W[j] + max-left + max-right`, and choosing the **earliest** argmax on each side gives the smallest `(i, l)` for that `j`. The middle loop runs left to right and accepts only strictly larger totals, so it keeps the **smallest `j`** that reaches the optimum. Could a different optimal triple be lexicographically smaller? It would need a smaller `i`. But a smaller `i` with a larger `j` is still covered: `left[j−k]` scans every start up to `j−k`, so for the first optimal `j` the earliest best `i` is already chosen. The Java file confirms this against brute force on 2,000 random arrays.

## Pitfalls
- `right` must use `>=` while scanning right-to-left (to move the index left on ties).
- Off-by-one on the middle range: `j ∈ [k, n − 2k]`.
- Using `int` sums is fine for LC constraints; use `long` for larger inputs.

## Follow-ups
- m subarrays → Approach 3.
- Two non-overlapping subarrays with given lengths L and M (LC 1031) — same "best left" idea.
- Best time to buy/sell stock III/IV — the same "k segments" DP shape.

## 30-second interview script
"I precompute every window sum, then for each possible middle window I combine it with the best window entirely to its left and the best entirely to its right, both precomputed in linear passes with careful tie-breaking for lexicographic order. That's O(n). For m windows I'd generalise to a dp over (windows used, prefix length)."
