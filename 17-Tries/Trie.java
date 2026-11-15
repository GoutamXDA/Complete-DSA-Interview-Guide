/**
 * LC 208 Implement Trie + "Trie II" (countWordsEqualTo, countWordsStartingWith, erase).
 * All operations O(L).
 */
public class Trie {

    static class Node {
        Node[] next = new Node[26];
        int endCount;     // words ending here
        int prefixCount;  // words passing through here
    }

    private final Node root = new Node();

    public void insert(String w) {
        Node cur = root;
        for (char c : w.toCharArray()) {
            int i = c - 'a';
            if (cur.next[i] == null) cur.next[i] = new Node();
            cur = cur.next[i];
            cur.prefixCount++;
        }
        cur.endCount++;
    }

    private Node walk(String s) {
        Node cur = root;
        for (char c : s.toCharArray()) {
            cur = cur.next[c - 'a'];
            if (cur == null) return null;
        }
        return cur;
    }

    public boolean search(String w) { Node n = walk(w); return n != null && n.endCount > 0; }
    public boolean startsWith(String p) { Node n = walk(p); return n != null && n.prefixCount > 0; }
    public int countWordsEqualTo(String w) { Node n = walk(w); return n == null ? 0 : n.endCount; }
    public int countWordsStartingWith(String p) { Node n = walk(p); return n == null ? 0 : n.prefixCount; }

    public void erase(String w) {
        if (countWordsEqualTo(w) == 0) return;
        Node cur = root;
        for (char c : w.toCharArray()) { cur = cur.next[c - 'a']; cur.prefixCount--; }
        cur.endCount--;
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("apple");
        check(t.search("apple"), true); check(t.search("app"), false); check(t.startsWith("app"), true);
        t.insert("app"); check(t.search("app"), true);
        t.insert("apple");
        check(t.countWordsEqualTo("apple"), 2); check(t.countWordsStartingWith("app"), 3);
        t.erase("apple"); check(t.countWordsEqualTo("apple"), 1); check(t.countWordsStartingWith("appl"), 1);
        t.erase("apple"); check(t.startsWith("appl"), false); check(t.search("app"), true);
        System.out.println("Trie: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
