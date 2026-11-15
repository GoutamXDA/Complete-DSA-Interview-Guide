import java.util.*;

/**
 * BFS / DFS foundations, all O(V + E) (grids: O(R*C)).
 *
 * LC 547 Provinces: DFS over adjacency matrix (or DSU).
 * LC 200 Islands: DFS sink each land cell (or BFS to avoid deep recursion).
 * LC 733 Flood fill: DFS; guard when new colour == old colour.
 * LC 994 Rotting oranges: multi-source BFS from all rotten at minute 0.
 * LC 542 01 Matrix: brute BFS from every cell O((RC)^2) | multi-source BFS from all zeros O(RC)
 * LC 130 Surrounded regions: mark 'O's connected to the border as safe; flip the rest.
 * LC 133 Clone graph: DFS with old->new map.
 */
public class GraphTraversals {

    static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static List<Integer> bfs(List<List<Integer>> adj, int src) {
        List<Integer> order = new ArrayList<>();
        boolean[] vis = new boolean[adj.size()];
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(src); vis[src] = true;                     // mark when ENQUEUED, not when polled
        while (!q.isEmpty()) {
            int u = q.poll(); order.add(u);
            for (int v : adj.get(u)) if (!vis[v]) { vis[v] = true; q.offer(v); }
        }
        return order;
    }

    static List<Integer> dfs(List<List<Integer>> adj, int src) {
        List<Integer> order = new ArrayList<>();
        dfsRec(adj, src, new boolean[adj.size()], order);
        return order;
    }

    static void dfsRec(List<List<Integer>> adj, int u, boolean[] vis, List<Integer> order) {
        vis[u] = true; order.add(u);
        for (int v : adj.get(u)) if (!vis[v]) dfsRec(adj, v, vis, order);
    }

    static int findCircleNum(int[][] m) {
        int n = m.length, count = 0;
        boolean[] vis = new boolean[n];
        for (int i = 0; i < n; i++) if (!vis[i]) { count++; province(m, i, vis); }
        return count;
    }

    static void province(int[][] m, int u, boolean[] vis) {
        vis[u] = true;
        for (int v = 0; v < m.length; v++) if (m[u][v] == 1 && !vis[v]) province(m, v, vis);
    }

    static int numIslands(char[][] g) {
        int count = 0;
        for (int r = 0; r < g.length; r++)
            for (int c = 0; c < g[0].length; c++)
                if (g[r][c] == '1') { count++; sink(g, r, c); }
        return count;
    }

    static void sink(char[][] g, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != '1') return;
        g[r][c] = '0';
        for (int[] d : DIRS) sink(g, r + d[0], c + d[1]);
    }

    static int[][] floodFill(int[][] img, int sr, int sc, int color) {
        int old = img[sr][sc];
        if (old != color) fill(img, sr, sc, old, color);
        return img;
    }

    static void fill(int[][] img, int r, int c, int old, int color) {
        if (r < 0 || c < 0 || r >= img.length || c >= img[0].length || img[r][c] != old) return;
        img[r][c] = color;
        for (int[] d : DIRS) fill(img, r + d[0], c + d[1], old, color);
    }

    static int orangesRotting(int[][] g) {
        int R = g.length, C = g[0].length, fresh = 0, minutes = 0;
        Queue<int[]> q = new ArrayDeque<>();
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) {
            if (g[r][c] == 2) q.offer(new int[]{r, c});
            else if (g[r][c] == 1) fresh++;
        }
        while (!q.isEmpty() && fresh > 0) {
            minutes++;
            for (int i = q.size(); i > 0; i--) {
                int[] cur = q.poll();
                for (int[] d : DIRS) {
                    int nr = cur[0] + d[0], nc = cur[1] + d[1];
                    if (nr >= 0 && nc >= 0 && nr < R && nc < C && g[nr][nc] == 1) {
                        g[nr][nc] = 2; fresh--; q.offer(new int[]{nr, nc});
                    }
                }
            }
        }
        return fresh == 0 ? minutes : -1;
    }

    static int[][] updateMatrix(int[][] mat) {
        int R = mat.length, C = mat[0].length;
        int[][] dist = new int[R][C];
        Queue<int[]> q = new ArrayDeque<>();
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) {
            if (mat[r][c] == 0) q.offer(new int[]{r, c}); else dist[r][c] = -1;
        }
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] d : DIRS) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nc >= 0 && nr < R && nc < C && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[cur[0]][cur[1]] + 1; q.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    static void solveSurrounded(char[][] b) {
        int R = b.length, C = b[0].length;
        for (int r = 0; r < R; r++) { safe(b, r, 0); safe(b, r, C - 1); }
        for (int c = 0; c < C; c++) { safe(b, 0, c); safe(b, R - 1, c); }
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++)
            b[r][c] = b[r][c] == 'S' ? 'O' : 'X';
    }

    static void safe(char[][] b, int r, int c) {
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] != 'O') return;
        b[r][c] = 'S';
        for (int[] d : DIRS) safe(b, r + d[0], c + d[1]);
    }

    static class Node { int val; List<Node> neighbors = new ArrayList<>(); Node(int v) { val = v; } }

    static Node cloneGraph(Node n) { return clone(n, new HashMap<>()); }
    static Node clone(Node n, Map<Node, Node> map) {
        if (n == null) return null;
        if (map.containsKey(n)) return map.get(n);
        Node copy = new Node(n.val);
        map.put(n, copy);
        for (Node nb : n.neighbors) copy.neighbors.add(clone(nb, map));
        return copy;
    }

    static List<List<Integer>> adj(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) { g.get(e[0]).add(e[1]); g.get(e[1]).add(e[0]); }
        return g;
    }

    public static void main(String[] args) {
        List<List<Integer>> g = adj(6, new int[][]{{0, 1}, {0, 2}, {1, 3}, {2, 4}, {3, 5}});
        check(bfs(g, 0), List.of(0, 1, 2, 3, 4, 5)); check(dfs(g, 0), List.of(0, 1, 3, 5, 2, 4));
        check(findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}), 2);
        char[][] grid = {"11000".toCharArray(), "11000".toCharArray(), "00100".toCharArray(), "00011".toCharArray()};
        check(numIslands(grid), 3);
        check(Arrays.deepToString(floodFill(new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2)), "[[2, 2, 2], [2, 2, 0], [2, 0, 1]]");
        check(orangesRotting(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}), 4); check(orangesRotting(new int[][]{{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}), -1);
        check(Arrays.deepToString(updateMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}})), "[[0, 0, 0], [0, 1, 0], [1, 2, 1]]");
        char[][] b = {"XXXX".toCharArray(), "XOOX".toCharArray(), "XXOX".toCharArray(), "XOXX".toCharArray()};
        solveSurrounded(b);
        check(new String(b[1]) + new String(b[3]), "XXXXXOXX");
        Node n1 = new Node(1), n2 = new Node(2); n1.neighbors.add(n2); n2.neighbors.add(n1);
        Node c1 = cloneGraph(n1);
        check(c1 != n1 && c1.neighbors.get(0).neighbors.get(0) == c1, true);
        System.out.println("GraphTraversals: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
