# LC 1723 — Find Minimum Time to Finish All Jobs (Hard)

**Pattern:** NP-hard partitioning → pruned backtracking, binary search + feasibility, or subset DP · **Target:** n ≤ 12 · **Code:** [MinimumTimeToFinishAllJobs.java](MinimumTimeToFinishAllJobs.java)

## Problem
Assign every job to one of `k` workers; a worker's time is the sum of its jobs. Minimise the **maximum** worker time.

`[3,2,3], k=3 → 3` · `[1,2,4,7,8], k=2 → 11` ({1,2,8} and {4,7})

## Why engineers fear it
It's multiprocessor scheduling — NP-hard. There's no greedy or polynomial trick; you must show **strong pruning** or an exponential DP and argue why n ≤ 12 makes it feasible.

## Approach 1 — Brute force assignment
Every job picks a worker: **kⁿ** assignments (12¹² ≈ 9·10¹²) — too slow, but trivially correct for tiny inputs.

## Approach 2 — Backtracking with pruning (fast in practice)
Assign jobs (sorted **descending**) one by one to workers:
1. **Bound:** skip if `load[w] + job ≥ best` (can't beat the current best).
2. **Symmetry:** if two workers have the same load, trying the job on both gives identical subtrees → skip duplicates (`seen` loads at this level).
3. **Empty worker:** once you've tried putting the job on an empty worker, other empty workers are equivalent → break.
Sorting descending makes the bound bite early. Typically thousands of nodes instead of trillions.

## Approach 3 — Binary search on the answer + feasibility backtracking
Binary search the limit `T` in `[max(jobs), sum(jobs)]`; feasibility "can all jobs fit with every worker ≤ T?" is a bin-packing check done with the same pruned backtracking. Monotonic → binary search. Often the fastest in practice because each check fails fast.

## Approach 4 — Bitmask DP (provable bound)
- `sum[mask]` = total time of the jobs in `mask` (O(2ⁿ) via lowest bit).
- `dp[i][mask]` = minimum possible max load when the first `i` workers handle exactly `mask`:
  `dp[i][mask] = min over submask s ⊆ mask of max(dp[i−1][mask \ s], sum[s])`.
- Answer `dp[k][full]`. Submask enumeration over all masks costs **O(3ⁿ)** per worker → **O(k · 3ⁿ)** ≈ 5·10⁶ for n = 12. Deterministic, no luck needed.

## Why sorting descending helps pruning
Big jobs placed first create large loads early, so the bound `load + job ≥ best` cuts deep branches near the root instead of at the leaves.

## Pitfalls
- The symmetry skip must compare **loads**, not worker indices.
- In the DP, iterate submasks with `s = (s − 1) & mask`.
- `k ≥ n` → answer is `max(jobs)` (each job alone).

## Follow-ups
- **Partition to K equal sum subsets** (LC 698) — feasibility version with a fixed target.
- **Fair distribution of cookies** (LC 2305) — identical problem, smaller n.
- **Minimum number of work sessions** (LC 1986) — bitmask DP with a session capacity.

## 30-second interview script
"This is NP-hard scheduling but n ≤ 12. I sort jobs descending and backtrack over workers, pruning when a load would reach the best answer so far and skipping workers with equal loads, which removes symmetric branches. For a guaranteed bound I'd use a subset DP: dp over workers and job masks with submask enumeration, O(k·3^n)."
