# LC 774 — Minimize Max Distance to Gas Station (Hard)

**Pattern:** binary search on a **real-valued** answer (or greedy heap) · **Target:** O(n log(W/ε)) · **Code:** [MinimizeMaxDistanceToGasStation.java](MinimizeMaxDistanceToGasStation.java)

## Problem
Stations at sorted positions on a line. Add exactly `k` new stations anywhere (real positions) to **minimise the largest gap** between adjacent stations. Answers within 10⁻⁶ are accepted.

`[1..10], k=9 → 0.5`

## Why engineers fear it
Real numbers (no integer binary search), and the greedy intuition ("put the next station in the biggest gap") has to be executed correctly — you split a gap into **equal** pieces, not halves.

## Approach 1 — Greedy, linear scan per station
For each of the `k` stations, find the gap whose current piece length `gap / (added + 1)` is largest and add one there. **O(k · n)**.

## Approach 2 — Greedy with a max-heap
Same greedy but the heap returns the largest current piece in O(log n). **O(n + k log n)**. Exact (no epsilon), but slow when k is 10⁶.

**Why greedy is optimal:** the final max gap is determined by the gap whose pieces are longest; giving a station to any other gap can't reduce the maximum, so the largest-piece gap must get it.

## Approach 3 — Binary search on the answer (optimal for large k)
For a candidate max gap `D`, gap `g` needs `⌈g / D⌉ − 1` extra stations. Total needed is non-increasing in `D` → binary search:
```
lo = 0, hi = maxGap
repeat ~100 times (or while hi - lo > 1e-6):
    mid = (lo + hi) / 2
    if need(mid) <= k: hi = mid else lo = mid
```
**Time** O(n · log(W/ε)) · **Space** O(1). Fixed iteration count avoids floating-point loop termination bugs.

## Approach 4 — Hybrid (bonus)
First give each gap `⌊g · k / total⌋` stations proportionally, then run the heap greedy for the remaining ≤ n stations: O(n log n), exact. Mentioning this shows depth.

## Dry run (heap) — stations [1, 2, 3, …, 10], k = 9
Nine gaps of 1. Each gets one station → every piece is 0.5 → answer **0.5**.

## Pitfalls
- Using integer division in `need` → use `Math.ceil(g / D) − 1` with doubles (or `(long)(g / D)` adjusted for exact multiples).
- Loop `while (hi - lo > 1e-6)` can be slow/infinite with float precision; a fixed 100 iterations is safe.
- The heap must store `(gap, count)` and compare `gap / (count + 1)`.

## Follow-ups
- Integer version (stations must be at integer positions) → integer binary search.
- **Magnetic force between two balls** (LC 1552) — maximise the minimum gap (the "opposite" problem).
- **Koko / split array** — integer "binary search on answer" cousins.

## 30-second interview script
"For a candidate maximum gap D, a gap g needs ceil(g/D)−1 extra stations, and the total is monotonic in D. So I binary search D on reals with a fixed number of iterations, checking whether k stations suffice. Alternatively a max-heap greedy that always splits the gap with the longest current piece is exact in O(k log n)."
