import java.util.*;

/**
 * Shortest path toolkit.
 *
 * BFS (unweighted)         O(V+E)
 * Dijkstra (non-negative)  O((V+E) log V). Skip stale heap entries (d > dist[u]).
 *   Why not negative edges? A settled node could later get a shorter path.
 * Bellman-Ford             O(V*E). Relax all edges V-1 times; a V-th improvement => negative cycle.
 * Floyd-Warshall           O(V^3). dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]).
 * DAG shortest path        O(V+E). Relax in topological order; works with negative weights.
 * LC 787 Cheapest flights with <= K stops: Bellman-Ford limited to K+1 rounds (copy array each round).
 * LC 1631 Min effort path: Dijkstra where path cost = max edge on path.
 * LC 127 Word ladder: BFS; neighbours = change one letter (26 * L per word).
 */
public class ShortestPaths {
    static final int INF = Integer.MAX_VALUE;

    static int[] bfsShortest(List<List<Integer>> g, int src) {
        int[] d = new int[g.size()]; Arrays.fill(d, -1); d[src] = 0;
        Queue<Integer> q = new ArrayDeque<>(List.of(src));
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : g.get(u)) if (d[v] == -1) { d[v] = d[u] + 1; q.offer(v); }
        }
        return d;
    }

    /** adj.get(u) = list of {v, w} */
    static int[] dijkstra(List<List<int[]>> adj, int src) {
        int[] dist = new int[adj.size()]; Arrays.fill(dist, INF); dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
        pq.offer(new int[]{src, 0});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;                       // stale entry
            for (int[] e : adj.get(u)) {
                if (d + e[1] < dist[e[0]]) { dist[e[0]] = d + e[1]; pq.offer(new int[]{e[0], dist[e[0]]}); }
            }
        }
        return dist;
    }

    /** LC 743 */
    static int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] t : times) adj.get(t[0]).add(new int[]{t[1], t[2]});
        int[] d = dijkstra(adj, k);
        int max = 0;
        for (int i = 1; i <= n; i++) { if (d[i] == INF) return -1; max = Math.max(max, d[i]); }
        return max;
    }

    /** Returns null if a negative cycle is reachable. edges: {u, v, w} */
    static int[] bellmanFord(int n, int[][] edges, int src) {
        int[] d = new int[n]; Arrays.fill(d, INF); d[src] = 0;
        for (int i = 0; i < n - 1; i++)
            for (int[] e : edges) if (d[e[0]] != INF && d[e[0]] + e[2] < d[e[1]]) d[e[1]] = d[e[0]] + e[2];
        for (int[] e : edges) if (d[e[0]] != INF && d[e[0]] + e[2] < d[e[1]]) return null;
        return d;
    }

    static long[][] floydWarshall(int n, int[][] edges) {
        long[][] d = new long[n][n];
        for (long[] row : d) Arrays.fill(row, Long.MAX_VALUE / 4);
        for (int i = 0; i < n; i++) d[i][i] = 0;
        for (int[] e : edges) d[e[0]][e[1]] = Math.min(d[e[0]][e[1]], e[2]);
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
        return d;
    }

    static int[] dagShortest(int n, int[][] edges, int src) {
        List<List<int[]>> adj = new ArrayList<>();
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) { adj.get(e[0]).add(new int[]{e[1], e[2]}); indeg[e[1]]++; }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);
        int[] d = new int[n]; Arrays.fill(d, INF); d[src] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int[] e : adj.get(u)) {
                if (d[u] != INF && d[u] + e[1] < d[e[0]]) d[e[0]] = d[u] + e[1];
                if (--indeg[e[0]] == 0) q.offer(e[0]);
            }
        }
        return d;
    }

    static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        int[] d = new int[n]; Arrays.fill(d, INF); d[src] = 0;
        for (int i = 0; i <= k; i++) {
            int[] next = d.clone();                       // use only last round's values => edge count bound
            for (int[] f : flights) if (d[f[0]] != INF && d[f[0]] + f[2] < next[f[1]]) next[f[1]] = d[f[0]] + f[2];
            d = next;
        }
        return d[dst] == INF ? -1 : d[dst];
    }

    static int minimumEffortPath(int[][] h) {
        int R = h.length, C = h[0].length;
        int[][] eff = new int[R][C];
        for (int[] row : eff) Arrays.fill(row, INF);
        eff[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[2]));
        pq.offer(new int[]{0, 0, 0});
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            if (c[0] == R - 1 && c[1] == C - 1) return c[2];
            if (c[2] > eff[c[0]][c[1]]) continue;
            for (int[] d : dirs) {
                int nr = c[0] + d[0], nc = c[1] + d[1];
                if (nr < 0 || nc < 0 || nr >= R || nc >= C) continue;
                int e = Math.max(c[2], Math.abs(h[nr][nc] - h[c[0]][c[1]]));
                if (e < eff[nr][nc]) { eff[nr][nc] = e; pq.offer(new int[]{nr, nc, e}); }
            }
        }
        return 0;
    }

    static int ladderLength(String begin, String end, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(end)) return 0;
        Queue<String> q = new ArrayDeque<>(List.of(begin));
        dict.remove(begin);
        for (int steps = 1; !q.isEmpty(); steps++) {
            for (int i = q.size(); i > 0; i--) {
                char[] w = q.poll().toCharArray();
                for (int j = 0; j < w.length; j++) {
                    char orig = w[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        w[j] = c;
                        String nx = new String(w);
                        if (nx.equals(end)) return steps + 1;
                        if (dict.remove(nx)) q.offer(nx);   // remove = mark visited
                    }
                    w[j] = orig;
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < 5; i++) g.add(new ArrayList<>());
        int[][] ue = {{0, 1}, {0, 2}, {1, 3}, {2, 3}, {3, 4}};
        for (int[] e : ue) { g.get(e[0]).add(e[1]); g.get(e[1]).add(e[0]); }
        check(Arrays.toString(bfsShortest(g, 0)), "[0, 1, 1, 2, 3]");
        check(networkDelayTime(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2), 2);
        check(networkDelayTime(new int[][]{{1, 2, 1}}, 2, 2), -1);
        int[][] edges = {{0, 1, 4}, {0, 2, 5}, {1, 2, -3}, {2, 3, 4}};
        check(Arrays.toString(bellmanFord(4, edges, 0)), "[0, 4, 1, 5]");
        check(bellmanFord(3, new int[][]{{0, 1, 1}, {1, 2, -1}, {2, 1, -1}}, 0) == null, true);
        long[][] fw = floydWarshall(4, edges);
        check(fw[0][3], 5L); check(fw[1][3], 1L);
        check(Arrays.toString(dagShortest(4, edges, 0)), "[0, 4, 1, 5]");
        check(findCheapestPrice(4, new int[][]{{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}}, 0, 3, 1), 700);
        check(minimumEffortPath(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}), 2);
        check(ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")), 5);
        check(ladderLength("hit", "cog", List.of("hot", "dot", "dog", "lot", "log")), 0);
        System.out.println("ShortestPaths: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
