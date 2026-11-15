# LC 10 — Regular Expression Matching (Hard)

**Pattern:** two-string DP where `x*` means "zero or more x" · **Target:** O(mn) · **Code:** [RegularExpressionMatching.java](RegularExpressionMatching.java)

## Problem
Implement matching of the **whole** string `s` against pattern `p` with `.` (any single char) and `*` (zero or more of the **preceding** element).

`"aa" ~ "a" → false` · `"aa" ~ "a*" → true` · `"ab" ~ ".*" → true` · `"aab" ~ "c*a*b" → true` · `"mississippi" ~ "mis*is*p*." → false`

## Why engineers fear it
`*` binds to the previous character and can match **zero** occurrences, so the decision at each step branches. Greedy matching fails (`"aaa" ~ "a*a"`). Getting the base cases (empty string vs `a*b*c*`) right is error-prone.

## Approach 1 — Recursion (backtracking)
```
match(i, j):                      # s[i..], p[j..]
    if j == len(p): return i == len(s)
    first = i < len(s) and p[j] in {s[i], '.'}
    if j+1 < len(p) and p[j+1] == '*':
        return match(i, j+2)                    # use x* zero times
            or (first and match(i+1, j))        # use it once more, stay on x*
    return first and match(i+1, j+1)
```
Exponential worst case (e.g. `a*a*a*…b`).

## Approach 2 — Memoised recursion (top-down DP)
Cache `(i, j)` → **O(mn)** states × O(1) work.

## Approach 3 — Bottom-up DP
`dp[i][j]` = does `s[0..i)` match `p[0..j)`?
- `dp[0][0] = true`; `dp[0][j] = p[j−1] == '*' && dp[0][j−2]` (patterns like `a*b*` match empty).
- If `p[j−1] == '*'` (with `x = p[j−2]`):
  `dp[i][j] = dp[i][j−2]` (zero x) **or** `(x matches s[i−1] and dp[i−1][j])` (one more x).
- Else: `dp[i][j] = (p[j−1] matches s[i−1]) and dp[i−1][j−1]`.
**O(mn)** time; one/two rows for O(n) space.

### Why `dp[i−1][j]` (not `dp[i−1][j−2]`) for "one more x"
After consuming `s[i−1]` with `x*`, the `x*` is still available for further repetitions, so the pattern index stays at `j`.

## Pitfalls
- Treating `*` as a standalone wildcard (that's LC 44, wildcard matching).
- Missing the empty-string row: `"" ~ "a*b*"` is true.
- Indexing `p[j−2]` when `j < 2` (valid patterns never start with `*`).

## Follow-ups
- **Wildcard matching** (LC 44) — `*` matches any sequence → `dp[i][j−1] || dp[i−1][j]`.
- Add `+` (one or more) → `x+` = `xx*`.
- Real regex engines: Thompson NFA simulation gives O(mn) without backtracking blow-ups.

## 30-second interview script
"dp[i][j] says whether the first i chars of s match the first j chars of p. For a normal char or dot it's a diagonal step if the chars match. For x* there are two choices: skip x* entirely, dp[i][j−2], or, if x matches s[i−1], consume one char and stay on the same pattern position, dp[i−1][j]. The empty-string row handles patterns like a*b*. O(mn)."
