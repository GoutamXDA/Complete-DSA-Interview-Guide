/**
 * Stock problems as a state machine: on each day you are either holding or not holding a share.
 *   hold[i] = max(hold[i-1], free[i-1] - price)   (buy)
 *   free[i] = max(free[i-1], hold[i-1] + price)   (sell)
 *
 * LC 121 one transaction        -> see 02-Arrays/StockBuySell (min so far)
 * LC 122 unlimited              -> state machine above
 * LC 123 at most 2 transactions -> 4 variables b1, s1, b2, s2
 * LC 188 at most k              -> buy[k], sell[k] arrays. O(nk)
 * LC 309 cooldown               -> free uses the state from 2 days ago when buying
 * LC 714 transaction fee        -> subtract fee on sell
 *
 * Recursion version (shown for LC 188): f(i, holding, k) = max(skip, buy/sell). Memo O(n*2*k).
 */
public class StockDP {

    static int unlimited(int[] p) {
        int hold = Integer.MIN_VALUE, free = 0;
        for (int x : p) { int h = Math.max(hold, free - x); free = Math.max(free, hold + x); hold = h; }
        return free;
    }

    static int atMostTwo(int[] p) {
        int b1 = Integer.MIN_VALUE, s1 = 0, b2 = Integer.MIN_VALUE, s2 = 0;
        for (int x : p) {
            b1 = Math.max(b1, -x);
            s1 = Math.max(s1, b1 + x);
            b2 = Math.max(b2, s1 - x);
            s2 = Math.max(s2, b2 + x);
        }
        return s2;
    }

    static int atMostKMemo(int[] p, int i, int holding, int k, Integer[][][] memo) {
        if (i == p.length || k == 0) return 0;
        if (memo[i][holding][k] != null) return memo[i][holding][k];
        int skip = atMostKMemo(p, i + 1, holding, k, memo);
        int act = holding == 1
                ? p[i] + atMostKMemo(p, i + 1, 0, k - 1, memo)   // sell completes a transaction
                : -p[i] + atMostKMemo(p, i + 1, 1, k, memo);     // buy
        return memo[i][holding][k] = Math.max(skip, act);
    }

    static int atMostK(int k, int[] p) {
        if (k >= p.length / 2) return unlimited(p);  // effectively unlimited
        int[] buy = new int[k + 1], sell = new int[k + 1];
        java.util.Arrays.fill(buy, Integer.MIN_VALUE);
        for (int x : p)
            for (int t = 1; t <= k; t++) {
                buy[t] = Math.max(buy[t], sell[t - 1] - x);
                sell[t] = Math.max(sell[t], buy[t] + x);
            }
        return sell[k];
    }

    static int withCooldown(int[] p) {
        int hold = Integer.MIN_VALUE, free = 0, freePrev = 0; // freePrev = free two days ago
        for (int x : p) {
            int h = Math.max(hold, freePrev - x);
            freePrev = free;
            free = Math.max(free, hold + x);
            hold = h;
        }
        return free;
    }

    static int withFee(int[] p, int fee) {
        int hold = Integer.MIN_VALUE / 2, free = 0;
        for (int x : p) { int h = Math.max(hold, free - x); free = Math.max(free, hold + x - fee); hold = h; }
        return free;
    }

    public static void main(String[] args) {
        check(unlimited(new int[]{7, 1, 5, 3, 6, 4}), 7);
        check(atMostTwo(new int[]{3, 3, 5, 0, 0, 3, 1, 4}), 6);
        int[] p = {3, 2, 6, 5, 0, 3};
        check(atMostK(2, p), 7); check(atMostKMemo(p, 0, 0, 2, new Integer[p.length][2][3]), 7);
        check(withCooldown(new int[]{1, 2, 3, 0, 2}), 3);
        check(withFee(new int[]{1, 3, 2, 8, 4, 9}, 2), 8);
        System.out.println("StockDP: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
