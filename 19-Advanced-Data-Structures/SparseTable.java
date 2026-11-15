/**
 * Sparse table for static Range Minimum Query.
 *   Build: st[k][i] = min of a[i .. i + 2^k - 1] = min(st[k-1][i], st[k-1][i + 2^(k-1)])   O(n log n)
 *   Query [l, r]: k = log2(r - l + 1); min(st[k][l], st[k][r - 2^k + 1])  O(1)  (overlap is fine for min/max/gcd)
 * Compare: brute O(n) per query; segment tree O(log n) per query but supports updates.
 */
public class SparseTable {
    final int[][] st;
    final int[] log;

    SparseTable(int[] a) {
        int n = a.length;
        log = new int[n + 1];
        for (int i = 2; i <= n; i++) log[i] = log[i / 2] + 1;
        int K = log[n] + 1;
        st = new int[K][n];
        st[0] = a.clone();
        for (int k = 1; k < K; k++)
            for (int i = 0; i + (1 << k) <= n; i++) st[k][i] = Math.min(st[k - 1][i], st[k - 1][i + (1 << (k - 1))]);
    }

    int query(int l, int r) {
        int k = log[r - l + 1];
        return Math.min(st[k][l], st[k][r - (1 << k) + 1]);
    }

    public static void main(String[] args) {
        int[] a = {7, 2, 3, 0, 5, 10, 3, 12, 18};
        SparseTable s = new SparseTable(a);
        for (int l = 0; l < a.length; l++)
            for (int r = l; r < a.length; r++) {
                int m = Integer.MAX_VALUE;
                for (int i = l; i <= r; i++) m = Math.min(m, a[i]);
                check(s.query(l, r), m);
            }
        System.out.println("SparseTable: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
