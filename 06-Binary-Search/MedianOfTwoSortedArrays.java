/**
 * LC 4 — Median of Two Sorted Arrays. Required O(log(min(m, n))).
 *
 * Approach 1 Merge   : merge into one array, take middle.              O(m+n), O(m+n)
 * Approach 2 Two ptrs: merge virtually, stop at the middle.            O(m+n), O(1)
 * Approach 3 Partition binary search on the smaller array:
 *   Choose i elements from A and j = (m+n+1)/2 - i from B so the left half has half the elements.
 *   Valid when A[i-1] <= B[j] and B[j-1] <= A[i]. Median from the max of left / min of right.
 *   O(log min(m,n)), O(1)
 */
public class MedianOfTwoSortedArrays {

    static double merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) c[k++] = a[i] <= b[j] ? a[i++] : b[j++];
        while (i < a.length) c[k++] = a[i++];
        while (j < b.length) c[k++] = b[j++];
        int n = c.length;
        return n % 2 == 1 ? c[n / 2] : (c[n / 2 - 1] + c[n / 2]) / 2.0;
    }

    static double twoPointers(int[] a, int[] b) {
        int n = a.length + b.length, i = 0, j = 0, prev = 0, cur = 0;
        for (int k = 0; k <= n / 2; k++) {
            prev = cur;
            if (i < a.length && (j >= b.length || a[i] <= b[j])) cur = a[i++]; else cur = b[j++];
        }
        return n % 2 == 1 ? cur : (prev + cur) / 2.0;
    }

    static double partition(int[] a, int[] b) {
        if (a.length > b.length) return partition(b, a);
        int m = a.length, n = b.length, lo = 0, hi = m, half = (m + n + 1) / 2;
        while (lo <= hi) {
            int i = (lo + hi) / 2, j = half - i;
            int aLeft = i == 0 ? Integer.MIN_VALUE : a[i - 1];
            int aRight = i == m ? Integer.MAX_VALUE : a[i];
            int bLeft = j == 0 ? Integer.MIN_VALUE : b[j - 1];
            int bRight = j == n ? Integer.MAX_VALUE : b[j];
            if (aLeft <= bRight && bLeft <= aRight) {
                if ((m + n) % 2 == 1) return Math.max(aLeft, bLeft);
                return (Math.max(aLeft, bLeft) + (double) Math.min(aRight, bRight)) / 2.0;
            }
            if (aLeft > bRight) hi = i - 1; else lo = i + 1;
        }
        throw new IllegalArgumentException("arrays not sorted");
    }

    public static void main(String[] args) {
        int[][][] tests = {{{1, 3}, {2}}, {{1, 2}, {3, 4}}, {{}, {1}}, {{0, 0}, {0, 0}}, {{1, 3, 8, 9, 15}, {7, 11, 18, 19, 21, 25}}};
        double[] exp = {2.0, 2.5, 1.0, 0.0, 11.0};
        for (int t = 0; t < tests.length; t++) {
            check(merge(tests[t][0], tests[t][1]), exp[t]);
            check(twoPointers(tests[t][0], tests[t][1]), exp[t]);
            check(partition(tests[t][0], tests[t][1]), exp[t]);
        }
        System.out.println("MedianOfTwoSortedArrays: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
