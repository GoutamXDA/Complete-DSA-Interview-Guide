import java.util.*;

/**
 * LC 330 — Patching Array. Explanation: PatchingArray.md
 * 1. Brute force: iterative deepening over patch sets, reachable-sum bitset (oracle, tiny n)
 * 2. Greedy on the smallest unreachable sum `miss`     O(m + log n)
 */
public class PatchingArray {

    static boolean coversAll(List<Integer> nums, int n) {
        boolean[] can = new boolean[n + 1];
        can[0] = true;
        for (int x : nums) for (int s = n; s >= x; s--) can[s] |= can[s - x];
        for (int s = 1; s <= n; s++) if (!can[s]) return false;
        return true;
    }

    static int brute(int[] nums, int n) {
        List<Integer> base = new ArrayList<>();
        for (int x : nums) base.add(x);
        for (int k = 0; ; k++) if (tryPatches(base, n, k, 1)) return k;
    }

    static boolean tryPatches(List<Integer> cur, int n, int left, int from) {
        if (left == 0) return coversAll(cur, n);
        for (int p = from; p <= n; p++) {                 // patches in non-decreasing order (order doesn't matter)
            cur.add(p);
            boolean ok = tryPatches(cur, n, left - 1, p);
            cur.remove(cur.size() - 1);
            if (ok) return true;
        }
        return false;
    }

    static int minPatches(int[] nums, int n) {
        long miss = 1;
        int i = 0, patches = 0;
        while (miss <= n) {
            if (i < nums.length && nums[i] <= miss) miss += nums[i++];
            else { miss += miss; patches++; }
        }
        return patches;
    }

    public static void main(String[] args) {
        check(minPatches(new int[]{1, 3}, 6), 1); check(minPatches(new int[]{1, 5, 10}, 20), 2); check(minPatches(new int[]{1, 2, 2}, 5), 0);
        check(minPatches(new int[]{1, 2, 31, 33}, Integer.MAX_VALUE), 28);           // needs long for miss
        Random r = new Random(65);
        for (int t = 0; t < 600; t++) {
            int n = r.nextInt(14) + 1;
            int[] nums = new int[r.nextInt(4)];
            for (int i = 0; i < nums.length; i++) nums[i] = r.nextInt(10) + 1;
            Arrays.sort(nums);
            check(minPatches(nums, n), brute(nums, n));
        }
        System.out.println("PatchingArray: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
