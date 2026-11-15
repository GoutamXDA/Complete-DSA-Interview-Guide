import java.util.*;

/**
 * LC 354 — Russian Doll Envelopes. Explanation: RussianDollEnvelopes.md
 * 1. Sort + O(n^2) DP                                       O(n^2)
 * 2. Sort (w asc, h desc) + patience-sorting LIS            O(n log n)
 * 3. Same sort + Fenwick tree of prefix maxima              O(n log n)
 * Also shows why the descending tie-break matters.
 */
public class RussianDollEnvelopes {

    static int dpN2(int[][] env) {
        int[][] e = env.clone();
        Arrays.sort(e, (a, b) -> a[0] - b[0]);
        int n = e.length, best = 0;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (e[j][0] < e[i][0] && e[j][1] < e[i][1]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    static int maxEnvelopes(int[][] env) { return lisAfterSort(env, true); }

    static int lisAfterSort(int[][] env, boolean descTie) {
        int[][] e = env.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? a[0] - b[0] : (descTie ? b[1] - a[1] : a[1] - b[1]));
        int[] tails = new int[e.length];
        int size = 0;
        for (int[] x : e) {
            int lo = 0, hi = size;
            while (lo < hi) { int m = (lo + hi) >>> 1; if (tails[m] < x[1]) lo = m + 1; else hi = m; }
            tails[lo] = x[1];
            if (lo == size) size++;
        }
        return size;
    }

    static int fenwick(int[][] env) {
        int[][] e = env.clone();
        Arrays.sort(e, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
        int[] hs = Arrays.stream(e).mapToInt(x -> x[1]).distinct().sorted().toArray();
        int[] bit = new int[hs.length + 1];
        int best = 0;
        for (int[] x : e) {
            int rank = Arrays.binarySearch(hs, x[1]) + 1;
            int q = 0;
            for (int i = rank - 1; i > 0; i -= i & -i) q = Math.max(q, bit[i]);        // best over strictly smaller heights
            int cur = q + 1;
            best = Math.max(best, cur);
            for (int i = rank; i < bit.length; i += i & -i) bit[i] = Math.max(bit[i], cur);
        }
        return best;
    }

    public static void main(String[] args) {
        int[][] ex = {{5, 4}, {6, 4}, {6, 7}, {2, 3}};
        check(maxEnvelopes(ex), 3); check(dpN2(ex), 3); check(fenwick(ex), 3);
        check(maxEnvelopes(new int[][]{{1, 1}, {1, 1}, {1, 1}}), 1);
        int[][] tie = {{3, 3}, {3, 4}};
        check(maxEnvelopes(tie), 1); check(lisAfterSort(tie, false), 2);       // ascending tie-break overcounts
        Random r = new Random(57);
        for (int t = 0; t < 2000; t++) {
            int[][] e = new int[r.nextInt(12) + 1][];
            for (int i = 0; i < e.length; i++) e[i] = new int[]{r.nextInt(6) + 1, r.nextInt(6) + 1};
            int x = dpN2(e);
            check(maxEnvelopes(e), x); check(fenwick(e), x);
        }
        System.out.println("RussianDollEnvelopes: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
