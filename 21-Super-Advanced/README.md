# 21 — Super-Advanced DSA: the 75 problems engineers fear most

The hardest, most-avoided interview problems — 5 per topic across 15 topics — each with:

- **`ProblemName.md`** — problem in plain words, *why it's hard*, every approach from brute force to optimal with complexity, the key insight and a correctness argument, a dry run, pitfalls, follow-ups, and a **30-second interview script**.
- **`ProblemName.java`** — every approach implemented, plus tests that compare the optimal versions against a brute-force oracle on thousands of random inputs (`./run-all.sh` from the repo root runs them all).

Famous hards that also appear in the main topic folders (Median of Two Sorted Arrays, Trapping Rain Water, Burst Balloons, …) are re-done here in the deeper format.

## How to use this folder

1. Finish topics 00–20 first; these problems combine several of those techniques.
2. For each problem: read only the **Problem** and **Why it's hard** sections, then attempt it for 45 minutes.
3. Read the approaches one at a time — stop as soon as you get a new idea and try again.
4. Re-solve from scratch after 3 days, then after 2 weeks. Practise the 30-second script out loud.

## The meta-techniques behind most of these problems

| Technique | Problems here that use it |
|---|---|
| Array as its own hash table | First Missing Positive |
| Prefix sums + monotonic deque | Shortest Subarray ≥ K, Max Value of Equation, Sliding Window Maximum |
| "Exactly K" = atMost(K) − atMost(K−1) | Subarrays with K Different Integers |
| Binary search on the answer + counting | Split Array, K-th Pair Distance, Multiplication Table, Gas Stations |
| Contribution technique (prev/next smaller) | Largest Rectangle, Maximal Rectangle, Visible People |
| Post-order "return one thing, record another" | Max Path Sum, Max-Sum BST, Binary Tree Cameras |
| Two heaps / lazy deletion | Median Stream, Sliding Window Median |
| Minimax paths (Dijkstra on max, DSU by weight) | Swim in Rising Water, Trapping Rain Water II |
| State-space BFS (node, mask) / 0-1 BFS | Shortest Path Visiting All Nodes, Minimum Cost Valid Path |
| Low-link DFS | Critical Connections |
| Euler path (Hierholzer) | Reconstruct Itinerary |
| Interval DP — choose the **last** split | Burst Balloons, Strange Printer, Scramble String |
| Inverting the DP question | Super Egg Drop |
| Two simultaneous walkers | Cherry Pickup |
| Exchange-argument greedy with a heap | Refueling Stops, Course Schedule III, Hire K Workers |
| Coordinate compression + sweep + segment tree | Rectangle Area II, Skyline |
| Offline queries | Maximum XOR With an Element |

## All 75 problems

### 01 · Arrays Math

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 1 | **First Missing Positive** (LC 41) | index-as-hash, cyclic sort | [notes](01-Arrays-Math/FirstMissingPositive.md) | [java](01-Arrays-Math/FirstMissingPositive.java) |
| 2 | **Max Points on a Line** (LC 149) | geometry + hashing with exact (reduced-fraction) slopes | [notes](01-Arrays-Math/MaxPointsOnALine.md) | [java](01-Arrays-Math/MaxPointsOnALine.java) |
| 3 | **Maximum Sum of 3 Non-Overlapping Subarrays** (LC 689) | window sums + best-left / best-right precomputation (or k-segment DP) | [notes](01-Arrays-Math/MaxSumOfThreeNonOverlappingSubarrays.md) | [java](01-Arrays-Math/MaxSumOfThreeNonOverlappingSubarrays.java) |
| 4 | **Minimum Number of K Consecutive Bit Flips** (LC 995) | greedy + difference array / sliding "flip parity" | [notes](01-Arrays-Math/MinKBitFlips.md) | [java](01-Arrays-Math/MinKBitFlips.java) |
| 5 | **Trapping Rain Water** (LC 42) | prefix maxima → two pointers / monotonic stack | [notes](01-Arrays-Math/TrappingRainWater.md) | [java](01-Arrays-Math/TrappingRainWater.java) |

### 02 · Strings

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 6 | **Minimum Window Subsequence** (LC 727) | forward match + backward shrink (two pointers) or DP on "latest start" | [notes](02-Strings/MinimumWindowSubsequence.md) | [java](02-Strings/MinimumWindowSubsequence.java) |
| 7 | **Minimum Window Substring** (LC 76) | variable sliding window with a "missing" counter | [notes](02-Strings/MinimumWindowSubstring.md) | [java](02-Strings/MinimumWindowSubstring.java) |
| 8 | **Shortest Palindrome** (LC 214) | longest palindromic **prefix** via KMP failure function (or Z / rolling hash) | [notes](02-Strings/ShortestPalindrome.md) | [java](02-Strings/ShortestPalindrome.java) |
| 9 | **Substring with Concatenation of All Words** (LC 30) | sliding window over **word-sized steps**, run once per offset | [notes](02-Strings/SubstringWithConcatenationOfAllWords.md) | [java](02-Strings/SubstringWithConcatenationOfAllWords.java) |
| 10 | **Text Justification** (LC 68) | greedy line packing + careful space distribution (pure implementation) | [notes](02-Strings/TextJustification.md) | [java](02-Strings/TextJustification.java) |

### 03 · Sliding Window Two Pointers

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 11 | **Max Value of Equation** (LC 1499) | algebraic split + monotonic deque over a constrained window | [notes](03-Sliding-Window-Two-Pointers/MaxValueOfEquation.md) | [java](03-Sliding-Window-Two-Pointers/MaxValueOfEquation.java) |
| 12 | **Shortest Subarray with Sum at Least K** (LC 862) | prefix sums + monotonic increasing deque | [notes](03-Sliding-Window-Two-Pointers/ShortestSubarrayWithSumAtLeastK.md) | [java](03-Sliding-Window-Two-Pointers/ShortestSubarrayWithSumAtLeastK.java) |
| 13 | **Sliding Window Maximum** (LC 239) | monotonic deque | [notes](03-Sliding-Window-Two-Pointers/SlidingWindowMaximum.md) | [java](03-Sliding-Window-Two-Pointers/SlidingWindowMaximum.java) |
| 14 | **Sliding Window Median** (LC 480) | two heaps with lazy deletion / two ordered multisets | [notes](03-Sliding-Window-Two-Pointers/SlidingWindowMedian.md) | [java](03-Sliding-Window-Two-Pointers/SlidingWindowMedian.java) |
| 15 | **Subarrays with K Different Integers** (LC 992) | "exactly K" = atMost(K) − atMost(K−1), or a three-pointer window | [notes](03-Sliding-Window-Two-Pointers/SubarraysWithKDistinct.md) | [java](03-Sliding-Window-Two-Pointers/SubarraysWithKDistinct.java) |

### 04 · Binary Search

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 16 | **Kth Smallest Number in Multiplication Table** (LC 668) | binary search on the value + O(m) counting by rows | [notes](04-Binary-Search/KthSmallestInMultiplicationTable.md) | [java](04-Binary-Search/KthSmallestInMultiplicationTable.java) |
| 17 | **Find K-th Smallest Pair Distance** (LC 719) | binary search on the value + two-pointer counting | [notes](04-Binary-Search/KthSmallestPairDistance.md) | [java](04-Binary-Search/KthSmallestPairDistance.java) |
| 18 | **Median of Two Sorted Arrays** (LC 4) | binary search on a **partition**, not on values | [notes](04-Binary-Search/MedianOfTwoSortedArrays.md) | [java](04-Binary-Search/MedianOfTwoSortedArrays.java) |
| 19 | **Minimize Max Distance to Gas Station** (LC 774) | binary search on a **real-valued** answer (or greedy heap) | [notes](04-Binary-Search/MinimizeMaxDistanceToGasStation.md) | [java](04-Binary-Search/MinimizeMaxDistanceToGasStation.java) |
| 20 | **Split Array Largest Sum** (LC 410) | binary search on the answer + greedy feasibility (also: partition DP) | [notes](04-Binary-Search/SplitArrayLargestSum.md) | [java](04-Binary-Search/SplitArrayLargestSum.java) |

### 05 · Backtracking

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 21 | **Expression Add Operators** (LC 282) | backtracking that evaluates on the fly, carrying the last operand for `*` precedence | [notes](05-Backtracking/ExpressionAddOperators.md) | [java](05-Backtracking/ExpressionAddOperators.java) |
| 22 | **Find Minimum Time to Finish All Jobs** (LC 1723) | NP-hard partitioning → pruned backtracking, binary search + feasibility, or subset DP | [notes](05-Backtracking/MinimumTimeToFinishAllJobs.md) | [java](05-Backtracking/MinimumTimeToFinishAllJobs.java) |
| 23 | **N-Queens** (LC 51 / 52) | constraint backtracking with O(1) conflict checks → bitmasks | [notes](05-Backtracking/NQueens.md) | [java](05-Backtracking/NQueens.java) |
| 24 | **Remove Invalid Parentheses** (LC 301) | BFS by number of removals, or DFS with pre-counted removals + duplicate pruning | [notes](05-Backtracking/RemoveInvalidParentheses.md) | [java](05-Backtracking/RemoveInvalidParentheses.java) |
| 25 | **Sudoku Solver** (LC 37) | constraint backtracking + bitmasks + MRV heuristic | [notes](05-Backtracking/SudokuSolver.md) | [java](05-Backtracking/SudokuSolver.java) |

### 06 · Linked List Design

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 26 | **All O`one Data Structure** (LC 432) | doubly linked list of **count buckets** (sorted by count) + key → bucket map | [notes](06-Linked-List-Design/AllOOneDataStructure.md) | [java](06-Linked-List-Design/AllOOneDataStructure.java) |
| 27 | **Design Skiplist** (LC 1206) | probabilistic multi-level linked list | [notes](06-Linked-List-Design/DesignSkiplist.md) | [java](06-Linked-List-Design/DesignSkiplist.java) |
| 28 | **LFU Cache** (LC 460) | hash maps + per-frequency doubly linked lists + `minFreq` | [notes](06-Linked-List-Design/LFUCache.md) | [java](06-Linked-List-Design/LFUCache.java) |
| 29 | **Merge k Sorted Lists** (LC 23) | k-way merge with a min-heap, or divide & conquer pairing | [notes](06-Linked-List-Design/MergeKSortedLists.md) | [java](06-Linked-List-Design/MergeKSortedLists.java) |
| 30 | **Reverse Nodes in k-Group** (LC 25) | in-place pointer surgery with a dummy head and group boundaries | [notes](06-Linked-List-Design/ReverseNodesInKGroup.md) | [java](06-Linked-List-Design/ReverseNodesInKGroup.java) |

### 07 · Stack Monotonic

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 31 | **Basic Calculator III** (LC 772 / 224 / 227) | expression parsing — recursive descent, operator-precedence stacks, or recursion on parentheses | [notes](07-Stack-Monotonic/BasicCalculatorIII.md) | [java](07-Stack-Monotonic/BasicCalculatorIII.java) |
| 32 | **Largest Rectangle in Histogram** (LC 84) | monotonic increasing stack → previous/next smaller element | [notes](07-Stack-Monotonic/LargestRectangleInHistogram.md) | [java](07-Stack-Monotonic/LargestRectangleInHistogram.java) |
| 33 | **Longest Valid Parentheses** (LC 32) | stack of indices with a "base" sentinel, DP, or two-direction counters | [notes](07-Stack-Monotonic/LongestValidParentheses.md) | [java](07-Stack-Monotonic/LongestValidParentheses.java) |
| 34 | **Maximal Rectangle** (LC 85) | reduce each row to a histogram, apply LC 84 | [notes](07-Stack-Monotonic/MaximalRectangle.md) | [java](07-Stack-Monotonic/MaximalRectangle.java) |
| 35 | **Number of Visible People in a Queue** (LC 1944) | monotonic decreasing stack scanned from the right, counting pops | [notes](07-Stack-Monotonic/NumberOfVisiblePeopleInQueue.md) | [java](07-Stack-Monotonic/NumberOfVisiblePeopleInQueue.java) |

### 08 · Trees

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 36 | **Binary Tree Cameras** (LC 968) | tree DP with states, or the equivalent greedy post-order | [notes](08-Trees/BinaryTreeCameras.md) | [java](08-Trees/BinaryTreeCameras.java) |
| 37 | **Binary Tree Maximum Path Sum** (LC 124) | post-order DFS that returns one thing (a downward "gain") and records another (a path through the node) | [notes](08-Trees/BinaryTreeMaximumPathSum.md) | [java](08-Trees/BinaryTreeMaximumPathSum.java) |
| 38 | **Maximum Sum BST in Binary Tree** (LC 1373) | post-order returning a summary tuple `(isBST, min, max, sum)` | [notes](08-Trees/MaximumSumBSTInBinaryTree.md) | [java](08-Trees/MaximumSumBSTInBinaryTree.java) |
| 39 | **Recover Binary Search Tree** (LC 99) | inorder violations; Morris traversal for constant space | [notes](08-Trees/RecoverBinarySearchTree.md) | [java](08-Trees/RecoverBinarySearchTree.java) |
| 40 | **Serialize and Deserialize Binary Tree** (LC 297) | traversal with explicit null markers (preorder DFS or level-order BFS) | [notes](08-Trees/SerializeDeserializeBinaryTree.md) | [java](08-Trees/SerializeDeserializeBinaryTree.java) |

### 09 · Heaps

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 41 | **Find Median from Data Stream** (LC 295) | two heaps (max-heap for the lower half, min-heap for the upper half) | [notes](09-Heaps/FindMedianFromDataStream.md) | [java](09-Heaps/FindMedianFromDataStream.java) |
| 42 | **Minimum Cost to Hire K Workers** (LC 857) | sort by a ratio, then a max-heap keeping the k smallest of another attribute | [notes](09-Heaps/MinimumCostToHireKWorkers.md) | [java](09-Heaps/MinimumCostToHireKWorkers.java) |
| 43 | **Smallest Range Covering Elements from K Lists** (LC 632) | k-way merge heap + running maximum (or merge + sliding window) | [notes](09-Heaps/SmallestRangeCoveringKLists.md) | [java](09-Heaps/SmallestRangeCoveringKLists.java) |
| 44 | **The Skyline Problem** (LC 218) | sweep line over start/end events + a max-structure of active heights | [notes](09-Heaps/TheSkylineProblem.md) | [java](09-Heaps/TheSkylineProblem.java) |
| 45 | **Trapping Rain Water II** (LC 407) | Dijkstra-like "flood from the boundary" with a min-heap | [notes](09-Heaps/TrappingRainWaterII.md) | [java](09-Heaps/TrappingRainWaterII.java) |

### 10 · Graphs Traversal

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 46 | **Alien Dictionary** (LC 269) | derive ordering constraints from adjacent pairs → topological sort with cycle detection | [notes](10-Graphs-Traversal/AlienDictionary.md) | [java](10-Graphs-Traversal/AlienDictionary.java) |
| 47 | **Critical Connections in a Network** (LC 1192) | Tarjan's bridge-finding with discovery times and low-links | [notes](10-Graphs-Traversal/CriticalConnections.md) | [java](10-Graphs-Traversal/CriticalConnections.java) |
| 48 | **Reconstruct Itinerary** (LC 332) | Eulerian path — Hierholzer's algorithm with lexicographic adjacency | [notes](10-Graphs-Traversal/ReconstructItinerary.md) | [java](10-Graphs-Traversal/ReconstructItinerary.java) |
| 49 | **Shortest Path Visiting All Nodes** (LC 847) | BFS over states `(node, visitedMask)` — multi-source | [notes](10-Graphs-Traversal/ShortestPathVisitingAllNodes.md) | [java](10-Graphs-Traversal/ShortestPathVisitingAllNodes.java) |
| 50 | **Word Ladder II** (LC 126) | BFS by layers to build a shortest-path DAG, then DFS/backtrack to enumerate paths | [notes](10-Graphs-Traversal/WordLadderII.md) | [java](10-Graphs-Traversal/WordLadderII.java) |

### 11 · Graphs Shortest Path MST

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 51 | **Bus Routes** (LC 815) | BFS on the right graph — routes as nodes, not stops | [notes](11-Graphs-Shortest-Path-MST/BusRoutes.md) | [java](11-Graphs-Shortest-Path-MST/BusRoutes.java) |
| 52 | **Find Critical and Pseudo-Critical Edges in MST** (LC 1489) | Kruskal with an edge **excluded** / **forced** | [notes](11-Graphs-Shortest-Path-MST/CriticalAndPseudoCriticalMSTEdges.md) | [java](11-Graphs-Shortest-Path-MST/CriticalAndPseudoCriticalMSTEdges.java) |
| 53 | **Minimum Cost to Reach Destination in Time** (LC 1928) | constrained shortest path — DP over (time, node) or Dijkstra on cost with time pruning | [notes](11-Graphs-Shortest-Path-MST/MinCostToReachDestinationInTime.md) | [java](11-Graphs-Shortest-Path-MST/MinCostToReachDestinationInTime.java) |
| 54 | **Minimum Cost to Make at Least One Valid Path in a Grid** (LC 1368) | 0-1 BFS (deque) on implicit edge weights 0/1 | [notes](11-Graphs-Shortest-Path-MST/MinimumCostToMakeValidPath.md) | [java](11-Graphs-Shortest-Path-MST/MinimumCostToMakeValidPath.java) |
| 55 | **Swim in Rising Water** (LC 778) | minimax path — Dijkstra on max, binary search + BFS, or Kruskal/DSU | [notes](11-Graphs-Shortest-Path-MST/SwimInRisingWater.md) | [java](11-Graphs-Shortest-Path-MST/SwimInRisingWater.java) |

### 12 · DP Sequences Strings

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 56 | **Distinct Subsequences** (LC 115) | counting DP over two strings, 1D with a backwards loop | [notes](12-DP-Sequences-Strings/DistinctSubsequences.md) | [java](12-DP-Sequences-Strings/DistinctSubsequences.java) |
| 57 | **Edit Distance** (LC 72) | two-string DP over prefixes (Levenshtein) | [notes](12-DP-Sequences-Strings/EditDistance.md) | [java](12-DP-Sequences-Strings/EditDistance.java) |
| 58 | **Regular Expression Matching** (LC 10) | two-string DP where `x*` means "zero or more x" | [notes](12-DP-Sequences-Strings/RegularExpressionMatching.md) | [java](12-DP-Sequences-Strings/RegularExpressionMatching.java) |
| 59 | **Russian Doll Envelopes** (LC 354) | 2D LIS → sort cleverly, then 1D LIS in O(n log n) | [notes](12-DP-Sequences-Strings/RussianDollEnvelopes.md) | [java](12-DP-Sequences-Strings/RussianDollEnvelopes.java) |
| 60 | **Scramble String** (LC 87) | interval DP over (start in s1, start in s2, length) with split choices | [notes](12-DP-Sequences-Strings/ScrambleString.md) | [java](12-DP-Sequences-Strings/ScrambleString.java) |

### 13 · DP Interval Bitmask Digit

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 61 | **Burst Balloons** (LC 312) | interval DP choosing the **last** element to process in a range | [notes](13-DP-Interval-Bitmask-Digit/BurstBalloons.md) | [java](13-DP-Interval-Bitmask-Digit/BurstBalloons.java) |
| 62 | **Cherry Pickup** (LC 741) | turn "go and return" into **two simultaneous walkers** → DP over (step, r1, r2) | [notes](13-DP-Interval-Bitmask-Digit/CherryPickup.md) | [java](13-DP-Interval-Bitmask-Digit/CherryPickup.java) |
| 63 | **Number of Digit One** (LC 233) | per-digit contribution counting (math) or digit DP | [notes](13-DP-Interval-Bitmask-Digit/NumberOfDigitOne.md) | [java](13-DP-Interval-Bitmask-Digit/NumberOfDigitOne.java) |
| 64 | **Strange Printer** (LC 664) | interval DP where matching endpoints **merge** turns | [notes](13-DP-Interval-Bitmask-Digit/StrangePrinter.md) | [java](13-DP-Interval-Bitmask-Digit/StrangePrinter.java) |
| 65 | **Super Egg Drop** (LC 887) | minimax DP → binary search on the monotone split → **inverted DP** (moves × eggs → floors) | [notes](13-DP-Interval-Bitmask-Digit/SuperEggDrop.md) | [java](13-DP-Interval-Bitmask-Digit/SuperEggDrop.java) |

### 14 · Greedy Intervals

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 66 | **Candy** (LC 135) | two-pass greedy (left constraints, then right constraints) or one-pass slope counting | [notes](14-Greedy-Intervals/Candy.md) | [java](14-Greedy-Intervals/Candy.java) |
| 67 | **Course Schedule III** (LC 630) | scheduling with deadlines — sort by deadline + max-heap "swap out the longest" | [notes](14-Greedy-Intervals/CourseScheduleIII.md) | [java](14-Greedy-Intervals/CourseScheduleIII.java) |
| 68 | **Create Maximum Number** (LC 321) | split k between two arrays; each part = max subsequence via monotonic stack; combine with a greedy lexicographic merge | [notes](14-Greedy-Intervals/CreateMaximumNumber.md) | [java](14-Greedy-Intervals/CreateMaximumNumber.java) |
| 69 | **Minimum Number of Refueling Stops** (LC 871) | "retroactive" greedy with a max-heap (or DP on stops) | [notes](14-Greedy-Intervals/MinimumRefuelingStops.md) | [java](14-Greedy-Intervals/MinimumRefuelingStops.java) |
| 70 | **Patching Array** (LC 330) | greedy on the "covered range" `[1, miss)` | [notes](14-Greedy-Intervals/PatchingArray.md) | [java](14-Greedy-Intervals/PatchingArray.java) |

### 15 · Advanced Data Structures

| # | Problem | Key pattern | Write-up | Code |
|---|---|---|---|---|
| 71 | **Count of Smaller Numbers After Self** (LC 315) | inversion counting per element — merge sort with indices, Fenwick tree, or order-statistic BST | [notes](15-Advanced-Data-Structures/CountOfSmallerNumbersAfterSelf.md) | [java](15-Advanced-Data-Structures/CountOfSmallerNumbersAfterSelf.java) |
| 72 | **Maximum XOR With an Element From Array** (LC 1707) | binary trie + **offline queries** sorted by limit | [notes](15-Advanced-Data-Structures/MaximumXorWithElementFromArray.md) | [java](15-Advanced-Data-Structures/MaximumXorWithElementFromArray.java) |
| 73 | **Number of Islands II** (LC 305) | online connectivity with Union-Find (path compression + union by rank/size) | [notes](15-Advanced-Data-Structures/NumberOfIslandsII.md) | [java](15-Advanced-Data-Structures/NumberOfIslandsII.java) |
| 74 | **Rectangle Area II** (LC 850) | coordinate compression + sweep line + segment tree of **covered length** | [notes](15-Advanced-Data-Structures/RectangleAreaII.md) | [java](15-Advanced-Data-Structures/RectangleAreaII.java) |
| 75 | **Word Search II** (LC 212) | trie of all words + single grid DFS with pruning | [notes](15-Advanced-Data-Structures/WordSearchII.md) | [java](15-Advanced-Data-Structures/WordSearchII.java) |

---
See also: [REMAINING.md](../REMAINING.md) for the full checklist of the core topics, and the main [README](../README.md) for the 12-week plan.
