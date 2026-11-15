/**
 * LC 53 — Maximum Subarray sum.
 *
 * Approach 1 Brute  : all (i, j), sum each.                    O(n^3)
 * Approach 2 Better : all i, extend j with running sum.        O(n^2)
 * Approach 3 Kadane : best ending at i = max(a[i], bestEnding + a[i]).  O(n), O(1)
 * Approach 4 Divide & conquer: max(left, right, crossing).    O(n log n)  (follow-up asked by LC)
 *
 * Follow-ups: print the subarray (track start index), max circular subarray (LC 918):
 *   answer = max(maxKadane, total - minKadane), unless all negative.
 */
public class KadaneMaxSubarray {

    static int brute(int[] a) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++)
            for (int j = i; j < a.length; j++) {
                int s = 0;
                for (int k = i; k <= j; k++) s += a[k];
                best = Math.max(best, s);
            }
        return best;
    }

    static int better(int[] a) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            int s = 0;
            for (int j = i; j < a.length; j++) { s += a[j]; best = Math.max(best, s); }
        }
        return best;
    }

    static int kadane(int[] a) {
        int best = a[0], cur = a[0];
        for (int i = 1; i < a.length; i++) {
            cur = Math.max(a[i], cur + a[i]); // extend or restart
            best = Math.max(best, cur);
        }
        return best;
    }

    /** Returns {sum, start, end}. */
    static int[] kadaneWithIndices(int[] a) {
        int best = a[0], cur = a[0], start = 0, bs = 0, be = 0;
        for (int i = 1; i < a.length; i++) {
            if (cur < 0) { cur = a[i]; start = i; } else cur += a[i];
            if (cur > best) { best = cur; bs = start; be = i; }
        }
        return new int[]{best, bs, be};
    }

    static int divideConquer(int[] a, int l, int r) {
        if (l == r) return a[l];
        int m = (l + r) / 2;
        int leftBest = Integer.MIN_VALUE, s = 0;
        for (int i = m; i >= l; i--) { s += a[i]; leftBest = Math.max(leftBest, s); }
        int rightBest = Integer.MIN_VALUE; s = 0;
        for (int i = m + 1; i <= r; i++) { s += a[i]; rightBest = Math.max(rightBest, s); }
        return Math.max(Math.max(divideConquer(a, l, m), divideConquer(a, m + 1, r)), leftBest + rightBest);
    }

    /** LC 918 */
    static int maxCircular(int[] a) {
        int total = 0, curMax = 0, maxS = a[0], curMin = 0, minS = a[0];
        for (int x : a) {
            curMax = Math.max(curMax + x, x); maxS = Math.max(maxS, curMax);
            curMin = Math.min(curMin + x, x); minS = Math.min(minS, curMin);
            total += x;
        }
        return maxS > 0 ? Math.max(maxS, total - minS) : maxS;
    }

    public static void main(String[] args) {
        int[] a = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        check(brute(a), 6); check(better(a), 6); check(kadane(a), 6);
        check(divideConquer(a, 0, a.length - 1), 6);
        int[] r = kadaneWithIndices(a); check(r[0] + "," + r[1] + "," + r[2], "6,3,6");
        check(kadane(new int[]{-3, -1, -2}), -1);
        check(maxCircular(new int[]{5, -3, 5}), 10); check(maxCircular(new int[]{-3, -2, -3}), -2);
        System.out.println("KadaneMaxSubarray: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
