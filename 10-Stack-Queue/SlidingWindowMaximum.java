import java.util.*;

/**
 * LC 239 — Sliding Window Maximum
 *   Brute : max of each window            O(n*k)
 *   Heap  : max-heap of (value, index), lazily drop out-of-window tops. O(n log n)
 *   Deque : keep indices with decreasing values; front is the max. Each index in/out once. O(n)
 *
 * Celebrity problem: knows[i][j]. Celebrity knows nobody, everybody knows them.
 *   Brute : check every person O(n^2)
 *   Two pointers elimination: if a knows b, a isn't celebrity; else b isn't. Then verify. O(n)
 */
public class SlidingWindowMaximum {

    static int[] brute(int[] a, int k) {
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i + k <= a.length; i++) {
            int m = Integer.MIN_VALUE;
            for (int j = i; j < i + k; j++) m = Math.max(m, a[j]);
            r[i] = m;
        }
        return r;
    }

    static int[] heap(int[] a, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] != x[0] ? Integer.compare(y[0], x[0]) : Integer.compare(x[1], y[1]));
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            pq.offer(new int[]{a[i], i});
            while (pq.peek()[1] <= i - k) pq.poll();
            if (i >= k - 1) r[i - k + 1] = pq.peek()[0];
        }
        return r;
    }

    static int[] maxSlidingWindow(int[] a, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();          // out of window
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();       // useless smaller elements
            dq.offerLast(i);
            if (i >= k - 1) r[i - k + 1] = a[dq.peekFirst()];
        }
        return r;
    }

    static int celebrityBrute(int[][] knows) {
        int n = knows.length;
        for (int c = 0; c < n; c++) {
            boolean ok = true;
            for (int o = 0; o < n && ok; o++) if (o != c && (knows[c][o] == 1 || knows[o][c] == 0)) ok = false;
            if (ok) return c;
        }
        return -1;
    }

    static int celebrity(int[][] knows) {
        int a = 0, b = knows.length - 1;
        while (a < b) { if (knows[a][b] == 1) a++; else b--; }
        for (int o = 0; o < knows.length; o++)
            if (o != a && (knows[a][o] == 1 || knows[o][a] == 0)) return -1;
        return a;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        String e = "[3, 3, 5, 5, 6, 7]";
        check(Arrays.toString(brute(a, 3)), e); check(Arrays.toString(heap(a, 3)), e);
        check(Arrays.toString(maxSlidingWindow(a, 3)), e);
        int[][] knows = {{0, 1, 0}, {0, 0, 0}, {0, 1, 0}};
        check(celebrityBrute(knows), 1); check(celebrity(knows), 1);
        check(celebrity(new int[][]{{0, 1}, {1, 0}}), -1);
        System.out.println("SlidingWindowMaximum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
