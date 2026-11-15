import java.util.*;

/**
 * LC 315 — Count of Smaller Numbers After Self. Explanation: CountOfSmallerNumbersAfterSelf.md
 * 1. Brute force                                O(n^2)
 * 2. Sorted list + binary search from right     O(n^2) worst (shifts), fast in practice
 * 3. Fenwick tree over compressed values        O(n log n)
 * 4. Merge sort with an index array             O(n log n)
 */
public class CountOfSmallerNumbersAfterSelf {

    static List<Integer> brute(int[] a) {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < a.length; i++) { int c = 0; for (int j = i + 1; j < a.length; j++) if (a[j] < a[i]) c++; r.add(c); }
        return r;
    }

    static List<Integer> sortedList(int[] a) {
        Integer[] res = new Integer[a.length];
        List<Integer> seen = new ArrayList<>();
        for (int i = a.length - 1; i >= 0; i--) {
            int lo = 0, hi = seen.size();
            while (lo < hi) { int m = (lo + hi) >>> 1; if (seen.get(m) < a[i]) lo = m + 1; else hi = m; }
            res[i] = lo;
            seen.add(lo, a[i]);
        }
        return Arrays.asList(res);
    }

    static List<Integer> fenwick(int[] a) {
        int[] sorted = Arrays.stream(a).distinct().sorted().toArray();
        int[] bit = new int[sorted.length + 1];
        Integer[] res = new Integer[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            int rank = Arrays.binarySearch(sorted, a[i]) + 1, c = 0;
            for (int j = rank - 1; j > 0; j -= j & -j) c += bit[j];
            res[i] = c;
            for (int j = rank; j < bit.length; j += j & -j) bit[j]++;
        }
        return Arrays.asList(res);
    }

    static int[] cnt;

    static List<Integer> mergeSort(int[] a) {
        int n = a.length;
        cnt = new int[n];
        int[] idx = new int[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        sort(a, idx, 0, n - 1, new int[n]);
        List<Integer> r = new ArrayList<>();
        for (int c : cnt) r.add(c);
        return r;
    }

    static void sort(int[] a, int[] idx, int l, int r, int[] tmp) {
        if (l >= r) return;
        int m = (l + r) / 2;
        sort(a, idx, l, m, tmp); sort(a, idx, m + 1, r, tmp);
        int i = l, j = m + 1, k = l, rightTaken = 0;
        while (i <= m || j <= r) {
            if (j > r || (i <= m && a[idx[i]] <= a[idx[j]])) { cnt[idx[i]] += rightTaken; tmp[k++] = idx[i++]; }
            else { rightTaken++; tmp[k++] = idx[j++]; }
        }
        System.arraycopy(tmp, l, idx, l, r - l + 1);
    }

    public static void main(String[] args) {
        check(fenwick(new int[]{5, 2, 6, 1}), List.of(2, 1, 1, 0)); check(mergeSort(new int[]{-1, -1}), List.of(0, 0));
        Random r = new Random(67);
        for (int t = 0; t < 3000; t++) {
            int[] a = new int[r.nextInt(15)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(11) - 5;
            List<Integer> e = brute(a);
            check(sortedList(a), e); check(fenwick(a), e); check(mergeSort(a), e);
        }
        System.out.println("CountOfSmallerNumbersAfterSelf: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
