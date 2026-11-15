# LC 1373 — Maximum Sum BST in Binary Tree (Hard)

**Pattern:** post-order returning a summary tuple `(isBST, min, max, sum)` · **Target:** O(n) · **Code:** [MaximumSumBSTInBinaryTree.java](MaximumSumBSTInBinaryTree.java)

## Problem
Return the maximum sum of keys of any **subtree** that is a valid BST (an empty subtree counts as sum 0, so the answer is ≥ 0).

`[1,4,3,2,4,2,5,null,null,null,null,null,null,4,6] → 20` (subtree rooted at 3)

## Why engineers fear it
Validating each subtree independently is O(n²). You need a single bottom-up pass that passes enough information for the parent to decide its own validity — and the sentinels for empty subtrees must be chosen carefully.

## Approach 1 — Validate every subtree
For each node: run an O(size) BST check with bounds and sum its subtree. **O(n²)** worst case (skewed tree).

## Approach 2 — Post-order summary (optimal)
Each call returns `(isBST, min, max, sum)` for its subtree:
```
null → (true, +∞, −∞, 0)                  # neutral: any parent value fits
node:
    L = f(left), R = f(right)
    if L.isBST and R.isBST and L.max < node.val < R.min:
        sum = L.sum + R.sum + node.val
        best = max(best, sum)
        return (true, min(L.min, node.val), max(R.max, node.val), sum)
    return (false, ...)                    # values don't matter any more
```
Every node is visited once → **O(n)** time, **O(h)** stack.

### Why the null sentinel is (+∞, −∞)
For a node with no left child, the check `L.max < node.val` must pass → `L.max = −∞`. And the subtree's min must become `node.val` → `min(+∞, node.val)`.

## Pitfalls
- Strict inequalities (`<`, not `≤`) — BST keys are distinct in this problem.
- Answer ≥ 0: if all BST sums are negative, return 0 (empty BST).
- Use `Integer.MIN_VALUE/MAX_VALUE` carefully when node values can equal them — `long` sentinels are safer.

## Follow-ups
- **Largest BST subtree** by size (LC 333) — same tuple with `size` instead of `sum`.
- **Validate BST** (LC 98) — bounds passed **down** instead of summaries passed up.
- **Count univalue subtrees** (LC 250) — same post-order "summary" pattern.

## 30-second interview script
"One post-order pass where each subtree reports whether it's a BST, its min, max and sum. A node forms a BST if both children do and left.max < val < right.min; then its sum is the children's sums plus its value and I update the best. Empty subtrees report min +∞ and max −∞ so they never block the check. O(n)."
