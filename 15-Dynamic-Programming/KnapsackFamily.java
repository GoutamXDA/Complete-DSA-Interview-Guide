import java.util.*;

/**
 * Knapsack family.
 *
 * Subset sum / LC 416 partition: dp[s] = can we make sum s. 0/1 -> iterate s DOWN. O(n*S)
 * Count subsets with sum K: dp[s] += dp[s - x]. (Zeros handled naturally by the backward loop.)
 * LC 494 Target sum: P - N = target, P + N = total -> count subsets with sum (total + target) / 2.
 * 0/1 knapsack: recursion O(2^n) -> memo O(nW) -> table -> 1D backwards.
 * Unbounded knapsack / rod cutting: iterate capacity FORWARDS (an item can be reused).
 * LC 322 Coin change (min coins): dp[a] = 1 + min(dp[a - c]).
 * LC 518 Coin change II (count combinations): coins outer loop, amount inner (else you count permutations).
 */
public class KnapsackFamily {

    static boolean subsetSumRec(int[] a, int i, int s) {
        if (s == 0) return true;
        if (i < 0 || s < 0) return false;
        return subsetSumRec(a, i - 1, s - a[i]) || subsetSumRec(a, i - 1, s);
    }

    static boolean subsetSum(int[] a, int target) {
        boolean[] dp = new boolean[target + 1]; dp[0] = true;
        for (int x : a) for (int s = target; s >= x; s--) dp[s] |= dp[s - x];
        return dp[target];
    }

    static boolean canPartition(int[] a) {
        int total = Arrays.stream(a).sum();
        return total % 2 == 0 && subsetSum(a, total / 2);
    }

    static int countSubsets(int[] a, int k) {
        int[] dp = new int[k + 1]; dp[0] = 1;
        for (int x : a) for (int s = k; s >= x; s--) dp[s] += dp[s - x];
        return dp[k];
    }

    static int findTargetSumWays(int[] a, int target) {
        int total = Arrays.stream(a).sum();
        if (Math.abs(target) > total || (total + target) % 2 != 0) return 0;
        return countSubsets(a, (total + target) / 2);
    }

    static int knapsackRec(int[] wt, int[] val, int i, int cap) {
        if (i < 0 || cap == 0) return 0;
        int skip = knapsackRec(wt, val, i - 1, cap);
        int take = wt[i] <= cap ? val[i] + knapsackRec(wt, val, i - 1, cap - wt[i]) : 0;
        return Math.max(skip, take);
    }

    static int knapsackMemo(int[] wt, int[] val, int i, int cap, int[][] memo) {
        if (i < 0 || cap == 0) return 0;
        if (memo[i][cap] != -1) return memo[i][cap];
        int skip = knapsackMemo(wt, val, i - 1, cap, memo);
        int take = wt[i] <= cap ? val[i] + knapsackMemo(wt, val, i - 1, cap - wt[i], memo) : 0;
        return memo[i][cap] = Math.max(skip, take);
    }

    static int knapsackTable(int[] wt, int[] val, int W) {
        int n = wt.length;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++)
            for (int c = 0; c <= W; c++) {
                dp[i][c] = dp[i - 1][c];
                if (wt[i - 1] <= c) dp[i][c] = Math.max(dp[i][c], val[i - 1] + dp[i - 1][c - wt[i - 1]]);
            }
        return dp[n][W];
    }

    static int knapsack(int[] wt, int[] val, int W) {
        int[] dp = new int[W + 1];
        for (int i = 0; i < wt.length; i++) for (int c = W; c >= wt[i]; c--) dp[c] = Math.max(dp[c], val[i] + dp[c - wt[i]]);
        return dp[W];
    }

    static int unboundedKnapsack(int[] wt, int[] val, int W) {
        int[] dp = new int[W + 1];
        for (int i = 0; i < wt.length; i++) for (int c = wt[i]; c <= W; c++) dp[c] = Math.max(dp[c], val[i] + dp[c - wt[i]]);
        return dp[W];
    }

    /** Rod cutting: piece length i+1 sells for price[i]; = unbounded knapsack with wt = length. */
    static int rodCutting(int[] price, int n) {
        int[] len = new int[price.length];
        for (int i = 0; i < len.length; i++) len[i] = i + 1;
        return unboundedKnapsack(len, price, n);
    }

    static int coinChangeRec(int[] coins, int amt, Map<Integer, Integer> memo) {
        if (amt == 0) return 0;
        if (amt < 0) return -1;
        if (memo.containsKey(amt)) return memo.get(amt);
        int best = Integer.MAX_VALUE;
        for (int c : coins) { int r = coinChangeRec(coins, amt - c, memo); if (r >= 0) best = Math.min(best, r + 1); }
        memo.put(amt, best == Integer.MAX_VALUE ? -1 : best);
        return memo.get(amt);
    }

    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); dp[0] = 0;            // amount+1 acts as infinity
        for (int a = 1; a <= amount; a++) for (int c : coins) if (c <= a) dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1]; dp[0] = 1;
        for (int c : coins) for (int a = c; a <= amount; a++) dp[a] += dp[a - c];
        return dp[amount];
    }

    public static void main(String[] args) {
        int[] s = {3, 34, 4, 12, 5, 2};
        check(subsetSumRec(s, s.length - 1, 9), true); check(subsetSum(s, 9), true); check(subsetSum(s, 30), false);
        check(canPartition(new int[]{1, 5, 11, 5}), true); check(canPartition(new int[]{1, 2, 3, 5}), false);
        check(countSubsets(new int[]{1, 2, 3, 3}, 6), 3); check(countSubsets(new int[]{0, 0, 1}, 1), 4);
        check(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3), 5);
        int[] wt = {1, 3, 4, 5}, val = {1, 4, 5, 7};
        int[][] memo = new int[4][8]; for (int[] r : memo) Arrays.fill(r, -1);
        check(knapsackRec(wt, val, 3, 7), 9); check(knapsackMemo(wt, val, 3, 7, memo), 9);
        check(knapsackTable(wt, val, 7), 9); check(knapsack(wt, val, 7), 9);
        check(unboundedKnapsack(new int[]{2, 4, 6}, new int[]{5, 11, 13}, 10), 27);
        check(rodCutting(new int[]{1, 5, 8, 9, 10, 17, 17, 20}, 8), 22);
        check(coinChangeRec(new int[]{1, 2, 5}, 11, new HashMap<>()), 3); check(coinChange(new int[]{1, 2, 5}, 11), 3);
        check(coinChange(new int[]{2}, 3), -1);
        check(change(5, new int[]{1, 2, 5}), 4);
        System.out.println("KnapsackFamily: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
