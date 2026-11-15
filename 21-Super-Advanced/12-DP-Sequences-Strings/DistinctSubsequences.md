# LC 115 — Distinct Subsequences (Hard)

**Pattern:** counting DP over two strings, 1D with a backwards loop · **Target:** O(mn) time, O(n) space · **Code:** [DistinctSubsequences.java](DistinctSubsequences.java)

## Problem
Count the number of distinct ways (index sets) to choose a subsequence of `s` equal to `t`.

`s = "rabbbit", t = "rabbit" → 3` · `s = "babgbag", t = "bag" → 5`

## Why engineers fear it
It's counting, not optimisation — every branch contributes, the numbers grow quickly, and the 1D space optimisation only works if you iterate **backwards**.

## State
`dp[i][j]` = number of ways the first `i` chars of `s` can form the first `j` chars of `t`.
- `dp[i][0] = 1` (the empty target is formed in exactly one way: choose nothing).
- `dp[0][j>0] = 0`.
- `dp[i][j] = dp[i−1][j]` (don't use `s[i−1]`) **+** `(s[i−1] == t[j−1] ? dp[i−1][j−1] : 0)` (use it to match `t[j−1]`).

## Approach 1 — Recursion
`f(i, j)` over suffixes with the same two choices. Exponential.

## Approach 2 — Memoisation
**O(mn)** time and space.

## Approach 3 — 2D tabulation
Straightforward table fill. **O(mn)** time and space.

## Approach 4 — 1D with backwards `j` (optimal space)
`dp[j]` holds row `i−1` before being overwritten. Iterating `j` from `n` down to `1` ensures `dp[j−1]` is still the **previous** row when `dp[j] += dp[j−1]`. **O(mn)** time, **O(n)** space.

## Approach 5 — Only update positions where `t` has `s[i]`
Precompute for each character the list of indices `j` in `t` (descending). For each `s[i]`, update only those `j`. Same worst case; much faster when the alphabet is large.

## Dry run — s = "babgbag", t = "bag" (1D, `dp = [1,0,0,0]`)
| s[i] | dp after |
|---|---|
| b | [1,1,0,0] |
| a | [1,1,1,0] |
| b | [1,2,1,0] |
| g | [1,2,1,1] |
| b | [1,3,1,1] |
| a | [1,3,4,1] |
| g | [1,3,4,**5**] |

## Pitfalls
- Forward iteration in 1D double-counts (uses the same `s[i]` for two positions of `t`).
- Overflow: LC guarantees the answer fits in 32 bits, but intermediate values can exceed it — use `long` (or `double`/mod) for intermediates.
- `t` longer than `s` → 0.

## Follow-ups
- **Number of matching subsequences** (LC 792) — many small `t`s, bucket by next char.
- **Distinct subsequences II** (LC 940) — count distinct subsequence *strings* of one string.
- **Count palindromic subsequences** (LC 730) — interval DP counting.

## 30-second interview script
"dp[j] counts the ways to form the first j chars of t using the prefix of s seen so far; dp[0] = 1. For each new char of s, every position j where t[j−1] equals it gains dp[j−1] ways — using this char as that position — and I iterate j backwards so I read the previous row's value. O(mn) time, O(n) space."
