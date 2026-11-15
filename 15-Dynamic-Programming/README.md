# 15 — Dynamic Programming

## When is it DP?
1. **Optimal substructure** – the answer is built from answers to subproblems.
2. **Overlapping subproblems** – the same subproblem is solved repeatedly by naive recursion.
Keywords: *count the ways*, *minimum/maximum*, *is it possible*, *longest/shortest*, with choices at each step.

## The 4-step method (show every step in the interview)
1. **Recursion** – define `f(state)` in words ("min cost to reach step i"). Write the recurrence + base cases. → exponential.
2. **Memoisation (top-down)** – cache `f(state)` in an array/map. → time = #states × work per state.
3. **Tabulation (bottom-up)** – fill the table in dependency order; no recursion stack.
4. **Space optimisation** – keep only the previous row(s)/variables you actually read.

## State design cheat sheet
| Family | State | Typical transition |
|---|---|---|
| 1D linear | `dp[i]` | `dp[i-1]`, `dp[i-2]` (stairs, robber) |
| Grid | `dp[r][c]` | from top / left |
| 0/1 knapsack | `dp[i][cap]` | take or skip item i (iterate cap **backwards** in 1D) |
| Unbounded knapsack | `dp[cap]` | take item again (iterate cap **forwards**) |
| Two strings | `dp[i][j]` over prefixes | match → `dp[i-1][j-1]`, else combine |
| LIS | `dp[i]` = LIS ending at i | max over j < i; or patience sort O(n log n) |
| Stocks | `dp[i][holding][k]` | buy / sell / rest |
| Intervals / partition | `dp[i][j]` over a range | try every split k (MCM, burst balloons) |
| Trees | return pair from children | rob / not rob |
| Bitmask | `dp[mask][i]` | add one unused element (TSP, assignment) n ≤ 20 |
| Digit DP | `(pos, tight, …)` | choose each digit ≤ limit |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Climbing stairs (LC 70) | Easy | fib-like, all 4 steps shown | [DP1D](DP1D.java) |
| 2 | Frog jump / min cost climbing stairs (LC 746) | Easy | `min(dp[i-1], dp[i-2]) + cost` | [DP1D](DP1D.java) |
| 3 | House robber I/II (LC 198/213) | Medium | take/skip; circular = 2 runs | [DP1D](DP1D.java) |
| 4 | Decode ways (LC 91) | Medium | 1-digit and 2-digit transitions | [DP1D](DP1D.java) |
| 5 | Word break (LC 139) | Medium | `dp[i]` prefix breakable | [DP1D](DP1D.java) |
| 6 | Unique paths I/II (LC 62/63) | Medium | grid, obstacles | [DPGrid](DPGrid.java) |
| 7 | Minimum path sum (LC 64) | Medium | grid min | [DPGrid](DPGrid.java) |
| 8 | Triangle (LC 120) | Medium | bottom-up | [DPGrid](DPGrid.java) |
| 9 | Minimum falling path sum (LC 931) | Medium | 3 parents | [DPGrid](DPGrid.java) |
| 10 | Maximal square (LC 221) | Medium | min of 3 neighbours + 1 | [DPGrid](DPGrid.java) |
| 11 | Cherry pickup II (LC 1463) | Hard | 3D: row, col1, col2 | [DPGrid](DPGrid.java) |
| 12 | Subset sum / Partition equal subset (LC 416) | Medium | boolean knapsack | [KnapsackFamily](KnapsackFamily.java) |
| 13 | Count subsets with sum K | Medium | counting knapsack | [KnapsackFamily](KnapsackFamily.java) |
| 14 | Target sum (LC 494) | Medium | reduce to count subsets | [KnapsackFamily](KnapsackFamily.java) |
| 15 | 0/1 knapsack | Medium | take/skip | [KnapsackFamily](KnapsackFamily.java) |
| 16 | Unbounded knapsack / rod cutting | Medium | forward loop | [KnapsackFamily](KnapsackFamily.java) |
| 17 | Coin change (LC 322) / Coin change II (LC 518) | Medium | min coins / count ways | [KnapsackFamily](KnapsackFamily.java) |
| 18 | Longest common subsequence (LC 1143) + print | Medium | two-string DP | [StringDP](StringDP.java) |
| 19 | Longest common substring | Medium | reset on mismatch | [StringDP](StringDP.java) |
| 20 | Longest palindromic subsequence (LC 516) | Medium | LCS(s, reverse s) | [StringDP](StringDP.java) |
| 21 | Min insertions to make palindrome (LC 1312) | Hard | n − LPS | [StringDP](StringDP.java) |
| 22 | Edit distance (LC 72) | Hard | insert/delete/replace | [StringDP](StringDP.java) |
| 23 | Distinct subsequences (LC 115) | Hard | count matches | [StringDP](StringDP.java) |
| 24 | Wildcard matching (LC 44) | Hard | `*` = empty or one more | [StringDP](StringDP.java) |
| 25 | Regular expression matching (LC 10) | Hard | `x*` zero or more | [StringDP](StringDP.java) |
| 26 | Longest increasing subsequence (LC 300) | Medium | O(n²) → O(n log n) | [LISFamily](LISFamily.java) |
| 27 | Print LIS / Number of LIS (LC 673) | Medium | parent array / count array | [LISFamily](LISFamily.java) |
| 28 | Largest divisible subset (LC 368) | Medium | sort + LIS variant | [LISFamily](LISFamily.java) |
| 29 | Russian doll envelopes (LC 354) | Hard | sort (w asc, h desc) + LIS | [LISFamily](LISFamily.java) |
| 30 | Stock II / III / IV / cooldown / fee (LC 122/123/188/309/714) | Med→Hard | state machine | [StockDP](StockDP.java) |
| 31 | Matrix chain multiplication | Hard | partition DP | [IntervalDP](IntervalDP.java) |
| 32 | Burst balloons (LC 312) | Hard | last balloon to burst | [IntervalDP](IntervalDP.java) |
| 33 | Min cost to cut a stick (LC 1547) | Hard | partition DP on cuts | [IntervalDP](IntervalDP.java) |
| 34 | Palindrome partitioning II (LC 132) | Hard | front partition | [IntervalDP](IntervalDP.java) |
| 35 | House robber III (LC 337) | Medium | tree DP pair | [AdvancedDP](AdvancedDP.java) |
| 36 | Travelling salesman | Hard | bitmask DP | [AdvancedDP](AdvancedDP.java) |
| 37 | Count numbers with unique digits ≤ N | Hard | digit DP | [AdvancedDP](AdvancedDP.java) |
| 38 | Partition array into two subsets min difference | Hard | subset sum table | [AdvancedDP](AdvancedDP.java) |
| 39 | Count square submatrices (LC 1277) | Medium | same as maximal square | [AdvancedDP](AdvancedDP.java) |
| 40 | Egg dropping (LC 887) | Hard | dp[moves][eggs] | [AdvancedDP](AdvancedDP.java) |
| 41 | Ninja's training | Medium | dp[day][last] | _(todo)_ |
| 42 | Shortest common supersequence (LC 1092) | Hard | from LCS table | _(todo)_ |
| 43 | Boolean parenthesization | Hard | interval DP true/false counts | _(todo)_ |
| 44 | Max rectangle of 1s (LC 85) | Hard | histogram per row | see 10 |
