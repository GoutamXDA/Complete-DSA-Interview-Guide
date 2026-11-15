import java.util.*;

/**
 * 1D DP — each problem shown with as many of the 4 stages as useful:
 *   recursion -> memoisation -> tabulation -> space optimised.
 *
 * LC 70  Climbing stairs : f(n) = f(n-1) + f(n-2)
 * LC 746 Min cost stairs : f(i) = cost[i] + min(f(i-1), f(i-2)); answer min(f(n-1), f(n-2))
 * LC 198 House robber    : f(i) = max(f(i-1), a[i] + f(i-2))
 * LC 213 House robber II : circular -> max(rob(0..n-2), rob(1..n-1))
 * LC 91  Decode ways     : f(i) = f(i-1) if s[i-1] != '0' + f(i-2) if "10".."26"
 * LC 139 Word break      : dp[i] = exists j < i with dp[j] && s[j..i) in dict
 */
public class DP1D {

    // ---------- Climbing stairs ----------
    static int stairsRec(int n) { return n <= 1 ? 1 : stairsRec(n - 1) + stairsRec(n - 2); }        // O(2^n)

    static int stairsMemo(int n, int[] memo) {                                                          // O(n), O(n)
        if (n <= 1) return 1;
        if (memo[n] != 0) return memo[n];
        return memo[n] = stairsMemo(n - 1, memo) + stairsMemo(n - 2, memo);
    }

    static int stairsTab(int n) {                                                                       // O(n), O(n)
        int[] dp = new int[n + 1]; dp[0] = dp[1] = 1;
        for (int i = 2; i <= n; i++) dp[i] = dp[i - 1] + dp[i - 2];
        return dp[n];
    }

    static int stairs(int n) {                                                                          // O(n), O(1)
        int a = 1, b = 1;
        for (int i = 2; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // ---------- Min cost climbing stairs ----------
    static int minCostClimbingStairs(int[] cost) {
        int a = 0, b = 0;                    // min cost to stand on step i-2, i-1
        for (int i = 2; i <= cost.length; i++) { int c = Math.min(b + cost[i - 1], a + cost[i - 2]); a = b; b = c; }
        return b;
    }

    // ---------- House robber ----------
    static int robRec(int[] a, int i) { return i < 0 ? 0 : Math.max(robRec(a, i - 1), a[i] + robRec(a, i - 2)); }

    static int robMemo(int[] a, int i, int[] memo) {
        if (i < 0) return 0;
        if (memo[i] != -1) return memo[i];
        return memo[i] = Math.max(robMemo(a, i - 1, memo), a[i] + robMemo(a, i - 2, memo));
    }

    static int robTab(int[] a) {
        int n = a.length; if (n == 0) return 0;
        int[] dp = new int[n]; dp[0] = a[0];
        for (int i = 1; i < n; i++) dp[i] = Math.max(dp[i - 1], a[i] + (i >= 2 ? dp[i - 2] : 0));
        return dp[n - 1];
    }

    static int rob(int[] a) { return robRange(a, 0, a.length - 1); }

    static int robRange(int[] a, int l, int r) {
        int prev2 = 0, prev = 0;
        for (int i = l; i <= r; i++) { int cur = Math.max(prev, a[i] + prev2); prev2 = prev; prev = cur; }
        return prev;
    }

    static int robCircular(int[] a) {
        if (a.length == 1) return a[0];
        return Math.max(robRange(a, 0, a.length - 2), robRange(a, 1, a.length - 1));
    }

    // ---------- Decode ways ----------
    static int numDecodings(String s) {
        int n = s.length(), prev2 = 1, prev = s.charAt(0) == '0' ? 0 : 1; // dp[0]=1 (empty), dp[1]
        for (int i = 2; i <= n; i++) {
            int cur = 0;
            if (s.charAt(i - 1) != '0') cur += prev;
            int two = Integer.parseInt(s.substring(i - 2, i));
            if (two >= 10 && two <= 26) cur += prev2;
            prev2 = prev; prev = cur;
        }
        return prev;
    }

    // ---------- Word break ----------
    static boolean wordBreakRec(String s, Set<String> dict, int start, Boolean[] memo) {
        if (start == s.length()) return true;
        if (memo[start] != null) return memo[start];
        for (int end = start + 1; end <= s.length(); end++)
            if (dict.contains(s.substring(start, end)) && wordBreakRec(s, dict, end, memo)) return memo[start] = true;
        return memo[start] = false;
    }

    static boolean wordBreak(String s, List<String> words) {
        Set<String> dict = new HashSet<>(words);
        boolean[] dp = new boolean[s.length() + 1]; dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i && !dp[i]; j++) dp[i] = dp[j] && dict.contains(s.substring(j, i));
        return dp[s.length()];
    }

    public static void main(String[] args) {
        check(stairsRec(10), 89); check(stairsMemo(10, new int[11]), 89); check(stairsTab(10), 89); check(stairs(10), 89);
        check(minCostClimbingStairs(new int[]{10, 15, 20}), 15);
        check(minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}), 6);
        int[] h = {2, 7, 9, 3, 1};
        int[] memo = new int[h.length]; Arrays.fill(memo, -1);
        check(robRec(h, h.length - 1), 12); check(robMemo(h, h.length - 1, memo), 12); check(robTab(h), 12); check(rob(h), 12);
        check(robCircular(new int[]{2, 3, 2}), 3); check(robCircular(new int[]{1, 2, 3, 1}), 4);
        check(numDecodings("226"), 3); check(numDecodings("06"), 0); check(numDecodings("11106"), 2);
        check(wordBreak("leetcode", List.of("leet", "code")), true); check(wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")), false);
        check(wordBreakRec("applepenapple", Set.of("apple", "pen"), 0, new Boolean[14]), true);
        System.out.println("DP1D: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
