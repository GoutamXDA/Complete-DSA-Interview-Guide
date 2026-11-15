# LC 850 — Rectangle Area II (Hard)

**Pattern:** coordinate compression + sweep line + segment tree of **covered length** · **Target:** O(n log n) · **Code:** [RectangleAreaII.java](RectangleAreaII.java)

## Problem
Axis-aligned rectangles `[x1, y1, x2, y2]` (up to 200, coordinates up to 10⁹). Return the total area covered by their **union**, modulo 10⁹ + 7.

`[[0,0,2,2],[1,0,2,3],[1,0,3,1]] → 6`

## Why engineers fear it
Inclusion–exclusion over overlaps is exponential; a grid is too big (10⁹). The canonical solution — a segment tree that tracks covered length with **cover counts and no lazy push-down** — is one of the most intricate classic data-structure tricks.

## Approach 1 — Coordinate-compressed grid
Compress all x and y coordinates; mark every compressed cell covered by any rectangle; sum the areas of covered cells. **O(n³)** (n² cells × n rectangles, or n² cells with 2D difference arrays). Fine for n = 200 and a great oracle.

## Approach 2 — Sweep line over x, recompute covered y-length each time
Events at each `x1` (add `[y1, y2)`) and `x2` (remove it). Between consecutive event x's, area += covered y-length × Δx. Computing the covered length by sorting the active intervals each time → **O(n² log n)**.

## Approach 3 — Sweep line + segment tree over compressed y (optimal)
Segment tree over compressed y-intervals, each node storing:
- `count` = how many active rectangles **fully** cover this node's range (only set where an update lands),
- `covered` = total covered length within this node's range.
Update `[y1, y2)` with +1/−1; then recompute:
```
covered(node) = count > 0 ? full length of node
              : leaf ? 0
              : covered(left) + covered(right)
```
No lazy propagation is needed, because every "add" is matched by an identical "remove" on the same set of nodes. The root's `covered` is the union length. **O(n log n)**.

## Pitfalls
- Overflow: Δx × length can reach 10¹⁸ → `long`, then mod at the end (or each step).
- Segment tree over **elementary intervals** between consecutive compressed y's (n−1 leaves), not over points.
- Process all events at the same x before computing the next strip (or just compute strip area before each event; zero width contributes nothing).

## Follow-ups
- **Perimeter of union of rectangles** — same sweep tracking the number of covered segments.
- **Rectangle area** (LC 223) — two rectangles, inclusion–exclusion.
- **Skyline** (LC 218, see `09-Heaps`), **falling squares** (LC 699) — related sweeps.

## 30-second interview script
"I sweep a vertical line across x. At each rectangle's left edge I add its y-interval, at its right edge I remove it, and between events the area grows by the covered y-length times the x-gap. The covered length comes from a segment tree over compressed y-coordinates where each node keeps how many rectangles fully cover it and its covered length — if count > 0 it's fully covered, otherwise it's the sum of its children. Updates and the query are O(log n), so O(n log n) overall."
