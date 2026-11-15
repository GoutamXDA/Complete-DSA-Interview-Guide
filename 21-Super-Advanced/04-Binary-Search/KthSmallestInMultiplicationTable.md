# LC 668 — Kth Smallest Number in Multiplication Table (Hard)

**Pattern:** binary search on the value + O(m) counting by rows · **Target:** O(m log(mn)) · **Code:** [KthSmallestInMultiplicationTable.java](KthSmallestInMultiplicationTable.java)

## Problem
The `m × n` multiplication table has `table[i][j] = i · j` (1-indexed). Return the k-th smallest entry (with duplicates).

`m=3, n=3, k=5 → 3` (sorted: 1 2 2 3 3 4 6 6 9)

## Why engineers fear it
`m, n` up to 3·10⁴ → 9·10⁸ cells; you can't build it, and a heap over rows is too slow for large k.

## Approach 1 — Build and sort
**O(mn log mn)** time and O(mn) space. Only for tiny inputs.

## Approach 2 — Heap merge of m sorted rows
Each row `i` is `i, 2i, 3i, …`. Push the first element of each row; pop k times, pushing the next in that row. **O(m + k log m)** — too slow when k ~ mn.

## Approach 3 — Binary search on the value (optimal)
`count(x)` = number of entries ≤ x = `Σ_{i=1..m} min(⌊x / i⌋, n)` — row `i` contributes every multiple of `i` up to `x`, capped at `n` columns. **O(m)** per count.
`count` is monotonic → binary search the smallest `x ∈ [1, m·n]` with `count(x) ≥ k`.
**Time** O(m log(mn)) (swap so m ≤ n for speed) · **Space** O(1).

### Why the result is an actual table value
Same argument as LC 719: if `x` weren't in the table, `count(x − 1) = count(x)` and `x` wouldn't be the smallest.

### Small optimisation
Rows with `i > x` contribute 0, and rows with `x / i ≥ n` contribute `n`; you can start with `(x / n) · n` and loop only over `i > x / n`.

## Dry run — m=3, n=3, k=5
lo=1, hi=9: mid=5 → count = min(5,3)+min(2,3)+min(1,3) = 3+2+1 = 6 ≥ 5 → hi=5; mid=3 → 3+1+1 = 5 ≥ 5 → hi=3; mid=2 → 2+1+0 = 3 < 5 → lo=3. Answer **3**.

## Pitfalls
- `m · n` = 9·10⁸ still fits in `int`, but `lo + hi` can reach 1.8·10⁹ and the running count can exceed `int` in variants — use `(lo + hi) >>> 1` and a `long` count.
- Cap each row's count at `n`.

## Follow-ups
- **Kth smallest pair distance** (LC 719), **Kth smallest in sorted matrix** (LC 378) — same template.
- **Nth magical number** (LC 878) — count via inclusion–exclusion `x/a + x/b − x/lcm`.
- **Ugly number III** (LC 1201) — inclusion–exclusion over three divisors.

## 30-second interview script
"I binary search the answer value. The number of entries ≤ x is a sum over rows of min(x / i, n), which is O(m). That count is monotonic, so the smallest x with count ≥ k is the answer — and it's guaranteed to be an actual table entry."
