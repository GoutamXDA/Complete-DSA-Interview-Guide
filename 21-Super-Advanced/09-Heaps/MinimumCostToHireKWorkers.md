# LC 857 — Minimum Cost to Hire K Workers (Hard)

**Pattern:** sort by a ratio, then a max-heap keeping the k smallest of another attribute · **Target:** O(n log n) · **Code:** [MinimumCostToHireKWorkers.java](MinimumCostToHireKWorkers.java)

## Problem
Worker `i` has `quality[i]` and minimum `wage[i]`. Hire exactly `k` workers such that:
1. pay is proportional to quality within the group, and
2. everyone gets at least their minimum wage.
Return the minimum total cost.

`quality=[10,20,5], wage=[70,50,30], k=2 → 105.0` (workers 0 and 2 at ratio 7)

## Why engineers fear it
It's an optimisation over subsets with a coupling constraint. The trick is to see that the group is paid at a single **ratio** (pay per unit quality), determined by its most "expensive" member.

## Key insight
If the group pays `r` per unit quality, every member needs `r ≥ wage[i] / quality[i]`. So `r = max ratio in the group`, and the cost is `r × (sum of qualities)`.
→ Fix the "captain" (the worker with the max ratio); among workers with ratio ≤ captain's, pick the `k − 1` **smallest qualities** (plus the captain).

## Approach 1 — Try every captain, sort qualities each time
For each worker as captain, collect eligible workers, sort by quality, sum the k smallest (must include the captain). **O(n² log n)**.

## Approach 2 — Sort by ratio + max-heap of qualities (optimal)
Sort workers by ratio ascending. Sweep; when worker `i` is processed, all previous workers have ratio ≤ `ratio[i]`, so `i` can be the captain.
```
for worker in ascending ratio:
    heap.push(quality); sumQ += quality
    if heap.size > k: sumQ -= heap.pop_max()      # drop the largest quality
    if heap.size == k: best = min(best, sumQ × ratio(worker))
```
**Subtlety:** if the current worker's own quality gets popped, the group no longer contains its captain — but then the computed cost uses a ratio **larger** than needed for that group, so it's an overestimate of a group already evaluated earlier. It never produces a wrong minimum.
**O(n log n)** time, **O(n)** space.

## Pitfalls
- Compute ratios as doubles; compare with `Double.compare`.
- Remember the result is a double (accept 10⁻⁵ error).
- The heap is a **max**-heap of qualities (we evict the biggest).

## Follow-ups
- **Maximum performance of a team** (LC 1383) — sort by efficiency desc, min-heap of speeds; same shape.
- **IPO** (LC 502) — sort by capital + max-heap of profits.
- **Maximum subsequence score** (LC 2542) — sort by one array, heap over the other.

## 30-second interview script
"A group is paid at one rate per unit quality, and that rate must be at least the largest wage/quality ratio in the group — so cost = max ratio × total quality. I sort workers by ratio; scanning in that order, the current worker can be the one setting the rate, and I want the smallest total quality among k workers seen so far, which a max-heap of size k maintains. O(n log n)."
