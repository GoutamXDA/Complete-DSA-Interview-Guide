# 03 — Strings

## Notes
- Java `String` is **immutable**: `s += c` in a loop is O(n²). Use `StringBuilder`.
- `s.charAt(i)`, `s.toCharArray()`, `s.substring(l, r)` (r exclusive, O(r-l) copy).
- Compare with `.equals()`, never `==`.
- Character tricks: `c - 'a'` (index 0–25), `Character.isLetterOrDigit`, `Character.toLowerCase`.
- Frequency of lowercase letters: `int[26]` beats `HashMap` (faster, O(1) space).

## Patterns
| Pattern | Use for |
|---|---|
| Two pointers from both ends | palindrome checks, reverse |
| Expand around centre | longest palindromic substring/count palindromes |
| Frequency array / sorting | anagrams |
| Sliding window | longest substring without repeat, min window (see 05) |
| Stack | valid parentheses, decode string (see 10) |
| KMP / Z / Rabin–Karp | pattern matching in O(n+m) (see 19) |
| DP | edit distance, LCS, palindromic subsequence (see 15) |

## Problems

| # | Problem | Level | Best approach | Solution |
|---|---|---|---|---|
| 1 | Reverse string (LC 344) | Easy | two pointers | [Palindromes](Palindromes.java) |
| 2 | Valid palindrome (LC 125) | Easy | two pointers, skip non-alnum | [Palindromes](Palindromes.java) |
| 3 | Longest palindromic substring (LC 5) | Medium | expand around centre (Manacher O(n) for bonus) | [Palindromes](Palindromes.java) |
| 4 | Palindromic substrings count (LC 647) | Medium | expand around centre | [Palindromes](Palindromes.java) |
| 5 | Valid anagram (LC 242) | Easy | int[26] | [Anagrams](Anagrams.java) |
| 6 | Group anagrams (LC 49) | Medium | sorted key / count key | [Anagrams](Anagrams.java) |
| 7 | Find all anagrams in a string (LC 438) | Medium | fixed sliding window | [Anagrams](Anagrams.java) |
| 8 | Reverse words in a string (LC 151) | Medium | split + reverse / in-place reverse | [StringBasics](StringBasics.java) |
| 9 | Longest common prefix (LC 14) | Easy | vertical scan / sort first & last | [StringBasics](StringBasics.java) |
| 10 | String to integer – atoi (LC 8) | Medium | parse with overflow checks | [StringBasics](StringBasics.java) |
| 11 | Roman to integer (LC 13) | Easy | subtract if smaller precedes larger | [StringBasics](StringBasics.java) |
| 12 | Integer to roman (LC 12) | Medium | greedy over value table | [StringBasics](StringBasics.java) |
| 13 | Isomorphic strings (LC 205) | Easy | two maps | [StringBasics](StringBasics.java) |
| 14 | Rotate string (LC 796) | Easy | `(s+s).contains(goal)` | [StringBasics](StringBasics.java) |
| 15 | Count and say (LC 38) | Medium | run-length build | [StringBasics](StringBasics.java) |
| 16 | Longest substring without repeating chars (LC 3) | Medium | sliding window | see 05 |
| 17 | Minimum window substring (LC 76) | Hard | sliding window | see 05 |
| 18 | Repeated string match / strStr (LC 28) | Easy/Med | KMP | see 19 |
| 19 | Edit distance (LC 72) | Hard | DP | see 15 |
| 20 | Sort characters by frequency (LC 451) | Medium | bucket sort | _(todo)_ |
| 21 | Shortest palindrome (LC 214) | Hard | KMP on s + '#' + rev(s) | _(todo)_ |
