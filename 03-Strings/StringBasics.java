import java.util.*;

/**
 * Frequently asked string problems.
 *
 * LC 151 Reverse words      : split on spaces, reverse list O(n); in-place char[] version: reverse all then each word.
 * LC 14  Longest common prefix: vertical scan O(total chars) | sort + compare first & last O(n k log n)
 * LC 8   atoi               : skip spaces, sign, digits; clamp on overflow before it happens.
 * LC 13  Roman -> int       : if value < next value, subtract it.
 * LC 12  Int -> Roman       : greedy over 13 value/symbol pairs.
 * LC 205 Isomorphic         : two maps (or two int[256]) for bijection.
 * LC 796 Rotate string      : goal is a rotation iff same length and (s+s) contains goal.
 * LC 38  Count and say      : run-length encode previous term n-1 times.
 */
public class StringBasics {

    static String reverseWords(String s) {
        String[] parts = s.trim().split("\\s+");
        Collections.reverse(Arrays.asList(parts));
        return String.join(" ", parts);
    }

    static String reverseWordsManual(String s) {
        StringBuilder sb = new StringBuilder();
        int i = s.length() - 1;
        while (i >= 0) {
            while (i >= 0 && s.charAt(i) == ' ') i--;
            if (i < 0) break;
            int end = i;
            while (i >= 0 && s.charAt(i) != ' ') i--;
            if (sb.length() > 0) sb.append(' ');
            sb.append(s, i + 1, end + 1);
        }
        return sb.toString();
    }

    static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (String s : strs) if (i == s.length() || s.charAt(i) != c) return strs[0].substring(0, i);
        }
        return strs[0];
    }

    static String longestCommonPrefixSort(String[] strs) {
        String[] s = strs.clone(); Arrays.sort(s);
        String a = s[0], b = s[s.length - 1];
        int i = 0;
        while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i)) i++;
        return a.substring(0, i);
    }

    static int myAtoi(String s) {
        int i = 0, n = s.length(), sign = 1, res = 0;
        while (i < n && s.charAt(i) == ' ') i++;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) sign = s.charAt(i++) == '-' ? -1 : 1;
        while (i < n && Character.isDigit(s.charAt(i))) {
            int d = s.charAt(i++) - '0';
            if (res > (Integer.MAX_VALUE - d) / 10) return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            res = res * 10 + d;
        }
        return res * sign;
    }

    static int romanToInt(String s) {
        Map<Character, Integer> v = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int cur = v.get(s.charAt(i));
            if (i + 1 < s.length() && cur < v.get(s.charAt(i + 1))) total -= cur; else total += cur;
        }
        return total;
    }

    static String intToRoman(int num) {
        int[] val = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] sym = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < val.length; i++) while (num >= val[i]) { num -= val[i]; sb.append(sym[i]); }
        return sb.toString();
    }

    static boolean isIsomorphic(String s, String t) {
        int[] ms = new int[256], mt = new int[256]; // last position + 1 where each char was seen
        for (int i = 0; i < s.length(); i++) {
            if (ms[s.charAt(i)] != mt[t.charAt(i)]) return false;
            ms[s.charAt(i)] = i + 1; mt[t.charAt(i)] = i + 1;
        }
        return true;
    }

    static boolean rotateString(String s, String goal) {
        return s.length() == goal.length() && (s + s).contains(goal);
    }

    static String countAndSay(int n) {
        String cur = "1";
        for (int k = 1; k < n; k++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cur.length(); ) {
                int j = i;
                while (j < cur.length() && cur.charAt(j) == cur.charAt(i)) j++;
                sb.append(j - i).append(cur.charAt(i));
                i = j;
            }
            cur = sb.toString();
        }
        return cur;
    }

    public static void main(String[] args) {
        check(reverseWords("  the sky  is blue "), "blue is sky the");
        check(reverseWordsManual("  the sky  is blue "), "blue is sky the");
        String[] f = {"flower", "flow", "flight"};
        check(longestCommonPrefix(f), "fl"); check(longestCommonPrefixSort(f), "fl");
        check(myAtoi("   -42"), -42); check(myAtoi("4193 with words"), 4193); check(myAtoi("-91283472332"), Integer.MIN_VALUE);
        check(romanToInt("MCMXCIV"), 1994); check(intToRoman(1994), "MCMXCIV");
        check(isIsomorphic("egg", "add"), true); check(isIsomorphic("badc", "baba"), false);
        check(rotateString("abcde", "cdeab"), true);
        check(countAndSay(4), "1211");
        System.out.println("StringBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
