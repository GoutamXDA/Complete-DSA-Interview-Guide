# 11 — Binary Trees

## Notes
```java
class TreeNode { int val; TreeNode left, right; }
```
- **Height** = edges/nodes on the longest root-to-leaf path. Balanced tree height = O(log n); skewed = O(n).
- **Full**: every node 0 or 2 children. **Complete**: all levels full except last, filled left to right (heaps). **Perfect**: all leaves same level, 2^h − 1 nodes.
- Recursion space = O(h).

## Traversals
| Order | Sequence | Use |
|---|---|---|
| Preorder | root, L, R | copy/serialize tree |
| Inorder | L, root, R | BST → sorted order |
| Postorder | L, R, root | delete tree, compute heights bottom-up |
| Level order (BFS) | level by level with a queue | views, min depth, zigzag |
| Morris | inorder in O(1) space via threads | follow-up question |

## Two ways to think recursively
1. **Top-down** (pass info down as parameters) — e.g., max depth with `depth+1`, path sums.
2. **Bottom-up** (return info up) — e.g., height, diameter, balanced check, max path sum. Combine children's answers at the parent.

```java
int dfs(TreeNode n) {          // bottom-up template
    if (n == null) return BASE;
    int l = dfs(n.left), r = dfs(n.right);
    global = best(global, combine(l, r, n));   // answer passing THROUGH n
    return extend(l, r, n);                    // answer passed UP to the parent (one branch only)
}
```

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Pre/In/Post order (LC 144/94/145) | Easy | recursive + iterative stack | [TreeTraversals](TreeTraversals.java) |
| 2 | Morris inorder traversal | Medium | threaded tree, O(1) space | [TreeTraversals](TreeTraversals.java) |
| 3 | Level order (LC 102) | Medium | queue, size per level | [TreeTraversals](TreeTraversals.java) |
| 4 | Zigzag level order (LC 103) | Medium | alternate insertion side | [TreeTraversals](TreeTraversals.java) |
| 5 | Max depth (LC 104) / Min depth (LC 111) | Easy | DFS / BFS | [TreeProperties](TreeProperties.java) |
| 6 | Balanced binary tree (LC 110) | Easy | height with -1 sentinel | [TreeProperties](TreeProperties.java) |
| 7 | Diameter (LC 543) | Easy | l + r at each node | [TreeProperties](TreeProperties.java) |
| 8 | Maximum path sum (LC 124) | Hard | max(0, child) gains | [TreeProperties](TreeProperties.java) |
| 9 | Same tree / Symmetric / Invert (LC 100/101/226) | Easy | paired recursion | [TreeProperties](TreeProperties.java) |
| 10 | Path sum II (LC 113) | Medium | backtracking path | [TreeProperties](TreeProperties.java) |
| 11 | Right/left side view (LC 199) | Medium | last per level / DFS depth | [TreeViews](TreeViews.java) |
| 12 | Top view / bottom view | Medium | BFS with horizontal distance | [TreeViews](TreeViews.java) |
| 13 | Vertical order traversal (LC 987) | Hard | (col,row,val) sort | [TreeViews](TreeViews.java) |
| 14 | Boundary traversal | Medium | left edge + leaves + reversed right edge | [TreeViews](TreeViews.java) |
| 15 | Maximum width (LC 662) | Medium | index positions per level | [TreeViews](TreeViews.java) |
| 16 | Lowest common ancestor (LC 236) | Medium | return node if found in both sides | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 17 | Construct from preorder + inorder (LC 105) | Medium | index map, recursive split | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 18 | Construct from inorder + postorder (LC 106) | Medium | same, from the back | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 19 | Serialize & deserialize (LC 297) | Hard | preorder with null markers | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 20 | All nodes distance K (LC 863) | Medium | parent map + BFS | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 21 | Count complete tree nodes (LC 222) | Medium | compare left/right heights O(log² n) | [TreeLCAConstruct](TreeLCAConstruct.java) |
| 22 | Flatten to linked list (LC 114) | Medium | reverse postorder / Morris-like | [TreeProblemsII](TreeProblemsII.java) |
| 23 | Children sum property | Medium | push values down, fix up | [TreeProblemsII](TreeProblemsII.java) |
| 24 | Time to burn tree (LC 2385) | Medium | parent map + BFS | [TreeProblemsII](TreeProblemsII.java) |
| 25 | Binary tree paths (LC 257) | Easy | DFS with path string | [TreeProblemsII](TreeProblemsII.java) |
| 26 | Subtree of another tree (LC 572) | Easy | isSame at every node / serialize + KMP | [TreeProblemsII](TreeProblemsII.java) |
| 27 | Sum root to leaf numbers (LC 129) | Medium | pass `cur*10 + val` down | [TreeProblemsII](TreeProblemsII.java) |
| 28 | Count good nodes (LC 1448) | Medium | pass max-so-far down | [TreeProblemsII](TreeProblemsII.java) |
| 29 | Path sum III (LC 437) | Medium | prefix sum + map along the path | [TreeProblemsII](TreeProblemsII.java) |
| 30 | Populating next right pointers (LC 116) | Medium | use already-built next pointers, O(1) space | [TreeProblemsII](TreeProblemsII.java) |
| 31 | Construct from preorder and postorder (LC 889) | Medium | next preorder element = left child root | [TreeProblemsII](TreeProblemsII.java) |
| 32 | Binary tree cameras (LC 968) | Hard | greedy postorder with 3 states | [TreeProblemsII](TreeProblemsII.java) |
