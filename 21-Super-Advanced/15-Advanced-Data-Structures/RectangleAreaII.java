import java.util.*;

/**
 * LC 850 — Rectangle Area II. Explanation: RectangleAreaII.md
 * 1. Coordinate-compressed grid marking             O(n^3)
 * 2. Sweep line, re-sort active y-intervals         O(n^2 log n)
 * 3. Sweep line + segment tree of covered length    O(n log n)
 */
public class RectangleAreaII {
    static final long MOD = 1_000_000_007L;

    static long grid(int[][] rects) {
        int[] xs = coords(rects, 0, 2), ys = coords(rects, 1, 3);
        long area = 0;
        for (int i = 0; i + 1 < xs.length; i++)
            for (int j = 0; j + 1 < ys.length; j++)
                for (int[] r : rects)
                    if (r[0] <= xs[i] && xs[i + 1] <= r[2] && r[1] <= ys[j] && ys[j + 1] <= r[3]) {
                        area += (long) (xs[i + 1] - xs[i]) * (ys[j + 1] - ys[j]);
                        break;
                    }
        return area % MOD;
    }

    static int[] coords(int[][] rects, int a, int b) {
        TreeSet<Integer> s = new TreeSet<>();
        for (int[] r : rects) { s.add(r[a]); s.add(r[b]); }
        return s.stream().mapToInt(Integer::intValue).toArray();
    }

    static long sweepSort(int[][] rects) {
        List<int[]> ev = new ArrayList<>();               // {x, type(+1/-1), y1, y2}
        for (int[] r : rects) { ev.add(new int[]{r[0], 1, r[1], r[3]}); ev.add(new int[]{r[2], -1, r[1], r[3]}); }
        ev.sort(Comparator.comparingInt(e -> e[0]));
        List<int[]> active = new ArrayList<>();
        long area = 0;
        int prevX = ev.get(0)[0];
        for (int[] e : ev) {
            area += coveredLength(active) * (e[0] - prevX);
            prevX = e[0];
            if (e[1] == 1) active.add(new int[]{e[2], e[3]});
            else for (int i = 0; i < active.size(); i++) if (active.get(i)[0] == e[2] && active.get(i)[1] == e[3]) { active.remove(i); break; }
        }
        return area % MOD;
    }

    static long coveredLength(List<int[]> intervals) {
        List<int[]> s = new ArrayList<>(intervals);
        s.sort(Comparator.comparingInt(x -> x[0]));
        long total = 0; int curL = -1, curR = -1;
        for (int[] iv : s) {
            if (iv[0] > curR) { total += curR - curL; curL = iv[0]; curR = iv[1]; }
            else curR = Math.max(curR, iv[1]);
        }
        return total + (curR - curL);
    }

    // ---------- segment tree over elementary y-intervals ----------
    static int[] ys;
    static int[] count;
    static long[] covered;

    static void update(int node, int l, int r, int ql, int qr, int delta) {   // node covers [ys[l], ys[r+1])
        if (qr < l || r < ql) return;
        if (ql <= l && r <= qr) count[node] += delta;
        else {
            int m = (l + r) / 2;
            update(2 * node, l, m, ql, qr, delta);
            update(2 * node + 1, m + 1, r, ql, qr, delta);
        }
        if (count[node] > 0) covered[node] = ys[r + 1] - ys[l];
        else if (l == r) covered[node] = 0;
        else covered[node] = covered[2 * node] + covered[2 * node + 1];
    }

    static long rectangleArea(int[][] rects) {
        ys = coords(rects, 1, 3);
        int leaves = ys.length - 1;
        count = new int[4 * leaves];
        covered = new long[4 * leaves];
        List<int[]> ev = new ArrayList<>();
        for (int[] r : rects) {
            int a = Arrays.binarySearch(ys, r[1]), b = Arrays.binarySearch(ys, r[3]) - 1;   // elementary intervals a..b
            ev.add(new int[]{r[0], 1, a, b}); ev.add(new int[]{r[2], -1, a, b});
        }
        ev.sort(Comparator.comparingInt(e -> e[0]));
        long area = 0;
        int prevX = ev.get(0)[0];
        for (int[] e : ev) {
            area = (area + covered[1] % MOD * (e[0] - prevX)) % MOD;
            prevX = e[0];
            if (e[2] <= e[3]) update(1, 0, leaves - 1, e[2], e[3], e[1]);
        }
        return area;
    }

    public static void main(String[] args) {
        int[][] ex = {{0, 0, 2, 2}, {1, 0, 2, 3}, {1, 0, 3, 1}};
        check(rectangleArea(ex), 6L); check(grid(ex), 6L); check(sweepSort(ex), 6L);
        check(rectangleArea(new int[][]{{0, 0, 1000000000, 1000000000}}), 49L);
        Random r = new Random(70);
        for (int t = 0; t < 1500; t++) {
            int[][] rects = new int[r.nextInt(6) + 1][];
            for (int i = 0; i < rects.length; i++) {
                int x1 = r.nextInt(10), y1 = r.nextInt(10);
                rects[i] = new int[]{x1, y1, x1 + r.nextInt(6) + 1, y1 + r.nextInt(6) + 1};
            }
            long e = grid(rects);
            check(sweepSort(rects), e); check(rectangleArea(rects), e);
        }
        System.out.println("RectangleAreaII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
