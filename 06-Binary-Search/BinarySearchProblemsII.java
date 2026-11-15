import java.util.*;

/**
 * LC 1539 Kth missing positive: brute walk numbers O(n + k) | missing(i) = a[i] - (i+1) is monotonic -> BS. Answer = lo + k.
 * Nth root of an integer (GFG): m with m^n == M else -1. Brute 1..M | BS with early-exit power (no overflow).
 * LC 1283 Smallest divisor given a threshold: BS on divisor in [1, max], sum of ceilings.
 * LC 658 Find K closest elements: brute sort by distance O(n log n) | BS on window start in [0, n-k]. O(log(n-k) + k)
 * Row with maximum 1s (GFG, rows sorted 0s then 1s): brute count O(nm) | lower_bound per row O(n log m) | staircase O(n+m)
 * LC 1901 Find a peak element II: BS on columns; take max of mid column, move toward the bigger neighbour. O(m log n)
 * LC 981 Time based key-value store: per key, list of (timestamp, value) in increasing time; floor search.
 * LC 378 Kth smallest in sorted matrix: min-heap O(k log n) | BS on value with staircase count O(n log(range))
 * Median of row-wise sorted matrix (GFG): BS on value; count(<= mid) via upper bound per row. O(R log C log range)
 * Kth element of two sorted arrays (GFG): merge O(k) | partition BS on the smaller array O(log min(n, m))
 * LC 774 Minimize max distance to gas station: greedy heap (add each station to largest current piece) O(k log n)
 *   | BS on the answer (double), needed(d) = sum(ceil(gap/d) - 1) <= k. O(n log(range/eps))
 * LC 1095 Find in mountain array: find peak by BS, then BS ascending half, then descending half. ~3 log n calls.
 */
public class BinarySearchProblemsII {

    static int findKthPositiveBrute(int[] a, int k) {
        int i = 0;
        for (int x = 1; ; x++) {
            if (i < a.length && a[i] == x) i++;
            else if (--k == 0) return x;
        }
    }

    static int findKthPositive(int[] a, int k) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] - (mid + 1) < k) lo = mid + 1; else hi = mid - 1;
        }
        return lo + k;   // lo numbers from the array are <= answer
    }

    static int nthRootBrute(int n, int m) {
        for (int x = 1; x <= m; x++) { long p = 1; for (int i = 0; i < n && p <= m; i++) p *= x; if (p == m) return x; if (p > m) break; }
        return -1;
    }

    /** returns 1 if mid^n == m, 0 if less, 2 if greater */
    static int cmpPow(long mid, int n, long m) {
        long p = 1;
        for (int i = 0; i < n; i++) { p *= mid; if (p > m) return 2; }
        return p == m ? 1 : 0;
    }

    static int nthRoot(int n, int m) {
        long lo = 1, hi = m;
        while (lo <= hi) {
            long mid = (lo + hi) / 2;
            int c = cmpPow(mid, n, m);
            if (c == 1) return (int) mid;
            if (c == 0) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }

    static int smallestDivisor(int[] a, int threshold) {
        int lo = 1, hi = Arrays.stream(a).max().getAsInt();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            long s = 0;
            for (int x : a) s += (x + mid - 1) / mid;
            if (s <= threshold) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static List<Integer> findClosestBrute(int[] a, int k, int x) {
        List<Integer> l = new ArrayList<>();
        for (int v : a) l.add(v);
        l.sort((p, q) -> Math.abs(p - x) != Math.abs(q - x) ? Math.abs(p - x) - Math.abs(q - x) : p - q);
        List<Integer> r = new ArrayList<>(l.subList(0, k));
        Collections.sort(r);
        return r;
    }

    static List<Integer> findClosestElements(int[] a, int k, int x) {
        int lo = 0, hi = a.length - k;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (x - a[mid] > a[mid + k] - x) lo = mid + 1; else hi = mid;   // window [mid, mid+k) vs shifting right
        }
        List<Integer> r = new ArrayList<>();
        for (int i = lo; i < lo + k; i++) r.add(a[i]);
        return r;
    }

    static int rowWithMax1sBrute(int[][] m) {
        int best = -1, bestCount = 0;
        for (int r = 0; r < m.length; r++) { int c = 0; for (int v : m[r]) c += v; if (c > bestCount) { bestCount = c; best = r; } }
        return best;
    }

    static int rowWithMax1s(int[][] m) {
        int r = 0, c = m[0].length - 1, best = -1;
        while (r < m.length && c >= 0) {
            if (m[r][c] == 1) { best = r; c--; }   // this row has more 1s than any earlier one
            else r++;
        }
        return best;
    }

    static int[] findPeakGrid(int[][] g) {
        int lo = 0, hi = g[0].length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2, row = 0;
            for (int r = 0; r < g.length; r++) if (g[r][mid] > g[row][mid]) row = r;
            int left = mid > 0 ? g[row][mid - 1] : -1, right = mid < g[0].length - 1 ? g[row][mid + 1] : -1;
            if (g[row][mid] > left && g[row][mid] > right) return new int[]{row, mid};
            if (left > g[row][mid]) hi = mid - 1; else lo = mid + 1;
        }
        return new int[]{-1, -1};
    }

    static class TimeMap {
        final Map<String, List<Object[]>> map = new HashMap<>();
        void set(String key, String value, int t) { map.computeIfAbsent(key, k -> new ArrayList<>()).add(new Object[]{t, value}); }
        String get(String key, int t) {
            List<Object[]> l = map.get(key);
            if (l == null) return "";
            int lo = 0, hi = l.size() - 1, ans = -1;
            while (lo <= hi) {
                int mid = (lo + hi) / 2;
                if ((int) l.get(mid)[0] <= t) { ans = mid; lo = mid + 1; } else hi = mid - 1;
            }
            return ans == -1 ? "" : (String) l.get(ans)[1];
        }
    }

    static int kthSmallestHeap(int[][] m, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> m[x[0]][x[1]]));
        for (int r = 0; r < Math.min(m.length, k); r++) pq.offer(new int[]{r, 0});
        while (--k > 0) {
            int[] c = pq.poll();
            if (c[1] + 1 < m[0].length) pq.offer(new int[]{c[0], c[1] + 1});
        }
        int[] c = pq.peek();
        return m[c[0]][c[1]];
    }

    static int kthSmallest(int[][] m, int k) {
        int n = m.length, lo = m[0][0], hi = m[n - 1][n - 1];
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2, cnt = 0;
            for (int r = n - 1, c = 0; r >= 0 && c < n; ) {
                if (m[r][c] <= mid) { cnt += r + 1; c++; } else r--;   // staircase from bottom-left
            }
            if (cnt >= k) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static int medianBrute(int[][] m) {
        List<Integer> all = new ArrayList<>();
        for (int[] row : m) for (int v : row) all.add(v);
        Collections.sort(all);
        return all.get(all.size() / 2);
    }

    static int medianRowSorted(int[][] m) {
        int lo = Integer.MAX_VALUE, hi = Integer.MIN_VALUE, R = m.length, C = m[0].length;
        for (int[] row : m) { lo = Math.min(lo, row[0]); hi = Math.max(hi, row[C - 1]); }
        int need = R * C / 2 + 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2, cnt = 0;
            for (int[] row : m) cnt += upperBound(row, mid);
            if (cnt >= need) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) { int mid = (lo + hi) / 2; if (a[mid] <= x) lo = mid + 1; else hi = mid; }
        return lo;
    }

    static int kthTwoSortedMerge(int[] a, int[] b, int k) {
        int i = 0, j = 0, v = 0;
        while (k-- > 0) v = (j >= b.length || (i < a.length && a[i] <= b[j])) ? a[i++] : b[j++];
        return v;
    }

    static int kthTwoSorted(int[] a, int[] b, int k) {
        if (a.length > b.length) return kthTwoSorted(b, a, k);
        int m = a.length, n = b.length, lo = Math.max(0, k - n), hi = Math.min(k, m);
        while (lo <= hi) {
            int i = (lo + hi) / 2, j = k - i;               // take i from a, j from b
            int aL = i == 0 ? Integer.MIN_VALUE : a[i - 1], aR = i == m ? Integer.MAX_VALUE : a[i];
            int bL = j == 0 ? Integer.MIN_VALUE : b[j - 1], bR = j == n ? Integer.MAX_VALUE : b[j];
            if (aL <= bR && bL <= aR) return Math.max(aL, bL);
            if (aL > bR) hi = i - 1; else lo = i + 1;
        }
        return -1;
    }

    static double minmaxGasDistHeap(int[] st, int k) {
        PriorityQueue<double[]> pq = new PriorityQueue<>((x, y) -> Double.compare(y[0] / (y[1] + 1), x[0] / (x[1] + 1)));
        for (int i = 0; i + 1 < st.length; i++) pq.offer(new double[]{st[i + 1] - st[i], 0}); // {gap, stations added}
        while (k-- > 0) { double[] g = pq.poll(); g[1]++; pq.offer(g); }
        double[] g = pq.peek();
        return g[0] / (g[1] + 1);
    }

    static double minmaxGasDist(int[] st, int k) {
        double lo = 0, hi = st[st.length - 1] - st[0];
        for (int it = 0; it < 100; it++) {
            double mid = (lo + hi) / 2;
            long need = 0;
            for (int i = 0; i + 1 < st.length; i++) need += (long) Math.ceil((st[i + 1] - st[i]) / mid) - 1;
            if (need <= k) hi = mid; else lo = mid;
        }
        return hi;
    }

    static class MountainArray {
        final int[] a; int calls = 0;
        MountainArray(int[] a) { this.a = a; }
        int get(int i) { calls++; return a[i]; }
        int length() { return a.length; }
    }

    static int findInMountainArray(int target, MountainArray m) {
        int n = m.length(), lo = 0, hi = n - 1;
        while (lo < hi) { int mid = (lo + hi) / 2; if (m.get(mid) < m.get(mid + 1)) lo = mid + 1; else hi = mid; }
        int peak = lo;
        lo = 0; hi = peak;
        while (lo <= hi) {
            int mid = (lo + hi) / 2, v = m.get(mid);
            if (v == target) return mid;
            if (v < target) lo = mid + 1; else hi = mid - 1;
        }
        lo = peak + 1; hi = n - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2, v = m.get(mid);
            if (v == target) return mid;
            if (v > target) lo = mid + 1; else hi = mid - 1;   // descending side
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] a = {2, 3, 4, 7, 11};
        check(findKthPositiveBrute(a, 5), 9); check(findKthPositive(a, 5), 9); check(findKthPositive(new int[]{1, 2, 3, 4}, 2), 6);
        check(nthRootBrute(3, 27), 3); check(nthRoot(3, 27), 3); check(nthRoot(4, 69), -1); check(nthRoot(30, 1000000000), -1);
        check(smallestDivisor(new int[]{1, 2, 5, 9}, 6), 5); check(smallestDivisor(new int[]{44, 22, 33, 11, 1}, 5), 44);
        int[] c = {1, 2, 3, 4, 5};
        check(findClosestElements(c, 4, 3), List.of(1, 2, 3, 4)); check(findClosestBrute(c, 4, -1), List.of(1, 2, 3, 4));
        check(findClosestElements(new int[]{1, 1, 2, 3, 4, 5}, 4, -1), List.of(1, 1, 2, 3));
        int[][] ones = {{0, 1, 1, 1}, {0, 0, 1, 1}, {1, 1, 1, 1}, {0, 0, 0, 0}};
        check(rowWithMax1sBrute(ones), 2); check(rowWithMax1s(ones), 2);
        int[][] pg = {{10, 20, 15}, {21, 30, 14}, {7, 16, 32}};
        int[] p = findPeakGrid(pg);
        int v = pg[p[0]][p[1]];
        check(v == 30 || v == 32, true);
        TimeMap tm = new TimeMap();
        tm.set("foo", "bar", 1);
        check(tm.get("foo", 1), "bar"); check(tm.get("foo", 3), "bar");
        tm.set("foo", "bar2", 4);
        check(tm.get("foo", 4), "bar2"); check(tm.get("foo", 5), "bar2"); check(tm.get("foo", 0), "");
        int[][] sm = {{1, 5, 9}, {10, 11, 13}, {12, 13, 15}};
        check(kthSmallestHeap(sm, 8), 13); check(kthSmallest(sm, 8), 13);
        int[][] rm = {{1, 3, 5}, {2, 6, 9}, {3, 6, 9}};
        check(medianBrute(rm), 5); check(medianRowSorted(rm), 5);
        int[] x = {2, 3, 6, 7, 9}, y = {1, 4, 8, 10};
        for (int k = 1; k <= 9; k++) check(kthTwoSorted(x, y, k), kthTwoSortedMerge(x, y, k));
        int[] st = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        check(Math.abs(minmaxGasDist(st, 9) - 0.5) < 1e-6, true); check(Math.abs(minmaxGasDistHeap(st, 9) - 0.5) < 1e-9, true);
        int[] st2 = {23, 24, 36, 39, 46, 56, 57, 65, 84, 98};
        check(Math.abs(minmaxGasDist(st2, 1) - minmaxGasDistHeap(st2, 1)) < 1e-6, true);
        MountainArray ma = new MountainArray(new int[]{1, 2, 3, 4, 5, 3, 1});
        check(findInMountainArray(3, ma), 2); check(findInMountainArray(0, new MountainArray(new int[]{0, 1, 2, 4, 2, 1})), 0);
        check(findInMountainArray(1, new MountainArray(new int[]{0, 5, 3, 1})), 3);
        System.out.println("BinarySearchProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
