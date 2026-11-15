# 04 — Hashing

## Notes
- **HashMap / HashSet**: average O(1) put/get/contains; worst O(n) (Java 8+ turns long chains into red-black trees → O(log n)).
- How it works: `index = hash(key) & (capacity - 1)`; collisions handled by chaining; resizes (×2) when size > capacity × 0.75 (load factor).
- Keys must implement `equals()` **and** `hashCode()` consistently. Mutable keys = bugs.
- `LinkedHashMap` keeps insertion order (and access order → LRU cache). `TreeMap` keeps keys sorted (O(log n)).

## Patterns
| Pattern | Example |
|---|---|
| Frequency counting | majority, top-K, anagram |
| Seen-set for duplicates | contains duplicate |
| Complement lookup | two sum |
| **Prefix sum + HashMap** | subarray sum = K, longest subarray with sum K, count of zero-sum subarrays |
| Index map | longest substring without repeat |
| Sequence heads | longest consecutive sequence |

### Prefix sum + map template
```java
Map<Integer,Integer> count = new HashMap<>();
count.put(0, 1);                 // empty prefix
int sum = 0, ans = 0;
for (int x : a) {
    sum += x;
    ans += count.getOrDefault(sum - k, 0);   // subarrays ending here with sum k
    count.merge(sum, 1, Integer::sum);
}
```

## Problems

| # | Problem | Level | Best approach | Solution |
|---|---|---|---|---|
| 1 | Contains duplicate (LC 217) | Easy | HashSet | [HashingBasics](HashingBasics.java) |
| 2 | Frequency of each element | Easy | HashMap | [HashingBasics](HashingBasics.java) |
| 3 | First unique character (LC 387) | Easy | int[26] two passes | [HashingBasics](HashingBasics.java) |
| 4 | Intersection of two arrays (LC 349/350) | Easy | set / count map | [HashingBasics](HashingBasics.java) |
| 5 | Subarray sum equals K (LC 560) | Medium | prefix sum + map | [SubarraySumK](SubarraySumK.java) |
| 6 | Longest subarray with sum K | Medium | prefix → first index map | [SubarraySumK](SubarraySumK.java) |
| 7 | Count subarrays with XOR K | Medium | prefix XOR + map | [SubarraySumK](SubarraySumK.java) |
| 8 | Contiguous array – equal 0s & 1s (LC 525) | Medium | 0 → -1, longest zero-sum | [SubarraySumK](SubarraySumK.java) |
| 9 | Longest consecutive sequence (LC 128) | Medium | set, start only at heads | [LongestConsecutiveSequence](LongestConsecutiveSequence.java) |
| 10 | Top K frequent elements (LC 347) | Medium | bucket sort / heap | see 13 |
| 11 | Group anagrams (LC 49) | Medium | count key | see 03 |
| 12 | Subarray sums divisible by K (LC 974) | Medium | prefix mod + map (fix negatives) | _(todo)_ |
| 13 | 4Sum II (LC 454) | Medium | pair sums in map | _(todo)_ |
| 14 | Design HashMap (LC 706) | Easy | array of buckets with chaining | [MyHashMap](MyHashMap.java) |
| 15 | LRU cache (LC 146) | Medium | HashMap + doubly linked list | see 09 |
