import java.util.*;

/**
 * LC 1368 — Minimum Cost to Make at Least One Valid Path in a Grid. Explanation: MinimumCostToMakeValidPath.md
 * 1. Dijkstra with 0/1 weights                     O(RC log RC)
 * 2. 0-1 BFS with a deque                          O(RC)
 * 3. Layered BFS with DFS along free arrows        O(RC)
 * Oracle: Bellman-Ford style relaxation.
 */
public class MinimumCostToMakeValidPath {

    static final int[][] D = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};   // arrow 1..4 -> index 0..3

    static int relaxOracle(int[][] g) {
        int R = g.length, C = g[0].length, INF = Integer.MAX_VALUE / 2;
        int[][] dist = new int[R][C];
        for (int[] row : dist) Arrays.fill(row, INF);
        dist[0][0] = 0;
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) {
                if (dist[r][c] == INF) continue;
                for (int k = 0; k < 4; k++) {
                    int nr = r + D[k][0], nc = c + D[k][1];
                    if (nr < 0 || nc < 0 || nr >= R || nc >= C) continue;
                    int nd = dist[r][c] + (g[r][c] - 1 == k ? 0 : 1);
                    if (nd < dist[nr][nc]) { dist[nr][nc] = nd; changed = true; }
                }
            }
        }
        return dist[R - 1][C - 1];
    }

    static int dijkstra(int[][] g) {
        int R = g.length, C = g[0].length;
        int[][] dist = new int[R][C];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[2]));
        pq.offer(new int[]{0, 0, 0});
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            if (c[2] > dist[c[0]][c[1]]) continue;
            for (int k = 0; k < 4; k++) {
                int nr = c[0] + D[k][0], nc = c[1] + D[k][1];
                if (nr < 0 || nc < 0 || nr >= R || nc >= C) continue;
                int nd = c[2] + (g[c[0]][c[1]] - 1 == k ? 0 : 1);
                if (nd < dist[nr][nc]) { dist[nr][nc] = nd; pq.offer(new int[]{nr, nc, nd}); }
            }
        }
        return dist[R - 1][C - 1];
    }

    static int minCost(int[][] g) {
        int R = g.length, C = g[0].length;
        int[][] dist = new int[R][C];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;
        Deque<int[]> dq = new ArrayDeque<>();
        dq.offer(new int[]{0, 0});
        while (!dq.isEmpty()) {
            int[] c = dq.pollFirst();
            int d = dist[c[0]][c[1]];
            for (int k = 0; k < 4; k++) {
                int nr = c[0] + D[k][0], nc = c[1] + D[k][1];
                if (nr < 0 || nc < 0 || nr >= R || nc >= C) continue;
                int w = g[c[0]][c[1]] - 1 == k ? 0 : 1;
                if (d + w < dist[nr][nc]) {
                    dist[nr][nc] = d + w;
                    if (w == 0) dq.offerFirst(new int[]{nr, nc}); else dq.offerLast(new int[]{nr, nc});
                }
            }
        }
        return dist[R - 1][C - 1];
    }

    static int layered(int[][] g) {
        int R = g.length, C = g[0].length;
        int[][] dist = new int[R][C];
        for (int[] row : dist) Arrays.fill(row, -1);
        List<int[]> frontier = new ArrayList<>();
        spread(g, 0, 0, 0, dist, frontier);
        for (int cost = 0; ; cost++) {
            if (dist[R - 1][C - 1] != -1) return dist[R - 1][C - 1];
            List<int[]> next = new ArrayList<>();
            for (int[] cell : frontier)
                for (int[] d : D) {
                    int nr = cell[0] + d[0], nc = cell[1] + d[1];
                    if (nr >= 0 && nc >= 0 && nr < R && nc < C && dist[nr][nc] == -1) spread(g, nr, nc, cost + 1, dist, next);
                }
            frontier = next;
        }
    }

    /** mark every cell reachable for free (following arrows) from (r, c) with this cost */
    static void spread(int[][] g, int r, int c, int cost, int[][] dist, List<int[]> out) {
        while (r >= 0 && c >= 0 && r < g.length && c < g[0].length && dist[r][c] == -1) {
            dist[r][c] = cost; out.add(new int[]{r, c});
            int k = g[r][c] - 1;
            r += D[k][0]; c += D[k][1];
        }
    }

    public static void main(String[] args) {
        int[][] a = {{1, 1, 1, 1}, {2, 2, 2, 2}, {1, 1, 1, 1}, {2, 2, 2, 2}};
        check(minCost(a), 3); check(dijkstra(a), 3); check(layered(a), 3);
        check(minCost(new int[][]{{1, 1, 3}, {3, 2, 2}, {1, 1, 4}}), 0); check(minCost(new int[][]{{1, 2}, {4, 3}}), 1);
        Random r = new Random(49);
        for (int t = 0; t < 2000; t++) {
            int R = r.nextInt(6) + 1, C = r.nextInt(6) + 1;
            int[][] g = new int[R][C];
            for (int[] row : g) for (int c = 0; c < C; c++) row[c] = r.nextInt(4) + 1;
            int e = relaxOracle(g);
            check(minCost(g), e); check(dijkstra(g), e); check(layered(g), e);
        }
        System.out.println("MinimumCostToMakeValidPath: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
