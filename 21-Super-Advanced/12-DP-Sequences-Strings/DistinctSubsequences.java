import java.util.*;

/**
 * LC 115 — Distinct Subsequences. Explanation: DistinctSubsequences.md
 * 0. Enumerate index subsets (oracle)                O(2^m)
 * 1. Recursion                                       exponential
 * 2. Memoisation                                     O(mn)
 * 3. 2D tabulation                                   O(mn)
 * 4. 1D backwards                                    O(mn) time, O(n) space
 * 5. 1D, update only matching positions of t         O(m * occurrences)
 */
public class DistinctSubsequences {

    static long enumerate(String s, String t) {
        long c = 0;
        for (int mask = 0; mask < 1 << s.length(); mask++) {
            if (Integer.bitCount(mask) != t.length()) continue;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) if ((mask >> i & 1) == 1) sb.append(s.charAt(i));
            if (sb.toString().equals(t)) c++;
        }
        return c;
    }

    static long recursion(String s, String t, int i, int j) {
        if (j == t.length()) return 1;
        if (i == s.length()) return 0;
        long ways = recursion(s, t, i + 1, j);
        if (s.charAt(i) == t.charAt(j)) ways += recursion(s, t, i + 1, j + 1);
        return ways;
    }

    static long memo(String s, String t) { return memo(s, t, 0, 0, new Long[s.length() + 1][t.length() + 1]); }

    static long memo(String s, String t, int i, int j, Long[][] m) {
        if (j == t.length()) return 1;
        if (i == s.length()) return 0;
        if (m[i][j] != null) return m[i][j];
        long ways = memo(s, t, i + 1, j, m);
        if (s.charAt(i) == t.charAt(j)) ways += memo(s, t, i + 1, j + 1, m);
        return m[i][j] = ways;
    }

    static long table(String s, String t) {
        int m = s.length(), n = t.length();
        long[][] dp = new long[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = 1;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = dp[i - 1][j] + (s.charAt(i - 1) == t.charAt(j - 1) ? dp[i - 1][j - 1] : 0);
        return dp[m][n];
    }

    static long numDistinct(String s, String t) {
        int n = t.length();
        long[] dp = new long[n + 1];
        dp[0] = 1;
        for (int i = 0; i < s.length(); i++)
            for (int j = n; j >= 1; j--)
                if (s.charAt(i) == t.charAt(j - 1)) dp[j] += dp[j - 1];
        return dp[n];
    }

    static long positionsOnly(String s, String t) {
        int n = t.length();
        Map<Character, List<Integer>> pos = new HashMap<>();
        for (int j = n; j >= 1; j--) pos.computeIfAbsent(t.charAt(j - 1), k -> new ArrayList<>()).add(j);   // descending
        long[] dp = new long[n + 1];
        dp[0] = 1;
        for (char c : s.toCharArray()) for (int j : pos.getOrDefault(c, List.of())) dp[j] += dp[j - 1];
        return dp[n];
    }

    public static void main(String[] args) {
        check(numDistinct("rabbbit", "rabbit"), 3L); check(numDistinct("babgbag", "bag"), 5L); check(numDistinct("a", "aa"), 0L);
        Random r = new Random(55);
        for (int t = 0; t < 2000; t++) {
            String s = rand(r, r.nextInt(11)), tt = rand(r, r.nextInt(4));
            long e = enumerate(s, tt);
            check(recursion(s, tt, 0, 0), e); check(memo(s, tt), e); check(table(s, tt), e);
            check(numDistinct(s, tt), e); check(positionsOnly(s, tt), e);
        }
        System.out.println("DistinctSubsequences: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(2))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
