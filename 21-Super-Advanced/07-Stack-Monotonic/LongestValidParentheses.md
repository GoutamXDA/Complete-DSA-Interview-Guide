# LC 32 — Longest Valid Parentheses (Hard)

**Pattern:** stack of indices with a "base" sentinel, DP, or two-direction counters · **Target:** O(n) time, O(1) space possible · **Code:** [LongestValidParentheses.java](LongestValidParentheses.java)

## Problem
Return the length of the longest **contiguous** well-formed parentheses substring.

`"(()" → 2` · `")()())" → 4` · `"()(())" → 6`

## Why engineers fear it
Validity checking is easy; finding the longest valid *substring* in one pass means remembering where the current valid run started — and runs can be **adjacent** (`"()()"`) or **nested** (`"(())"`).

## Approach 1 — Brute force
Check every even-length substring with a balance counter. **O(n³)** (O(n²) if you extend each start incrementally and stop when balance < 0).

## Approach 2 — DP
`dp[i]` = length of the longest valid substring **ending at** `i`.
- `s[i] == ')' and s[i−1] == '('` → `dp[i] = dp[i−2] + 2`
- `s[i] == ')' and s[i−1] == ')'` → let `j = i − dp[i−1] − 1` (the char before the inner valid block); if `s[j] == '('`: `dp[i] = dp[i−1] + 2 + dp[j−1]` (glue the run before `j`).
**O(n)** time, O(n) space.

## Approach 3 — Stack of indices with a base (classic)
Push `−1` as the initial "base" (index before the current valid run).
- `(` → push its index.
- `)` → pop. If the stack is now empty, this `)` is unmatched → push its index as the **new base**. Otherwise the current valid run is `i − stack.top`.
**O(n)** time and space.

## Approach 4 — Two counters, two passes (O(1) space)
Left→right: count `open`, `close`; when equal record `2·close`; when `close > open` reset both. This misses cases like `"(()"` where opens never balance, so do the same **right→left** with the roles swapped. The max of both passes is the answer. **O(n)** time, **O(1)** space.

### Why two passes are needed
A left-to-right pass can't close a run that has extra `(` at its start (balance never returns to 0); scanning right-to-left handles exactly those.

## Dry run (stack) — `")()())"`
| i | ch | action | stack | best |
|---|---|---|---|---|
| – | – | init | [−1] | 0 |
| 0 | ) | pop → empty → push 0 | [0] | 0 |
| 1 | ( | push | [0,1] | 0 |
| 2 | ) | pop → top 0 → 2−0 | [0] | 2 |
| 3 | ( | push | [0,3] | 2 |
| 4 | ) | pop → 4−0 | [0] | 4 |
| 5 | ) | pop → empty → push 5 | [5] | 4 |

## Pitfalls
- Forgetting the initial `−1` base → off-by-one lengths.
- DP: check `j ≥ 0` and use `dp[j−1]` only when `j ≥ 1`.
- Counters: reset on `close > open` (L→R) and on `open > close` (R→L).

## Follow-ups
- **Remove invalid parentheses** (LC 301) — all minimal fixes (see `05-Backtracking`).
- **Minimum remove to make valid** (LC 1249) — stack + mark.
- **Count valid substrings** — DP counting ending positions.

## 30-second interview script
"I keep a stack of indices with a sentinel −1 marking the position just before the current valid run. Opens are pushed; a close pops, and if the stack becomes empty that close is unmatched and becomes the new base, otherwise the valid length is i minus the top. For O(1) space I can instead count opens and closes left-to-right and right-to-left, recording lengths when they balance."
