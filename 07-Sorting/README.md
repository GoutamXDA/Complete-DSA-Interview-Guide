# 07 — Sorting

## Algorithm comparison

| Algorithm | Best | Average | Worst | Space | Stable | Notes |
|---|---|---|---|---|---|---|
| Bubble | O(n) | O(n²) | O(n²) | O(1) | ✅ | swap adjacent; early exit if no swaps |
| Selection | O(n²) | O(n²) | O(n²) | O(1) | ❌ | min swaps (n−1) |
| Insertion | O(n) | O(n²) | O(n²) | O(1) | ✅ | great for nearly sorted / small n |
| Merge | O(n log n) | O(n log n) | O(n log n) | O(n) | ✅ | linked lists, external sort, inversions |
| Quick | O(n log n) | O(n log n) | O(n²) | O(log n) | ❌ | random pivot avoids worst case |
| Heap | O(n log n) | O(n log n) | O(n log n) | O(1) | ❌ | guaranteed, in-place |
| Counting | O(n+k) | O(n+k) | O(n+k) | O(k) | ✅ | small integer range k |
| Radix | O(d(n+b)) | | | O(n+b) | ✅ | fixed-width integers / strings |
| Bucket | O(n+k) | O(n+k) | O(n²) | O(n) | ✅ | uniform floats |

- **Stable** = equal keys keep original order (matters when sorting by multiple keys).
- Java: `Arrays.sort(int[])` → dual-pivot quicksort; `Arrays.sort(Object[])` / `Collections.sort` → TimSort (stable, merge + insertion).
- Comparison sorts have a lower bound of **Ω(n log n)**.

## Custom comparators
```java
Arrays.sort(people, (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(b[1], a[1]));
list.sort(Comparator.comparing(Person::getAge).thenComparing(Person::getName));
// LC 179 Largest number: sort strings by (b + a).compareTo(a + b)
```

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Implement all sorts | Easy→Med | see table | [SortingAlgorithms](SortingAlgorithms.java) |
| 2 | Merge sorted array (LC 88) | Easy | fill from the back | [SortingProblems](SortingProblems.java) |
| 3 | Count inversions | Hard | merge sort count | [SortingProblems](SortingProblems.java) |
| 4 | Reverse pairs (LC 493) | Hard | merge sort with two-pointer count | [SortingProblems](SortingProblems.java) |
| 5 | Kth largest element (LC 215) | Medium | quickselect O(n) avg / heap | [SortingProblems](SortingProblems.java) |
| 6 | Largest number (LC 179) | Medium | custom comparator | [SortingProblems](SortingProblems.java) |
| 7 | Sort list (LC 148) | Medium | merge sort on linked list | see 09 |
| 8 | Wiggle sort II (LC 324) | Medium | median + virtual indexing | _(todo)_ |
| 9 | Maximum gap (LC 164) | Hard | bucket / pigeonhole | _(todo)_ |
| 10 | Sort colors (LC 75) | Medium | Dutch flag | see 02 |
