# LC 1707 — Maximum XOR With an Element From Array (Hard)

**Pattern:** binary trie + **offline queries** sorted by limit · **Target:** O((n + q) · 30 + sorting) · **Code:** [MaximumXorWithElementFromArray.java](MaximumXorWithElementFromArray.java)

## Problem
For each query `(x, m)`, return `max(nums[j] XOR x)` over all `nums[j] ≤ m`, or −1 if no such element.

`nums=[0,1,2,3,4], queries=[[3,1],[1,3],[5,6]] → [3,3,7]`

## Why engineers fear it
"Max XOR" needs a binary trie, but the `≤ m` constraint changes per query. Answering online needs extra trie bookkeeping; the trick is to answer queries **offline** in a convenient order.

## Approach 1 — Brute force
For each query, scan all eligible numbers. **O(n · q)**.

## Approach 2 — Offline: sort both, insert progressively (optimal, simplest)
Sort `nums`; sort query indices by `m`. Sweep queries in increasing `m`, inserting every `nums[j] ≤ m` into a binary trie before answering. Query: walk bits 29 → 0, preferring the **opposite** bit of `x` at each level.
**O(n log n + q log q + (n + q) · 30)**.

## Approach 3 — Online: trie with subtree minimum
Insert everything once; store in each trie node the **minimum value** in its subtree. During the query, only step into a child whose `min ≤ m`. Answers queries in any order; same complexity. Useful when queries arrive as a stream.

## Why greedy bit choice is optimal
A higher bit dominates all lower bits combined (2^b > 2^b − 1), so making the current bit 1 whenever possible maximises the XOR.

## Pitfalls
- Values up to 10⁹ → 30 bits (bit 29 down to 0).
- Queries with no eligible number → −1 (offline: trie still empty; online: root min > m).
- Restore the original query order when writing answers.

## Follow-ups
- **Maximum XOR of two numbers** (LC 421) — the unconstrained version.
- **Maximum genetic difference query** (LC 1938) — trie with insert/delete during a tree DFS.
- **Count pairs with XOR in a range** (LC 1803) — trie with counts.

## 30-second interview script
"Max XOR is a binary-trie greedy: walk from the top bit and go to the opposite bit when it exists. The limit m varies per query, so I answer offline: sort numbers and queries by m, and before each query insert all numbers ≤ m into the trie. Each insert and query is 30 steps. If queries must be online, I store the minimum value in each trie node and only descend into children whose minimum is ≤ m."
