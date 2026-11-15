# LC 630 — Course Schedule III (Hard)

**Pattern:** scheduling with deadlines — sort by deadline + max-heap "swap out the longest" · **Target:** O(n log n) · **Code:** [CourseScheduleIII.java](CourseScheduleIII.java)

## Problem
Courses `[duration, lastDay]`: a course must be taken continuously and finished by `lastDay`. Starting on day 1, take one course at a time. Maximum number of courses you can complete?

`[[100,200],[200,1300],[1000,1250],[2000,3200]] → 3`

## Why engineers fear it
It looks like a knapsack (time budget vs. count) but with per-item deadlines; the greedy that works needs a swap argument, and a naive greedy (shortest first, or earliest deadline without swapping) fails.

## Approach 1 — DP after sorting by deadline
Sort by `lastDay`. `dp[t]` = max courses finishing exactly by time `t` (0/1 knapsack, iterate `t` backwards, only `t ≤ lastDay`). **O(n · maxDay)** — too slow for 10⁴ days × 10⁴ courses in the worst case, but correct and a clear stepping stone.

## Approach 2 — Sort by deadline + max-heap (optimal)
```
sort courses by lastDay
time = 0; heap = max-heap of durations
for (d, last) in courses:
    time += d; heap.push(d)
    if time > last:                   # can't fit everything taken so far
        time -= heap.pop()            # drop the longest course taken (maybe this one)
answer = heap.size()
```
**Why sorting by deadline:** among any feasible set, scheduling in deadline order is always feasible (classic exchange argument).
**Why dropping the longest:** the count stays the same as before adding the current course, but the total time is as small as possible, leaving the most room for future courses.
**O(n log n)** time, **O(n)** space.

## Dry run — example
Sorted by deadline: (100,200), (1000,1250), (200,1300), (2000,3200)
- take 100 → time 100
- take 1000 → 1100 ≤ 1250
- take 200 → 1300 ≤ 1300
- take 2000 → 3300 > 3200 → drop 2000 → time 1300
Answer **3**.

## Pitfalls
- Sorting by duration instead of deadline.
- Using `if` vs `while` when popping — one pop suffices because before adding the current course the set was feasible.
- `long`/`int`: sums fit in `int` for LC limits.

## Follow-ups
- **Maximum number of events attended** (LC 1353) — min-heap of end days.
- **Job scheduling with profits** (LC 1235) — weighted → DP + binary search.
- **Minimise lateness** — earliest-deadline-first (no dropping).

## 30-second interview script
"Process courses in order of deadline, adding each one's duration to the running time and pushing it onto a max-heap. If the running time passes the current deadline, remove the longest course taken so far — the count stays the same but the total time shrinks the most, leaving room for later courses. The heap size at the end is the answer; O(n log n)."
