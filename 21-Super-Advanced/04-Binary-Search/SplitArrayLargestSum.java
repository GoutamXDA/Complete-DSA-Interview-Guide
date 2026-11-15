import java.util.*;

/**
 * LC 410 — Split Array Largest Sum (= book allocation, painter's partition, LC 1011). Explanation: SplitArrayLargestSum.md
 * 1. Recursion over the first cut + memo       O(k n^2)
 * 2. Bottom-up partition DP                    O(k n^2)
 * 3. Binary search on answer + greedy check    O(n log sum)
 */
public class SplitArrayLargestSum {

    static long memo(int[] a, int k) {
        long[] p = prefix(a);
        return f(0, k, a.length, p, new Long[a.length + 1][k + 1]);
    }

    static long f(int i, int k, int n, long[] p, Long[][] memo) {
        if (k == 1) return p[n] - p[i];
        if (memo[i][k] != null) return memo[i][k];
        long best = Long.MAX_VALUE;
        for (int j = i; j <= n - k; j++)                   // first part = a[i..j], leave >= k-1 elements
            best = Math.min(best, Math.max(p[j + 1] - p[i], f(j + 1, k - 1, n, p, memo)));
        return memo[i][k] = best;
    }

    static long dp(int[] a, int k) {
        int n = a.length;
        long[] p = prefix(a);
        long[][] dp = new long[k + 1][n + 1];
        for (long[] row : dp) Arrays.fill(row, Long.MAX_VALUE);
        dp[0][0] = 0;
        for (int parts = 1; parts <= k; parts++)
            for (int i = 1; i <= n; i++)
                for (int j = parts - 1; j < i; j++)
                    if (dp[parts - 1][j] != Long.MAX_VALUE)
                        dp[parts][i] = Math.min(dp[parts][i], Math.max(dp[parts - 1][j], p[i] - p[j]));
        return dp[k][n];
    }

    static long splitArray(int[] a, int k) {
        long lo = 0, hi = 0;
        for (int x : a) { lo = Math.max(lo, x); hi += x; }
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (parts(a, mid) <= k) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static int parts(int[] a, long cap) {
        int cnt = 1; long cur = 0;
        for (int x : a) { if (cur + x > cap) { cnt++; cur = 0; } cur += x; }
        return cnt;
    }

    static long[] prefix(int[] a) { long[] p = new long[a.length + 1]; for (int i = 0; i < a.length; i++) p[i + 1] = p[i] + a[i]; return p; }

    public static void main(String[] args) {
        check(splitArray(new int[]{7, 2, 5, 10, 8}, 2), 18L); check(splitArray(new int[]{1, 2, 3, 4, 5}, 2), 9L);
        check(splitArray(new int[]{1, 4, 4}, 3), 4L);
        Random r = new Random(16);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(9) + 1, k = r.nextInt(n) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(10);
            long e = dp(a, k);
            check(memo(a, k), e); check(splitArray(a, k), e);
        }
        System.out.println("SplitArrayLargestSum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
