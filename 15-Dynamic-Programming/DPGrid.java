import java.util.*;

/**
 * Grid DP.
 * LC 62 Unique paths: recursion O(2^(m+n)) | dp table O(mn) | 1 row O(n) | combinatorics C(m+n-2, m-1)
 * LC 63 with obstacles: cell blocked => 0.
 * LC 64 Min path sum: dp[r][c] = g[r][c] + min(top, left). 1-row space.
 * LC 120 Triangle: bottom-up, dp[j] = t[i][j] + min(dp[j], dp[j+1]). O(n) space.
 * LC 931 Min falling path: 3 parents per cell.
 * LC 221 Maximal square: dp = 1 + min(top, left, topLeft).
 * LC 1463 Cherry pickup II: two robots on the same row -> dp[row][c1][c2]. O(R*C^2*9)
 */
public class DPGrid {

    static int uniquePathsRec(int r, int c) { return (r == 0 || c == 0) ? 1 : uniquePathsRec(r - 1, c) + uniquePathsRec(r, c - 1); }

    static int uniquePaths(int m, int n) {
        int[] dp = new int[n]; Arrays.fill(dp, 1);
        for (int r = 1; r < m; r++) for (int c = 1; c < n; c++) dp[c] += dp[c - 1];
        return dp[n - 1];
    }

    static int uniquePathsMath(int m, int n) {
        long res = 1;
        for (int i = 1; i <= m - 1; i++) res = res * (n - 1 + i) / i;   // C(m+n-2, m-1), exact at each step
        return (int) res;
    }

    static int uniquePathsWithObstacles(int[][] g) {
        int n = g[0].length;
        int[] dp = new int[n]; dp[0] = 1;
        for (int[] row : g)
            for (int c = 0; c < n; c++) {
                if (row[c] == 1) dp[c] = 0;
                else if (c > 0) dp[c] += dp[c - 1];
            }
        return dp[n - 1];
    }

    static int minPathSum(int[][] g) {
        int R = g.length, C = g[0].length;
        int[] dp = new int[C];
        for (int r = 0; r < R; r++)
            for (int c = 0; c < C; c++) {
                if (r == 0 && c == 0) dp[c] = g[0][0];
                else if (r == 0) dp[c] = dp[c - 1] + g[r][c];
                else if (c == 0) dp[c] = dp[c] + g[r][c];
                else dp[c] = Math.min(dp[c], dp[c - 1]) + g[r][c];
            }
        return dp[C - 1];
    }

    static int minimumTotal(List<List<Integer>> t) {
        int n = t.size();
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--)
            for (int j = 0; j <= i; j++) dp[j] = t.get(i).get(j) + Math.min(dp[j], dp[j + 1]);
        return dp[0];
    }

    static int minFallingPathSum(int[][] m) {
        int n = m.length;
        int[] prev = m[0].clone();
        for (int r = 1; r < n; r++) {
            int[] cur = new int[n];
            for (int c = 0; c < n; c++) {
                int best = prev[c];
                if (c > 0) best = Math.min(best, prev[c - 1]);
                if (c < n - 1) best = Math.min(best, prev[c + 1]);
                cur[c] = m[r][c] + best;
            }
            prev = cur;
        }
        return Arrays.stream(prev).min().getAsInt();
    }

    static int maximalSquare(char[][] m) {
        int R = m.length, C = m[0].length, best = 0;
        int[][] dp = new int[R + 1][C + 1];
        for (int r = 1; r <= R; r++)
            for (int c = 1; c <= C; c++)
                if (m[r - 1][c - 1] == '1') {
                    dp[r][c] = 1 + Math.min(dp[r - 1][c - 1], Math.min(dp[r - 1][c], dp[r][c - 1]));
                    best = Math.max(best, dp[r][c]);
                }
        return best * best;
    }

    static int cherryPickup(int[][] g) {
        int R = g.length, C = g[0].length;
        int[][] dp = new int[C][C];
        for (int[] row : dp) Arrays.fill(row, Integer.MIN_VALUE);
        dp[0][C - 1] = g[0][0] + (C - 1 == 0 ? 0 : g[0][C - 1]);
        for (int r = 1; r < R; r++) {
            int[][] nx = new int[C][C];
            for (int[] row : nx) Arrays.fill(row, Integer.MIN_VALUE);
            for (int a = 0; a < C; a++) for (int b = 0; b < C; b++) {
                if (dp[a][b] == Integer.MIN_VALUE) continue;
                for (int da = -1; da <= 1; da++) for (int db = -1; db <= 1; db++) {
                    int na = a + da, nb = b + db;
                    if (na < 0 || nb < 0 || na >= C || nb >= C) continue;
                    int gain = g[r][na] + (na == nb ? 0 : g[r][nb]);
                    nx[na][nb] = Math.max(nx[na][nb], dp[a][b] + gain);
                }
            }
            dp = nx;
        }
        int best = 0;
        for (int[] row : dp) for (int v : row) best = Math.max(best, v);
        return best;
    }

    public static void main(String[] args) {
        check(uniquePathsRec(2, 6), 28); check(uniquePaths(3, 7), 28); check(uniquePathsMath(3, 7), 28);
        check(uniquePathsWithObstacles(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}), 2);
        check(minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}), 7);
        check(minimumTotal(List.of(List.of(2), List.of(3, 4), List.of(6, 5, 7), List.of(4, 1, 8, 3))), 11);
        check(minFallingPathSum(new int[][]{{2, 1, 3}, {6, 5, 4}, {7, 8, 9}}), 13);
        char[][] sq = {"10100".toCharArray(), "10111".toCharArray(), "11111".toCharArray(), "10010".toCharArray()};
        check(maximalSquare(sq), 4);
        check(cherryPickup(new int[][]{{3, 1, 1}, {2, 5, 1}, {1, 5, 5}, {2, 1, 1}}), 24);
        System.out.println("DPGrid: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
