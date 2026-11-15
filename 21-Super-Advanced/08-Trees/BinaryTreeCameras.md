# LC 968 — Binary Tree Cameras (Hard)

**Pattern:** tree DP with states, or the equivalent greedy post-order · **Target:** O(n) · **Code:** [BinaryTreeCameras.java](BinaryTreeCameras.java)

## Problem
A camera at a node covers itself, its parent and its children. Return the minimum number of cameras so every node is covered.

`[0,0,null,0,0] → 1` (camera on the root's left child)

## Why engineers fear it
It's minimum dominating set on a tree. The greedy works but needs a justification, and the DP needs three carefully defined states.

## Approach 1 — Brute force over subsets (oracle)
Try every subset of nodes as camera positions (2ⁿ), check coverage, keep the smallest. Tiny trees only.

## Approach 2 — Tree DP with three states (provable)
For each node return a triple, each value = minimum cameras in its subtree:
- `a` = camera **at** this node (subtree fully covered),
- `b` = no camera here, but this node is covered by a child (subtree fully covered),
- `c` = no camera here, node **not** covered yet (children's subtrees are covered; parent must cover it).
```
a = 1 + min(L.a, L.b, L.c) + min(R.a, R.b, R.c)
b = min(L.a + min(R.a, R.b), R.a + min(L.a, L.b))
c = L.b + R.b
null child → (a = ∞, b = 0, c = ∞)  # "covered, no camera" is the only state that adds nothing
```
Answer at the root: `min(a, b)` (it has no parent to cover it). **O(n)**.

## Approach 3 — Greedy post-order (optimal and short)
Return a state for each node: `0` = needs coverage, `1` = has camera, `2` = covered (no camera).
```
dfs(null) = 2
l, r = dfs(left), dfs(right)
if l == 0 or r == 0: cameras++; return 1    # a child needs us
if l == 1 or r == 1: return 2               # covered by a child
return 0                                    # leave it to the parent
answer = cameras + (dfs(root) == 0 ? 1 : 0)
```
**Why greedy is optimal:** consider the deepest uncovered leaf; putting the camera on its **parent** covers everything the leaf's camera would cover (the leaf, its parent) plus more (siblings, grandparent). So never put cameras on leaves — always as high as possible. Post-order applies this bottom-up.
**O(n)** time, **O(h)** stack.

## Pitfalls
- `null` must count as covered (state 2), otherwise leaves would demand cameras on non-existent children.
- Root left as "needs coverage" → add one camera.
- DP: `b` must have at least one child with a camera.

## Follow-ups
- **House robber III** (LC 337) — two-state tree DP (take / skip).
- **Minimum vertex cover / maximum independent set on trees** — same DP style.
- **Distribute coins in binary tree** (LC 979) — post-order flows.

## 30-second interview script
"Post-order, each node reports whether it needs coverage, has a camera, or is covered. If any child needs coverage, I put a camera here; if a child has a camera, I'm covered; otherwise I ask my parent. Nulls count as covered. Greedy is optimal because a camera on a leaf's parent dominates a camera on the leaf. If the root ends up uncovered, add one. The three-state DP proves the same result."
