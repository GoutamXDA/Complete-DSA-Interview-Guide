import java.util.*;

/**
 * LC 214 — Shortest Palindrome. Explanation: ShortestPalindrome.md
 * 1. Brute: longest palindromic prefix by trying every length     O(n^2)
 * 2. Two-pointer recursion                                         O(n^2) worst, fast in practice
 * 3. KMP prefix function on s + '#' + reverse(s)                    O(n)
 * 4. Rolling hash (forward vs reverse), verified                    O(n) expected
 */
public class ShortestPalindrome {

    static String brute(String s) {
        int n = s.length(), L = 0;
        for (int len = n; len >= 0; len--) if (isPal(s, 0, len - 1)) { L = len; break; }
        return new StringBuilder(s.substring(L)).reverse() + s;
    }

    static boolean isPal(String s, int l, int r) { while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false; return true; }

    static String twoPointer(String s) {
        int i = 0;
        for (int j = s.length() - 1; j >= 0; j--) if (s.charAt(i) == s.charAt(j)) i++;
        if (i == s.length()) return s;
        String rest = s.substring(i);
        return new StringBuilder(rest).reverse() + twoPointer(s.substring(0, i)) + rest;
    }

    static String kmp(String s) {
        String t = s + "#" + new StringBuilder(s).reverse();
        int[] lps = new int[t.length()];
        for (int i = 1, len = 0; i < t.length(); ) {
            if (t.charAt(i) == t.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        int L = lps[t.length() - 1];
        return new StringBuilder(s.substring(L)).reverse() + s;
    }

    static String rollingHash(String s) {
        final long MOD = 1_000_000_007L, B = 131;
        long fwd = 0, rev = 0, pow = 1;
        int L = 0;
        for (int i = 0; i < s.length(); i++) {
            fwd = (fwd * B + s.charAt(i)) % MOD;            // hash of s[0..i]
            rev = (rev + s.charAt(i) * pow) % MOD;          // hash of reverse(s[0..i])
            pow = pow * B % MOD;
            if (fwd == rev && isPal(s, 0, i)) L = i + 1;     // verify to rule out collisions
        }
        return new StringBuilder(s.substring(L)).reverse() + s;
    }

    public static void main(String[] args) {
        for (String s : new String[]{"aacecaaa", "abcd", "", "a", "aba", "aab"}) {
            String e = brute(s);
            check(kmp(s), e); check(twoPointer(s), e); check(rollingHash(s), e);
        }
        check(kmp("aacecaaa"), "aaacecaaa"); check(kmp("abcd"), "dcbabcd");
        Random r = new Random(8);
        for (int t = 0; t < 2000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(12); i > 0; i--) sb.append((char) ('a' + r.nextInt(2)));
            String s = sb.toString(), e = brute(s);
            check(kmp(s), e); check(twoPointer(s), e); check(rollingHash(s), e);
        }
        System.out.println("ShortestPalindrome: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
