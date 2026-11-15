import java.util.*;

/**
 * LC 307 Range sum query – mutable.
 *   Brute     : update O(1), query O(n)
 *   Prefix sum: query O(1), update O(n)
 *   Segment tree: both O(log n)  <-- this file
 * Range min query: same tree with min().
 * Lazy propagation: range add + range sum, both O(log n). lazy[node] = pending add for the whole segment.
 */
public class SegmentTree {

    // ---------- Sum / point update ----------
    static class SumTree {
        int n; long[] t;
        SumTree(int[] a) { n = a.length; t = new long[4 * n]; build(a, 1, 0, n - 1); }
        void build(int[] a, int node, int l, int r) {
            if (l == r) { t[node] = a[l]; return; }
            int m = (l + r) / 2;
            build(a, 2 * node, l, m); build(a, 2 * node + 1, m + 1, r);
            t[node] = t[2 * node] + t[2 * node + 1];
        }
        void update(int idx, int val) { update(1, 0, n - 1, idx, val); }
        void update(int node, int l, int r, int idx, int val) {
            if (l == r) { t[node] = val; return; }
            int m = (l + r) / 2;
            if (idx <= m) update(2 * node, l, m, idx, val); else update(2 * node + 1, m + 1, r, idx, val);
            t[node] = t[2 * node] + t[2 * node + 1];
        }
        long query(int ql, int qr) { return query(1, 0, n - 1, ql, qr); }
        long query(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;                   // no overlap
            if (ql <= l && r <= qr) return t[node];           // full overlap
            int m = (l + r) / 2;
            return query(2 * node, l, m, ql, qr) + query(2 * node + 1, m + 1, r, ql, qr);
        }
    }

    // ---------- Min ----------
    static class MinTree {
        int n; int[] t;
        MinTree(int[] a) { n = a.length; t = new int[4 * n]; build(a, 1, 0, n - 1); }
        void build(int[] a, int node, int l, int r) {
            if (l == r) { t[node] = a[l]; return; }
            int m = (l + r) / 2;
            build(a, 2 * node, l, m); build(a, 2 * node + 1, m + 1, r);
            t[node] = Math.min(t[2 * node], t[2 * node + 1]);
        }
        int query(int ql, int qr) { return query(1, 0, n - 1, ql, qr); }
        int query(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return Integer.MAX_VALUE;
            if (ql <= l && r <= qr) return t[node];
            int m = (l + r) / 2;
            return Math.min(query(2 * node, l, m, ql, qr), query(2 * node + 1, m + 1, r, ql, qr));
        }
    }

    // ---------- Lazy: range add, range sum ----------
    static class LazyTree {
        int n; long[] t, lazy;
        LazyTree(int n) { this.n = n; t = new long[4 * n]; lazy = new long[4 * n]; }
        void push(int node, int l, int r) {
            if (lazy[node] == 0 || l == r) return;
            int m = (l + r) / 2;
            apply(2 * node, l, m, lazy[node]); apply(2 * node + 1, m + 1, r, lazy[node]);
            lazy[node] = 0;
        }
        void apply(int node, int l, int r, long v) { t[node] += v * (r - l + 1); lazy[node] += v; }
        void add(int ql, int qr, long v) { add(1, 0, n - 1, ql, qr, v); }
        void add(int node, int l, int r, int ql, int qr, long v) {
            if (qr < l || r < ql) return;
            if (ql <= l && r <= qr) { apply(node, l, r, v); return; }
            push(node, l, r);
            int m = (l + r) / 2;
            add(2 * node, l, m, ql, qr, v); add(2 * node + 1, m + 1, r, ql, qr, v);
            t[node] = t[2 * node] + t[2 * node + 1];
        }
        long sum(int ql, int qr) { return sum(1, 0, n - 1, ql, qr); }
        long sum(int node, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;
            if (ql <= l && r <= qr) return t[node];
            push(node, l, r);
            int m = (l + r) / 2;
            return sum(2 * node, l, m, ql, qr) + sum(2 * node + 1, m + 1, r, ql, qr);
        }
    }

    public static void main(String[] args) {
        SumTree st = new SumTree(new int[]{1, 3, 5});
        check(st.query(0, 2), 9L); st.update(1, 2); check(st.query(0, 2), 8L); check(st.query(1, 1), 2L);
        MinTree mt = new MinTree(new int[]{5, 2, 8, 1, 9, 3});
        check(mt.query(0, 2), 2); check(mt.query(2, 5), 1); check(mt.query(4, 5), 3);

        Random rnd = new Random(3);
        int n = 50; long[] brute = new long[n];
        LazyTree lt = new LazyTree(n);
        for (int op = 0; op < 500; op++) {
            int l = rnd.nextInt(n), r = l + rnd.nextInt(n - l);
            if (rnd.nextBoolean()) { int v = rnd.nextInt(21) - 10; lt.add(l, r, v); for (int i = l; i <= r; i++) brute[i] += v; }
            else { long s = 0; for (int i = l; i <= r; i++) s += brute[i]; check(lt.sum(l, r), s); }
        }
        System.out.println("SegmentTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
