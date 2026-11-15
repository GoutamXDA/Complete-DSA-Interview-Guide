import java.util.*;

/**
 * LC 68 — Text Justification. Explanation: TextJustification.md
 * 1. Greedy packing + even distribution (left gaps get extras)   O(total chars)
 * 2. Follow-up: word wrap minimising sum of squared trailing spaces (DP)   O(n^2)
 */
public class TextJustification {

    static List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        int i = 0, n = words.length;
        while (i < n) {
            int j = i, letters = 0;
            while (j < n && letters + words[j].length() + (j - i) <= maxWidth) letters += words[j++].length();
            int gaps = j - i - 1;
            StringBuilder line = new StringBuilder(maxWidth);
            if (j == n || gaps == 0) {
                for (int k = i; k < j; k++) { if (k > i) line.append(' '); line.append(words[k]); }
                while (line.length() < maxWidth) line.append(' ');
            } else {
                int spaces = maxWidth - letters, each = spaces / gaps, extra = spaces % gaps;
                for (int k = i; k < j; k++) {
                    line.append(words[k]);
                    if (k < j - 1) line.append(" ".repeat(each + (k - i < extra ? 1 : 0)));
                }
            }
            res.add(line.toString());
            i = j;
        }
        return res;
    }

    /** Minimum sum over non-last lines of (trailing spaces)^2. Returns -1 if a word is too long. */
    static long wordWrapMinCost(String[] w, int width) {
        int n = w.length;
        long[] dp = new long[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = Long.MAX_VALUE;
            int len = -1;
            for (int j = i; j < n; j++) {
                len += w[j].length() + 1;
                if (len > width) break;
                long cost = (j == n - 1) ? 0 : (long) (width - len) * (width - len);
                if (dp[j + 1] != Long.MAX_VALUE) dp[i] = Math.min(dp[i], cost + dp[j + 1]);
            }
        }
        return dp[0] == Long.MAX_VALUE ? -1 : dp[0];
    }

    static long greedyCost(String[] w, int width) {
        long cost = 0;
        int i = 0;
        while (i < w.length) {
            int j = i, len = -1;
            while (j < w.length && len + w[j].length() + 1 <= width) len += w[j++].length() + 1;
            if (j < w.length) cost += (long) (width - len) * (width - len);
            i = j;
        }
        return cost;
    }

    public static void main(String[] args) {
        check(fullJustify(new String[]{"This", "is", "an", "example", "of", "text", "justification."}, 16),
                List.of("This    is    an", "example  of text", "justification.  "));
        check(fullJustify(new String[]{"What", "must", "be", "acknowledgment", "shall", "be"}, 16),
                List.of("What   must   be", "acknowledgment  ", "shall be        "));
        check(fullJustify(new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer.",
                "Art", "is", "everything", "else", "we", "do"}, 20),
                List.of("Science  is  what we", "understand      well", "enough to explain to", "a  computer.  Art is", "everything  else  we", "do                  "));
        for (String line : fullJustify(new String[]{"a", "bb", "ccc", "dddd", "e"}, 6)) check(line.length(), 6);
        String[] ww = {"aaa", "bb", "cc", "ddddd"};
        check(wordWrapMinCost(ww, 6), 10L);              // "aaa" / "bb cc" / "ddddd" -> 3^2 + 1^2 (last line free)
        check(greedyCost(ww, 6), 16L);                    // greedy "aaa bb" / "cc" / "ddddd" -> 0 + 4^2: greedy is NOT optimal here
        System.out.println("TextJustification: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
