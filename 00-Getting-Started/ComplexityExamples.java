/**
 * One method per complexity class. Count the operations to build intuition.
 * Run: java ComplexityExamples.java
 */
public class ComplexityExamples {

    // O(1): fixed work regardless of n
    static int first(int[] a) { return a[0]; }

    // O(log n): the search space halves every step
    static int halvings(int n) {
        int steps = 0;
        while (n > 1) { n /= 2; steps++; }
        return steps;
    }

    // O(n): touch each element once
    static long sum(int[] a) {
        long s = 0;
        for (int x : a) s += x;
        return s;
    }

    // O(n log n): n iterations, each doing O(log n) work
    static int nLogN(int n) {
        int ops = 0;
        for (int i = 0; i < n; i++)
            for (int j = 1; j < n; j *= 2) ops++;
        return ops;
    }

    // O(n^2): every pair
    static int pairs(int n) {
        int ops = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) ops++;
        return ops; // n(n-1)/2
    }

    // O(2^n): naive recursion branching twice. T(n) = T(n-1) + T(n-2)
    static int fibNaive(int n) { return n < 2 ? n : fibNaive(n - 1) + fibNaive(n - 2); }

    // O(n) time, O(n) space recursion stack
    static int depth(int n) { return n == 0 ? 0 : 1 + depth(n - 1); }

    /*
     * Tricky one: harmonic series.
     * for i in 1..n: for j = i; j <= n; j += i   ->  n/1 + n/2 + ... + n/n = O(n log n)
     * (This is why the Sieve of Eratosthenes is O(n log log n).)
     */
    static int harmonic(int n) {
        int ops = 0;
        for (int i = 1; i <= n; i++)
            for (int j = i; j <= n; j += i) ops++;
        return ops;
    }

    public static void main(String[] args) {
        int[] a = {4, 2, 7};
        System.out.println("O(1)      first = " + first(a));
        System.out.println("O(log n)  halvings(1024) = " + halvings(1024));
        System.out.println("O(n)      sum = " + sum(a));
        System.out.println("O(nlogn)  nLogN(16) ops = " + nLogN(16));
        System.out.println("O(n^2)    pairs(10) ops = " + pairs(10));
        System.out.println("O(2^n)    fib(20) = " + fibNaive(20));
        System.out.println("O(n) stk  depth(1000) = " + depth(1000));
        System.out.println("O(nlogn)  harmonic(1000) ops = " + harmonic(1000));
    }
}
