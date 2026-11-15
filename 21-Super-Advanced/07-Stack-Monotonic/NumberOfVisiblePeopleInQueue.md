# LC 1944 — Number of Visible People in a Queue (Hard)

**Pattern:** monotonic decreasing stack scanned from the right, counting pops · **Target:** O(n) · **Code:** [NumberOfVisiblePeopleInQueue.java](NumberOfVisiblePeopleInQueue.java)

## Problem
People stand in a line with distinct heights, looking right. Person `i` can see person `j > i` if everyone strictly between them is shorter than **both** `h[i]` and `h[j]`. Return, for each person, how many people they can see.

`[10,6,8,5,11,9] → [3,1,2,1,1,0]`

## Why engineers fear it
It's a "next greater" variant where you must **count** everyone visible, not just find one boundary — and the definition (shorter than both) needs care.

## Key insight
Looking right from `i`, the visible people are exactly the **increasing staircase** of heights starting next to `i`, stopping at (and including) the first person taller than `h[i]`.

## Approach 1 — Brute force
For each `i`, walk right keeping the max height seen between; `j` is visible iff that max is `< min(h[i], h[j])`. Stop after the first `h[j] > h[i]`. **O(n²)**.

## Approach 2 — Monotonic stack from the right (optimal)
Process `i = n−1 … 0` with a stack of heights that is **decreasing from bottom to top** (top = nearest to the right):
```
count = 0
while stack and stack.top < h[i]:
    pop; count++            # each popped person is visible to i (next step of the staircase)
if stack: count++           # the first taller person is visible too
ans[i] = count
push h[i]
```
**Why the popped people are visible:** the stack holds exactly the staircase seen from position `i + 1`, each shorter than the one below it, so nobody between blocks them from `i`.
**Why we can pop them for good:** they're shorter than `h[i]`, so `i` blocks them from everyone further left.
Each person is pushed/popped once → **O(n)** time, O(n) space.

## Dry run — `[10,6,8,5,11,9]` (right to left)
| i | h | pops | +taller? | ans | stack after |
|---|---|---|---|---|---|
| 5 | 9 | – | – | 0 | [9] |
| 4 | 11 | 9 | – | 1 | [11] |
| 3 | 5 | – | 11 | 1 | [11,5] |
| 2 | 8 | 5 | 11 | 2 | [11,8] |
| 1 | 6 | – | 8 | 1 | [11,8,6] |
| 0 | 10 | 6, 8 | 11 | 3 | [11,10] |

## Pitfalls
- Heights are distinct in LC 1944; with duplicates, equal heights block the view — use `<=` when popping and decide whether an equal person is visible (+1 for the first equal one).
- Count the first taller person **after** popping, only if the stack isn't empty.

## Follow-ups
- **Buildings with an ocean view** (LC 1762) — monotonic from the right, no counting.
- **Next greater element** (LC 496/503), **daily temperatures** (LC 739).
- **Number of valid subarrays** (LC 1063) — count via next-smaller indices.

## 30-second interview script
"Scanning from the right I keep a stack of heights that decreases toward the top — that's the staircase visible from the position to my right. For the current person I pop every shorter person, each of whom is visible, then if someone is left in the stack, that first taller person is visible too. Popped people are hidden from everyone further left by the current person, so each is popped once: O(n)."
