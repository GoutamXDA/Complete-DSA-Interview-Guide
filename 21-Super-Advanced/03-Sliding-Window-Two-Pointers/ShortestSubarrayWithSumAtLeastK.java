import java.util.*;

/**
 * LC 862 — Shortest Subarray with Sum at Least K (negatives allowed). Explanation: ShortestSubarrayWithSumAtLeastK.md
 * 1. Brute force over all pairs of prefix sums        O(n^2)
 * 2. Monotonic increasing deque of candidate starts   O(n)
 * (Also: the positive-only sliding window, to show why it fails with negatives.)
 */
public class ShortestSubarrayWithSumAtLeastK {

    static int brute(int[] a, int k) {
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; i++) {
            long s = 0;
            for (int j = i; j < a.length; j++) { s += a[j]; if (s >= k) { best = Math.min(best, j - i + 1); break; } }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    static int shortestSubarray(int[] a, int k) {
        int n = a.length;
        long[] p = new long[n + 1];
        for (int i = 0; i < n; i++) p[i + 1] = p[i] + a[i];
        Deque<Integer> dq = new ArrayDeque<>();
        int best = Integer.MAX_VALUE;
        for (int j = 0; j <= n; j++) {
            while (!dq.isEmpty() && p[j] - p[dq.peekFirst()] >= k) best = Math.min(best, j - dq.pollFirst());
            while (!dq.isEmpty() && p[dq.peekLast()] >= p[j]) dq.pollLast();
            dq.offerLast(j);
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    /** LC 209 window — correct only for non-negative values. */
    static int positiveWindow(int[] a, int k) {
        int l = 0, best = Integer.MAX_VALUE; long s = 0;
        for (int r = 0; r < a.length; r++) {
            s += a[r];
            while (s >= k) { best = Math.min(best, r - l + 1); s -= a[l++]; }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    public static void main(String[] args) {
        check(shortestSubarray(new int[]{1}, 1), 1); check(shortestSubarray(new int[]{1, 2}, 4), -1);
        check(shortestSubarray(new int[]{2, -1, 2}, 3), 3); check(shortestSubarray(new int[]{84, -37, 32, 40, 95}, 167), 3);
        check(positiveWindow(new int[]{84, -37, 32, 40, 95}, 167) != 3, true);   // the plain window gets this wrong
        Random r = new Random(11);
        for (int t = 0; t < 3000; t++) {
            int[] a = new int[r.nextInt(15) + 1];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(21) - 8;
            int k = r.nextInt(20) + 1;
            check(shortestSubarray(a, k), brute(a, k));
        }
        System.out.println("ShortestSubarrayWithSumAtLeastK: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
