# LC 233 — Number of Digit One (Hard)

**Pattern:** per-digit contribution counting (math) or digit DP · **Target:** O(log n) · **Code:** [NumberOfDigitOne.java](NumberOfDigitOne.java)

## Problem
Count the total number of digit `1`s appearing in all integers from `0` to `n`.

`n = 13 → 6` (1, 10, 11 ×2, 12, 13)

## Why engineers fear it
n up to 10⁹ rules out iteration. You need either a clean counting formula per digit position or a general digit-DP template — both are easy to get off-by-one.

## Approach 1 — Brute force
Count ones in every number. **O(n log n)**.

## Approach 2 — Contribution of each digit position (optimal)
For position with weight `p = 1, 10, 100, …`, split `n` into `high = n / (10p)`, `cur = (n / p) % 10`, `low = n % p`:
- `cur == 0` → this position is `1` exactly `high · p` times
- `cur == 1` → `high · p + (low + 1)` times
- `cur ≥ 2` → `(high + 1) · p` times
Sum over all positions. **O(log n)** time, O(1) space.

**Intuition:** the position cycles through `0..9` every `10p` numbers, spending `p` consecutive numbers on digit 1. `high` full cycles contribute `high · p`; the partial cycle contributes 0, `low + 1`, or a full `p` depending on `cur`.

## Approach 3 — Digit DP (general template)
State `(pos, countSoFar, tight)`; at each position choose digit `d ∈ [0, tight ? digit[pos] : 9]`; memoise non-tight states. Result = sum of `countSoFar` at the end. **O(log n · 10 · log n)**. This template solves the whole family (count numbers with property X up to n).

## Dry run (Approach 2) — n = 13
- p = 1: high = 1, cur = 3, low = 0 → (1 + 1) · 1 = 2 (1, 11)
- p = 10: high = 0, cur = 1, low = 3 → 0 + 3 + 1 = 4 (10, 11, 12, 13)
Total **6**.

## Pitfalls
- `long` for `p` (10 × 10⁹ overflows `int`).
- `n ≤ 0` → 0.
- The `cur == 1` case adds `low + 1` (includes the number with low = 0).

## Follow-ups
- **Digit count in range** (count digit d in [L, R]) → `f(R) − f(L−1)`; careful with `d = 0` (leading zeros).
- **Numbers at most N given digit set** (LC 902), **count numbers with unique digits** (LC 357), **non-negative integers without consecutive ones** (LC 600) — digit DP.

## 30-second interview script
"I count each digit position separately. For position p, the digits above it (high), the digit itself (cur) and the digits below (low) tell me how many full cycles of ten there are and how far into the current cycle n reaches: high·p ones from full cycles, plus 0, low+1, or p more depending on whether cur is 0, 1, or bigger. Summing over positions is O(log n). Digit DP is the general fallback."
