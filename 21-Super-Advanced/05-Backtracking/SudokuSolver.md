# LC 37 — Sudoku Solver (Hard)

**Pattern:** constraint backtracking + bitmasks + MRV heuristic · **Target:** fast in practice (exponential worst case) · **Code:** [SudokuSolver.java](SudokuSolver.java)

## Problem
Fill a 9×9 grid (`'.'` = empty) so each row, column and 3×3 box contains digits 1–9 exactly once. The puzzle has exactly one solution.

## Why engineers fear it
Writing it correctly is easy; writing it **fast** on hard puzzles (where naive backtracking explores millions of nodes) and explaining why requires constraint-propagation ideas.

## Approach 1 — Naive backtracking with validity scans
For each empty cell in order, try 1–9; check row/column/box by scanning 27 cells. **9^E** worst case (E = empty cells) × 27 per check.

## Approach 2 — Backtracking with boolean / bitmask state (standard)
Keep `rows[r]`, `cols[c]`, `boxes[b]` as 9-bit masks of used digits (`b = r/3*3 + c/3`). Candidates for a cell = `~(rows[r] | cols[c] | boxes[b]) & 0x1FF`. O(1) checks and O(1) undo.

## Approach 3 — MRV: pick the most constrained cell (optimal in practice)
Instead of filling cells left-to-right, always fill the empty cell with the **fewest candidates** (Minimum Remaining Values):
- A cell with 0 candidates → dead end immediately (fail fast).
- A cell with 1 candidate → forced move, no branching ("naked single").
**Measured by the Java file's node counters** (digits placed):
| Puzzle | cells in order | MRV |
|---|---|---|
| LeetCode example | 4,208 | **51** |
| a famously hard puzzle | 49,558 | **10,101** |

**Cost per node** O(81) to find the MRV cell (cheap); the total number of nodes drops sharply — about 80× on the easy puzzle and 5× on the hard one. Adding hidden singles (below) cuts the hard case much further.

## Further constraint propagation (talking points)
- **Hidden singles:** a digit that fits in only one cell of a row/column/box must go there.
- **Dancing Links (Algorithm X):** models Sudoku as exact cover; the gold standard for solvers.

## Pitfalls
- Box index formula `r / 3 * 3 + c / 3`.
- Undo **both** the board and the three masks when backtracking.
- Return `true` up the stack immediately when solved so you don't overwrite the solution.

## Follow-ups
- **Valid Sudoku** (LC 36) — the checking part only.
- Count solutions / detect non-unique puzzles → don't stop at the first solution.
- Generalise to n²×n² boards — masks become `long` or `BitSet`.

## 30-second interview script
"I track used digits per row, column and box as 9-bit masks, so each cell's candidate set is one bit operation. I backtrack, but always on the empty cell with the fewest candidates: zero means fail immediately and one means a forced move. On the LeetCode example that MRV heuristic cuts the search from about 4,000 placements to about 50."
