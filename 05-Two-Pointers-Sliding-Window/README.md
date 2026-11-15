# 05 — Two Pointers & Sliding Window

## Two pointers
| Variant | When | Example |
|---|---|---|
| Opposite ends (l=0, r=n-1) | sorted array, pair sums, palindromes, container | 2Sum II, 3Sum, container with most water |
| Same direction (slow/fast) | in-place filtering, dedupe | remove duplicates, move zeroes |
| Fast/slow on linked list | cycle, middle | see 09 |

## Sliding window
Use for **contiguous** subarray/substring problems where you can grow the right edge and shrink the left edge **monotonically** (works when adding elements only makes the window "more invalid" — e.g., non-negative numbers, distinct counts).

### Fixed-size window template
```java
for (int r = 0; r < n; r++) {
    add(a[r]);
    if (r >= k) remove(a[r - k]);
    if (r >= k - 1) record answer;
}
```

### Variable-size window template (longest valid)
```java
int l = 0;
for (int r = 0; r < n; r++) {
    add(a[r]);
    while (window invalid) remove(a[l++]);
    best = max(best, r - l + 1);
}
```

### Variable-size (shortest valid)
```java
for (int r = 0; r < n; r++) {
    add(a[r]);
    while (window valid) { best = min(best, r - l + 1); remove(a[l++]); }
}
```

### "Exactly K" trick
`count(exactly K) = count(at most K) − count(at most K−1)` — e.g., subarrays with K distinct integers (LC 992), binary subarrays with sum (LC 930).

> With negative numbers, sliding window breaks → use prefix sum + HashMap (see 04).

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Two Sum II – sorted (LC 167) | Medium | opposite pointers | [ThreeSum](ThreeSum.java) |
| 2 | 3Sum (LC 15) | Medium | sort + fix one + 2 pointers, skip dupes | [ThreeSum](ThreeSum.java) |
| 3 | 3Sum closest (LC 16) | Medium | same, track closest | [ThreeSum](ThreeSum.java) |
| 4 | 4Sum (LC 18) | Medium | two loops + 2 pointers, use long | [ThreeSum](ThreeSum.java) |
| 5 | Container with most water (LC 11) | Medium | move the shorter side | [ContainerWithMostWater](ContainerWithMostWater.java) |
| 6 | Trapping rain water (LC 42) | Hard | prefix max → two pointers | [TrappingRainWater](TrappingRainWater.java) |
| 7 | Max sum subarray of size K | Easy | fixed window | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 8 | Longest substring without repeating (LC 3) | Medium | variable window + last index | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 9 | Longest repeating character replacement (LC 424) | Medium | window − maxFreq ≤ k | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 10 | Max consecutive ones III (LC 1004) | Medium | window with ≤ k zeros | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 11 | Subarrays with K different integers (LC 992) | Hard | atMost(K) − atMost(K−1) | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 12 | Minimum size subarray sum (LC 209) | Medium | shortest valid window | [SlidingWindowBasics](SlidingWindowBasics.java) |
| 13 | Minimum window substring (LC 76) | Hard | need-count + shrink | [MinWindowSubstring](MinWindowSubstring.java) |
| 14 | Permutation in string (LC 567) | Medium | fixed window counts | see 03 Anagrams |
| 15 | Sliding window maximum (LC 239) | Hard | monotonic deque | see 10 |
| 16 | Fruit into baskets (LC 904) | Medium | ≤ 2 distinct window | [WindowProblemsII](WindowProblemsII.java) |
| 17 | Number of substrings containing all three chars (LC 1358) | Medium | last-seen indices | [WindowProblemsII](WindowProblemsII.java) |
| 18 | Count nice subarrays (LC 1248) | Medium | atMost trick | [WindowProblemsII](WindowProblemsII.java) |
| 19 | Boats to save people (LC 881) | Medium | sort, pair heaviest with lightest | [WindowProblemsII](WindowProblemsII.java) |
| 20 | Maximum points you can obtain from cards (LC 1423) | Medium | take k from ends = total − min window of n−k | [WindowProblemsII](WindowProblemsII.java) |
| 21 | Longest substring with at most K distinct chars (LC 340) | Medium | window + count map | [WindowProblemsII](WindowProblemsII.java) |
| 22 | Binary subarrays with sum (LC 930) | Medium | atMost(goal) − atMost(goal−1) | [WindowProblemsII](WindowProblemsII.java) |
