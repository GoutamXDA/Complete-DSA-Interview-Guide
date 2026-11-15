import java.util.*;

/**
 * LC 1 — Two Sum. Return indices i != j with nums[i] + nums[j] == target.
 * https://leetcode.com/problems/two-sum/
 *
 * Approach 1 Brute force : check every pair.                       Time O(n^2)      Space O(1)
 * Approach 2 Sort + 2ptr : sort (value,index) pairs, move pointers. Time O(n log n) Space O(n)
 *                          (great when the array is already sorted -> O(n), O(1). LC 167)
 * Approach 3 HashMap     : for each x, look up target - x seen so far. Time O(n) Space O(n)
 *
 * Follow-ups: 3Sum (sort + 2ptr, skip duplicates), 4Sum, Two Sum in BST, count pairs.
 */
public class TwoSum {

    static int[] bruteForce(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++)
            for (int j = i + 1; j < nums.length; j++)
                if (nums[i] + nums[j] == target) return new int[]{i, j};
        return new int[]{-1, -1};
    }

    static int[] sortTwoPointers(int[] nums, int target) {
        int n = nums.length;
        int[][] pairs = new int[n][2];
        for (int i = 0; i < n; i++) pairs[i] = new int[]{nums[i], i};
        Arrays.sort(pairs, (a, b) -> Integer.compare(a[0], b[0]));
        int l = 0, r = n - 1;
        while (l < r) {
            int sum = pairs[l][0] + pairs[r][0];
            if (sum == target) {
                int i = pairs[l][1], j = pairs[r][1];
                return new int[]{Math.min(i, j), Math.max(i, j)};
            }
            if (sum < target) l++; else r--;
        }
        return new int[]{-1, -1};
    }

    static int[] hashMap(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value -> index
        for (int i = 0; i < nums.length; i++) {
            Integer j = seen.get(target - nums[i]);
            if (j != null) return new int[]{j, i};
            seen.put(nums[i], i);      // put AFTER lookup so we don't pair an element with itself
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] a = {2, 7, 11, 15};
        check(Arrays.toString(bruteForce(a, 9)), "[0, 1]");
        check(Arrays.toString(sortTwoPointers(a, 9)), "[0, 1]");
        check(Arrays.toString(hashMap(a, 9)), "[0, 1]");
        check(Arrays.toString(hashMap(new int[]{3, 3}, 6)), "[0, 1]");
        check(Arrays.toString(sortTwoPointers(new int[]{3, 2, 4}, 6)), "[1, 2]");
        System.out.println("TwoSum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
