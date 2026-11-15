import java.util.*;

/**
 * LC 664 — Strange Printer. Explanation: StrangePrinter.md
 * 1. BFS over paper states (oracle, tiny strings)
 * 2. Memoised interval DP (merge s[i] into a later equal char)     O(n^3)
 * 3. Bottom-up, alternative formulation                            O(n^3)
 */
public class StrangePrinter {

    static int bfs(String target) {
        int n = target.length();
        Set<Character> alphabet = new TreeSet<>();
        for (char c : target.toCharArray()) alphabet.add(c);
        String start = ".".repeat(n);
        Map<String, Integer> dist = new HashMap<>(Map.of(start, 0));
        Deque<String> q = new ArrayDeque<>(List.of(start));
        while (!q.isEmpty()) {
            String cur = q.poll();
            if (cur.equals(target)) return dist.get(cur);
            for (char c : alphabet)
                for (int i = 0; i < n; i++)
                    for (int j = i; j < n; j++) {
                        String nx = cur.substring(0, i) + String.valueOf(c).repeat(j - i + 1) + cur.substring(j + 1);
                        if (!dist.containsKey(nx)) { dist.put(nx, dist.get(cur) + 1); q.offer(nx); }
                    }
        }
        return -1;
    }

    static String collapse(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) if (sb.length() == 0 || sb.charAt(sb.length() - 1) != c) sb.append(c);
        return sb.toString();
    }

    static int strangePrinter(String str) {
        String s = collapse(str);
        int n = s.length();
        if (n == 0) return 0;
        return go(s, 0, n - 1, new Integer[n][n]);
    }

    static int go(String s, int i, int j, Integer[][] m) {
        if (i > j) return 0;
        if (m[i][j] != null) return m[i][j];
        int best = 1 + go(s, i + 1, j, m);                                   // s[i] printed alone
        for (int k = i + 1; k <= j; k++)
            if (s.charAt(k) == s.charAt(i)) best = Math.min(best, go(s, i + 1, k - 1, m) + go(s, k, j, m));
        return m[i][j] = best;
    }

    static int bottomUpAlt(String str) {
        String s = collapse(str);
        int n = s.length();
        if (n == 0) return 0;
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(j) == s.charAt(i)) { dp[i][j] = dp[i][j - 1]; continue; }   // extend s[i]'s turn to j
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
            }
        }
        return dp[0][n - 1];
    }

    public static void main(String[] args) {
        check(strangePrinter("aaabbb"), 2); check(strangePrinter("aba"), 2); check(bottomUpAlt("aba"), 2);
        check(strangePrinter("tbgtgb"), 4); check(bottomUpAlt("tbgtgb"), 4);
        Random r = new Random(59);
        for (int t = 0; t < 300; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(5) + 1; i > 0; i--) sb.append((char) ('a' + r.nextInt(3)));
            String s = sb.toString();
            int e = bfs(s);
            check(strangePrinter(s), e); check(bottomUpAlt(s), e);
        }
        for (int t = 0; t < 300; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(30) + 1; i > 0; i--) sb.append((char) ('a' + r.nextInt(4)));
            check(strangePrinter(sb.toString()), bottomUpAlt(sb.toString()));
        }
        System.out.println("StrangePrinter: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
