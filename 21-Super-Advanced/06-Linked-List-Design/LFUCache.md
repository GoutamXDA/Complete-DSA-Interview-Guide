# LC 460 — LFU Cache (Hard)

**Pattern:** hash maps + per-frequency doubly linked lists + `minFreq` · **Target:** O(1) per operation · **Code:** [LFUCache.java](LFUCache.java)

## Problem
Design a cache with capacity `c` supporting `get(key)` and `put(key, value)` in **O(1)**. When full, evict the **least frequently used** key; among ties, evict the **least recently used** one. Every `get`/`put` on a key counts as a use.

## Why engineers fear it
LRU (LC 146) is one map + one list. LFU needs a *second* dimension (frequency) while keeping recency order inside each frequency — and everything must stay O(1).

## Approach 1 — Map + scan on eviction
`key → (value, freq, lastUsedTime)`; on eviction scan all keys for min `(freq, time)`. get/put O(1), **eviction O(c)**.

## Approach 2 — Map + TreeSet ordered by (freq, time)
Every use removes and re-inserts the key in the TreeSet. **O(log c)** per operation. Good stepping stone; interviewers then ask for O(1).

## Approach 3 — Frequency buckets (optimal, O(1))
- `nodes: key → Node(key, value, freq)`
- `buckets: freq → doubly linked list of nodes` (head = most recent, tail = least recent)
- `minFreq`: smallest frequency currently present.

**touch(node)** (on every get/put of an existing key):
1. Remove `node` from `buckets[f]`.
2. If that bucket is now empty **and** `f == minFreq`, then `minFreq = f + 1`.
3. `node.freq++`, add to the front of `buckets[f + 1]`.

**put(new key)** when full: evict `buckets[minFreq].tail` (LFU, and LRU within it); then insert the new node with `freq = 1` and set **`minFreq = 1`**.

Every step is a hash lookup or a list splice → **O(1)**.

### Why `minFreq` never needs a search
- It only increases through `touch`, and only by 1 — exactly when its bucket empties.
- It resets to 1 on every new insertion.
So it's always correct without scanning.

## Java shortcut
`LinkedHashSet<Integer>` per frequency gives O(1) insertion-ordered add/remove and "first element" = least recent. The Java file shows both this and the explicit DLL version.

## Pitfalls
- Capacity 0 → `put` does nothing.
- Updating an existing key's value **also** counts as a use (`touch`).
- Evict **before** inserting the new key, and only when the key is new.

## Follow-ups
- **LRU cache** (LC 146) — single list.
- **All O(1) data structure** (LC 432) — the same bucket idea for max and min keys (next file).
- **LFU with TTL / weighted sizes** — bucket structure + a min-heap of expiries.

## 30-second interview script
"Each key maps to a node holding its value and frequency. Nodes live in a doubly linked list for their frequency, most recent at the head. I also track the minimum frequency. On access I move the node from bucket f to f+1 and bump minFreq if bucket f emptied and was the minimum. On insert into a full cache I evict the tail of the minFreq bucket, then add the new key with frequency 1 and reset minFreq to 1. Everything is O(1)."
