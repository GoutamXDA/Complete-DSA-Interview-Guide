import java.util.*;

/**
 * LC 847 — Shortest Path Visiting All Nodes. Explanation: ShortestPathVisitingAllNodes.md
 * 1. Held–Karp DP over all-pairs BFS distances      O(2^n n^2)
 * 2. Multi-source BFS over (node, mask) states      O(2^n E)
 */
public class ShortestPathVisitingAllNodes {

    static int heldKarp(int[][] g) {
        int n = g.length, full = (1 << n) - 1, INF = Integer.MAX_VALUE / 2;
        int[][] dist = new int[n][n];
        for (int s = 0; s < n; s++) {
            Arrays.fill(dist[s], INF); dist[s][s] = 0;
            Deque<Integer> q = new ArrayDeque<>(List.of(s));
            while (!q.isEmpty()) { int u = q.poll(); for (int v : g[u]) if (dist[s][v] == INF) { dist[s][v] = dist[s][u] + 1; q.offer(v); } }
        }
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) Arrays.fill(row, INF);
        for (int i = 0; i < n; i++) dp[1 << i][i] = 0;
        for (int mask = 1; mask <= full; mask++)
            for (int i = 0; i < n; i++) {
                if (dp[mask][i] == INF) continue;
                for (int j = 0; j < n; j++) {
                    if ((mask >> j & 1) == 1) continue;
                    int nm = mask | 1 << j;
                    dp[nm][j] = Math.min(dp[nm][j], dp[mask][i] + dist[i][j]);
                }
            }
        int best = INF;
        for (int i = 0; i < n; i++) best = Math.min(best, dp[full][i]);
        return best;
    }

    static int shortestPathLength(int[][] g) {
        int n = g.length, full = (1 << n) - 1;
        if (n == 1) return 0;
        boolean[][] seen = new boolean[n][1 << n];
        Deque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) { q.offer(new int[]{i, 1 << i}); seen[i][1 << i] = true; }
        for (int steps = 1; !q.isEmpty(); steps++)
            for (int sz = q.size(); sz > 0; sz--) {
                int[] s = q.poll();
                for (int v : g[s[0]]) {
                    int nm = s[1] | 1 << v;
                    if (nm == full) return steps;
                    if (!seen[v][nm]) { seen[v][nm] = true; q.offer(new int[]{v, nm}); }
                }
            }
        return -1;
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {0}, {0}, {0}}, b = {{1}, {0, 2, 4}, {1, 3, 4}, {2}, {1, 2}};
        check(shortestPathLength(a), 4); check(heldKarp(a), 4); check(shortestPathLength(b), 4); check(heldKarp(b), 4);
        check(shortestPathLength(new int[][]{{}}), 0);
        Random r = new Random(47);
        for (int t = 0; t < 400; t++) {
            int n = r.nextInt(9) + 1;
            List<Set<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new TreeSet<>());
            for (int v = 1; v < n; v++) { int u = r.nextInt(v); adj.get(u).add(v); adj.get(v).add(u); }   // connected
            for (int k = r.nextInt(n + 1); k > 0; k--) { int x = r.nextInt(n), y = r.nextInt(n); if (x != y) { adj.get(x).add(y); adj.get(y).add(x); } }
            int[][] g = new int[n][];
            for (int i = 0; i < n; i++) g[i] = adj.get(i).stream().mapToInt(Integer::intValue).toArray();
            check(shortestPathLength(g), heldKarp(g));
        }
        int[][] big = new int[12][];
        for (int i = 0; i < 12; i++) big[i] = i == 0 ? new int[]{1} : i == 11 ? new int[]{10} : new int[]{i - 1, i + 1};
        check(shortestPathLength(big), 11);                    // a path of 12 nodes
        System.out.println("ShortestPathVisitingAllNodes: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
