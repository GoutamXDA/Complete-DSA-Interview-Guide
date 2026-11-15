import java.util.*;

/**
 * Warm-up array problems (all O(n) time, O(1) space for the optimal versions).
 *
 * Second largest : brute = sort O(n log n) | optimal = one pass with largest & second
 * Is sorted      : compare neighbours
 * LC 26 Remove duplicates (sorted): brute = TreeSet O(n log n) + O(n) space | optimal = write pointer
 * LC 283 Move zeroes: brute = temp array O(n) space | optimal = write pointer, swap
 * LC 268 Missing number: brute = check each 0..n O(n^2) | better = HashSet | optimal = sum or XOR
 */
public class BasicArrayOps {

    static int secondLargestBrute(int[] a) {
        int[] s = a.clone(); Arrays.sort(s);
        for (int i = s.length - 2; i >= 0; i--) if (s[i] != s[s.length - 1]) return s[i];
        return -1;
    }

    static int secondLargest(int[] a) {
        int first = Integer.MIN_VALUE, second = Integer.MIN_VALUE;
        for (int x : a) {
            if (x > first) { second = first; first = x; }
            else if (x < first && x > second) second = x;
        }
        return second == Integer.MIN_VALUE ? -1 : second;
    }

    static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) if (a[i] < a[i - 1]) return false;
        return true;
    }

    /** LC 26: returns k = number of unique elements, placed in a[0..k-1]. */
    static int removeDuplicates(int[] a) {
        if (a.length == 0) return 0;
        int write = 1;
        for (int i = 1; i < a.length; i++)
            if (a[i] != a[write - 1]) a[write++] = a[i];
        return write;
    }

    /** LC 283 */
    static void moveZeroes(int[] a) {
        int write = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) { int t = a[i]; a[i] = a[write]; a[write] = t; write++; }
        }
    }

    static int missingNumberSet(int[] a) {
        Set<Integer> s = new HashSet<>();
        for (int x : a) s.add(x);
        for (int i = 0; i <= a.length; i++) if (!s.contains(i)) return i;
        return -1;
    }

    static int missingNumberSum(int[] a) {
        long n = a.length, expected = n * (n + 1) / 2, sum = 0;
        for (int x : a) sum += x;
        return (int) (expected - sum);
    }

    /** XOR: x ^ x = 0, so XOR of 0..n with all elements leaves the missing one. No overflow risk. */
    static int missingNumberXor(int[] a) {
        int x = a.length;
        for (int i = 0; i < a.length; i++) x ^= i ^ a[i];
        return x;
    }

    public static void main(String[] args) {
        check(secondLargestBrute(new int[]{12, 35, 1, 10, 34, 1}), 34);
        check(secondLargest(new int[]{12, 35, 1, 10, 34, 1}), 34);
        check(secondLargest(new int[]{5, 5, 5}), -1);
        check(isSorted(new int[]{1, 2, 2, 3}), true); check(isSorted(new int[]{3, 1}), false);
        int[] d = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int k = removeDuplicates(d);
        check(k, 5); check(Arrays.toString(Arrays.copyOf(d, k)), "[0, 1, 2, 3, 4]");
        int[] z = {0, 1, 0, 3, 12}; moveZeroes(z);
        check(Arrays.toString(z), "[1, 3, 12, 0, 0]");
        int[] m = {9, 6, 4, 2, 3, 5, 7, 0, 1};
        check(missingNumberSet(m), 8); check(missingNumberSum(m), 8); check(missingNumberXor(m), 8);
        System.out.println("BasicArrayOps: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
