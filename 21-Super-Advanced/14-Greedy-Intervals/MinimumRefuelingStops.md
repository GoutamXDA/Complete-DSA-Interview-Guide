# LC 871 — Minimum Number of Refueling Stops (Hard)

**Pattern:** "retroactive" greedy with a max-heap (or DP on stops) · **Target:** O(n log n) · **Code:** [MinimumRefuelingStops.java](MinimumRefuelingStops.java)

## Problem
A car starts with `startFuel` and must travel `target` miles (1 mile per litre). Stations `[position, fuel]` lie on the way (sorted by position). Each stop takes all of that station's fuel; the tank is unlimited. Minimum number of stops, or −1.

`target=100, startFuel=10, stations=[[10,60],[20,30],[30,30],[60,40]] → 2` (stop at 10 and 60)

## Why engineers fear it
The decision "should I stop here?" depends on the future. The insight is to **delay the decision**: drive as far as possible and, only when you run dry, pretend you had stopped at the best station you already passed.

## Approach 1 — DP over number of stops
`dp[k]` = the farthest distance reachable using exactly `k` stops (among the stations processed so far).
For each station `i` (in order), for `k` from `i` down to 0: if `dp[k] ≥ pos[i]`, then `dp[k+1] = max(dp[k+1], dp[k] + fuel[i])`.
Answer = smallest `k` with `dp[k] ≥ target`. **O(n²)** time, O(n) space. Clean to prove (0/1 knapsack shape, backwards loop).

## Approach 2 — Max-heap of passed stations (optimal)
```
fuel = startFuel; stops = 0; heap = max-heap
for each station (and finally the target):
    while fuel < station.position:          # can't reach it
        if heap empty: return -1
        fuel += heap.pop(); stops++          # refuel retroactively at the best passed station
    heap.push(station.fuel)
```
**Why it's optimal:** when you must refuel, you've already passed a set of stations; choosing the one with the most fuel maximises reach for the same number of stops (exchange argument — swapping any other choice for the largest never reduces reach). Order of stops doesn't matter because the tank is unlimited.
**O(n log n)** time, **O(n)** space.

## Dry run — example
fuel 10 → reach station 10 (push 60) → need 20: pop 60 (stops 1, fuel 70) → push 30, 30 at 20, 30 → station 60 reachable (push 40) → target 100 > 70: pop 40 (stops 2, fuel 110) → done. **2**.

## Pitfalls
- Treat the target as a final station with 0 fuel (so the loop checks reachability).
- `long` for fuel sums.
- Popping only **while** fuel is insufficient — not one pop per station.

## Follow-ups
- **Gas station** (LC 134) — circular route, single pass.
- **IPO** (LC 502), **course schedule III** (LC 630, next file) — same "max-heap of available options" greedy.
- Limited tank capacity → DP over (station, fuel) or a deque-based greedy.

## 30-second interview script
"I drive as far as my fuel allows, pushing every station I pass onto a max-heap of its fuel. Whenever I can't reach the next station or the target, I retroactively refuel at the best station I've passed — pop the max — and count a stop. Choosing the largest is always at least as good as any other choice, so this is optimal; O(n log n). A dp over 'farthest reach with k stops' gives an O(n²) alternative."
