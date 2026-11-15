import java.util.*;

/**
 * LC 630 — Course Schedule III. Explanation: CourseScheduleIII.md
 * 0. Brute force over subsets, check in deadline order (oracle)
 * 1. Sort by deadline + knapsack over time             O(n * maxDay)
 * 2. Sort by deadline + max-heap swap-out              O(n log n)
 */
public class CourseScheduleIII {

    static int[][] byDeadline(int[][] c) { int[][] s = c.clone(); Arrays.sort(s, Comparator.comparingInt(x -> x[1])); return s; }

    static int brute(int[][] courses) {
        int[][] c = byDeadline(courses);
        int n = c.length, best = 0;
        for (int mask = 0; mask < 1 << n; mask++) {
            int t = 0; boolean ok = true;
            for (int i = 0; i < n && ok; i++) if ((mask >> i & 1) == 1) { t += c[i][0]; if (t > c[i][1]) ok = false; }
            if (ok) best = Math.max(best, Integer.bitCount(mask));
        }
        return best;
    }

    static int knapsack(int[][] courses) {
        int[][] c = byDeadline(courses);
        int maxDay = 0;
        for (int[] x : c) maxDay = Math.max(maxDay, x[1]);
        int[] dp = new int[maxDay + 1];                         // dp[t] = max courses with total time exactly t (-1 = impossible)
        Arrays.fill(dp, -1);
        dp[0] = 0;
        for (int[] x : c)
            for (int t = x[1]; t >= x[0]; t--)
                if (dp[t - x[0]] >= 0) dp[t] = Math.max(dp[t], dp[t - x[0]] + 1);
        int best = 0;
        for (int v : dp) best = Math.max(best, v);
        return best;
    }

    static int scheduleCourse(int[][] courses) {
        int[][] c = byDeadline(courses);
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int time = 0;
        for (int[] x : c) {
            time += x[0]; pq.offer(x[0]);
            if (time > x[1]) time -= pq.poll();
        }
        return pq.size();
    }

    public static void main(String[] args) {
        int[][] ex = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
        check(scheduleCourse(ex), 3); check(knapsack(ex), 3);
        check(scheduleCourse(new int[][]{{1, 2}}), 1); check(scheduleCourse(new int[][]{{3, 2}, {4, 3}}), 0);
        Random r = new Random(64);
        for (int t = 0; t < 3000; t++) {
            int[][] c = new int[r.nextInt(10) + 1][];
            for (int i = 0; i < c.length; i++) c[i] = new int[]{r.nextInt(6) + 1, r.nextInt(20) + 1};
            int e = brute(c);
            check(knapsack(c), e); check(scheduleCourse(c), e);
        }
        System.out.println("CourseScheduleIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
