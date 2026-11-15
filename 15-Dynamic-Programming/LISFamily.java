import java.util.*;

/**
 * LC 300 LIS
 *   Recursion take/skip with prev index   O(2^n)
 *   DP: dp[i] = 1 + max dp[j] (j < i, a[j] < a[i])  O(n^2)
 *   Patience sorting: tails[k] = smallest tail of an increasing subsequence of length k+1;
 *     binary search position for each x.  O(n log n)   (tails is NOT the LIS itself)
 * Print LIS: O(n^2) DP with parent pointers.
 * LC 673 Number of LIS: count[i] alongside len[i].
 * LC 368 Largest divisible subset: sort; LIS where a[i] % a[j] == 0.
 * LC 354 Russian doll: sort width asc, height DESC (so same widths can't chain), LIS on heights.
 */
public class LISFamily {

    static int lisRec(int[] a, int i, int prev) {
        if (i == a.length) return 0;
        int skip = lisRec(a, i + 1, prev);
        int take = (prev == -1 || a[i] > a[prev]) ? 1 + lisRec(a, i + 1, i) : 0;
        return Math.max(skip, take);
    }

    static int lisN2(int[] a) {
        int n = a.length, best = 0;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (a[j] < a[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    static int lis(int[] a) {
        int[] tails = new int[a.length];
        int size = 0;
        for (int x : a) {
            int lo = 0, hi = size;
            while (lo < hi) { int m = (lo + hi) / 2; if (tails[m] < x) lo = m + 1; else hi = m; } // lower_bound
            tails[lo] = x;
            if (lo == size) size++;
        }
        return size;
    }

    static List<Integer> printLIS(int[] a) {
        int n = a.length, end = 0;
        int[] dp = new int[n], parent = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; parent[i] = -1;
            for (int j = 0; j < i; j++) if (a[j] < a[i] && dp[j] + 1 > dp[i]) { dp[i] = dp[j] + 1; parent[i] = j; }
            if (dp[i] > dp[end]) end = i;
        }
        LinkedList<Integer> res = new LinkedList<>();
        for (int i = end; i != -1; i = parent[i]) res.addFirst(a[i]);
        return res;
    }

    static int findNumberOfLIS(int[] a) {
        int n = a.length, maxLen = 0, total = 0;
        int[] len = new int[n], cnt = new int[n];
        for (int i = 0; i < n; i++) {
            len[i] = cnt[i] = 1;
            for (int j = 0; j < i; j++) {
                if (a[j] >= a[i]) continue;
                if (len[j] + 1 > len[i]) { len[i] = len[j] + 1; cnt[i] = cnt[j]; }
                else if (len[j] + 1 == len[i]) cnt[i] += cnt[j];
            }
            if (len[i] > maxLen) { maxLen = len[i]; total = cnt[i]; } else if (len[i] == maxLen) total += cnt[i];
        }
        return total;
    }

    static List<Integer> largestDivisibleSubset(int[] nums) {
        int[] a = nums.clone(); Arrays.sort(a);
        int n = a.length, end = 0;
        int[] dp = new int[n], parent = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1; parent[i] = -1;
            for (int j = 0; j < i; j++) if (a[i] % a[j] == 0 && dp[j] + 1 > dp[i]) { dp[i] = dp[j] + 1; parent[i] = j; }
            if (dp[i] > dp[end]) end = i;
        }
        LinkedList<Integer> res = new LinkedList<>();
        for (int i = end; i != -1; i = parent[i]) res.addFirst(a[i]);
        return res;
    }

    static int maxEnvelopes(int[][] env) {
        int[][] e = env.clone();
        Arrays.sort(e, (x, y) -> x[0] != y[0] ? Integer.compare(x[0], y[0]) : Integer.compare(y[1], x[1]));
        int[] h = new int[e.length];
        for (int i = 0; i < e.length; i++) h[i] = e[i][1];
        return lis(h);
    }

    public static void main(String[] args) {
        int[] a = {10, 9, 2, 5, 3, 7, 101, 18};
        check(lisRec(a, 0, -1), 4); check(lisN2(a), 4); check(lis(a), 4);
        check(lis(new int[]{7, 7, 7, 7}), 1);
        check(printLIS(a), List.of(2, 5, 7, 101));
        check(findNumberOfLIS(new int[]{1, 3, 5, 4, 7}), 2); check(findNumberOfLIS(new int[]{2, 2, 2, 2, 2}), 5);
        check(largestDivisibleSubset(new int[]{1, 2, 4, 8}), List.of(1, 2, 4, 8));
        check(maxEnvelopes(new int[][]{{5, 4}, {6, 4}, {6, 7}, {2, 3}}), 3);
        System.out.println("LISFamily: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
