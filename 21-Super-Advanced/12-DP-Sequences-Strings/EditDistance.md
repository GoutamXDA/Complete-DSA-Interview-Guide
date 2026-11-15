# LC 72 — Edit Distance (Hard)

**Pattern:** two-string DP over prefixes (Levenshtein) · **Target:** O(mn) time, O(min(m, n)) space · **Code:** [EditDistance.java](EditDistance.java)

## Problem
Minimum number of single-character insertions, deletions and replacements to turn `word1` into `word2`.

`horse → ros = 3` (replace h→r, delete r, delete e) · `intention → execution = 5`

## Why engineers fear it
Mapping the three operations to the three neighbouring DP cells — and proving that this is exhaustive — trips people up, and the follow-ups (reconstruct the edits, O(n) space) are common.

## State
`dp[i][j]` = edit distance between `a[0..i)` and `b[0..j)`.
Base: `dp[i][0] = i` (delete all), `dp[0][j] = j` (insert all).
Transition:
- `a[i−1] == b[j−1]` → `dp[i][j] = dp[i−1][j−1]` (nothing to do)
- else `1 + min(`
  - `dp[i−1][j−1]` — **replace** `a[i−1]` with `b[j−1]`,
  - `dp[i−1][j]` — **delete** `a[i−1]`,
  - `dp[i][j−1]` — **insert** `b[j−1]` `)`

**Why these three cover everything:** in any optimal edit script, the last characters are either matched/replaced against each other, or `a`'s last char is deleted, or `b`'s last char was inserted.

## Approach 1 — Plain recursion
`f(i, j)` with the recurrence above. **O(3^(m+n))**.

## Approach 2 — Memoisation
Cache `f(i, j)`. **O(mn)** time and space.

## Approach 3 — Tabulation
Fill the `(m+1) × (n+1)` table row by row. **O(mn)** time and space; supports **reconstructing the operations** by walking back from `dp[m][n]`.

## Approach 4 — Two rows / one row (optimal space)
Row `i` only needs row `i−1`. With one array, keep the diagonal value in a temp variable before overwriting. **O(mn)** time, **O(n)** space (swap to make `n` the shorter string).

## Dry run — "horse" → "ros"
|   | ∅ | r | o | s |
|---|---|---|---|---|
| ∅ | 0 | 1 | 2 | 3 |
| h | 1 | 1 | 2 | 3 |
| o | 2 | 2 | 1 | 2 |
| r | 3 | 2 | 2 | 2 |
| s | 4 | 3 | 3 | 2 |
| e | 5 | 4 | 4 | **3** |

## Pitfalls
- Mixing up which neighbour is insert vs delete (both cost 1, so the number is right either way — but reconstruction will be wrong).
- One-row version: losing the diagonal (`prev`) before it's used.

## Follow-ups
- **One edit distance** (LC 161) — linear two-pointer check.
- **Delete operation for two strings** (LC 583) — only deletions → `m + n − 2·LCS`.
- **Minimum ASCII delete sum** (LC 712) — weighted deletions.
- **Edit distance with a threshold k** → only fill a diagonal band of width 2k+1: O(nk).

## 30-second interview script
"dp[i][j] is the edit distance between the first i chars of word1 and the first j chars of word2. If the last characters match it's dp[i−1][j−1]; otherwise one plus the min of replace (diagonal), delete (up) and insert (left). Base cases are i and j. O(mn) time, and since each row only depends on the previous one I can do it with a single array."
