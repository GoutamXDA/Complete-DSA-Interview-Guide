import java.util.*;
import java.util.regex.Pattern;

/**
 * LC 10 — Regular Expression Matching. Explanation: RegularExpressionMatching.md
 * 1. Backtracking recursion       exponential worst
 * 2. Memoised recursion           O(mn)
 * 3. Bottom-up DP                 O(mn)
 * 4. One-row DP                   O(mn) time, O(n) space
 * Oracle: java.util.regex on the same pattern (only '.', '*' and letters are generated).
 */
public class RegularExpressionMatching {

    static boolean recursion(String s, String p, int i, int j) {
        if (j == p.length()) return i == s.length();
        boolean first = i < s.length() && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.');
        if (j + 1 < p.length() && p.charAt(j + 1) == '*')
            return recursion(s, p, i, j + 2) || (first && recursion(s, p, i + 1, j));
        return first && recursion(s, p, i + 1, j + 1);
    }

    static boolean memo(String s, String p) { return memo(s, p, 0, 0, new Boolean[s.length() + 1][p.length() + 1]); }

    static boolean memo(String s, String p, int i, int j, Boolean[][] m) {
        if (m[i][j] != null) return m[i][j];
        boolean res;
        if (j == p.length()) res = i == s.length();
        else {
            boolean first = i < s.length() && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.');
            if (j + 1 < p.length() && p.charAt(j + 1) == '*') res = memo(s, p, i, j + 2, m) || (first && memo(s, p, i + 1, j, m));
            else res = first && memo(s, p, i + 1, j + 1, m);
        }
        return m[i][j] = res;
    }

    static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 2; j <= n; j++) dp[0][j] = p.charAt(j - 1) == '*' && dp[0][j - 2];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                char c = p.charAt(j - 1);
                if (c == '*') {
                    char x = p.charAt(j - 2);
                    dp[i][j] = dp[i][j - 2] || ((x == '.' || x == s.charAt(i - 1)) && dp[i - 1][j]);
                } else dp[i][j] = (c == '.' || c == s.charAt(i - 1)) && dp[i - 1][j - 1];
            }
        return dp[m][n];
    }

    static boolean oneRow(String s, String p) {
        int n = p.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int j = 2; j <= n; j++) dp[j] = p.charAt(j - 1) == '*' && dp[j - 2];
        for (int i = 1; i <= s.length(); i++) {
            boolean diag = dp[0];
            dp[0] = false;
            for (int j = 1; j <= n; j++) {
                boolean up = dp[j];
                char c = p.charAt(j - 1);
                if (c == '*') { char x = p.charAt(j - 2); dp[j] = dp[j - 2] || ((x == '.' || x == s.charAt(i - 1)) && up); }
                else dp[j] = (c == '.' || c == s.charAt(i - 1)) && diag;
                diag = up;
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        String[][] cases = {{"aa", "a"}, {"aa", "a*"}, {"ab", ".*"}, {"aab", "c*a*b"}, {"mississippi", "mis*is*p*."}, {"", "a*b*"}, {"aaa", "a*a"}};
        boolean[] exp = {false, true, true, true, false, true, true};
        for (int k = 0; k < cases.length; k++) {
            String s = cases[k][0], p = cases[k][1];
            check(isMatch(s, p), exp[k]); check(memo(s, p), exp[k]); check(oneRow(s, p), exp[k]); check(recursion(s, p, 0, 0), exp[k]);
        }
        Random r = new Random(54);
        for (int t = 0; t < 4000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(7); i > 0; i--) sb.append((char) ('a' + r.nextInt(2)));
            StringBuilder pb = new StringBuilder();
            for (int i = r.nextInt(5); i > 0; i--) { pb.append(r.nextInt(4) == 0 ? '.' : (char) ('a' + r.nextInt(2))); if (r.nextBoolean()) pb.append('*'); }
            String s = sb.toString(), p = pb.toString();
            boolean e = Pattern.matches(p, s);
            check(isMatch(s, p), e); check(memo(s, p), e); check(oneRow(s, p), e); check(recursion(s, p, 0, 0), e);
        }
        System.out.println("RegularExpressionMatching: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
