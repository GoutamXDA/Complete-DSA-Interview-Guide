import java.util.*;

/**
 * LC 1499 — Max Value of Equation. Explanation: MaxValueOfEquation.md
 * Rewrite: yi + yj + xj - xi = (yj + xj) + (yi - xi), i < j, xj - xi <= k.
 * 1. Brute force over pairs                    O(n^2)
 * 2. Max-heap of (y - x, x) with lazy expiry   O(n log n)
 * 3. Monotonic deque on y - x                  O(n)
 */
public class MaxValueOfEquation {

    static long brute(int[][] p, int k) {
        long best = Long.MIN_VALUE;
        for (int i = 0; i < p.length; i++)
            for (int j = i + 1; j < p.length && p[j][0] - p[i][0] <= k; j++)
                best = Math.max(best, (long) p[i][1] + p[j][1] + p[j][0] - p[i][0]);
        return best;
    }

    static long heap(int[][] p, int k) {
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(b[0], a[0]));   // {y - x, x}
        long best = Long.MIN_VALUE;
        for (int[] pt : p) {
            while (!pq.isEmpty() && pt[0] - pq.peek()[1] > k) pq.poll();
            if (!pq.isEmpty()) best = Math.max(best, pq.peek()[0] + pt[0] + pt[1]);
            pq.offer(new long[]{pt[1] - pt[0], pt[0]});
        }
        return best;
    }

    static long findMaxValueOfEquation(int[][] p, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        long best = Long.MIN_VALUE;
        for (int j = 0; j < p.length; j++) {
            while (!dq.isEmpty() && p[j][0] - p[dq.peekFirst()][0] > k) dq.pollFirst();
            if (!dq.isEmpty()) { int i = dq.peekFirst(); best = Math.max(best, (long) p[j][1] + p[j][0] + p[i][1] - p[i][0]); }
            while (!dq.isEmpty() && p[dq.peekLast()][1] - p[dq.peekLast()][0] <= p[j][1] - p[j][0]) dq.pollLast();
            dq.offerLast(j);
        }
        return best;
    }

    public static void main(String[] args) {
        int[][] a = {{1, 3}, {2, 0}, {5, 10}, {6, -10}};
        check(findMaxValueOfEquation(a, 1), 4L); check(heap(a, 1), 4L);
        check(findMaxValueOfEquation(new int[][]{{0, 0}, {3, 0}, {9, 2}}, 3), 3L);
        Random r = new Random(14);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(12) + 2;
            int[][] p = new int[n][2];
            int x = r.nextInt(5);
            for (int i = 0; i < n; i++) { x += r.nextInt(3) + 1; p[i] = new int[]{x, r.nextInt(21) - 10}; }
            int k = r.nextInt(6) + 1;
            long e = brute(p, k);
            if (e == Long.MIN_VALUE) continue;                 // no valid pair
            check(findMaxValueOfEquation(p, k), e); check(heap(p, k), e);
        }
        System.out.println("MaxValueOfEquation: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
