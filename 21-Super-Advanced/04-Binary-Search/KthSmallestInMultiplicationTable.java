import java.util.*;

/**
 * LC 668 — Kth Smallest Number in Multiplication Table. Explanation: KthSmallestInMultiplicationTable.md
 * 1. Build + sort                      O(mn log mn)
 * 2. Heap merge of m rows              O(m + k log m)
 * 3. Binary search on value + count    O(m log mn)
 */
public class KthSmallestInMultiplicationTable {

    static int buildSort(int m, int n, int k) {
        int[] all = new int[m * n];
        for (int i = 1, p = 0; i <= m; i++) for (int j = 1; j <= n; j++) all[p++] = i * j;
        Arrays.sort(all);
        return all[k - 1];
    }

    static int heap(int m, int n, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0] * x[1]));   // {row, col}
        for (int i = 1; i <= m; i++) pq.offer(new int[]{i, 1});
        int[] c = null;
        while (k-- > 0) { c = pq.poll(); if (c[1] < n) pq.offer(new int[]{c[0], c[1] + 1}); }
        return c[0] * c[1];
    }

    static int findKthNumber(int m, int n, int k) {
        if (m > n) { int t = m; m = n; n = t; }          // loop over the smaller dimension
        int lo = 1, hi = m * n;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (count(mid, m, n) >= k) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static long count(int x, int m, int n) {
        int full = Math.min(m, x / n);                   // rows 1..full contribute all n columns
        long c = (long) full * n;
        for (int i = full + 1; i <= m; i++) c += x / i;
        return c;
    }

    public static void main(String[] args) {
        check(findKthNumber(3, 3, 5), 3); check(findKthNumber(2, 3, 6), 6); check(findKthNumber(30000, 30000, 900000000), 900000000);
        Random r = new Random(18);
        for (int t = 0; t < 2000; t++) {
            int m = r.nextInt(8) + 1, n = r.nextInt(8) + 1, k = r.nextInt(m * n) + 1;
            int e = buildSort(m, n, k);
            check(heap(m, n, k), e); check(findKthNumber(m, n, k), e);
        }
        System.out.println("KthSmallestInMultiplicationTable: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
