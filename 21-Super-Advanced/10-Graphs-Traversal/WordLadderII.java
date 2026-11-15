import java.util.*;

/**
 * LC 126 — Word Ladder II. Explanation: WordLadderII.md
 * 1. DFS over all simple paths, keep shortest (oracle)      exponential
 * 2. BFS storing full paths                                  heavy memory
 * 3. Layered BFS building parent links + backtracking        O(N L 26 + output)
 */
public class WordLadderII {

    static boolean adjacent(String a, String b) {
        int d = 0;
        for (int i = 0; i < a.length(); i++) if (a.charAt(i) != b.charAt(i) && ++d > 1) return false;
        return d == 1;
    }

    static List<List<String>> dfsAll(String begin, String end, List<String> words) {
        List<List<String>> res = new ArrayList<>();
        int[] best = {Integer.MAX_VALUE};
        Set<String> dict = new HashSet<>(words);
        if (!dict.contains(end)) return res;
        LinkedList<String> path = new LinkedList<>(List.of(begin));
        dfs(begin, end, dict, new HashSet<>(List.of(begin)), path, res, best);
        res.removeIf(p -> p.size() > best[0]);
        return sorted(res);
    }

    static void dfs(String cur, String end, Set<String> dict, Set<String> used, LinkedList<String> path, List<List<String>> res, int[] best) {
        if (path.size() > best[0]) return;
        if (cur.equals(end)) { best[0] = Math.min(best[0], path.size()); res.add(new ArrayList<>(path)); return; }
        for (String w : dict) {
            if (used.contains(w) || !adjacent(cur, w)) continue;
            used.add(w); path.add(w);
            dfs(w, end, dict, used, path, res, best);
            path.removeLast(); used.remove(w);
        }
    }

    static List<List<String>> bfsPaths(String begin, String end, List<String> words) {
        Set<String> dict = new HashSet<>(words);
        List<List<String>> res = new ArrayList<>();
        if (!dict.contains(end)) return res;
        Queue<List<String>> q = new ArrayDeque<>();
        q.offer(List.of(begin));
        dict.remove(begin);
        while (!q.isEmpty() && res.isEmpty()) {
            Set<String> usedThisLevel = new HashSet<>();
            for (int sz = q.size(); sz > 0; sz--) {
                List<String> p = q.poll();
                String last = p.get(p.size() - 1);
                for (String nx : neighbours(last, dict)) {
                    List<String> np = new ArrayList<>(p); np.add(nx);
                    if (nx.equals(end)) res.add(np); else q.offer(np);
                    usedThisLevel.add(nx);
                }
            }
            dict.removeAll(usedThisLevel);
        }
        return sorted(res);
    }

    static List<String> neighbours(String w, Set<String> dict) {
        List<String> out = new ArrayList<>();
        char[] cs = w.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char orig = cs[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == orig) continue;
                cs[i] = c;
                String nw = new String(cs);
                if (dict.contains(nw)) out.add(nw);
            }
            cs[i] = orig;
        }
        return out;
    }

    static List<List<String>> findLadders(String begin, String end, List<String> words) {
        Set<String> dict = new HashSet<>(words);
        List<List<String>> res = new ArrayList<>();
        if (!dict.contains(end)) return res;
        Map<String, List<String>> parents = new HashMap<>();
        Set<String> layer = new HashSet<>(List.of(begin));
        dict.remove(begin);
        boolean found = false;
        while (!layer.isEmpty() && !found) {
            Set<String> next = new HashSet<>();
            for (String w : layer)
                for (String nx : neighbours(w, dict)) {
                    next.add(nx);
                    parents.computeIfAbsent(nx, k -> new ArrayList<>()).add(w);
                    if (nx.equals(end)) found = true;
                }
            dict.removeAll(next);              // only after the whole layer
            layer = next;
        }
        if (found) back(end, begin, parents, new LinkedList<>(List.of(end)), res);
        return sorted(res);
    }

    static void back(String w, String begin, Map<String, List<String>> parents, LinkedList<String> path, List<List<String>> res) {
        if (w.equals(begin)) { res.add(new ArrayList<>(path)); return; }
        for (String p : parents.get(w)) { path.addFirst(p); back(p, begin, parents, path, res); path.removeFirst(); }
    }

    static List<List<String>> sorted(List<List<String>> r) { r.sort(Comparator.comparing(Object::toString)); return r; }

    public static void main(String[] args) {
        List<String> words = List.of("hot", "dot", "dog", "lot", "log", "cog");
        String e = "[[hit, hot, dot, dog, cog], [hit, hot, lot, log, cog]]";
        check(findLadders("hit", "cog", words).toString(), e); check(bfsPaths("hit", "cog", words).toString(), e);
        check(dfsAll("hit", "cog", words).toString(), e);
        check(findLadders("hit", "cog", List.of("hot", "dot", "dog", "lot", "log")).size(), 0);
        Random r = new Random(43);
        for (int t = 0; t < 300; t++) {
            Set<String> ws = new TreeSet<>();
            for (int i = r.nextInt(10) + 2; i > 0; i--) ws.add("" + (char) ('a' + r.nextInt(3)) + (char) ('a' + r.nextInt(3)) + (char) ('a' + r.nextInt(3)));
            List<String> list = new ArrayList<>(ws);
            String begin = list.get(0), end = list.get(list.size() - 1);
            if (begin.equals(end)) continue;
            List<String> dict = list.subList(1, list.size());
            String x = dfsAll(begin, end, dict).toString();
            check(findLadders(begin, end, dict).toString(), x); check(bfsPaths(begin, end, dict).toString(), x);
        }
        System.out.println("WordLadderII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
