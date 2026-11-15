import java.util.*;

/**
 * LC 72 — Edit Distance. Explanation: EditDistance.md
 * 1. Plain recursion                 O(3^(m+n))
 * 2. Memoisation                     O(mn)
 * 3. Tabulation + reconstruction     O(mn) time and space
 * 4. One-row DP                      O(mn) time, O(n) space
 */
public class EditDistance {

    static int recursion(String a, String b, int i, int j) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (a.charAt(i - 1) == b.charAt(j - 1)) return recursion(a, b, i - 1, j - 1);
        return 1 + Math.min(recursion(a, b, i - 1, j - 1), Math.min(recursion(a, b, i - 1, j), recursion(a, b, i, j - 1)));
    }

    static int memo(String a, String b) { return memo(a, b, a.length(), b.length(), new Integer[a.length() + 1][b.length() + 1]); }

    static int memo(String a, String b, int i, int j, Integer[][] m) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (m[i][j] != null) return m[i][j];
        if (a.charAt(i - 1) == b.charAt(j - 1)) return m[i][j] = memo(a, b, i - 1, j - 1, m);
        return m[i][j] = 1 + Math.min(memo(a, b, i - 1, j - 1, m), Math.min(memo(a, b, i - 1, j, m), memo(a, b, i, j - 1, m)));
    }

    static int[][] table(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
        return dp;
    }

    /** Walk back to list the operations, then apply them to `a` to prove they produce `b`. */
    static List<String> operations(String a, String b) {
        int[][] dp = table(a, b);
        LinkedList<String> ops = new LinkedList<>();
        int i = a.length(), j = b.length();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && a.charAt(i - 1) == b.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) { i--; j--; }
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) { ops.addFirst("R" + (i - 1) + b.charAt(j - 1)); i--; j--; }
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) { ops.addFirst("D" + (i - 1)); i--; }
            else { ops.addFirst("I" + i + b.charAt(j - 1)); j--; }       // insert b[j-1] after a's first i chars
        }
        return ops;
    }

    static String apply(String a, List<String> ops) {
        // ops refer to original indices of a; apply from the back so earlier indices stay valid
        StringBuilder sb = new StringBuilder(a);
        List<String> rev = new ArrayList<>(ops);
        Collections.reverse(rev);
        for (String op : rev) {
            char kind = op.charAt(0);
            int idx = Integer.parseInt(op.substring(1, kind == 'D' ? op.length() : op.length() - 1));
            if (kind == 'R') sb.setCharAt(idx, op.charAt(op.length() - 1));
            else if (kind == 'D') sb.deleteCharAt(idx);
            else sb.insert(idx, op.charAt(op.length() - 1));
        }
        return sb.toString();
    }

    static int oneRow(String a, String b) {
        if (b.length() > a.length()) { String t = a; a = b; b = t; }
        int n = b.length();
        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++) dp[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            int diag = dp[0];
            dp[0] = i;
            for (int j = 1; j <= n; j++) {
                int up = dp[j];
                dp[j] = a.charAt(i - 1) == b.charAt(j - 1) ? diag : 1 + Math.min(diag, Math.min(up, dp[j - 1]));
                diag = up;
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        check(oneRow("horse", "ros"), 3); check(oneRow("intention", "execution"), 5); check(table("horse", "ros")[5][3], 3);
        Random r = new Random(53);
        for (int t = 0; t < 2000; t++) {
            String a = rand(r, r.nextInt(7)), b = rand(r, r.nextInt(7));
            int e = recursion(a, b, a.length(), b.length());
            check(memo(a, b), e); check(table(a, b)[a.length()][b.length()], e); check(oneRow(a, b), e);
            List<String> ops = operations(a, b);
            check(ops.size(), e); check(apply(a, ops), b);
        }
        System.out.println("EditDistance: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(3))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
