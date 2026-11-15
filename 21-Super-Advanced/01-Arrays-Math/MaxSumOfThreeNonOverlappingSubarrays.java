import java.util.*;

/**
 * LC 689 — Maximum Sum of 3 Non-Overlapping Subarrays. Explanation: MaxSumOfThreeNonOverlappingSubarrays.md
 * 1. Brute force over three starts              O(n^3)
 * 2. Fix the middle + best-left / best-right    O(n)
 * 3. General DP for m windows + reconstruction  O(m n)
 */
public class MaxSumOfThreeNonOverlappingSubarrays {

    static int[] windows(int[] a, int k) {
        int[] w = new int[a.length - k + 1];
        int s = 0;
        for (int i = 0; i < a.length; i++) {
            s += a[i];
            if (i >= k) s -= a[i - k];
            if (i >= k - 1) w[i - k + 1] = s;
        }
        return w;
    }

    static int[] brute(int[] a, int k) {
        int[] w = windows(a, k), best = null;
        int bestSum = Integer.MIN_VALUE;
        for (int i = 0; i < w.length; i++)
            for (int j = i + k; j < w.length; j++)
                for (int l = j + k; l < w.length; l++)
                    if (w[i] + w[j] + w[l] > bestSum) { bestSum = w[i] + w[j] + w[l]; best = new int[]{i, j, l}; }
        return best;
    }

    static int[] maxSumOfThreeSubarrays(int[] a, int k) {
        int[] w = windows(a, k);
        int m = w.length;
        int[] left = new int[m], right = new int[m];
        for (int i = 0, best = 0; i < m; i++) { if (w[i] > w[best]) best = i; left[i] = best; }
        for (int i = m - 1, best = m - 1; i >= 0; i--) { if (w[i] >= w[best]) best = i; right[i] = best; }
        int[] ans = null;
        int bestSum = Integer.MIN_VALUE;
        for (int j = k; j + k < m; j++) {
            int i = left[j - k], l = right[j + k];
            int s = w[i] + w[j] + w[l];
            if (s > bestSum) { bestSum = s; ans = new int[]{i, j, l}; }
        }
        return ans;
    }

    /** m non-overlapping windows of length k; lexicographically smallest optimal starts. */
    static int[] maxSumOfMSubarrays(int[] a, int k, int m) {
        int[] w = windows(a, k);
        int n = a.length;
        // best[t][i] = best sum of t windows fully inside the SUFFIX starting at i (suffix form gives lexicographic order on reconstruction)
        long NEG = Long.MIN_VALUE / 4;
        long[][] best = new long[m + 1][n + 1];
        for (int t = 1; t <= m; t++) Arrays.fill(best[t], NEG);
        for (int t = 1; t <= m; t++)
            for (int i = n - k; i >= 0; i--) {
                long take = best[t - 1][i + k] == NEG ? NEG : w[i] + best[t - 1][i + k];
                best[t][i] = Math.max(best[t][i + 1], take);
            }
        int[] res = new int[m];
        int i = 0;
        for (int t = m; t >= 1; t--) {
            while (best[t - 1][i + k] == NEG || w[i] + best[t - 1][i + k] != best[t][i]) i++;   // first start achieving the optimum
            res[m - t] = i;
            i += k;
        }
        return res;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 1, 2, 6, 7, 5, 1};
        check(Arrays.toString(maxSumOfThreeSubarrays(a, 2)), "[0, 3, 5]"); check(Arrays.toString(brute(a, 2)), "[0, 3, 5]");
        check(Arrays.toString(maxSumOfMSubarrays(a, 2, 3)), "[0, 3, 5]");
        int[] b = {1, 2, 1, 2, 1, 2, 1, 2, 1};
        check(Arrays.toString(maxSumOfThreeSubarrays(b, 2)), "[0, 2, 4]");
        Random r = new Random(4);
        for (int t = 0; t < 2000; t++) {
            int k = r.nextInt(3) + 1, n = 3 * k + r.nextInt(8);
            int[] x = new int[n];
            for (int i = 0; i < n; i++) x[i] = r.nextInt(4);
            String e = Arrays.toString(brute(x, k));
            check(Arrays.toString(maxSumOfThreeSubarrays(x, k)), e);
            check(Arrays.toString(maxSumOfMSubarrays(x, k, 3)), e);
        }
        System.out.println("MaxSumOfThreeNonOverlappingSubarrays: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
