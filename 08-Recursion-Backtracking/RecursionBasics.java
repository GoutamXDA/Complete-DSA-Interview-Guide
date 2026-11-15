import java.util.*;

/**
 * Recursion warm-ups — build the "leap of faith" muscle.
 *
 * sumN / factorial      : T(n) = T(n-1) + O(1) -> O(n) time, O(n) stack
 * reverse / palindrome  : two-index recursion
 * Fibonacci (LC 509)    : naive O(2^n) -> memo O(n) -> iterative O(n), O(1)
 * Tower of Hanoi        : 2^n - 1 moves
 */
public class RecursionBasics {

    static void printNto1(int n, List<Integer> out) { if (n == 0) return; out.add(n); printNto1(n - 1, out); }
    static void print1toN(int n, List<Integer> out) { if (n == 0) return; print1toN(n - 1, out); out.add(n); } // backtracking order

    static int sumN(int n) { return n == 0 ? 0 : n + sumN(n - 1); }
    static long factorial(int n) { return n <= 1 ? 1 : n * factorial(n - 1); }

    static void reverse(int[] a, int l, int r) {
        if (l >= r) return;
        int t = a[l]; a[l] = a[r]; a[r] = t;
        reverse(a, l + 1, r - 1);
    }

    static boolean isPalindrome(String s, int i) {
        if (i >= s.length() / 2) return true;
        return s.charAt(i) == s.charAt(s.length() - 1 - i) && isPalindrome(s, i + 1);
    }

    static int fibNaive(int n) { return n < 2 ? n : fibNaive(n - 1) + fibNaive(n - 2); }

    static int fibMemo(int n, int[] memo) {
        if (n < 2) return n;
        if (memo[n] != 0) return memo[n];
        return memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    }

    static int fibIterative(int n) {
        if (n < 2) return n;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    static void hanoi(int n, char from, char to, char via, List<String> moves) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to, moves);
        moves.add(n + ":" + from + "->" + to);
        hanoi(n - 1, via, to, from, moves);
    }

    public static void main(String[] args) {
        List<Integer> o1 = new ArrayList<>(), o2 = new ArrayList<>();
        printNto1(3, o1); print1toN(3, o2);
        check(o1, List.of(3, 2, 1)); check(o2, List.of(1, 2, 3));
        check(sumN(100), 5050); check(factorial(10), 3628800L);
        int[] a = {1, 2, 3, 4, 5}; reverse(a, 0, 4); check(Arrays.toString(a), "[5, 4, 3, 2, 1]");
        check(isPalindrome("madam", 0), true); check(isPalindrome("hello", 0), false);
        check(fibNaive(20), 6765); check(fibMemo(40, new int[41]), 102334155); check(fibIterative(40), 102334155);
        List<String> moves = new ArrayList<>(); hanoi(3, 'A', 'C', 'B', moves);
        check(moves.size(), 7); check(moves.get(0), "1:A->C");
        System.out.println("RecursionBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
