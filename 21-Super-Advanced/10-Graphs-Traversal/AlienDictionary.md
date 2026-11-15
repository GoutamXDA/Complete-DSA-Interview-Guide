# LC 269 — Alien Dictionary (Hard)

**Pattern:** derive ordering constraints from adjacent pairs → topological sort with cycle detection · **Target:** O(C) (total characters) · **Code:** [AlienDictionary.java](AlienDictionary.java)

## Problem
`words` are sorted lexicographically in an unknown alphabet. Return any letter order consistent with it, or `""` if impossible.

`["wrt","wrf","er","ett","rftt"] → "wertf"` · `["z","x","z"] → ""` (cycle) · `["abc","ab"] → ""` (prefix violation)

## Why engineers fear it
Building the graph correctly is the hard part: only the **first** differing character of **adjacent** words gives information, letters that appear without constraints must still be output, and there's an easily missed invalid case.

## Building the graph
1. Every character that appears in any word is a node (even if it has no edges).
2. For each adjacent pair `(a, b)`: find the first index where they differ → edge `a[i] → b[i]`. Stop at the first difference (later characters say nothing).
3. **If no difference and `a` is longer than `b`** (`"abc"` before `"ab"`) → invalid → return `""`.

## Approach 1 — Brute force over permutations (oracle)
Try every ordering of the distinct letters and check whether the word list is sorted under it. **O(L! · C)** — only for ≤ 7 letters.

## Approach 2 — Kahn's algorithm (BFS topological sort)
Compute in-degrees; repeatedly output letters with in-degree 0. If the output has fewer letters than the node count → **cycle** → `""`. **O(C + V + E)** where C = total characters.

## Approach 3 — DFS topological sort with three colours
Post-order DFS; reverse the finish order. A back edge to a **grey** (in-progress) node means a cycle. Same complexity; often preferred when you also need the cycle itself.

## Why only adjacent pairs?
Lexicographic order is transitive: if `w1 < w2 < w3` then the constraints from `(w1, w3)` are implied by those from `(w1, w2)` and `(w2, w3)`. Comparing all pairs adds nothing but cost.

## Pitfalls
- Adding duplicate edges inflates in-degrees → use a set per node.
- Forgetting isolated letters → output is missing characters.
- The prefix case `["abc","ab"]`.
- Comparing beyond the first difference.

## Follow-ups
- **Verifying an alien dictionary** (LC 953) — given the order, check sortedness.
- **Course schedule II** (LC 210) — the topological sort itself.
- Return the **lexicographically smallest** valid order → Kahn with a min-heap instead of a queue.
- Is the order **unique**? → Kahn's queue must have exactly one element at every step.

## 30-second interview script
"Each adjacent pair of words gives at most one constraint: the first position where they differ says that letter comes before the other. A longer word before its own prefix is invalid. I add every letter as a node, add those edges, and run Kahn's topological sort; if not every letter gets output, there's a cycle and I return the empty string. Linear in the total number of characters."
