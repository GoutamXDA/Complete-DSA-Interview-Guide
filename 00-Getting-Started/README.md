# 00 — Getting Started (Absolute Zero)

## 1. What is DSA?
- **Data Structure**: a way to organise data so operations (insert, search, delete) are efficient. E.g., array, linked list, hash map, tree, graph.
- **Algorithm**: a finite sequence of steps that solves a problem. E.g., binary search, merge sort, Dijkstra.

Interviewers test: can you pick the right structure, design a correct algorithm, analyse its cost, and write clean code?

## 2. Time & Space Complexity (Big-O)

Big-O describes how runtime/memory **grows** with input size `n`, ignoring constants.

| Complexity | Name | n = 10^5 feasible? | Example |
|---|---|---|---|
| O(1) | constant | ✅ | array index, HashMap get |
| O(log n) | logarithmic | ✅ | binary search |
| O(n) | linear | ✅ | single loop |
| O(n log n) | linearithmic | ✅ | merge sort |
| O(n²) | quadratic | ❌ (10^10) | nested loops |
| O(2^n) | exponential | only n ≤ 20–25 | subsets |
| O(n!) | factorial | only n ≤ 10 | permutations |

**Rule of thumb**: ~10^8 simple operations ≈ 1 second.

| Constraint on n | Target complexity |
|---|---|
| n ≤ 10 | O(n!) |
| n ≤ 20 | O(2^n) |
| n ≤ 500 | O(n³) |
| n ≤ 5,000 | O(n²) |
| n ≤ 10^6 | O(n log n) or O(n) |
| n ≥ 10^9 | O(log n) or O(1) |

### How to compute it
- Sequential blocks → **add**: O(n) + O(n²) = O(n²)
- Nested blocks → **multiply**: loop n × loop m = O(n·m)
- Loop where `i *= 2` or `i /= 2` → O(log n)
- Recursion → recurrence. `T(n) = 2T(n/2) + O(n)` → O(n log n) (Master theorem).
- Space includes the **recursion stack** (depth of recursion).

### Best / Average / Worst
- Big-O = upper bound (worst case usually), Ω = lower bound, Θ = tight bound.
- Quick sort: average O(n log n), worst O(n²).
- **Amortised**: ArrayList `add` is O(1) amortised even though resizing is O(n) occasionally.

## 3. Problem-solving framework (use it in every interview)

1. **Clarify** — inputs, outputs, constraints, duplicates? negatives? empty input?
2. **Examples** — walk through 1 normal + 1 edge case.
3. **Brute force** — say it out loud with complexity. Never skip this.
4. **Optimise** — look for repeated work. Ask: can sorting / hashing / two pointers / binary search / DP / heap help?
5. **Code** — clean names, small helper methods.
6. **Test** — dry run with your examples, then edge cases (empty, one element, all same, large values → overflow).
7. **Analyse** — final time & space.

## 4. Pattern recognition cheat sheet

| If the problem says… | Think about |
|---|---|
| sorted array / "find in log n" | Binary search |
| subarray / substring with condition | Sliding window, prefix sum + hashmap |
| pair/triplet with target sum | Two pointers (sorted) or HashMap |
| top K / Kth largest | Heap |
| all combinations / permutations | Backtracking |
| min/max ways, count ways, overlapping subproblems | Dynamic Programming |
| next greater/smaller element | Monotonic stack |
| shortest path unweighted | BFS |
| shortest path weighted | Dijkstra / Bellman-Ford |
| dependencies / ordering | Topological sort |
| connected components, "are they connected" | DFS/BFS / Union-Find |
| prefix of strings | Trie |
| range sum queries with updates | Fenwick / Segment tree |
| "minimise the maximum" / "maximise the minimum" | Binary search on answer |
| intervals | Sort by start/end + greedy |

## 5. Files in this folder
- [JavaDSACheatSheet.java](JavaDSACheatSheet.java) — every Java collection API you need, runnable.
- [ComplexityExamples.java](ComplexityExamples.java) — code examples of each complexity class.
