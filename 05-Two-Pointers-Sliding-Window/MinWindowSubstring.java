/**
 * LC 76 — Minimum Window Substring: smallest window of s containing all chars of t (with multiplicity).
 *
 * Brute  : every substring, check counts.                              O(n^2 * 128) or O(n^3)
 * Optimal: sliding window with need[] counts and a "missing" counter.
 *   Expand r; when missing == 0, shrink l while still valid, recording the best. O(n + m)
 */
public class MinWindowSubstring {

    static String brute(String s, String t) {
        String best = "";
        for (int i = 0; i < s.length(); i++)
            for (int j = i + t.length(); j <= s.length(); j++)
                if ((best.isEmpty() || j - i < best.length()) && covers(s.substring(i, j), t)) best = s.substring(i, j);
        return best;
    }

    static boolean covers(String w, String t) {
        int[] c = new int[128];
        for (char ch : w.toCharArray()) c[ch]++;
        for (char ch : t.toCharArray()) if (--c[ch] < 0) return false;
        return true;
    }

    static String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int missing = t.length(), l = 0, bestL = 0, bestLen = Integer.MAX_VALUE;
        for (int r = 0; r < s.length(); r++) {
            if (need[s.charAt(r)]-- > 0) missing--;          // this char was actually needed
            while (missing == 0) {
                if (r - l + 1 < bestLen) { bestLen = r - l + 1; bestL = l; }
                if (++need[s.charAt(l++)] > 0) missing++;    // removing a needed char breaks validity
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }

    public static void main(String[] args) {
        check(brute("ADOBECODEBANC", "ABC"), "BANC"); check(minWindow("ADOBECODEBANC", "ABC"), "BANC");
        check(minWindow("a", "aa"), ""); check(minWindow("a", "a"), "a");
        System.out.println("MinWindowSubstring: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
