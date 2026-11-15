# LC 124 — Binary Tree Maximum Path Sum (Hard)

**Pattern:** post-order DFS that returns one thing (a downward "gain") and records another (a path through the node) · **Target:** O(n) · **Code:** [BinaryTreeMaximumPathSum.java](BinaryTreeMaximumPathSum.java)

## Problem
A path is any sequence of adjacent nodes, each used at most once; it doesn't need to pass through the root. Return the maximum sum of node values over all non-empty paths. Values may be negative.

`[-10,9,20,null,null,15,7] → 42` (15 → 20 → 7)

## Why engineers fear it
The value you need to **return** to the parent (a path that can still be extended upward — one branch only) is different from the value you need to **record** (a path that bends at this node — both branches). Mixing them up is the classic bug.

## Approach 1 — Brute force: every node as the "top" of the path
For each node, compute the best downward path in its left and right subtrees separately (O(n) each) and combine. **O(n²)**.

## Approach 2 — Single post-order DFS (optimal)
```
gain(node):                       # best sum of a path starting at node going DOWN (one side)
    if node is null: return 0
    L = max(0, gain(node.left))   # drop negative branches
    R = max(0, gain(node.right))
    best = max(best, node.val + L + R)      # path that bends here (uses both sides)
    return node.val + max(L, R)             # parent can extend only one side
```
**O(n)** time, **O(h)** recursion stack.

### Why `max(0, …)`
A path may stop anywhere, so a negative branch should simply be excluded. If **all** values are negative, `best` still ends up as the largest single node because `node.val + 0 + 0` is considered at every node.

## Approach 3 — Iterative post-order (no recursion)
For skewed trees with 10⁵ nodes, Java recursion can overflow. Do a post-order with an explicit stack (or reverse of a preorder), storing `gain` in a map/array, and apply the same two formulas. **O(n)** time and space.

## Dry run — `[-10, 9, 20, null, null, 15, 7]`
- gain(15) = 15, gain(7) = 7 → at 20: best = 20 + 15 + 7 = **42**, return 20 + 15 = 35
- gain(9) = 9
- at −10: best = max(42, −10 + 9 + 35 = 34) = 42, return 25
Answer **42**.

## Pitfalls
- Initialising `best = 0` (wrong for all-negative trees) → use `Integer.MIN_VALUE` or the root value.
- Returning `node.val + L + R` to the parent (that would create a forked "path").
- Overflow is not an issue in LC limits, but use `long` for larger values.

## Follow-ups
- **Diameter of binary tree** (LC 543) — same pattern with edge counts.
- **Longest univalue path** (LC 687) — same pattern with a value constraint.
- **Maximum path sum between two leaves** (GFG) — record only when both children exist.
- Max path sum in a general tree/graph without cycles — take the top two child gains.

## 30-second interview script
"One post-order pass. Each node returns its best downward gain — its value plus the better child gain, ignoring negative children — because a parent can only extend one side. At the node itself I also consider the path bending through it, value plus both child gains, and keep the global maximum. O(n) time, O(h) stack."
