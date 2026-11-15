# LC 68 — Text Justification (Hard)

**Pattern:** greedy line packing + careful space distribution (pure implementation) · **Target:** O(total characters) · **Code:** [TextJustification.java](TextJustification.java)

## Problem
Format `words` into lines of exactly `maxWidth` characters:
- Pack as many words as fit on each line (greedy), with at least one space between words.
- Distribute extra spaces **as evenly as possible**; if uneven, the **left** gaps get more.
- A line with a single word, and the **last line**, are left-justified (single spaces, padded on the right).

## Why engineers fear it
No clever algorithm — just many edge cases under time pressure. Interviewers use it to test clean decomposition.

## Approach 1 — Greedy packing + formula (optimal)
1. **Pack:** from word `i`, extend `j` while `lettersLen + words[j].length + (j − i) ≤ maxWidth` (the `(j − i)` term is the minimum one space before each added word).
2. **Distribute** for a normal line with `gaps = j − i − 1 > 0`:
   `spaces = maxWidth − lettersLen`, each gap gets `spaces / gaps`, and the first `spaces % gaps` gaps get one extra.
3. **Left-justify** when `gaps == 0` or `j == words.length`.
**Time** O(total chars) · **Space** O(maxWidth) per line.

**Why greedy packing is right:** the rules *define* greedy packing ("pack as many words as you can"), so there's no optimisation to do.

## Approach 2 — Minimum raggedness (the *real* hard variant)
If asked to minimise the sum of squared trailing spaces (Knuth–Plass style "word wrap"), greedy is **not** optimal; use DP: `dp[i] = min over j (cost(i..j) + dp[j+1])`, O(n²). Included in the Java file as `wordWrapMinCost` because it's a common follow-up.

Counter-example (width 6, words `aaa bb cc ddddd`): greedy packs `"aaa bb" / "cc" / "ddddd"` → cost 0 + 4² = 16, while DP finds `"aaa" / "bb cc" / "ddddd"` → 3² + 1² = 10.

## Worked example — width 16
`["This","is","an","example","of","text","justification."]`
- Line 1: "This","is","an" → letters 8, gaps 2, spaces 8 → 4 + 4 → `"This    is    an"`
- Line 2: "example","of","text" → letters 13, gaps 2, spaces 3 → 2 + 1 → `"example  of text"`
- Line 3 (last): `"justification.  "`

## Pitfalls
- Dividing by zero when a line has one word.
- Giving extra spaces to the right gaps instead of the left.
- Forgetting to pad the last line to full width.
- A word exactly `maxWidth` long.

## Follow-ups
- Word wrap minimising raggedness (DP above).
- Right-justify / centre variants — change only the distribution step.
- Streaming input — keep a buffer for the current line only.

## 30-second interview script
"Greedily pack words while letters plus one space per gap fit. For a normal line, the leftover spaces are split as spaces/gaps each with the remainder going to the leftmost gaps. Single-word lines and the last line are left-justified and padded. Linear in the total characters; if they want minimum raggedness instead, that's a DP over break points."
