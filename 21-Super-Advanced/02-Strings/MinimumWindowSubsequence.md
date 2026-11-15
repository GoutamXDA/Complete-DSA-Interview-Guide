# LC 727 — Minimum Window Subsequence (Hard)

**Pattern:** forward match + backward shrink (two pointers) or DP on "latest start" · **Target:** O(|s| · |t|) · **Code:** [MinimumWindowSubsequence.java](MinimumWindowSubsequence.java)

## Problem
Find the shortest contiguous substring `W` of `s` such that `t` is a **subsequence** of `W` (order matters, gaps allowed). If several, return the leftmost. Return `""` if none.

`s = "abcdebdde", t = "bde" → "bcde"`

## How it differs from LC 76
In LC 76 order doesn't matter, so counts suffice and a linear window works. Here order matters, so a simple count window is wrong — and the classic sliding window can't decide when to shrink.

## Approach 1 — Brute force
For each start `i`, greedily match `t` forward; the first end `j` where `t` is fully matched gives the shortest window starting at `i` (greedy earliest matching is optimal for a fixed start). **O(n · n)** worst — actually O(n²) with the greedy scan.

## Approach 2 — Forward scan, backward shrink (optimal in practice)
1. From position `i`, scan forward matching `t` character by character until `t` is fully matched at `end`.
2. From `end`, scan **backward** matching `t` from its last character; you stop at `start`, the **latest** start for which `s[start..end]` still contains `t` as a subsequence.
3. Record `[start, end]`, then restart the forward scan at `start + 1`.
**Time** O(n · m) worst case · **Space** O(1).

**Why the backward pass gives the tightest window for this end:** matching `t` backwards greedily picks each character as late as possible, so the resulting start is maximal.

## Approach 3 — DP of latest start
`dp[j]` = the largest start index `st` such that `t[0..j]` is a subsequence of `s[st..i]` (for the current `i`), or −1.
For each `i`, update `j` from high to low: if `s[i] == t[j]`, `dp[j] = (j == 0 ? i : dp[j−1])`.
Whenever `dp[m−1] ≥ 0`, window `[dp[m−1], i]` is a candidate. **O(n · m)** time, **O(m)** space. Deterministic bound; nice to present when asked for a proof-friendly method.

## Dry run (Approach 2) — s = "abcdebdde", t = "bde"
- Forward from 0: b(1) d(3) e(4) → end = 4. Backward from 4: e(4) d(3) b(1) → start = 1 → "bcde" (len 4).
- Forward from 2: d.. no b until 5: b(5) d(6) e(8) → end 8; backward: e(8) d(7) b(5) → "bdde" (len 4) — not shorter.
Answer **"bcde"**.

## Pitfalls
- Using a character-count window (LC 76 logic) — ignores order.
- Ties: keep the first (leftmost) window → update only on strictly shorter.
- Restart at `start + 1`, not `end + 1` (windows can overlap).

## Follow-ups
- **Number of matching subsequences** (LC 792) — bucket by next needed char.
- **Is subsequence** with many queries (LC 392 follow-up) — next-occurrence table + binary search.
- **Distinct subsequences** (LC 115) — counting version (see DP folder).

## 30-second interview script
"Because order matters, counts don't work. I scan forward to find where t first completes, then scan backward from there to find the latest start that still contains t — that's the tightest window ending there. Record it and restart just after that start. O(n·m) time, O(1) space; a dp of latest-start per prefix of t gives the same bound."
