import java.util.*;

/**
 * LC 727 — Minimum Window Subsequence. Explanation: MinimumWindowSubsequence.md
 * 1. For each start, greedy forward match         O(n^2)
 * 2. Forward match + backward shrink               O(n * m)
 * 3. DP of latest start per prefix of t            O(n * m) time, O(m) space
 */
public class MinimumWindowSubsequence {

    static String brute(String s, String t) {
        String best = "";
        for (int i = 0; i < s.length(); i++) {
            int j = 0, k = i;
            for (; k < s.length() && j < t.length(); k++) if (s.charAt(k) == t.charAt(j)) j++;
            if (j == t.length() && (best.isEmpty() || k - i < best.length())) best = s.substring(i, k);
        }
        return best;
    }

    static String forwardBackward(String s, String t) {
        int n = s.length(), m = t.length(), bestStart = -1, bestLen = Integer.MAX_VALUE, i = 0;
        while (i < n) {
            int j = 0;
            while (i < n) { if (s.charAt(i) == t.charAt(j) && ++j == m) break; i++; }   // forward
            if (i == n) break;
            int end = i;
            j = m - 1;
            while (j >= 0) { if (s.charAt(i) == t.charAt(j)) j--; i--; }                  // backward
            int start = i + 1;
            if (end - start + 1 < bestLen) { bestLen = end - start + 1; bestStart = start; }
            i = start + 1;
        }
        return bestStart < 0 ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    static String dpLatestStart(String s, String t) {
        int m = t.length();
        int[] dp = new int[m];
        Arrays.fill(dp, -1);
        int bestStart = -1, bestLen = Integer.MAX_VALUE;
        for (int i = 0; i < s.length(); i++) {
            for (int j = m - 1; j >= 0; j--)                   // high to low: dp[j-1] still describes s[..i-1], so s[i] is used once
                if (s.charAt(i) == t.charAt(j)) dp[j] = (j == 0) ? i : dp[j - 1];
            if (dp[m - 1] >= 0 && i - dp[m - 1] + 1 < bestLen) { bestLen = i - dp[m - 1] + 1; bestStart = dp[m - 1]; }
        }
        return bestStart < 0 ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    public static void main(String[] args) {
        check(forwardBackward("abcdebdde", "bde"), "bcde"); check(dpLatestStart("abcdebdde", "bde"), "bcde"); check(brute("abcdebdde", "bde"), "bcde");
        check(forwardBackward("jmeqksfrsdcmsiwvaovztaqenprpvnbstl", "u"), "");
        Random r = new Random(9);
        for (int k = 0; k < 3000; k++) {
            String s = rand(r, r.nextInt(15)), t = rand(r, r.nextInt(4) + 1);
            String e = brute(s, t);
            check(forwardBackward(s, t), e); check(dpLatestStart(s, t), e);
        }
        System.out.println("MinimumWindowSubsequence: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(3))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
