# LC 212 — Word Search II (Hard)

**Pattern:** trie of all words + single grid DFS with pruning · **Target:** O(R · C · 4 · 3^(L−1)) worst, fast with pruning · **Code:** [WordSearchII.java](WordSearchII.java)

## Problem
Given a letter grid and a list of words, return every word that can be formed by a path of horizontally/vertically adjacent cells, using each cell at most once per word.

## Why engineers fear it
Running LC 79 (single word search) once per word is too slow for 3·10⁴ words. The trie-guided DFS is the standard fix, and the **pruning tricks** are what separate passing from TLE.

## Approach 1 — Word Search per word
For each word, DFS from every cell. **O(W · R · C · 4^L)**.

## Approach 2 — Trie + DFS (standard)
Insert all words into a trie. DFS from each cell, walking the trie in parallel: stop as soon as the current prefix isn't in the trie. When a node marks the end of a word, record it.
Shares work across words with common prefixes.

## Approach 3 — Trie + DFS with pruning (optimal in practice)
1. **Store the word at its end node** (no string building during DFS).
2. **Remove found words** (`node.word = null`) to avoid duplicates and re-searching.
3. **Prune dead branches:** after exploring a child, if it has no word and no children left, delete it from its parent — later searches skip it entirely.
4. Mark visited cells in place (`board[r][c] = '#'`) and restore after.
Worst case is the same exponent, but in practice the trie shrinks as words are found.

## Why a trie, not a HashSet of prefixes
A prefix set answers "is this a prefix?" in O(L) per step (hashing the whole prefix); the trie advances one node per character in O(1).

## Pitfalls
- Duplicates in the answer when a word can be found along several paths → clear `node.word` after recording.
- Forgetting to restore the cell after DFS.
- Building `String`s along the path (slow) instead of storing words at trie ends.

## Follow-ups
- **Word search** (LC 79) — single word.
- **Boggle** with diagonal moves — 8 directions.
- **Concatenated words** (LC 472) — trie + DP over word breaks.

## 30-second interview script
"I put all the words in a trie and do one DFS from every cell, advancing in the trie with each letter and stopping as soon as the path isn't a prefix of any word. Words are stored at their end nodes; when I find one I record it and clear it, and I prune trie nodes that become empty so later searches skip them. Visited cells are marked in place and restored on backtrack."
