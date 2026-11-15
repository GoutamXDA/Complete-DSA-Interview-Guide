import java.util.*;

/**
 * LC 741 — Cherry Pickup. Explanation: CherryPickup.md
 * 0. Brute force over all pairs of monotone paths (oracle)
 * W. Wrong approach: best path twice (greedy) — shown to fail
 * 1. Top-down DP on (r1, c1, r2)              O(n^3)
 * 2. Bottom-up over steps, rolling 2D array   O(n^3) time, O(n^2) space
 */
public class CherryPickup {

    static void paths(int[][] g, int r, int c, List<int[]> cur, List<List<int[]>> out) {
        int n = g.length;
        if (g[r][c] == -1) return;
        cur.add(new int[]{r, c});
        if (r == n - 1 && c == n - 1) out.add(new ArrayList<>(cur));
        else {
            if (r + 1 < n) paths(g, r + 1, c, cur, out);
            if (c + 1 < n) paths(g, r, c + 1, cur, out);
        }
        cur.remove(cur.size() - 1);
    }

    static int brute(int[][] g) {
        List<List<int[]>> all = new ArrayList<>();
        paths(g, 0, 0, new ArrayList<>(), all);
        int best = 0;
        for (List<int[]> a : all) for (List<int[]> b : all) {
            Set<Integer> cells = new HashSet<>();
            for (int[] p : a) cells.add(p[0] * 100 + p[1]);
            for (int[] p : b) cells.add(p[0] * 100 + p[1]);
            int s = 0;
            for (int x : cells) s += g[x / 100][x % 100];
            best = Math.max(best, s);
        }
        return best;
    }

    static int greedyTwice(int[][] grid) {
        int n = grid.length;
        int[][] g = new int[n][];
        for (int i = 0; i < n; i++) g[i] = grid[i].clone();
        int total = 0;
        for (int pass = 0; pass < 2; pass++) {
            int[][] dp = new int[n][n];
            for (int[] row : dp) Arrays.fill(row, Integer.MIN_VALUE);
            for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
                if (g[r][c] == -1) continue;
                int prev = (r == 0 && c == 0) ? 0 : Math.max(r > 0 ? dp[r - 1][c] : Integer.MIN_VALUE, c > 0 ? dp[r][c - 1] : Integer.MIN_VALUE);
                if (prev != Integer.MIN_VALUE) dp[r][c] = prev + g[r][c];
            }
            if (dp[n - 1][n - 1] == Integer.MIN_VALUE) return 0;
            total += dp[n - 1][n - 1];
            for (int r = n - 1, c = n - 1; ; ) {                      // clear the chosen path
                g[r][c] = 0;
                if (r == 0 && c == 0) break;
                if (r > 0 && dp[r - 1][c] != Integer.MIN_VALUE && (c == 0 || dp[r - 1][c] >= dp[r][c - 1])) r--; else c--;
            }
        }
        return total;
    }

    static final int NEG = Integer.MIN_VALUE / 4;

    static int topDown(int[][] g) {
        int n = g.length;
        Integer[][][] memo = new Integer[n][n][n];
        return Math.max(0, f(g, 0, 0, 0, memo));
    }

    static int f(int[][] g, int r1, int c1, int r2, Integer[][][] memo) {
        int n = g.length, c2 = r1 + c1 - r2;
        if (r1 >= n || c1 >= n || r2 >= n || c2 >= n || g[r1][c1] == -1 || g[r2][c2] == -1) return NEG;
        if (r1 == n - 1 && c1 == n - 1) return g[r1][c1];
        if (memo[r1][c1][r2] != null) return memo[r1][c1][r2];
        int here = g[r1][c1] + (r1 != r2 ? g[r2][c2] : 0);
        int best = Math.max(Math.max(f(g, r1 + 1, c1, r2 + 1, memo), f(g, r1 + 1, c1, r2, memo)),
                Math.max(f(g, r1, c1 + 1, r2 + 1, memo), f(g, r1, c1 + 1, r2, memo)));
        return memo[r1][c1][r2] = best == NEG ? NEG : here + best;
    }

    static int cherryPickup(int[][] g) {
        int n = g.length;
        int[][] dp = new int[n][n];
        for (int[] row : dp) Arrays.fill(row, NEG);
        dp[0][0] = g[0][0];
        for (int t = 1; t <= 2 * n - 2; t++) {
            int[][] nx = new int[n][n];
            for (int[] row : nx) Arrays.fill(row, NEG);
            for (int r1 = Math.max(0, t - n + 1); r1 <= Math.min(n - 1, t); r1++)
                for (int r2 = Math.max(0, t - n + 1); r2 <= Math.min(n - 1, t); r2++) {
                    int c1 = t - r1, c2 = t - r2;
                    if (g[r1][c1] == -1 || g[r2][c2] == -1) continue;
                    int best = dp[r1][r2];
                    if (r1 > 0) best = Math.max(best, dp[r1 - 1][r2]);
                    if (r2 > 0) best = Math.max(best, dp[r1][r2 - 1]);
                    if (r1 > 0 && r2 > 0) best = Math.max(best, dp[r1 - 1][r2 - 1]);
                    if (best == NEG) continue;
                    nx[r1][r2] = best + g[r1][c1] + (r1 != r2 ? g[r2][c2] : 0);
                }
            dp = nx;
        }
        return Math.max(0, dp[n - 1][n - 1]);
    }

    public static void main(String[] args) {
        check(cherryPickup(new int[][]{{0, 1, -1}, {1, 0, -1}, {1, 1, 1}}), 5); check(cherryPickup(new int[][]{{1, 1, -1}, {1, -1, 1}, {-1, 1, 1}}), 0);
        check(topDown(new int[][]{{0, 1, -1}, {1, 0, -1}, {1, 1, 1}}), 5);
        Random r = new Random(60);
        int[][] counter = null;
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(4) + 1;
            int[][] g = new int[n][n];
            for (int[] row : g) for (int c = 0; c < n; c++) { int x = r.nextInt(6); row[c] = x == 0 ? -1 : x <= 2 ? 1 : 0; }
            g[0][0] = Math.max(0, g[0][0]); g[n - 1][n - 1] = Math.max(0, g[n - 1][n - 1]);
            int e = brute(g);
            check(cherryPickup(g), e); check(topDown(g), e);
            if (counter == null && greedyTwice(g) < e) counter = g;
        }
        check(counter != null, true);   // the greedy "best path twice" approach really does fail on some grid
        System.out.println("  greedy-twice counter-example: " + Arrays.deepToString(counter));
        System.out.println("CherryPickup: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
