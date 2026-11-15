import java.util.*;

/**
 * LC 87 — Scramble String. Explanation: ScrambleString.md
 * 0. Generate every scramble of s1 (oracle, tiny n)
 * 1. Plain recursion                              exponential
 * 2. Memo on (i, j, len) + character-count prune  O(n^4)
 * 3. Bottom-up 3D DP                              O(n^4) time, O(n^3) space
 */
public class ScrambleString {

    static Set<String> allScrambles(String s, Map<String, Set<String>> memo) {
        if (memo.containsKey(s)) return memo.get(s);
        Set<String> res = new HashSet<>();
        if (s.length() == 1) res.add(s);
        for (int k = 1; k < s.length(); k++)
            for (String x : allScrambles(s.substring(0, k), memo))
                for (String y : allScrambles(s.substring(k), memo)) { res.add(x + y); res.add(y + x); }
        memo.put(s, res);
        return res;
    }

    static boolean recursion(String a, String b) {
        if (a.equals(b)) return true;
        int n = a.length();
        for (int k = 1; k < n; k++) {
            if (recursion(a.substring(0, k), b.substring(0, k)) && recursion(a.substring(k), b.substring(k))) return true;
            if (recursion(a.substring(0, k), b.substring(n - k)) && recursion(a.substring(k), b.substring(0, n - k))) return true;
        }
        return false;
    }

    static String s1, s2;
    static Boolean[][][] memo;

    static boolean isScramble(String a, String b) {
        if (a.length() != b.length()) return false;
        s1 = a; s2 = b;
        int n = a.length();
        memo = new Boolean[n][n][n + 1];
        return go(0, 0, n);
    }

    static boolean go(int i, int j, int len) {
        if (memo[i][j][len] != null) return memo[i][j][len];
        boolean res = false;
        if (s1.regionMatches(i, s2, j, len)) res = true;
        else if (sameCounts(i, j, len)) {
            for (int k = 1; k < len && !res; k++)
                res = (go(i, j, k) && go(i + k, j + k, len - k))
                        || (go(i, j + len - k, k) && go(i + k, j, len - k));
        }
        return memo[i][j][len] = res;
    }

    static boolean sameCounts(int i, int j, int len) {
        int[] c = new int[26];
        for (int t = 0; t < len; t++) { c[s1.charAt(i + t) - 'a']++; c[s2.charAt(j + t) - 'a']--; }
        for (int x : c) if (x != 0) return false;
        return true;
    }

    static boolean bottomUp(String a, String b) {
        int n = a.length();
        if (n != b.length()) return false;
        boolean[][][] dp = new boolean[n + 1][n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) dp[1][i][j] = a.charAt(i) == b.charAt(j);
        for (int len = 2; len <= n; len++)
            for (int i = 0; i + len <= n; i++)
                for (int j = 0; j + len <= n; j++)
                    for (int k = 1; k < len && !dp[len][i][j]; k++)
                        dp[len][i][j] = (dp[k][i][j] && dp[len - k][i + k][j + k])
                                || (dp[k][i][j + len - k] && dp[len - k][i + k][j]);
        return dp[n][0][0];
    }

    public static void main(String[] args) {
        check(isScramble("great", "rgeat"), true); check(isScramble("abcde", "caebd"), false); check(isScramble("a", "a"), true);
        check(bottomUp("great", "rgeat"), true); check(bottomUp("abcde", "caebd"), false);
        Random r = new Random(56);
        for (int t = 0; t < 600; t++) {
            int n = r.nextInt(5) + 1;
            String a = rand(r, n);
            Set<String> scr = allScrambles(a, new HashMap<>());
            String b = r.nextBoolean() ? new ArrayList<>(scr).get(r.nextInt(scr.size())) : rand(r, n);
            boolean e = scr.contains(b);
            check(recursion(a, b), e); check(isScramble(a, b), e); check(bottomUp(a, b), e);
        }
        check(isScramble("eebaacbcbcadaaedceaaacadccd", "eadcaacabaddaceacbceaabeccd"), false);   // pruning keeps this fast
        System.out.println("ScrambleString: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(3))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
