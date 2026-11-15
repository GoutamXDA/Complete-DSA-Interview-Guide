import java.util.*;

/**
 * Grid / BFS / DSU graph problems.
 *
 * LC 695  Max area of island: DFS returning area. O(RC)
 * LC 1020 Number of enclaves: sink land reachable from the border, count what's left. O(RC)
 * LC 417  Pacific Atlantic: brute DFS from every cell O((RC)^2) | reverse flow from each ocean's border, intersect O(RC)
 * LC 286  Walls and gates: BFS from each gate O(k*RC) | multi-source BFS from all gates at once O(RC)
 * LC 694  Number of distinct islands: record each island's cells relative to its first cell; set of shapes.
 * LC 323  Number of connected components: DFS | DSU.
 * LC 261  Graph valid tree: exactly n-1 edges and no cycle (DSU union fails => cycle).
 * LC 1091 Shortest path in binary matrix: BFS with 8 directions; path length counts cells.
 * LC 399  Evaluate division: graph a -> b weight a/b, DFS multiply along a path per query. O(Q * (V+E))
 * Minimum multiplications to reach end (GFG): BFS over states (value * a[i]) % 100000. O(100000 * n)
 * LC 1334 City with the smallest number of neighbours within threshold: Floyd–Warshall O(n^3)
 *         (or Dijkstra from each city O(n * E log V)). Tie -> largest index.
 * LC 1976 Number of ways to arrive at destination: Dijkstra + ways[v] accumulates on equal distance. O(E log V)
 */
public class GraphProblemsII {

    static final int[][] D4 = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int maxAreaOfIsland(int[][] g) {
        int best = 0;
        for (int r = 0; r < g.length; r++) for (int c = 0; c < g[0].length; c++) best = Math.max(best, area(g, r, c));
        return best;
    }

    static int area(int[][] g, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != 1) return 0;
        g[r][c] = 0;
        int a = 1;
        for (int[] d : D4) a += area(g, r + d[0], c + d[1]);
        return a;
    }

    static int numEnclaves(int[][] g) {
        int R = g.length, C = g[0].length;
        for (int r = 0; r < R; r++) { area(g, r, 0); area(g, r, C - 1); }
        for (int c = 0; c < C; c++) { area(g, 0, c); area(g, R - 1, c); }
        int cnt = 0;
        for (int[] row : g) for (int v : row) cnt += v;
        return cnt;
    }

    static List<List<Integer>> pacificAtlanticBrute(int[][] h) {
        List<List<Integer>> res = new ArrayList<>();
        int R = h.length, C = h[0].length;
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) {
            boolean[] ocean = new boolean[2];
            flowDown(h, r, c, new boolean[R][C], ocean);
            if (ocean[0] && ocean[1]) res.add(List.of(r, c));
        }
        return res;
    }

    static void flowDown(int[][] h, int r, int c, boolean[][] vis, boolean[] ocean) {
        vis[r][c] = true;
        if (r == 0 || c == 0) ocean[0] = true;
        if (r == h.length - 1 || c == h[0].length - 1) ocean[1] = true;
        for (int[] d : D4) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nc >= 0 && nr < h.length && nc < h[0].length && !vis[nr][nc] && h[nr][nc] <= h[r][c]) flowDown(h, nr, nc, vis, ocean);
        }
    }

    static List<List<Integer>> pacificAtlantic(int[][] h) {
        int R = h.length, C = h[0].length;
        boolean[][] pac = new boolean[R][C], atl = new boolean[R][C];
        for (int r = 0; r < R; r++) { climb(h, r, 0, pac); climb(h, r, C - 1, atl); }
        for (int c = 0; c < C; c++) { climb(h, 0, c, pac); climb(h, R - 1, c, atl); }
        List<List<Integer>> res = new ArrayList<>();
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) if (pac[r][c] && atl[r][c]) res.add(List.of(r, c));
        return res;
    }

    static void climb(int[][] h, int r, int c, boolean[][] vis) {   // reverse flow: only go uphill
        if (vis[r][c]) return;
        vis[r][c] = true;
        for (int[] d : D4) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nc >= 0 && nr < h.length && nc < h[0].length && h[nr][nc] >= h[r][c]) climb(h, nr, nc, vis);
        }
    }

    static final int INF = Integer.MAX_VALUE;

    static void wallsAndGatesBrute(int[][] rooms) {
        for (int r = 0; r < rooms.length; r++) for (int c = 0; c < rooms[0].length; c++)
            if (rooms[r][c] == 0) bfsFill(rooms, List.of(new int[]{r, c}));
    }

    static void wallsAndGates(int[][] rooms) {
        List<int[]> gates = new ArrayList<>();
        for (int r = 0; r < rooms.length; r++) for (int c = 0; c < rooms[0].length; c++) if (rooms[r][c] == 0) gates.add(new int[]{r, c});
        bfsFill(rooms, gates);
    }

    static void bfsFill(int[][] rooms, List<int[]> sources) {
        Queue<int[]> q = new ArrayDeque<>(sources);
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] d : D4) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr < 0 || nc < 0 || nr >= rooms.length || nc >= rooms[0].length || rooms[nr][nc] == -1) continue;
                if (rooms[nr][nc] > rooms[cur[0]][cur[1]] + 1) { rooms[nr][nc] = rooms[cur[0]][cur[1]] + 1; q.offer(new int[]{nr, nc}); }
            }
        }
    }

    static int numDistinctIslands(int[][] g) {
        Set<String> shapes = new HashSet<>();
        for (int r = 0; r < g.length; r++) for (int c = 0; c < g[0].length; c++)
            if (g[r][c] == 1) { StringBuilder sb = new StringBuilder(); shape(g, r, c, r, c, sb); shapes.add(sb.toString()); }
        return shapes.size();
    }

    static void shape(int[][] g, int r, int c, int r0, int c0, StringBuilder sb) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != 1) return;
        g[r][c] = 0;
        sb.append(r - r0).append(':').append(c - c0).append(';');
        for (int[] d : D4) shape(g, r + d[0], c + d[1], r0, c0, sb);
    }

    static int[] parent;
    static int find(int x) { return parent[x] == x ? x : (parent[x] = find(parent[x])); }

    static int countComponentsDFS(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) { g.get(e[0]).add(e[1]); g.get(e[1]).add(e[0]); }
        boolean[] vis = new boolean[n];
        int c = 0;
        for (int i = 0; i < n; i++) if (!vis[i]) { c++; Deque<Integer> st = new ArrayDeque<>(List.of(i)); vis[i] = true;
            while (!st.isEmpty()) for (int v : g.get(st.pop())) if (!vis[v]) { vis[v] = true; st.push(v); } }
        return c;
    }

    static int countComponents(int n, int[][] edges) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        int comps = n;
        for (int[] e : edges) { int a = find(e[0]), b = find(e[1]); if (a != b) { parent[a] = b; comps--; } }
        return comps;
    }

    static boolean validTree(int n, int[][] edges) {
        if (edges.length != n - 1) return false;
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        for (int[] e : edges) { int a = find(e[0]), b = find(e[1]); if (a == b) return false; parent[a] = b; }
        return true;
    }

    static int shortestPathBinaryMatrix(int[][] g) {
        int n = g.length;
        if (g[0][0] == 1 || g[n - 1][n - 1] == 1) return -1;
        int[][] dist = new int[n][n];
        dist[0][0] = 1;
        Queue<int[]> q = new ArrayDeque<>(List.of(new int[]{0, 0}));
        while (!q.isEmpty()) {
            int[] c = q.poll();
            if (c[0] == n - 1 && c[1] == n - 1) return dist[c[0]][c[1]];
            for (int dr = -1; dr <= 1; dr++) for (int dc = -1; dc <= 1; dc++) {
                int nr = c[0] + dr, nc = c[1] + dc;
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && g[nr][nc] == 0 && dist[nr][nc] == 0) {
                    dist[nr][nc] = dist[c[0]][c[1]] + 1; q.offer(new int[]{nr, nc});
                }
            }
        }
        return -1;
    }

    static double[] calcEquation(List<List<String>> eq, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> g = new HashMap<>();
        for (int i = 0; i < eq.size(); i++) {
            String a = eq.get(i).get(0), b = eq.get(i).get(1);
            g.computeIfAbsent(a, k -> new HashMap<>()).put(b, values[i]);
            g.computeIfAbsent(b, k -> new HashMap<>()).put(a, 1 / values[i]);
        }
        double[] res = new double[queries.size()];
        for (int i = 0; i < res.length; i++) {
            String a = queries.get(i).get(0), b = queries.get(i).get(1);
            res[i] = (!g.containsKey(a) || !g.containsKey(b)) ? -1 : div(g, a, b, new HashSet<>());
        }
        return res;
    }

    static double div(Map<String, Map<String, Double>> g, String a, String b, Set<String> seen) {
        if (a.equals(b)) return 1;
        seen.add(a);
        for (var e : g.get(a).entrySet()) {
            if (seen.contains(e.getKey())) continue;
            double r = div(g, e.getKey(), b, seen);
            if (r != -1) return e.getValue() * r;
        }
        return -1;
    }

    static int minimumMultiplications(int[] arr, int start, int end) {
        final int MOD = 100000;
        int[] dist = new int[MOD];
        Arrays.fill(dist, -1);
        dist[start] = 0;
        Queue<Integer> q = new ArrayDeque<>(List.of(start));
        while (!q.isEmpty()) {
            int u = q.poll();
            if (u == end) return dist[u];
            for (int a : arr) {
                int v = (int) ((long) u * a % MOD);
                if (dist[v] == -1) { dist[v] = dist[u] + 1; q.offer(v); }
            }
        }
        return -1;
    }

    static int findTheCity(int n, int[][] edges, int threshold) {
        int[][] d = new int[n][n];
        for (int[] row : d) Arrays.fill(row, 1_000_000_000);
        for (int i = 0; i < n; i++) d[i][i] = 0;
        for (int[] e : edges) { d[e[0]][e[1]] = Math.min(d[e[0]][e[1]], e[2]); d[e[1]][e[0]] = Math.min(d[e[1]][e[0]], e[2]); }
        for (int k = 0; k < n; k++) for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
        int best = -1, bestCnt = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < n; j++) if (i != j && d[i][j] <= threshold) cnt++;
            if (cnt <= bestCnt) { bestCnt = cnt; best = i; }   // <= keeps the largest index on ties
        }
        return best;
    }

    static int countPaths(int n, int[][] roads) {
        final long MOD = 1_000_000_007L;
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] r : roads) { g.get(r[0]).add(new int[]{r[1], r[2]}); g.get(r[1]).add(new int[]{r[0], r[2]}); }
        long[] dist = new long[n]; Arrays.fill(dist, Long.MAX_VALUE); dist[0] = 0;
        long[] ways = new long[n]; ways[0] = 1;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(x -> x[1]));
        pq.offer(new long[]{0, 0});
        while (!pq.isEmpty()) {
            long[] c = pq.poll();
            int u = (int) c[0];
            if (c[1] > dist[u]) continue;
            for (int[] e : g.get(u)) {
                long nd = dist[u] + e[1];
                if (nd < dist[e[0]]) { dist[e[0]] = nd; ways[e[0]] = ways[u]; pq.offer(new long[]{e[0], nd}); }
                else if (nd == dist[e[0]]) ways[e[0]] = (ways[e[0]] + ways[u]) % MOD;
            }
        }
        return (int) ways[n - 1];
    }

    static int[][] grid(int[][] g) { int[][] c = new int[g.length][]; for (int i = 0; i < g.length; i++) c[i] = g[i].clone(); return c; }

    public static void main(String[] args) {
        int[][] isl = {{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0}, {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0}, {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
        check(maxAreaOfIsland(grid(isl)), 6);
        check(numEnclaves(new int[][]{{0, 0, 0, 0}, {1, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}), 3);
        int[][] h = {{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}};
        String pa = "[[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]";
        check(pacificAtlantic(h).toString(), pa); check(pacificAtlanticBrute(h).toString(), pa);
        int[][] rooms = {{INF, -1, 0, INF}, {INF, INF, INF, -1}, {INF, -1, INF, -1}, {0, -1, INF, INF}};
        int[][] r2 = grid(rooms);
        wallsAndGates(rooms); wallsAndGatesBrute(r2);
        String wg = "[[3, -1, 0, 1], [2, 2, 1, -1], [1, -1, 2, -1], [0, -1, 3, 4]]";
        check(Arrays.deepToString(rooms), wg); check(Arrays.deepToString(r2), wg);
        check(numDistinctIslands(new int[][]{{1, 1, 0, 1, 1}, {1, 0, 0, 0, 0}, {0, 0, 0, 0, 1}, {1, 1, 0, 1, 1}}), 3);
        int[][] ce = {{0, 1}, {1, 2}, {3, 4}};
        check(countComponents(5, ce), 2); check(countComponentsDFS(5, ce), 2);
        check(validTree(5, new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 4}}), true); check(validTree(5, new int[][]{{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}}), false);
        check(shortestPathBinaryMatrix(new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}), 4); check(shortestPathBinaryMatrix(new int[][]{{1, 0, 0}, {1, 1, 0}, {1, 1, 0}}), -1);
        double[] ans = calcEquation(List.of(List.of("a", "b"), List.of("b", "c")), new double[]{2.0, 3.0},
                List.of(List.of("a", "c"), List.of("b", "a"), List.of("a", "e"), List.of("a", "a"), List.of("x", "x")));
        check(Arrays.toString(ans), "[6.0, 0.5, -1.0, 1.0, -1.0]");
        check(minimumMultiplications(new int[]{2, 5, 7}, 3, 30), 2); check(minimumMultiplications(new int[]{3, 4, 65}, 7, 66175), 4);
        check(findTheCity(4, new int[][]{{0, 1, 3}, {1, 2, 1}, {1, 3, 4}, {2, 3, 1}}, 4), 3);
        check(findTheCity(5, new int[][]{{0, 1, 2}, {0, 4, 8}, {1, 2, 3}, {1, 4, 2}, {2, 3, 1}, {3, 4, 1}}, 2), 0);
        check(countPaths(7, new int[][]{{0, 6, 7}, {0, 1, 2}, {1, 2, 3}, {1, 3, 3}, {6, 3, 3}, {3, 5, 1}, {6, 5, 1}, {2, 5, 1}, {0, 4, 5}, {4, 6, 2}}), 4);
        System.out.println("GraphProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
