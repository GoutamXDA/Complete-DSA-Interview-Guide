# Remaining Problems Tracker

This is the master list of everything **not yet solved in code** in this repo. Work through it topic by topic. When a problem gets a Java solution, tick it here **and** link it from the topic README's table.

- **Source** says where a problem came from: `todo` = already listed in the topic README as _(todo)_; `new` = found by checking Blind 75, NeetCode 150 and Striver's A2Z/SDE sheet against this repo.
- `GFG` = GeeksforGeeks / Striver-sheet problem with no LeetCode number.
- Order inside each topic = recommended solving order (easy → hard, prerequisites first).

## Summary

| # | Topic | Remaining |
|---|---|---|
| 01 | Basic Maths | 4 |
| 02 | Arrays | 13 |
| 03 | Strings | 8 |
| 04 | Hashing | 4 |
| 05 | Two Pointers & Sliding Window | 7 |
| 06 | Binary Search | 12 |
| 07 | Sorting | 2 |
| 08 | Recursion & Backtracking | 6 |
| 09 | Linked List | 11 |
| 10 | Stack & Queue | 7 |
| 11 | Binary Trees | 11 |
| 12 | Binary Search Trees | 6 |
| 13 | Heaps / Priority Queue | 9 |
| 14 | Graphs | 18 |
| 15 | Dynamic Programming | 18 |
| 16 | Greedy | 7 |
| 17 | Tries | 3 |
| 18 | Bit Manipulation | 5 |
| 19 | Advanced Data Structures | 8 |
| | **Total** | **159** |

---

## 01 — Basic Maths
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Plus one | 66 | Easy | carry from the back; all 9s → new leading 1 | new |
| [x] | Excel sheet column number | 171 | Easy | base-26: `res = res*26 + (c-'A'+1)` | todo |
| [x] | Happy number | 202 | Easy | Floyd cycle on digit-square sum | todo |
| [x] | nCr % p | GFG | Hard | precompute fact & inverse fact (Fermat) | todo |

## 02 — Arrays
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Leaders in an array | GFG | Easy | scan from right, track max | todo |
| [x] | Pascal's triangle | 118 | Easy | each row from previous; nCr for single element | todo |
| [x] | Rearrange array elements by sign | 2149 | Medium | two write pointers (even/odd indices) | new |
| [x] | Spiral matrix II | 59 | Medium | 4 boundaries, fill 1..n² | new |
| [x] | Game of life | 289 | Medium | encode old/new state in bits, in place | new |
| [x] | Range sum query 2D – immutable | 304 | Medium | 2D prefix sums, inclusion–exclusion | new |
| [x] | Maximum product subarray | 152 | Medium | track max & min ending here (negatives swap) | todo |
| [x] | Find the duplicate number | 287 | Medium | Floyd cycle on index → value | todo |
| [x] | Merge two sorted arrays without extra space | GFG | Medium | gap method (shell-sort style) | new |
| [x] | Encode and decode strings | 271 | Medium | length + '#' prefix per string | new |
| [x] | Insert delete getRandom O(1) | 380 | Medium | ArrayList + value→index map, swap with last | new |
| [x] | Count of range sum | 327 | Hard | merge sort on prefix sums / BIT | new |
| [x] | Maximum sum rectangle in 2D matrix | GFG | Hard | fix row pair + Kadane on column sums | new |

## 03 — Strings
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Add binary | 67 | Easy | add from the back with carry | new |
| [x] | Valid palindrome II | 680 | Easy | on first mismatch, try skipping either side | new |
| [x] | Maximum nesting depth of parentheses | 1614 | Easy | running depth counter | new |
| [x] | Zigzag conversion | 6 | Medium | row index bouncing 0..n-1 | new |
| [x] | Multiply strings | 43 | Medium | `res[i+j+1] += a[i]*b[j]` grade-school | new |
| [x] | Sort characters by frequency | 451 | Medium | count + bucket by frequency | todo |
| [x] | Sum of beauty of all substrings | 1781 | Medium | for each start, extend with int[26] | new |
| [x] | Text justification | 68 | Hard | greedy line fill, distribute spaces left-first | new |

> Shortest palindrome (LC 214) was listed as todo but is already solved in [19/StringMatching.java](19-Advanced-Data-Structures/StringMatching.java).

## 04 — Hashing
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Valid sudoku | 36 | Medium | sets per row / col / box (`r/3*3 + c/3`) | new |
| [x] | Subarray sums divisible by K | 974 | Medium | prefix mod + map; fix negative mod | todo |
| [x] | 4Sum II | 454 | Medium | map of all a+b sums, look up −(c+d) | todo |
| [x] | Brick wall | 554 | Medium | count edge positions, answer = rows − max | new |

## 05 — Two Pointers & Sliding Window
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Boats to save people | 881 | Medium | sort, pair heaviest with lightest | new |
| [x] | Maximum points you can obtain from cards | 1423 | Medium | take k from ends = total − min window of n−k | new |
| [x] | Fruit into baskets | 904 | Medium | longest window with ≤ 2 distinct | todo |
| [x] | Longest substring with at most K distinct chars | 340 | Medium | window + count map | new |
| [x] | Binary subarrays with sum | 930 | Medium | atMost(goal) − atMost(goal−1) | new |
| [x] | Count number of nice subarrays | 1248 | Medium | same atMost trick on odd counts | todo |
| [x] | Substrings containing all three characters | 1358 | Medium | last-seen index of a/b/c | todo |

## 06 — Binary Search
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Kth missing positive number | 1539 | Easy | missing before i = a[i] − (i+1) | todo |
| [x] | Nth root of an integer | GFG | Easy | binary search on answer, careful overflow | new |
| [x] | Find the smallest divisor given a threshold | 1283 | Medium | BS on divisor, sum of ceilings | new |
| [x] | Find K closest elements | 658 | Medium | BS on window start `[lo, n−k]` | new |
| [x] | Row with maximum 1s | GFG | Medium | lower_bound per row / staircase | new |
| [x] | Find a peak element II | 1901 | Medium | BS on columns, max in column | new |
| [x] | Time based key-value store | 981 | Medium | map → list of (time, value), upper bound | new |
| [x] | Kth smallest element in a sorted matrix | 378 | Medium | BS on value + staircase count (or heap) | new |
| [x] | Median of row-wise sorted matrix | GFG | Hard | BS on value, count ≤ mid per row | new |
| [x] | Kth element of two sorted arrays | GFG | Hard | partition idea from LC 4 | todo |
| [x] | Minimize max distance to gas station | 774 | Hard | BS on doubles / heap of gaps | new |
| [x] | Find in mountain array | 1095 | Hard | find peak, then BS each side | todo |

## 07 — Sorting
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Wiggle sort II | 324 | Medium | median via quickselect + virtual indexing | todo |
| [x] | Maximum gap | 164 | Hard | pigeonhole buckets of size (max−min)/(n−1) | todo |

## 08 — Recursion & Backtracking
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Restore IP addresses | 93 | Medium | 4 parts, each 0–255, no leading zeros | new |
| [x] | M-coloring problem | GFG | Medium | try each colour, check neighbours | todo |
| [x] | Partition to K equal sum subsets | 698 | Medium | sort desc, fill buckets, prune | new |
| [x] | Permutation sequence (kth permutation) | 60 | Hard | factorial number system, no backtracking | new |
| [x] | Word break II | 140 | Hard | backtracking + memo on start index | todo |
| [x] | Expression add operators | 282 | Hard | carry value and last operand for `*` | todo |

## 09 — Linked List
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Swap nodes in pairs | 24 | Medium | dummy + rewire 3 pointers | new |
| [x] | Remove duplicates from sorted list II | 82 | Medium | dummy, skip whole duplicate runs | new |
| [x] | Partition list | 86 | Medium | two chains < x and ≥ x | new |
| [x] | Delete the middle node | 2095 | Medium | fast/slow with prev | new |
| [x] | Reverse linked list II | 92 | Medium | reverse sublist m..n with dummy | todo |
| [x] | Reorder list | 143 | Medium | middle + reverse second half + merge | new |
| [x] | Add one to a number in a linked list | 369 | Medium | reverse / recursion carry | new |
| [x] | Sort a linked list of 0s, 1s and 2s | GFG | Medium | three dummy chains | new |
| [x] | Design browser history | 1472 | Medium | doubly linked list / array + pointer | new |
| [x] | Flatten a multilevel doubly linked list | 430 | Medium | DFS / stack | todo |
| [x] | Flatten a linked list (bottom pointers) | GFG | Hard | merge sorted lists pairwise | new |

## 10 — Stack & Queue
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Simplify path | 71 | Medium | stack of directory names | todo |
| [x] | Remove all adjacent duplicates in string II | 1209 | Medium | stack of (char, count) | new |
| [x] | Car fleet | 853 | Medium | sort by position, stack of arrival times | new |
| [x] | Design hit counter | 362 | Medium | queue / circular buckets | new |
| [x] | Sum of subarray ranges | 2104 | Medium | Σ max contribution − Σ min contribution | todo |
| [x] | Longest valid parentheses | 32 | Hard | stack of indices / two-pass counters | new |
| [x] | Maximum frequency stack | 895 | Hard | freq map + stack per frequency | new |

## 11 — Binary Trees
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Binary tree paths | 257 | Easy | DFS with path string | new |
| [x] | Subtree of another tree | 572 | Easy | isSame at every node / serialize + KMP | new |
| [x] | Sum root to leaf numbers | 129 | Medium | pass `cur*10 + val` down | new |
| [x] | Count good nodes | 1448 | Medium | pass max-so-far down | new |
| [x] | Path sum III | 437 | Medium | prefix sum + map along the path | new |
| [x] | Populating next right pointers | 116 | Medium | use already-built next pointers, O(1) space | new |
| [x] | Flatten binary tree to linked list | 114 | Medium | reverse postorder / Morris-like | todo |
| [x] | Children sum property | GFG | Medium | push values down, fix up on return | todo |
| [x] | Amount of time to burn the tree | 2385 | Medium | parent map + BFS from start | todo |
| [x] | Construct from preorder and postorder | 889 | Medium | next preorder element = left child root | new |
| [x] | Binary tree cameras | 968 | Hard | greedy postorder with 3 states | new |

## 12 — Binary Search Trees
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Inorder predecessor (and successor together) | 285 | Medium | walk from root tracking candidate | new |
| [x] | Convert BST to greater tree | 538 | Medium | reverse inorder with running sum | new |
| [x] | Trim a BST | 669 | Medium | recurse on bounds | todo |
| [x] | Balance a BST | 1382 | Medium | inorder → sorted array → build | todo |
| [x] | Unique binary search trees | 96 | Medium | Catalan DP `dp[n] = Σ dp[i-1]*dp[n-i]` | new |
| [x] | Unique binary search trees II | 95 | Medium | recursive generation per root | new |

## 13 — Heaps / Priority Queue
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Minimum cost to connect ropes | 1167 | Medium | min-heap, always join two smallest | new |
| [x] | Hand of straights | 846 | Medium | TreeMap counts from smallest | todo |
| [x] | Find K pairs with smallest sums | 373 | Medium | heap seeded with (i, 0) | new |
| [x] | Maximum sum combinations | GFG | Medium | sort desc + heap + visited set | new |
| [x] | Design Twitter | 355 | Medium | per-user tweet lists + k-way merge heap | new |
| [x] | IPO | 502 | Hard | sort by capital + max-heap of profits | todo |
| [x] | Sliding window median | 480 | Hard | two heaps + lazy delete / two TreeMaps | todo |
| [x] | Smallest range covering elements from K lists | 632 | Hard | heap of heads + track current max | todo |
| [x] | Minimum cost to hire K workers | 857 | Hard | sort by wage/quality ratio + max-heap of quality | new |

## 14 — Graphs
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [x] | Max area of island | 695 | Medium | DFS returning size | new |
| [x] | Number of enclaves | 1020 | Medium | flood from border, count rest | new |
| [x] | Pacific Atlantic water flow | 417 | Medium | reverse flow from both oceans | new |
| [x] | Walls and gates | 286 | Medium | multi-source BFS from gates | new |
| [x] | Number of distinct islands | 694 | Medium | shape signature (relative coords) in a set | new |
| [x] | Number of connected components | 323 | Medium | DFS / DSU | new |
| [x] | Graph valid tree | 261 | Medium | n−1 edges + connected (DSU) | new |
| [x] | Shortest path in binary matrix | 1091 | Medium | BFS with 8 directions | new |
| [x] | Evaluate division | 399 | Medium | weighted graph DFS / weighted DSU | new |
| [x] | Minimum multiplications to reach end | GFG | Medium | BFS over states mod 100000 | new |
| [x] | Find the city with the smallest number of neighbours | 1334 | Medium | Floyd–Warshall | new |
| [x] | Number of ways to arrive at destination | 1976 | Medium | Dijkstra + count ways mod | new |
| [x] | Making a large island | 827 | Hard | label islands with sizes, try each 0 | new |
| [x] | Number of islands II | 305 | Hard | online DSU | todo |
| [x] | Swim in rising water | 778 | Hard | Dijkstra on max / BS + BFS | todo |
| [x] | Bus routes | 815 | Hard | BFS over routes, stop → routes map | new |
| [x] | Word ladder II | 126 | Hard | BFS levels + backtrack paths | new |
| [x] | Reconstruct itinerary | 332 | Hard | Hierholzer Euler path with min-heap | todo |

## 15 — Dynamic Programming
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [ ] | Ninja's training | GFG | Medium | dp[day][last activity] | todo |
| [ ] | Delete and earn | 740 | Medium | bucket values → house robber | new |
| [ ] | Perfect squares | 279 | Medium | unbounded knapsack over squares / BFS | new |
| [ ] | Combination sum IV | 377 | Medium | amount outer loop → counts permutations | new |
| [ ] | Count partitions with given difference | GFG | Medium | count subsets with sum (total − d)/2 | new |
| [ ] | Ones and zeroes | 474 | Medium | 2D 0/1 knapsack | new |
| [ ] | Partition array for maximum sum | 1043 | Medium | front partition, try last k lengths | new |
| [ ] | Delete operation for two strings | 583 | Medium | m + n − 2·LCS | new |
| [ ] | Longest string chain | 1048 | Medium | sort by length, map word → best | new |
| [ ] | Longest arithmetic subsequence | 1027 | Medium | dp[i][diff] map | new |
| [ ] | Longest bitonic subsequence | GFG | Medium | LIS from left + LIS from right − 1 | new |
| [ ] | Interleaving string | 97 | Medium | dp[i][j] over both prefixes | new |
| [ ] | Shortest common supersequence | 1092 | Hard | build from LCS table | todo |
| [ ] | Longest increasing path in a matrix | 329 | Hard | DFS + memo (DAG) | new |
| [ ] | Dungeon game | 174 | Hard | bottom-right to top-left min health | new |
| [ ] | Frog jump | 403 | Hard | map stone → set of reachable jump sizes | new |
| [ ] | Boolean parenthesization | GFG | Hard | interval DP with true/false counts | todo |
| [ ] | Partition array into two arrays to minimise sum difference | 2035 | Hard | meet in the middle + binary search | new |

## 16 — Greedy
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [ ] | Two city scheduling | 1029 | Medium | sort by costA − costB | new |
| [ ] | Maximum length of pair chain | 646 | Medium | sort by end (activity selection) | new |
| [ ] | Merge triplets to form target triplet | 1899 | Medium | keep triplets ≤ target, OR the matches | new |
| [ ] | Broken calculator | 991 | Medium | work backwards from target | new |
| [ ] | Shortest job first (average waiting time) | GFG | Easy | sort burst times, prefix sum | new |
| [ ] | Queue reconstruction by height | 406 | Medium | sort h desc, k asc; insert at index k | todo |
| [ ] | Minimum number of taps to water a garden | 1326 | Hard | convert to intervals → jump game II | todo |

## 17 — Tries
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [ ] | Replace words | 648 | Medium | shortest root prefix via trie | todo |
| [ ] | Search suggestions system | 1268 | Medium | trie with top-3 per node / sort + binary search | todo |
| [ ] | Palindrome pairs | 336 | Hard | trie of reversed words + palindrome checks | todo |

## 18 — Bit Manipulation
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [ ] | Power of four | 342 | Easy | power of 2 and bit in `0x55555555` | new |
| [ ] | Gray code | 89 | Medium | `i ^ (i >> 1)` | todo |
| [ ] | Bitwise AND of numbers range | 201 | Medium | common prefix: shift until equal | new |
| [ ] | Total hamming distance | 477 | Medium | per bit: ones × zeros | new |
| [ ] | XOR queries of a subarray | 1310 | Medium | prefix XOR | new |

## 19 — Advanced Data Structures
| ✓ | Problem | LC | Level | Approach hint | Source |
|---|---|---|---|---|---|
| [ ] | Repeated DNA sequences | 187 | Medium | rolling hash / 20-bit encoding | new |
| [ ] | Snapshot array | 1146 | Medium | per index list of (snap, val) + binary search | new |
| [ ] | Range sum query 2D – mutable | 308 | Hard | 2D Fenwick tree | new |
| [ ] | My calendar III | 732 | Hard | sweep line TreeMap / dynamic segment tree | new |
| [ ] | The skyline problem | 218 | Hard | sweep events + max-heap / TreeMap | new |
| [ ] | Longest duplicate substring | 1044 | Hard | binary search length + Rabin–Karp | new |
| [ ] | Manacher's algorithm (LC 5 in O(n)) | 5 | Hard | mirror radii on `#a#b#` transformed string | new |
| [ ] | Count inversions with Fenwick tree | GFG | Medium | compress ranks, BIT from right | new |

---

## Definition of done for each problem
1. `ProblemName.java` in the topic folder, self-contained, same header template.
2. Brute force → better → optimal (when they differ), each with time & space.
3. `main` with `check(...)` tests including edge cases.
4. `./run-all.sh` passes.
5. Row in the topic README changed from _(todo)_ (or added) with a link; box ticked here.
