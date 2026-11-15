# LC 214 — Shortest Palindrome (Hard)

**Pattern:** longest palindromic **prefix** via KMP failure function (or Z / rolling hash) · **Target:** O(n) · **Code:** [ShortestPalindrome.java](ShortestPalindrome.java)

## Problem
You may only add characters **in front** of `s`. Return the shortest palindrome you can make.

`"aacecaaa" → "aaacecaaa"` · `"abcd" → "dcbabcd"`

## Reduction (the whole problem)
If the longest palindromic prefix of `s` is `s[0..L)`, the answer is `reverse(s[L..]) + s`. Everything after that prefix must be mirrored in front, and nothing less works. So: **find L fast**.

## Approach 1 — Brute force
Try `L = n, n−1, …` and check whether `s[0..L)` is a palindrome. **O(n²)** worst case (e.g., `"aaaa…ab"`).

## Approach 2 — Two-pointer recursion (classic trick)
Walk `i` from 0 and `j` from n−1 down; whenever `s[i] == s[j]` advance `i`. The prefix `s[0..i)` is guaranteed to contain the longest palindromic prefix; recurse on it. O(n²) worst, fast on average. Mention it, don't rely on it.

## Approach 3 — KMP on `s + '#' + reverse(s)` (optimal)
Build `T = s + '#' + rev(s)` and compute the LPS (failure) array. `lps[last]` = longest prefix of `T` that is also a suffix of `T`.
- A prefix of `T` (without crossing `#`) is a prefix of `s`.
- A suffix of `T` is a suffix of `rev(s)` = reversed prefix of `s`.
- So `lps[last]` = the longest prefix of `s` that equals its own reverse → the **longest palindromic prefix**. The `#` stops matches from crossing the boundary.
**Time** O(n) · **Space** O(n).

## Approach 4 — Rolling hash
Scan `i`, maintaining a forward hash of `s[0..i]` and a reverse hash; when they match (and verify), update L. O(n) expected; collision-prone without verification.

## Dry run — s = "aacecaaa"
`T = "aacecaaa#aaacecaa"` → lps at the end = 7 → `s[0..7) = "aacecaa"` is the palindromic prefix → prepend `reverse("a") = "a"` → **"aaacecaaa"**.

## Pitfalls
- Forgetting the separator: `"aa" + "aa"` would report lps 3 > n.
- The separator must not occur in `s` (LC uses lowercase letters, `#` is safe).
- Building the answer: reverse the **suffix after L**, not the whole string.

## Follow-ups
- **Longest palindromic substring** (LC 5) — Manacher O(n) (see [19-Advanced-Data-Structures/AdvancedProblemsII.java](../../19-Advanced-Data-Structures/AdvancedProblemsII.java)).
- **Palindrome pairs** (LC 336) — the same "palindromic prefix/suffix" reasoning with a trie.
- Append characters at the **end** instead → longest palindromic suffix, mirror the trick.

## 30-second interview script
"Only the longest palindromic prefix can stay; the rest must be mirrored in front. To find it in linear time I run KMP's prefix function on s + '#' + reverse(s): the final LPS value is the longest prefix of s equal to a suffix of its reverse, i.e., the longest palindromic prefix."
