import java.util.*;

/**
 * Merge two sorted arrays without extra space (GFG): a (size n) gets the n smallest, b the rest, both sorted.
 *   Brute: merge into temp array, copy back O(n+m) space
 *   Better: swap a's largest with b's smallest while a[i] > b[j], then sort both. O((n+m) log)
 *   Gap method (shell sort): gap = ceil(len/2), compare elements gap apart across both arrays. O((n+m) log(n+m)), O(1)
 * LC 271 Encode and decode strings: "len#str" for each string; delimiter-only schemes break on any content.
 * LC 380 Insert delete getRandom O(1): ArrayList of values + map value -> index; delete swaps with last.
 * LC 327 Count of range sum (sums in [lower, upper]):
 *   Brute O(n^2) on prefix sums | merge sort on prefix sums counting with two moving pointers O(n log n)
 * Maximum sum rectangle in 2D matrix (GFG):
 *   Brute: every rectangle with 2D prefix sums O(R^2 C^2) | fix column pair, Kadane on row sums O(C^2 R)
 */
public class ArrayProblemsIII {

    static void mergeBrute(long[] a, long[] b) {
        long[] t = new long[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) t[k++] = a[i] <= b[j] ? a[i++] : b[j++];
        while (i < a.length) t[k++] = a[i++];
        while (j < b.length) t[k++] = b[j++];
        for (k = 0; k < t.length; k++) if (k < a.length) a[k] = t[k]; else b[k - a.length] = t[k];
    }

    static void mergeSwapSort(long[] a, long[] b) {
        int i = a.length - 1, j = 0;
        while (i >= 0 && j < b.length && a[i] > b[j]) { long t = a[i]; a[i] = b[j]; b[j] = t; i--; j++; }
        Arrays.sort(a); Arrays.sort(b);
    }

    static void mergeGap(long[] a, long[] b) {
        int n = a.length, m = b.length, len = n + m;
        int gap = (len + 1) / 2;
        while (gap > 0) {
            for (int l = 0, r = gap; r < len; l++, r++) {
                long x = l < n ? a[l] : b[l - n], y = r < n ? a[r] : b[r - n];
                if (x > y) {
                    if (l < n) a[l] = y; else b[l - n] = y;
                    if (r < n) a[r] = x; else b[r - n] = x;
                }
            }
            gap = gap == 1 ? 0 : (gap + 1) / 2;
        }
    }

    static String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs) sb.append(s.length()).append('#').append(s);
        return sb.toString();
    }

    static List<String> decode(String s) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int hash = s.indexOf('#', i);
            int len = Integer.parseInt(s.substring(i, hash));
            res.add(s.substring(hash + 1, hash + 1 + len));
            i = hash + 1 + len;
        }
        return res;
    }

    static class RandomizedSet {
        final List<Integer> vals = new ArrayList<>();
        final Map<Integer, Integer> idx = new HashMap<>();
        final Random rnd = new Random(1);
        boolean insert(int v) { if (idx.containsKey(v)) return false; idx.put(v, vals.size()); vals.add(v); return true; }
        boolean remove(int v) {
            Integer i = idx.remove(v);
            if (i == null) return false;
            int last = vals.remove(vals.size() - 1);
            if (i < vals.size()) { vals.set(i, last); idx.put(last, i); }
            return true;
        }
        int getRandom() { return vals.get(rnd.nextInt(vals.size())); }
    }

    static int countRangeSumBrute(int[] a, int lower, int upper) {
        int c = 0;
        for (int i = 0; i < a.length; i++) { long s = 0; for (int j = i; j < a.length; j++) { s += a[j]; if (s >= lower && s <= upper) c++; } }
        return c;
    }

    static int countRangeSum(int[] a, int lower, int upper) {
        long[] pre = new long[a.length + 1];
        for (int i = 0; i < a.length; i++) pre[i + 1] = pre[i] + a[i];
        return sortCount(pre, 0, pre.length - 1, lower, upper, new long[pre.length]);
    }

    static int sortCount(long[] p, int l, int r, int lo, int hi, long[] tmp) {
        if (l >= r) return 0;
        int m = (l + r) / 2;
        int c = sortCount(p, l, m, lo, hi, tmp) + sortCount(p, m + 1, r, lo, hi, tmp);
        int s = m + 1, e = m + 1;            // for each left prefix, window of right prefixes with diff in [lo, hi]
        for (int i = l; i <= m; i++) {
            while (s <= r && p[s] - p[i] < lo) s++;
            while (e <= r && p[e] - p[i] <= hi) e++;
            c += e - s;
        }
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) tmp[k++] = p[i] <= p[j] ? p[i++] : p[j++];
        while (i <= m) tmp[k++] = p[i++];
        while (j <= r) tmp[k++] = p[j++];
        System.arraycopy(tmp, l, p, l, r - l + 1);
        return c;
    }

    static int maxSumRectangleBrute(int[][] m) {
        int R = m.length, C = m[0].length, best = Integer.MIN_VALUE;
        int[][] pre = new int[R + 1][C + 1];
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) pre[r + 1][c + 1] = m[r][c] + pre[r][c + 1] + pre[r + 1][c] - pre[r][c];
        for (int r1 = 0; r1 < R; r1++) for (int r2 = r1; r2 < R; r2++)
            for (int c1 = 0; c1 < C; c1++) for (int c2 = c1; c2 < C; c2++)
                best = Math.max(best, pre[r2 + 1][c2 + 1] - pre[r1][c2 + 1] - pre[r2 + 1][c1] + pre[r1][c1]);
        return best;
    }

    static int maxSumRectangle(int[][] m) {
        int R = m.length, C = m[0].length, best = Integer.MIN_VALUE;
        for (int left = 0; left < C; left++) {
            int[] rowSum = new int[R];
            for (int right = left; right < C; right++) {
                for (int r = 0; r < R; r++) rowSum[r] += m[r][right];
                int cur = rowSum[0], bestHere = rowSum[0];          // Kadane
                for (int r = 1; r < R; r++) { cur = Math.max(rowSum[r], cur + rowSum[r]); bestHere = Math.max(bestHere, cur); }
                best = Math.max(best, bestHere);
            }
        }
        return best;
    }

    public static void main(String[] args) {
        String ea = "[1, 2, 3, 5]", eb = "[8, 9, 10, 20]";
        long[][] in = {{1, 5, 9, 10}, {2, 3, 8, 20}};
        for (int v = 0; v < 3; v++) {
            long[] a = in[0].clone(), b = in[1].clone();
            if (v == 0) mergeBrute(a, b); else if (v == 1) mergeSwapSort(a, b); else mergeGap(a, b);
            check(Arrays.toString(a), "[1, 2, 3, 5]"); check(Arrays.toString(b), "[8, 9, 10, 20]");
        }
        long[] a2 = {10, 12}, b2 = {5, 18, 20}; mergeGap(a2, b2);
        check(Arrays.toString(a2) + Arrays.toString(b2), "[5, 10][12, 18, 20]");
        List<String> strs = List.of("lint", "co#de", "", "4#you");
        check(decode(encode(strs)), strs);
        RandomizedSet rs = new RandomizedSet();
        check(rs.insert(1), true); check(rs.remove(2), false); check(rs.insert(2), true);
        int r = rs.getRandom(); check(r == 1 || r == 2, true);
        check(rs.remove(1), true); check(rs.insert(2), false); check(rs.getRandom(), 2);
        int[] cr = {-2, 5, -1};
        check(countRangeSumBrute(cr, -2, 2), 3); check(countRangeSum(cr, -2, 2), 3);
        Random rnd = new Random(5);
        for (int t = 0; t < 50; t++) {
            int[] x = new int[rnd.nextInt(30) + 1];
            for (int i = 0; i < x.length; i++) x[i] = rnd.nextInt(21) - 10;
            check(countRangeSum(x, -3, 4), countRangeSumBrute(x, -3, 4));
        }
        int[][] m = {{1, 2, -1, -4, -20}, {-8, -3, 4, 2, 1}, {3, 8, 10, 1, 3}, {-4, -1, 1, 7, -6}};
        check(maxSumRectangleBrute(m), 29); check(maxSumRectangle(m), 29);
        System.out.println("ArrayProblemsIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
