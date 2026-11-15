# LC 41 — First Missing Positive (Hard)

**Pattern:** index-as-hash, cyclic sort · **Target:** O(n) time, O(1) extra space · **Code:** [FirstMissingPositive.java](FirstMissingPositive.java)

## Problem
Given an unsorted integer array (may contain negatives, zeros, duplicates), return the smallest positive integer that does **not** appear in it. The solution must run in linear time and use only constant extra memory.

`[3, 4, -1, 1] → 2` · `[7, 8, 9, 11, 12] → 1` · `[1, 2, 0] → 3`

## Why engineers fear it
Everything easy is forbidden: sorting breaks O(n), a HashSet breaks O(1) space. The trick is to realise the **array itself can be the hash table**.

## Key insight
For an array of length `n`, the answer is always in **`[1, n + 1]`**. (If all of 1..n are present, the answer is n+1; otherwise some value in 1..n is missing.) So any value `≤ 0` or `> n` is irrelevant, and value `v` "belongs" at index `v − 1`.

## Approach 1 — Sort and scan
Sort, then walk looking for 1, 2, 3, … skipping duplicates/negatives.
**Time** O(n log n) · **Space** O(1)–O(n) depending on sort. Violates the time bound.

## Approach 2 — HashSet
Insert everything, then test 1, 2, … until one is missing (at most n+1 checks).
**Time** O(n) · **Space** O(n). Violates the space bound.

## Approach 3 — Boolean "seen" array of size n+1
Same as the set but with `boolean[n + 2]`, ignoring values outside `[1, n]`. Faster constant, still O(n) space. This is the stepping-stone idea: **only 1..n matter**.

## Approach 4 — Cyclic sort (optimal)
Put every value `v ∈ [1, n]` at index `v − 1` by swapping:
```
for i in 0..n-1:
    while 1 <= a[i] <= n and a[a[i]-1] != a[i]:
        swap(a[i], a[a[i]-1])
first i with a[i] != i+1  → answer i+1, else n+1
```
**Why it's O(n):** every swap places at least one value in its final home, which is never moved again, so total swaps ≤ n even though there's a nested `while`.
**Why `a[a[i]-1] != a[i]` and not `a[i] != i+1`:** with duplicates (`[1, 1]`) the second form loops forever.

## Approach 5 — Sign marking (optimal, no swaps)
1. Replace every value `≤ 0` or `> n` with `n + 1` (harmless filler).
2. For each `|v| ≤ n`, make `a[|v| − 1]` negative ("v is present").
3. First index with a positive value → answer `i + 1`; else `n + 1`.
**Time** O(n) · **Space** O(1). Three clean passes; no tricky loop.

## Dry run (cyclic sort) — `[3, 4, -1, 1]`
| i | action | array |
|---|---|---|
| 0 | 3 → idx 2 (swap with −1) | [−1, 4, 3, 1] |
| 0 | −1 out of range, stop | |
| 1 | 4 → idx 3 (swap with 1) | [−1, 1, 3, 4] |
| 1 | 1 → idx 0 (swap with −1) | [1, −1, 3, 4] |
| 2, 3 | already home | |
Scan: index 1 holds −1 ≠ 2 → **answer 2**.

## Pitfalls
- Infinite loop on duplicates (see above).
- Using `a[i] - 1` as an index without the range check → `ArrayIndexOutOfBounds`.
- Sign marking: read `Math.abs(a[i])` because earlier steps may already have negated it.
- The problem allows modifying the input; say so out loud in the interview.

## Follow-ups
- **Find all missing numbers in 1..n** (LC 448) — same sign trick.
- **Find all duplicates** (LC 442) — when you try to negate an already-negative slot, it's a duplicate.
- **Missing and repeating number** — sign trick or XOR / math.

## 30-second interview script
"The answer must lie in 1..n+1, so values outside that range don't matter. I'll use the array as its own hash table: cyclic-sort each value v into index v−1 — every swap fixes one element so it's linear — then the first index i where a[i] ≠ i+1 gives the answer, otherwise n+1. That's O(n) time and O(1) extra space."
