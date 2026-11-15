# LC 301 — Remove Invalid Parentheses (Hard)

**Pattern:** BFS by number of removals, or DFS with pre-counted removals + duplicate pruning · **Code:** [RemoveInvalidParentheses.java](RemoveInvalidParentheses.java)

## Problem
Remove the **minimum** number of parentheses to make the string valid; return **all** distinct results. Letters stay.

`"()())()" → ["(())()", "()()()"]` · `"(a)())()" → ["(a())()", "(a)()()"]` · `")(" → [""]`

## Why engineers fear it
Two requirements at once — minimum removals **and** all distinct answers — and naive approaches either produce duplicates or explore 2ⁿ strings.

## Approach 1 — Brute force over subsets
Try every subset of characters to delete (2ⁿ), keep valid strings with the max length. **O(2ⁿ · n)**. Oracle only.

## Approach 2 — BFS by levels (clean minimality)
Level 0 = the string. Level d = all strings obtained by deleting d parentheses (generated from level d−1, deduped with a set). The **first level containing a valid string** has the minimum removals; return all valid strings from that level and stop.
Worst case still exponential, but simple and obviously minimal.

## Approach 3 — DFS with exact removal counts (optimal in practice)
1. **Count what must go:** scan once; `openRem` = unmatched `(`, `closeRem` = unmatched `)`. These are exactly the minimum removals.
2. **DFS** over positions with `(index, openRem, closeRem, balance)`:
   - **Delete** the current paren (if we still need to remove that type) — but within a run of identical parens only allow deletions that form a **prefix of the run**: delete `s[i]` only if it starts a run or `s[i−1]` was also deleted. Deleting any j chars of a run gives the same string, so this canonical choice removes every duplicate.
   - **Keep** it if the balance stays ≥ 0.
   - At the end, accept if `openRem == closeRem == 0` and `balance == 0`.
Exact counts guarantee minimality; the prefix-of-run rule guarantees distinct outputs — the Java version collects into a plain list and the tests confirm there are no duplicates.

## Approach 4 — Two-pass "remove from the left, then from the reversed string" (LeetCode classic)
Scan left→right removing extra `)` (choosing positions in a way that avoids duplicates), then reverse and do the same for `(`. Elegant, no set needed; harder to explain under pressure.

## Why the counts are the minimum
Every unmatched `)` in the left-to-right scan and every unmatched `(` left at the end must be deleted (nothing can match them), and deleting exactly those makes the string valid → minimum = `openRem + closeRem`.

## Pitfalls
- Duplicates: `"())"` → removing either `)` gives the same string.
- Letters must be copied through.
- Empty string `""` is a valid answer.

## Follow-ups
- **Minimum remove to make valid parentheses** (LC 1249) — return one answer, O(n) stack.
- **Minimum add to make valid** (LC 921) — counting only.
- **Longest valid parentheses** (LC 32) — stack / DP.

## 30-second interview script
"One scan tells me exactly how many '(' and ')' must be deleted — that's the minimum. Then I DFS over the string choosing to keep or delete parens, never deleting more than those counts, never letting the balance go negative, and only deleting the first char of a run of identical parens so duplicates aren't generated. BFS by number of deletions is the simpler alternative."
