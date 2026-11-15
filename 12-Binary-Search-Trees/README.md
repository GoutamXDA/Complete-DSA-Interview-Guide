# 12 — Binary Search Trees

## Notes
- Invariant: **all** keys in left subtree < node < **all** keys in right subtree.
- **Inorder traversal = sorted order** — the single most useful BST fact.
- Search/insert/delete: O(h). Balanced (AVL, Red-Black → Java `TreeMap`) h = O(log n); skewed h = O(n).
- Use **range (min, max) bounds** when validating — checking only parent vs child is a classic bug.

### Delete a node — 3 cases
1. Leaf → remove.
2. One child → replace with child.
3. Two children → copy inorder successor (min of right subtree) into node, delete successor from right subtree.

### Floor / ceil
Walk from root: going right when `node.val <= key` records floor candidates; going left when `node.val >= key` records ceil candidates.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Search in BST (LC 700) | Easy | go left/right | [BSTOperations](BSTOperations.java) |
| 2 | Insert into BST (LC 701) | Medium | walk to null spot | [BSTOperations](BSTOperations.java) |
| 3 | Delete node in BST (LC 450) | Medium | 3 cases | [BSTOperations](BSTOperations.java) |
| 4 | Floor & ceil in BST | Medium | track candidate | [BSTOperations](BSTOperations.java) |
| 5 | Min / max in BST | Easy | leftmost / rightmost | [BSTOperations](BSTOperations.java) |
| 6 | Validate BST (LC 98) | Medium | bounds / inorder increasing | [BSTProblems](BSTProblems.java) |
| 7 | Kth smallest (LC 230) | Medium | inorder count / Morris | [BSTProblems](BSTProblems.java) |
| 8 | LCA of BST (LC 235) | Medium | split point | [BSTProblems](BSTProblems.java) |
| 9 | Convert sorted array to BST (LC 108) | Easy | middle as root | [BSTProblems](BSTProblems.java) |
| 10 | Construct BST from preorder (LC 1008) | Medium | upper bound recursion O(n) | [BSTProblems](BSTProblems.java) |
| 11 | Inorder successor | Medium | walk from root | [BSTProblems](BSTProblems.java) |
| 12 | BST iterator (LC 173) | Medium | controlled stack, O(h) space | [BSTProblems](BSTProblems.java) |
| 13 | Two sum IV in BST (LC 653) | Easy | two BST iterators (asc + desc) | [BSTProblems](BSTProblems.java) |
| 14 | Recover BST – two swapped nodes (LC 99) | Medium | inorder, find violations | [BSTProblems](BSTProblems.java) |
| 15 | Largest BST in binary tree | Hard | postorder returning (min, max, size) | [BSTProblems](BSTProblems.java) |
| 16 | Trim a BST (LC 669) | Medium | recursion on bounds | [BSTProblemsII](BSTProblemsII.java) |
| 17 | Balance a BST (LC 1382) | Medium | inorder → sorted array → build | [BSTProblemsII](BSTProblemsII.java) |
| 18 | Inorder predecessor (and successor together) (LC 285) | Medium | walk from root tracking candidate | [BSTProblemsII](BSTProblemsII.java) |
| 19 | Convert BST to greater tree (LC 538) | Medium | reverse inorder with running sum | [BSTProblemsII](BSTProblemsII.java) |
| 20 | Unique binary search trees (LC 96) | Medium | Catalan DP `dp[n] = Σ dp[i-1]*dp[n-i]` | [BSTProblemsII](BSTProblemsII.java) |
| 21 | Unique binary search trees II (LC 95) | Medium | recursive generation per root | [BSTProblemsII](BSTProblemsII.java) |
