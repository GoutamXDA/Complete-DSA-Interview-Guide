# LC 321 — Create Maximum Number (Hard)

**Pattern:** split k between two arrays; each part = max subsequence via monotonic stack; combine with a greedy lexicographic merge · **Target:** O(k · (m + n) · k) · **Code:** [CreateMaximumNumber.java](CreateMaximumNumber.java)

## Problem
Two digit arrays `nums1` (length m) and `nums2` (length n). Create the maximum number of length `k ≤ m + n` using digits from both while **preserving the relative order within each array**.

`nums1=[3,4,6,5], nums2=[9,1,2,5,8,3], k=5 → [9,8,6,5,3]`

## Why engineers fear it
It combines **three** sub-problems, and the merge step has a subtle tie-breaking rule that breaks naive implementations.

## Decomposition
For every split `i` digits from `nums1` and `k − i` from `nums2` (`max(0, k−n) ≤ i ≤ min(k, m)`):
1. `a = maxSubsequence(nums1, i)`, `b = maxSubsequence(nums2, k − i)`
2. `c = merge(a, b)`
Answer = lexicographically largest `c`.

## Sub-problem 1 — Max subsequence of length t (monotonic stack)
Keep a decreasing stack; for each digit, pop smaller tops **while** there are still enough digits left to fill `t` (`drop` budget = len − t). Push if the stack has room. **O(len)**. (Same as LC 402 "remove k digits", maximising instead of minimising.)

## Sub-problem 2 — Merge two sequences into the largest one
At each step take from the sequence whose **remaining suffix is lexicographically larger** — not just whose current digit is larger.

Counter-example for the naive rule "on equal digits, take from `a`": `a = [6,0,4]`, `b = [6,7]`.
- Naive: take `a`'s 6 → then `b`'s 6, 7 → then 0, 4 → `[6,6,7,0,4]`.
- Suffix rule: `[6,7] > [6,0,4]`, so take `b`'s 6 first → `[6,7,6,0,4]`, which is larger (7 > 6 at index 1).
The equal first digits hide which side unlocks a bigger digit sooner; comparing the whole remaining suffix resolves it. **O((i + j)²)** worst case per merge (suffix comparisons), fine for k ≤ 500.

## Approach 1 — Brute force (oracle)
Enumerate all ways to pick `k` digits from both arrays as an interleaving, keep the max. Exponential; tiny inputs only.

## Approach 2 — Split + stack + suffix-compare merge (standard optimal)
Try every split, build both parts in linear time, merge, compare. **O(k · (m + n + k²))** worst case — accepted for LC limits.

## Pitfalls
- Merge tie-breaking (compare suffixes).
- Valid split range for `i`.
- Comparing candidate answers lexicographically (same length, so digit by digit).

## Follow-ups
- **Remove K digits** (LC 402) — sub-problem 1 alone (minimise).
- **Smallest subsequence of distinct characters** (LC 1081) — monotonic stack with "must keep" counts.
- **Largest merge of two strings** (LC 1754) — sub-problem 2 alone.

## 30-second interview script
"I try every way to split k between the two arrays. For each part, the largest subsequence of a given length comes from a monotonic stack that pops smaller digits while I can still afford to drop them. Then I merge the two parts greedily, always taking from the sequence whose remaining suffix is lexicographically larger — that handles ties correctly. The best merged result over all splits is the answer."
