# LC 126 — Word Ladder II (Hard)

**Pattern:** BFS by layers to build a shortest-path DAG, then DFS/backtrack to enumerate paths · **Code:** [WordLadderII.java](WordLadderII.java)

## Problem
Return **all** shortest transformation sequences from `beginWord` to `endWord`, changing one letter at a time, every intermediate word in `wordList`.

`hit → cog`, list `[hot,dot,dog,lot,log,cog]` → `[[hit,hot,dot,dog,cog],[hit,hot,lot,log,cog]]`

## Why engineers fear it
LC 127 (just the length) is a plain BFS. Returning **all** shortest paths naively (BFS storing whole paths, or DFS exploring everything) is exponential in memory/time and times out. You must separate "find shortest distances" from "enumerate paths".

## Approach 1 — DFS over all paths with pruning by length
Backtracking from `beginWord`, abandoning paths longer than the best found. Exponential; TLE on real inputs. Useful only as an oracle on tiny dictionaries.

## Approach 2 — BFS storing full paths
Queue of paths; expand the last word. Correct (layer by layer, remove words only after finishing a layer), but **every partial path is copied** → memory blow-up.

## Approach 3 — Layered BFS building parent links + backtracking (optimal)
1. **BFS by layers** from `beginWord`. For each word in the current layer, generate neighbours (change each position to a–z, keep dictionary words not yet visited in **earlier** layers). Record `parents[next].add(word)`.
2. **Remove a layer's words from the dictionary only after the whole layer is processed** — so a word reachable from two words in the same layer keeps both parents (both shortest).
3. Stop after the layer that reaches `endWord`.
4. **Backtrack** from `endWord` to `beginWord` along `parents`, building each path in reverse.
Only edges between consecutive BFS layers are stored, i.e., exactly the shortest-path DAG.
**Time** O(N · L · 26) for BFS (N words, length L) + O(#answers · path length) for output.

## Approach 4 — Bidirectional BFS (speed-up)
Expand from both ends, always growing the smaller frontier; stop when they meet. Track edge direction carefully when recording parents. Big constant-factor win on large dictionaries.

## Pitfalls
- Removing a word from the dictionary as soon as it's discovered → loses alternative parents in the same layer → missing paths.
- Not stopping after the layer that found `endWord` → longer paths sneak in.
- `endWord` not in the list → return `[]` immediately.

## Follow-ups
- **Word ladder** (LC 127) — length only.
- **Minimum genetic mutation** (LC 433) — same BFS with a small alphabet.
- **All shortest paths in an unweighted graph** in general — BFS parents + DFS.

## 30-second interview script
"I run BFS level by level from the start word, and for every word discovered in the next level I record which current-level words lead to it. I only delete a level's words from the dictionary after finishing that level, so words keep every shortest-path parent. Once a level reaches the end word I stop and backtrack from it through the parent lists to list every shortest ladder."
