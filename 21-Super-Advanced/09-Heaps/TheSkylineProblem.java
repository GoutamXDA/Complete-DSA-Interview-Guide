import java.util.*;

/**
 * LC 218 — The Skyline Problem. Explanation: TheSkylineProblem.md
 * 1. Height at every critical x                    O(n^2)
 * 2. Sweep events + TreeMap multiset               O(n log n)
 * 3. Max-heap of (height, right) + lazy expiry     O(n log n)
 * 4. Divide & conquer, merge skylines              O(n log n)
 */
public class TheSkylineProblem {

    static List<List<Integer>> brute(int[][] b) {
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

    static List<List<Integer>> sweepTreeMap(int[][] b) {
        List<int[]> ev = new ArrayList<>();
        for (int[] x : b) { ev.add(new int[]{x[0], -x[2]}); ev.add(new int[]{x[1], x[2]}); }
        ev.sort((p, q) -> p[0] != q[0] ? p[0] - q[0] : p[1] - q[1]);
        TreeMap<Integer, Integer> active = new TreeMap<>(Map.of(0, 1));
        List<List<Integer>> res = new ArrayList<>();
        int prev = 0;
        for (int[] e : ev) {
            if (e[1] < 0) active.merge(-e[1], 1, Integer::sum);
            else if (active.merge(e[1], -1, Integer::sum) == 0) active.remove(e[1]);
            int cur = active.lastKey();
            if (cur != prev) { res.add(List.of(e[0], cur)); prev = cur; }
        }
        return res;
    }

    static List<List<Integer>> heapLazy(int[][] b) {
        int[][] s = b.clone();
        Arrays.sort(s, Comparator.comparingInt(x -> x[0]));
        TreeSet<Integer> xs = new TreeSet<>();
        for (int[] x : b) { xs.add(x[0]); xs.add(x[1]); }
        PriorityQueue<int[]> pq = new PriorityQueue<>((p, q) -> q[0] - p[0]);   // {height, right}
        List<List<Integer>> res = new ArrayList<>();
        int i = 0, prev = 0;
        for (int x : xs) {
            while (i < s.length && s[i][0] == x) { pq.offer(new int[]{s[i][2], s[i][1]}); i++; }
            while (!pq.isEmpty() && pq.peek()[1] <= x) pq.poll();             // expired tops only
            int cur = pq.isEmpty() ? 0 : pq.peek()[0];
            if (cur != prev) { res.add(List.of(x, cur)); prev = cur; }
        }
        return res;
    }

    static List<List<Integer>> divide(int[][] b) {
        if (b.length == 0) return new ArrayList<>();
        return dc(b, 0, b.length - 1);
    }

    static List<List<Integer>> dc(int[][] b, int l, int r) {
        if (l == r) {
            List<List<Integer>> s = new ArrayList<>();
            s.add(List.of(b[l][0], b[l][2])); s.add(List.of(b[l][1], 0));
            return s;
        }
        int m = (l + r) / 2;
        return merge(dc(b, l, m), dc(b, m + 1, r));
    }

    static List<List<Integer>> merge(List<List<Integer>> a, List<List<Integer>> c) {
        List<List<Integer>> res = new ArrayList<>();
        int i = 0, j = 0, h1 = 0, h2 = 0, prev = 0;
        while (i < a.size() || j < c.size()) {
            int x;
            if (j == c.size() || (i < a.size() && a.get(i).get(0) < c.get(j).get(0))) { x = a.get(i).get(0); h1 = a.get(i++).get(1); }
            else if (i == a.size() || c.get(j).get(0) < a.get(i).get(0)) { x = c.get(j).get(0); h2 = c.get(j++).get(1); }
            else { x = a.get(i).get(0); h1 = a.get(i++).get(1); h2 = c.get(j++).get(1); }   // same x on both sides
            int h = Math.max(h1, h2);
            if (h != prev) { res.add(List.of(x, h)); prev = h; }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] ex = {{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}};
        String e = "[[2, 10], [3, 15], [7, 12], [12, 0], [15, 10], [20, 8], [24, 0]]";
        check(sweepTreeMap(ex).toString(), e); check(heapLazy(ex).toString(), e); check(divide(ex).toString(), e);
        check(sweepTreeMap(new int[][]{{0, 2, 3}, {2, 5, 3}}).toString(), "[[0, 3], [5, 0]]");
        Random r = new Random(42);
        for (int t = 0; t < 2000; t++) {
            int[][] b = new int[r.nextInt(7) + 1][];
            for (int i = 0; i < b.length; i++) { int L = r.nextInt(10); b[i] = new int[]{L, L + r.nextInt(6) + 1, r.nextInt(5) + 1}; }
            String x = brute(b).toString();
            check(sweepTreeMap(b).toString(), x); check(heapLazy(b).toString(), x); check(divide(b).toString(), x);
        }
        System.out.println("TheSkylineProblem: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
