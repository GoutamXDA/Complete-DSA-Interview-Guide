# LC 87 — Scramble String (Hard)

**Pattern:** interval DP over (start in s1, start in s2, length) with split choices · **Target:** O(n⁴) · **Code:** [ScrambleString.java](ScrambleString.java)

## Problem
A string is scrambled by recursively splitting it into two non-empty parts and **optionally swapping** them. Given `s1` and `s2` of equal length, can `s2` be a scramble of `s1`?

`"great" → "rgeat" : true` · `"abcde" → "caebd" : false` · `"a" → "a" : true`

## Why engineers fear it
Exponential branching (every split point × swap/no-swap) and a 3-dimensional DP state that's easy to index wrongly.

## Recurrence
`scramble(a, b)` for equal-length strings is true if `a == b`, or for some split `k` (1 ≤ k < len):
- **no swap:** `scramble(a[0..k), b[0..k))` and `scramble(a[k..), b[k..))`, or
- **swap:** `scramble(a[0..k), b[len−k..))` and `scramble(a[k..), b[0..len−k))`.

## Approach 1 — Plain recursion
Exponential (each level tries 2(len−1) splits).

## Approach 2 — Recursion with pruning + memo (optimal in practice)
- **Prune:** if `a` and `b` don't have the same character counts, return false immediately (cheap and cuts most branches).
- **Memoise** on `(i, j, len)` (start in s1, start in s2, length) → at most n³ states, each trying n splits.
**O(n⁴)** worst case, very fast in practice.

## Approach 3 — Bottom-up 3D DP
`dp[len][i][j]` = is `s2[j..j+len)` a scramble of `s1[i..i+len)`?
- `len = 1`: `s1[i] == s2[j]`.
- `len > 1`: OR over `k ∈ [1, len)` of
  `(dp[k][i][j] && dp[len−k][i+k][j+k]) || (dp[k][i][j+len−k] && dp[len−k][i+k][j])`.
Answer `dp[n][0][0]`. **O(n⁴)** time, **O(n³)** space; no recursion.

## Why counting characters is a valid prune
Splitting and swapping never changes the multiset of characters, so different counts ⇒ impossible.

## Pitfalls
- Swap case indices: the first `k` chars of `a` pair with the **last** `k` chars of `b`.
- Memo keys must include both starts and the length (`s1`/`s2` substrings alone as keys work but are slower).
- Length mismatch → false immediately.

## Follow-ups
- **Interleaving string** (LC 97) — 2D DP with a different merge rule.
- **Burst balloons / matrix chain** — the same "try every split of an interval" structure.
- Count the number of scramble trees producing `s2` → same DP with counts instead of booleans.

## 30-second interview script
"For two equal-length strings, s2 is a scramble if there's a split where both halves match without a swap or with the halves swapped. I memoise on (start in s1, start in s2, length), giving n³ states each trying n splits — O(n⁴) — and prune any pair whose character counts differ, which makes it fast in practice. The same recurrence works bottom-up by increasing length."
