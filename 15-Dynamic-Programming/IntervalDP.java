import java.util.*;

/**
 * Interval / partition DP: dp[i][j] = best answer for range i..j, try every split k. Usually O(n^3).
 * Fill by increasing length (or i from right to left).
 *
 * Matrix chain multiplication: dims p[0..n]; cost(i,j) = min_k cost(i,k) + cost(k+1,j) + p[i-1]*p[k]*p[j]
 * LC 312 Burst balloons: think of k as the LAST balloon burst in (i, j): a[i]*a[k]*a[j] + dp[i][k] + dp[k][j]
 * LC 1547 Min cost to cut stick: sort cuts, add 0 and n; dp over cut indices, cost = cuts[j] - cuts[i].
 * LC 132 Palindrome partitioning II: front partition dp[i] = min cuts for s[i..]; isPal precomputed. O(n^2)
 */
public class IntervalDP {

    static int mcmRec(int[] p, int i, int j, int[][] memo) {
        if (i == j) return 0;
        if (memo[i][j] != -1) return memo[i][j];
        int best = Integer.MAX_VALUE;
        for (int k = i; k < j; k++) best = Math.min(best, mcmRec(p, i, k, memo) + mcmRec(p, k + 1, j, memo) + p[i - 1] * p[k] * p[j]);
        return memo[i][j] = best;
    }

    static int mcm(int[] p) {
        int n = p.length;
        int[][] dp = new int[n][n];
        for (int len = 2; len < n; len++)
            for (int i = 1; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j]);
            }
        return dp[1][n - 1];
    }

    static int maxCoins(int[] nums) {
        int n = nums.length + 2;
        int[] a = new int[n];
        a[0] = a[n - 1] = 1;
        for (int i = 0; i < nums.length; i++) a[i + 1] = nums[i];
        int[][] dp = new int[n][n];
        for (int len = 2; len < n; len++)
            for (int i = 0; i + len < n; i++) {
                int j = i + len;
                for (int k = i + 1; k < j; k++) dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + a[i] * a[k] * a[j]);
            }
        return dp[0][n - 1];
    }

    static int minCostCut(int n, int[] cuts) {
        int m = cuts.length + 2;
        int[] c = new int[m];
        c[0] = 0; c[m - 1] = n;
        int[] s = cuts.clone(); Arrays.sort(s);
        System.arraycopy(s, 0, c, 1, s.length);
        int[][] dp = new int[m][m];
        for (int len = 2; len < m; len++)
            for (int i = 0; i + len < m; i++) {
                int j = i + len;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + c[j] - c[i]);
            }
        return dp[0][m - 1];
    }

    static int minCut(String s) {
        int n = s.length();
        boolean[][] pal = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--)
            for (int j = i; j < n; j++) pal[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 2 || pal[i + 1][j - 1]);
        int[] dp = new int[n + 1];          // dp[i] = min partitions of s[i..]
        dp[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = Integer.MAX_VALUE;
            for (int j = i; j < n; j++) if (pal[i][j]) dp[i] = Math.min(dp[i], 1 + dp[j + 1]);
        }
        return dp[0] - 1;                   // partitions - 1 = cuts
    }

    public static void main(String[] args) {
        int[] p = {40, 20, 30, 10, 30};
        int[][] memo = new int[p.length][p.length]; for (int[] r : memo) Arrays.fill(r, -1);
        check(mcmRec(p, 1, p.length - 1, memo), 26000); check(mcm(p), 26000);
        check(maxCoins(new int[]{3, 1, 5, 8}), 167);
        check(minCostCut(7, new int[]{1, 3, 4, 5}), 16);
        check(minCut("aab"), 1); check(minCut("a"), 0); check(minCut("abccbc"), 2);
        System.out.println("IntervalDP: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
