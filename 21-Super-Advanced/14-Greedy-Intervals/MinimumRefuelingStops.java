import java.util.*;

/**
 * LC 871 — Minimum Number of Refueling Stops. Explanation: MinimumRefuelingStops.md
 * 0. Brute force over subsets of stations (oracle)
 * 1. DP: farthest reach with k stops             O(n^2)
 * 2. Max-heap retroactive greedy                 O(n log n)
 */
public class MinimumRefuelingStops {

    static int brute(int target, int start, int[][] st) {
        int n = st.length, best = Integer.MAX_VALUE;
        for (int mask = 0; mask < 1 << n; mask++) {
            long fuel = start; boolean ok = true;
            for (int i = 0; i < n && ok; i++) {
                if (fuel < st[i][0]) ok = false;
                else if ((mask >> i & 1) == 1) fuel += st[i][1];
            }
            if (ok && fuel >= target) best = Math.min(best, Integer.bitCount(mask));
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    static int dp(int target, int start, int[][] st) {
        int n = st.length;
        long[] reach = new long[n + 1];
        reach[0] = start;
        for (int i = 0; i < n; i++)
            for (int k = i; k >= 0; k--)
                if (reach[k] >= st[i][0]) reach[k + 1] = Math.max(reach[k + 1], reach[k] + st[i][1]);
        for (int k = 0; k <= n; k++) if (reach[k] >= target) return k;
        return -1;
    }

    static int minRefuelStops(int target, int start, int[][] st) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        long fuel = start;
        int stops = 0;
        for (int i = 0; i <= st.length; i++) {
            int pos = i == st.length ? target : st[i][0];
            while (fuel < pos) {
                if (pq.isEmpty()) return -1;
                fuel += pq.poll(); stops++;
            }
            if (i < st.length) pq.offer(st[i][1]);
        }
        return stops;
    }

    public static void main(String[] args) {
        int[][] ex = {{10, 60}, {20, 30}, {30, 30}, {60, 40}};
        check(minRefuelStops(100, 10, ex), 2); check(dp(100, 10, ex), 2);
        check(minRefuelStops(1, 1, new int[][]{}), 0); check(minRefuelStops(100, 1, new int[][]{{10, 100}}), -1);
        Random r = new Random(63);
        for (int t = 0; t < 3000; t++) {
            int target = r.nextInt(60) + 2, start = r.nextInt(20) + 1;
            int n = Math.min(r.nextInt(9), target - 1);            // stations at distinct positions in [1, target-1]
            TreeSet<Integer> positions = new TreeSet<>();
            while (positions.size() < n) positions.add(r.nextInt(target - 1) + 1);
            int[][] st = new int[n][];
            int i = 0;
            for (int p : positions) st[i++] = new int[]{p, r.nextInt(25) + 1};
            int e = brute(target, start, st);
            check(dp(target, start, st), e); check(minRefuelStops(target, start, st), e);
        }
        System.out.println("MinimumRefuelingStops: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
