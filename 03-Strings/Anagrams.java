import java.util.*;

/**
 * Anagram family.
 *
 * LC 242 Valid anagram
 *   Sort both and compare  O(n log n)
 *   int[26] counts         O(n), O(1)    (HashMap for Unicode)
 *
 * LC 49 Group anagrams
 *   Key = sorted string    O(n * k log k)
 *   Key = count signature  O(n * k)
 *
 * LC 438 Find all anagram start indices of p in s
 *   Brute : sort every window   O(n * m log m)
 *   Optimal: fixed window of size m with int[26] counts, compare in O(26). O(n)
 */
public class Anagrams {

    static boolean isAnagramSort(String s, String t) {
        if (s.length() != t.length()) return false;
        char[] a = s.toCharArray(), b = t.toCharArray();
        Arrays.sort(a); Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] c = new int[26];
        for (int i = 0; i < s.length(); i++) { c[s.charAt(i) - 'a']++; c[t.charAt(i) - 'a']--; }
        for (int x : c) if (x != 0) return false;
        return true;
    }

    static List<List<String>> groupAnagramsSort(String[] words) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String w : words) {
            char[] cs = w.toCharArray(); Arrays.sort(cs);
            map.computeIfAbsent(new String(cs), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(map.values());
    }

    static List<List<String>> groupAnagramsCount(String[] words) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String w : words) {
            int[] c = new int[26];
            for (char ch : w.toCharArray()) c[ch - 'a']++;
            map.computeIfAbsent(Arrays.toString(c), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(map.values());
    }

    static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        int m = p.length();
        if (s.length() < m) return res;
        int[] need = new int[26], win = new int[26];
        for (char c : p.toCharArray()) need[c - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            win[s.charAt(i) - 'a']++;
            if (i >= m) win[s.charAt(i - m) - 'a']--;     // slide: drop leftmost
            if (i >= m - 1 && Arrays.equals(win, need)) res.add(i - m + 1);
        }
        return res;
    }

    public static void main(String[] args) {
        check(isAnagramSort("anagram", "nagaram"), true); check(isAnagram("anagram", "nagaram"), true);
        check(isAnagram("rat", "car"), false);
        String[] w = {"eat", "tea", "tan", "ate", "nat", "bat"};
        String exp = "[[eat, tea, ate], [tan, nat], [bat]]";
        check(groupAnagramsSort(w).toString(), exp); check(groupAnagramsCount(w).toString(), exp);
        check(findAnagrams("cbaebabacd", "abc"), List.of(0, 6));
        check(findAnagrams("abab", "ab"), List.of(0, 1, 2));
        System.out.println("Anagrams: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
