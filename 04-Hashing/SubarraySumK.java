import java.util.*;

/**
 * The prefix-sum + HashMap family. Works with NEGATIVE numbers (sliding window doesn't).
 *
 * LC 560 Count subarrays with sum K
 *   Brute  : all (i,j) with inner sum     O(n^3)
 *   Better : all i, running sum over j    O(n^2)
 *   Optimal: prefix sums; #subarrays ending at j = count[prefix - k]. O(n), O(n)
 *
 * Longest subarray with sum K: store FIRST index of each prefix sum (don't overwrite). O(n)
 *   (If all elements are positive, a sliding window gives O(1) space.)
 *
 * Count subarrays with XOR K: same idea with prefix XOR, look up prefix ^ k.
 *
 * LC 525 Contiguous array: map 0 -> -1, find longest subarray with sum 0.
 */
public class SubarraySumK {

    static int countBrute(int[] a, int k) {
        int c = 0;
        for (int i = 0; i < a.length; i++) {
            int s = 0;
            for (int j = i; j < a.length; j++) { s += a[j]; if (s == k) c++; }
        }
        return c;
    }

    static int count(int[] a, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int sum = 0, ans = 0;
        for (int x : a) {
            sum += x;
            ans += freq.getOrDefault(sum - k, 0);
            freq.merge(sum, 1, Integer::sum);
        }
        return ans;
    }

    static int longestWithSumK(int[] a, int k) {
        Map<Long, Integer> first = new HashMap<>();
        first.put(0L, -1);
        long sum = 0; int best = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            Integer j = first.get(sum - k);
            if (j != null) best = Math.max(best, i - j);
            first.putIfAbsent(sum, i);
        }
        return best;
    }

    /** Positive-only variant: sliding window, O(1) space. */
    static int longestWithSumKPositive(int[] a, int k) {
        int l = 0, best = 0; long sum = 0;
        for (int r = 0; r < a.length; r++) {
            sum += a[r];
            while (sum > k && l <= r) sum -= a[l++];
            if (sum == k) best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int countXorK(int[] a, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);
        int x = 0, ans = 0;
        for (int v : a) {
            x ^= v;
            ans += freq.getOrDefault(x ^ k, 0);
            freq.merge(x, 1, Integer::sum);
        }
        return ans;
    }

    static int findMaxLength(int[] bits) {
        Map<Integer, Integer> first = new HashMap<>();
        first.put(0, -1);
        int sum = 0, best = 0;
        for (int i = 0; i < bits.length; i++) {
            sum += bits[i] == 0 ? -1 : 1;
            if (first.containsKey(sum)) best = Math.max(best, i - first.get(sum));
            else first.put(sum, i);
        }
        return best;
    }

    public static void main(String[] args) {
        check(countBrute(new int[]{1, 1, 1}, 2), 2); check(count(new int[]{1, 1, 1}, 2), 2);
        check(count(new int[]{1, -1, 0}, 0), 3);
        check(longestWithSumK(new int[]{10, 5, 2, 7, 1, -10}, 15), 6);
        check(longestWithSumKPositive(new int[]{2, 3, 5, 1, 9}, 10), 3);
        check(countXorK(new int[]{4, 2, 2, 6, 4}, 6), 4);
        check(findMaxLength(new int[]{0, 1, 0}), 2); check(findMaxLength(new int[]{0, 0, 1, 0, 0, 0, 1, 1}), 6);
        System.out.println("SubarraySumK: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
