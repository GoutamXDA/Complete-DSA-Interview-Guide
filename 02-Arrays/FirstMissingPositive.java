import java.util.*;

/**
 * LC 41 — First Missing Positive (O(n) time, O(1) space required).
 *
 * Approach 1 Sort    : sort, scan for 1, 2, 3...           O(n log n)
 * Approach 2 HashSet : add all, check 1..n+1               O(n), O(n)
 * Approach 3 Cyclic sort: put value v at index v-1 by swapping. First index i with a[i] != i+1 -> i+1.
 *   Key insight: answer is always in [1, n+1], so values outside that range can be ignored. O(n), O(1)
 *   Each swap places one number permanently, so total swaps <= n.
 */
public class FirstMissingPositive {

    static int sorting(int[] a) {
        int[] s = a.clone(); Arrays.sort(s);
        int want = 1;
        for (int x : s) if (x == want) want++;
        return want;
    }

    static int hashSet(int[] a) {
        Set<Integer> set = new HashSet<>();
        for (int x : a) set.add(x);
        for (int i = 1; ; i++) if (!set.contains(i)) return i;
    }

    static int cyclicSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            while (a[i] > 0 && a[i] <= n && a[a[i] - 1] != a[i]) { // != handles duplicates
                int t = a[a[i] - 1]; a[a[i] - 1] = a[i]; a[i] = t;
            }
        }
        for (int i = 0; i < n; i++) if (a[i] != i + 1) return i + 1;
        return n + 1;
    }

    public static void main(String[] args) {
        int[][] tests = {{1, 2, 0}, {3, 4, -1, 1}, {7, 8, 9, 11, 12}, {1, 1}};
        int[] exp = {3, 2, 1, 2};
        for (int t = 0; t < tests.length; t++) {
            check(sorting(tests[t]), exp[t]); check(hashSet(tests[t]), exp[t]);
            check(cyclicSort(tests[t].clone()), exp[t]);
        }
        System.out.println("FirstMissingPositive: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
