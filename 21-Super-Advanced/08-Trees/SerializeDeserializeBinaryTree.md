# LC 297 — Serialize and Deserialize Binary Tree (Hard)

**Pattern:** traversal with explicit null markers (preorder DFS or level-order BFS) · **Target:** O(n) · **Code:** [SerializeDeserializeBinaryTree.java](SerializeDeserializeBinaryTree.java)

## Problem
Design `serialize(root) → String` and `deserialize(String) → root` so that `deserialize(serialize(t))` reproduces the exact tree (any binary tree, values may repeat or be negative).

## Why engineers fear it
It's open-ended design: you must pick a format, prove it's **unambiguous**, and parse it back without off-by-one errors.

## Why a single traversal without nulls fails
Preorder `1,2,3` could be a left chain or a root with two children. Inorder + preorder works only with **distinct** values. Null markers fix this for any tree.

## Approach 1 — Preorder DFS with null markers (classic)
`serialize`: `val,` for a node, `#,` for null, in preorder.
`deserialize`: read tokens in order; a recursive builder takes the next token — `#` → null, otherwise create a node and build its left then right subtree.
**Why it's unambiguous:** in preorder with nulls, each subtree is a contiguous block that ends exactly when its last null is read, so the recursion consumes tokens deterministically.
**O(n)** time and space (n nodes produce 2n + 1 tokens).

## Approach 2 — Level-order BFS (LeetCode's format)
Write nodes level by level, `null` for missing children; trim trailing nulls. Deserialize with a queue: each dequeued node takes the next two tokens as its children. **O(n)**. Iterative → no stack overflow on skewed trees.

## Approach 3 — Compact binary/length encoding (follow-up)
For BSTs, preorder **without** nulls is enough (LC 449) because the BST property determines the structure; rebuild with value bounds in O(n). For general trees, you can encode each node as `(value, 2-bit child mask)` to save space.

## Pitfalls
- Negative numbers and multi-digit values → split on a delimiter, don't read char by char.
- Use an index/iterator into the token array, not repeated `substring` (O(n²)).
- Deep skewed trees (10⁴+) → recursion depth; BFS avoids it.

## Follow-ups
- **Serialize and deserialize BST** (LC 449) — no null markers needed.
- **Serialize N-ary tree** (LC 428) — write the child count after each value.
- **Find duplicate subtrees** (LC 652) — serialize each subtree as a key.
- **Subtree of another tree** (LC 572) — serialize + substring/KMP.

## 30-second interview script
"I serialize with a preorder traversal that writes a marker for every null child. That makes the encoding unambiguous: when deserializing, I read tokens in the same order, and a recursive builder returns null on a marker or creates the node and builds its left and right subtrees. Both directions are O(n). A level-order version with a queue avoids deep recursion."
