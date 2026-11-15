# LC 1206 — Design Skiplist (Hard)

**Pattern:** probabilistic multi-level linked list · **Target:** O(log n) expected per op · **Code:** [DesignSkiplist.java](DesignSkiplist.java)

## Problem
Implement a skiplist (no built-in ordered structures) with `search(target)`, `add(num)` and `erase(num)` (removing one occurrence; duplicates allowed), each in expected O(log n).

## Why engineers fear it
Most people have never coded one. It's a randomised structure, so you must explain *why* it's O(log n) on average, and pointer updates across levels are easy to get wrong.

## Idea
A sorted linked list where each node is also promoted to higher "express lanes" with probability ½ per level. Level 0 holds everything, level 1 about half, level 2 about a quarter… Searching starts at the top level and drops down when the next node would overshoot — like binary search on a list.

## Approach 1 — Sorted linked list (baseline)
search/add/erase O(n). Simple; shows what the levels buy you.

## Approach 2 — Sorted array list with binary search
search O(log n), add/erase O(n) shifts. Mention as a comparison point.

## Approach 3 — Skiplist (optimal expected)
```
Node: val, next[] (one forward pointer per level)
head: sentinel with MAX_LEVEL pointers

findPredecessors(x): update[l] = last node at level l with val < x
    cur = head
    for l = top..0:
        while cur.next[l] != null and cur.next[l].val < x: cur = cur.next[l]
        update[l] = cur

search(x): cur = findPredecessors(x)[0].next[0]; return cur != null and cur.val == x
add(x):    lvl = randomLevel(); node = new Node(x, lvl)
           for l < lvl: node.next[l] = update[l].next[l]; update[l].next[l] = node
erase(x):  target = update[0].next[0]; if not x → false
           for each level l where update[l].next[l] == target: update[l].next[l] = target.next[l]
```
`randomLevel()`: start at 1, increment while a coin flip is heads (cap at MAX_LEVEL ≈ 16–32).

### Why O(log n) expected
- Expected height ≈ log₂ n.
- At each level, the expected number of horizontal steps is ≤ 2 (going backwards along the search path, each node is "promoted" with probability ½, so you climb after ~2 steps).
- Total expected work = O(log n). Space: expected 2n pointers.

## Pitfalls
- Erasing a node must unlink it at **every** level it appears on, and only where `update[l].next[l]` is exactly that node (duplicates!).
- Using `<` (not `<=`) in the predecessor search so `update[0].next[0]` is the **first** node with value ≥ x.
- Seeding the RNG differently can make tests flaky — seed it in tests.

## Follow-ups
- **Redis sorted sets** and **LevelDB/RocksDB memtables** use skiplists — good real-world mention.
- Indexable skiplist (store span widths) → k-th element in O(log n).
- Concurrent skiplists (`ConcurrentSkipListMap` in Java) — lock-free friendly, unlike balanced trees.

## 30-second interview script
"A skiplist is a sorted linked list with extra express levels; each node gets a random height with probability ½ per level. To search I start at the highest level and move right while the next value is smaller, dropping a level when it would overshoot, recording the predecessor at each level. Add and erase splice the node in or out at each level using those predecessors. Expected height is log n and expected steps per level are constant, so every operation is O(log n) expected."
