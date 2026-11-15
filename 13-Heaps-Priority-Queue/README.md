# 13 — Heaps / Priority Queue

## Notes
- **Binary heap** = complete binary tree stored in an array. Min-heap: parent ≤ children.
  - children of `i`: `2i+1`, `2i+2`; parent: `(i-1)/2`.
- `offer`/`poll`: O(log n) (sift up / sift down). `peek`: O(1). **Build heap from array: O(n)**.
- Java: `new PriorityQueue<>()` = min-heap; `new PriorityQueue<>(Collections.reverseOrder())` = max-heap.
- `pq.remove(obj)` is **O(n)** — use lazy deletion instead.

## Patterns
| Pattern | Trick |
|---|---|
| **Top K largest** | min-heap of size K (pop when size > K) → O(n log K) |
| **Top K smallest / K closest** | max-heap of size K |
| **K-way merge** | heap of (value, listIndex, elementIndex) |
| **Two heaps** | max-heap for lower half, min-heap for upper half → running median |
| **Scheduling** | sort by start + heap of end times (meeting rooms) |
| **Greedy with heap** | always take best available (IPO, task scheduler) |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Implement min-heap | Medium | array + sift up/down | [MinHeap](MinHeap.java) |
| 2 | Kth largest element (LC 215) | Medium | size-K min-heap | [HeapProblems](HeapProblems.java) |
| 3 | Kth largest in a stream (LC 703) | Easy | size-K min-heap | [HeapProblems](HeapProblems.java) |
| 4 | Top K frequent elements (LC 347) | Medium | heap / bucket sort O(n) | [HeapProblems](HeapProblems.java) |
| 5 | K closest points to origin (LC 973) | Medium | size-K max-heap | [HeapProblems](HeapProblems.java) |
| 6 | Last stone weight (LC 1046) | Easy | max-heap simulation | [HeapProblems](HeapProblems.java) |
| 7 | Merge K sorted lists (LC 23) | Hard | heap of heads | [HeapProblems](HeapProblems.java) |
| 8 | Find median from data stream (LC 295) | Hard | two heaps | [HeapProblems](HeapProblems.java) |
| 9 | Task scheduler (LC 621) | Medium | formula / heap | [HeapProblems](HeapProblems.java) |
| 10 | Meeting rooms II (LC 253) | Medium | heap of end times | [HeapProblems](HeapProblems.java) |
| 11 | Reorganize string (LC 767) | Medium | max-heap, alternate top two | [HeapProblems](HeapProblems.java) |
| 12 | Sliding window median (LC 480) | Hard | two heaps + lazy delete / TreeMap | [HeapProblemsII](HeapProblemsII.java) |
| 13 | IPO (LC 502) | Hard | sort by capital + max-heap profit | [HeapProblemsII](HeapProblemsII.java) |
| 14 | Smallest range covering K lists (LC 632) | Hard | heap + track max | [HeapProblemsII](HeapProblemsII.java) |
| 15 | Hand of straights (LC 846) | Medium | TreeMap counts | [HeapProblemsII](HeapProblemsII.java) |
| 16 | Minimum cost to connect ropes (LC 1167) | Medium | min-heap, always join two smallest | [HeapProblemsII](HeapProblemsII.java) |
| 17 | Find K pairs with smallest sums (LC 373) | Medium | heap seeded with (i, 0) | [HeapProblemsII](HeapProblemsII.java) |
| 18 | Maximum sum combinations (GFG) | Medium | sort desc + heap + visited set | [HeapProblemsII](HeapProblemsII.java) |
| 19 | Design Twitter (LC 355) | Medium | per-user tweet lists + k-way merge heap | [HeapProblemsII](HeapProblemsII.java) |
| 20 | Minimum cost to hire K workers (LC 857) | Hard | sort by wage/quality ratio + max-heap of quality | [HeapProblemsII](HeapProblemsII.java) |
