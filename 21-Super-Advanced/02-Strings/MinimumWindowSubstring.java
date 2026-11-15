import java.util.*;

/**
 * LC 76 — Minimum Window Substring. Explanation: MinimumWindowSubstring.md
 * 1. Brute force every substring            O(n^2 * Σ)
 * 2. Window + full count comparison         O(n * Σ)
 * 3. Window + need[] + missing counter      O(n + m)
 */
public class MinimumWindowSubstring {

    static boolean covers(int[] have, int[] need) {
        for (int c = 0; c < 128; c++) if (have[c] < need[c]) return false;
        return true;
    }

    static String brute(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        String best = "";
        for (int i = 0; i < s.length(); i++) {
            int[] have = new int[128];
            for (int j = i; j < s.length(); j++) {
                have[s.charAt(j)]++;
                if (covers(have, need)) { if (best.isEmpty() || j - i + 1 < best.length()) best = s.substring(i, j + 1); break; }
            }
        }
        return best;
    }

    static String windowCompare(String s, String t) {
        int[] need = new int[128], have = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int l = 0, bl = 0, blen = Integer.MAX_VALUE;
        for (int r = 0; r < s.length(); r++) {
            have[s.charAt(r)]++;
            while (covers(have, need)) {
                if (r - l + 1 < blen) { blen = r - l + 1; bl = l; }
                have[s.charAt(l++)]--;
            }
        }
        return blen == Integer.MAX_VALUE ? "" : s.substring(bl, bl + blen);
    }

    static String minWindow(String s, String t) {
        if (t.length() > s.length()) return "";
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int missing = t.length(), l = 0, bl = 0, blen = Integer.MAX_VALUE;
        for (int r = 0; r < s.length(); r++) {
            if (need[s.charAt(r)]-- > 0) missing--;
            while (missing == 0) {
                if (r - l + 1 < blen) { blen = r - l + 1; bl = l; }
                if (++need[s.charAt(l++)] > 0) missing++;
            }
        }
        return blen == Integer.MAX_VALUE ? "" : s.substring(bl, bl + blen);
    }

    public static void main(String[] args) {
        check(minWindow("ADOBECODEBANC", "ABC"), "BANC"); check(minWindow("a", "a"), "a"); check(minWindow("a", "aa"), "");
        Random r = new Random(6);
        for (int k = 0; k < 2000; k++) {
            String s = rand(r, r.nextInt(15)), t = rand(r, r.nextInt(4) + 1);
            String e = brute(s, t);
            check(minWindow(s, t).length(), e.length()); check(windowCompare(s, t).length(), e.length());
        }
        System.out.println("MinimumWindowSubstring: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(3))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
