import java.util.*;

/**
 * LC 332 — Reconstruct Itinerary. Explanation: ReconstructItinerary.md
 * 1. Backtracking with sorted destinations       exponential worst
 * 2. Hierholzer (recursive) with min-heaps       O(E log E)
 * 3. Hierholzer (iterative)                      O(E log E)
 */
public class ReconstructItinerary {

    static List<String> backtracking(List<List<String>> tickets) {
        Map<String, List<String>> g = new HashMap<>();
        for (List<String> t : tickets) g.computeIfAbsent(t.get(0), k -> new ArrayList<>()).add(t.get(1));
        for (List<String> l : g.values()) Collections.sort(l);
        Map<String, boolean[]> used = new HashMap<>();
        for (var e : g.entrySet()) used.put(e.getKey(), new boolean[e.getValue().size()]);
        LinkedList<String> path = new LinkedList<>(List.of("JFK"));
        bt("JFK", tickets.size(), g, used, path);
        return path;
    }

    static boolean bt(String a, int left, Map<String, List<String>> g, Map<String, boolean[]> used, LinkedList<String> path) {
        if (left == 0) return true;
        List<String> d = g.get(a);
        if (d == null) return false;
        boolean[] u = used.get(a);
        for (int i = 0; i < d.size(); i++) {
            if (u[i] || (i > 0 && d.get(i).equals(d.get(i - 1)) && !u[i - 1])) continue;   // skip duplicate tickets
            u[i] = true; path.add(d.get(i));
            if (bt(d.get(i), left - 1, g, used, path)) return true;
            u[i] = false; path.removeLast();
        }
        return false;
    }

    static Map<String, PriorityQueue<String>> heaps(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> g = new HashMap<>();
        for (List<String> t : tickets) g.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
        return g;
    }

    static List<String> findItinerary(List<List<String>> tickets) {
        LinkedList<String> route = new LinkedList<>();
        visit("JFK", heaps(tickets), route);
        return route;
    }

    static void visit(String a, Map<String, PriorityQueue<String>> g, LinkedList<String> route) {
        PriorityQueue<String> pq = g.get(a);
        while (pq != null && !pq.isEmpty()) visit(pq.poll(), g, route);
        route.addFirst(a);
    }

    static List<String> iterative(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> g = heaps(tickets);
        LinkedList<String> route = new LinkedList<>();
        Deque<String> st = new ArrayDeque<>(List.of("JFK"));
        while (!st.isEmpty()) {
            PriorityQueue<String> pq = g.get(st.peek());
            if (pq != null && !pq.isEmpty()) st.push(pq.poll());
            else route.addFirst(st.pop());
        }
        return route;
    }

    public static void main(String[] args) {
        List<List<String>> a = List.of(List.of("MUC", "LHR"), List.of("JFK", "MUC"), List.of("SFO", "SJC"), List.of("LHR", "SFO"));
        check(findItinerary(a), List.of("JFK", "MUC", "LHR", "SFO", "SJC"));
        List<List<String>> b = List.of(List.of("JFK", "SFO"), List.of("JFK", "ATL"), List.of("SFO", "ATL"), List.of("ATL", "JFK"), List.of("ATL", "SFO"));
        check(findItinerary(b), List.of("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"));
        List<List<String>> dead = List.of(List.of("JFK", "KUL"), List.of("JFK", "NRT"), List.of("NRT", "JFK"));
        check(findItinerary(dead), List.of("JFK", "NRT", "JFK", "KUL")); check(iterative(dead), List.of("JFK", "NRT", "JFK", "KUL"));
        String[] airports = {"JFK", "AAA", "BBB", "CCC"};
        Random r = new Random(46);
        for (int t = 0; t < 1500; t++) {
            List<List<String>> tickets = new ArrayList<>();      // build from a random walk so a valid itinerary exists
            String cur = "JFK";
            for (int k = r.nextInt(8) + 1; k > 0; k--) { String nx = airports[r.nextInt(4)]; if (nx.equals(cur)) continue; tickets.add(List.of(cur, nx)); cur = nx; }
            if (tickets.isEmpty()) continue;
            Collections.shuffle(tickets, r);
            List<String> e = backtracking(tickets);
            check(findItinerary(tickets), e); check(iterative(tickets), e);
        }
        System.out.println("ReconstructItinerary: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
