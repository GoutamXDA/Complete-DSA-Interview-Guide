# LC 51 / 52 — N-Queens (Hard)

**Pattern:** constraint backtracking with O(1) conflict checks → bitmasks · **Target:** O(n!) with tight pruning · **Code:** [NQueens.java](NQueens.java)

## Problem
Place `n` queens on an `n × n` board so none attack each other. LC 51 returns all boards; LC 52 returns only the count.

n=4 → 2 solutions · n=8 → 92 solutions

## Why engineers fear it
Representing diagonals, avoiding O(n) safety checks, and cleanly undoing state.

## Key observation
Exactly one queen per row → place row by row; choose a column for each. Conflicts are only column, "\" diagonal (`r − c` constant) and "/" diagonal (`r + c` constant).

## Approach 1 — Brute force over permutations
Every permutation of columns (one per row) automatically avoids row/column clashes; check diagonals for each. **O(n! · n²)** (or n!·n with sets). Fine for n ≤ 8, and a good oracle.

## Approach 2 — Backtracking with scanning
Place row by row; for each candidate cell scan upward for conflicts (O(n)). **O(n! · n)**.

## Approach 3 — Backtracking with boolean arrays (standard)
`cols[c]`, `diag[r − c + n − 1]`, `anti[r + c]` → O(1) checks; set before recursing, clear after. **O(n!)** worst, far less in practice.

## Approach 4 — Bitmask backtracking (fastest, LC 52)
Carry three masks for the current row: occupied columns, and diagonals shifted one step each row:
```
solve(row, cols, d1, d2):
    if row == n: count++
    free = ~(cols | d1 | d2) & ((1 << n) - 1)
    while free:
        bit = free & -free; free -= bit          # lowest free column
        solve(row+1, cols|bit, (d1|bit) << 1, (d2|bit) >> 1)
```
The shifts move each diagonal's "attack" to where it lands in the next row. No arrays, no undo step (values are passed by copy).

## Complexity
Upper bound O(n!) placements; actual node counts are much smaller (n = 12: ~850k nodes). Space O(n) recursion.

## Dry run (n = 4)
Row 0 col 1 → row 1 col 3 → row 2 col 0 → row 3 col 2 ✔ → `[".Q..","...Q","Q...","..Q."]`. Backtrack… second solution is its mirror.

## Pitfalls
- Diagonal index `r − c` is negative → offset by `n − 1`.
- Forgetting to undo the board character / flags after recursion.
- In the bitmask version, mask with `(1 << n) − 1` so shifted bits beyond the board don't matter.

## Follow-ups
- **Symmetry** halves the work: count solutions with the first queen in the left half, double, handle the middle column for odd n.
- **N-Queens existence for huge n** → constructive formula (solutions exist for all n ≥ 4).
- **Sudoku solver** (next file) — same "constraint sets + backtracking" pattern.

## 30-second interview script
"One queen per row, so I recurse row by row choosing a column. Columns and both diagonal families (r−c and r+c) are tracked in boolean arrays or bitmasks, so each safety check is O(1). The bitmask version computes all free columns at once and shifts the diagonal masks per row — that's the fastest way to count solutions."
