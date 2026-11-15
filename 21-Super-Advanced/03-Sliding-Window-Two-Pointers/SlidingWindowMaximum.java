import java.util.*;

/**
 * LC 239 — Sliding Window Maximum. Explanation: SlidingWindowMaximum.md
 * 1. Brute force                         O(n k)
 * 2. Max-heap + lazy deletion            O(n log n)
 * 3. TreeMap multiset                    O(n log k)
 * 4. Monotonic deque                     O(n)
 * 5. Block prefix / suffix maxima        O(n)
 */
public class SlidingWindowMaximum {

    static int[] brute(int[] a, int k) {
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < r.length; i++) { int m = Integer.MIN_VALUE; for (int j = i; j < i + k; j++) m = Math.max(m, a[j]); r[i] = m; }
        return r;
    }

    static int[] heap(int[] a, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] - x[0]);
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            pq.offer(new int[]{a[i], i});
            while (pq.peek()[1] <= i - k) pq.poll();
            if (i >= k - 1) r[i - k + 1] = pq.peek()[0];
        }
        return r;
    }

    static int[] treeMap(int[] a, int k) {
        TreeMap<Integer, Integer> tm = new TreeMap<>();
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            tm.merge(a[i], 1, Integer::sum);
            if (i >= k && tm.merge(a[i - k], -1, Integer::sum) == 0) tm.remove(a[i - k]);
            if (i >= k - 1) r[i - k + 1] = tm.lastKey();
        }
        return r;
    }

    static int[] deque(int[] a, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        int[] r = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            if (!dq.isEmpty() && dq.peekFirst() == i - k) dq.pollFirst();
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) r[i - k + 1] = a[dq.peekFirst()];
        }
        return r;
    }

    static int[] blocks(int[] a, int k) {
        int n = a.length;
        int[] pre = new int[n], suf = new int[n], r = new int[n - k + 1];
        for (int i = 0; i < n; i++) pre[i] = (i % k == 0) ? a[i] : Math.max(pre[i - 1], a[i]);
        for (int i = n - 1; i >= 0; i--) suf[i] = (i == n - 1 || (i + 1) % k == 0) ? a[i] : Math.max(suf[i + 1], a[i]);
        for (int i = 0; i + k <= n; i++) r[i] = Math.max(suf[i], pre[i + k - 1]);
        return r;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        check(Arrays.toString(deque(a, 3)), "[3, 3, 5, 5, 6, 7]");
        Random r = new Random(10);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(20) + 1, k = r.nextInt(n) + 1;
            int[] x = new int[n];
            for (int i = 0; i < n; i++) x[i] = r.nextInt(11) - 5;
            String e = Arrays.toString(brute(x, k));
            check(Arrays.toString(heap(x, k)), e); check(Arrays.toString(treeMap(x, k)), e);
            check(Arrays.toString(deque(x, k)), e); check(Arrays.toString(blocks(x, k)), e);
        }
        System.out.println("SlidingWindowMaximum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
