# 10 — Stack & Queue

## Notes
- **Stack** LIFO: push/pop/peek O(1). Use `Deque<Integer> st = new ArrayDeque<>()` (not legacy `Stack`).
- **Queue** FIFO: offer/poll/peek O(1). `ArrayDeque` or `LinkedList`.
- **Deque**: both ends O(1) → sliding window max/min.
- **Priority queue** is a heap, not a queue (see 13).

## Monotonic stack (most important pattern here)
Keep the stack **increasing** or **decreasing**; each element is pushed and popped once → **O(n)**.

| Want | Stack order (bottom→top) | Pop while |
|---|---|---|
| Next greater element | decreasing | `a[top] < a[i]` |
| Next smaller element | increasing | `a[top] > a[i]` |
| Previous greater | decreasing | `a[top] <= a[i]` |
| Previous smaller | increasing | `a[top] >= a[i]` |

```java
int[] nge = new int[n]; Arrays.fill(nge, -1);
Deque<Integer> st = new ArrayDeque<>();     // indices
for (int i = 0; i < n; i++) {
    while (!st.isEmpty() && a[st.peek()] < a[i]) nge[st.pop()] = a[i];
    st.push(i);
}
```
Circular array → loop `i` from 0 to `2n - 1`, use `i % n`.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Implement stack using array / linked list | Easy | top pointer | [StackQueueDesign](StackQueueDesign.java) |
| 2 | Queue using stacks (LC 232) | Easy | in/out stacks, amortised O(1) | [StackQueueDesign](StackQueueDesign.java) |
| 3 | Stack using queues (LC 225) | Easy | rotate after push | [StackQueueDesign](StackQueueDesign.java) |
| 4 | Design circular queue (LC 622) | Medium | array + head + size | [StackQueueDesign](StackQueueDesign.java) |
| 5 | Min stack (LC 155) | Medium | pair stack / encoded value | [StackQueueDesign](StackQueueDesign.java) |
| 6 | Valid parentheses (LC 20) | Easy | push expected closer | [ExpressionProblems](ExpressionProblems.java) |
| 7 | Evaluate reverse Polish notation (LC 150) | Medium | operand stack | [ExpressionProblems](ExpressionProblems.java) |
| 8 | Infix → postfix | Medium | operator precedence stack | [ExpressionProblems](ExpressionProblems.java) |
| 9 | Basic calculator II (LC 227) | Medium | apply * / immediately | [ExpressionProblems](ExpressionProblems.java) |
| 10 | Basic calculator (LC 224) | Hard | sign stack for parentheses | [ExpressionProblems](ExpressionProblems.java) |
| 11 | Decode string (LC 394) | Medium | count + string stacks | [ExpressionProblems](ExpressionProblems.java) |
| 12 | Next greater element I/II (LC 496/503) | Easy/Med | monotonic stack, circular | [MonotonicStack](MonotonicStack.java) |
| 13 | Daily temperatures (LC 739) | Medium | NGE distance | [MonotonicStack](MonotonicStack.java) |
| 14 | Online stock span (LC 901) | Medium | previous greater | [MonotonicStack](MonotonicStack.java) |
| 15 | Largest rectangle in histogram (LC 84) | Hard | prev/next smaller | [MonotonicStack](MonotonicStack.java) |
| 16 | Maximal rectangle (LC 85) | Hard | histogram per row | [MonotonicStack](MonotonicStack.java) |
| 17 | Sum of subarray minimums (LC 907) | Medium | contribution technique | [MonotonicStack](MonotonicStack.java) |
| 18 | Asteroid collision (LC 735) | Medium | stack simulation | [MonotonicStack](MonotonicStack.java) |
| 19 | Remove K digits (LC 402) | Medium | increasing stack, greedy | [MonotonicStack](MonotonicStack.java) |
| 20 | Sliding window maximum (LC 239) | Hard | monotonic deque | [SlidingWindowMaximum](SlidingWindowMaximum.java) |
| 21 | Celebrity problem | Medium | stack / two pointers elimination | [SlidingWindowMaximum](SlidingWindowMaximum.java) |
| 22 | Trapping rain water (LC 42) | Hard | stack | see 05 |
| 23 | Sum of subarray ranges (LC 2104) | Medium | max contribution − min contribution | [StackProblemsII](StackProblemsII.java) |
| 24 | Simplify path (LC 71) | Medium | stack of dirs | [StackProblemsII](StackProblemsII.java) |
| 25 | Remove all adjacent duplicates in string II (LC 1209) | Medium | stack of (char, count) | [StackProblemsII](StackProblemsII.java) |
| 26 | Car fleet (LC 853) | Medium | sort by position, stack of arrival times | [StackProblemsII](StackProblemsII.java) |
| 27 | Design hit counter (LC 362) | Medium | queue / circular buckets | [StackProblemsII](StackProblemsII.java) |
| 28 | Longest valid parentheses (LC 32) | Hard | stack of indices / two-pass counters | [StackProblemsII](StackProblemsII.java) |
| 29 | Maximum frequency stack (LC 895) | Hard | freq map + stack per frequency | [StackProblemsII](StackProblemsII.java) |
