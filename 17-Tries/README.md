# 17 — Tries (Prefix Trees)

## Notes
- Each node has up to 26 children (`TrieNode[26]`) or a `HashMap<Character, TrieNode>` for large alphabets, plus an `isEnd` flag (or counts).
- Insert / search / startsWith: **O(L)** where L = word length, independent of how many words are stored.
- Space: O(total characters × alphabet) worst case.
- **Binary trie** (bits 31 → 0) solves maximum-XOR problems in O(32) per query.

## Trie vs HashSet
| | Trie | HashSet |
|---|---|---|
| Exact lookup | O(L) | O(L) to hash |
| Prefix queries | O(prefix) ✅ | ❌ scan all |
| Sorted iteration | ✅ (DFS) | ❌ |
| Memory | higher | lower |

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Implement Trie (LC 208) | Medium | array children | [Trie](Trie.java) |
| 2 | Trie II – count words / prefixes, erase | Medium | counters on nodes | [Trie](Trie.java) |
| 3 | Longest word with all prefixes (LC 720) | Medium | insert all, DFS on end nodes | [TrieProblems](TrieProblems.java) |
| 4 | Count distinct substrings | Hard | insert all suffixes, count nodes | [TrieProblems](TrieProblems.java) |
| 5 | Maximum XOR of two numbers (LC 421) | Medium | binary trie, greedy opposite bit | [TrieProblems](TrieProblems.java) |
| 6 | Maximum XOR with an element from array (LC 1707) | Hard | offline: sort queries, insert ≤ limit | [TrieProblems](TrieProblems.java) |
| 7 | Word search II (LC 212) | Hard | trie + grid DFS, prune found words | [TrieProblems](TrieProblems.java) |
| 8 | Design add & search words (LC 211) | Medium | DFS on '.' | [TrieProblems](TrieProblems.java) |
| 9 | Replace words (LC 648) | Medium | shortest root prefix | [TrieProblemsII](TrieProblemsII.java) |
| 10 | Search suggestions system (LC 1268) | Medium | trie with top-3 per node / sort + binary search | [TrieProblemsII](TrieProblemsII.java) |
| 11 | Palindrome pairs (LC 336) | Hard | trie of reversed words | [TrieProblemsII](TrieProblemsII.java) |
