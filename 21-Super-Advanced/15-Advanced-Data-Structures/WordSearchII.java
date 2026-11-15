import java.util.*;

/**
 * LC 212 — Word Search II. Explanation: WordSearchII.md
 * 1. Word Search (LC 79) once per word
 * 2. Trie + DFS
 * 3. Trie + DFS with found-word removal and dead-branch pruning
 */
public class WordSearchII {

    static final int[][] D = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static List<String> perWord(char[][] b, String[] words) {
        Set<String> res = new TreeSet<>();
        for (String w : words)
            outer:
            for (int r = 0; r < b.length; r++) for (int c = 0; c < b[0].length; c++) if (exist(b, w, 0, r, c)) { res.add(w); break outer; }
        return new ArrayList<>(res);
    }

    static boolean exist(char[][] b, String w, int k, int r, int c) {
        if (k == w.length()) return true;
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] != w.charAt(k)) return false;
        char ch = b[r][c]; b[r][c] = '#';
        boolean ok = false;
        for (int[] d : D) if (!ok) ok = exist(b, w, k + 1, r + d[0], c + d[1]);
        b[r][c] = ch;
        return ok;
    }

    static class Node { Node[] next = new Node[26]; String word; int children; }

    static Node build(String[] words) {
        Node root = new Node();
        for (String w : words) {
            Node c = root;
            for (char ch : w.toCharArray()) {
                if (c.next[ch - 'a'] == null) { c.next[ch - 'a'] = new Node(); c.children++; }
                c = c.next[ch - 'a'];
            }
            c.word = w;
        }
        return root;
    }

    static List<String> trieBasic(char[][] board, String[] words) {
        Node root = build(words);
        Set<String> res = new TreeSet<>();
        for (int r = 0; r < board.length; r++) for (int c = 0; c < board[0].length; c++) basic(board, r, c, root, res);
        return new ArrayList<>(res);
    }

    static void basic(char[][] b, int r, int c, Node parent, Set<String> res) {
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] == '#') return;
        Node n = parent.next[b[r][c] - 'a'];
        if (n == null) return;
        if (n.word != null) res.add(n.word);
        char ch = b[r][c]; b[r][c] = '#';
        for (int[] d : D) basic(b, r + d[0], c + d[1], n, res);
        b[r][c] = ch;
    }

    static List<String> findWords(char[][] board, String[] words) {
        Node root = build(words);
        List<String> res = new ArrayList<>();
        for (int r = 0; r < board.length; r++) for (int c = 0; c < board[0].length; c++) pruned(board, r, c, root, res);
        Collections.sort(res);
        return res;
    }

    static void pruned(char[][] b, int r, int c, Node parent, List<String> res) {
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] == '#') return;
        int idx = b[r][c] - 'a';
        Node n = parent.next[idx];
        if (n == null) return;
        if (n.word != null) { res.add(n.word); n.word = null; }        // record once
        char ch = b[r][c]; b[r][c] = '#';
        for (int[] d : D) pruned(b, r + d[0], c + d[1], n, res);
        b[r][c] = ch;
        if (n.word == null && n.children == 0) { parent.next[idx] = null; parent.children--; }   // dead branch
    }

    public static void main(String[] args) {
        char[][] board = {"oaan".toCharArray(), "etae".toCharArray(), "ihkr".toCharArray(), "iflv".toCharArray()};
        check(findWords(board, new String[]{"oath", "pea", "eat", "rain"}), List.of("eat", "oath"));
        check(findWords(new char[][]{"ab".toCharArray()}, new String[]{"ab", "abc", "ba", "b"}), List.of("ab", "b", "ba"));
        Random r = new Random(68);
        for (int t = 0; t < 800; t++) {
            int R = r.nextInt(4) + 1, C = r.nextInt(4) + 1;
            char[][] b = new char[R][C];
            for (char[] row : b) for (int c = 0; c < C; c++) row[c] = (char) ('a' + r.nextInt(3));
            Set<String> ws = new TreeSet<>();
            for (int k = r.nextInt(8) + 1; k > 0; k--) {
                StringBuilder sb = new StringBuilder();
                for (int i = r.nextInt(5) + 1; i > 0; i--) sb.append((char) ('a' + r.nextInt(3)));
                ws.add(sb.toString());
            }
            String[] words = ws.toArray(new String[0]);
            List<String> e = perWord(b, words);
            check(trieBasic(b, words), e); check(findWords(b, words), e);
        }
        System.out.println("WordSearchII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
