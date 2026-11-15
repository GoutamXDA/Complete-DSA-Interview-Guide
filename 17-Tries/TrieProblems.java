import java.util.*;

/**
 * LC 720 Longest word built one char at a time: brute = set + check every prefix O(sum L^2) | trie DFS through end-nodes only.
 * Distinct substrings: brute HashSet of all substrings O(n^3) | insert every suffix into trie, count nodes O(n^2).
 * LC 421 Max XOR pair: brute O(n^2) | binary trie, for each number go to the opposite bit when possible. O(32n)
 * LC 1707 Max XOR with element <= m: sort nums and queries by m; insert nums progressively. O((n+q) log + 32(n+q))
 * LC 212 Word search II: brute = LC 79 per word O(W * RC * 4^L) | one trie DFS over the grid; remove words once found.
 * LC 211 Add and search with '.': trie + DFS branching on '.'.
 */
public class TrieProblems {

    static class Node { Node[] next = new Node[26]; String word; boolean end; }

    static Node build(String[] words) {
        Node root = new Node();
        for (String w : words) {
            Node c = root;
            for (char ch : w.toCharArray()) { if (c.next[ch - 'a'] == null) c.next[ch - 'a'] = new Node(); c = c.next[ch - 'a']; }
            c.end = true; c.word = w;
        }
        return root;
    }

    static String longestWord(String[] words) {
        Node root = build(words);
        String[] best = {""};
        dfsLongest(root, best);
        return best[0];
    }

    static void dfsLongest(Node n, String[] best) {
        for (Node c : n.next) {
            if (c == null || !c.end) continue;
            if (c.word.length() > best[0].length() || (c.word.length() == best[0].length() && c.word.compareTo(best[0]) < 0)) best[0] = c.word;
            dfsLongest(c, best);
        }
    }

    static int distinctSubstringsBrute(String s) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) for (int j = i + 1; j <= s.length(); j++) set.add(s.substring(i, j));
        return set.size() + 1; // include empty string, as in the classic problem
    }

    static int distinctSubstrings(String s) {
        Node root = new Node();
        int nodes = 0;
        for (int i = 0; i < s.length(); i++) {
            Node c = root;
            for (int j = i; j < s.length(); j++) {
                int k = s.charAt(j) - 'a';
                if (c.next[k] == null) { c.next[k] = new Node(); nodes++; }
                c = c.next[k];
            }
        }
        return nodes + 1;
    }

    static class BitNode { BitNode[] child = new BitNode[2]; }

    static void insertBits(BitNode root, int x) {
        BitNode c = root;
        for (int b = 31; b >= 0; b--) {
            int bit = (x >>> b) & 1;
            if (c.child[bit] == null) c.child[bit] = new BitNode();
            c = c.child[bit];
        }
    }

    static int maxXorWith(BitNode root, int x) {
        BitNode c = root; int res = 0;
        for (int b = 31; b >= 0; b--) {
            int bit = (x >>> b) & 1;
            if (c.child[1 - bit] != null) { res |= 1 << b; c = c.child[1 - bit]; } else c = c.child[bit];
        }
        return res;
    }

    static int findMaximumXORBrute(int[] a) {
        int best = 0;
        for (int i = 0; i < a.length; i++) for (int j = i; j < a.length; j++) best = Math.max(best, a[i] ^ a[j]);
        return best;
    }

    static int findMaximumXOR(int[] a) {
        BitNode root = new BitNode();
        int best = 0;
        for (int x : a) { insertBits(root, x); best = Math.max(best, maxXorWith(root, x)); }
        return best;
    }

    static int[] maximizeXor(int[] nums, int[][] queries) {
        int[] a = nums.clone(); Arrays.sort(a);
        Integer[] order = new Integer[queries.length];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> queries[i][1]));
        int[] ans = new int[queries.length];
        BitNode root = new BitNode();
        int p = 0;
        for (int qi : order) {
            while (p < a.length && a[p] <= queries[qi][1]) insertBits(root, a[p++]);
            ans[qi] = p == 0 ? -1 : maxXorWith(root, queries[qi][0]);
        }
        return ans;
    }

    static List<String> findWords(char[][] board, String[] words) {
        Node root = build(words);
        List<String> res = new ArrayList<>();
        for (int r = 0; r < board.length; r++) for (int c = 0; c < board[0].length; c++) dfsBoard(board, r, c, root, res);
        Collections.sort(res);
        return res;
    }

    static void dfsBoard(char[][] b, int r, int c, Node parent, List<String> res) {
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] == '#') return;
        Node n = parent.next[b[r][c] - 'a'];
        if (n == null) return;
        if (n.word != null) { res.add(n.word); n.word = null; }   // avoid duplicates
        char ch = b[r][c]; b[r][c] = '#';
        dfsBoard(b, r + 1, c, n, res); dfsBoard(b, r - 1, c, n, res);
        dfsBoard(b, r, c + 1, n, res); dfsBoard(b, r, c - 1, n, res);
        b[r][c] = ch;
    }

    static class WordDictionary {
        final Node root = new Node();
        void addWord(String w) {
            Node c = root;
            for (char ch : w.toCharArray()) { if (c.next[ch - 'a'] == null) c.next[ch - 'a'] = new Node(); c = c.next[ch - 'a']; }
            c.end = true;
        }
        boolean search(String w) { return search(w, 0, root); }
        boolean search(String w, int i, Node n) {
            if (n == null) return false;
            if (i == w.length()) return n.end;
            char ch = w.charAt(i);
            if (ch != '.') return search(w, i + 1, n.next[ch - 'a']);
            for (Node c : n.next) if (search(w, i + 1, c)) return true;
            return false;
        }
    }

    public static void main(String[] args) {
        check(longestWord(new String[]{"w", "wo", "wor", "worl", "world"}), "world");
        check(longestWord(new String[]{"a", "banana", "app", "appl", "ap", "apply", "apple"}), "apple");
        check(distinctSubstringsBrute("abab"), 8); check(distinctSubstrings("abab"), 8);
        int[] x = {3, 10, 5, 25, 2, 8};
        check(findMaximumXORBrute(x), 28); check(findMaximumXOR(x), 28);
        check(Arrays.toString(maximizeXor(new int[]{0, 1, 2, 3, 4}, new int[][]{{3, 1}, {1, 3}, {5, 6}})), "[3, 3, 7]");
        char[][] board = {"oaan".toCharArray(), "etae".toCharArray(), "ihkr".toCharArray(), "iflv".toCharArray()};
        check(findWords(board, new String[]{"oath", "pea", "eat", "rain"}), List.of("eat", "oath"));
        WordDictionary wd = new WordDictionary();
        wd.addWord("bad"); wd.addWord("dad"); wd.addWord("mad");
        check(wd.search("pad"), false); check(wd.search(".ad"), true); check(wd.search("b.."), true);
        System.out.println("TrieProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
