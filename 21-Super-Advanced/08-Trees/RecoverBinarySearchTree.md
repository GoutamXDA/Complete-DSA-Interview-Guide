# LC 99 — Recover Binary Search Tree (Hard follow-up: O(1) space)

**Pattern:** inorder violations; Morris traversal for constant space · **Target:** O(n) time, O(1) space · **Code:** [RecoverBinarySearchTree.java](RecoverBinarySearchTree.java)

## Problem
Exactly two nodes of a BST had their values swapped by mistake. Fix the tree **without changing its structure**. Follow-up: use O(1) extra space.

`[1,3,null,null,2] → [3,1,null,null,2]`

## Why engineers fear it
The O(n)-space solution is easy; the follow-up demands **Morris traversal**, which temporarily rewires the tree — most people have never written it under pressure.

## Key insight
The inorder sequence of a BST is sorted. Swapping two values creates either:
- **one** inversion (adjacent nodes swapped): `… 3, 2 …` → swap those two;
- **two** inversions (non-adjacent): `1, [6], 3, 4, 5, [2], 7` → first inversion's **larger** element (6) and second inversion's **smaller** element (2).
Rule: `first = prev` at the first inversion; `second = cur` at **every** inversion (the last one wins).

## Approach 1 — Inorder to array, sort, write back
Collect node references in inorder, collect values, sort the values, assign them back in order. **O(n log n)** time, O(n) space. Simple and correct.

## Approach 2 — Inorder with prev pointer (recursive or stack)
Track `prev` during inorder; apply the inversion rule; swap `first.val` and `second.val` at the end. **O(n)** time, **O(h)** space.

## Approach 3 — Morris inorder (optimal, O(1) space)
Morris traversal visits nodes in inorder using the empty right pointers of inorder predecessors as temporary "threads":
```
cur = root
while cur:
    if cur.left is null: visit(cur); cur = cur.right
    else:
        pre = rightmost node of cur.left (stop if pre.right == cur)
        if pre.right is null: pre.right = cur; cur = cur.left        # create thread
        else: pre.right = null; visit(cur); cur = cur.right          # remove thread
```
Apply the same inversion rule inside `visit`. Every edge is walked at most a constant number of times → **O(n)** time, **O(1)** space, and the tree is restored by the end.

## Dry run — inorder `[1, 6, 3, 4, 5, 2, 7]`
- (6, 3): first inversion → first = 6, second = 3
- (5, 2): second inversion → second = 2
Swap 6 and 2 → `[1, 2, 3, 4, 5, 6, 7]`.

## Pitfalls
- Setting `second` only once (fails for the two-inversion case) or only at the second inversion (fails for adjacent swaps).
- Breaking out of the Morris loop early → threads left in the tree (corrupt structure).
- Swap **values**, not nodes.

## Follow-ups
- **Validate BST** (LC 98) — same inorder check.
- **Kth smallest in BST** with O(1) space — Morris again.
- **Sort an array with exactly one swap** — same two-inversion rule on arrays.

## 30-second interview script
"Inorder of a BST is sorted, so the swap shows up as one or two inversions. The first wrong node is the larger element of the first inversion and the second is the smaller element of the last inversion; swapping their values fixes the tree. For O(1) space I do the inorder with Morris traversal, threading each node's predecessor to it and removing the thread on the second visit."
