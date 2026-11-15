import java.util.*;

/**
 * Kosaraju SCC (directed): 1) DFS, push by finish time  2) reverse graph  3) DFS in stack order; each tree = SCC. O(V+E)
 * Tarjan bridges (LC 1192): tin[u] discovery time, low[u] = min tin reachable using one back edge.
 *   Edge (u,v) is a bridge if low[v] > tin[u]. O(V+E)
 * Articulation points: u (non-root) is a cut vertex if some child v has low[v] >= tin[u];
 *   root is a cut vertex if it has > 1 DFS child.
 */
public class AdvancedGraphs {

    static int kosaraju(List<List<Integer>> g) {
        int n = g.size();
        boolean[] vis = new boolean[n];
        Deque<Integer> order = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (!vis[i]) finish(g, i, vis, order);
        List<List<Integer>> rev = new ArrayList<>();
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
        for (int u = 0; u < n; u++) for (int v : g.get(u)) rev.get(v).add(u);
        Arrays.fill(vis, false);
        int scc = 0;
        while (!order.isEmpty()) {
            int u = order.pop();
            if (!vis[u]) { scc++; mark(rev, u, vis); }
        }
        return scc;
    }

    static void finish(List<List<Integer>> g, int u, boolean[] vis, Deque<Integer> order) {
        vis[u] = true;
        for (int v : g.get(u)) if (!vis[v]) finish(g, v, vis, order);
        order.push(u);
    }

    static void mark(List<List<Integer>> g, int u, boolean[] vis) {
        vis[u] = true;
        for (int v : g.get(u)) if (!vis[v]) mark(g, v, vis);
    }

    static int timer;

    static List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (List<Integer> c : connections) { g.get(c.get(0)).add(c.get(1)); g.get(c.get(1)).add(c.get(0)); }
        int[] tin = new int[n], low = new int[n];
        Arrays.fill(tin, -1);
        List<List<Integer>> bridges = new ArrayList<>();
        timer = 0;
        for (int i = 0; i < n; i++) if (tin[i] == -1) bridgeDfs(g, i, -1, tin, low, bridges);
        return bridges;
    }

    static void bridgeDfs(List<List<Integer>> g, int u, int parent, int[] tin, int[] low, List<List<Integer>> out) {
        tin[u] = low[u] = timer++;
        for (int v : g.get(u)) {
            if (v == parent) continue;
            if (tin[v] == -1) {
                bridgeDfs(g, v, u, tin, low, out);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > tin[u]) out.add(List.of(Math.min(u, v), Math.max(u, v)));
            } else low[u] = Math.min(low[u], tin[v]);
        }
    }

    static List<Integer> articulationPoints(List<List<Integer>> g) {
        int n = g.size();
        int[] tin = new int[n], low = new int[n];
        Arrays.fill(tin, -1);
        boolean[] cut = new boolean[n];
        timer = 0;
        for (int i = 0; i < n; i++) if (tin[i] == -1) apDfs(g, i, -1, tin, low, cut);
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) if (cut[i]) res.add(i);
        return res;
    }

    static void apDfs(List<List<Integer>> g, int u, int parent, int[] tin, int[] low, boolean[] cut) {
        tin[u] = low[u] = timer++;
        int children = 0;
        for (int v : g.get(u)) {
            if (v == parent) continue;
            if (tin[v] == -1) {
                children++;
                apDfs(g, v, u, tin, low, cut);
                low[u] = Math.min(low[u], low[v]);
                if (parent != -1 && low[v] >= tin[u]) cut[u] = true;
            } else low[u] = Math.min(low[u], tin[v]);
        }
        if (parent == -1 && children > 1) cut[u] = true;
    }

    static List<List<Integer>> graph(int n, int[][] edges, boolean directed) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) { g.get(e[0]).add(e[1]); if (!directed) g.get(e[1]).add(e[0]); }
        return g;
    }

    public static void main(String[] args) {
        check(kosaraju(graph(5, new int[][]{{1, 0}, {0, 2}, {2, 1}, {0, 3}, {3, 4}}, true)), 3);
        check(criticalConnections(4, List.of(List.of(0, 1), List.of(1, 2), List.of(2, 0), List.of(1, 3))).toString(), "[[1, 3]]");
        check(articulationPoints(graph(5, new int[][]{{1, 0}, {0, 2}, {2, 1}, {0, 3}, {3, 4}}, false)), List.of(0, 3));
        System.out.println("AdvancedGraphs: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
