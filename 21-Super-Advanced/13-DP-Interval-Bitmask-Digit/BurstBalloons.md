# LC 312 — Burst Balloons (Hard)

**Pattern:** interval DP choosing the **last** element to process in a range · **Target:** O(n³) · **Code:** [BurstBalloons.java](BurstBalloons.java)

## Problem
Bursting balloon `i` earns `nums[left] × nums[i] × nums[right]`, where `left`/`right` are its **current** neighbours (out-of-range neighbours count as 1). Burst all balloons; maximise the coins.

`[3,1,5,8] → 167` (burst 1, then 5, then 3, then 8: 3·1·5 + 3·5·8 + 1·3·8 + 1·8·1 = 15 + 120 + 24 + 8)

## Why engineers fear it
Thinking "which balloon do I burst **first**" makes subproblems depend on each other (neighbours change). The trick — think about which balloon is burst **last** in a range — is the canonical "aha" of interval DP.

## Key insight
Pad with 1s: `a = [1, nums..., 1]`. For an open interval `(i, j)`, let `k` be the **last** balloon burst inside it. When `k` bursts, everything else in `(i, j)` is already gone, so its neighbours are exactly `a[i]` and `a[j]` — fixed boundaries. The left part `(i, k)` and right part `(k, j)` are independent subproblems.

`dp[i][j] = max over i < k < j of dp[i][k] + a[i]·a[k]·a[j] + dp[k][j]`

## Approach 1 — Brute force over burst orders
All n! orders, simulating with a list. **O(n! · n)** — oracle only.

## Approach 2 — Memoised recursion on (i, j)
Top-down with the recurrence above. **O(n³)** time, O(n²) space.

## Approach 3 — Bottom-up by interval length
```
for len = 2 .. n+1:                 # j - i
    for i in 0 .. n+1-len:
        j = i + len
        for k in i+1 .. j-1:
            dp[i][j] = max(dp[i][j], dp[i][k] + a[i]*a[k]*a[j] + dp[k][j])
answer dp[0][n+1]
```
**O(n³)** time, **O(n²)** space.

## Why "first" fails and "last" works
If `k` is burst first, the left and right parts become neighbours of each other, so they're not independent. If `k` is burst last, it acts as a wall that separates them for the entire process.

## Pitfalls
- Using closed intervals and forgetting the padding → messy boundary products.
- Iterating `i` forward with dependencies on larger `i` — use length order (or `i` descending).

## Follow-ups (same "last split" pattern)
- **Minimum cost to cut a stick** (LC 1547) — choose the first cut instead (cuts don't change neighbours).
- **Matrix chain multiplication** — choose the last multiplication.
- **Remove boxes** (LC 546) — interval DP with an extra dimension for attached same-colour boxes.
- **Strange printer** (LC 664) — next file.

## 30-second interview script
"Pad the array with 1s. For an open interval (i, j), pick the balloon k that's burst last inside it: at that moment its neighbours are exactly a[i] and a[j], and the two sides are independent subproblems. So dp[i][j] = max over k of dp[i][k] + a[i]·a[k]·a[j] + dp[k][j], filled by increasing length — O(n³)."
