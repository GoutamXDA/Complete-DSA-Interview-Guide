import java.util.*;

/**
 * LC 31 — Next Permutation (in place).
 *
 * Approach 1 Brute : generate all permutations in sorted order, find current, return next. O(n! * n)
 * Approach 2 Optimal (3 steps) O(n), O(1):
 *   1. From the right, find first i with a[i] < a[i+1] (the "dip"). If none -> reverse all (last perm).
 *   2. From the right, find first j with a[j] > a[i]; swap(i, j).
 *   3. Reverse a[i+1..end] (it was descending; make it the smallest ascending suffix).
 */
public class NextPermutation {

    static void nextPermutation(int[] a) {
        int n = a.length, i = n - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i >= 0) {
            int j = n - 1;
            while (a[j] <= a[i]) j--;
            swap(a, i, j);
        }
        for (int l = i + 1, r = n - 1; l < r; l++, r--) swap(a, l, r);
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    /** Brute force for verification on small inputs. */
    static int[] bruteNext(int[] a) {
        List<String> perms = new ArrayList<>();
        permute(a.clone(), 0, perms);
        TreeSet<String> sorted = new TreeSet<>(perms);
        String cur = Arrays.toString(a);
        String next = sorted.higher(cur);
        if (next == null) next = sorted.first();
        return Arrays.stream(next.replaceAll("[\\[\\] ]", "").split(",")).mapToInt(Integer::parseInt).toArray();
    }

    static void permute(int[] a, int k, List<String> out) {
        if (k == a.length) { out.add(Arrays.toString(a)); return; }
        for (int i = k; i < a.length; i++) { swap(a, k, i); permute(a, k + 1, out); swap(a, k, i); }
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3}; nextPermutation(a); check(Arrays.toString(a), "[1, 3, 2]");
        int[] b = {3, 2, 1}; nextPermutation(b); check(Arrays.toString(b), "[1, 2, 3]");
        int[] c = {1, 1, 5}; nextPermutation(c); check(Arrays.toString(c), "[1, 5, 1]");
        int[] d = {2, 3, 1}; check(Arrays.toString(bruteNext(d)), "[3, 1, 2]");
        nextPermutation(d); check(Arrays.toString(d), "[3, 1, 2]");
        System.out.println("NextPermutation: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
