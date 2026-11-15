# Complete DSA Interview Guide (Java) — Zero to Advanced

A single, structured repository to prepare **Data Structures & Algorithms** for a job switch — from absolute zero to advanced — with:

- **Notes** for every topic (theory, when to use, templates, pitfalls)
- **Patterns** you can recognise in interviews
- **Problem lists** ordered Easy → Medium → Hard, each with a one-line approach
- **Java solutions** showing **every approach**: Brute Force → Better → Optimal, with time/space complexity and runnable tests
- **Conceptual Q&A** for theory rounds

> Every `.java` file is self-contained (no packages, no dependencies). Run any file with:
> ```bash
> java 02-Arrays/TwoSum.java      # Java 11+ single-file launch
> ```
> Or run the whole suite: `./run-all.sh`

---

## Roadmap

| # | Topic | Level | What you'll learn |
|---|-------|-------|-------------------|
| 00 | [Getting Started](00-Getting-Started) | Zero | Complexity analysis, Java collections cheat sheet, how to approach a problem |
| 01 | [Basic Maths](01-Basic-Maths) | Beginner | Digits, GCD, primes, sieve, modular arithmetic, fast power |
| 02 | [Arrays](02-Arrays) | Beginner | Prefix sums, Kadane, Dutch flag, rotation, matrix tricks |
| 03 | [Strings](03-Strings) | Beginner | Palindromes, anagrams, StringBuilder, parsing |
| 04 | [Hashing](04-Hashing) | Beginner | HashMap/HashSet patterns, frequency counting, prefix-sum + map |
| 05 | [Two Pointers & Sliding Window](05-Two-Pointers-Sliding-Window) | Beginner → Medium | Fixed & variable windows, opposite-end pointers |
| 06 | [Binary Search](06-Binary-Search) | Medium | Lower/upper bound, rotated arrays, binary search on answer |
| 07 | [Sorting](07-Sorting) | Medium | Bubble → Merge/Quick/Heap/Counting, custom comparators |
| 08 | [Recursion & Backtracking](08-Recursion-Backtracking) | Medium | Subsets, permutations, N-Queens, Sudoku |
| 09 | [Linked List](09-Linked-List) | Medium | Reverse, cycle detection, merge, LRU |
| 10 | [Stack & Queue](10-Stack-Queue) | Medium | Monotonic stack, next greater, min-stack, deque |
| 11 | [Binary Trees](11-Binary-Trees) | Medium | DFS/BFS traversals, views, LCA, diameter, serialize |
| 12 | [Binary Search Trees](12-Binary-Search-Trees) | Medium | Insert/delete, validate, kth smallest, iterator |
| 13 | [Heaps / Priority Queue](13-Heaps-Priority-Queue) | Medium | Top-K, merge K lists, median stream |
| 14 | [Graphs](14-Graphs) | Medium → Hard | BFS/DFS, topo sort, Dijkstra, Bellman-Ford, MST, SCC |
| 15 | [Dynamic Programming](15-Dynamic-Programming) | Medium → Hard | 1D/2D, knapsack, LIS, LCS, intervals, bitmask, digit DP |
| 16 | [Greedy](16-Greedy) | Medium | Intervals, jump game, gas station, scheduling |
| 17 | [Tries](17-Tries) | Medium → Hard | Prefix search, word search II, max XOR |
| 18 | [Bit Manipulation](18-Bit-Manipulation) | Medium | XOR tricks, masks, counting bits |
| 19 | [Advanced Data Structures](19-Advanced-Data-Structures) | Hard | DSU, Segment Tree, Fenwick, KMP, Z, Rabin-Karp, LFU |
| 20 | [Conceptual Q&A](20-Conceptual-QA) | All | Theory questions interviewers ask |

## Suggested study plan (12 weeks)

| Week | Topics |
|------|--------|
| 1 | 00, 01, 02 |
| 2 | 03, 04, 05 |
| 3 | 06, 07 |
| 4 | 08 |
| 5 | 09, 10 |
| 6 | 11, 12 |
| 7 | 13, 16 |
| 8–9 | 14 |
| 10–11 | 15 |
| 12 | 17, 18, 19, 20 + mock interviews |

## How each problem is documented

```
/**
 * Problem   : statement in short + link
 * Approach 1: Brute force  — idea, Time, Space
 * Approach 2: Better       — idea, Time, Space
 * Approach 3: Optimal      — idea, Time, Space
 * Pitfalls  : edge cases interviewers look for
 */
```

## How to use this repo

1. Read the topic `README.md` (notes + patterns).
2. Try each problem yourself for 20–30 minutes.
3. Read the solution file: compare your idea against brute → optimal.
4. Re-solve after 3 days and after 1 week (spaced repetition). Tick it in the topic table.

## Progress tracker

- [PROGRESS.md](PROGRESS.md) — tick topics as you finish them.
- **[REMAINING.md](REMAINING.md) — master list of all 159 problems not yet solved in code, by topic, with approach hints. Nothing gets skipped.**

## Contributing / extending

Pick any unticked problem from [REMAINING.md](REMAINING.md), add a `ProblemName.java` following the same template, and run `./run-all.sh`.
