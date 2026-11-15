import java.util.*;

/**
 * Fenwick tree (Binary Indexed Tree), 1-indexed.
 *   update(i, delta): i += i & -i     O(log n)
 *   prefix(i)       : i -= i & -i     O(log n)
 *
 * LC 315 Count of smaller numbers after self
 *   Brute : for each i scan right O(n^2)
 *   Merge sort with index tracking O(n log n)
 *   BIT   : compress values to ranks; scan from right, answer = prefix(rank - 1), then add rank. O(n log n)
 */
public class FenwickTree {
    final long[] bit;

    FenwickTree(int n) { bit = new long[n + 1]; }

    void update(int i, long delta) { for (; i < bit.length; i += i & -i) bit[i] += delta; }

    long prefix(int i) { long s = 0; for (; i > 0; i -= i & -i) s += bit[i]; return s; }

    long range(int l, int r) { return prefix(r) - prefix(l - 1); }

    static List<Integer> countSmallerBrute(int[] a) {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < a.length; i++) { int c = 0; for (int j = i + 1; j < a.length; j++) if (a[j] < a[i]) c++; r.add(c); }
        return r;
    }

    static List<Integer> countSmaller(int[] a) {
        int[] sorted = Arrays.stream(a).distinct().sorted().toArray();
        FenwickTree ft = new FenwickTree(sorted.length);
        Integer[] res = new Integer[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            int rank = Arrays.binarySearch(sorted, a[i]) + 1;     // 1-based rank
            res[i] = (int) ft.prefix(rank - 1);
            ft.update(rank, 1);
        }
        return Arrays.asList(res);
    }

    public static void main(String[] args) {
        FenwickTree ft = new FenwickTree(5);
        int[] a = {1, 3, 5, 7, 9};
        for (int i = 0; i < a.length; i++) ft.update(i + 1, a[i]);
        check(ft.prefix(3), 9L); check(ft.range(2, 4), 15L);
        ft.update(3, -5); check(ft.range(1, 5), 20L);
        int[] x = {5, 2, 6, 1};
        check(countSmaller(x), List.of(2, 1, 1, 0)); check(countSmallerBrute(x), List.of(2, 1, 1, 0));
        check(countSmaller(new int[]{-1, -1}), List.of(0, 0));
        System.out.println("FenwickTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
