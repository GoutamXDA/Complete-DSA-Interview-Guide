import java.util.*;

/**
 * LC 887 — Super Egg Drop. Explanation: SuperEggDrop.md
 * 1. Classic minimax DP                   O(k n^2)
 * 2. Binary search on the drop floor      O(k n log n)
 * 3. Inverted DP: moves -> floors         O(k * answer)
 * 4. Binomial closed form + binary search O(k log n)
 */
public class SuperEggDrop {

    static int classic(int k, int n) {
        int[][] dp = new int[k + 1][n + 1];
        for (int f = 1; f <= n; f++) dp[1][f] = f;
        for (int e = 2; e <= k; e++)
            for (int f = 1; f <= n; f++) {
                dp[e][f] = Integer.MAX_VALUE;
                for (int x = 1; x <= f; x++) dp[e][f] = Math.min(dp[e][f], 1 + Math.max(dp[e - 1][x - 1], dp[e][f - x]));
            }
        return dp[k][n];
    }

    static int binarySearch(int k, int n) {
        int[][] dp = new int[k + 1][n + 1];
        for (int f = 1; f <= n; f++) dp[1][f] = f;
        for (int e = 2; e <= k; e++)
            for (int f = 1; f <= n; f++) {
                int lo = 1, hi = f;
                while (lo + 1 < hi) {                                   // find the crossing of the two monotone curves
                    int x = (lo + hi) >>> 1;
                    if (dp[e - 1][x - 1] < dp[e][f - x]) lo = x; else hi = x;
                }
                dp[e][f] = 1 + Math.min(Math.max(dp[e - 1][lo - 1], dp[e][f - lo]), Math.max(dp[e - 1][hi - 1], dp[e][f - hi]));
            }
        return dp[k][n];
    }

    static int superEggDrop(int k, int n) {
        int[] F = new int[k + 1];                                       // F[e] = floors coverable with current moves
        int moves = 0;
        while (F[k] < n) {
            moves++;
            for (int e = k; e >= 1; e--) F[e] = F[e - 1] + F[e] + 1;    // backwards: F[e-1] is still the previous move count
        }
        return moves;
    }

    static int binomial(int k, int n) {
        int lo = 1, hi = n;
        while (lo < hi) {
            int m = (lo + hi) >>> 1;
            if (floors(m, k, n) >= n) hi = m; else lo = m + 1;
        }
        return lo;
    }

    /** sum_{i=1..k} C(m, i), capped at n */
    static long floors(int m, int k, int n) {
        long sum = 0, c = 1;
        for (int i = 1; i <= k && i <= m; i++) {
            c = c * (m - i + 1) / i;
            sum += c;
            if (sum >= n) return n;
        }
        return sum;
    }

    public static void main(String[] args) {
        check(superEggDrop(1, 2), 2); check(superEggDrop(2, 6), 3); check(superEggDrop(3, 14), 4); check(superEggDrop(2, 100), 14);
        check(binomial(2, 100), 14); check(superEggDrop(100, 10000), 14); check(binomial(4, 5000), 19); check(superEggDrop(4, 5000), 19);
        for (int k = 1; k <= 4; k++)
            for (int n = 1; n <= 120; n++) {
                int e = classic(k, n);
                check(binarySearch(k, n), e); check(superEggDrop(k, n), e); check(binomial(k, n), e);
            }
        System.out.println("SuperEggDrop: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
