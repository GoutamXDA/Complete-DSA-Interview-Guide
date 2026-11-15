import java.util.*;

/**
 * LC 1029 Two city scheduling (n to A, n to B): DP over (people, sentToA) O(n^2) | sort by costA - costB, first half to A. O(n log n)
 * LC 646  Maximum length of pair chain: LIS-style DP O(n^2) | sort by end, greedy activity selection O(n log n)
 * LC 1899 Merge triplets to form target: ignore triplets exceeding target anywhere; check each target coordinate is hit. O(n)
 * LC 991  Broken calculator (x2 or -1 from start to target): BFS O(range) | work backwards: target even -> /2 else +1. O(log target)
 * Shortest job first (GFG): non-preemptive SJF with all arrivals at 0: sort bursts, average waiting = sum of prefix sums / n.
 * LC 406  Queue reconstruction by height: sort h asc, k desc and put each into the k-th empty slot O(n^2)
 *         | sort h desc, k asc and insert at index k O(n^2), simpler.
 * LC 1326 Minimum taps to water the garden: DP dp[i] = min taps to cover [0, i] O(n * range)
 *         | build maxReach[left] then jump game II O(n)
 */
public class GreedyProblemsII {

    static int twoCitySchedDP(int[][] c) {
        int m = c.length, n = m / 2;
        int[][] dp = new int[m + 1][n + 1];       // dp[i][a] = min cost for first i people with a sent to A
        for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
        dp[0][0] = 0;
        for (int i = 1; i <= m; i++)
            for (int a = 0; a <= Math.min(i, n); a++) {
                int b = i - a;
                if (b > n) continue;
                if (a > 0) dp[i][a] = Math.min(dp[i][a], dp[i - 1][a - 1] + c[i - 1][0]);
                if (b > 0) dp[i][a] = Math.min(dp[i][a], dp[i - 1][a] + c[i - 1][1]);
            }
        return dp[m][n];
    }

    static int twoCitySchedCost(int[][] costs) {
        int[][] c = costs.clone();
        Arrays.sort(c, Comparator.comparingInt(x -> x[0] - x[1]));   // most "A-favouring" first
        int total = 0, n = c.length / 2;
        for (int i = 0; i < c.length; i++) total += i < n ? c[i][0] : c[i][1];
        return total;
    }

    static int findLongestChainDP(int[][] pairs) {
        int[][] p = pairs.clone();
        Arrays.sort(p, Comparator.comparingInt(x -> x[0]));
        int[] dp = new int[p.length];
        int best = 0;
        for (int i = 0; i < p.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (p[j][1] < p[i][0]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    static int findLongestChain(int[][] pairs) {
        int[][] p = pairs.clone();
        Arrays.sort(p, Comparator.comparingInt(x -> x[1]));
        int count = 0, end = Integer.MIN_VALUE;
        for (int[] x : p) if (x[0] > end) { count++; end = x[1]; }
        return count;
    }

    static boolean mergeTriplets(int[][] triplets, int[] target) {
        boolean[] hit = new boolean[3];
        for (int[] t : triplets) {
            if (t[0] > target[0] || t[1] > target[1] || t[2] > target[2]) continue;   // would overshoot via max
            for (int i = 0; i < 3; i++) if (t[i] == target[i]) hit[i] = true;
        }
        return hit[0] && hit[1] && hit[2];
    }

    static int brokenCalcBFS(int start, int target) {
        int limit = 2 * Math.max(start, target) + 2;
        int[] dist = new int[limit + 1];
        Arrays.fill(dist, -1);
        dist[start] = 0;
        Queue<Integer> q = new ArrayDeque<>(List.of(start));
        while (!q.isEmpty()) {
            int u = q.poll();
            if (u == target) return dist[u];
            for (int v : new int[]{u * 2, u - 1}) if (v >= 1 && v <= limit && dist[v] == -1) { dist[v] = dist[u] + 1; q.offer(v); }
        }
        return -1;
    }

    static int brokenCalc(int start, int target) {
        int ops = 0;
        while (target > start) { target = target % 2 == 0 ? target / 2 : target + 1; ops++; }
        return ops + start - target;           // remaining gap closed by decrements
    }

    /** Shortest job first: average waiting time (floor), all jobs arrive at time 0. */
    static int shortestJobFirst(int[] bursts) {
        int[] b = bursts.clone(); Arrays.sort(b);
        long wait = 0, t = 0;
        for (int x : b) { wait += t; t += x; }
        return (int) (wait / b.length);
    }

    static int[][] reconstructQueueSlots(int[][] people) {
        int[][] p = people.clone();
        Arrays.sort(p, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);   // shortest first
        int[][] res = new int[p.length][];
        for (int[] x : p) {
            int empty = x[1];          // need exactly k taller-or-equal people in front -> k-th empty slot
            for (int i = 0; i < res.length; i++) {
                if (res[i] != null) continue;
                if (empty-- == 0) { res[i] = x; break; }
            }
        }
        return res;
    }

    static int[][] reconstructQueue(int[][] people) {
        int[][] p = people.clone();
        Arrays.sort(p, (a, b) -> a[0] != b[0] ? b[0] - a[0] : a[1] - b[1]);   // tallest first
        List<int[]> res = new ArrayList<>();
        for (int[] x : p) res.add(x[1], x);   // shorter people inserted later never disturb taller ones' k
        return res.toArray(new int[0][]);
    }

    static int minTapsDP(int n, int[] ranges) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for (int i = 0; i <= n; i++) {
            int l = Math.max(0, i - ranges[i]), r = Math.min(n, i + ranges[i]);
            for (int j = l + 1; j <= r; j++) dp[j] = Math.min(dp[j], dp[l] + 1);
        }
        return dp[n] >= Integer.MAX_VALUE / 2 ? -1 : dp[n];
    }

    static int minTaps(int n, int[] ranges) {
        int[] reach = new int[n + 1];               // reach[l] = farthest right end of a tap starting at l
        for (int i = 0; i <= n; i++) {
            int l = Math.max(0, i - ranges[i]);
            reach[l] = Math.max(reach[l], Math.min(n, i + ranges[i]));
        }
        int taps = 0, curEnd = 0, far = 0;
        for (int i = 0; i < n; i++) {               // jump game II
            far = Math.max(far, reach[i]);
            if (i == curEnd) {
                if (far <= i) return -1;
                taps++; curEnd = far;
            }
        }
        return taps;
    }

    public static void main(String[] args) {
        int[][] c = {{10, 20}, {30, 200}, {400, 50}, {30, 20}};
        check(twoCitySchedCost(c), 110); check(twoCitySchedDP(c), 110);
        int[][] c2 = {{259, 770}, {448, 54}, {926, 667}, {184, 139}, {840, 118}, {577, 469}};
        check(twoCitySchedCost(c2), 1859); check(twoCitySchedDP(c2), 1859);
        check(findLongestChain(new int[][]{{1, 2}, {2, 3}, {3, 4}}), 2); check(findLongestChainDP(new int[][]{{1, 2}, {7, 8}, {4, 5}}), 3);
        check(mergeTriplets(new int[][]{{2, 5, 3}, {1, 8, 4}, {1, 7, 5}}, new int[]{2, 7, 5}), true);
        check(mergeTriplets(new int[][]{{3, 4, 5}, {4, 5, 6}}, new int[]{3, 2, 5}), false);
        for (int s = 1; s <= 12; s++) for (int t = 1; t <= 30; t++) check(brokenCalc(s, t), brokenCalcBFS(s, t));
        check(brokenCalc(5, 8), 2); check(brokenCalc(3, 10), 3);
        check(shortestJobFirst(new int[]{4, 3, 7, 1, 2}), 4); check(shortestJobFirst(new int[]{1, 2, 3, 4}), 2);
        int[][] people = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
        String q = "[[5, 0], [7, 0], [5, 2], [6, 1], [4, 4], [7, 1]]";
        check(Arrays.deepToString(reconstructQueue(people)), q); check(Arrays.deepToString(reconstructQueueSlots(people)), q);
        check(minTaps(5, new int[]{3, 4, 1, 1, 0, 0}), 1); check(minTaps(3, new int[]{0, 0, 0, 0}), -1);
        Random rnd = new Random(2);
        for (int t = 0; t < 300; t++) {
            int n = rnd.nextInt(10) + 1;
            int[] r = new int[n + 1];
            for (int i = 0; i <= n; i++) r[i] = rnd.nextInt(4);
            check(minTaps(n, r), minTapsDP(n, r));
        }
        System.out.println("GreedyProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
