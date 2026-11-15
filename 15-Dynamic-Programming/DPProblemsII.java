import java.util.*;

/**
 * Ninja's training (GFG): points[day][3]; can't repeat the previous day's activity.
 *   recursion f(day, last) O(3^n) -> memo/table dp[day][last] O(n*4*3) -> two rows.
 * LC 740 Delete and earn: taking x deletes x-1 and x+1 -> bucket sums by value, then house robber over values. O(n + maxV)
 * LC 279 Perfect squares: recursion O(sqrt(n)^depth) -> unbounded knapsack dp[i] = 1 + min dp[i - j*j] O(n sqrt n) | BFS on remainders.
 * LC 377 Combination sum IV (order matters = permutations): amount OUTER loop, nums inner. Compare coin change II (combinations).
 * Count partitions with given difference (GFG): S1 - S2 = d, S1 + S2 = total -> count subsets with sum (total - d) / 2.
 * LC 474 Ones and zeroes: 0/1 knapsack with two capacities (m zeros, n ones), iterate both backwards. O(L * m * n)
 * LC 1043 Partition array for maximum sum (subarrays of length <= k): dp[i] = max over last len of dp[i-len] + len * max. O(nk)
 * LC 583 Delete operation for two strings: m + n - 2 * LCS (or direct edit-distance-style DP with only deletes).
 * LC 1048 Longest string chain: brute DFS from each word O(n * L^2 * depth) | sort by length; dp[word] = 1 + max dp[word minus one char]. O(n L^2)
 */
public class DPProblemsII {

    static int ninjaRec(int[][] p, int day, int last) {
        if (day < 0) return 0;
        int best = 0;
        for (int a = 0; a < 3; a++) if (a != last) best = Math.max(best, p[day][a] + ninjaRec(p, day - 1, a));
        return best;
    }

    static int ninjaTraining(int[][] p) {
        int[] prev = new int[4];                 // prev[last] = best up to previous day when yesterday's activity is `last`
        for (int[] day : p) {
            int[] cur = new int[4];
            for (int last = 0; last < 4; last++)
                for (int a = 0; a < 3; a++) if (a != last) cur[last] = Math.max(cur[last], day[a] + prev[a]);
            prev = cur;
        }
        return prev[3];                          // 3 = "no restriction"
    }

    static int deleteAndEarn(int[] nums) {
        int max = Arrays.stream(nums).max().getAsInt();
        int[] gain = new int[max + 1];
        for (int x : nums) gain[x] += x;
        int take = 0, skip = 0;
        for (int v = 0; v <= max; v++) { int t = skip + gain[v]; skip = Math.max(skip, take); take = t; }
        return Math.max(take, skip);
    }

    static int numSquaresRec(int n, Map<Integer, Integer> memo) {
        if (n == 0) return 0;
        if (memo.containsKey(n)) return memo.get(n);
        int best = Integer.MAX_VALUE;
        for (int j = 1; j * j <= n; j++) best = Math.min(best, 1 + numSquaresRec(n - j * j, memo));
        memo.put(n, best);
        return best;
    }

    static int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE); dp[0] = 0;
        for (int i = 1; i <= n; i++) for (int j = 1; j * j <= i; j++) dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
        return dp[n];
    }

    static int numSquaresBFS(int n) {
        boolean[] seen = new boolean[n + 1];
        Queue<Integer> q = new ArrayDeque<>(List.of(n));
        for (int steps = 1; ; steps++)
            for (int sz = q.size(); sz > 0; sz--) {
                int r = q.poll();
                for (int j = 1; j * j <= r; j++) {
                    int nr = r - j * j;
                    if (nr == 0) return steps;
                    if (!seen[nr]) { seen[nr] = true; q.offer(nr); }
                }
            }
    }

    static int combinationSum4Rec(int[] nums, int t, Integer[] memo) {
        if (t == 0) return 1;
        if (memo[t] != null) return memo[t];
        int ways = 0;
        for (int x : nums) if (x <= t) ways += combinationSum4Rec(nums, t - x, memo);
        return memo[t] = ways;
    }

    static int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1]; dp[0] = 1;
        for (int t = 1; t <= target; t++) for (int x : nums) if (x <= t) dp[t] += dp[t - x];
        return dp[target];
    }

    static int countPartitionsBrute(int[] a, int d) {
        int n = a.length, c = 0, total = Arrays.stream(a).sum();
        for (int mask = 0; mask < 1 << n; mask++) {
            int s1 = 0;
            for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) s1 += a[i];
            if (s1 - (total - s1) == d) c++;
        }
        return c;
    }

    static int countPartitions(int[] a, int d) {
        int total = Arrays.stream(a).sum();
        if (total - d < 0 || (total - d) % 2 != 0) return 0;
        int k = (total - d) / 2;
        int[] dp = new int[k + 1]; dp[0] = 1;
        for (int x : a) for (int s = k; s >= x; s--) dp[s] += dp[s - x];
        return dp[k];
    }

    static int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (String s : strs) {
            int z = 0, o = 0;
            for (char c : s.toCharArray()) if (c == '0') z++; else o++;
            for (int i = m; i >= z; i--) for (int j = n; j >= o; j--) dp[i][j] = Math.max(dp[i][j], 1 + dp[i - z][j - o]);
        }
        return dp[m][n];
    }

    static int maxSumAfterPartitioningRec(int[] a, int i, int k, Integer[] memo) {
        if (i == a.length) return 0;
        if (memo[i] != null) return memo[i];
        int best = 0, max = 0;
        for (int len = 1; len <= k && i + len <= a.length; len++) {
            max = Math.max(max, a[i + len - 1]);
            best = Math.max(best, max * len + maxSumAfterPartitioningRec(a, i + len, k, memo));
        }
        return memo[i] = best;
    }

    static int maxSumAfterPartitioning(int[] a, int k) {
        int n = a.length;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int max = 0;
            for (int len = 1; len <= k && len <= i; len++) {
                max = Math.max(max, a[i - len]);
                dp[i] = Math.max(dp[i], dp[i - len] + max * len);
            }
        }
        return dp[n];
    }

    static int minDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) for (int j = 1; j <= n; j++)
            dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
        return m + n - 2 * dp[m][n];
    }

    static int longestStrChainBrute(String[] words) {
        Set<String> set = new HashSet<>(Arrays.asList(words));
        int best = 0;
        for (String w : words) best = Math.max(best, chainDown(w, set));
        return best;
    }

    static int chainDown(String w, Set<String> set) {   // longest chain ending at w (no memo)
        int best = 1;
        for (int i = 0; i < w.length(); i++) {
            String p = w.substring(0, i) + w.substring(i + 1);
            if (set.contains(p)) best = Math.max(best, 1 + chainDown(p, set));
        }
        return best;
    }

    static int longestStrChain(String[] words) {
        String[] w = words.clone();
        Arrays.sort(w, Comparator.comparingInt(String::length));
        Map<String, Integer> dp = new HashMap<>();
        int best = 0;
        for (String s : w) {
            int cur = 1;
            for (int i = 0; i < s.length(); i++) cur = Math.max(cur, dp.getOrDefault(s.substring(0, i) + s.substring(i + 1), 0) + 1);
            dp.put(s, cur);
            best = Math.max(best, cur);
        }
        return best;
    }

    public static void main(String[] args) {
        int[][] pts = {{10, 40, 70}, {20, 50, 80}, {30, 60, 90}};
        check(ninjaRec(pts, 2, 3), 210); check(ninjaTraining(pts), 210); check(ninjaTraining(new int[][]{{1, 2, 5}, {3, 1, 1}, {3, 3, 3}}), 11);
        check(deleteAndEarn(new int[]{3, 4, 2}), 6); check(deleteAndEarn(new int[]{2, 2, 3, 3, 3, 4}), 9);
        for (int n = 1; n <= 50; n++) { int e = numSquares(n); check(numSquaresBFS(n), e); check(numSquaresRec(n, new HashMap<>()), e); }
        check(numSquares(12), 3); check(numSquares(13), 2);
        check(combinationSum4(new int[]{1, 2, 3}, 4), 7); check(combinationSum4Rec(new int[]{1, 2, 3}, 4, new Integer[5]), 7);
        check(countPartitions(new int[]{5, 2, 6, 4}, 3), 1); check(countPartitionsBrute(new int[]{5, 2, 6, 4}, 3), 1);
        check(countPartitions(new int[]{1, 1, 1, 1}, 0), 6); check(countPartitionsBrute(new int[]{1, 1, 1, 1}, 0), 6);
        check(findMaxForm(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3), 4); check(findMaxForm(new String[]{"10", "0", "1"}, 1, 1), 2);
        int[] pa = {1, 15, 7, 9, 2, 5, 10};
        check(maxSumAfterPartitioning(pa, 3), 84); check(maxSumAfterPartitioningRec(pa, 0, 3, new Integer[pa.length]), 84);
        check(minDistance("sea", "eat"), 2); check(minDistance("leetcode", "etco"), 4);
        String[] ch = {"xbc", "pcxbcf", "xb", "cxbc", "pcxbc"};
        check(longestStrChain(ch), 5); check(longestStrChainBrute(ch), 5); check(longestStrChain(new String[]{"a", "b", "ba", "bca", "bda", "bdca"}), 4);
        System.out.println("DPProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
