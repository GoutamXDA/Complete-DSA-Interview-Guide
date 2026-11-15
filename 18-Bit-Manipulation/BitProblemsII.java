import java.util.*;

/**
 * LC 342  Power of four: loop dividing by 4 O(log n) | power of two AND the single bit at an even position (mask 0x55555555) O(1)
 * LC 89   Gray code: reflect-and-prefix construction O(2^n) | formula g(i) = i ^ (i >> 1) O(2^n)
 * LC 201  Bitwise AND of numbers range [l, r]: brute AND all O(r - l) | common binary prefix (shift both until equal) O(log r)
 *         or Kernighan: while r > l, r &= r - 1.
 * LC 477  Total hamming distance: brute all pairs O(n^2 * 32) | per bit: ones * (n - ones). O(32n)
 * LC 1310 XOR queries of a subarray: brute O(q * n) | prefix XOR, answer = px[r+1] ^ px[l]. O(n + q)
 */
public class BitProblemsII {

    static boolean isPowerOfFourLoop(int n) {
        if (n <= 0) return false;
        while (n % 4 == 0) n /= 4;
        return n == 1;
    }

    static boolean isPowerOfFour(int n) { return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0; }

    static List<Integer> grayCodeReflect(int n) {
        List<Integer> g = new ArrayList<>(List.of(0));
        for (int b = 0; b < n; b++)
            for (int i = g.size() - 1; i >= 0; i--) g.add(g.get(i) | 1 << b);   // mirror and set the new bit
        return g;
    }

    static List<Integer> grayCode(int n) {
        List<Integer> g = new ArrayList<>();
        for (int i = 0; i < 1 << n; i++) g.add(i ^ (i >> 1));
        return g;
    }

    static boolean isGray(List<Integer> g, int n) {
        if (g.size() != 1 << n || g.get(0) != 0 || new HashSet<>(g).size() != g.size()) return false;
        for (int i = 0; i < g.size(); i++) if (Integer.bitCount(g.get(i) ^ g.get((i + 1) % g.size())) != 1) return false;
        return true;
    }

    static int rangeBitwiseAndBrute(int l, int r) { int x = l; for (long v = l; v <= r; v++) x &= (int) v; return x; }

    static int rangeBitwiseAnd(int l, int r) {
        int shift = 0;
        while (l != r) { l >>= 1; r >>= 1; shift++; }
        return l << shift;
    }

    static int rangeBitwiseAndKernighan(int l, int r) { while (r > l) r &= r - 1; return r; }

    static int totalHammingBrute(int[] a) {
        int s = 0;
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) s += Integer.bitCount(a[i] ^ a[j]);
        return s;
    }

    static int totalHammingDistance(int[] a) {
        int s = 0;
        for (int b = 0; b < 32; b++) {
            int ones = 0;
            for (int x : a) ones += (x >> b) & 1;
            s += ones * (a.length - ones);       // each (one, zero) pair differs at this bit
        }
        return s;
    }

    static int[] xorQueriesBrute(int[] a, int[][] q) {
        int[] r = new int[q.length];
        for (int i = 0; i < q.length; i++) for (int j = q[i][0]; j <= q[i][1]; j++) r[i] ^= a[j];
        return r;
    }

    static int[] xorQueries(int[] a, int[][] q) {
        int[] px = new int[a.length + 1];
        for (int i = 0; i < a.length; i++) px[i + 1] = px[i] ^ a[i];
        int[] r = new int[q.length];
        for (int i = 0; i < q.length; i++) r[i] = px[q[i][1] + 1] ^ px[q[i][0]];
        return r;
    }

    public static void main(String[] args) {
        for (int n = -5; n <= 300; n++) check(isPowerOfFour(n), isPowerOfFourLoop(n));
        check(isPowerOfFour(16), true); check(isPowerOfFour(8), false); check(isPowerOfFour(1073741824), true);
        for (int n = 1; n <= 6; n++) { check(isGray(grayCode(n), n), true); check(isGray(grayCodeReflect(n), n), true); }
        check(grayCode(2), List.of(0, 1, 3, 2));
        for (int l = 0; l <= 40; l++) for (int r = l; r <= 40; r++) { int e = rangeBitwiseAndBrute(l, r); check(rangeBitwiseAnd(l, r), e); check(rangeBitwiseAndKernighan(l, r), e); }
        check(rangeBitwiseAnd(5, 7), 4); check(rangeBitwiseAnd(1, 2147483647), 0);
        check(totalHammingDistance(new int[]{4, 14, 2}), 6); check(totalHammingBrute(new int[]{4, 14, 4}), 4); check(totalHammingDistance(new int[]{4, 14, 4}), 4);
        int[] xa = {1, 3, 4, 8};
        int[][] xq = {{0, 1}, {1, 2}, {0, 3}, {3, 3}};
        check(Arrays.toString(xorQueries(xa, xq)), "[2, 7, 14, 8]"); check(Arrays.toString(xorQueriesBrute(xa, xq)), "[2, 7, 14, 8]");
        System.out.println("BitProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
