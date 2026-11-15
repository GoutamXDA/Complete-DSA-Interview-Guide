# LC 282 — Expression Add Operators (Hard)

**Pattern:** backtracking that evaluates on the fly, carrying the last operand for `*` precedence · **Target:** O(4ⁿ · n) worst · **Code:** [ExpressionAddOperators.java](ExpressionAddOperators.java)

## Problem
Given a digit string `num` and a `target`, insert `+`, `-`, `*` (or nothing, to join digits) between digits so the expression equals `target`. Return all such expressions. Operands can't have leading zeros (except `0` itself).

`"123", 6 → ["1*2*3","1+2+3"]` · `"105", 5 → ["1*0+5","10-5"]` · `"00", 0 → ["0*0","0+0","0-0"]`

## Why engineers fear it
Multiplication has higher precedence, so you can't evaluate left to right naïvely — and re-parsing every candidate expression is too slow.

## Approach 1 — Generate all, then evaluate
Each of the `n−1` gaps gets one of 4 choices (`+ − *` or join). Build the string, evaluate it with a precedence-aware parser, keep matches. **O(4ⁿ⁻¹ · n)** with a heavy constant (string building + parsing). Correct and a good oracle.

## Approach 2 — Backtracking with running value and last operand (optimal)
Carry `value` (expression result so far) and `last` (the signed last term, i.e., the last product chain):
- `+ cur`: `value + cur`, `last = cur`
- `− cur`: `value − cur`, `last = −cur`
- `* cur`: undo the last term and replace it with `last · cur`:
  `value − last + last · cur`, `last = last · cur`

Choose the next operand `cur = num[pos..i]` for every `i ≥ pos` (break when it would have a leading zero).
**Time** O(4ⁿ · n) worst (n string appends per leaf) · **Space** O(n) recursion + output.

### Why the `*` update works
The expression is a sum of terms; only the last term can grow by multiplication. Subtracting `last` removes it, adding `last · cur` puts back the extended term.

## Dry run — "123", target 6
- `1` → `1+2` (value 3, last 2) → `1+2+3` = 6 ✔ ; `1+2*3` = 3 − 2 + 6 = 7 ✗
- `1*2` (value 2, last 2) → `1*2*3` = 2 − 2 + 6 = 6 ✔
- `12+3` = 15, `123` = 123, …

## Pitfalls
- Leading zeros: `"05"` is not a valid operand → stop extending when `num[pos] == '0'` and `i > pos`.
- Overflow: operands up to 10 digits → use `long`.
- Use a `StringBuilder` and truncate (`setLength`) on backtrack instead of concatenating strings.

## Follow-ups
- **Target sum** (LC 494) — only `+`/`−`, counts → DP.
- **Basic calculator** family — evaluating given expressions (topic 07 here).
- **24 game** (LC 679) — operators and parentheses over 4 numbers.

## 30-second interview script
"I backtrack over where to cut the next operand and which operator to put before it, evaluating as I go. To respect multiplication precedence I carry the last term: for '*' I subtract it and add last×cur. That avoids re-parsing; the worst case is still 4^n leaves."
