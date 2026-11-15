import java.util.Arrays;

/**
 * Binary search fundamentals. All O(log n).
 *
 * LC 704 search    : linear O(n) vs iterative / recursive binary search
 * lowerBound(x)    : first index with a[i] >= x   (LC 35 search insert = lowerBound)
 * upperBound(x)    : first index with a[i] >  x
 * LC 34 first/last : [lowerBound(x), upperBound(x) - 1] if present
 * LC 69 sqrt       : largest m with m*m <= x (use long)
 */
public class BinarySearchBasics {

    static int linear(int[] a, int t) { for (int i = 0; i < a.length; i++) if (a[i] == t) return i; return -1; }

    static int iterative(int[] a, int t) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == t) return mid;
            if (a[mid] < t) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }

    static int recursive(int[] a, int t, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        if (a[mid] == t) return mid;
        return a[mid] < t ? recursive(a, t, mid + 1, hi) : recursive(a, t, lo, mid - 1);
    }

    static int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = a.length;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] >= x) { ans = mid; hi = mid - 1; } else lo = mid + 1;
        }
        return ans;
    }

    static int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length - 1, ans = a.length;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] > x) { ans = mid; hi = mid - 1; } else lo = mid + 1;
        }
        return ans;
    }

    static int[] searchRange(int[] a, int x) {
        int l = lowerBound(a, x);
        if (l == a.length || a[l] != x) return new int[]{-1, -1};
        return new int[]{l, upperBound(a, x) - 1};
    }

    static int countOccurrences(int[] a, int x) { return upperBound(a, x) - lowerBound(a, x); }

    static int mySqrt(int x) {
        long lo = 0, hi = x, ans = 0;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid * mid <= x) { ans = mid; lo = mid + 1; } else hi = mid - 1;
        }
        return (int) ans;
    }

    public static void main(String[] args) {
        int[] a = {-1, 0, 3, 5, 9, 12};
        check(linear(a, 9), 4); check(iterative(a, 9), 4); check(recursive(a, 9, 0, a.length - 1), 4);
        check(iterative(a, 2), -1);
        int[] b = {5, 7, 7, 8, 8, 10};
        check(lowerBound(b, 8), 3); check(upperBound(b, 8), 5); check(lowerBound(b, 6), 1);
        check(Arrays.toString(searchRange(b, 8)), "[3, 4]"); check(Arrays.toString(searchRange(b, 6)), "[-1, -1]");
        check(countOccurrences(b, 7), 2);
        check(mySqrt(8), 2); check(mySqrt(2147395599), 46339);
        System.out.println("BinarySearchBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
