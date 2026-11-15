import java.util.*;

/**
 * LC 324 Wiggle sort II (a0 < a1 > a2 < a3 ...):
 *   A) sort, then fill odd indices from the end of the large half and even indices from the end of the small half. O(n log n), O(n)
 *   B) median by quickselect + 3-way partition with virtual index idx(i) = (1 + 2i) % (n | 1). O(n) avg, O(1) extra
 * LC 164 Maximum gap (in sorted order, O(n) required):
 *   A) sort and scan O(n log n)
 *   B) pigeonhole: n-1 buckets of size ceil((max-min)/(n-1)); the max gap is never inside a bucket,
 *      so only compare bucket min with previous bucket max. O(n)
 */
public class SortingProblemsII {

    static void wiggleSortSorting(int[] a) {
        int[] s = a.clone(); Arrays.sort(s);
        int n = a.length, small = (n - 1) / 2, large = n - 1;
        for (int i = 0; i < n; i++) a[i] = (i % 2 == 0) ? s[small--] : s[large--];
    }

    static void wiggleSort(int[] a) {
        int n = a.length;
        int median = quickSelect(a.clone(), n / 2);
        int left = 0, i = 0, right = n - 1;
        while (i <= right) {
            int vi = idx(i, n);
            if (a[vi] > median) swap(a, idx(left++, n), idx(i++, n));
            else if (a[vi] < median) swap(a, idx(right--, n), vi);
            else i++;
        }
    }

    static int idx(int i, int n) { return (1 + 2 * i) % (n | 1); }   // 1, 3, 5, ..., 0, 2, 4, ...

    static int quickSelect(int[] a, int k) {
        int l = 0, r = a.length - 1;
        Random rnd = new Random(7);
        while (true) {
            int p = l + rnd.nextInt(r - l + 1);
            swap(a, p, r);
            int store = l;
            for (int i = l; i < r; i++) if (a[i] < a[r]) swap(a, i, store++);
            swap(a, store, r);
            if (store == k) return a[store];
            if (store < k) l = store + 1; else r = store - 1;
        }
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    static boolean isWiggle(int[] a) {
        for (int i = 1; i < a.length; i++) if (i % 2 == 1 ? a[i] <= a[i - 1] : a[i] >= a[i - 1]) return false;
        return true;
    }

    static int maximumGapSort(int[] a) {
        if (a.length < 2) return 0;
        int[] s = a.clone(); Arrays.sort(s);
        int best = 0;
        for (int i = 1; i < s.length; i++) best = Math.max(best, s[i] - s[i - 1]);
        return best;
    }

    static int maximumGap(int[] a) {
        int n = a.length;
        if (n < 2) return 0;
        int min = Arrays.stream(a).min().getAsInt(), max = Arrays.stream(a).max().getAsInt();
        if (min == max) return 0;
        int size = Math.max(1, (max - min + n - 2) / (n - 1));      // ceil((max-min)/(n-1))
        int buckets = (max - min) / size + 1;
        int[] bMin = new int[buckets], bMax = new int[buckets];
        Arrays.fill(bMin, Integer.MAX_VALUE); Arrays.fill(bMax, Integer.MIN_VALUE);
        for (int x : a) {
            int b = (x - min) / size;
            bMin[b] = Math.min(bMin[b], x); bMax[b] = Math.max(bMax[b], x);
        }
        int best = 0, prevMax = min;
        for (int b = 0; b < buckets; b++) {
            if (bMin[b] == Integer.MAX_VALUE) continue;           // empty bucket
            best = Math.max(best, bMin[b] - prevMax);
            prevMax = bMax[b];
        }
        return best;
    }

    public static void main(String[] args) {
        Random rnd = new Random(11);
        for (int t = 0; t < 200; t++) {
            int n = rnd.nextInt(12) + 1;
            int[] base = new int[n];
            for (int i = 0; i < n; i++) base[i] = rnd.nextInt(5);
            int[] s = base.clone(); Arrays.sort(s);
            int[] x = base.clone(); wiggleSortSorting(x);
            if (!isWiggle(x)) continue;          // no valid wiggle exists for this input
            int[] y = base.clone(); wiggleSort(y);
            check(isWiggle(y), true);
            int[] ys = y.clone(); Arrays.sort(ys); check(Arrays.toString(ys), Arrays.toString(s));
        }
        int[] w = {1, 5, 1, 1, 6, 4}; wiggleSort(w); check(isWiggle(w), true);
        int[] w2 = {1, 3, 2, 2, 3, 1}; wiggleSortSorting(w2); check(isWiggle(w2), true);
        check(maximumGap(new int[]{3, 6, 9, 1}), 3); check(maximumGap(new int[]{10}), 0);
        for (int t = 0; t < 200; t++) {
            int[] a = new int[rnd.nextInt(20) + 1];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(1000);
            check(maximumGap(a), maximumGapSort(a));
        }
        System.out.println("SortingProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
