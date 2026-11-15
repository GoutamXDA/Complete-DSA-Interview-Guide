import java.util.*;

/**
 * LC 815 — Bus Routes. Explanation: BusRoutes.md
 * 1. Stop graph with all same-route pairs + BFS     O(Σ len^2)
 * 2. BFS over routes                                O(Σ len)
 * 3. Bipartite stop/route graph with 0-1 BFS        O(Σ len)
 */
public class BusRoutes {

    static int stopGraph(int[][] routes, int s, int t) {
        if (s == t) return 0;
        Map<Integer, Set<Integer>> g = new HashMap<>();
        for (int[] r : routes) for (int a : r) for (int b : r) if (a != b) g.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        Map<Integer, Integer> dist = new HashMap<>(Map.of(s, 0));
        Deque<Integer> q = new ArrayDeque<>(List.of(s));
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : g.getOrDefault(u, Set.of())) if (!dist.containsKey(v)) { dist.put(v, dist.get(u) + 1); if (v == t) return dist.get(v); q.offer(v); }
        }
        return -1;
    }

    static int numBusesToDestination(int[][] routes, int s, int t) {
        if (s == t) return 0;
        Map<Integer, List<Integer>> stopRoutes = new HashMap<>();
        for (int i = 0; i < routes.length; i++) for (int x : routes[i]) stopRoutes.computeIfAbsent(x, k -> new ArrayList<>()).add(i);
        boolean[] usedRoute = new boolean[routes.length];
        Set<Integer> seenStop = new HashSet<>(List.of(s));
        Deque<Integer> q = new ArrayDeque<>();
        for (int rt : stopRoutes.getOrDefault(s, List.of())) { usedRoute[rt] = true; q.offer(rt); }
        for (int buses = 1; !q.isEmpty(); buses++)
            for (int sz = q.size(); sz > 0; sz--) {
                int rt = q.poll();
                for (int x : routes[rt]) {
                    if (x == t) return buses;
                    if (!seenStop.add(x)) continue;
                    for (int nr : stopRoutes.get(x)) if (!usedRoute[nr]) { usedRoute[nr] = true; q.offer(nr); }
                }
            }
        return -1;
    }

    static int bipartite01(int[][] routes, int s, int t) {
        if (s == t) return 0;
        Map<Integer, Integer> stopId = new HashMap<>();
        for (int[] r : routes) for (int x : r) stopId.putIfAbsent(x, stopId.size());
        if (!stopId.containsKey(s) || !stopId.containsKey(t)) return -1;
        int S = stopId.size(), N = S + routes.length;
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < N; i++) g.add(new ArrayList<>());
        for (int i = 0; i < routes.length; i++) for (int x : routes[i]) { g.get(stopId.get(x)).add(S + i); g.get(S + i).add(stopId.get(x)); }
        int[] dist = new int[N];
        Arrays.fill(dist, Integer.MAX_VALUE);
        int src = stopId.get(s), dst = stopId.get(t);
        dist[src] = 0;
        Deque<Integer> dq = new ArrayDeque<>(List.of(src));
        while (!dq.isEmpty()) {
            int u = dq.pollFirst();
            for (int v : g.get(u)) {
                int w = v >= S ? 1 : 0;                       // boarding a route costs 1, getting off is free
                if (dist[u] + w < dist[v]) { dist[v] = dist[u] + w; if (w == 0) dq.offerFirst(v); else dq.offerLast(v); }
            }
        }
        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
    }

    public static void main(String[] args) {
        check(numBusesToDestination(new int[][]{{1, 2, 7}, {3, 6, 7}}, 1, 6), 2);
        check(numBusesToDestination(new int[][]{{7, 12}, {4, 5, 15}, {6}, {15, 19}, {9, 12, 13}}, 15, 12), -1);
        check(numBusesToDestination(new int[][]{{1, 7}, {3, 5}}, 5, 5), 0);
        Random r = new Random(51);
        for (int t = 0; t < 2000; t++) {
            int[][] routes = new int[r.nextInt(5) + 1][];
            for (int i = 0; i < routes.length; i++) {
                Set<Integer> stops = new TreeSet<>();
                for (int k = r.nextInt(4) + 1; k > 0; k--) stops.add(r.nextInt(10));
                routes[i] = stops.stream().mapToInt(Integer::intValue).toArray();
            }
            int s = r.nextInt(10), d = r.nextInt(10);
            int e = stopGraph(routes, s, d);
            check(numBusesToDestination(routes, s, d), e); check(bipartite01(routes, s, d), e);
        }
        System.out.println("BusRoutes: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
