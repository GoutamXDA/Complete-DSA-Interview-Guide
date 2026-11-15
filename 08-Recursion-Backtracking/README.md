# 08 — Recursion & Backtracking

## Recursion notes
Every recursive function needs:
1. **Base case** – when to stop.
2. **Recursive case** – reduce the problem toward the base case.
3. **Trust** – assume the smaller call works ("leap of faith").

- Space = recursion depth (call stack). Java default stack ≈ 10⁴–10⁵ frames → deep recursion can `StackOverflowError`.
- Recurrence → complexity: `T(n) = T(n-1) + O(1)` → O(n); `T(n) = 2T(n-1)` → O(2ⁿ); `T(n) = 2T(n/2) + O(n)` → O(n log n).
- Overlapping subproblems? Add memoisation → DP (see 15).

## Backtracking template
```java
void backtrack(State state, List<Choice> path) {
    if (isSolution(state)) { result.add(new ArrayList<>(path)); return; }  // copy!
    for (Choice c : choices(state)) {
        if (!valid(c)) continue;      // prune
        path.add(c);                  // choose
        backtrack(next(state, c), path);  // explore
        path.remove(path.size() - 1); // un-choose
    }
}
```

## Pattern map
| Pattern | Choice at each step | Count |
|---|---|---|
| Subsets | take / skip element | 2ⁿ |
| Combinations (size k) | pick next index > previous | C(n,k) |
| Permutations | pick any unused element | n! |
| Duplicates in input | sort + `if (i > start && a[i] == a[i-1]) continue` | |
| Grid search | 4 directions, mark visited, unmark | |
| Constraint placement | N-Queens, Sudoku: check row/col/box sets | |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Print 1..N, sum, factorial, reverse array, palindrome | Easy | basic recursion | [RecursionBasics](RecursionBasics.java) |
| 2 | Fibonacci (LC 509) | Easy | naive → memo → iterative | [RecursionBasics](RecursionBasics.java) |
| 3 | Tower of Hanoi | Medium | move n-1, move 1, move n-1 | [RecursionBasics](RecursionBasics.java) |
| 4 | Subsets (LC 78) | Medium | take/skip, loop, bitmask | [SubsetsPermutations](SubsetsPermutations.java) |
| 5 | Subsets II (LC 90) | Medium | sort + skip dupes | [SubsetsPermutations](SubsetsPermutations.java) |
| 6 | Permutations (LC 46) | Medium | used[] / swapping | [SubsetsPermutations](SubsetsPermutations.java) |
| 7 | Permutations II (LC 47) | Medium | sort + skip if prev unused | [SubsetsPermutations](SubsetsPermutations.java) |
| 8 | Subset sums | Medium | take/skip | [SubsetsPermutations](SubsetsPermutations.java) |
| 9 | Combination sum (LC 39) | Medium | reuse allowed → stay on i | [CombinationSum](CombinationSum.java) |
| 10 | Combination sum II (LC 40) | Medium | no reuse + skip dupes | [CombinationSum](CombinationSum.java) |
| 11 | Combination sum III (LC 216) | Medium | k numbers 1..9 | [CombinationSum](CombinationSum.java) |
| 12 | Combinations (LC 77) | Medium | choose k of n | [CombinationSum](CombinationSum.java) |
| 13 | Generate parentheses (LC 22) | Medium | open < n, close < open | [StringBacktracking](StringBacktracking.java) |
| 14 | Letter combinations of phone number (LC 17) | Medium | digit → letters | [StringBacktracking](StringBacktracking.java) |
| 15 | Palindrome partitioning (LC 131) | Medium | cut at palindromic prefixes | [StringBacktracking](StringBacktracking.java) |
| 16 | Word search (LC 79) | Medium | DFS grid, mark/unmark | [StringBacktracking](StringBacktracking.java) |
| 17 | N-Queens (LC 51/52) | Hard | col + 2 diagonal sets | [NQueensSudoku](NQueensSudoku.java) |
| 18 | Sudoku solver (LC 37) | Hard | try 1–9 with row/col/box masks | [NQueensSudoku](NQueensSudoku.java) |
| 19 | Rat in a maze | Medium | DFS with path string | [NQueensSudoku](NQueensSudoku.java) |
| 20 | M-coloring problem | Medium | try each colour, check neighbours | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
| 21 | Word break II (LC 140) | Hard | backtrack + memo | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
| 22 | Expression add operators (LC 282) | Hard | track prev operand for * | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
| 23 | Restore IP addresses (LC 93) | Medium | 4 parts, each 0–255, no leading zeros | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
| 24 | Partition to K equal sum subsets (LC 698) | Medium | sort desc, fill buckets, prune | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
| 25 | Permutation sequence (kth permutation) (LC 60) | Hard | factorial number system, no backtracking | [BacktrackingProblemsII](BacktrackingProblemsII.java) |
