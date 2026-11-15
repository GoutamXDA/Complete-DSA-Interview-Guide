# LC 30 — Substring with Concatenation of All Words (Hard)

**Pattern:** sliding window over **word-sized steps**, run once per offset · **Target:** O(n · w) · **Code:** [SubstringWithConcatenationOfAllWords.java](SubstringWithConcatenationOfAllWords.java)

## Problem
`words` all have the same length `w`. Find every start index in `s` where some concatenation of **all** words (each used exactly as many times as it appears, any order) occurs.

`s = "barfoothefoobarman", words = ["foo","bar"] → [0, 9]`
`s = "barfoofoobarthefoobarman", words = ["bar","foo","the"] → [6, 9, 12]`

## Why engineers fear it
It's a sliding window, but the window moves in jumps of `w`, needs a multiset of words, and must handle duplicates in `words` and "bad" words that reset the window.

## Approach 1 — Check every start
For each `i`, read `k` words of length `w` starting at `i` and compare against the word count map. **O(n · k · w)** (k = number of words). Simple and often accepted.

## Approach 2 — Sliding window per offset (optimal)
Any valid start `i` has an offset `i mod w ∈ [0, w)`. For each offset, the string splits into a **sequence of word slots**. Now it's the classic "window over tokens with target counts":
```
for off in 0..w-1:
    l = off; seen.clear(); count = 0
    for r = off; r + w <= n; r += w:
        word = s[r .. r+w)
        if word not in need: seen.clear(); count = 0; l = r + w; continue   # reset
        seen[word]++; count++
        while seen[word] > need[word]:            # too many of this word → shrink
            seen[s[l .. l+w)]--; count--; l += w
        if count == k: record l; then drop leftmost word to keep sliding
```
Each slot enters and leaves once per offset → **O(n)** slot operations, each costing O(w) for the substring/hash → **O(n · w)** total.

## Why per-offset windows are enough
A concatenation starting at `i` occupies slots `i, i+w, …, i+(k−1)w`, all with the same offset, so it is exactly one window in that offset's slot sequence.

## Pitfalls
- Duplicate words: `need` must be a count map, not a set.
- After recording a match, shrink by one word (otherwise the window grows past `k`).
- Resetting on an unknown word must move `l` past it.
- `words` empty or `k·w > n` → empty answer.

## Follow-ups
- **Minimum window substring** (LC 76) — character-level version.
- Words of **different lengths** → trie + DP / backtracking; no simple window.
- Rolling hash for each word slot reduces the O(w) factor to O(1) amortised.

## 30-second interview script
"All words have length w, so any answer's words sit on slots with the same offset mod w. For each of the w offsets I run a sliding window over word slots with a target count map, shrinking when a word is over-used and resetting on unknown words. Each slot is processed once per offset, so it's O(n·w)."
