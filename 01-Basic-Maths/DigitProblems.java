/**
 * Digit-based problems.
 *
 * 1. Count digits          : loop O(log n) | log10 O(1)
 * 2. Reverse integer (LC 7): pop digits, guard overflow BEFORE rev*10. O(log n)
 * 3. Palindrome (LC 9)     : A) string reverse O(d) space  B) reverse half the number O(1) space
 * 4. Armstrong number      : sum of digit^(#digits) == n
 * 5. Trailing zeroes in n! (LC 172): brute compute n! overflows -> count 5s: sum n/5^k. O(log n)
 */
public class DigitProblems {

    static int countDigitsLoop(int n) {
        if (n == 0) return 1;
        n = Math.abs(n);
        int c = 0;
        while (n > 0) { c++; n /= 10; }
        return c;
    }

    static int countDigitsLog(int n) {
        return n == 0 ? 1 : (int) Math.log10(Math.abs((long) n)) + 1;
    }

    /** LC 7: return 0 on overflow. */
    static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int d = x % 10;   // negative for negative x — works naturally
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && d > 7)) return 0;
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && d < -8)) return 0;
            rev = rev * 10 + d;
        }
        return rev;
    }

    /** Approach A: convert to string. O(d) time and space. */
    static boolean isPalindromeString(int x) {
        String s = String.valueOf(x);
        return new StringBuilder(s).reverse().toString().equals(s);
    }

    /** Approach B (optimal): reverse only the second half. O(d) time, O(1) space. */
    static boolean isPalindromeHalf(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        int half = 0;
        while (x > half) { half = half * 10 + x % 10; x /= 10; }
        return x == half || x == half / 10; // odd length: drop middle digit
    }

    static boolean isArmstrong(int n) {
        int k = countDigitsLoop(n), sum = 0, t = n;
        while (t > 0) { sum += (int) Math.pow(t % 10, k); t /= 10; }
        return sum == n;
    }

    /** LC 172: zeros come from 2*5 pairs; 5s are rarer. */
    static int trailingZeroes(int n) {
        int c = 0;
        while (n > 0) { n /= 5; c += n; }
        return c;
    }

    public static void main(String[] args) {
        check(countDigitsLoop(12345), 5); check(countDigitsLog(12345), 5); check(countDigitsLoop(0), 1);
        check(reverse(123), 321); check(reverse(-120), -21); check(reverse(1534236469), 0);
        check(isPalindromeString(121), true); check(isPalindromeHalf(121), true);
        check(isPalindromeHalf(-121), false); check(isPalindromeHalf(10), false); check(isPalindromeHalf(1221), true);
        check(isArmstrong(153), true); check(isArmstrong(154), false);
        check(trailingZeroes(25), 6); check(trailingZeroes(3), 0);
        System.out.println("DigitProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
