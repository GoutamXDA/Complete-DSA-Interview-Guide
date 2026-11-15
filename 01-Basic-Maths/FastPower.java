/**
 * LC 50 Pow(x, n) + modular exponentiation + modular inverse.
 *
 * Approach 1 (brute)    : multiply x, n times. O(n) — TLE for n = 2^31.
 * Approach 2 (recursive): x^n = (x^(n/2))^2 * (x if n odd). O(log n) time, O(log n) stack.
 * Approach 3 (iterative): binary exponentiation. O(log n) time, O(1) space.
 * Pitfall: n = Integer.MIN_VALUE -> -n overflows. Use long.
 */
public class FastPower {
    static final long MOD = 1_000_000_007L;

    static double powBrute(double x, int n) {
        long N = n; double r = 1;
        if (N < 0) { x = 1 / x; N = -N; }
        for (long i = 0; i < N; i++) r *= x;
        return r;
    }

    static double powRecursive(double x, long n) {
        if (n < 0) return 1 / powRecursive(x, -n);
        if (n == 0) return 1;
        double half = powRecursive(x, n / 2);
        return n % 2 == 0 ? half * half : half * half * x;
    }

    static double myPow(double x, int n) {
        long N = n;
        if (N < 0) { x = 1 / x; N = -N; }
        double result = 1;
        while (N > 0) {
            if ((N & 1) == 1) result *= x;  // current bit set -> include this power
            x *= x;                          // x, x^2, x^4, x^8 ...
            N >>= 1;
        }
        return result;
    }

    static long modPow(long base, long exp, long mod) {
        long r = 1; base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) r = r * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return r;
    }

    /** Fermat's little theorem: a^(p-2) ≡ a^-1 (mod p), p prime. */
    static long modInverse(long a) { return modPow(a, MOD - 2, MOD); }

    public static void main(String[] args) {
        check(Math.abs(powBrute(2, 10) - 1024) < 1e-9, true);
        check(Math.abs(powRecursive(2, -2) - 0.25) < 1e-9, true);
        check(Math.abs(myPow(2.1, 3) - 9.261) < 1e-9, true);
        check(myPow(1.0, Integer.MIN_VALUE), 1.0);
        check(modPow(2, 30, MOD), 73741817L);
        check(modInverse(2) * 2 % MOD, 1L);
        System.out.println("FastPower: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
