# LC 432 — All O`one Data Structure (Hard)

**Pattern:** doubly linked list of **count buckets** (sorted by count) + key → bucket map · **Target:** O(1) per op · **Code:** [AllOOneDataStructure.java](AllOOneDataStructure.java)

## Problem
Store string keys with counts. All in O(1):
- `inc(key)`: count + 1 (insert with count 1 if new)
- `dec(key)`: count − 1 (remove at 0; key is guaranteed to exist)
- `getMaxKey()` / `getMinKey()`: any key with the max / min count, or `""` if empty.

## Why engineers fear it
Max **and** min in O(1) with increments **and** decrements rules out heaps (O(log n)) and TreeMaps. You need a structure where counts move by ±1 to an adjacent position.

## Approach 1 — HashMap + scan for max/min
inc/dec O(1), **getMax/getMin O(n)**.

## Approach 2 — HashMap + TreeMap<count, Set<keys>>
Move the key between sets; `firstKey`/`lastKey` for min/max. **O(log n)** per op.

## Approach 3 — Bucket list (optimal O(1))
Doubly linked list of buckets in **increasing count order**, each bucket = `(count, Set<keys>)`, with sentinel `head` and `tail`. Map `key → bucket`.
- **inc(key)** from bucket `b` (or from `head` if new): the destination is `b.next` if `b.next.count == b.count + 1`, otherwise **insert a new bucket right after `b`**. Move the key; delete `b` if it became empty.
- **dec(key)** symmetric toward `b.prev` (count − 1); if the count becomes 0 just drop the key.
- **getMinKey** = any key of `head.next`; **getMaxKey** = any key of `tail.prev`.

Because counts change by exactly 1, the destination bucket is always **adjacent** — no searching. Every operation is O(1) (set add/remove + pointer splice).

## Invariants to state
1. Buckets are strictly increasing in count from head to tail.
2. No bucket is empty.
3. `map[key]` is the bucket containing key.

## Pitfalls
- Delete the old bucket **after** moving the key (and only if empty), otherwise you lose the anchor for inserting the neighbour.
- New key: insert relative to `head` (count 0 sentinel).
- dec to 0: remove the key from the map.

## Follow-ups
- **LFU cache** (LC 460) — the same frequency-bucket idea plus recency.
- **Maximum frequency stack** (LC 895) — stacks per frequency.
- Support `getKeysWithCount(c)` in O(1) — add `count → bucket` map.

## 30-second interview script
"I keep a doubly linked list of buckets sorted by count, each holding the keys with that count, plus a map from key to its bucket. Since inc and dec change a count by exactly one, the key always moves to an adjacent bucket — which I create on the spot if it doesn't exist — and I delete buckets that become empty. Min is the first bucket, max is the last. All O(1)."
