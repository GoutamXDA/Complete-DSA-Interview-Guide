import java.util.*;

/**
 * LC 337 House robber III (tree DP): each node returns {robbed, notRobbed}. O(n)
 * TSP (bitmask DP): dp[mask][i] = min cost to visit `mask` ending at i. O(2^n * n^2), n <= ~16.
 * Digit DP: count x in [0, N] with all distinct digits; state (pos, mask, tight, started).
 * Min subset sum difference: subset-sum table up to total/2, pick largest reachable s.
 * LC 1277 Count square submatrices: dp = 1 + min(3 neighbours); sum of all dp.
 * LC 887 Super egg drop: dp[m][e] = floors coverable with m moves, e eggs = dp[m-1][e-1] + dp[m-1][e] + 1.
 *   (Classic O(k*n^2) recurrence is TLE; this inverted DP is O(k * answer).)
 */
public class AdvancedDP {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static int robTree(TreeNode root) { int[] r = robPair(root); return Math.max(r[0], r[1]); }

    static int[] robPair(TreeNode n) {
        if (n == null) return new int[]{0, 0};
        int[] l = robPair(n.left), r = robPair(n.right);
        int robbed = n.val + l[1] + r[1];
        int skip = Math.max(l[0], l[1]) + Math.max(r[0], r[1]);
        return new int[]{robbed, skip};
    }

    static int tsp(int[][] dist) {
        int n = dist.length, FULL = (1 << n) - 1, INF = Integer.MAX_VALUE / 2;
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) Arrays.fill(row, INF);
        dp[1][0] = 0;                                     // start at city 0
        for (int mask = 1; mask <= FULL; mask++)
            for (int i = 0; i < n; i++) {
                if (dp[mask][i] == INF || (mask >> i & 1) == 0) continue;
                for (int j = 0; j < n; j++) {
                    if ((mask >> j & 1) == 1) continue;
                    int nm = mask | 1 << j;
                    dp[nm][j] = Math.min(dp[nm][j], dp[mask][i] + dist[i][j]);
                }
            }
        int best = INF;
        for (int i = 0; i < n; i++) best = Math.min(best, dp[FULL][i] + dist[i][0]);
        return best;
    }

    // ---------- Digit DP ----------
    static int countDistinctBrute(int N) {
        int c = 0;
        for (int x = 0; x <= N; x++) {
            String s = String.valueOf(x);
            if (s.chars().distinct().count() == s.length()) c++;
        }
        return c;
    }

    static char[] digits;
    static Integer[][] memo;

    static int countDistinctDigits(int N) {
        digits = String.valueOf(N).toCharArray();
        memo = new Integer[digits.length][1 << 10];
        return go(0, 0, true, false);
    }

    static int go(int pos, int mask, boolean tight, boolean started) {
        if (pos == digits.length) return 1;               // counts 0 as well (empty -> number 0)
        if (!tight && started && memo[pos][mask] != null) return memo[pos][mask];
        int limit = tight ? digits[pos] - '0' : 9, total = 0;
        for (int d = 0; d <= limit; d++) {
            boolean nowStarted = started || d != 0;
            if (nowStarted && (mask >> d & 1) == 1) continue;
            int nm = nowStarted ? mask | 1 << d : mask;
            total += go(pos + 1, nm, tight && d == limit, nowStarted);
        }
        if (!tight && started) memo[pos][mask] = total;
        return total;
    }

    static int minSubsetSumDiff(int[] a) {
        int total = Arrays.stream(a).sum();
        boolean[] dp = new boolean[total / 2 + 1]; dp[0] = true;
        for (int x : a) for (int s = total / 2; s >= x; s--) dp[s] |= dp[s - x];
        for (int s = total / 2; s >= 0; s--) if (dp[s]) return total - 2 * s;
        return total;
    }

    static int countSquares(int[][] m) {
        int total = 0;
        int[][] dp = new int[m.length + 1][m[0].length + 1];
        for (int r = 1; r <= m.length; r++)
            for (int c = 1; c <= m[0].length; c++)
                if (m[r - 1][c - 1] == 1) total += dp[r][c] = 1 + Math.min(dp[r - 1][c - 1], Math.min(dp[r - 1][c], dp[r][c - 1]));
        return total;
    }

    static int superEggDrop(int k, int n) {
        int[] dp = new int[k + 1];       // dp[e] = floors coverable with current moves and e eggs
        int moves = 0;
        while (dp[k] < n) {
            moves++;
            for (int e = k; e >= 1; e--) dp[e] = dp[e - 1] + dp[e] + 1;
        }
        return moves;
    }

    public static void main(String[] args) {
        TreeNode t = new TreeNode(3);
        t.left = new TreeNode(2); t.right = new TreeNode(3);
        t.left.right = new TreeNode(3); t.right.right = new TreeNode(1);
        check(robTree(t), 7);
        int[][] d = {{0, 10, 15, 20}, {10, 0, 35, 25}, {15, 35, 0, 30}, {20, 25, 30, 0}};
        check(tsp(d), 80);
        for (int N : new int[]{0, 9, 10, 11, 100, 1234, 98765})
            check(countDistinctDigits(N), countDistinctBrute(N));
        check(minSubsetSumDiff(new int[]{1, 6, 11, 5}), 1);
        check(countSquares(new int[][]{{0, 1, 1, 1}, {1, 1, 1, 1}, {0, 1, 1, 1}}), 15);
        check(superEggDrop(1, 2), 2); check(superEggDrop(2, 6), 3); check(superEggDrop(3, 14), 4);
        System.out.println("AdvancedDP: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
