import java.util.*;

/**
 * LC 187  Repeated DNA sequences (10-letter): HashSet of substrings O(10n) | 2 bits per base -> 20-bit rolling code O(n)
 * LC 1146 Snapshot array: copy whole array per snap O(n) per snap | per index list of (snapId, value) + binary search O(log S)
 * LC 308  Range sum query 2D – mutable: naive update O(1) / query O(RC) | 2D Fenwick tree, both O(log R log C)
 * LC 732  My calendar III (max overlapping bookings): sweep line TreeMap of +1/-1, scan O(n) per booking
 *         | dynamic segment tree with lazy range add + max, O(log range) per booking
 * LC 218  The skyline problem: brute height at every critical x O(n^2) | sweep events + TreeMap multiset of heights O(n log n)
 * LC 1044 Longest duplicate substring: brute all substrings in a set O(n^3) | binary search length + Rabin–Karp O(n log n)
 * Manacher's algorithm (LC 5 in O(n)): transform to ^#a#b#$, keep the rightmost palindrome (center, right),
 *         mirror radius = min(right - i, P[mirror]) and expand. Compare with expand-around-centre O(n^2).
 * Count inversions with Fenwick tree (GFG): compress values, scan right to left, add count of smaller seen so far. O(n log n)
 */
public class AdvancedProblemsII {

    static List<String> findRepeatedDnaBrute(String s) {
        Set<String> seen = new HashSet<>(), rep = new TreeSet<>();
        for (int i = 0; i + 10 <= s.length(); i++) { String t = s.substring(i, i + 10); if (!seen.add(t)) rep.add(t); }
        return new ArrayList<>(rep);
    }

    static List<String> findRepeatedDnaSequences(String s) {
        Map<Character, Integer> code = Map.of('A', 0, 'C', 1, 'G', 2, 'T', 3);
        Set<Integer> seen = new HashSet<>(), added = new HashSet<>();
        List<String> res = new ArrayList<>();
        int h = 0, mask = (1 << 20) - 1;
        for (int i = 0; i < s.length(); i++) {
            h = ((h << 2) | code.get(s.charAt(i))) & mask;   // keep the last 10 bases
            if (i >= 9 && !seen.add(h) && added.add(h)) res.add(s.substring(i - 9, i + 1));
        }
        Collections.sort(res);
        return res;
    }

    static class SnapshotArray {
        final List<List<int[]>> hist = new ArrayList<>();   // per index: {snapId, value}
        int snap = 0;
        SnapshotArray(int n) { for (int i = 0; i < n; i++) { List<int[]> l = new ArrayList<>(); l.add(new int[]{0, 0}); hist.add(l); } }
        void set(int i, int v) {
            List<int[]> l = hist.get(i);
            if (l.get(l.size() - 1)[0] == snap) l.get(l.size() - 1)[1] = v; else l.add(new int[]{snap, v});
        }
        int snap() { return snap++; }
        int get(int i, int id) {
            List<int[]> l = hist.get(i);
            int lo = 0, hi = l.size() - 1;
            while (lo < hi) { int mid = (lo + hi + 1) / 2; if (l.get(mid)[0] <= id) lo = mid; else hi = mid - 1; }
            return l.get(lo)[1];
        }
    }

    static class NumMatrix2D {
        final int[][] a; final int[][] bit; final int R, C;
        NumMatrix2D(int[][] m) {
            R = m.length; C = m[0].length; a = new int[R][C]; bit = new int[R + 1][C + 1];
            for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) update(r, c, m[r][c]);
        }
        void update(int r, int c, int val) {
            int delta = val - a[r][c];
            a[r][c] = val;
            for (int i = r + 1; i <= R; i += i & -i) for (int j = c + 1; j <= C; j += j & -j) bit[i][j] += delta;
        }
        int prefix(int r, int c) {    // sum of a[0..r-1][0..c-1]
            int s = 0;
            for (int i = r; i > 0; i -= i & -i) for (int j = c; j > 0; j -= j & -j) s += bit[i][j];
            return s;
        }
        int sumRegion(int r1, int c1, int r2, int c2) { return prefix(r2 + 1, c2 + 1) - prefix(r1, c2 + 1) - prefix(r2 + 1, c1) + prefix(r1, c1); }
    }

    static class MyCalendarThreeSweep {
        final TreeMap<Integer, Integer> delta = new TreeMap<>();
        int book(int s, int e) {
            delta.merge(s, 1, Integer::sum); delta.merge(e, -1, Integer::sum);
            int cur = 0, best = 0;
            for (int d : delta.values()) { cur += d; best = Math.max(best, cur); }
            return best;
        }
    }

    static class MyCalendarThree {       // dynamic segment tree over [0, 1e9]: range +1, global max
        static class Seg { Seg l, r; int max, lazy; }
        final Seg root = new Seg();
        int book(int s, int e) { add(root, 0, 1_000_000_000, s, e - 1); return root.max; }
        void add(Seg n, int lo, int hi, int s, int e) {
            if (e < lo || hi < s) return;
            if (s <= lo && hi <= e) { n.max++; n.lazy++; return; }
            int mid = lo + (hi - lo) / 2;
            if (n.l == null) n.l = new Seg();
            if (n.r == null) n.r = new Seg();
            add(n.l, lo, mid, s, e); add(n.r, mid + 1, hi, s, e);
            n.max = n.lazy + Math.max(n.l.max, n.r.max);   // lazy applies to the whole segment (never pushed down)
        }
    }

    static List<List<Integer>> getSkylineBrute(int[][] b) {
        TreeSet<Integer> xs = new TreeSet<>();
        for (int[] x : b) { xs.add(x[0]); xs.add(x[1]); }
        List<List<Integer>> res = new ArrayList<>();
        int prev = 0;
        for (int x : xs) {
            int h = 0;
            for (int[] bd : b) if (bd[0] <= x && x < bd[1]) h = Math.max(h, bd[2]);
            if (h != prev) { res.add(List.of(x, h)); prev = h; }
        }
        return res;
    }

    static List<List<Integer>> getSkyline(int[][] buildings) {
        List<int[]> ev = new ArrayList<>();
        for (int[] b : buildings) { ev.add(new int[]{b[0], -b[2]}); ev.add(new int[]{b[1], b[2]}); }  // start = negative height
        ev.sort((x, y) -> x[0] != y[0] ? x[0] - y[0] : x[1] - y[1]);   // at same x: taller starts first, ends after starts
        TreeMap<Integer, Integer> heights = new TreeMap<>(Map.of(0, 1));
        List<List<Integer>> res = new ArrayList<>();
        int prev = 0;
        for (int[] e : ev) {
            if (e[1] < 0) heights.merge(-e[1], 1, Integer::sum);
            else if (heights.merge(e[1], -1, Integer::sum) == 0) heights.remove(e[1]);
            int cur = heights.lastKey();
            if (cur != prev) { res.add(List.of(e[0], cur)); prev = cur; }
        }
        return res;
    }

    static String longestDupSubstringBrute(String s) {
        String best = "";
        for (int len = s.length() - 1; len > 0; len--) {
            Set<String> seen = new HashSet<>();
            for (int i = 0; i + len <= s.length(); i++) if (!seen.add(s.substring(i, i + len))) return s.substring(i, i + len);
        }
        return best;
    }

    static String longestDupSubstring(String s) {
        int lo = 1, hi = s.length() - 1, start = -1, bestLen = 0;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            int at = dupOfLength(s, mid);
            if (at >= 0) { start = at; bestLen = mid; lo = mid + 1; } else hi = mid - 1;
        }
        return start < 0 ? "" : s.substring(start, start + bestLen);
    }

    /** Rabin–Karp: start index of a repeated substring of length L, or -1. Hash hits are verified. */
    static int dupOfLength(String s, int L) {
        final long MOD = 1_000_000_007L, B = 131;      // products stay < 2^63; collisions are handled by verification
        long h = 0, pow = 1;
        for (int i = 0; i < L; i++) { h = (h * B + s.charAt(i)) % MOD; if (i < L - 1) pow = pow * B % MOD; }
        Map<Long, List<Integer>> seen = new HashMap<>();
        seen.computeIfAbsent(h, k -> new ArrayList<>()).add(0);
        for (int i = 1; i + L <= s.length(); i++) {
            h = (h - s.charAt(i - 1) * pow % MOD + MOD) % MOD;
            h = (h * B + s.charAt(i + L - 1)) % MOD;
            List<Integer> starts = seen.computeIfAbsent(h, k -> new ArrayList<>());
            for (int j : starts) if (s.regionMatches(j, s, i, L)) return i;
            starts.add(i);
        }
        return -1;
    }

    static String longestPalindromeExpand(String s) {
        int bs = 0, bl = 0;
        for (int c = 0; c < s.length(); c++)
            for (int[] lr : new int[][]{{c, c}, {c, c + 1}}) {
                int l = lr[0], r = lr[1];
                while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
                if (r - l - 1 > bl) { bl = r - l - 1; bs = l + 1; }
            }
        return s.substring(bs, bs + bl);
    }

    static String manacher(String s) {
        if (s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("^");
        for (char c : s.toCharArray()) sb.append('#').append(c);
        sb.append("#$");
        char[] t = sb.toString().toCharArray();
        int n = t.length, center = 0, right = 0;
        int[] p = new int[n];
        for (int i = 1; i < n - 1; i++) {
            if (i < right) p[i] = Math.min(right - i, p[2 * center - i]);   // mirror inside the known palindrome
            while (t[i + p[i] + 1] == t[i - p[i] - 1]) p[i]++;              // sentinels ^ and $ stop the expansion
            if (i + p[i] > right) { center = i; right = i + p[i]; }
        }
        int best = 0, at = 0;
        for (int i = 1; i < n - 1; i++) if (p[i] > best) { best = p[i]; at = i; }
        int start = (at - best) / 2;              // map back to original indices
        return s.substring(start, start + best);
    }

    static long countInversionsBrute(int[] a) {
        long c = 0;
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) if (a[i] > a[j]) c++;
        return c;
    }

    static long countInversionsFenwick(int[] a) {
        int[] sorted = Arrays.stream(a).distinct().sorted().toArray();
        long[] bit = new long[sorted.length + 1];
        long inv = 0;
        for (int i = a.length - 1; i >= 0; i--) {
            int rank = Arrays.binarySearch(sorted, a[i]) + 1;
            for (int j = rank - 1; j > 0; j -= j & -j) inv += bit[j];       // strictly smaller elements to the right
            for (int j = rank; j < bit.length; j += j & -j) bit[j]++;
        }
        return inv;
    }

    public static void main(String[] args) {
        String dna = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        check(findRepeatedDnaSequences(dna), List.of("AAAAACCCCC", "CCCCCAAAAA")); check(findRepeatedDnaBrute(dna), findRepeatedDnaSequences(dna));
        check(findRepeatedDnaSequences("AAAAAAAAAAAAA"), List.of("AAAAAAAAAA"));
        SnapshotArray sa = new SnapshotArray(3);
        sa.set(0, 5); check(sa.snap(), 0); sa.set(0, 6);
        check(sa.get(0, 0), 5); check(sa.snap(), 1); check(sa.get(0, 1), 6); check(sa.get(1, 1), 0);
        int[][] m = {{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}};
        NumMatrix2D nm = new NumMatrix2D(m);
        check(nm.sumRegion(2, 1, 4, 3), 8); nm.update(3, 2, 2); check(nm.sumRegion(2, 1, 4, 3), 10);
        MyCalendarThree c3 = new MyCalendarThree(); MyCalendarThreeSweep cs = new MyCalendarThreeSweep();
        int[][] books = {{10, 20}, {50, 60}, {10, 40}, {5, 15}, {5, 10}, {25, 55}};
        int[] want = {1, 1, 2, 3, 3, 3};
        for (int i = 0; i < books.length; i++) { check(c3.book(books[i][0], books[i][1]), want[i]); check(cs.book(books[i][0], books[i][1]), want[i]); }
        int[][] bld = {{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}};
        String sky = "[[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]";
        check(getSkyline(bld).toString(), sky); check(getSkylineBrute(bld).toString(), sky);
        check(getSkyline(new int[][]{{0, 2, 3}, {2, 5, 3}}).toString(), "[[0, 3], [5, 0]]");
        check(longestDupSubstring("banana"), "ana"); check(longestDupSubstring("abcd"), "");
        Random rnd = new Random(8);
        for (int t = 0; t < 100; t++) {
            StringBuilder sb = new StringBuilder();
            int n = rnd.nextInt(25) + 2;
            for (int i = 0; i < n; i++) sb.append((char) ('a' + rnd.nextInt(3)));
            String x = sb.toString();
            check(longestDupSubstring(x).length(), longestDupSubstringBrute(x).length());
            String pal = manacher(x);
            check(pal.length(), longestPalindromeExpand(x).length());
            check(new StringBuilder(pal).reverse().toString(), pal); check(x.contains(pal), true);
        }
        check(manacher("babad").length(), 3); check(manacher("cbbd"), "bb");
        int[] inv = {8, 4, 2, 1, 4};
        check(countInversionsFenwick(inv), countInversionsBrute(inv)); check(countInversionsFenwick(new int[]{2, 4, 1, 3, 5}), 3L);
        System.out.println("AdvancedProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
