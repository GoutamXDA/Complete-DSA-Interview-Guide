import java.util.Arrays;

/**
 * LC 238 — Product of Array Except Self (no division).
 *
 * Approach 1 Brute     : for each i multiply all others.                  O(n^2)
 * Approach 2 Division  : total / a[i] — breaks with zeros, and disallowed. O(n)
 * Approach 3 Prefix/suffix arrays: left[i] * right[i].                   O(n), O(n)
 * Approach 4 Optimal   : output array holds prefix, then multiply a running suffix. O(n), O(1) extra
 */
public class ProductExceptSelf {

    static int[] brute(int[] a) {
        int n = a.length; int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int p = 1;
            for (int j = 0; j < n; j++) if (j != i) p *= a[j];
            res[i] = p;
        }
        return res;
    }

    static int[] prefixSuffix(int[] a) {
        int n = a.length; int[] left = new int[n], right = new int[n], res = new int[n];
        left[0] = 1; for (int i = 1; i < n; i++) left[i] = left[i - 1] * a[i - 1];
        right[n - 1] = 1; for (int i = n - 2; i >= 0; i--) right[i] = right[i + 1] * a[i + 1];
        for (int i = 0; i < n; i++) res[i] = left[i] * right[i];
        return res;
    }

    static int[] optimal(int[] a) {
        int n = a.length; int[] res = new int[n];
        res[0] = 1;
        for (int i = 1; i < n; i++) res[i] = res[i - 1] * a[i - 1];
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) { res[i] *= suffix; suffix *= a[i]; }
        return res;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        String exp = "[24, 12, 8, 6]";
        check(Arrays.toString(brute(a)), exp); check(Arrays.toString(prefixSuffix(a)), exp);
        check(Arrays.toString(optimal(a)), exp);
        check(Arrays.toString(optimal(new int[]{-1, 1, 0, -3, 3})), "[0, 0, 9, 0, 0]");
        System.out.println("ProductExceptSelf: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
