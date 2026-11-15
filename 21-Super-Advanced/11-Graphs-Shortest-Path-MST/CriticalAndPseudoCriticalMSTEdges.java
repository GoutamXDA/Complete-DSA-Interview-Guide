import java.util.*;

/**
 * LC 1489 — Find Critical and Pseudo-Critical Edges in MST. Explanation: CriticalAndPseudoCriticalMSTEdges.md
 * 1. Enumerate all spanning trees (oracle)              exponential
 * 2. Kruskal with one edge skipped / forced             O(E^2 α(V))
 */
public class CriticalAndPseudoCriticalMSTEdges {

    static int[] par;
    static int find(int x) { while (par[x] != x) { par[x] = par[par[x]]; x = par[x]; } return x; }
    static boolean union(int a, int b) { a = find(a); b = find(b); if (a == b) return false; par[a] = b; return true; }

    static List<List<Integer>> enumerate(int n, int[][] edges) {
        int m = edges.length, best = Integer.MAX_VALUE;
        List<Integer> masks = new ArrayList<>();
        for (int mask = 0; mask < 1 << m; mask++) {
            if (Integer.bitCount(mask) != n - 1) continue;
            par = new int[n];
            for (int i = 0; i < n; i++) par[i] = i;
            int w = 0; boolean ok = true;
            for (int i = 0; i < m && ok; i++) if ((mask >> i & 1) == 1) { if (!union(edges[i][0], edges[i][1])) ok = false; w += edges[i][2]; }
            if (!ok) continue;
            if (w < best) { best = w; masks.clear(); }
            if (w == best) masks.add(mask);
        }
        List<Integer> crit = new ArrayList<>(), pseudo = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int in = 0;
            for (int mask : masks) if ((mask >> i & 1) == 1) in++;
            if (in == masks.size()) crit.add(i); else if (in > 0) pseudo.add(i);
        }
        return List.of(crit, pseudo);
    }

    /** MST weight using sorted order; skip edge `skip`, force edge `force` first. MAX_VALUE if disconnected. */
    static int kruskal(int n, int[][] edges, Integer[] order, int skip, int force) {
        par = new int[n];
        for (int i = 0; i < n; i++) par[i] = i;
        int w = 0, used = 0;
        if (force >= 0) { union(edges[force][0], edges[force][1]); w += edges[force][2]; used++; }
        for (int i : order) {
            if (i == skip || i == force) continue;
            if (union(edges[i][0], edges[i][1])) { w += edges[i][2]; used++; }
        }
        return used == n - 1 ? w : Integer.MAX_VALUE;
    }

    static List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        Integer[] order = new Integer[edges.length];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> edges[i][2]));
        int W = kruskal(n, edges, order, -1, -1);
        List<Integer> crit = new ArrayList<>(), pseudo = new ArrayList<>();
        for (int e = 0; e < edges.length; e++) {
            if (kruskal(n, edges, order, e, -1) > W) crit.add(e);
            else if (kruskal(n, edges, order, -1, e) == W) pseudo.add(e);
        }
        return List.of(crit, pseudo);
    }

    public static void main(String[] args) {
        int[][] ex = {{0, 1, 1}, {1, 2, 1}, {2, 3, 2}, {0, 3, 2}, {0, 4, 3}, {3, 4, 3}, {1, 4, 6}};
        check(findCriticalAndPseudoCriticalEdges(5, ex).toString(), "[[0, 1], [2, 3, 4, 5]]");
        check(findCriticalAndPseudoCriticalEdges(4, new int[][]{{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {0, 3, 1}}).toString(), "[[], [0, 1, 2, 3]]");
        Random r = new Random(50);
        for (int t = 0; t < 400; t++) {
            int n = r.nextInt(5) + 2;
            List<int[]> es = new ArrayList<>();
            for (int v = 1; v < n; v++) es.add(new int[]{r.nextInt(v), v, r.nextInt(4) + 1});     // connected
            for (int k = r.nextInt(5); k > 0; k--) { int a = r.nextInt(n), b = r.nextInt(n); if (a != b) es.add(new int[]{a, b, r.nextInt(4) + 1}); }
            int[][] edges = es.toArray(new int[0][]);
            if (edges.length > 12) continue;
            check(findCriticalAndPseudoCriticalEdges(n, edges).toString(), enumerate(n, edges).toString());
        }
        System.out.println("CriticalAndPseudoCriticalMSTEdges: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
