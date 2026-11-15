import java.util.*;

/**
 * LC 1192 — Critical Connections in a Network (bridges). Explanation: CriticalConnections.md
 * 1. Remove each edge + connectivity check     O(E (V + E))
 * 2. Tarjan low-link, recursive                O(V + E)
 * 3. Tarjan low-link, iterative (no stack overflow on 10^5 nodes)
 */
public class CriticalConnections {

    static List<List<Integer>>[] adjOf(int n, List<List<Integer>> edges) {
        List<List<Integer>>[] g = new List[n];   // each entry: {neighbour, edgeId}
        for (int i = 0; i < n; i++) g[i] = new ArrayList<>();
        for (int id = 0; id < edges.size(); id++) {
            int a = edges.get(id).get(0), b = edges.get(id).get(1);
            g[a].add(List.of(b, id)); g[b].add(List.of(a, id));
        }
        return g;
    }

    static List<List<Integer>> brute(int n, List<List<Integer>> edges) {
        List<List<Integer>> res = new ArrayList<>();
        for (int skip = 0; skip < edges.size(); skip++) {
            List<List<Integer>>[] g = adjOf(n, edges);
            boolean[] seen = new boolean[n];
            Deque<Integer> st = new ArrayDeque<>(List.of(0)); seen[0] = true; int cnt = 1;
            while (!st.isEmpty()) for (List<Integer> e : g[st.pop()]) if (e.get(1) != skip && !seen[e.get(0)]) { seen[e.get(0)] = true; cnt++; st.push(e.get(0)); }
            if (cnt < n) res.add(norm(edges.get(skip)));
        }
        return sorted(res);
    }

    static int timer;

    static List<List<Integer>> tarjan(int n, List<List<Integer>> edges) {
        List<List<Integer>>[] g = adjOf(n, edges);
        int[] tin = new int[n], low = new int[n];
        Arrays.fill(tin, -1);
        List<List<Integer>> res = new ArrayList<>();
        timer = 0;
        for (int s = 0; s < n; s++) if (tin[s] == -1) dfs(s, -1, g, tin, low, res);
        return sorted(res);
    }

    static void dfs(int u, int parentEdge, List<List<Integer>>[] g, int[] tin, int[] low, List<List<Integer>> res) {
        tin[u] = low[u] = timer++;
        for (List<Integer> e : g[u]) {
            int v = e.get(0), id = e.get(1);
            if (id == parentEdge) continue;                       // skip the tree edge itself (handles parallel edges)
            if (tin[v] == -1) {
                dfs(v, id, g, tin, low, res);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > tin[u]) res.add(norm(List.of(u, v)));
            } else low[u] = Math.min(low[u], tin[v]);
        }
    }

    static List<List<Integer>> tarjanIterative(int n, List<List<Integer>> edges) {
        List<List<Integer>>[] g = adjOf(n, edges);
        int[] tin = new int[n], low = new int[n], parentEdge = new int[n], it = new int[n], parent = new int[n];
        Arrays.fill(tin, -1);
        List<List<Integer>> res = new ArrayList<>();
        int t = 0;
        for (int s = 0; s < n; s++) {
            if (tin[s] != -1) continue;
            Deque<Integer> st = new ArrayDeque<>();
            st.push(s); tin[s] = low[s] = t++; parentEdge[s] = -1; parent[s] = -1;
            while (!st.isEmpty()) {
                int u = st.peek();
                if (it[u] < g[u].size()) {
                    List<Integer> e = g[u].get(it[u]++);
                    int v = e.get(0), id = e.get(1);
                    if (id == parentEdge[u]) continue;
                    if (tin[v] == -1) { tin[v] = low[v] = t++; parentEdge[v] = id; parent[v] = u; st.push(v); }
                    else low[u] = Math.min(low[u], tin[v]);
                } else {
                    st.pop();
                    int p = parent[u];
                    if (p != -1) {
                        low[p] = Math.min(low[p], low[u]);
                        if (low[u] > tin[p]) res.add(norm(List.of(p, u)));
                    }
                }
            }
        }
        return sorted(res);
    }

    static List<Integer> norm(List<Integer> e) { return List.of(Math.min(e.get(0), e.get(1)), Math.max(e.get(0), e.get(1))); }
    static List<List<Integer>> sorted(List<List<Integer>> r) { r.sort(Comparator.comparing((List<Integer> e) -> e.get(0)).thenComparing(e -> e.get(1))); return r; }

    public static void main(String[] args) {
        List<List<Integer>> ex = List.of(List.of(0, 1), List.of(1, 2), List.of(2, 0), List.of(1, 3));
        check(tarjan(4, ex).toString(), "[[1, 3]]"); check(tarjanIterative(4, ex).toString(), "[[1, 3]]");
        check(tarjan(2, List.of(List.of(0, 1), List.of(0, 1))).toString(), "[]");          // parallel edges
        Random r = new Random(45);
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(8) + 2;
            List<List<Integer>> edges = new ArrayList<>();
            for (int v = 1; v < n; v++) edges.add(List.of(r.nextInt(v), v));             // spanning tree -> connected
            for (int k = r.nextInt(n); k > 0; k--) { int a = r.nextInt(n), b = r.nextInt(n); if (a != b) edges.add(List.of(a, b)); }
            String e = brute(n, edges).toString();
            check(tarjan(n, edges).toString(), e); check(tarjanIterative(n, edges).toString(), e);
        }
        List<List<Integer>> chain = new ArrayList<>();
        for (int v = 1; v < 100000; v++) chain.add(List.of(v - 1, v));
        check(tarjanIterative(100000, chain).size(), 99999);                                  // deep path: iterative is safe
        System.out.println("CriticalConnections: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
