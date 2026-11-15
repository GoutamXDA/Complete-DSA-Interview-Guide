import java.util.*;

/**
 * LC 67   Add binary: from the back with carry. O(max(m,n))
 * LC 680  Valid palindrome II (delete at most one): brute delete each index O(n^2) | on first mismatch try skip l or r. O(n)
 * LC 1614 Maximum nesting depth of parentheses: running depth. O(n)
 * LC 6    Zigzag conversion: simulate rows (bounce) O(n) | direct index math with cycle 2*(rows-1). O(n)
 * LC 43   Multiply strings: grade-school; a[i]*b[j] lands at positions i+j and i+j+1. O(mn)
 * LC 451  Sort characters by frequency: count + sort O(n log n) | bucket by frequency O(n)
 * LC 1781 Sum of beauty of all substrings (max freq - min freq): for each start extend with int[26]. O(26 n^2)
 * LC 68   Text justification: greedily pack words; spread spaces left-heavy; last line left-justified.
 */
public class StringProblemsII {

    static String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int s = carry + (i >= 0 ? a.charAt(i--) - '0' : 0) + (j >= 0 ? b.charAt(j--) - '0' : 0);
            sb.append(s % 2); carry = s / 2;
        }
        return sb.reverse().toString();
    }

    static boolean validPalindromeBrute(String s) {
        if (isPal(s, 0, s.length() - 1)) return true;
        for (int i = 0; i < s.length(); i++) {
            String t = s.substring(0, i) + s.substring(i + 1);
            if (isPal(t, 0, t.length() - 1)) return true;
        }
        return false;
    }

    static boolean validPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) return isPal(s, l + 1, r) || isPal(s, l, r - 1);
            l++; r--;
        }
        return true;
    }

    static boolean isPal(String s, int l, int r) { while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false; return true; }

    static int maxDepth(String s) {
        int d = 0, best = 0;
        for (char c : s.toCharArray()) { if (c == '(') best = Math.max(best, ++d); else if (c == ')') d--; }
        return best;
    }

    static String convertSimulate(String s, int rows) {
        if (rows == 1) return s;
        StringBuilder[] sb = new StringBuilder[rows];
        for (int i = 0; i < rows; i++) sb[i] = new StringBuilder();
        int r = 0, dir = 1;
        for (char c : s.toCharArray()) {
            sb[r].append(c);
            if (r == 0) dir = 1; else if (r == rows - 1) dir = -1;
            r += dir;
        }
        StringBuilder out = new StringBuilder();
        for (StringBuilder x : sb) out.append(x);
        return out.toString();
    }

    static String convert(String s, int rows) {
        if (rows == 1) return s;
        int cycle = 2 * (rows - 1), n = s.length();
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++)
            for (int j = r; j < n; j += cycle) {
                sb.append(s.charAt(j));
                int diag = j + cycle - 2 * r;              // middle rows have one extra char per cycle
                if (r != 0 && r != rows - 1 && diag < n) sb.append(s.charAt(diag));
            }
        return sb.toString();
    }

    static String multiply(String a, String b) {
        int m = a.length(), n = b.length();
        int[] res = new int[m + n];
        for (int i = m - 1; i >= 0; i--)
            for (int j = n - 1; j >= 0; j--) {
                int sum = (a.charAt(i) - '0') * (b.charAt(j) - '0') + res[i + j + 1];
                res[i + j + 1] = sum % 10;
                res[i + j] += sum / 10;
            }
        StringBuilder sb = new StringBuilder();
        for (int d : res) if (!(sb.length() == 0 && d == 0)) sb.append(d);
        return sb.length() == 0 ? "0" : sb.toString();
    }

    static String frequencySortSorting(String s) {
        Map<Character, Integer> f = new HashMap<>();
        for (char c : s.toCharArray()) f.merge(c, 1, Integer::sum);
        List<Character> keys = new ArrayList<>(f.keySet());
        keys.sort((x, y) -> f.get(y) != f.get(x).intValue() ? f.get(y) - f.get(x) : x - y);
        StringBuilder sb = new StringBuilder();
        for (char c : keys) sb.append(String.valueOf(c).repeat(f.get(c)));
        return sb.toString();
    }

    static String frequencySort(String s) {
        int[] f = new int[128];
        for (char c : s.toCharArray()) f[c]++;
        List<Character>[] bucket = new List[s.length() + 1];
        for (char c = 0; c < 128; c++) if (f[c] > 0) {
            if (bucket[f[c]] == null) bucket[f[c]] = new ArrayList<>();
            bucket[f[c]].add(c);
        }
        StringBuilder sb = new StringBuilder();
        for (int k = s.length(); k > 0; k--) if (bucket[k] != null) for (char c : bucket[k]) sb.append(String.valueOf(c).repeat(k));
        return sb.toString();
    }

    static int beautySum(String s) {
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int[] f = new int[26];
            for (int j = i; j < s.length(); j++) {
                f[s.charAt(j) - 'a']++;
                int max = 0, min = Integer.MAX_VALUE;
                for (int x : f) if (x > 0) { max = Math.max(max, x); min = Math.min(min, x); }
                total += max - min;
            }
        }
        return total;
    }

    static List<String> fullJustify(String[] words, int width) {
        List<String> res = new ArrayList<>();
        int i = 0;
        while (i < words.length) {
            int j = i, len = 0;
            while (j < words.length && len + words[j].length() + (j - i) <= width) len += words[j++].length();
            int gaps = j - i - 1;
            StringBuilder sb = new StringBuilder();
            if (j == words.length || gaps == 0) {            // last line or single word: left-justify
                for (int k = i; k < j; k++) { sb.append(words[k]); if (k < j - 1) sb.append(' '); }
                while (sb.length() < width) sb.append(' ');
            } else {
                int spaces = width - len, each = spaces / gaps, extra = spaces % gaps;
                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) sb.append(" ".repeat(each + (k - i < extra ? 1 : 0)));
                }
            }
            res.add(sb.toString());
            i = j;
        }
        return res;
    }

    public static void main(String[] args) {
        check(addBinary("11", "1"), "100"); check(addBinary("1010", "1011"), "10101");
        for (String t : new String[]{"aba", "abca", "abc", "deeee", "cbbcc"}) check(validPalindrome(t), validPalindromeBrute(t));
        check(validPalindrome("abca"), true); check(validPalindrome("abc"), false);
        check(maxDepth("(1+(2*3)+((8)/4))+1"), 3);
        check(convertSimulate("PAYPALISHIRING", 3), "PAHNAPLSIIGYIR"); check(convert("PAYPALISHIRING", 4), "PINALSIGYAHRPI");
        check(convert("AB", 1), "AB");
        check(multiply("123", "456"), "56088"); check(multiply("0", "52"), "0");
        check(frequencySort("tree").substring(0, 2), "ee"); check(frequencySortSorting("cccaaa"), "aaaccc");
        check(frequencySort("Aabb").substring(0, 2), "bb");
        check(beautySum("aabcb"), 5); check(beautySum("aabcbaa"), 17);
        check(fullJustify(new String[]{"This", "is", "an", "example", "of", "text", "justification."}, 16),
                List.of("This    is    an", "example  of text", "justification.  "));
        check(fullJustify(new String[]{"What", "must", "be", "acknowledgment", "shall", "be"}, 16),
                List.of("What   must   be", "acknowledgment  ", "shall be        "));
        System.out.println("StringProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
