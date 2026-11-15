# 09 — Linked List

## Notes
```java
class ListNode { int val; ListNode next; }
```
- O(1) insert/delete **given the node** (or its predecessor); O(n) access by index.
- Doubly linked list: `prev` + `next` → O(1) removal of any node (LRU cache).
- Java `LinkedList` is doubly linked; for stacks/queues prefer `ArrayDeque`.

## Must-know techniques
| Technique | Use |
|---|---|
| **Dummy (sentinel) head** | avoids special-casing the head on insert/delete/merge |
| **Fast & slow pointers** | middle, cycle detection (Floyd), cycle start, palindrome |
| **Reverse in place** | prev/cur/next — iterative O(1) space |
| **Gap of n** | remove nth from end: move fast n steps first |
| **Two-list switch** | intersection: walk A then B, B then A |
| **Merge** | merge two sorted, merge sort on list |

### Floyd's cycle — why the start is found
If slow and fast meet, reset one pointer to head; moving both 1 step, they meet at the cycle start, because `distance(head → start) ≡ distance(meet → start) (mod cycle length)`.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Design linked list (LC 707) | Medium | dummy head + size | [LinkedListBasics](LinkedListBasics.java) |
| 2 | Reverse linked list (LC 206) | Easy | iterative / recursive / stack | [LinkedListBasics](LinkedListBasics.java) |
| 3 | Middle of the list (LC 876) | Easy | count / fast-slow | [LinkedListBasics](LinkedListBasics.java) |
| 4 | Remove Nth node from end (LC 19) | Medium | gap of n with dummy | [LinkedListBasics](LinkedListBasics.java) |
| 5 | Delete node given only that node (LC 237) | Medium | copy next into it | [LinkedListBasics](LinkedListBasics.java) |
| 6 | Linked list cycle (LC 141) | Easy | HashSet / Floyd | [CycleProblems](CycleProblems.java) |
| 7 | Cycle start (LC 142) | Medium | Floyd + reset | [CycleProblems](CycleProblems.java) |
| 8 | Length of cycle | Medium | after meeting, loop once | [CycleProblems](CycleProblems.java) |
| 9 | Intersection of two lists (LC 160) | Easy | set / length diff / switch heads | [CycleProblems](CycleProblems.java) |
| 10 | Merge two sorted lists (LC 21) | Easy | dummy + compare | [MergeSortList](MergeSortList.java) |
| 11 | Sort list (LC 148) | Medium | merge sort (split at middle) | [MergeSortList](MergeSortList.java) |
| 12 | Palindrome linked list (LC 234) | Easy | reverse second half | [MergeSortList](MergeSortList.java) |
| 13 | Add two numbers (LC 2) | Medium | carry digit by digit | [MergeSortList](MergeSortList.java) |
| 14 | Odd even list (LC 328) | Medium | two chains | [MergeSortList](MergeSortList.java) |
| 15 | Reverse nodes in k-group (LC 25) | Hard | reverse k, link groups | [ReverseKGroup](ReverseKGroup.java) |
| 16 | Rotate list (LC 61) | Medium | make ring, cut at n − k | [ReverseKGroup](ReverseKGroup.java) |
| 17 | Copy list with random pointer (LC 138) | Medium | HashMap / interleave | [ReverseKGroup](ReverseKGroup.java) |
| 18 | LRU cache (LC 146) | Medium | HashMap + DLL | [LRUCache](LRUCache.java) |
| 19 | Merge K sorted lists (LC 23) | Hard | min-heap | see 13 |
| 20 | Flatten a multilevel doubly linked list (LC 430) | Medium | DFS / stack | [LinkedListProblemsIII](LinkedListProblemsIII.java) |
| 21 | Reverse linked list II (LC 92) | Medium | reverse sublist with dummy | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 22 | LFU cache (LC 460) | Hard | freq → DLL map | see 19 |
| 23 | Swap nodes in pairs (LC 24) | Medium | dummy + rewire 3 pointers | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 24 | Remove duplicates from sorted list II (LC 82) | Medium | dummy, skip whole duplicate runs | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 25 | Partition list (LC 86) | Medium | two chains < x and ≥ x | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 26 | Delete the middle node (LC 2095) | Medium | fast/slow with prev | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 27 | Reorder list (LC 143) | Medium | middle + reverse second half + merge | [LinkedListProblemsII](LinkedListProblemsII.java) |
| 28 | Add one to a number in a linked list (LC 369) | Medium | reverse / recursion carry | [LinkedListProblemsIII](LinkedListProblemsIII.java) |
| 29 | Sort a linked list of 0s, 1s and 2s (GFG) | Medium | three dummy chains | [LinkedListProblemsIII](LinkedListProblemsIII.java) |
| 30 | Design browser history (LC 1472) | Medium | doubly linked list / array + pointer | [LinkedListProblemsIII](LinkedListProblemsIII.java) |
| 31 | Flatten a linked list (bottom pointers) (GFG) | Hard | merge sorted lists pairwise | [LinkedListProblemsIII](LinkedListProblemsIII.java) |
