import java.util.*;

/**
 * LC 312 — Burst Balloons. Explanation: BurstBalloons.md
 * 1. Brute force over all burst orders (oracle)     O(n! n)
 * 2. Memoised recursion over open intervals         O(n^3)
 * 3. Bottom-up by interval length                   O(n^3)
 */
public class BurstBalloons {

    static int brute(List<Integer> b) {
        if (b.isEmpty()) return 0;
        int best = 0;
        for (int i = 0; i < b.size(); i++) {
            int l = i > 0 ? b.get(i - 1) : 1, r = i + 1 < b.size() ? b.get(i + 1) : 1;
            List<Integer> rest = new ArrayList<>(b); rest.remove(i);
            best = Math.max(best, l * b.get(i) * r + brute(rest));
        }
        return best;
    }

    static int[] pad(int[] nums) { int[] a = new int[nums.length + 2]; a[0] = a[a.length - 1] = 1; System.arraycopy(nums, 0, a, 1, nums.length); return a; }

    static int memo(int[] nums) { int[] a = pad(nums); return go(a, 0, a.length - 1, new Integer[a.length][a.length]); }

    static int go(int[] a, int i, int j, Integer[][] m) {
        if (j - i < 2) return 0;
        if (m[i][j] != null) return m[i][j];
        int best = 0;
        for (int k = i + 1; k < j; k++) best = Math.max(best, go(a, i, k, m) + a[i] * a[k] * a[j] + go(a, k, j, m));
        return m[i][j] = best;
    }

    static int maxCoins(int[] nums) {
        int[] a = pad(nums);
        int n = a.length;
        int[][] dp = new int[n][n];
        for (int len = 2; len < n; len++)
            for (int i = 0; i + len < n; i++) {
                int j = i + len;
                for (int k = i + 1; k < j; k++) dp[i][j] = Math.max(dp[i][j], dp[i][k] + a[i] * a[k] * a[j] + dp[k][j]);
            }
        return dp[0][n - 1];
    }

    public static void main(String[] args) {
        check(maxCoins(new int[]{3, 1, 5, 8}), 167); check(maxCoins(new int[]{1, 5}), 10); check(memo(new int[]{3, 1, 5, 8}), 167);
        Random r = new Random(58);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(7)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(10);
            List<Integer> l = new ArrayList<>(); for (int x : a) l.add(x);
            int e = brute(l);
            check(memo(a), e); check(maxCoins(a), e);
        }
        System.out.println("BurstBalloons: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
