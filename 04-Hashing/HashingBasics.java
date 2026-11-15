import java.util.*;

/**
 * LC 217 Contains duplicate: brute O(n^2) | sort O(n log n) | HashSet O(n)
 * Frequency count: HashMap.merge
 * LC 387 First unique char: two passes over int[26]. O(n)
 * LC 349 Intersection (unique): two sets. LC 350 Intersection with counts: count map.
 */
public class HashingBasics {

    static boolean containsDuplicateBrute(int[] a) {
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) if (a[i] == a[j]) return true;
        return false;
    }

    static boolean containsDuplicateSort(int[] a) {
        int[] s = a.clone(); Arrays.sort(s);
        for (int i = 1; i < s.length; i++) if (s[i] == s[i - 1]) return true;
        return false;
    }

    static boolean containsDuplicate(int[] a) {
        Set<Integer> seen = new HashSet<>();
        for (int x : a) if (!seen.add(x)) return true;
        return false;
    }

    static Map<Integer, Integer> frequencies(int[] a) {
        Map<Integer, Integer> f = new TreeMap<>();
        for (int x : a) f.merge(x, 1, Integer::sum);
        return f;
    }

    static int firstUniqChar(String s) {
        int[] c = new int[26];
        for (char ch : s.toCharArray()) c[ch - 'a']++;
        for (int i = 0; i < s.length(); i++) if (c[s.charAt(i) - 'a'] == 1) return i;
        return -1;
    }

    static int[] intersection(int[] a, int[] b) {
        Set<Integer> sa = new HashSet<>(); for (int x : a) sa.add(x);
        Set<Integer> res = new TreeSet<>(); for (int x : b) if (sa.contains(x)) res.add(x);
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    static int[] intersect(int[] a, int[] b) {
        Map<Integer, Integer> c = new HashMap<>();
        for (int x : a) c.merge(x, 1, Integer::sum);
        List<Integer> res = new ArrayList<>();
        for (int x : b) if (c.getOrDefault(x, 0) > 0) { res.add(x); c.merge(x, -1, Integer::sum); }
        return res.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 1};
        check(containsDuplicateBrute(a), true); check(containsDuplicateSort(a), true); check(containsDuplicate(a), true);
        check(containsDuplicate(new int[]{1, 2, 3}), false);
        check(frequencies(new int[]{1, 2, 2, 3, 3, 3}).toString(), "{1=1, 2=2, 3=3}");
        check(firstUniqChar("loveleetcode"), 2); check(firstUniqChar("aabb"), -1);
        check(Arrays.toString(intersection(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4})), "[4, 9]");
        check(Arrays.toString(intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2})), "[2, 2]");
        System.out.println("HashingBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
