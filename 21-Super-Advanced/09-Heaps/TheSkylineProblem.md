# LC 218 — The Skyline Problem (Hard)

**Pattern:** sweep line over start/end events + a max-structure of active heights · **Target:** O(n log n) · **Code:** [TheSkylineProblem.java](TheSkylineProblem.java)

## Problem
Buildings `[left, right, height]` (rectangles on the x-axis). Return the skyline as **key points** `[x, h]`: the x where the outline's height changes to `h`. No consecutive key points with the same height.

`[[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]] → [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]`

## Why engineers fear it
Event ordering at equal x-coordinates is a minefield (a building ending where another starts, two buildings starting together, duplicate heights). Plus Java's heap can't delete arbitrary elements.

## Approach 1 — Height at every critical x
Collect all lefts/rights; for each x, the height is the max over buildings with `left ≤ x < right`. Emit when it changes. **O(n²)**.

## Approach 2 — Sweep with a TreeMap multiset (clean)
Events: `(left, −h)` for starts, `(right, +h)` for ends. Sort by x; at equal x, by the second value — so **starts come before ends**, and **taller starts first**, **shorter ends first**.
```
active = multiset {0: 1}
for (x, v) in events:
    if v < 0: active.add(−v) else active.remove(v)
    cur = active.max()
    if cur != prev: output [x, cur]; prev = cur
```
**O(n log n)** time, O(n) space. Real deletions via counts in a `TreeMap<Integer, Integer>`.

## Approach 3 — Max-heap with lazy deletion (classic)
Heap of `(height, right)`. At each critical x: push buildings starting at x, then pop heap tops whose `right ≤ x` (expired). The top is the current height. **O(n log n)**. No arbitrary deletions needed because only expired **tops** matter.

## Approach 4 — Divide and conquer (merge skylines)
Split buildings in half, compute both skylines, **merge** them like merge sort, tracking the current height of each side and emitting `max(h1, h2)` when it changes. **O(n log n)**. Elegant and shows mastery.

## Why the event ordering matters
- Two buildings `[0,2,3]` and `[2,5,3]`: if the end at x=2 were processed before the start, we'd emit `[2,0]` then `[2,3]` — wrong. Starts first avoids it.
- Starts at the same x: taller first, so we don't emit an intermediate lower key point.

## Pitfalls
- Duplicate heights in the active set → use counts.
- Merging consecutive equal heights (only emit on change).
- The ground `0` must always be present in the active set.

## Follow-ups
- **Rectangle area II** (LC 850) — sweep + segment tree for covered length (see `15-Advanced-Data-Structures`).
- **My calendar III** (LC 732), **meeting rooms II** — sweep lines over intervals.
- **Falling squares** (LC 699) — coordinate compression + segment tree.

## 30-second interview script
"I sweep over building edges. Each start adds a height, each end removes one, and I keep the active heights in a TreeMap multiset (always containing 0). After processing an event the skyline height is the max active height; whenever it changes I emit a key point. Sorting events with starts before ends and taller starts first handles all the tie cases. O(n log n)."
