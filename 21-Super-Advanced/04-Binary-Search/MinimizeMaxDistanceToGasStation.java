import java.util.*;

/**
 * LC 774 — Minimize Max Distance to Gas Station. Explanation: MinimizeMaxDistanceToGasStation.md
 * 1. Greedy with linear scan for the largest piece    O(k n)
 * 2. Greedy with a max-heap                           O(n + k log n)
 * 3. Binary search on the real-valued answer          O(n log(W/eps))
 * 4. Hybrid: proportional allocation + heap greedy    O(n log n)
 */
public class MinimizeMaxDistanceToGasStation {

    static double greedyScan(int[] st, int k) {
        int n = st.length - 1;
        int[] added = new int[n];
        for (int t = 0; t < k; t++) {
            int best = 0;
            for (int i = 1; i < n; i++)
                if ((double) gap(st, i) / (added[i] + 1) > (double) gap(st, best) / (added[best] + 1)) best = i;
            added[best]++;
        }
        double m = 0;
        for (int i = 0; i < n; i++) m = Math.max(m, (double) gap(st, i) / (added[i] + 1));
        return m;
    }

    static int gap(int[] st, int i) { return st[i + 1] - st[i]; }

    static double heap(int[] st, int k) { return heapFrom(st, k, new int[st.length - 1]); }

    static double heapFrom(int[] st, int k, int[] added) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Double.compare((double) gap(st, b) / (added[b] + 1), (double) gap(st, a) / (added[a] + 1)));
        for (int i = 0; i < added.length; i++) pq.offer(i);
        while (k-- > 0) { int i = pq.poll(); added[i]++; pq.offer(i); }
        int i = pq.peek();
        return (double) gap(st, i) / (added[i] + 1);
    }

    static double binarySearch(int[] st, int k) {
        double lo = 0, hi = 0;
        for (int i = 0; i + 1 < st.length; i++) hi = Math.max(hi, gap(st, i));
        for (int it = 0; it < 100; it++) {
            double mid = (lo + hi) / 2;
            if (need(st, mid) <= k) hi = mid; else lo = mid;
        }
        return hi;
    }

    static long need(int[] st, double d) {
        long cnt = 0;
        for (int i = 0; i + 1 < st.length; i++) cnt += (long) Math.ceil(gap(st, i) / d) - 1;
        return cnt;
    }

    static double hybrid(int[] st, int k) {
        int n = st.length - 1;
        long total = st[n] - st[0];
        int[] added = new int[n];
        int used = 0;
        for (int i = 0; i < n; i++) { added[i] = (int) ((long) gap(st, i) * k / total); used += added[i]; }
        return heapFrom(st, k - used, added);               // at most n stations left
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        check(Math.abs(binarySearch(a, 9) - 0.5) < 1e-6, true); check(Math.abs(heap(a, 9) - 0.5) < 1e-12, true);
        int[] b = {23, 24, 36, 39, 46, 56, 57, 65, 84, 98};
        check(Math.abs(binarySearch(b, 1) - 14.0) < 1e-6, true);
        Random r = new Random(19);
        for (int t = 0; t < 500; t++) {
            int n = r.nextInt(6) + 2;
            int[] st = new int[n];
            st[0] = r.nextInt(5);
            for (int i = 1; i < n; i++) st[i] = st[i - 1] + r.nextInt(20) + 1;
            int k = r.nextInt(25) + 1;
            double e = greedyScan(st, k);
            check(Math.abs(heap(st, k) - e) < 1e-9, true);
            check(Math.abs(hybrid(st, k) - e) < 1e-9, true);
            check(Math.abs(binarySearch(st, k) - e) < 1e-6, true);
        }
        System.out.println("MinimizeMaxDistanceToGasStation: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
