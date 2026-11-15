import java.util.*;

/**
 * LC 857 — Minimum Cost to Hire K Workers. Explanation: MinimumCostToHireKWorkers.md
 * 0. Exhaustive over all k-subsets (oracle)              O(C(n,k) k)
 * 1. Every captain + sort eligible qualities             O(n^2 log n)
 * 2. Sort by ratio + max-heap of qualities               O(n log n)
 */
public class MinimumCostToHireKWorkers {

    static double subsets(int[] q, int[] w, int k) {
        int n = q.length;
        double best = Double.MAX_VALUE;
        for (int mask = 0; mask < 1 << n; mask++) {
            if (Integer.bitCount(mask) != k) continue;
            double ratio = 0; long sumQ = 0;
            for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) { ratio = Math.max(ratio, (double) w[i] / q[i]); sumQ += q[i]; }
            best = Math.min(best, ratio * sumQ);
        }
        return best;
    }

    static double everyCaptain(int[] q, int[] w, int k) {
        double best = Double.MAX_VALUE;
        for (int c = 0; c < q.length; c++) {
            double ratio = (double) w[c] / q[c];
            List<Integer> others = new ArrayList<>();
            for (int i = 0; i < q.length; i++) if (i != c && (double) w[i] / q[i] <= ratio) others.add(q[i]);
            if (others.size() < k - 1) continue;
            Collections.sort(others);
            long s = q[c];
            for (int i = 0; i < k - 1; i++) s += others.get(i);
            best = Math.min(best, s * ratio);
        }
        return best;
    }

    static double mincostToHireWorkers(int[] q, int[] w, int k) {
        int n = q.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare((double) w[a] / q[a], (double) w[b] / q[b]));
        PriorityQueue<Integer> maxQ = new PriorityQueue<>(Collections.reverseOrder());
        long sumQ = 0;
        double best = Double.MAX_VALUE;
        for (int i : idx) {
            maxQ.offer(q[i]); sumQ += q[i];
            if (maxQ.size() > k) sumQ -= maxQ.poll();
            if (maxQ.size() == k) best = Math.min(best, sumQ * ((double) w[i] / q[i]));
        }
        return best;
    }

    public static void main(String[] args) {
        check(Math.abs(mincostToHireWorkers(new int[]{10, 20, 5}, new int[]{70, 50, 30}, 2) - 105.0) < 1e-9, true);
        check(Math.abs(mincostToHireWorkers(new int[]{3, 1, 10, 10, 1}, new int[]{4, 8, 2, 2, 7}, 3) - 30.66667) < 1e-5, true);
        Random r = new Random(41);
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(8) + 1, k = r.nextInt(n) + 1;
            int[] q = new int[n], w = new int[n];
            for (int i = 0; i < n; i++) { q[i] = r.nextInt(10) + 1; w[i] = r.nextInt(30) + 1; }
            double e = subsets(q, w, k);
            check(Math.abs(everyCaptain(q, w, k) - e) < 1e-9, true);
            check(Math.abs(mincostToHireWorkers(q, w, k) - e) < 1e-9, true);
        }
        System.out.println("MinimumCostToHireKWorkers: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
