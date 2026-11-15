import java.util.*;

/**
 * Undirected cycle: BFS/DFS — visited neighbour that is not the parent => cycle. (Or DSU.)
 * Directed cycle  : DFS with colours 0=unvisited, 1=on current path, 2=done. Back edge to colour 1 => cycle.
 *                   (Or Kahn: if topo order has < V nodes, there's a cycle.)
 * Topological sort: DFS post-order reversed | Kahn's BFS on indegree 0.
 * LC 207/210 Course schedule: Kahn.
 * LC 785 Bipartite: BFS 2-colouring per component.
 * LC 269 Alien dictionary: first differing char of adjacent words gives an edge; prefix conflict -> "".
 * LC 802 Safe states: nodes not reaching a cycle = Kahn on the reversed graph (outdegree 0 = terminal).
 */
public class CycleTopoSort {

    static boolean undirectedCycleBFS(List<List<Integer>> g) {
        int n = g.size();
        boolean[] vis = new boolean[n];
        for (int s = 0; s < n; s++) {
            if (vis[s]) continue;
            Queue<int[]> q = new ArrayDeque<>();
            q.offer(new int[]{s, -1}); vis[s] = true;
            while (!q.isEmpty()) {
                int[] cur = q.poll();
                for (int v : g.get(cur[0])) {
                    if (!vis[v]) { vis[v] = true; q.offer(new int[]{v, cur[0]}); }
                    else if (v != cur[1]) return true;
                }
            }
        }
        return false;
    }

    static boolean undirectedCycleDFS(List<List<Integer>> g) {
        boolean[] vis = new boolean[g.size()];
        for (int s = 0; s < g.size(); s++) if (!vis[s] && ucd(g, s, -1, vis)) return true;
        return false;
    }

    static boolean ucd(List<List<Integer>> g, int u, int parent, boolean[] vis) {
        vis[u] = true;
        for (int v : g.get(u)) {
            if (!vis[v]) { if (ucd(g, v, u, vis)) return true; }
            else if (v != parent) return true;
        }
        return false;
    }

    static boolean directedCycle(List<List<Integer>> g) {
        int[] color = new int[g.size()];
        for (int s = 0; s < g.size(); s++) if (color[s] == 0 && dcd(g, s, color)) return true;
        return false;
    }

    static boolean dcd(List<List<Integer>> g, int u, int[] color) {
        color[u] = 1;
        for (int v : g.get(u)) {
            if (color[v] == 1) return true;
            if (color[v] == 0 && dcd(g, v, color)) return true;
        }
        color[u] = 2;
        return false;
    }

    static List<Integer> topoDFS(List<List<Integer>> g) {
        Deque<Integer> st = new ArrayDeque<>();
        boolean[] vis = new boolean[g.size()];
        for (int s = 0; s < g.size(); s++) if (!vis[s]) topo(g, s, vis, st);
        return new ArrayList<>(st);  // stack iteration order = reverse post-order
    }

    static void topo(List<List<Integer>> g, int u, boolean[] vis, Deque<Integer> st) {
        vis[u] = true;
        for (int v : g.get(u)) if (!vis[v]) topo(g, v, vis, st);
        st.push(u);
    }

    static List<Integer> kahn(List<List<Integer>> g) {
        int n = g.size();
        int[] indeg = new int[n];
        for (List<Integer> nbrs : g) for (int v : nbrs) indeg[v]++;
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.offer(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll(); order.add(u);
            for (int v : g.get(u)) if (--indeg[v] == 0) q.offer(v);
        }
        return order; // size < n => cycle
    }

    static int[] findOrder(int n, int[][] prereq) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] p : prereq) g.get(p[1]).add(p[0]);
        List<Integer> order = kahn(g);
        return order.size() == n ? order.stream().mapToInt(Integer::intValue).toArray() : new int[0];
    }

    static boolean canFinish(int n, int[][] prereq) { return findOrder(n, prereq).length == n; }

    static boolean isBipartite(int[][] graph) {
        int[] color = new int[graph.length]; // 0 none, 1 / -1
        for (int s = 0; s < graph.length; s++) {
            if (color[s] != 0) continue;
            Queue<Integer> q = new ArrayDeque<>(List.of(s)); color[s] = 1;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : graph[u]) {
                    if (color[v] == 0) { color[v] = -color[u]; q.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    static String alienOrder(String[] words) {
        Map<Character, Set<Character>> g = new TreeMap<>();
        Map<Character, Integer> indeg = new TreeMap<>();
        for (String w : words) for (char c : w.toCharArray()) { g.putIfAbsent(c, new TreeSet<>()); indeg.putIfAbsent(c, 0); }
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            if (a.length() > b.length() && a.startsWith(b)) return "";   // "abc" before "ab" is invalid
            for (int j = 0; j < Math.min(a.length(), b.length()); j++) {
                if (a.charAt(j) != b.charAt(j)) {
                    if (g.get(a.charAt(j)).add(b.charAt(j))) indeg.merge(b.charAt(j), 1, Integer::sum);
                    break;
                }
            }
        }
        Queue<Character> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue() == 0) q.offer(e.getKey());
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char nx : g.get(c)) if (indeg.merge(nx, -1, Integer::sum) == 0) q.offer(nx);
        }
        return sb.length() == indeg.size() ? sb.toString() : "";
    }

    static List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> rev = new ArrayList<>();
        for (int i = 0; i < n; i++) rev.add(new ArrayList<>());
        int[] out = new int[n];
        for (int u = 0; u < n; u++) for (int v : graph[u]) { rev.get(v).add(u); out[u]++; }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (out[i] == 0) q.offer(i);
        boolean[] safe = new boolean[n];
        while (!q.isEmpty()) {
            int u = q.poll(); safe[u] = true;
            for (int p : rev.get(u)) if (--out[p] == 0) q.offer(p);
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) if (safe[i]) res.add(i);
        return res;
    }

    static List<List<Integer>> graph(int n, int[][] edges, boolean directed) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) { g.get(e[0]).add(e[1]); if (!directed) g.get(e[1]).add(e[0]); }
        return g;
    }

    public static void main(String[] args) {
        List<List<Integer>> tree = graph(4, new int[][]{{0, 1}, {1, 2}, {2, 3}}, false);
        List<List<Integer>> cyc = graph(4, new int[][]{{0, 1}, {1, 2}, {2, 0}, {2, 3}}, false);
        check(undirectedCycleBFS(tree), false); check(undirectedCycleBFS(cyc), true);
        check(undirectedCycleDFS(tree), false); check(undirectedCycleDFS(cyc), true);
        List<List<Integer>> dag = graph(6, new int[][]{{5, 2}, {5, 0}, {4, 0}, {4, 1}, {2, 3}, {3, 1}}, true);
        check(directedCycle(dag), false); check(directedCycle(graph(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}, true)), true);
        check(isValidTopo(dag, topoDFS(dag)), true); check(isValidTopo(dag, kahn(dag)), true);
        check(canFinish(2, new int[][]{{1, 0}}), true); check(canFinish(2, new int[][]{{1, 0}, {0, 1}}), false);
        check(Arrays.toString(findOrder(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}})), "[0, 1, 2, 3]");
        check(isBipartite(new int[][]{{1, 3}, {0, 2}, {1, 3}, {0, 2}}), true);
        check(isBipartite(new int[][]{{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}}), false);
        check(alienOrder(new String[]{"wrt", "wrf", "er", "ett", "rftt"}), "wertf");
        check(alienOrder(new String[]{"abc", "ab"}), "");
        check(eventualSafeNodes(new int[][]{{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}}), List.of(2, 4, 5, 6));
        System.out.println("CycleTopoSort: all tests passed");
    }

    static boolean isValidTopo(List<List<Integer>> g, List<Integer> order) {
        if (order.size() != g.size()) return false;
        int[] pos = new int[g.size()];
        for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;
        for (int u = 0; u < g.size(); u++) for (int v : g.get(u)) if (pos[u] > pos[v]) return false;
        return true;
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
