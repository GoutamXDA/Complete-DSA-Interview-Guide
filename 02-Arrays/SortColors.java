import java.util.Arrays;

/**
 * LC 75 — Sort Colors (0s, 1s, 2s) in place.
 *
 * Approach 1 Sort         : Arrays.sort                    O(n log n)
 * Approach 2 Counting     : count 0/1/2, overwrite         O(n), two passes
 * Approach 3 Dutch flag   : low, mid, high pointers        O(n), one pass, O(1)
 *   Invariant: [0,low) = 0s, [low,mid) = 1s, (high,n-1] = 2s, [mid,high] unknown.
 *   Pitfall: after swapping with high, do NOT advance mid (the swapped-in value is unchecked).
 */
public class SortColors {

    static void sortCounting(int[] a) {
        int[] c = new int[3];
        for (int x : a) c[x]++;
        int i = 0;
        for (int v = 0; v < 3; v++) while (c[v]-- > 0) a[i++] = v;
    }

    static void dutchFlag(int[] a) {
        int low = 0, mid = 0, high = a.length - 1;
        while (mid <= high) {
            if (a[mid] == 0) swap(a, low++, mid++);
            else if (a[mid] == 1) mid++;
            else swap(a, mid, high--);
        }
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    public static void main(String[] args) {
        int[] a = {2, 0, 2, 1, 1, 0}; sortCounting(a); check(Arrays.toString(a), "[0, 0, 1, 1, 2, 2]");
        int[] b = {2, 0, 2, 1, 1, 0}; dutchFlag(b); check(Arrays.toString(b), "[0, 0, 1, 1, 2, 2]");
        int[] c = {2, 0, 1}; dutchFlag(c); check(Arrays.toString(c), "[0, 1, 2]");
        System.out.println("SortColors: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
