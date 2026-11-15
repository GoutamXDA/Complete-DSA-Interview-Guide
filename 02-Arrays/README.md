# 02 — Arrays

## Notes
- Contiguous memory, **O(1)** random access, O(n) insert/delete in the middle.
- Java: `int[]` fixed size (default 0), `ArrayList<Integer>` dynamic (boxing overhead).
- 2D: `int[][] g = new int[rows][cols]`, row-major.

## Core techniques
| Technique | Idea | Classic problems |
|---|---|---|
| Prefix sum | `pre[i] = a[0] + … + a[i-1]`, range sum = `pre[r+1] - pre[l]` | Range sum query, subarray sum = K |
| Kadane | keep best sum ending here; reset when negative | Maximum subarray |
| Dutch national flag | 3 pointers low/mid/high | Sort 0s 1s 2s |
| Reversal trick | reverse whole, then parts | Rotate array |
| Boyer–Moore voting | cancel out different elements | Majority element |
| Left/right pass | precompute from both ends | Product except self, trapping rain water |
| In-place marking | use sign or index as a flag | First missing positive, find duplicates |
| Matrix layers | transpose + reverse, boundary pointers | Rotate image, spiral |

## Pitfalls
- Off-by-one on bounds; empty array; `int` overflow on sums (use `long`).
- Modifying an array while iterating; `Arrays.asList(int[])` doesn't do what you think.

## Problems

| # | Problem | Level | Best approach | Solution |
|---|---|---|---|---|
| 1 | Largest / second largest element | Easy | one pass, two variables | [BasicArrayOps](BasicArrayOps.java) |
| 2 | Check if array is sorted | Easy | one pass | [BasicArrayOps](BasicArrayOps.java) |
| 3 | Remove duplicates from sorted array (LC 26) | Easy | write pointer | [BasicArrayOps](BasicArrayOps.java) |
| 4 | Move zeroes (LC 283) | Easy | write pointer + swap | [BasicArrayOps](BasicArrayOps.java) |
| 5 | Missing number (LC 268) | Easy | sum formula / XOR | [BasicArrayOps](BasicArrayOps.java) |
| 6 | Two Sum (LC 1) | Easy | HashMap complement | [TwoSum](TwoSum.java) |
| 7 | Best time to buy & sell stock (LC 121) | Easy | track min so far | [StockBuySell](StockBuySell.java) |
| 8 | Rotate array (LC 189) | Medium | 3 reversals | [RotateArray](RotateArray.java) |
| 9 | Maximum subarray (LC 53) | Medium | Kadane | [KadaneMaxSubarray](KadaneMaxSubarray.java) |
| 10 | Sort colors (LC 75) | Medium | Dutch flag | [SortColors](SortColors.java) |
| 11 | Majority element (LC 169 / 229) | Easy/Med | Boyer–Moore | [MajorityElement](MajorityElement.java) |
| 12 | Product of array except self (LC 238) | Medium | prefix × suffix | [ProductExceptSelf](ProductExceptSelf.java) |
| 13 | Next permutation (LC 31) | Medium | find dip, swap, reverse | [NextPermutation](NextPermutation.java) |
| 14 | Rotate image (LC 48) | Medium | transpose + reverse rows | [MatrixProblems](MatrixProblems.java) |
| 15 | Spiral matrix (LC 54) | Medium | 4 boundaries | [MatrixProblems](MatrixProblems.java) |
| 16 | Set matrix zeroes (LC 73) | Medium | first row/col as markers | [MatrixProblems](MatrixProblems.java) |
| 17 | Trapping rain water (LC 42) | Hard | two pointers | [../05-Two-Pointers-Sliding-Window/TrappingRainWater](../05-Two-Pointers-Sliding-Window/TrappingRainWater.java) |
| 18 | First missing positive (LC 41) | Hard | cyclic sort / index marking | [FirstMissingPositive](FirstMissingPositive.java) |
| 19 | Merge intervals (LC 56) | Medium | sort by start | see 16-Greedy |
| 20 | Leaders in an array | Easy | scan from right | _(todo)_ |
| 21 | Longest consecutive sequence (LC 128) | Medium | HashSet, start at sequence heads | see 04-Hashing |
| 22 | Pascal's triangle (LC 118) | Easy | row from previous row | _(todo)_ |
| 23 | Find the duplicate number (LC 287) | Medium | Floyd cycle on indices | _(todo)_ |
| 24 | Maximum product subarray (LC 152) | Medium | track max & min ending here | _(todo)_ |
| 25 | Count inversions | Hard | merge sort | see 07-Sorting |
