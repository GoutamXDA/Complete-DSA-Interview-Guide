import java.util.*;

/**
 * DP on two strings: dp[i][j] describes prefixes a[0..i), b[0..j).
 *
 * LC 1143 LCS: match -> 1 + dp[i-1][j-1], else max(dp[i-1][j], dp[i][j-1]). Recursion O(2^(m+n)) -> O(mn).
 *   Print: walk back from dp[m][n].
 * Longest common substring: match -> 1 + diag, else 0; track max.
 * LC 516 LPS = LCS(s, reverse(s)).   LC 1312 Min insertions = n - LPS.
 * LC 72 Edit distance: match -> diag; else 1 + min(insert dp[i][j-1], delete dp[i-1][j], replace diag).
 * LC 115 Distinct subsequences: dp[i][j] = dp[i-1][j] + (match ? dp[i-1][j-1] : 0).
 * LC 44 Wildcard: '?' = any one; '*' -> dp[i][j-1] (empty) || dp[i-1][j] (one more char).
 * LC 10 Regex: 'x*' -> dp[i][j-2] (zero x) || (x matches s[i-1] && dp[i-1][j]).
 */
public class StringDP {

    static int lcsRec(String a, String b, int i, int j, int[][] memo) {
        if (i == 0 || j == 0) return 0;
        if (memo[i][j] != -1) return memo[i][j];
        if (a.charAt(i - 1) == b.charAt(j - 1)) return memo[i][j] = 1 + lcsRec(a, b, i - 1, j - 1, memo);
        return memo[i][j] = Math.max(lcsRec(a, b, i - 1, j, memo), lcsRec(a, b, i, j - 1, memo));
    }

    static int[][] lcsTable(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? 1 + dp[i - 1][j - 1] : Math.max(dp[i - 1][j], dp[i][j - 1]);
        return dp;
    }

    static int lcs(String a, String b) {                     // two rows
        int[] prev = new int[b.length() + 1], cur = new int[b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++)
                cur[j] = a.charAt(i - 1) == b.charAt(j - 1) ? 1 + prev[j - 1] : Math.max(prev[j], cur[j - 1]);
            int[] t = prev; prev = cur; cur = t;
        }
        return prev[b.length()];
    }

    static String printLCS(String a, String b) {
        int[][] dp = lcsTable(a, b);
        StringBuilder sb = new StringBuilder();
        int i = a.length(), j = b.length();
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) { sb.append(a.charAt(i - 1)); i--; j--; }
            else if (dp[i - 1][j] >= dp[i][j - 1]) i--; else j--;
        }
        return sb.reverse().toString();
    }

    static int longestCommonSubstring(String a, String b) {
        int best = 0;
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                if (a.charAt(i - 1) == b.charAt(j - 1)) best = Math.max(best, dp[i][j] = 1 + dp[i - 1][j - 1]);
        return best;
    }

    static int longestPalindromeSubseq(String s) { return lcs(s, new StringBuilder(s).reverse().toString()); }

    static int minInsertions(String s) { return s.length() - longestPalindromeSubseq(s); }

    static int editDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp[m][n];
    }

    static int numDistinct(String s, String t) {
        int[] dp = new int[t.length() + 1]; dp[0] = 1;
        for (int i = 1; i <= s.length(); i++)
            for (int j = t.length(); j >= 1; j--)       // backwards: dp[j-1] still holds row i-1
                if (s.charAt(i - 1) == t.charAt(j - 1)) dp[j] += dp[j - 1];
        return dp[t.length()];
    }

    static boolean wildcard(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) dp[0][j] = p.charAt(j - 1) == '*' && dp[0][j - 1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                char c = p.charAt(j - 1);
                if (c == '*') dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                else dp[i][j] = (c == '?' || c == s.charAt(i - 1)) && dp[i - 1][j - 1];
            }
        return dp[m][n];
    }

    static boolean regex(String s, String p) {
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

    public static void main(String[] args) {
        int[][] memo = new int[6][4]; for (int[] r : memo) Arrays.fill(r, -1);
        check(lcsRec("abcde", "ace", 5, 3, memo), 3); check(lcs("abcde", "ace"), 3);
        check(printLCS("abcde", "ace"), "ace");
        check(longestCommonSubstring("abcjklp", "acjkp"), 3);
        check(longestPalindromeSubseq("bbbab"), 4); check(minInsertions("leetcode"), 5);
        check(editDistance("horse", "ros"), 3); check(editDistance("intention", "execution"), 5);
        check(numDistinct("rabbbit", "rabbit"), 3); check(numDistinct("babgbag", "bag"), 5);
        check(wildcard("adceb", "*a*b"), true); check(wildcard("acdcb", "a*c?b"), false);
        check(regex("aab", "c*a*b"), true); check(regex("mississippi", "mis*is*p*."), false); check(regex("ab", ".*"), true);
        System.out.println("StringDP: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
