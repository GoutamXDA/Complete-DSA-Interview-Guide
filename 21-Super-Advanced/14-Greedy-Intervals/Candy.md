# LC 135 — Candy (Hard)

**Pattern:** two-pass greedy (left constraints, then right constraints) or one-pass slope counting · **Target:** O(n) · **Code:** [Candy.java](Candy.java)

## Problem
Children in a line with ratings. Each child gets ≥ 1 candy; a child with a **higher** rating than an adjacent neighbour must get **more** candy than that neighbour. Minimum total candies?

`[1,0,2] → 5` (2,1,2) · `[1,2,2] → 4` (1,2,1)

## Why engineers fear it
Constraints go in both directions, and fixing one side can break the other. Equal ratings impose **no** constraint, which people often get wrong.

## Approach 1 — Repeated relaxation
Start everyone at 1; repeatedly sweep fixing any violated pair until stable. Correct but **O(n²)** worst case (a long decreasing run fixes one step per sweep).

## Approach 2 — Two passes (optimal and standard)
1. Left → right: if `r[i] > r[i−1]`, `c[i] = c[i−1] + 1` (else 1).
2. Right → left: if `r[i] > r[i+1]`, `c[i] = max(c[i], c[i+1] + 1)`.
Sum. **O(n)** time, **O(n)** space.

**Why it's minimal:** after pass 1, `c[i]` is exactly the length of the increasing run ending at `i` (the minimum forced by left neighbours); pass 2 does the same for the right. Each child needs at least the max of the two lower bounds, and that assignment satisfies both sides.

## Approach 3 — One pass, O(1) space (slopes)
Walk the ratings as mountains: count the current `up` run length and `down` run length. A peak's candy is `max(up, down) + 1`; each strictly increasing/decreasing run of length `k` contributes `1 + 2 + … + k`. Adjust when the down run becomes longer than the preceding up run (the peak must grow). Equal ratings reset both runs.
**O(n)** time, **O(1)** space — the follow-up interviewers ask for.

## Dry run (two passes) — `[1,3,4,5,2]`
pass 1: `[1,2,3,4,1]`; pass 2 (from right): index 3 (5 > 2) → max(4, 2) = 4 … total **11**.

## Pitfalls
- Equal neighbours: **no** requirement (`[1,2,2] → 4`, the last child gets 1).
- Pass 2 must take the `max`, not overwrite.
- One-pass version: off-by-one when a descent is longer than the ascent before it.

## Follow-ups
- **Trapping rain water** (LC 42) — the same "constraints from both sides" two-pass idea.
- **Minimum number of increments on subarrays** (LC 1526) — slope counting.
- Circular arrangement → handle the wrap by starting at a local minimum.

## 30-second interview script
"Everyone starts at one. A left-to-right pass enforces 'higher than the left neighbour gets more', a right-to-left pass enforces the same for the right neighbour while keeping the max of the two. Each value is the smallest that satisfies both sides, so the sum is minimal — O(n). There's also an O(1)-space one-pass version that counts lengths of up and down slopes."
