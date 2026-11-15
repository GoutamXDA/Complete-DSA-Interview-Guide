# LC 330 — Patching Array (Hard)

**Pattern:** greedy on the "covered range" `[1, miss)` · **Target:** O(m + log n) · **Code:** [PatchingArray.java](PatchingArray.java)

## Problem
Sorted positive array `nums` and integer `n`. Add (patch) the minimum number of numbers so that every value in `[1, n]` can be formed as a sum of some elements.

`nums=[1,3], n=6 → 1` (add 2) · `nums=[1,5,10], n=20 → 2` (add 2, 4) · `nums=[1,2,2], n=5 → 0`

## Why engineers fear it
It sounds like subset-sum (exponential), but there's a one-variable invariant that makes it linear — once you see it, the proof is short.

## Key invariant
Let `miss` = the smallest sum we **cannot** form yet, so `[1, miss)` is fully covered.
- If the next number `x ≤ miss`: adding it extends coverage to `[1, miss + x)`.
- If `x > miss` (or no numbers left): `miss` itself can never be formed by later (bigger) numbers, so we **must** patch. The best patch is `miss` itself, which doubles coverage to `[1, 2·miss)`.

## Approach 1 — Brute force with a bitset of reachable sums
Try adding patches by BFS/IDDFS over candidate values, recomputing reachable sums each time. Exponential — oracle for tiny `n` only.

## Approach 2 — Greedy on `miss` (optimal)
```
miss = 1; i = 0; patches = 0
while miss <= n:
    if i < len(nums) and nums[i] <= miss:
        miss += nums[i]; i++
    else:
        miss += miss; patches++         # patch with miss
```
**Why patching with `miss` is optimal:** any patch `p ≤ miss` extends coverage to `miss + p ≤ 2·miss`, so `p = miss` is the largest possible extension; patches `> miss` leave `miss` unreachable.
**O(m + log n)** time (each patch doubles `miss`), O(1) space. Use `long` — `miss` can exceed `Integer.MAX_VALUE`.

## Dry run — nums = [1,5,10], n = 20
miss 1 → use 1 → 2 → 5 > 2: patch 2 → 4 → 5 > 4: patch 4 → 8 → use 5 → 13 → use 10 → 23 > 20. **2 patches**.

## Pitfalls
- `int` overflow for `miss` (n up to 2³¹ − 1).
- Using `nums[i] < miss` (strict) — `x == miss` is usable.

## Follow-ups
- **Smallest unreachable sum** of an array (GFG) — the same invariant without patching.
- **Maximum number of consecutive values you can make** (LC 1798) — identical loop with coins.
- **Minimum number of coins to add** (LC 2952) — same greedy.

## 30-second interview script
"I track miss, the smallest sum I can't build; everything below it is covered. If the next number is at most miss, using it extends coverage to miss + x. Otherwise nothing later can create miss, so I must add a patch, and the best patch is miss itself, doubling the covered range. Each patch doubles miss, so it's O(m + log n)."
