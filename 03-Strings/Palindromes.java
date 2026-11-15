/**
 * Palindrome family.
 *
 * LC 344 Reverse string: two pointers swap. O(n), O(1)
 * LC 125 Valid palindrome: brute = clean + reverse O(n) space | optimal = two pointers skipping non-alnum O(1)
 * LC 5 Longest palindromic substring
 *   Brute  : check every substring                      O(n^3)
 *   DP     : dp[i][j] = s[i]==s[j] && dp[i+1][j-1]      O(n^2) time & space
 *   Expand : 2n-1 centres, expand while equal           O(n^2) time, O(1) space  <-- interview answer
 *   Manacher: O(n) (rarely required; know it exists)
 * LC 647 Count palindromic substrings: same expansion, count each successful expansion.
 */
public class Palindromes {

    static void reverseString(char[] s) {
        for (int l = 0, r = s.length - 1; l < r; l++, r--) { char t = s[l]; s[l] = s[r]; s[r] = t; }
    }

    static boolean isPalindromeBrute(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) if (Character.isLetterOrDigit(c)) sb.append(Character.toLowerCase(c));
        String clean = sb.toString();
        return clean.equals(sb.reverse().toString());
    }

    static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l++)) != Character.toLowerCase(s.charAt(r--))) return false;
        }
        return true;
    }

    static String longestPalindromeBrute(String s) {
        String best = "";
        for (int i = 0; i < s.length(); i++)
            for (int j = i; j < s.length(); j++)
                if (j - i + 1 > best.length() && isPal(s, i, j)) best = s.substring(i, j + 1);
        return best;
    }

    static boolean isPal(String s, int l, int r) {
        while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
        return true;
    }

    static String longestPalindromeDP(String s) {
        int n = s.length(), start = 0, len = 1;
        boolean[][] dp = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--)
            for (int j = i; j < n; j++) {
                dp[i][j] = s.charAt(i) == s.charAt(j) && (j - i < 3 || dp[i + 1][j - 1]);
                if (dp[i][j] && j - i + 1 > len) { len = j - i + 1; start = i; }
            }
        return s.substring(start, start + len);
    }

    static String longestPalindrome(String s) {
        int start = 0, end = 0;
        for (int c = 0; c < s.length(); c++) {
            int len = Math.max(expand(s, c, c), expand(s, c, c + 1)); // odd and even centres
            if (len > end - start + 1) { start = c - (len - 1) / 2; end = c + len / 2; }
        }
        return s.substring(start, end + 1);
    }

    static int expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return r - l - 1;
    }

    static int countSubstrings(String s) {
        int count = 0;
        for (int c = 0; c < s.length(); c++)
            for (int[] lr : new int[][]{{c, c}, {c, c + 1}}) {
                int l = lr[0], r = lr[1];
                while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { count++; l--; r++; }
            }
        return count;
    }

    public static void main(String[] args) {
        char[] h = "hello".toCharArray(); reverseString(h); check(new String(h), "olleh");
        String p = "A man, a plan, a canal: Panama";
        check(isPalindromeBrute(p), true); check(isPalindrome(p), true); check(isPalindrome("race a car"), false);
        check(longestPalindromeBrute("babad").length(), 3);
        check(longestPalindromeDP("cbbd"), "bb");
        check(longestPalindrome("cbbd"), "bb"); check(longestPalindrome("forgeeksskeegfor"), "geeksskeeg");
        check(countSubstrings("aaa"), 6); check(countSubstrings("abc"), 3);
        System.out.println("Palindromes: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
