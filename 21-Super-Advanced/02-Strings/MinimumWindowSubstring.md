# LC 76 — Minimum Window Substring (Hard)

**Pattern:** variable sliding window with a "missing" counter · **Target:** O(|s| + |t|) · **Code:** [MinimumWindowSubstring.java](MinimumWindowSubstring.java)

## Problem
Return the shortest substring of `s` that contains every character of `t` **with multiplicity** (if `t = "aab"`, the window needs two `a`s). Return `""` if none exists.

`s = "ADOBECODEBANC", t = "ABC" → "BANC"`

## Why engineers fear it
Getting the window to shrink correctly while tracking counts — and deciding validity in O(1) instead of comparing two count arrays every step.

## Approach 1 — Brute force
Check every substring `s[i..j]` against `t`'s counts. O(n²) substrings × O(n) check = **O(n³)** (O(n²·Σ) with incremental counts).

## Approach 2 — Sliding window, compare count arrays
Expand `r`; while the window covers `t` (compare 128-length arrays), record and shrink `l`. **O(n · Σ)** where Σ = alphabet size. Correct, a bit slow.

## Approach 3 — Sliding window with `need[]` and `missing` (optimal)
- `need[c]` = how many more `c` the window still needs (can go negative = surplus).
- `missing` = total characters of `t` still not covered.
```
for r in s:
    if need[s[r]] > 0: missing--      # this char was actually needed
    need[s[r]]--
    while missing == 0:              # window valid → try to shrink
        record (l, r)
        need[s[l]]++
        if need[s[l]] > 0: missing++  # removed a needed char → invalid
        l++
```
Each index enters and leaves the window once → **O(n + m)** time, **O(Σ)** space.

## Why it's correct
The window `[l, r]` after shrinking is the **shortest valid window ending at `r`**: we shrink until it becomes invalid, so the last recorded window is minimal for this `r`. Every optimal answer ends at some `r`, so we see it.

## Dry run (start) — s = "ADOBECODEBANC", t = "ABC"
r=0..5 → "ADOBEC" becomes valid (missing 0) → record length 6 → shrink: removing `A` makes it invalid. Continue expanding… final best is "BANC" (l=9, r=12).

## Pitfalls
- Recording the window **inside** the while loop before shrinking.
- Using a HashMap is fine but `int[128]` is faster and simpler for ASCII.
- `t` longer than `s` → return "".

## Follow-ups
- **Minimum window subsequence** (LC 727) — order matters → DP / two-pointer scan (next file).
- **Find all anagrams** / **permutation in string** — fixed window version.
- **Smallest window containing all distinct characters of s itself** — `t` = set of chars of s.

## 30-second interview script
"Sliding window with a need-count per character and a single 'missing' counter so validity is O(1). Expand right; once nothing is missing, shrink from the left while it stays valid, recording the best length. Each index enters and leaves once — linear time."
