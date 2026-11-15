# LC 887 — Super Egg Drop (Hard)

**Pattern:** minimax DP → binary search on the monotone split → **inverted DP** (moves × eggs → floors) · **Target:** O(k · log n) · **Code:** [SuperEggDrop.java](SuperEggDrop.java)

## Problem
`k` identical eggs, `n` floors. There's an unknown floor `f` (0 ≤ f ≤ n): eggs dropped above `f` break, at or below survive. Minimum number of drops that **guarantees** finding `f` in the worst case.

`k=1, n=2 → 2` · `k=2, n=6 → 3` · `k=3, n=14 → 4` · `k=2, n=100 → 14`

## Why engineers fear it
The textbook DP is O(k·n²) and times out (n = 10⁴). Getting to an efficient solution requires either a monotonicity argument or completely flipping the DP's question.

## Approach 1 — Classic minimax DP
`dp[e][f]` = min drops with `e` eggs and `f` floors. Drop at floor `x`:
- breaks → `dp[e−1][x−1]` floors below remain,
- survives → `dp[e][f−x]` floors above remain.
`dp[e][f] = 1 + min over x of max(dp[e−1][x−1], dp[e][f−x])`. **O(k · n²)**.

## Approach 2 — Binary search on x
`dp[e−1][x−1]` increases with `x`, `dp[e][f−x]` decreases with `x` → the max is minimised where they cross → binary search `x`. **O(k · n log n)**. (A monotone "optimal x" pointer gives O(k·n).)

## Approach 3 — Inverted DP (optimal)
Ask instead: with `m` moves and `e` eggs, what's the **maximum number of floors** you can fully resolve?
`F(m, e) = F(m−1, e−1) + F(m−1, e) + 1`
- Drop once (the `+1` is the floor you drop from):
  - if it breaks, the floors **below** must be solvable with `m−1` moves and `e−1` eggs;
  - if it survives, the floors **above** with `m−1` moves and `e` eggs.
Increase `m` until `F(m, k) ≥ n`. With a 1D array over eggs updated backwards: **O(k · m)** where `m ≤ log₂ n`-ish for large k (and ≤ n for k = 1).

## Approach 4 — Closed form via binomials
`F(m, k) = Σ_{i=1..k} C(m, i)`; binary search the smallest `m` with `F(m, k) ≥ n`. **O(k log n)**. A nice mathematical finish.

## Why the inverted DP is valid
It's the same game seen from the other side: the minimum `m` with `F(m, k) ≥ n` is exactly the minimum worst-case number of drops, because a strategy with `m` moves can distinguish at most `F(m, k) + 1` outcomes (floors 0..F).

## Pitfalls
- `k = 1` → answer is `n` (linear search) — the inverted DP handles it but takes n iterations; that's fine (n ≤ 10⁴).
- Overflow in binomial sums → cap at `n`.
- In Approach 2, be careful which side of the crossing to take (check both neighbours).

## Follow-ups
- **Egg drop with 2 eggs and N floors** (LC 1884) — `m(m+1)/2 ≥ n`.
- Report the **strategy** (which floors to drop from) — follow the inverted DP: first drop at `F(m−1, k−1) + 1`.

## 30-second interview script
"The direct DP over (eggs, floors) is O(k n²). I flip the question: with m moves and k eggs, how many floors can I cover? One drop splits the problem into the floors below (if it breaks: m−1 moves, k−1 eggs) and above (m−1 moves, k eggs), so F(m, k) = F(m−1, k−1) + F(m−1, k) + 1. I increase m until F(m, k) ≥ n, which takes O(k · answer) time."
