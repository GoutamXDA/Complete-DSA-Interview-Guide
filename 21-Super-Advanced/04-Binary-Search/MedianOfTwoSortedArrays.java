import java.util.*;

/**
 * LC 4 — Median of Two Sorted Arrays. Explanation: MedianOfTwoSortedArrays.md
 * 1. Merge                               O(m+n)  O(m+n)
 * 2. Virtual merge (two pointers)        O(m+n)  O(1)
 * 3. k-th element by discarding k/2      O(log(m+n))
 * 4. Partition binary search             O(log min(m,n))
 */
public class MedianOfTwoSortedArrays {

    static double merge(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length || j < b.length) c[k++] = (j >= b.length || (i < a.length && a[i] <= b[j])) ? a[i++] : b[j++];
        int n = c.length;
        return n % 2 == 1 ? c[n / 2] : ((double) c[n / 2 - 1] + c[n / 2]) / 2;
    }

    static double virtualMerge(int[] a, int[] b) {
        int n = a.length + b.length, i = 0, j = 0, prev = 0, cur = 0;
        for (int step = 0; step <= n / 2; step++) {
            prev = cur;
            cur = (j >= b.length || (i < a.length && a[i] <= b[j])) ? a[i++] : b[j++];
        }
        return n % 2 == 1 ? cur : ((double) prev + cur) / 2;
    }

    /** k is 1-based; a[ai..], b[bi..] */
    static int kth(int[] a, int ai, int[] b, int bi, int k) {
        if (ai >= a.length) return b[bi + k - 1];
        if (bi >= b.length) return a[ai + k - 1];
        if (k == 1) return Math.min(a[ai], b[bi]);
        int half = k / 2;
        int av = ai + half - 1 < a.length ? a[ai + half - 1] : Integer.MAX_VALUE;
        int bv = bi + half - 1 < b.length ? b[bi + half - 1] : Integer.MAX_VALUE;
        return av < bv ? kth(a, ai + half, b, bi, k - half) : kth(a, ai, b, bi + half, k - half);
    }

    static double kthApproach(int[] a, int[] b) {
        int n = a.length + b.length;
        if (n % 2 == 1) return kth(a, 0, b, 0, n / 2 + 1);
        return ((double) kth(a, 0, b, 0, n / 2) + kth(a, 0, b, 0, n / 2 + 1)) / 2;
    }

    static double partition(int[] a, int[] b) {
        if (a.length > b.length) return partition(b, a);
        int m = a.length, n = b.length, lo = 0, hi = m, half = (m + n + 1) / 2;
        while (lo <= hi) {
            int i = (lo + hi) >>> 1, j = half - i;
            int aL = i == 0 ? Integer.MIN_VALUE : a[i - 1], aR = i == m ? Integer.MAX_VALUE : a[i];
            int bL = j == 0 ? Integer.MIN_VALUE : b[j - 1], bR = j == n ? Integer.MAX_VALUE : b[j];
            if (aL > bR) hi = i - 1;
            else if (bL > aR) lo = i + 1;
            else return (m + n) % 2 == 1 ? Math.max(aL, bL) : ((double) Math.max(aL, bL) + Math.min(aR, bR)) / 2;
        }
        throw new IllegalArgumentException("inputs must be sorted");
    }

    public static void main(String[] args) {
        check(partition(new int[]{1, 3}, new int[]{2}), 2.0); check(partition(new int[]{1, 2}, new int[]{3, 4}), 2.5);
        check(partition(new int[]{1, 3, 8, 9, 15}, new int[]{7, 11, 18, 19, 21, 25}), 11.0);
        check(partition(new int[]{}, new int[]{1}), 1.0);
        check(partition(new int[]{Integer.MAX_VALUE}, new int[]{Integer.MAX_VALUE}), (double) Integer.MAX_VALUE);
        Random r = new Random(15);
        for (int t = 0; t < 3000; t++) {
            int[] a = sorted(r, r.nextInt(7)), b = sorted(r, r.nextInt(7));
            if (a.length + b.length == 0) continue;
            double e = merge(a, b);
            check(virtualMerge(a, b), e); check(kthApproach(a, b), e); check(partition(a, b), e);
        }
        System.out.println("MedianOfTwoSortedArrays: all tests passed");
    }

    static int[] sorted(Random r, int n) { int[] a = new int[n]; for (int i = 0; i < n; i++) a[i] = r.nextInt(20) - 5; Arrays.sort(a); return a; }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
