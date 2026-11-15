# LC 995 — Minimum Number of K Consecutive Bit Flips (Hard)

**Pattern:** greedy + difference array / sliding "flip parity" · **Target:** O(n) time, O(1) extra · **Code:** [MinKBitFlips.java](MinKBitFlips.java)

## Problem
A binary array; one operation flips a contiguous block of exactly `k` bits. Return the minimum number of operations to make every bit 1, or −1 if impossible.

`[0,1,0], k=1 → 2` · `[1,1,0], k=2 → −1` · `[0,0,0,1,0,1,1,0], k=3 → 3`

## Why engineers fear it
It smells like BFS over 2ⁿ states or DP. The real solution is a greedy argument plus a trick to avoid actually flipping k bits each time.

## Key insight (greedy is forced)
Scan left to right. The **leftmost 0** at position `i` can only be fixed by a flip that starts at `i` (any flip starting earlier would affect already-fixed bits to the left; flips are commutative and flipping the same window twice cancels). So the greedy "flip at the first 0" is **the only** choice → it's optimal and also decides feasibility.

## Approach 1 — Brute BFS over states (tiny n only)
Each state is a bitmask; each move flips one of `n−k+1` windows. BFS gives the minimum. **O(2ⁿ · n)** — useful only as a test oracle and to show why greedy is needed.

## Approach 2 — Greedy with real flipping
When `a[i] == 0`, flip `a[i..i+k−1]` explicitly. If `i + k > n`, return −1. **O(n·k)** time.

## Approach 3 — Greedy with flip parity (optimal)
Track `active` = number of flips currently covering `i` (mod 2 is all that matters). The effective bit is `a[i] ^ (active & 1)`.
- When a flip starts at `i`, it ends after `i + k − 1`; record its end in a difference array `ended[i + k]`, or mark `a[i] = 2` in place so that when the window passes `i`, we know to decrement.
- At each `i`: first remove flips that ended (`if i ≥ k and a[i − k] marked → active−−`), then if the effective bit is 0, start a flip.

**Time** O(n) · **Space** O(1) extra with in-place marking (O(n) with a separate diff array).

## Dry run — `[0,0,0,1,0,1,1,0]`, k = 3
| i | active | effective | action |
|---|---|---|---|
| 0 | 0 | 0 | flip [0..2] → active 1 |
| 1 | 1 | 1 | – |
| 2 | 1 | 1 | – |
| 3 | 0 (flip 0 ended) | 1 | – |
| 4 | 0 | 0 | flip [4..6] → active 1 |
| 5 | 1 | 0 | flip [5..7] → active 2 |
| 6 | 2 | 1 | – |
| 7 | 1 (flip 4 ended) | 1 | – |
Answer **3**.

## Pitfalls
- Removing expired flips **before** reading the effective bit.
- Starting a flip with `i + k > n` → impossible, return −1.
- With in-place marking, restore the input if the caller needs it (or work on a copy).

## Follow-ups
- **Minimum operations to make all 1s with flips of length 3** (LC 3191) — same greedy.
- **Bulb switcher / range-update parity** problems use the same difference-array trick.
- **Range add with a difference array** is the general tool: O(1) per range update, prefix sum to read.

## 30-second interview script
"The leftmost zero can only be fixed by a flip starting exactly there, so greedily flipping at each zero is forced and optimal. Instead of flipping k bits, I keep the count of active flips covering the current index and expire them with a difference array — the effective bit is the original XOR the parity. That's O(n)."
