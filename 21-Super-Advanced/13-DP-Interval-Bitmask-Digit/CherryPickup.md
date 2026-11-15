# LC 741 — Cherry Pickup (Hard)

**Pattern:** turn "go and return" into **two simultaneous walkers** → DP over (step, r1, r2) · **Target:** O(n³) · **Code:** [CherryPickup.java](CherryPickup.java)

## Problem
`n × n` grid: `1` cherry, `0` empty, `−1` thorn. Walk from (0,0) to (n−1,n−1) moving right/down, then back moving left/up, collecting cherries (each cell's cherry once). Maximum cherries, or 0 if no valid path.

`[[0,1,-1],[1,0,-1],[1,1,1]] → 5`

## Why engineers fear it
The obvious idea — run the best path DP twice (take the max path, clear its cherries, run again) — is **wrong**: the greedy first path can block a much better pair of paths.

## Why two greedy passes fail
Counter-example found by the Java file's random search (−1 = thorn):
```
1  0  1  0
1  1  0  0
0  1  1 -1
0  1  0  0
```
There are 7 cherries and a coordinated pair of paths collects **all 7** (one path takes (0,2) via the top row, the other takes the left column and (3,1)). Greedy first takes a 5-cherry path, clears it, and the second pass can only add 1 more → **6**. Committing to the single best path first destroys the better combination.

## Key reformulation
The return trip reversed is just another forward path. So: **two people walk from (0,0) to (n−1,n−1) at the same time**, one step each per time unit. After `t` steps, both are on the anti-diagonal `r + c = t`, so the state is `(t, r1, r2)` (columns follow: `c = t − r`). If they're on the same cell, count its cherry once.

## Approach 1 — Brute force over path pairs (oracle)
Enumerate all monotone paths (C(2n−2, n−1) of them) and try every pair. Tiny grids only.

## Approach 2 — Top-down DP on (r1, c1, r2) (c2 derived)
`f(r1, c1, r2)` = best from these positions to the end; 4 transitions (each walker goes down or right). Memo → **O(n³)** states, O(1) work.

## Approach 3 — Bottom-up over steps (optimal space)
`dp[r1][r2]` for the current step `t`; for each `t` from 1 to `2n−2`, compute from the previous step's four combinations `(r1 or r1−1, r2 or r2−1)`. Thorns or out-of-range → −∞.
**O(n³)** time, **O(n²)** space.

## Pitfalls
- Double counting when `r1 == r2` (same cell).
- Initialise unreachable states with −∞ (not 0), and return `max(0, answer)`.
- Thorn at the start or end → 0.

## Follow-ups
- **Cherry pickup II** (LC 1463) — two robots moving down rows: DP over (row, c1, c2).
- **Maximum path sum with k walkers** → state grows to (t, r1, …, rk).
- **Minimum path sum** (LC 64) — the one-walker base case.

## 30-second interview script
"Going there and back is the same as two people walking forward together, one step at a time, so after t steps both are on the diagonal r + c = t and the state is (t, r1, r2). Each step has four combinations of moves; if they land on the same cell the cherry counts once, thorns are −∞. That's O(n³) time and O(n²) space with a rolling array. Running the single-path DP twice is wrong, because the first greedy path can block a better pair."
