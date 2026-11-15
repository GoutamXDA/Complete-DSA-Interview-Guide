import java.util.*;

/**
 * LC 269 — Alien Dictionary. Explanation: AlienDictionary.md
 * 1. Brute force over permutations of letters (oracle)
 * 2. Kahn's BFS topological sort
 * 3. DFS topological sort with three colours
 * A result is checked by verifying the word list is sorted under it (any valid order is accepted).
 */
public class AlienDictionary {

    static boolean sortedUnder(String[] words, String order) {
        int[] rank = new int[26];
        for (int i = 0; i < order.length(); i++) rank[order.charAt(i) - 'a'] = i;
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            int j = 0;
            while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
            if (j == a.length() || j == b.length()) { if (a.length() > b.length()) return false; continue; }
            if (rank[a.charAt(j) - 'a'] > rank[b.charAt(j) - 'a']) return false;
        }
        return true;
    }

    static Set<Character> letters(String[] words) {
        Set<Character> s = new TreeSet<>();
        for (String w : words) for (char c : w.toCharArray()) s.add(c);
        return s;
    }

    static boolean bruteExists(String[] words) {
        List<Character> ls = new ArrayList<>(letters(words));
        return permute(ls, 0, words);
    }

    static boolean permute(List<Character> ls, int k, String[] words) {
        if (k == ls.size()) { StringBuilder sb = new StringBuilder(); for (char c : ls) sb.append(c); return sortedUnder(words, sb.toString()); }
        for (int i = k; i < ls.size(); i++) {
            Collections.swap(ls, k, i);
            if (permute(ls, k + 1, words)) return true;
            Collections.swap(ls, k, i);
        }
        return false;
    }

    /** returns adjacency, or null if a prefix violation makes the input invalid */
    static Map<Character, Set<Character>> graph(String[] words) {
        Map<Character, Set<Character>> g = new TreeMap<>();
        for (char c : letters(words)) g.put(c, new TreeSet<>());
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            int j = 0;
            while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
            if (j < a.length() && j < b.length()) g.get(a.charAt(j)).add(b.charAt(j));
            else if (a.length() > b.length()) return null;
        }
        return g;
    }

    static String kahn(String[] words) {
        Map<Character, Set<Character>> g = graph(words);
        if (g == null) return "";
        Map<Character, Integer> indeg = new TreeMap<>();
        for (char c : g.keySet()) indeg.putIfAbsent(c, 0);
        for (Set<Character> s : g.values()) for (char c : s) indeg.merge(c, 1, Integer::sum);
        Deque<Character> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue() == 0) q.offer(e.getKey());
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char nx : g.get(c)) if (indeg.merge(nx, -1, Integer::sum) == 0) q.offer(nx);
        }
        return sb.length() == g.size() ? sb.toString() : "";
    }

    static String dfsTopo(String[] words) {
        Map<Character, Set<Character>> g = graph(words);
        if (g == null) return "";
        Map<Character, Integer> color = new HashMap<>();
        StringBuilder post = new StringBuilder();
        for (char c : g.keySet()) if (!color.containsKey(c) && !visit(c, g, color, post)) return "";
        return post.reverse().toString();
    }

    static boolean visit(char c, Map<Character, Set<Character>> g, Map<Character, Integer> color, StringBuilder post) {
        color.put(c, 1);                                        // grey
        for (char nx : g.get(c)) {
            Integer col = color.get(nx);
            if (col != null && col == 1) return false;          // back edge -> cycle
            if (col == null && !visit(nx, g, color, post)) return false;
        }
        color.put(c, 2);
        post.append(c);
        return true;
    }

    public static void main(String[] args) {
        String[] w = {"wrt", "wrf", "er", "ett", "rftt"};
        check(kahn(w), "wertf"); check(sortedUnder(w, dfsTopo(w)), true);
        check(kahn(new String[]{"z", "x", "z"}), ""); check(dfsTopo(new String[]{"z", "x", "z"}), "");
        check(kahn(new String[]{"abc", "ab"}), ""); check(kahn(new String[]{"z", "z"}), "z");
        Random r = new Random(44);
        for (int t = 0; t < 1500; t++) {
            String[] words = new String[r.nextInt(5) + 1];
            for (int i = 0; i < words.length; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = r.nextInt(3) + 1; j > 0; j--) sb.append((char) ('a' + r.nextInt(4)));
                words[i] = sb.toString();
            }
            boolean exists = bruteExists(words);
            String k = kahn(words), d = dfsTopo(words);
            check(!k.isEmpty(), exists); check(!d.isEmpty(), exists);
            if (exists) {
                check(sortedUnder(words, k), true); check(sortedUnder(words, d), true);
                check(k.length(), letters(words).size()); check(d.length(), letters(words).size());
            }
        }
        System.out.println("AlienDictionary: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
