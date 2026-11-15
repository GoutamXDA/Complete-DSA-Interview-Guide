/**
 * "Binary search on the answer" — the most important medium/hard binary search pattern.
 * For each: brute = try every candidate answer linearly (O(range * n)); optimal = binary search (O(n log range)).
 *
 * LC 875  Koko: min speed k so sum(ceil(p/k)) <= h.                 range [1, max]
 * LC 1011 Ship within D days: min capacity, greedy day count.      range [max, sum]
 * LC 410  Split array largest sum (= Book allocation, Painter's partition): same check as 1011.
 * Aggressive cows / LC 1552: maximise min distance; greedy place.  range [1, max-min]
 * LC 1482 Min days for m bouquets of k adjacent flowers.          range [min, max]
 */
public class BinarySearchOnAnswer {

    // ---------- Koko ----------
    static long hoursNeeded(int[] piles, int k) {
        long h = 0;
        for (int p : piles) h += (p + k - 1) / k;   // ceil without floating point
        return h;
    }

    static int kokoBrute(int[] piles, int h) {
        for (int k = 1; ; k++) if (hoursNeeded(piles, k) <= h) return k;
    }

    static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (hoursNeeded(piles, mid) <= h) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    // ---------- Ship packages / Split array / Book allocation ----------
    static int partsNeeded(int[] w, long cap) {
        int parts = 1; long cur = 0;
        for (int x : w) {
            if (cur + x > cap) { parts++; cur = 0; }
            cur += x;
        }
        return parts;
    }

    static int shipWithinDays(int[] w, int days) {
        long lo = 0, hi = 0;
        for (int x : w) { lo = Math.max(lo, x); hi += x; }
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (partsNeeded(w, mid) <= days) hi = mid; else lo = mid + 1;
        }
        return (int) lo;
    }

    static int splitArray(int[] nums, int k) { return shipWithinDays(nums, k); } // identical problem

    // ---------- Aggressive cows ----------
    static boolean canPlace(int[] stalls, int cows, int dist) {
        int placed = 1, last = stalls[0];
        for (int i = 1; i < stalls.length; i++)
            if (stalls[i] - last >= dist) { placed++; last = stalls[i]; if (placed >= cows) return true; }
        return placed >= cows;
    }

    static int aggressiveCows(int[] stalls, int cows) {
        int[] s = stalls.clone(); java.util.Arrays.sort(s);
        int lo = 1, hi = s[s.length - 1] - s[0], ans = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canPlace(s, cows, mid)) { ans = mid; lo = mid + 1; } else hi = mid - 1; // maximise
        }
        return ans;
    }

    // ---------- Bouquets ----------
    static int minDays(int[] bloom, int m, int k) {
        if ((long) m * k > bloom.length) return -1;
        int lo = Integer.MAX_VALUE, hi = 0;
        for (int b : bloom) { lo = Math.min(lo, b); hi = Math.max(hi, b); }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            int bouquets = 0, run = 0;
            for (int b : bloom) {
                if (b <= mid) { if (++run == k) { bouquets++; run = 0; } } else run = 0;
            }
            if (bouquets >= m) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        check(kokoBrute(new int[]{3, 6, 7, 11}, 8), 4); check(minEatingSpeed(new int[]{3, 6, 7, 11}, 8), 4);
        check(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5), 30);
        check(shipWithinDays(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5), 15);
        check(splitArray(new int[]{7, 2, 5, 10, 8}, 2), 18);
        check(aggressiveCows(new int[]{1, 2, 4, 8, 9}, 3), 3);
        check(minDays(new int[]{1, 10, 3, 10, 2}, 3, 1), 3); check(minDays(new int[]{1, 10, 3, 10, 2}, 3, 2), -1);
        System.out.println("BinarySearchOnAnswer: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
