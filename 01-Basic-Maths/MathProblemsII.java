import java.util.*;

/**
 * LC 66  Plus one: add 1 from the back; carry stops at the first digit < 9; all 9s -> new array [1,0,...,0]. O(n)
 * LC 171 Excel sheet column number: base 26 with digits 1..26: res = res*26 + (c - 'A' + 1). O(L)
 * LC 202 Happy number:
 *   A) HashSet of seen sums -> loop detected when a sum repeats. O(log n) per step, O(k) space
 *   B) Floyd fast/slow on the "next" function, O(1) space.
 * nCr % p:
 *   A) Pascal DP C(n,r) = C(n-1,r-1) + C(n-1,r)       O(n*r)
 *   B) fact[n] * invFact[r] * invFact[n-r] (Fermat)    O(n) precompute, O(1) per query, p prime
 */
public class MathProblemsII {
    static final long MOD = 1_000_000_007L;

    static int[] plusOne(int[] d) {
        for (int i = d.length - 1; i >= 0; i--) {
            if (d[i] < 9) { d[i]++; return d; }
            d[i] = 0;
        }
        int[] r = new int[d.length + 1];
        r[0] = 1;
        return r;
    }

    static int titleToNumber(String col) {
        int res = 0;
        for (char c : col.toCharArray()) res = res * 26 + (c - 'A' + 1);
        return res;
    }

    static int next(int n) { int s = 0; while (n > 0) { int d = n % 10; s += d * d; n /= 10; } return s; }

    static boolean isHappySet(int n) {
        Set<Integer> seen = new HashSet<>();
        while (n != 1 && seen.add(n)) n = next(n);
        return n == 1;
    }

    static boolean isHappy(int n) {
        int slow = n, fast = next(n);
        while (fast != 1 && slow != fast) { slow = next(slow); fast = next(next(fast)); }
        return fast == 1;
    }

    static long nCrPascal(int n, int r) {
        long[] row = new long[r + 1];
        row[0] = 1;
        for (int i = 1; i <= n; i++)
            for (int j = Math.min(i, r); j >= 1; j--) row[j] = (row[j] + row[j - 1]) % MOD;
        return row[r];
    }

    static long[] fact, invFact;

    static void precompute(int n) {
        fact = new long[n + 1]; invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i % MOD;
        invFact[n] = power(fact[n], MOD - 2);
        for (int i = n; i > 0; i--) invFact[i - 1] = invFact[i] * i % MOD;   // 1/(i-1)! = i / i!
    }

    static long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return fact[n] * invFact[r] % MOD * invFact[n - r] % MOD;
    }

    static long power(long b, long e) {
        long r = 1; b %= MOD;
        while (e > 0) { if ((e & 1) == 1) r = r * b % MOD; b = b * b % MOD; e >>= 1; }
        return r;
    }

    public static void main(String[] args) {
        check(Arrays.toString(plusOne(new int[]{1, 2, 9})), "[1, 3, 0]");
        check(Arrays.toString(plusOne(new int[]{9, 9})), "[1, 0, 0]");
        check(titleToNumber("A"), 1); check(titleToNumber("AB"), 28); check(titleToNumber("ZY"), 701);
        check(isHappySet(19), true); check(isHappy(19), true); check(isHappy(2), false); check(isHappySet(2), false);
        precompute(1000);
        check(nCr(5, 2), 10L); check(nCrPascal(5, 2), 10L);
        check(nCr(1000, 500), nCrPascal(1000, 500)); check(nCr(3, 5), 0L);
        System.out.println("MathProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
