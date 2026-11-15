# 16 — Greedy

## Notes
Greedy = make the locally best choice and never revisit it. It works only when you can argue:
1. **Greedy-choice property** – some optimal solution starts with the greedy choice.
2. **Optimal substructure** – after the choice, the rest is the same problem.

Prove it with an **exchange argument**: take any optimal solution, swap in the greedy choice, show it doesn't get worse. If you can't, try DP (e.g., 0/1 knapsack and coin change with arbitrary coins are **not** greedy).

## Interval cheat sheet
| Goal | Sort by | Rule |
|---|---|---|
| Max non-overlapping intervals (activity selection) | **end** | take if start ≥ last end |
| Min removals to make non-overlapping (LC 435) | end | n − max non-overlapping |
| Min arrows / points to cover all (LC 452) | end | new arrow when start > arrow position |
| Merge overlapping (LC 56) | start | extend while start ≤ current end |
| Min rooms (LC 253) | start (+ heap of ends) | see 13 |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Assign cookies (LC 455) | Easy | sort both, two pointers | [GreedyBasics](GreedyBasics.java) |
| 2 | Lemonade change (LC 860) | Easy | give $10 before $5s | [GreedyBasics](GreedyBasics.java) |
| 3 | Fractional knapsack | Medium | value/weight ratio | [GreedyBasics](GreedyBasics.java) |
| 4 | Minimum coins (canonical system) | Easy | largest coin first | [GreedyBasics](GreedyBasics.java) |
| 5 | Job sequencing with deadlines | Medium | sort by profit, latest free slot (DSU for speed) | [GreedyBasics](GreedyBasics.java) |
| 6 | Candy (LC 135) | Hard | two passes | [GreedyBasics](GreedyBasics.java) |
| 7 | Valid parenthesis string (LC 678) | Medium | track min/max open count | [GreedyBasics](GreedyBasics.java) |
| 8 | Jump game I (LC 55) | Medium | farthest reachable | [JumpAndGas](JumpAndGas.java) |
| 9 | Jump game II (LC 45) | Medium | BFS-like levels | [JumpAndGas](JumpAndGas.java) |
| 10 | Gas station (LC 134) | Medium | reset start when tank < 0 | [JumpAndGas](JumpAndGas.java) |
| 11 | Partition labels (LC 763) | Medium | last occurrence | [JumpAndGas](JumpAndGas.java) |
| 12 | Merge intervals (LC 56) | Medium | sort by start | [IntervalProblems](IntervalProblems.java) |
| 13 | Insert interval (LC 57) | Medium | 3 phases | [IntervalProblems](IntervalProblems.java) |
| 14 | Non-overlapping intervals (LC 435) | Medium | sort by end | [IntervalProblems](IntervalProblems.java) |
| 15 | N meetings in one room / activity selection | Easy | sort by end | [IntervalProblems](IntervalProblems.java) |
| 16 | Min arrows to burst balloons (LC 452) | Medium | sort by end | [IntervalProblems](IntervalProblems.java) |
| 17 | Minimum platforms | Medium | sort arrivals & departures | [IntervalProblems](IntervalProblems.java) |
| 18 | Task scheduler (LC 621) | Medium | formula | see 13 |
| 19 | Queue reconstruction by height (LC 406) | Medium | sort h desc, k asc, insert at k | _(todo)_ |
| 20 | Minimum number of taps (LC 1326) | Hard | convert to jump game II | _(todo)_ |
