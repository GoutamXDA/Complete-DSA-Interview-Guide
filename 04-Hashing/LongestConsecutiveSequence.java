import java.util.*;

/**
 * LC 128 — Longest Consecutive Sequence (unsorted, need O(n)).
 *
 * Approach 1 Brute : for each x, keep checking x+1, x+2 via linear search.  O(n^3) / O(n^2)
 * Approach 2 Sort  : sort, count runs (skip duplicates).                    O(n log n)
 * Approach 3 HashSet: only start counting at x where x-1 is NOT in the set (sequence head).
 *                     Each element visited at most twice -> O(n), O(n)
 */
public class LongestConsecutiveSequence {

    static int sorting(int[] a) {
        if (a.length == 0) return 0;
        int[] s = a.clone(); Arrays.sort(s);
        int best = 1, cur = 1;
        for (int i = 1; i < s.length; i++) {
            if (s[i] == s[i - 1]) continue;
            cur = (s[i] == s[i - 1] + 1) ? cur + 1 : 1;
            best = Math.max(best, cur);
        }
        return best;
    }

    static int hashSet(int[] a) {
        Set<Integer> set = new HashSet<>();
        for (int x : a) set.add(x);
        int best = 0;
        for (int x : set) {
            if (set.contains(x - 1)) continue; // not a head
            int len = 1;
            while (set.contains(x + len)) len++;
            best = Math.max(best, len);
        }
        return best;
    }

    public static void main(String[] args) {
        int[] a = {100, 4, 200, 1, 3, 2};
        check(sorting(a), 4); check(hashSet(a), 4);
        int[] b = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        check(sorting(b), 9); check(hashSet(b), 9); check(hashSet(new int[]{}), 0);
        System.out.println("LongestConsecutiveSequence: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
