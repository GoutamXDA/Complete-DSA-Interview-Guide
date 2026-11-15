import java.util.*;

/**
 * Prime number toolkit.
 *
 * isPrime         : brute O(n) -> √n trial division O(√n) -> 6k±1 optimisation
 * countPrimes LC204: brute n*√n -> Sieve of Eratosthenes O(n log log n)
 * factorise       : trial division O(√n); for many queries use Smallest Prime Factor sieve O(log n)/query
 * divisors        : O(√n) by pairing i with n/i
 */
public class Primes {

    static boolean isPrimeBrute(int n) {
        if (n < 2) return false;
        for (int i = 2; i < n; i++) if (n % i == 0) return false;
        return true;
    }

    static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n < 4) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6)       // every prime > 3 is 6k±1
            if (n % i == 0 || n % (i + 2) == 0) return false;
        return true;
    }

    /** LC 204: count primes strictly less than n. */
    static int countPrimes(int n) {
        if (n < 3) return 0;
        boolean[] composite = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (composite[i]) continue;
            count++;
            for (long j = (long) i * i; j < n; j += i) composite[(int) j] = true; // smaller multiples already marked
        }
        return count;
    }

    static List<Integer> factorise(int n) {
        List<Integer> f = new ArrayList<>();
        for (int i = 2; (long) i * i <= n; i++)
            while (n % i == 0) { f.add(i); n /= i; }
        if (n > 1) f.add(n); // leftover prime > √original
        return f;
    }

    /** Smallest prime factor sieve: build once O(n log log n), factorise each number in O(log n). */
    static int[] spfSieve(int n) {
        int[] spf = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            if (spf[i] != 0) continue;
            for (int j = i; j <= n; j += i) if (spf[j] == 0) spf[j] = i;
        }
        return spf;
    }

    static List<Integer> factoriseWithSpf(int x, int[] spf) {
        List<Integer> f = new ArrayList<>();
        while (x > 1) { f.add(spf[x]); x /= spf[x]; }
        return f;
    }

    static List<Integer> divisors(int n) {
        List<Integer> small = new ArrayList<>(), large = new ArrayList<>();
        for (int i = 1; (long) i * i <= n; i++) {
            if (n % i == 0) {
                small.add(i);
                if (i != n / i) large.add(n / i);
            }
        }
        Collections.reverse(large);
        small.addAll(large);
        return small;
    }

    public static void main(String[] args) {
        check(isPrimeBrute(29), true); check(isPrime(29), true); check(isPrime(1), false); check(isPrime(49), false);
        check(isPrime(2147483647), true);
        check(countPrimes(10), 4); check(countPrimes(0), 0);
        check(factorise(360), List.of(2, 2, 2, 3, 3, 5));
        int[] spf = spfSieve(1000);
        check(factoriseWithSpf(360, spf), List.of(2, 2, 2, 3, 3, 5));
        check(divisors(36), List.of(1, 2, 3, 4, 6, 9, 12, 18, 36));
        System.out.println("Primes: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
