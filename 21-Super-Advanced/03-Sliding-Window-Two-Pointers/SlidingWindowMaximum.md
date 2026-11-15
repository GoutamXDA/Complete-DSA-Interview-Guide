# LC 239 — Sliding Window Maximum (Hard)

**Pattern:** monotonic deque · **Target:** O(n) · **Code:** [SlidingWindowMaximum.java](SlidingWindowMaximum.java)

## Problem
For every window of size `k` sliding over `nums`, report its maximum.

`[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]`

## Why engineers fear it
A max-heap feels right but deletion of the outgoing element is O(n) in Java. The monotonic deque is the canonical "you either know it or you don't" structure.

## Approach 1 — Brute force
Scan each window. **O(n·k)**.

## Approach 2 — Max-heap with lazy deletion
Push `(value, index)`; before reading the top, pop while its index is outside the window. Each element pushed/popped once → **O(n log n)**.

## Approach 3 — TreeMap multiset
Add `nums[i]`, remove `nums[i−k]`, read `lastKey()`. **O(n log k)**. Works for any order-statistic query (min, max, median-ish).

## Approach 4 — Monotonic decreasing deque (optimal)
Deque holds **indices** whose values are strictly decreasing from front to back.
```
for i in 0..n-1:
    if dq.front == i - k: pop front              # out of window
    while dq not empty and nums[dq.back] <= nums[i]: pop back   # can never be a max again
    push i at back
    if i >= k-1: answer.add(nums[dq.front])
```
**Why popping smaller elements is safe:** if `j < i` and `nums[j] ≤ nums[i]`, then for every future window containing `j`, it also contains `i` (which leaves later) and `i` is at least as large — `j` is useless forever.
Each index is pushed and popped at most once → **O(n)** time, **O(k)** space.

## Approach 5 — Block prefix/suffix maxima
Split into blocks of size `k`. `pre[i]` = max from block start to `i`; `suf[i]` = max from `i` to block end. Window `[i, i+k−1]` spans at most two blocks → `max(suf[i], pre[i+k−1])`. **O(n)** with no deque — a neat alternative proof of linear time.

## Dry run (deque), k = 3 on `[1,3,-1,-3,5,3,6,7]`
| i | deque (values) | output |
|---|---|---|
| 0 | [1] | |
| 1 | [3] | |
| 2 | [3, −1] | 3 |
| 3 | [3, −1, −3] | 3 |
| 4 | [5] | 5 |
| 5 | [5, 3] | 5 |
| 6 | [6] | 6 |
| 7 | [7] | 7 |

## Pitfalls
- Store indices, not values (you need them to expire the front).
- `<=` vs `<` when popping: both are correct for max; `<=` keeps the deque smaller.
- Expire the front **before** reading it.

## Follow-ups
- **Shortest subarray with sum ≥ K** (LC 862) — deque over prefix sums (next file).
- **Constrained subsequence sum** (LC 1425), **jump game VI** (LC 1696) — DP + deque max.
- **Max value of equation** (LC 1499) — deque on `y − x`.

## 30-second interview script
"I keep a deque of indices with decreasing values. When a new element arrives, anything smaller at the back can never be a maximum again, so I pop it; the front expires when it leaves the window. The front is always the window max, and each index enters and leaves once — O(n)."
