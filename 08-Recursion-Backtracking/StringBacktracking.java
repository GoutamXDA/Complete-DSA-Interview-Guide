import java.util.*;

/**
 * LC 22 Generate parentheses: brute = all 2^(2n) strings + validate | backtrack with open/close counts. O(Catalan(n))
 * LC 17 Letter combinations: product of choices per digit. O(4^n * n)
 * LC 131 Palindrome partitioning: try every palindromic prefix, recurse on rest. O(n * 2^n)
 *   Optimisation: precompute isPal[i][j] with DP so each check is O(1).
 * LC 79 Word search: DFS from each cell, mark visited in place ('#'), restore. O(m*n*4^L)
 */
public class StringBacktracking {

    static List<String> generateParenthesisBrute(int n) {
        List<String> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << 2 * n); mask++) {
            StringBuilder sb = new StringBuilder(); int bal = 0; boolean ok = true;
            for (int b = 2 * n - 1; b >= 0; b--) {
                boolean open = (mask >> b & 1) == 1;
                sb.append(open ? '(' : ')'); bal += open ? 1 : -1;
                if (bal < 0) { ok = false; break; }
            }
            if (ok && bal == 0) res.add(sb.toString());
        }
        return res;
    }

    static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        gen(n, 0, 0, new StringBuilder(), res);
        return res;
    }

    static void gen(int n, int open, int close, StringBuilder sb, List<String> res) {
        if (sb.length() == 2 * n) { res.add(sb.toString()); return; }
        if (open < n) { sb.append('('); gen(n, open + 1, close, sb, res); sb.deleteCharAt(sb.length() - 1); }
        if (close < open) { sb.append(')'); gen(n, open, close + 1, sb, res); sb.deleteCharAt(sb.length() - 1); }
    }

    static final String[] PHONE = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    static List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;
        letters(digits, 0, new StringBuilder(), res);
        return res;
    }

    static void letters(String d, int i, StringBuilder sb, List<String> res) {
        if (i == d.length()) { res.add(sb.toString()); return; }
        for (char c : PHONE[d.charAt(i) - '0'].toCharArray()) {
            sb.append(c); letters(d, i + 1, sb, res); sb.deleteCharAt(sb.length() - 1);
        }
    }

    static List<List<String>> partition(String s) {
        int n = s.length();
        boolean[][] pal = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--)
            for (int j = i; j < n; j++) pal[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 2 || pal[i + 1][j - 1]);
        List<List<String>> res = new ArrayList<>();
        part(s, 0, pal, new ArrayList<>(), res);
        return res;
    }

    static void part(String s, int start, boolean[][] pal, List<String> path, List<List<String>> res) {
        if (start == s.length()) { res.add(new ArrayList<>(path)); return; }
        for (int end = start; end < s.length(); end++) {
            if (!pal[start][end]) continue;
            path.add(s.substring(start, end + 1)); part(s, end + 1, pal, path, res); path.remove(path.size() - 1);
        }
    }

    static boolean exist(char[][] board, String word) {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                if (dfs(board, word, 0, r, c)) return true;
        return false;
    }

    static boolean dfs(char[][] b, String w, int k, int r, int c) {
        if (k == w.length()) return true;
        if (r < 0 || c < 0 || r >= b.length || c >= b[0].length || b[r][c] != w.charAt(k)) return false;
        char saved = b[r][c]; b[r][c] = '#';
        boolean found = dfs(b, w, k + 1, r + 1, c) || dfs(b, w, k + 1, r - 1, c)
                || dfs(b, w, k + 1, r, c + 1) || dfs(b, w, k + 1, r, c - 1);
        b[r][c] = saved;
        return found;
    }

    public static void main(String[] args) {
        check(generateParenthesis(3), List.of("((()))", "(()())", "(())()", "()(())", "()()()"));
        List<String> brute = generateParenthesisBrute(3); Collections.sort(brute);
        check(brute, generateParenthesis(3));
        check(letterCombinations("23"), List.of("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"));
        check(partition("aab").toString(), "[[a, a, b], [aa, b]]");
        char[][] board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        check(exist(board, "ABCCED"), true); check(exist(board, "SEE"), true); check(exist(board, "ABCB"), false);
        System.out.println("StringBacktracking: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
