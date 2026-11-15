import java.util.*;

/**
 * Hard graph problems.
 *
 * LC 827 Making a large island (flip at most one 0): brute flip each 0 and DFS O((RC)^2)
 *        | label each island with its size once, then each 0 sums distinct neighbouring island sizes + 1. O(RC)
 * LC 305 Number of islands II (add land online): BFS recount after each add O(k*RC) | DSU, each add O(α). O(k α)
 * LC 778 Swim in rising water: binary search on time + BFS O(n^2 log n) | Dijkstra minimising the max height O(n^2 log n)
 * LC 815 Bus routes: BFS over routes (not stops); stop -> routes map; each route expanded once. O(sum of route lengths)
 * LC 126 Word ladder II: BFS layer by layer recording parents (only from the previous layer), then DFS back from end.
 * LC 332 Reconstruct itinerary: Hierholzer's Euler path; adjacency as min-heaps for lexical order; add airport on backtrack.
 */
public class GraphProblemsIII {

    static final int[][] D4 = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int largestIslandBrute(int[][] g) {
        int n = g.length, best = 0;
        boolean anyZero = false;
        for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
            if (g[r][c] != 0) continue;
            anyZero = true;
            g[r][c] = 1;
            best = Math.max(best, size(g, r, c, new boolean[n][n]));
            g[r][c] = 0;
        }
        return anyZero ? best : n * n;
    }

    static int size(int[][] g, int r, int c, boolean[][] vis) {
        if (r < 0 || c < 0 || r >= g.length || c >= g.length || g[r][c] == 0 || vis[r][c]) return 0;
        vis[r][c] = true;
        int s = 1;
        for (int[] d : D4) s += size(g, r + d[0], c + d[1], vis);
        return s;
    }

    static int largestIsland(int[][] grid) {
        int n = grid.length, label = 2;
        int[][] g = new int[n][];
        for (int i = 0; i < n; i++) g[i] = grid[i].clone();
        Map<Integer, Integer> area = new HashMap<>();
        for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) if (g[r][c] == 1) area.put(label, paint(g, r, c, label++));
        int best = area.values().stream().max(Integer::compare).orElse(0);
        for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) {
            if (g[r][c] != 0) continue;
            Set<Integer> seen = new HashSet<>();
            int s = 1;
            for (int[] d : D4) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && g[nr][nc] > 1 && seen.add(g[nr][nc])) s += area.get(g[nr][nc]);
            }
            best = Math.max(best, s);
        }
        return best;
    }

    static int paint(int[][] g, int r, int c, int label) {
        if (r < 0 || c < 0 || r >= g.length || c >= g.length || g[r][c] != 1) return 0;
        g[r][c] = label;
        int s = 1;
        for (int[] d : D4) s += paint(g, r + d[0], c + d[1], label);
        return s;
    }

    static List<Integer> numIslands2Brute(int m, int n, int[][] positions) {
        int[][] g = new int[m][n];
        List<Integer> res = new ArrayList<>();
        for (int[] p : positions) {
            g[p[0]][p[1]] = 1;
            boolean[][] vis = new boolean[m][n];
            int cnt = 0;
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) if (g[r][c] == 1 && !vis[r][c]) { cnt++; mark(g, r, c, vis); }
            res.add(cnt);
        }
        return res;
    }

    static void mark(int[][] g, int r, int c, boolean[][] vis) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] == 0 || vis[r][c]) return;
        vis[r][c] = true;
        for (int[] d : D4) mark(g, r + d[0], c + d[1], vis);
    }

    static int[] par;
    static int find(int x) { while (par[x] != x) { par[x] = par[par[x]]; x = par[x]; } return x; }

    static List<Integer> numIslands2(int m, int n, int[][] positions) {
        par = new int[m * n];
        Arrays.fill(par, -1);                      // -1 = water
        List<Integer> res = new ArrayList<>();
        int count = 0;
        for (int[] p : positions) {
            int id = p[0] * n + p[1];
            if (par[id] != -1) { res.add(count); continue; }   // duplicate position
            par[id] = id; count++;
            for (int[] d : D4) {
                int r = p[0] + d[0], c = p[1] + d[1], nid = r * n + c;
                if (r < 0 || c < 0 || r >= m || c >= n || par[nid] == -1) continue;
                int a = find(id), b = find(nid);
                if (a != b) { par[a] = b; count--; }
            }
            res.add(count);
        }
        return res;
    }

    static int swimInWaterBinarySearch(int[][] g) {
        int n = g.length, lo = g[0][0], hi = n * n - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (reachable(g, mid)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static boolean reachable(int[][] g, int t) {
        int n = g.length;
        if (g[0][0] > t) return false;
        boolean[][] vis = new boolean[n][n];
        Deque<int[]> st = new ArrayDeque<>(List.of(new int[]{0, 0}));
        vis[0][0] = true;
        while (!st.isEmpty()) {
            int[] c = st.pop();
            if (c[0] == n - 1 && c[1] == n - 1) return true;
            for (int[] d : D4) {
                int nr = c[0] + d[0], nc = c[1] + d[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && !vis[nr][nc] && g[nr][nc] <= t) { vis[nr][nc] = true; st.push(new int[]{nr, nc}); }
            }
        }
        return false;
    }

    static int swimInWater(int[][] g) {
        int n = g.length;
        boolean[][] vis = new boolean[n][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[2]));
        pq.offer(new int[]{0, 0, g[0][0]});
        while (true) {
            int[] c = pq.poll();
            if (vis[c[0]][c[1]]) continue;
            vis[c[0]][c[1]] = true;
            if (c[0] == n - 1 && c[1] == n - 1) return c[2];
            for (int[] d : D4) {
                int nr = c[0] + d[0], nc = c[1] + d[1];
                if (nr >= 0 && nc >= 0 && nr < n && nc < n && !vis[nr][nc]) pq.offer(new int[]{nr, nc, Math.max(c[2], g[nr][nc])});
            }
        }
    }

    static int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target) return 0;
        Map<Integer, List<Integer>> stopRoutes = new HashMap<>();
        for (int i = 0; i < routes.length; i++) for (int s : routes[i]) stopRoutes.computeIfAbsent(s, k -> new ArrayList<>()).add(i);
        boolean[] usedRoute = new boolean[routes.length];
        Set<Integer> seenStop = new HashSet<>(List.of(source));
        Queue<Integer> q = new ArrayDeque<>(List.of(source));
        for (int buses = 1; !q.isEmpty(); buses++) {
            for (int sz = q.size(); sz > 0; sz--) {
                int stop = q.poll();
                for (int r : stopRoutes.getOrDefault(stop, List.of())) {
                    if (usedRoute[r]) continue;
                    usedRoute[r] = true;
                    for (int s : routes[r]) {
                        if (s == target) return buses;
                        if (seenStop.add(s)) q.offer(s);
                    }
                }
            }
        }
        return -1;
    }

    static List<List<String>> findLadders(String begin, String end, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        List<List<String>> res = new ArrayList<>();
        if (!dict.contains(end)) return res;
        Map<String, List<String>> parents = new HashMap<>();
        Set<String> layer = new HashSet<>(List.of(begin));
        dict.remove(begin);
        boolean found = false;
        while (!layer.isEmpty() && !found) {
            Set<String> next = new HashSet<>();
            for (String w : layer) {
                char[] cs = w.toCharArray();
                for (int i = 0; i < cs.length; i++) {
                    char orig = cs[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        cs[i] = c;
                        String nw = new String(cs);
                        if (!dict.contains(nw)) continue;
                        next.add(nw);
                        parents.computeIfAbsent(nw, k -> new ArrayList<>()).add(w);
                        if (nw.equals(end)) found = true;
                    }
                    cs[i] = orig;
                }
            }
            dict.removeAll(next);          // remove after the whole layer so several parents can reach a word
            layer = next;
        }
        if (found) back(end, begin, parents, new LinkedList<>(List.of(end)), res);
        res.sort(Comparator.comparing(Object::toString));
        return res;
    }

    static void back(String w, String begin, Map<String, List<String>> parents, LinkedList<String> path, List<List<String>> res) {
        if (w.equals(begin)) { res.add(new ArrayList<>(path)); return; }
        for (String p : parents.get(w)) { path.addFirst(p); back(p, begin, parents, path, res); path.removeFirst(); }
    }

    static List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> g = new HashMap<>();
        for (List<String> t : tickets) g.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
        LinkedList<String> route = new LinkedList<>();
        euler("JFK", g, route);
        return route;
    }

    static void euler(String a, Map<String, PriorityQueue<String>> g, LinkedList<String> route) {
        PriorityQueue<String> pq = g.get(a);
        while (pq != null && !pq.isEmpty()) euler(pq.poll(), g, route);
        route.addFirst(a);              // airports are added when they have no unused outgoing tickets
    }

    public static void main(String[] args) {
        int[][][] tests = {{{1, 0}, {0, 1}}, {{1, 1}, {1, 0}}, {{1, 1}, {1, 1}}, {{0, 0}, {0, 0}}, {{1, 0, 1}, {0, 0, 0}, {1, 0, 1}}};
        int[] exp = {3, 4, 4, 1, 3};
        for (int i = 0; i < tests.length; i++) { check(largestIsland(tests[i]), exp[i]); check(largestIslandBrute(tests[i]), exp[i]); }
        int[][] pos = {{0, 0}, {0, 1}, {1, 2}, {2, 1}, {1, 1}, {1, 1}};
        check(numIslands2(3, 3, pos), List.of(1, 1, 2, 3, 1, 1)); check(numIslands2Brute(3, 3, pos), List.of(1, 1, 2, 3, 1, 1));
        int[][] sw = {{0, 1, 2, 3, 4}, {24, 23, 22, 21, 5}, {12, 13, 14, 15, 16}, {11, 17, 18, 19, 20}, {10, 9, 8, 7, 6}};
        check(swimInWater(sw), 16); check(swimInWaterBinarySearch(sw), 16); check(swimInWater(new int[][]{{0, 2}, {1, 3}}), 3);
        check(numBusesToDestination(new int[][]{{1, 2, 7}, {3, 6, 7}}, 1, 6), 2);
        check(numBusesToDestination(new int[][]{{7, 12}, {4, 5, 15}, {6}, {15, 19}, {9, 12, 13}}, 15, 12), -1);
        check(findLadders("hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog")).toString(),
                "[[hit, hot, dot, dog, cog], [hit, hot, lot, log, cog]]");
        check(findLadders("hit", "cog", List.of("hot", "dot", "dog", "lot", "log")).size(), 0);
        check(findItinerary(List.of(List.of("MUC", "LHR"), List.of("JFK", "MUC"), List.of("SFO", "SJC"), List.of("LHR", "SFO"))),
                List.of("JFK", "MUC", "LHR", "SFO", "SJC"));
        check(findItinerary(List.of(List.of("JFK", "SFO"), List.of("JFK", "ATL"), List.of("SFO", "ATL"), List.of("ATL", "JFK"), List.of("ATL", "SFO"))),
                List.of("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"));
        check(findItinerary(List.of(List.of("JFK", "KUL"), List.of("JFK", "NRT"), List.of("NRT", "JFK"))), List.of("JFK", "NRT", "JFK", "KUL"));
        System.out.println("GraphProblemsIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
