import java.util.*;

/**
 * LC 32 — Longest Valid Parentheses. Explanation: LongestValidParentheses.md
 * 1. Brute force (incremental from each start)    O(n^2)
 * 2. DP: longest valid ending at i                O(n)  O(n)
 * 3. Stack of indices with a base sentinel        O(n)  O(n)
 * 4. Two counters, two directions                 O(n)  O(1)
 */
public class LongestValidParentheses {

    static int brute(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            int bal = 0;
            for (int j = i; j < s.length(); j++) {
                bal += s.charAt(j) == '(' ? 1 : -1;
                if (bal < 0) break;
                if (bal == 0) best = Math.max(best, j - i + 1);
            }
        }
        return best;
    }

    static int dp(String s) {
        int n = s.length(), best = 0;
        int[] dp = new int[n];
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) != ')') continue;
            if (s.charAt(i - 1) == '(') dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
            else {
                int j = i - dp[i - 1] - 1;
                if (j >= 0 && s.charAt(j) == '(') dp[i] = dp[i - 1] + 2 + (j >= 1 ? dp[j - 1] : 0);
            }
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    static int stack(String s) {
        Deque<Integer> st = new ArrayDeque<>();
        st.push(-1);
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') st.push(i);
            else {
                st.pop();
                if (st.isEmpty()) st.push(i);
                else best = Math.max(best, i - st.peek());
            }
        }
        return best;
    }

    static int counters(String s) {
        int best = 0, open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * close); else if (close > open) open = close = 0;
        }
        open = close = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * open); else if (open > close) open = close = 0;
        }
        return best;
    }

    public static void main(String[] args) {
        check(stack("(()"), 2); check(stack(")()())"), 4); check(stack(""), 0); check(dp("()(())"), 6); check(counters("(()"), 2);
        Random r = new Random(31);
        for (int t = 0; t < 4000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(16); i > 0; i--) sb.append(r.nextBoolean() ? '(' : ')');
            String s = sb.toString();
            int e = brute(s);
            check(dp(s), e); check(stack(s), e); check(counters(s), e);
        }
        System.out.println("LongestValidParentheses: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
