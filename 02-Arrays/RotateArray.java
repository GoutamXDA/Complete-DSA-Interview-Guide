import java.util.Arrays;

/**
 * LC 189 — Rotate array right by k.
 *
 * Approach 1 Brute  : rotate by 1, k times.                    O(n*k), O(1)
 * Approach 2 Extra  : res[(i + k) % n] = a[i].                 O(n),   O(n)
 * Approach 3 Reverse: reverse all, reverse [0,k), reverse [k,n) O(n),   O(1)
 *   Why it works: [1 2 3 4 5], k=2 -> reverse -> [5 4 3 2 1] -> [4 5 | 1 2 3]
 * Pitfall: k can exceed n -> k %= n.
 */
public class RotateArray {

    static void rotateBrute(int[] a, int k) {
        int n = a.length; k %= n;
        for (int t = 0; t < k; t++) {
            int last = a[n - 1];
            for (int i = n - 1; i > 0; i--) a[i] = a[i - 1];
            a[0] = last;
        }
    }

    static void rotateExtra(int[] a, int k) {
        int n = a.length; int[] res = new int[n];
        for (int i = 0; i < n; i++) res[(i + k) % n] = a[i];
        System.arraycopy(res, 0, a, 0, n);
    }

    static void rotate(int[] a, int k) {
        int n = a.length; k %= n;
        reverse(a, 0, n - 1);
        reverse(a, 0, k - 1);
        reverse(a, k, n - 1);
    }

    static void reverse(int[] a, int l, int r) {
        while (l < r) { int t = a[l]; a[l++] = a[r]; a[r--] = t; }
    }

    public static void main(String[] args) {
        String exp = "[5, 6, 7, 1, 2, 3, 4]";
        int[] a = {1, 2, 3, 4, 5, 6, 7}; rotateBrute(a, 3); check(Arrays.toString(a), exp);
        int[] b = {1, 2, 3, 4, 5, 6, 7}; rotateExtra(b, 3); check(Arrays.toString(b), exp);
        int[] c = {1, 2, 3, 4, 5, 6, 7}; rotate(c, 10); check(Arrays.toString(c), exp);
        System.out.println("RotateArray: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
