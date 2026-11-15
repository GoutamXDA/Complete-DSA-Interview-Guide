import java.util.*;

/**
 * LC 778 — Swim in Rising Water. Explanation: SwimInRisingWater.md
 * 1. Binary search on t + DFS flood          O(n^2 log n)
 * 2. Dijkstra with max-cost                  O(n^2 log n)
 * 3. DSU, activate cells by elevation        O(n^2 α)
 */
public class SwimInRisingWater {

    static final int[][] D = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int binarySearch(int[][] g) {
        int n = g.length, lo = g[0][0], hi = n * n - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (reachable(g, mid)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static boolean reachable(int[][] g, int t) {
        int n = g.length;
        boolean[][] vis = new boolean[n][n];
        Deque<int[]> st = new ArrayDeque<>(List.of(new int[]{0, 0}));
        vis[0][0] = true;
        while (!st.isEmpty()) {
            int[] c = st.pop();
            if (c[0] == n - 1 && c[1] == n - 1) return true;
            for (int[] d : D) {
                int r = c[0] + d[0], k = c[1] + d[1];
                if (r >= 0 && k >= 0 && r < n && k < n && !vis[r][k] && g[r][k] <= t) { vis[r][k] = true; st.push(new int[]{r, k}); }
            }
        }
        return false;
    }

    static int dijkstra(int[][] g) {
        int n = g.length;
        boolean[][] done = new boolean[n][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[2]));
        pq.offer(new int[]{0, 0, g[0][0]});
        while (true) {
            int[] c = pq.poll();
            if (done[c[0]][c[1]]) continue;
            done[c[0]][c[1]] = true;
            if (c[0] == n - 1 && c[1] == n - 1) return c[2];
            for (int[] d : D) {
                int r = c[0] + d[0], k = c[1] + d[1];
                if (r >= 0 && k >= 0 && r < n && k < n && !done[r][k]) pq.offer(new int[]{r, k, Math.max(c[2], g[r][k])});
            }
        }
    }

    static int[] par;
    static int find(int x) { while (par[x] != x) { par[x] = par[par[x]]; x = par[x]; } return x; }

    static int dsu(int[][] g) {
        int n = g.length;
        int[] pos = new int[n * n];
        for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) pos[g[r][c]] = r * n + c;
        par = new int[n * n];
        boolean[] active = new boolean[n * n];
        for (int i = 0; i < n * n; i++) par[i] = i;
        for (int t = 0; t < n * n; t++) {
            int cell = pos[t], r = cell / n, c = cell % n;
            active[cell] = true;
            for (int[] d : D) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && active[nr * n + nc]) par[find(cell)] = find(nr * n + nc);
            }
            if (active[0] && active[n * n - 1] && find(0) == find(n * n - 1)) return t;
        }
        return -1;
    }

    public static void main(String[] args) {
        check(dijkstra(new int[][]{{0, 2}, {1, 3}}), 3);
        int[][] ex = {{0, 1, 2, 3, 4}, {24, 23, 22, 21, 5}, {12, 13, 14, 15, 16}, {11, 17, 18, 19, 20}, {10, 9, 8, 7, 6}};
        check(dijkstra(ex), 16); check(binarySearch(ex), 16); check(dsu(ex), 16);
        Random r = new Random(48);
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(6) + 1;
            List<Integer> perm = new ArrayList<>();
            for (int i = 0; i < n * n; i++) perm.add(i);
            Collections.shuffle(perm, r);
            int[][] g = new int[n][n];
            for (int i = 0; i < n * n; i++) g[i / n][i % n] = perm.get(i);
            int e = binarySearch(g);
            check(dijkstra(g), e); check(dsu(g), e);
        }
        System.out.println("SwimInRisingWater: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
