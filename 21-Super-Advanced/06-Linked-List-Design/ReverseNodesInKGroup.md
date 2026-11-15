# LC 25 — Reverse Nodes in k-Group (Hard)

**Pattern:** in-place pointer surgery with a dummy head and group boundaries · **Target:** O(n) time, O(1) space · **Code:** [ReverseNodesInKGroup.java](ReverseNodesInKGroup.java)

## Problem
Reverse the nodes of a linked list `k` at a time; a final group with fewer than `k` nodes stays as is. Only pointers may change (not values).

`1→2→3→4→5, k=2 → 2→1→4→3→5` · `k=3 → 3→2→1→4→5`

## Why engineers fear it
Four pointers (previous group's tail, group start, group end, next group's head) must be rewired in the right order with no off-by-one — and the last partial group must be left untouched.

## Approach 1 — Values into an array (not allowed, but a good oracle)
Copy values, reverse each full chunk, write back. O(n) time, O(n) space.

## Approach 2 — Stack per group
Push k nodes; if the stack has k nodes, pop them to relink in reverse order; otherwise attach the remainder unchanged. O(n) time, **O(k)** space.

## Approach 3 — Recursion
Check that k nodes exist; reverse them; `head.next = reverseKGroup(nextGroupHead, k)`; return the new head. O(n) time, **O(n/k)** recursion stack. Clean to explain.

## Approach 4 — Iterative in place (optimal)
```
dummy → head; groupPrev = dummy
loop:
    kth = walk k steps from groupPrev; if null → break      # fewer than k left
    groupNext = kth.next
    reverse nodes from groupPrev.next up to kth, with prev starting at groupNext
    first = groupPrev.next                                    # becomes the group's tail
    groupPrev.next = kth                                      # connect previous part to new head
    groupPrev = first                                         # advance
```
Initialising `prev = groupNext` while reversing means the reversed group's tail already points to the next group — no extra fix-up.
**O(n)** time (each node visited twice), **O(1)** space.

## Dry run — 1→2→3→4→5, k=3
- groupPrev = dummy, kth = 3, groupNext = 4. Reverse 1,2,3 with prev=4 → 3→2→1→4. dummy→3. groupPrev = 1.
- kth from 1: 4, 5, null → fewer than 3 → stop. Result **3→2→1→4→5**.

## Pitfalls
- Reversing before checking that k nodes remain (breaks the "leave the tail" rule).
- Losing `groupNext` before reversing.
- Forgetting to move `groupPrev` to the old group start (the new tail).

## Follow-ups
- **Reverse linked list II** (LC 92) — reverse one sublist m..n.
- **Swap nodes in pairs** (LC 24) — k = 2.
- **Reverse alternate k-groups**, or **reverse the last partial group too** — tiny changes to the loop.

## 30-second interview script
"I use a dummy head and process one group at a time: first walk k nodes to make sure the group exists and remember the node after it. Then I reverse the group in place, starting prev at that next node so the reversed tail is already connected, hook the previous group's tail to the new head, and move on. O(n) time, O(1) space."
