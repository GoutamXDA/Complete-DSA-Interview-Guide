import java.util.*;

/**
 * LC 1723 — Find Minimum Time to Finish All Jobs. Explanation: MinimumTimeToFinishAllJobs.md
 * 1. Brute force: every job picks a worker          O(k^n)
 * 2. Pruned backtracking (sort desc, bound, symmetry)
 * 3. Binary search on the answer + feasibility backtracking
 * 4. Bitmask DP over workers and submasks           O(k * 3^n)
 */
public class MinimumTimeToFinishAllJobs {

    static int brute(int[] jobs, int k) { return all(jobs, 0, new int[k]); }

    static int all(int[] jobs, int i, int[] load) {
        if (i == jobs.length) return Arrays.stream(load).max().getAsInt();
        int best = Integer.MAX_VALUE;
        for (int w = 0; w < load.length; w++) { load[w] += jobs[i]; best = Math.min(best, all(jobs, i + 1, load)); load[w] -= jobs[i]; }
        return best;
    }

    static int best;

    static int backtracking(int[] jobsIn, int k) {
        int[] jobs = sortedDesc(jobsIn);
        best = Arrays.stream(jobs).sum();
        bt(jobs, 0, new int[k], 0);
        return best;
    }

    static void bt(int[] jobs, int i, int[] load, int curMax) {
        if (curMax >= best) return;                               // bound
        if (i == jobs.length) { best = curMax; return; }
        Set<Integer> seen = new HashSet<>();
        for (int w = 0; w < load.length; w++) {
            if (!seen.add(load[w])) continue;                      // symmetric worker
            if (load[w] + jobs[i] >= best) continue;
            load[w] += jobs[i];
            bt(jobs, i + 1, load, Math.max(curMax, load[w]));
            load[w] -= jobs[i];
            if (load[w] == 0) break;                               // all remaining empties are equivalent
        }
    }

    static int binarySearch(int[] jobsIn, int k) {
        int[] jobs = sortedDesc(jobsIn);
        int lo = jobs[0], hi = Arrays.stream(jobs).sum();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (fits(jobs, 0, new int[k], mid)) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static boolean fits(int[] jobs, int i, int[] load, int cap) {
        if (i == jobs.length) return true;
        for (int w = 0; w < load.length; w++) {
            if (load[w] + jobs[i] > cap) continue;
            boolean dupLoad = false;
            for (int v = 0; v < w; v++) if (load[v] == load[w]) { dupLoad = true; break; }
            if (dupLoad) continue;
            load[w] += jobs[i];
            if (fits(jobs, i + 1, load, cap)) return true;
            load[w] -= jobs[i];
            if (load[w] == 0) break;
        }
        return false;
    }

    static int bitmaskDP(int[] jobs, int k) {
        int n = jobs.length, full = (1 << n) - 1;
        int[] sum = new int[1 << n];
        for (int m = 1; m <= full; m++) { int low = Integer.numberOfTrailingZeros(m); sum[m] = sum[m & (m - 1)] + jobs[low]; }
        int[] dp = sum.clone();                                   // one worker
        for (int w = 2; w <= k; w++) {
            int[] nd = new int[1 << n];
            for (int mask = 0; mask <= full; mask++) {
                int bestHere = dp[mask];                          // new worker gets nothing
                for (int s = mask; s > 0; s = (s - 1) & mask)
                    bestHere = Math.min(bestHere, Math.max(dp[mask ^ s], sum[s]));
                nd[mask] = bestHere;
            }
            dp = nd;
        }
        return dp[full];
    }

    static int[] sortedDesc(int[] a) {
        return Arrays.stream(a).boxed().sorted(Collections.reverseOrder()).mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        check(backtracking(new int[]{3, 2, 3}, 3), 3); check(backtracking(new int[]{1, 2, 4, 7, 8}, 2), 11);
        check(bitmaskDP(new int[]{1, 2, 4, 7, 8}, 2), 11); check(binarySearch(new int[]{1, 2, 4, 7, 8}, 2), 11);
        int[] big = {254, 256, 256, 254, 251, 256, 254, 253, 255, 251, 251, 255};
        int e = bitmaskDP(big, 10);
        check(backtracking(big, 10), e); check(binarySearch(big, 10), e);
        Random r = new Random(22);
        for (int t = 0; t < 400; t++) {
            int n = r.nextInt(7) + 1, k = r.nextInt(n) + 1;
            int[] jobs = new int[n];
            for (int i = 0; i < n; i++) jobs[i] = r.nextInt(12) + 1;
            int x = brute(jobs, k);
            check(backtracking(jobs, k), x); check(binarySearch(jobs, k), x); check(bitmaskDP(jobs, k), x);
        }
        System.out.println("MinimumTimeToFinishAllJobs: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
