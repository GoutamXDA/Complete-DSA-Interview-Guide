import java.util.*;

/**
 * Minimum Spanning Tree: connect all V vertices with V-1 edges of minimum total weight.
 *
 * Prim    : grow from a vertex, always take the cheapest edge leaving the tree (min-heap). O(E log V)
 *           Dense graph variant (LC 1584): O(V^2) without a heap.
 * Kruskal : sort edges, add if endpoints are in different DSU sets. O(E log E)
 * LC 1319 : need (components - 1) extra cables; impossible if edges < n - 1.
 */
public class MinimumSpanningTree {

    static class DSU {
        int[] parent, rank; int components;
        DSU(int n) { parent = new int[n]; rank = new int[n]; components = n; for (int i = 0; i < n; i++) parent[i] = i; }
        int find(int x) { return parent[x] == x ? x : (parent[x] = find(parent[x])); }
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
            components--;
            return true;
        }
    }

    /** edges: {u, v, w}, undirected */
    static int kruskal(int n, int[][] edges) {
        int[][] e = edges.clone();
        Arrays.sort(e, Comparator.comparingInt(x -> x[2]));
        DSU dsu = new DSU(n);
        int total = 0, used = 0;
        for (int[] x : e) if (dsu.union(x[0], x[1])) { total += x[2]; if (++used == n - 1) break; }
        return total;
    }

    static int prim(int n, int[][] edges) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] x : edges) { adj.get(x[0]).add(new int[]{x[1], x[2]}); adj.get(x[1]).add(new int[]{x[0], x[2]}); }
        boolean[] in = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
        pq.offer(new int[]{0, 0});
        int total = 0;
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            if (in[c[0]]) continue;
            in[c[0]] = true; total += c[1];
            for (int[] nb : adj.get(c[0])) if (!in[nb[0]]) pq.offer(nb);
        }
        return total;
    }

    /** LC 1584: complete graph with Manhattan distances -> O(V^2) Prim. */
    static int minCostConnectPoints(int[][] p) {
        int n = p.length, total = 0;
        int[] best = new int[n]; Arrays.fill(best, Integer.MAX_VALUE); best[0] = 0;
        boolean[] in = new boolean[n];
        for (int k = 0; k < n; k++) {
            int u = -1;
            for (int i = 0; i < n; i++) if (!in[i] && (u == -1 || best[i] < best[u])) u = i;
            in[u] = true; total += best[u];
            for (int v = 0; v < n; v++) if (!in[v])
                best[v] = Math.min(best[v], Math.abs(p[u][0] - p[v][0]) + Math.abs(p[u][1] - p[v][1]));
        }
        return total;
    }

    static int makeConnected(int n, int[][] connections) {
        if (connections.length < n - 1) return -1;
        DSU d = new DSU(n);
        for (int[] c : connections) d.union(c[0], c[1]);
        return d.components - 1;
    }

    public static void main(String[] args) {
        int[][] edges = {{0, 1, 2}, {0, 3, 6}, {1, 2, 3}, {1, 3, 8}, {1, 4, 5}, {2, 4, 7}, {3, 4, 9}};
        check(kruskal(5, edges), 16); check(prim(5, edges), 16);
        check(minCostConnectPoints(new int[][]{{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}}), 20);
        check(makeConnected(4, new int[][]{{0, 1}, {0, 2}, {1, 2}}), 1);
        check(makeConnected(6, new int[][]{{0, 1}, {0, 2}, {0, 3}, {1, 2}}), -1);
        System.out.println("MinimumSpanningTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
