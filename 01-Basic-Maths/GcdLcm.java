/**
 * GCD and LCM.
 *
 * Approach 1 (brute)   : try every d from min(a,b) down to 1. O(min(a,b))
 * Approach 2 (subtract): gcd(a,b) = gcd(a-b, b). O(max) worst case (e.g. gcd(10^9, 1))
 * Approach 3 (Euclid)  : gcd(a,b) = gcd(b, a % b). O(log min(a,b))  <-- use this
 * LCM                  : a / gcd * b  (divide first to avoid overflow)
 */
public class GcdLcm {

    static int gcdBrute(int a, int b) {
        for (int d = Math.min(a, b); d >= 1; d--)
            if (a % d == 0 && b % d == 0) return d;
        return Math.max(a, b); // one of them is 0
    }

    static int gcdSubtract(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;
        while (a != b) { if (a > b) a -= b; else b -= a; }
        return a;
    }

    static int gcdEuclidRecursive(int a, int b) { return b == 0 ? a : gcdEuclidRecursive(b, a % b); }

    static int gcd(int a, int b) {
        while (b != 0) { int t = a % b; a = b; b = t; }
        return a;
    }

    static long lcm(long a, long b) { return a / gcd((int) a, (int) b) * b; }

    /** GCD of an array: fold. */
    static int gcdArray(int[] arr) {
        int g = 0;
        for (int x : arr) g = gcd(g, x);
        return g;
    }

    public static void main(String[] args) {
        check(gcdBrute(12, 18), 6); check(gcdSubtract(12, 18), 6);
        check(gcdEuclidRecursive(12, 18), 6); check(gcd(12, 18), 6); check(gcd(7, 0), 7);
        check(lcm(4, 6), 12L); check(gcdArray(new int[]{12, 18, 24}), 6);
        System.out.println("GcdLcm: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
