import java.util.*;

/**
 * LC 648  Replace words with their shortest dictionary root:
 *         brute check every prefix against a HashSet O(sum L^2) | trie walk stops at the first end node O(sum L)
 * LC 1268 Search suggestions system (top 3 lexicographic per typed prefix):
 *         sort + binary search for each prefix O(n log n + L log n) | trie storing up to 3 sorted words per node
 * LC 336  Palindrome pairs: brute all pairs O(n^2 L)
 *         | trie of REVERSED words; each node keeps indices of words whose remaining (unmatched) part is a palindrome.
 *           For word i walk its characters: if a reversed word ends here and the rest of word i is a palindrome -> pair;
 *           at the end of word i, every index in node.palBelow is a pair. O(n L^2)
 */
public class TrieProblemsII {

    static String replaceWordsBrute(List<String> dict, String sentence) {
        Set<String> roots = new HashSet<>(dict);
        StringBuilder sb = new StringBuilder();
        for (String w : sentence.split(" ")) {
            String rep = w;
            for (int i = 1; i <= w.length(); i++) if (roots.contains(w.substring(0, i))) { rep = w.substring(0, i); break; }
            if (sb.length() > 0) sb.append(' ');
            sb.append(rep);
        }
        return sb.toString();
    }

    static class Node { Node[] next = new Node[26]; String word; List<String> top = new ArrayList<>(); int idx = -1; List<Integer> palBelow = new ArrayList<>(); }

    static String replaceWords(List<String> dict, String sentence) {
        Node root = new Node();
        for (String d : dict) {
            Node c = root;
            for (char ch : d.toCharArray()) { if (c.next[ch - 'a'] == null) c.next[ch - 'a'] = new Node(); c = c.next[ch - 'a']; }
            c.word = d;
        }
        StringBuilder sb = new StringBuilder();
        for (String w : sentence.split(" ")) {
            Node c = root;
            String rep = w;
            for (char ch : w.toCharArray()) {
                c = c.next[ch - 'a'];
                if (c == null) break;
                if (c.word != null) { rep = c.word; break; }
            }
            if (sb.length() > 0) sb.append(' ');
            sb.append(rep);
        }
        return sb.toString();
    }

    static List<List<String>> suggestedProductsSort(String[] products, String search) {
        String[] p = products.clone(); Arrays.sort(p);
        List<List<String>> res = new ArrayList<>();
        String prefix = "";
        for (char ch : search.toCharArray()) {
            prefix += ch;
            int lo = 0, hi = p.length;                   // first index with p[i] >= prefix
            while (lo < hi) { int mid = (lo + hi) / 2; if (p[mid].compareTo(prefix) < 0) lo = mid + 1; else hi = mid; }
            List<String> l = new ArrayList<>();
            for (int i = lo; i < Math.min(p.length, lo + 3); i++) if (p[i].startsWith(prefix)) l.add(p[i]);
            res.add(l);
        }
        return res;
    }

    static List<List<String>> suggestedProducts(String[] products, String search) {
        String[] p = products.clone(); Arrays.sort(p);    // inserting in sorted order keeps each node's top-3 sorted
        Node root = new Node();
        for (String w : p) {
            Node c = root;
            for (char ch : w.toCharArray()) {
                if (c.next[ch - 'a'] == null) c.next[ch - 'a'] = new Node();
                c = c.next[ch - 'a'];
                if (c.top.size() < 3) c.top.add(w);
            }
        }
        List<List<String>> res = new ArrayList<>();
        Node c = root;
        for (char ch : search.toCharArray()) {
            c = c == null ? null : c.next[ch - 'a'];
            res.add(c == null ? new ArrayList<>() : c.top);
        }
        return res;
    }

    static List<List<Integer>> palindromePairsBrute(String[] w) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < w.length; i++) for (int j = 0; j < w.length; j++) if (i != j && isPal(w[i] + w[j], 0, w[i].length() + w[j].length() - 1)) res.add(List.of(i, j));
        return res;
    }

    static List<List<Integer>> palindromePairs(String[] w) {
        Node root = new Node();
        for (int i = 0; i < w.length; i++) {                // insert reversed words
            Node c = root;
            String s = w[i];
            for (int k = s.length() - 1; k >= 0; k--) {
                if (isPal(s, 0, k)) c.palBelow.add(i);       // s[0..k] still unmatched and is a palindrome
                int ch = s.charAt(k) - 'a';
                if (c.next[ch] == null) c.next[ch] = new Node();
                c = c.next[ch];
            }
            c.palBelow.add(i);                               // empty remainder is a palindrome
            c.idx = i;
        }
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < w.length; i++) {
            Node c = root;
            String s = w[i];
            for (int k = 0; k < s.length() && c != null; k++) {
                if (c.idx >= 0 && c.idx != i && isPal(s, k, s.length() - 1)) res.add(List.of(i, c.idx)); // shorter word j ends here
                c = c.next[s.charAt(k) - 'a'];
            }
            if (c == null) continue;
            for (int j : c.palBelow) if (j != i) res.add(List.of(i, j));                                  // word j at least as long
        }
        return res;
    }

    static boolean isPal(String s, int l, int r) { while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false; return true; }

    static String norm(List<List<Integer>> r) { List<String> s = new ArrayList<>(); for (List<Integer> p : r) s.add(p.toString()); Collections.sort(s); return s.toString(); }

    public static void main(String[] args) {
        List<String> d = List.of("cat", "bat", "rat");
        String s = "the cattle was rattled by the battery";
        check(replaceWords(d, s), "the cat was rat by the bat"); check(replaceWordsBrute(d, s), "the cat was rat by the bat");
        check(replaceWords(List.of("a", "b", "c"), "aadsfasf absbs bbab cadsfafs"), "a a b c");
        String[] prods = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        String exp = "[[mobile, moneypot, monitor], [mobile, moneypot, monitor], [mouse, mousepad], [mouse, mousepad], [mouse, mousepad]]";
        check(suggestedProducts(prods, "mouse").toString(), exp); check(suggestedProductsSort(prods, "mouse").toString(), exp);
        check(suggestedProducts(new String[]{"havana"}, "tatiana").toString(), "[[], [], [], [], [], [], []]");
        String[][] tests = {{"abcd", "dcba", "lls", "s", "sssll"}, {"bat", "tab", "cat"}, {"a", ""}, {"a", "abc", "aba", ""}, {"ab", "ba", "abc", "cba", "b"}};
        for (String[] t : tests) check(norm(palindromePairs(t)), norm(palindromePairsBrute(t)));
        check(norm(palindromePairs(tests[0])), "[[0, 1], [1, 0], [2, 4], [3, 2]]");
        System.out.println("TrieProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
