import java.util.*;

/**
 * LC 1928 — Minimum Cost to Reach Destination in Time. Explanation: MinCostToReachDestinationInTime.md
 * 1. DP over (time, node)                                  O(maxTime * E)
 * 2. Dijkstra on cost + best-time pruning (Pareto)         fast in practice
 * 3. Approach 2 + lower-bound pruning with time-to-goal    faster still
 */
public class MinCostToReachDestinationInTime {

    static int dpOverTime(int maxTime, int[][] edges, int[] fee) {
        int n = fee.length, INF = Integer.MAX_VALUE / 2;
        int[][] dp = new int[maxTime + 1][n];
        for (int[] row : dp) Arrays.fill(row, INF);
        dp[0][0] = fee[0];
        for (int t = 0; t <= maxTime; t++)
            for (int[] e : edges)
                for (int dir = 0; dir < 2; dir++) {
                    int u = dir == 0 ? e[0] : e[1], v = dir == 0 ? e[1] : e[0], nt = t + e[2];
                    if (nt <= maxTime && dp[t][u] != INF) dp[nt][v] = Math.min(dp[nt][v], dp[t][u] + fee[v]);
                }
        int best = INF;
        for (int t = 0; t <= maxTime; t++) best = Math.min(best, dp[t][n - 1]);
        return best == INF ? -1 : best;
    }

    static List<List<int[]>> graph(int n, int[][] edges) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) { g.get(e[0]).add(new int[]{e[1], e[2]}); g.get(e[1]).add(new int[]{e[0], e[2]}); }
        return g;
    }

    static int dijkstraPareto(int maxTime, int[][] edges, int[] fee) { return search(maxTime, edges, fee, false); }

    static int dijkstraWithBound(int maxTime, int[][] edges, int[] fee) { return search(maxTime, edges, fee, true); }

    static int search(int maxTime, int[][] edges, int[] fee, boolean bound) {
        int n = fee.length;
        List<List<int[]>> g = graph(n, edges);
        int[] toEnd = new int[n];
        if (bound) {                                          // shortest time from every node to n-1
            Arrays.fill(toEnd, Integer.MAX_VALUE);
            toEnd[n - 1] = 0;
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
            pq.offer(new int[]{n - 1, 0});
            while (!pq.isEmpty()) {
                int[] c = pq.poll();
                if (c[1] > toEnd[c[0]]) continue;
                for (int[] e : g.get(c[0])) if (c[1] + e[1] < toEnd[e[0]]) { toEnd[e[0]] = c[1] + e[1]; pq.offer(new int[]{e[0], toEnd[e[0]]}); }
            }
            if (toEnd[0] > maxTime) return -1;
        }
        int[] bestTime = new int[n];
        Arrays.fill(bestTime, Integer.MAX_VALUE);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);   // {cost, time, node}
        pq.offer(new int[]{fee[0], 0, 0});
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            int cost = c[0], time = c[1], u = c[2];
            if (u == n - 1) return cost;
            if (time >= bestTime[u]) continue;                // dominated: an earlier pop was cheaper AND faster
            bestTime[u] = time;
            for (int[] e : g.get(u)) {
                int nt = time + e[1];
                if (nt > maxTime) continue;
                if (bound && (toEnd[e[0]] == Integer.MAX_VALUE || nt + toEnd[e[0]] > maxTime)) continue;
                pq.offer(new int[]{cost + fee[e[0]], nt, e[0]});
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] e = {{0, 1, 10}, {1, 2, 10}, {2, 5, 10}, {0, 3, 1}, {3, 4, 10}, {4, 5, 15}};
        int[] fee = {5, 1, 2, 20, 20, 3};
        check(dpOverTime(30, e, fee), 11); check(dijkstraPareto(30, e, fee), 11); check(dijkstraWithBound(30, e, fee), 11);
        check(dpOverTime(29, e, fee), 48); check(dijkstraPareto(29, e, fee), 48); check(dpOverTime(25, e, fee), -1);
        Random r = new Random(52);
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(6) + 2, maxTime = r.nextInt(25) + 1;
            List<int[]> es = new ArrayList<>();
            for (int k = r.nextInt(10) + 1; k > 0; k--) { int a = r.nextInt(n), b = r.nextInt(n); if (a != b) es.add(new int[]{a, b, r.nextInt(8) + 1}); }
            int[] f = new int[n];
            for (int i = 0; i < n; i++) f[i] = r.nextInt(10) + 1;
            int[][] edges = es.toArray(new int[0][]);
            int x = dpOverTime(maxTime, edges, f);
            check(dijkstraPareto(maxTime, edges, f), x); check(dijkstraWithBound(maxTime, edges, f), x);
        }
        System.out.println("MinCostToReachDestinationInTime: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
