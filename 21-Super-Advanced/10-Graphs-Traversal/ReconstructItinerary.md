# LC 332 — Reconstruct Itinerary (Hard)

**Pattern:** Eulerian path — Hierholzer's algorithm with lexicographic adjacency · **Target:** O(E log E) · **Code:** [ReconstructItinerary.java](ReconstructItinerary.java)

## Problem
Given airline tickets `[from, to]`, use **every ticket exactly once**, starting at `"JFK"`. If several itineraries work, return the lexicographically smallest. At least one valid itinerary exists.

`[["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]] → [JFK, ATL, JFK, SFO, ATL, SFO]`

## Why engineers fear it
Greedy "always take the smallest next airport" gets stuck in dead ends. Backtracking works but is exponential. The elegant solution needs recognising an **Euler path** problem.

## Approach 1 — Backtracking with sorted adjacency
DFS trying destinations in lexicographic order, marking tickets used; the first complete path (all E tickets used) is the answer. Correct, but exponential in the worst case.

## Approach 2 — Hierholzer's algorithm (optimal)
Using every edge exactly once = Eulerian path. Hierholzer: walk edges until stuck; the stuck vertex is the **end** of the remaining path; prepend it and backtrack.
```
visit(a):
    while adj[a] not empty:
        visit(adj[a].poll_smallest())
    route.addFirst(a)                 # post-order, built in reverse
visit("JFK")
```
With each adjacency list as a **min-heap**, the lexicographically smallest valid itinerary falls out.
**Why it works:** the walk can only get stuck at the path's final vertex (the Euler end). Any edges skipped earlier form cycles that get spliced in when the recursion unwinds, and post-order places them correctly before the stuck part.
**O(E log E)** time (heap operations), **O(E)** space.

## Approach 3 — Iterative Hierholzer
Same with an explicit stack: push `JFK`; while stack non-empty, if top has edges push the smallest neighbour, else pop and prepend to the route. Avoids recursion depth issues.

## Dry run (example)
JFK → ATL (smallest) → JFK → SFO → ATL → SFO (stuck) → prepend SFO, ATL, SFO, JFK, ATL, JFK → **[JFK, ATL, JFK, SFO, ATL, SFO]**.

Dead-end example `[[JFK,KUL],[JFK,NRT],[NRT,JFK]]`: greedy goes JFK → KUL and gets stuck; Hierholzer records KUL as the end and finds `JFK → NRT → JFK → KUL`.

## Pitfalls
- Duplicate tickets — heaps/multisets, not sets.
- Building the route in forward order (it must be prepended in post-order).
- Removing tickets as they're used (polling the heap does that).

## Follow-ups
- **Valid arrangement of pairs** (LC 2097) — Euler path with an unknown start (start = node with out − in = 1).
- **Cracking the safe** (LC 753) — Euler circuit on a De Bruijn graph.
- Euler path existence: directed → in/out-degree conditions; undirected → 0 or 2 odd-degree vertices.

## 30-second interview script
"Using every ticket once is an Eulerian path. I use Hierholzer's algorithm: from JFK, repeatedly follow the smallest unused ticket; when an airport has no tickets left, it goes on the front of the route, and I back up. With min-heaps for destinations that yields the lexicographically smallest valid itinerary, and dead ends are handled automatically. O(E log E)."
