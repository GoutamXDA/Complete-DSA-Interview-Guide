# LC 664 — Strange Printer (Hard)

**Pattern:** interval DP where matching endpoints **merge** turns · **Target:** O(n³) · **Code:** [StrangePrinter.java](StrangePrinter.java)

## Problem
A printer prints a run of **one** character over any contiguous range per turn, overwriting what's there. Minimum turns to print string `s`?

`"aaabbb" → 2` · `"aba" → 2` (print "aaa", then "b" in the middle)

## Why engineers fear it
The optimal strategy prints long background runs first and overwrites later, so greedy left-to-right fails. The DP insight is about which characters can **share** a turn.

## Preprocessing
Collapse consecutive duplicates (`"aaabbb"` → `"ab"`); it never changes the answer and shrinks n.

## Key insight
Consider `dp[i][j]` = min turns for `s[i..j]`. The character `s[i]` is printed in some turn. If that turn also covers a later position `k` with `s[k] == s[i]`, then printing `s[i..k]` can **start** with that same turn — so the cost of `s[i]` is absorbed into the segment ending at `k`.

`dp[i][j] = min( dp[i+1][j] + 1,                                   # s[i] printed alone`
`                min over k in (i, j] with s[k]==s[i] of dp[i+1][k−1] + dp[k][j] )`  — `s[i]`'s turn is merged into `s[k]`'s.

## Approach 1 — Brute-force BFS over printer states (oracle)
Each state is the current paper (string over the alphabet + blank); each move paints a range with a character. BFS until the paper equals `s`. Only for tiny strings.

## Approach 2 — Memoised interval DP
Top-down with the recurrence. **O(n³)** time, O(n²) space.

## Approach 3 — Bottom-up by increasing length
Same recurrence filled with `i` from right to left, `j` from `i` to the right. **O(n³)**.

## Alternative formulation
`dp[i][j] = dp[i][j−1]` if `s[j] == s[i]`, else `min over k of dp[i][k] + dp[k+1][j]` — also O(n³) and common in editorials. The tests check both.

## Why merging same characters is safe
In an optimal schedule we can always assume the first turn on a segment prints `s[i]` across the whole range up to its last "partner" `k`; everything in between is printed on top later. This exchange argument is why the split only happens at equal characters.

## Pitfalls
- Forgetting the `+1` when `s[i]` stands alone.
- `dp[i+1][k−1]` with `i+1 > k−1` → 0 (empty).
- Not collapsing runs → still correct, just slower.

## Follow-ups
- **Strange printer II** (LC 1591) — 2D, cycle detection on colour dependencies.
- **Remove boxes** (LC 546), **zuma game** (LC 488) — interval DP with merging of equal endpoints.
- **Minimum insertions to make a palindrome** — interval DP on endpoints.

## 30-second interview script
"dp[i][j] is the minimum turns for s[i..j]. Either s[i] gets its own turn — 1 + dp[i+1][j] — or its turn is extended to a later position k with the same character, in which case the middle part s[i+1..k−1] is printed on top and the cost is dp[i+1][k−1] + dp[k][j]. Taking the min over such k gives an O(n³) interval DP; collapsing repeated characters first shrinks n."
