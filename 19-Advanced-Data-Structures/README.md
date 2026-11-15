# 19 — Advanced Data Structures & String Algorithms

## Disjoint Set Union (Union-Find)
- `find` with **path compression** + `union` by **rank/size** → amortised **O(α(n))** ≈ constant.
- Use for: dynamic connectivity, cycle detection in undirected graphs, Kruskal, grouping (accounts merge), "number of islands II".

## Segment Tree
- Range query (sum/min/max/gcd) + point update in **O(log n)**; build O(n); array size 4n.
- **Lazy propagation** for range updates in O(log n).

## Fenwick Tree (Binary Indexed Tree)
- Prefix sums + point updates in **O(log n)** with ~10 lines of code. `i += i & -i` (update), `i -= i & -i` (query).
- Range sum = `prefix(r) − prefix(l−1)`. Counts inversions, "count of smaller numbers after self".

## String matching
| Algorithm | Time | Idea |
|---|---|---|
| Naive | O(n·m) | try every shift |
| **KMP** | O(n+m) | LPS array = longest proper prefix that is also a suffix; never move text pointer back |
| **Z-algorithm** | O(n+m) | Z[i] = longest substring from i matching a prefix; search in `pattern + '$' + text` |
| **Rabin–Karp** | O(n+m) avg | rolling hash; verify on hash match |

## Other designs
- **LFU cache** — key→node map + freq→doubly-linked-list map + `minFreq`, all O(1).
- **Sparse table** — static range min/max in O(1) after O(n log n) preprocessing.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | DSU implementation | Medium | path compression + rank | [DisjointSetUnion](DisjointSetUnion.java) |
| 2 | Redundant connection (LC 684) | Medium | first edge joining same set | [DisjointSetUnion](DisjointSetUnion.java) |
| 3 | Accounts merge (LC 721) | Medium | DSU on emails | [DisjointSetUnion](DisjointSetUnion.java) |
| 4 | Most stones removed (LC 947) | Medium | union rows & cols | [DisjointSetUnion](DisjointSetUnion.java) |
| 5 | Segment tree – range sum + point update (LC 307) | Medium | recursive tree | [SegmentTree](SegmentTree.java) |
| 6 | Segment tree – range min | Medium | same with min | [SegmentTree](SegmentTree.java) |
| 7 | Lazy propagation – range add, range sum | Hard | lazy array | [SegmentTree](SegmentTree.java) |
| 8 | Fenwick tree | Medium | BIT | [FenwickTree](FenwickTree.java) |
| 9 | Count of smaller numbers after self (LC 315) | Hard | BIT over compressed ranks | [FenwickTree](FenwickTree.java) |
| 10 | KMP / strStr (LC 28) | Medium | LPS array | [StringMatching](StringMatching.java) |
| 11 | Z-algorithm | Medium | Z box | [StringMatching](StringMatching.java) |
| 12 | Rabin–Karp | Medium | rolling hash | [StringMatching](StringMatching.java) |
| 13 | Shortest palindrome (LC 214) | Hard | KMP on s#rev(s) | [StringMatching](StringMatching.java) |
| 14 | Repeated substring pattern (LC 459) | Easy | LPS: n % (n − lps) == 0 | [StringMatching](StringMatching.java) |
| 15 | LFU cache (LC 460) | Hard | freq buckets | [LFUCache](LFUCache.java) |
| 16 | Sparse table RMQ | Medium | doubling | [SparseTable](SparseTable.java) |
| 17 | Repeated DNA sequences (LC 187) | Medium | rolling hash / 20-bit encoding | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 18 | Snapshot array (LC 1146) | Medium | per index list of (snap, val) + binary search | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 19 | Range sum query 2D – mutable (LC 308) | Hard | 2D Fenwick tree | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 20 | My calendar III (LC 732) | Hard | sweep line TreeMap / dynamic segment tree | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 21 | The skyline problem (LC 218) | Hard | sweep events + max-heap / TreeMap | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 22 | Longest duplicate substring (LC 1044) | Hard | binary search length + Rabin–Karp | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 23 | Manacher's algorithm (LC 5 in O(n)) (LC 5) | Hard | mirror radii on `#a#b#` transformed string | [AdvancedProblemsII](AdvancedProblemsII.java) |
| 24 | Count inversions with Fenwick tree (GFG) | Medium | compress ranks, BIT from right | [AdvancedProblemsII](AdvancedProblemsII.java) |
