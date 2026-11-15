import java.util.*;

/**
 * LC 136 Single Number (others twice): HashMap O(n) space | XOR all O(1)
 * LC 137 Single Number II (others thrice):
 *   count each of 32 bits mod 3 O(32n) | ones/twos state machine O(n), O(1)
 * LC 260 Single Number III (two singles): xor = a ^ b; lowest set bit splits the array into two groups.
 * LC 29 Divide two integers: subtract the largest divisor << k that fits. O(log^2 n). Overflow: MIN / -1.
 * LC 371 Sum without '+': sum = a ^ b, carry = (a & b) << 1, repeat until carry 0.
 */
public class SingleNumber {

    static int singleNumberMap(int[] a) {
        Map<Integer, Integer> f = new HashMap<>();
        for (int x : a) f.merge(x, 1, Integer::sum);
        for (var e : f.entrySet()) if (e.getValue() == 1) return e.getKey();
        return -1;
    }

    static int singleNumber(int[] a) { int x = 0; for (int v : a) x ^= v; return x; }

    static int singleNumberIIBits(int[] a) {
        int res = 0;
        for (int b = 0; b < 32; b++) {
            int c = 0;
            for (int x : a) c += (x >> b) & 1;
            if (c % 3 != 0) res |= 1 << b;
        }
        return res;
    }

    static int singleNumberII(int[] a) {
        int ones = 0, twos = 0;
        for (int x : a) {
            ones = (ones ^ x) & ~twos;   // bits seen 1 (mod 3) times
            twos = (twos ^ x) & ~ones;   // bits seen 2 (mod 3) times
        }
        return ones;
    }

    static int[] singleNumberIII(int[] a) {
        int xor = 0;
        for (int x : a) xor ^= x;
        int low = xor & -xor;           // a bit where the two singles differ
        int p = 0, q = 0;
        for (int x : a) if ((x & low) == 0) p ^= x; else q ^= x;
        return p < q ? new int[]{p, q} : new int[]{q, p};
    }

    static int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
        long a = Math.abs((long) dividend), b = Math.abs((long) divisor), q = 0;
        while (a >= b) {
            long t = b, m = 1;
            while (a >= (t << 1)) { t <<= 1; m <<= 1; }
            a -= t; q += m;
        }
        return (int) ((dividend < 0) ^ (divisor < 0) ? -q : q);
    }

    static int getSum(int a, int b) {
        while (b != 0) { int carry = (a & b) << 1; a ^= b; b = carry; }
        return a;
    }

    public static void main(String[] args) {
        check(singleNumberMap(new int[]{4, 1, 2, 1, 2}), 4); check(singleNumber(new int[]{4, 1, 2, 1, 2}), 4);
        check(singleNumberIIBits(new int[]{0, 1, 0, 1, 0, 1, 99}), 99); check(singleNumberII(new int[]{0, 1, 0, 1, 0, 1, 99}), 99);
        check(singleNumberII(new int[]{-2, -2, 1, 1, 4, 1, 4, 4, -4, -2}), -4);
        check(Arrays.toString(singleNumberIII(new int[]{1, 2, 1, 3, 2, 5})), "[3, 5]");
        check(divide(10, 3), 3); check(divide(7, -3), -2); check(divide(Integer.MIN_VALUE, -1), Integer.MAX_VALUE);
        check(divide(Integer.MIN_VALUE, 1), Integer.MIN_VALUE);
        check(getSum(2, 3), 5); check(getSum(-2, 3), 1);
        System.out.println("SingleNumber: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
