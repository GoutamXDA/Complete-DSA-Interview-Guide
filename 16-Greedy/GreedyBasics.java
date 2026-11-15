import java.util.*;

/**
 * LC 455 Assign cookies: smallest cookie that satisfies the least greedy child. O(n log n)
 * LC 860 Lemonade change: for $20 prefer giving 10+5 (keeps more $5s).
 * Fractional knapsack: sort by value/weight desc; take fractions. O(n log n)
 * Min coins (Indian/US denominations are canonical => greedy is optimal; NOT for arbitrary coins).
 * Job sequencing: sort by profit desc; put each job in the latest free slot <= deadline.
 *   Naive slot scan O(n * maxDeadline) | DSU "next free slot" O(n α(n)).
 * LC 135 Candy: brute repeat until stable O(n^2) | left pass + right pass O(n)
 * LC 678 Valid parenthesis string with '*': brute try 3 options O(3^n) | track range [lo, hi] of open count O(n)
 */
public class GreedyBasics {

    static int findContentChildren(int[] g, int[] s) {
        int[] a = g.clone(), b = s.clone(); Arrays.sort(a); Arrays.sort(b);
        int i = 0;
        for (int j = 0; j < b.length && i < a.length; j++) if (b[j] >= a[i]) i++;
        return i;
    }

    static boolean lemonadeChange(int[] bills) {
        int five = 0, ten = 0;
        for (int b : bills) {
            if (b == 5) five++;
            else if (b == 10) { if (five == 0) return false; five--; ten++; }
            else if (ten > 0 && five > 0) { ten--; five--; }
            else if (five >= 3) five -= 3;
            else return false;
        }
        return true;
    }

    static double fractionalKnapsack(int[] val, int[] wt, int W) {
        Integer[] idx = new Integer[val.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Double.compare((double) val[b] / wt[b], (double) val[a] / wt[a]));
        double total = 0; int cap = W;
        for (int i : idx) {
            if (cap == 0) break;
            int take = Math.min(cap, wt[i]);
            total += (double) val[i] * take / wt[i];
            cap -= take;
        }
        return total;
    }

    static List<Integer> minCoins(int amount, int[] coinsDesc) {
        List<Integer> res = new ArrayList<>();
        for (int c : coinsDesc) while (amount >= c) { amount -= c; res.add(c); }
        return res;
    }

    /** jobs: {id, deadline, profit}. Returns {count, profit}. */
    static int[] jobSequencing(int[][] jobs) {
        int[][] j = jobs.clone();
        Arrays.sort(j, (a, b) -> b[2] - a[2]);
        int maxD = 0;
        for (int[] x : j) maxD = Math.max(maxD, x[1]);
        int[] parent = new int[maxD + 1];              // DSU: parent[t] = latest free slot <= t (0 = none)
        for (int i = 0; i <= maxD; i++) parent[i] = i;
        int count = 0, profit = 0;
        for (int[] x : j) {
            int slot = find(parent, x[1]);
            if (slot > 0) { count++; profit += x[2]; parent[slot] = slot - 1; }
        }
        return new int[]{count, profit};
    }

    static int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }

    static int candyBrute(int[] r) {
        int n = r.length; int[] c = new int[n]; Arrays.fill(c, 1);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (i > 0 && r[i] > r[i - 1] && c[i] <= c[i - 1]) { c[i] = c[i - 1] + 1; changed = true; }
                if (i < n - 1 && r[i] > r[i + 1] && c[i] <= c[i + 1]) { c[i] = c[i + 1] + 1; changed = true; }
            }
        }
        return Arrays.stream(c).sum();
    }

    static int candy(int[] r) {
        int n = r.length; int[] c = new int[n]; Arrays.fill(c, 1);
        for (int i = 1; i < n; i++) if (r[i] > r[i - 1]) c[i] = c[i - 1] + 1;
        for (int i = n - 2; i >= 0; i--) if (r[i] > r[i + 1]) c[i] = Math.max(c[i], c[i + 1] + 1);
        return Arrays.stream(c).sum();
    }

    static boolean checkValidString(String s) {
        int lo = 0, hi = 0;   // min and max possible open brackets
        for (char ch : s.toCharArray()) {
            if (ch == '(') { lo++; hi++; }
            else if (ch == ')') { lo--; hi--; }
            else { lo--; hi++; }
            if (hi < 0) return false;
            lo = Math.max(lo, 0);
        }
        return lo == 0;
    }

    public static void main(String[] args) {
        check(findContentChildren(new int[]{1, 2, 3}, new int[]{1, 1}), 1); check(findContentChildren(new int[]{1, 2}, new int[]{1, 2, 3}), 2);
        check(lemonadeChange(new int[]{5, 5, 5, 10, 20}), true); check(lemonadeChange(new int[]{5, 5, 10, 10, 20}), false);
        check(fractionalKnapsack(new int[]{60, 100, 120}, new int[]{10, 20, 30}, 50), 240.0);
        check(minCoins(49, new int[]{2000, 500, 200, 100, 50, 20, 10, 5, 2, 1}), List.of(20, 20, 5, 2, 2));
        int[] js = jobSequencing(new int[][]{{1, 4, 20}, {2, 1, 10}, {3, 1, 40}, {4, 1, 30}});
        check(js[0] + "," + js[1], "2,60");
        check(candyBrute(new int[]{1, 0, 2}), 5); check(candy(new int[]{1, 0, 2}), 5); check(candy(new int[]{1, 2, 2}), 4);
        check(checkValidString("(*))"), true); check(checkValidString("(((*)"), false);
        System.out.println("GreedyBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
