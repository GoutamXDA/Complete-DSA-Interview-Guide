import java.util.*;

/**
 * LC 233 — Number of Digit One. Explanation: NumberOfDigitOne.md
 * 1. Brute force                                  O(n log n)
 * 2. Per-position contribution formula            O(log n)
 * 3. Digit DP (pos, count, tight)                 O(log^2 n * 10)
 */
public class NumberOfDigitOne {

    static long brute(int n) {
        long c = 0;
        for (int x = 1; x <= n; x++) for (int y = x; y > 0; y /= 10) if (y % 10 == 1) c++;
        return c;
    }

    static long countDigitOne(int n) {
        long total = 0;
        for (long p = 1; p <= n; p *= 10) {
            long high = n / (p * 10), cur = (n / p) % 10, low = n % p;
            if (cur == 0) total += high * p;
            else if (cur == 1) total += high * p + low + 1;
            else total += (high + 1) * p;
        }
        return total;
    }

    static char[] digits;
    static Long[][] memo;

    static long digitDP(int n) {
        if (n <= 0) return 0;
        digits = String.valueOf(n).toCharArray();
        memo = new Long[digits.length][digits.length + 1];
        return go(0, 0, true);
    }

    static long go(int pos, int ones, boolean tight) {
        if (pos == digits.length) return ones;
        if (!tight && memo[pos][ones] != null) return memo[pos][ones];
        int limit = tight ? digits[pos] - '0' : 9;
        long sum = 0;
        for (int d = 0; d <= limit; d++) sum += go(pos + 1, ones + (d == 1 ? 1 : 0), tight && d == limit);
        if (!tight) memo[pos][ones] = sum;
        return sum;
    }

    public static void main(String[] args) {
        check(countDigitOne(13), 6L); check(countDigitOne(0), 0L); check(digitDP(13), 6L);
        check(countDigitOne(1000000000), digitDP(1000000000));
        check(countDigitOne(Integer.MAX_VALUE), digitDP(Integer.MAX_VALUE));
        for (int n = 0; n <= 3000; n++) { long e = brute(n); check(countDigitOne(n), e); check(digitDP(n), e); }
        Random r = new Random(61);
        for (int t = 0; t < 200; t++) { int n = r.nextInt(2_000_000); check(countDigitOne(n), digitDP(n)); }
        System.out.println("NumberOfDigitOne: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
