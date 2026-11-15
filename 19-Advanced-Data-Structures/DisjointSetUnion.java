import java.util.*;

/**
 * Disjoint Set Union with path compression + union by size: amortised O(α(n)) per operation.
 *
 * LC 684 Redundant connection: first edge whose endpoints are already connected.
 * LC 721 Accounts merge: union all emails of an account with its first email; group by root.
 * LC 947 Most stones removed: union row r with column (c + OFFSET); answer = stones - components.
 */
public class DisjointSetUnion {

    static class DSU {
        int[] parent, size; int sets;
        DSU(int n) { parent = new int[n]; size = new int[n]; sets = n; for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; } }

        int find(int x) {                             // iterative path compression (no stack overflow)
            int root = x;
            while (parent[root] != root) root = parent[root];
            while (parent[x] != root) { int nx = parent[x]; parent[x] = root; x = nx; }
            return root;
        }

        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra; size[ra] += size[rb]; sets--;
            return true;
        }

        boolean connected(int a, int b) { return find(a) == find(b); }
    }

    static int[] findRedundantConnection(int[][] edges) {
        DSU d = new DSU(edges.length + 1);
        for (int[] e : edges) if (!d.union(e[0], e[1])) return e;
        return new int[0];
    }

    static List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> id = new HashMap<>();
        Map<String, String> owner = new HashMap<>();
        for (List<String> acc : accounts)
            for (int i = 1; i < acc.size(); i++) { id.putIfAbsent(acc.get(i), id.size()); owner.put(acc.get(i), acc.get(0)); }
        DSU d = new DSU(id.size());
        for (List<String> acc : accounts)
            for (int i = 2; i < acc.size(); i++) d.union(id.get(acc.get(1)), id.get(acc.get(i)));
        Map<Integer, TreeSet<String>> groups = new TreeMap<>();
        for (String email : id.keySet()) groups.computeIfAbsent(d.find(id.get(email)), k -> new TreeSet<>()).add(email);
        List<List<String>> res = new ArrayList<>();
        for (TreeSet<String> g : groups.values()) {
            List<String> row = new ArrayList<>(); row.add(owner.get(g.first())); row.addAll(g); res.add(row);
        }
        res.sort(Comparator.comparing((List<String> l) -> l.get(0)).thenComparing(l -> l.get(1)));
        return res;
    }

    static int removeStones(int[][] stones) {
        final int OFFSET = 10001;
        DSU d = new DSU(20002);
        Set<Integer> used = new HashSet<>();
        for (int[] s : stones) { d.union(s[0], s[1] + OFFSET); used.add(s[0]); used.add(s[1] + OFFSET); }
        Set<Integer> roots = new HashSet<>();
        for (int u : used) roots.add(d.find(u));
        return stones.length - roots.size();
    }

    public static void main(String[] args) {
        DSU d = new DSU(5);
        d.union(0, 1); d.union(3, 4);
        check(d.connected(0, 1), true); check(d.connected(1, 3), false); check(d.sets, 3);
        check(Arrays.toString(findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}})), "[2, 3]");
        List<List<String>> acc = List.of(
                List.of("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                List.of("John", "johnsmith@mail.com", "john00@mail.com"),
                List.of("Mary", "mary@mail.com"),
                List.of("John", "johnnybravo@mail.com"));
        check(accountsMerge(acc).toString(),
                "[[John, john00@mail.com, john_newyork@mail.com, johnsmith@mail.com], [John, johnnybravo@mail.com], [Mary, mary@mail.com]]");
        check(removeStones(new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 2}}), 5);
        System.out.println("DisjointSetUnion: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
