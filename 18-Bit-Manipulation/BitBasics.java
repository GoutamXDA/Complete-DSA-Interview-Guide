import java.util.*;

/**
 * Bit basics.
 * Count set bits: loop all 32 bits O(32) | Kernighan O(#set bits) | Integer.bitCount
 * LC 338 Counting bits: popcount each O(n log n) | DP O(n)
 * XOR 1..n: n%4 == 0 -> n, 1 -> 1, 2 -> n+1, 3 -> 0
 * LC 190 Reverse bits: build result bit by bit (use >>> for unsigned).
 */
public class BitBasics {

    static boolean getBit(int n, int i) { return ((n >> i) & 1) == 1; }
    static int setBit(int n, int i) { return n | (1 << i); }
    static int clearBit(int n, int i) { return n & ~(1 << i); }
    static int toggleBit(int n, int i) { return n ^ (1 << i); }

    static boolean isPowerOfTwo(int n) { return n > 0 && (n & (n - 1)) == 0; }

    static int countBitsLoop(int n) { int c = 0; for (int i = 0; i < 32; i++) c += (n >>> i) & 1; return c; }

    static int hammingWeight(int n) { int c = 0; while (n != 0) { n &= n - 1; c++; } return c; } // Kernighan

    static int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) dp[i] = dp[i >> 1] + (i & 1);
        return dp;
    }

    static int[] xorSwap(int a, int b) { a ^= b; b ^= a; a ^= b; return new int[]{a, b}; }

    static int xorTo(int n) {
        switch (n % 4) { case 0: return n; case 1: return 1; case 2: return n + 1; default: return 0; }
    }

    /** XOR of range [l, r] */
    static int xorRange(int l, int r) { return xorTo(r) ^ xorTo(l - 1); }

    static int minBitFlips(int a, int b) { return Integer.bitCount(a ^ b); }

    static int reverseBits(int n) {
        int r = 0;
        for (int i = 0; i < 32; i++) { r = (r << 1) | (n & 1); n >>>= 1; }
        return r;
    }

    public static void main(String[] args) {
        int n = 0b1010;
        check(getBit(n, 1), true); check(setBit(n, 0), 0b1011); check(clearBit(n, 3), 0b0010); check(toggleBit(n, 2), 0b1110);
        check(isPowerOfTwo(16), true); check(isPowerOfTwo(6), false); check(isPowerOfTwo(0), false); check(isPowerOfTwo(Integer.MIN_VALUE), false);
        check(countBitsLoop(11), 3); check(hammingWeight(11), 3); check(hammingWeight(-1), 32);
        check(Arrays.toString(countBits(5)), "[0, 1, 1, 2, 1, 2]");
        check(Arrays.toString(xorSwap(3, 7)), "[7, 3]");
        for (int k = 1; k <= 20; k++) { int x = 0; for (int i = 1; i <= k; i++) x ^= i; check(xorTo(k), x); }
        check(xorRange(4, 8), 4 ^ 5 ^ 6 ^ 7 ^ 8);
        check(minBitFlips(10, 7), 3);
        check(reverseBits(0b00000010100101000001111010011100), 0b00111001011110000010100101000000);
        System.out.println("BitBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
