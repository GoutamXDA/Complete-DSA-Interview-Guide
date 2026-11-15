import java.util.*;

/**
 * Pattern matching: find all occurrences of pattern p in text t.
 *
 * Naive       O(n*m)
 * KMP         O(n+m): lps[i] = length of longest proper prefix of p[0..i] that's also a suffix.
 * Z-algorithm O(n+m): Z of p + '$' + t; positions with Z == m are matches.
 * Rabin-Karp  O(n+m) average: rolling hash, verify on hash equality (avoid false positives).
 *
 * LC 214 Shortest palindrome: lps of s + '#' + reverse(s) gives longest palindromic prefix.
 * LC 459 Repeated substring pattern: L = lps[n-1]; true iff L > 0 and n % (n - L) == 0.
 */
public class StringMatching {

    static List<Integer> naive(String t, String p) {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i + p.length() <= t.length(); i++) if (t.startsWith(p, i)) r.add(i);
        return r;
    }

    static int[] lps(String p) {
        int[] lps = new int[p.length()];
        for (int i = 1, len = 0; i < p.length(); ) {
            if (p.charAt(i) == p.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];            // fall back, don't move i
            else lps[i++] = 0;
        }
        return lps;
    }

    static List<Integer> kmp(String t, String p) {
        List<Integer> r = new ArrayList<>();
        int[] lps = lps(p);
        for (int i = 0, j = 0; i < t.length(); ) {
            if (t.charAt(i) == p.charAt(j)) {
                i++; j++;
                if (j == p.length()) { r.add(i - j); j = lps[j - 1]; }
            } else if (j > 0) j = lps[j - 1];
            else i++;
        }
        return r;
    }

    static int[] zArray(String s) {
        int n = s.length(); int[] z = new int[n];
        for (int i = 1, l = 0, r = 0; i < n; i++) {
            if (i < r) z[i] = Math.min(r - i, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] > r) { l = i; r = i + z[i]; }
        }
        return z;
    }

    static List<Integer> zSearch(String t, String p) {
        String s = p + "$" + t;
        int[] z = zArray(s);
        List<Integer> r = new ArrayList<>();
        for (int i = p.length() + 1; i < s.length(); i++) if (z[i] == p.length()) r.add(i - p.length() - 1);
        return r;
    }

    static List<Integer> rabinKarp(String t, String p) {
        final long MOD = 1_000_000_007L, B = 131;
        int n = t.length(), m = p.length();
        List<Integer> r = new ArrayList<>();
        if (m > n) return r;
        long hp = 0, ht = 0, pow = 1;
        for (int i = 0; i < m; i++) {
            hp = (hp * B + p.charAt(i)) % MOD;
            ht = (ht * B + t.charAt(i)) % MOD;
            if (i > 0) pow = pow * B % MOD;
        }
        for (int i = 0; ; i++) {
            if (hp == ht && t.regionMatches(i, p, 0, m)) r.add(i);
            if (i + m >= n) break;
            ht = (ht - t.charAt(i) * pow % MOD + MOD) % MOD;     // drop left char
            ht = (ht * B + t.charAt(i + m)) % MOD;               // add right char
        }
        return r;
    }

    static String shortestPalindrome(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        int[] l = lps(s + "#" + rev);
        int palPrefix = l[l.length - 1];
        return rev.substring(0, s.length() - palPrefix) + s;
    }

    static boolean repeatedSubstringPattern(String s) {
        int n = s.length(), L = lps(s)[n - 1];
        return L > 0 && n % (n - L) == 0;
    }

    public static void main(String[] args) {
        String t = "aabaacaadaabaaba", p = "aaba";
        List<Integer> exp = List.of(0, 9, 12);
        check(naive(t, p), exp); check(kmp(t, p), exp); check(zSearch(t, p), exp); check(rabinKarp(t, p), exp);
        check(Arrays.toString(lps("aabaaab")), "[0, 1, 0, 1, 2, 2, 3]");
        check(kmp("sadbutsad", "sad"), List.of(0, 6));
        check(shortestPalindrome("aacecaaa"), "aaacecaaa"); check(shortestPalindrome("abcd"), "dcbabcd");
        check(repeatedSubstringPattern("abab"), true); check(repeatedSubstringPattern("aba"), false);
        check(repeatedSubstringPattern("abcabcabcabc"), true);
        System.out.println("StringMatching: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
