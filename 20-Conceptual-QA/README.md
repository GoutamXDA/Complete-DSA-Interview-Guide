# 20 — Conceptual DSA Interview Questions & Answers

Short, interview-ready answers. Read aloud and practise explaining them in under a minute each.

## A. Complexity & fundamentals

**1. What is Big-O, Big-Ω, Big-Θ?**
Big-O is an upper bound on growth, Ω a lower bound, Θ a tight bound (both). In interviews "Big-O" usually means the worst-case tight bound.

**2. What is amortised complexity? Example?**
Average cost per operation over a worst-case sequence. `ArrayList.add` doubles capacity occasionally (O(n)), but n adds cost O(n) total → O(1) amortised.

**3. Why is comparison sorting Ω(n log n)?**
A comparison sort is a decision tree with n! leaves; its height is at least log₂(n!) = Θ(n log n).

**4. Time vs space trade-off example?**
Two Sum: O(n²) time / O(1) space with nested loops vs O(n) time / O(n) space with a HashMap. Memoisation trades memory for time.

**5. What is the Master theorem?**
For `T(n) = aT(n/b) + f(n)`: compare `f(n)` with `n^(log_b a)`. Equal → `n^(log_b a) log n` (merge sort: n log n); `f` smaller → `n^(log_b a)`; `f` larger → `f(n)`.

**6. Stack vs heap memory in Java?**
Stack holds method frames (locals, references), is per-thread and fast; heap holds objects, shared, garbage-collected. Deep recursion → `StackOverflowError`; too many objects → `OutOfMemoryError`.

## B. Arrays, strings, hashing

**7. Array vs linked list?**
Array: O(1) index access, cache-friendly, O(n) middle insert. Linked list: O(1) insert/delete given the node, O(n) access, extra pointer memory, poor locality.

**8. Why is String immutable in Java and why does that matter for DSA?**
Security, caching hash codes, string pool, thread safety. `s += x` in a loop copies each time → O(n²); use `StringBuilder`.

**9. How does HashMap work internally?**
Array of buckets. `hash(key)` is mixed (`h ^ h>>>16`), index = `hash & (n-1)`. Collisions chain in a linked list, converted to a red-black tree when a bucket exceeds 8 entries (and table ≥ 64). Resizes ×2 at load factor 0.75, rehashing entries. Average O(1), worst O(log n) (Java 8+).

**10. What happens if you override `equals` but not `hashCode`?**
Equal objects may land in different buckets, so `contains`/`get` fail. Contract: equal objects must have equal hash codes.

**11. HashMap vs TreeMap vs LinkedHashMap?**
HashMap: O(1) avg, no order. TreeMap: red-black tree, O(log n), sorted keys, `floorKey`/`ceilingKey`. LinkedHashMap: insertion or access order; basis of an LRU cache.

**12. HashMap vs Hashtable vs ConcurrentHashMap?**
Hashtable is synchronized (legacy, slow), no null keys. HashMap is unsynchronized, allows one null key. ConcurrentHashMap uses fine-grained locking/CAS for concurrent access.

## C. Stacks, queues, heaps

**13. Why prefer `ArrayDeque` over `Stack` and `LinkedList`?**
`Stack` extends `Vector` (synchronized, legacy). `ArrayDeque` is a resizable circular array: faster, less memory than `LinkedList`, no per-node objects.

**14. What is a monotonic stack and when do you use it?**
A stack kept in increasing/decreasing order. Each element is pushed/popped once → O(n). Use for next/previous greater/smaller, histogram area, stock span.

**15. How is a priority queue implemented? Complexities?**
Binary heap in an array. Insert/poll O(log n) via sift-up/down, peek O(1), build-heap O(n). Java `PriorityQueue` is a min-heap.

**16. Why is build-heap O(n), not O(n log n)?**
Most nodes are near the bottom: n/2 leaves need 0 work, n/4 need 1 level, … Sum of `n/2^(h+1) · h` = O(n).

**17. Heap vs BST for top-K?**
Heap of size K gives O(n log K) and is simpler. BST/TreeMap supports deletes of arbitrary elements and ordered iteration in O(log n).

## D. Trees

**18. Difference: binary tree, BST, balanced BST, complete tree, heap?**
Binary tree: ≤ 2 children. BST: ordered left < node < right. Balanced BST (AVL/Red-Black): height O(log n). Complete tree: all levels full except the last, filled left-to-right. Heap: complete tree with heap order.

**19. AVL vs Red-Black tree?**
Both O(log n). AVL is more strictly balanced (faster lookups, more rotations on insert/delete). Red-Black allows up to 2× height difference, fewer rotations → used in `TreeMap`, and in `HashMap` bucket trees.

**20. Why does inorder traversal of a BST give sorted output?**
By the BST property, every key in the left subtree is smaller and every key in the right is larger; visiting left → node → right enumerates keys in order.

**21. DFS vs BFS on trees — when to use which?**
BFS: level-wise questions (level order, min depth, right view), shortest path in unweighted graphs; space O(width). DFS: path questions, subtree aggregates; space O(height).

**22. How do you serialize a tree uniquely?**
Preorder (or level order) with null markers. Without null markers you need two traversals (inorder + pre/post) and unique values.

**23. What is Morris traversal?**
Inorder in O(1) extra space by temporarily threading each node's inorder predecessor's right pointer back to the node, then removing the thread.

## E. Graphs

**24. Adjacency list vs matrix?**
List: O(V+E) space, good for sparse graphs. Matrix: O(V²) space, O(1) edge lookup, good for dense graphs or Floyd–Warshall.

**25. When does BFS give shortest paths?**
When all edges have equal weight. For 0/1 weights use 0-1 BFS with a deque; for non-negative weights use Dijkstra.

**26. Why does Dijkstra fail with negative edges?**
It finalises a node when popped, assuming no later path can be shorter. A negative edge discovered later can make it shorter. Use Bellman–Ford.

**27. How does Bellman–Ford detect a negative cycle?**
After V−1 rounds all shortest paths are final. If a V-th round still relaxes an edge, a negative cycle is reachable.

**28. Topological sort — what and when?**
A linear order where every edge u→v has u before v; exists only for DAGs. Use for build order, course schedule, task scheduling. Kahn (BFS on indegree 0) or DFS post-order reversed.

**29. Cycle detection: directed vs undirected?**
Undirected: a visited neighbour that isn't the parent, or DSU union of already-connected nodes. Directed: a back edge to a node on the current DFS path (grey colour), or Kahn's order having fewer than V nodes.

**30. Prim vs Kruskal?**
Both produce an MST. Kruskal sorts edges and uses DSU — good for sparse graphs/edge lists. Prim grows from a vertex with a heap — good for dense graphs/adjacency lists (O(V²) variant without heap).

**31. What is Union-Find and its complexity?**
Disjoint-set forest with `find` and `union`. With path compression and union by rank/size, operations are amortised O(α(n)) — effectively constant.

**32. What are SCCs, bridges and articulation points?**
SCC: maximal set where every node reaches every other (Kosaraju/Tarjan). Bridge: edge whose removal increases components. Articulation point: vertex whose removal does so. Bridges/APs found with Tarjan low-link in O(V+E).

## F. Algorithms paradigms

**33. Recursion vs iteration?**
Recursion is clearer for trees/backtracking but uses O(depth) stack and has call overhead; iteration avoids stack overflow. Any recursion can be converted with an explicit stack.

**34. Backtracking vs brute force?**
Backtracking is brute force with pruning: build candidates incrementally and abandon a branch as soon as it can't lead to a valid solution.

**35. Greedy vs DP?**
Greedy commits to the locally best choice (needs a proof such as exchange argument). DP explores all choices via subproblems and is required when greedy fails (0/1 knapsack, arbitrary coin change).

**36. Memoisation vs tabulation?**
Memoisation: top-down recursion with cache; computes only needed states; recursion overhead/stack risk. Tabulation: bottom-up loops; no recursion, easy space optimisation; computes all states.

**37. How do you identify a DP problem?**
Choices at each step + "min/max/count/possible" + overlapping subproblems. Define the state in words, write the recurrence, then the base cases.

**38. Divide and conquer vs DP?**
Both split into subproblems. D&C subproblems are independent (merge sort); DP subproblems overlap and are cached.

**39. Why is quicksort often faster than merge sort in practice?**
In-place, cache-friendly partitioning, small constants. Worst case O(n²) is avoided with random pivots. Merge sort is stable and guaranteed O(n log n), preferred for linked lists and external sorting.

**40. What is a stable sort? Why does Java use different sorts for primitives and objects?**
Stable keeps equal elements' order. For primitives stability is irrelevant, so Java uses fast dual-pivot quicksort; for objects it uses TimSort (stable) because equal objects can be distinguishable.

**41. What is binary search on the answer?**
When feasibility is monotonic in the answer (if X works, X+1 works), binary search the smallest/largest feasible X, checking each candidate in O(n).

**42. Sliding window vs prefix sum + hash map?**
Sliding window needs monotonic validity (e.g., non-negative numbers). With negative numbers, use prefix sums + HashMap of earlier prefix values.

## G. Strings

**43. How does KMP avoid re-scanning text?**
The LPS array tells how much of the pattern is still matched after a mismatch, so the text pointer never moves backwards → O(n+m).

**44. Rabin–Karp drawbacks?**
Hash collisions: average O(n+m) but worst O(nm) if many spurious hits. Mitigate with a large prime modulus, double hashing, and verification.

**45. When is a trie better than a hash set?**
Prefix queries (autocomplete, startsWith), lexicographic iteration, and word-search pruning. Costs more memory.

## H. Design-style DSA

**46. Design LRU cache.**
HashMap key→node + doubly linked list ordered by recency. `get` moves node to front; `put` inserts at front and evicts the tail. All O(1).

**47. Design LFU cache.**
key→(value, freq), freq→LinkedHashSet of keys, track `minFreq`. Access moves key to freq+1; evict first key in `minFreq` bucket. All O(1).

**48. Design a data structure with insert, delete, getRandom in O(1) (LC 380).**
ArrayList of values + HashMap value→index. Delete by swapping with the last element and removing the tail.

**49. Find the median of a stream.**
Max-heap for the lower half, min-heap for the upper half, keep sizes balanced (differ by ≤ 1). Add O(log n), median O(1).

**50. Implement a rate limiter / hit counter (LC 362).**
Queue of timestamps (drop older than window) or circular buckets of size window for O(1) memory.

---

### How to answer any conceptual question (STAR for DSA)
1. **Define** the concept in one sentence.
2. **Explain** how it works (mechanism).
3. **Complexity** – time and space.
4. **When to use / not use** – with one example problem.
