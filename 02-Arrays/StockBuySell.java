/**
 * Stock problems family.
 *
 * LC 121 (one transaction)
 *   Brute  : every buy/sell pair                O(n^2)
 *   Optimal: track min price so far, best diff  O(n), O(1)
 * LC 122 (unlimited transactions)
 *   Greedy : add every positive day-to-day gain O(n)
 * LC 123 / 188 (at most k transactions) and cooldown/fee variants -> see 15-Dynamic-Programming
 */
public class StockBuySell {

    static int maxProfitBrute(int[] p) {
        int best = 0;
        for (int i = 0; i < p.length; i++)
            for (int j = i + 1; j < p.length; j++) best = Math.max(best, p[j] - p[i]);
        return best;
    }

    static int maxProfit(int[] p) {
        int min = Integer.MAX_VALUE, best = 0;
        for (int x : p) {
            min = Math.min(min, x);
            best = Math.max(best, x - min);
        }
        return best;
    }

    static int maxProfitUnlimited(int[] p) {
        int profit = 0;
        for (int i = 1; i < p.length; i++) if (p[i] > p[i - 1]) profit += p[i] - p[i - 1];
        return profit;
    }

    public static void main(String[] args) {
        int[] p = {7, 1, 5, 3, 6, 4};
        check(maxProfitBrute(p), 5); check(maxProfit(p), 5);
        check(maxProfit(new int[]{7, 6, 4, 3, 1}), 0);
        check(maxProfitUnlimited(p), 7);
        System.out.println("StockBuySell: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
