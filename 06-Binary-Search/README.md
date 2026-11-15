# 06 — Binary Search

## Notes
Binary search works on any **monotonic predicate**: a function `ok(x)` that is `false…false true…true`. You find the boundary in **O(log n)**.

### Template (find first index where `ok` is true)
```java
int lo = 0, hi = n - 1, ans = n;
while (lo <= hi) {
    int mid = lo + (hi - lo) / 2;   // avoids overflow of (lo + hi)
    if (ok(mid)) { ans = mid; hi = mid - 1; }
    else lo = mid + 1;
}
```
- **lower_bound(x)**: first index with `a[i] >= x`
- **upper_bound(x)**: first index with `a[i] > x`
- count of x = upper − lower

### Binary search on the answer
When asked to **minimise the maximum** / **maximise the minimum** / "smallest capacity/speed/days such that…":
1. Identify answer range `[lo, hi]`.
2. Write `feasible(mid)` — usually a greedy O(n) check.
3. Binary search for the boundary → O(n log(range)).

### Rotated sorted array
At any `mid`, **one half is sorted**. Check if target lies in the sorted half; otherwise go to the other half.

### Pitfalls
- Infinite loops with `lo < hi` + `lo = mid` → use `mid = lo + (hi - lo + 1) / 2` when moving `lo = mid`.
- Duplicates in rotated arrays break the "which half is sorted" test → shrink both ends when `a[lo] == a[mid] == a[hi]`.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Binary search (LC 704) | Easy | iterative & recursive | [BinarySearchBasics](BinarySearchBasics.java) |
| 2 | Lower / upper bound, search insert (LC 35) | Easy | boundary template | [BinarySearchBasics](BinarySearchBasics.java) |
| 3 | First & last position (LC 34) | Medium | two boundary searches | [BinarySearchBasics](BinarySearchBasics.java) |
| 4 | Count occurrences | Easy | upper − lower | [BinarySearchBasics](BinarySearchBasics.java) |
| 5 | Sqrt(x) (LC 69) | Easy | search on answer | [BinarySearchBasics](BinarySearchBasics.java) |
| 6 | Search in rotated sorted array (LC 33) | Medium | find sorted half | [RotatedSortedArray](RotatedSortedArray.java) |
| 7 | Search in rotated array II – duplicates (LC 81) | Medium | shrink ends on ties | [RotatedSortedArray](RotatedSortedArray.java) |
| 8 | Find minimum in rotated array (LC 153) | Medium | compare mid with hi | [RotatedSortedArray](RotatedSortedArray.java) |
| 9 | Number of rotations | Easy | index of minimum | [RotatedSortedArray](RotatedSortedArray.java) |
| 10 | Find peak element (LC 162) | Medium | go toward the larger neighbour | [PeakAndMatrix](PeakAndMatrix.java) |
| 11 | Single element in sorted array (LC 540) | Medium | pair index parity | [PeakAndMatrix](PeakAndMatrix.java) |
| 12 | Search a 2D matrix (LC 74) | Medium | treat as 1D | [PeakAndMatrix](PeakAndMatrix.java) |
| 13 | Search a 2D matrix II (LC 240) | Medium | staircase from top-right | [PeakAndMatrix](PeakAndMatrix.java) |
| 14 | Koko eating bananas (LC 875) | Medium | BS on speed | [BinarySearchOnAnswer](BinarySearchOnAnswer.java) |
| 15 | Capacity to ship packages (LC 1011) | Medium | BS on capacity | [BinarySearchOnAnswer](BinarySearchOnAnswer.java) |
| 16 | Split array largest sum / Book allocation / Painter's partition (LC 410) | Hard | BS on max sum | [BinarySearchOnAnswer](BinarySearchOnAnswer.java) |
| 17 | Aggressive cows / Magnetic force (LC 1552) | Hard | BS on min distance | [BinarySearchOnAnswer](BinarySearchOnAnswer.java) |
| 18 | Minimum days to make bouquets (LC 1482) | Medium | BS on days | [BinarySearchOnAnswer](BinarySearchOnAnswer.java) |
| 19 | Median of two sorted arrays (LC 4) | Hard | partition the smaller array | [MedianOfTwoSortedArrays](MedianOfTwoSortedArrays.java) |
| 20 | Kth element of two sorted arrays | Hard | same partition idea | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 21 | Kth missing positive (LC 1539) | Easy | missing = a[i] − (i+1) | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 22 | Find in mountain array (LC 1095) | Hard | peak + two searches | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 23 | Nth root of an integer (GFG) | Easy | binary search on answer, careful overflow | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 24 | Find the smallest divisor given a threshold (LC 1283) | Medium | BS on divisor, sum of ceilings | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 25 | Find K closest elements (LC 658) | Medium | BS on window start `[lo, n−k]` | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 26 | Row with maximum 1s (GFG) | Medium | lower_bound per row / staircase | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 27 | Find a peak element II (LC 1901) | Medium | BS on columns, max in column | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 28 | Time based key-value store (LC 981) | Medium | map → list of (time, value), upper bound | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 29 | Kth smallest element in a sorted matrix (LC 378) | Medium | BS on value + staircase count (or heap) | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 30 | Median of row-wise sorted matrix (GFG) | Hard | BS on value, count ≤ mid per row | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
| 31 | Minimize max distance to gas station (LC 774) | Hard | BS on doubles / heap of gaps | [BinarySearchProblemsII](BinarySearchProblemsII.java) |
