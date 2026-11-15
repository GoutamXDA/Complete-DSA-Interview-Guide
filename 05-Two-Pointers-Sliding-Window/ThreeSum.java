import java.util.*;

/**
 * k-Sum family.
 *
 * LC 167 Two Sum II (sorted): l, r pointers. O(n), O(1)
 *
 * LC 15 3Sum — unique triplets summing to 0
 *   Brute  : 3 loops + Set<List> to dedupe          O(n^3)
 *   Better : fix i, HashSet for the pair            O(n^2), O(n)
 *   Optimal: sort, fix i, two pointers, skip dupes  O(n^2), O(1) extra
 *
 * LC 16 3Sum closest: same loop, keep closest sum.
 * LC 18 4Sum: fix i and j, two pointers. O(n^3). Use long to avoid overflow.
 */
public class ThreeSum {

    static int[] twoSumSorted(int[] a, int target) {
        int l = 0, r = a.length - 1;
        while (l < r) {
            int s = a[l] + a[r];
            if (s == target) return new int[]{l + 1, r + 1};
            if (s < target) l++; else r--;
        }
        return new int[]{-1, -1};
    }

    static List<List<Integer>> threeSumBrute(int[] a) {
        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                for (int k = j + 1; k < a.length; k++)
                    if (a[i] + a[j] + a[k] == 0) {
                        List<Integer> t = Arrays.asList(a[i], a[j], a[k]); Collections.sort(t); set.add(t);
                    }
        return new ArrayList<>(set);
    }

    static List<List<Integer>> threeSumHash(int[] a) {
        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < a.length; i++) {
            Set<Integer> seen = new HashSet<>();
            for (int j = i + 1; j < a.length; j++) {
                int need = -(a[i] + a[j]);
                if (seen.contains(need)) {
                    List<Integer> t = Arrays.asList(a[i], a[j], need); Collections.sort(t); set.add(t);
                }
                seen.add(a[j]);
            }
        }
        return new ArrayList<>(set);
    }

    static List<List<Integer>> threeSum(int[] nums) {
        int[] a = nums.clone(); Arrays.sort(a);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < a.length - 2; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;     // skip duplicate anchor
            if (a[i] > 0) break;                          // sorted: no way to reach 0
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (s < 0) l++;
                else if (s > 0) r--;
                else {
                    res.add(List.of(a[i], a[l], a[r]));
                    while (l < r && a[l] == a[l + 1]) l++;
                    while (l < r && a[r] == a[r - 1]) r--;
                    l++; r--;
                }
            }
        }
        return res;
    }

    static int threeSumClosest(int[] nums, int target) {
        int[] a = nums.clone(); Arrays.sort(a);
        int best = a[0] + a[1] + a[2];
        for (int i = 0; i < a.length - 2; i++) {
            int l = i + 1, r = a.length - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (Math.abs(s - target) < Math.abs(best - target)) best = s;
                if (s < target) l++; else if (s > target) r--; else return s;
            }
        }
        return best;
    }

    static List<List<Integer>> fourSum(int[] nums, int target) {
        int[] a = nums.clone(); Arrays.sort(a);
        List<List<Integer>> res = new ArrayList<>();
        int n = a.length;
        for (int i = 0; i < n - 3; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;
            for (int j = i + 1; j < n - 2; j++) {
                if (j > i + 1 && a[j] == a[j - 1]) continue;
                int l = j + 1, r = n - 1;
                while (l < r) {
                    long s = (long) a[i] + a[j] + a[l] + a[r];
                    if (s < target) l++;
                    else if (s > target) r--;
                    else {
                        res.add(List.of(a[i], a[j], a[l], a[r]));
                        while (l < r && a[l] == a[l + 1]) l++;
                        while (l < r && a[r] == a[r - 1]) r--;
                        l++; r--;
                    }
                }
            }
        }
        return res;
    }

    static String norm(List<List<Integer>> r) {
        List<String> s = new ArrayList<>(); for (List<Integer> t : r) s.add(t.toString());
        Collections.sort(s); return s.toString();
    }

    public static void main(String[] args) {
        check(Arrays.toString(twoSumSorted(new int[]{2, 7, 11, 15}, 9)), "[1, 2]");
        int[] a = {-1, 0, 1, 2, -1, -4};
        String exp = "[[-1, -1, 2], [-1, 0, 1]]";
        check(norm(threeSumBrute(a)), exp); check(norm(threeSumHash(a)), exp); check(norm(threeSum(a)), exp);
        check(threeSum(new int[]{0, 0, 0, 0}).size(), 1);
        check(threeSumClosest(new int[]{-1, 2, 1, -4}, 1), 2);
        check(norm(fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0)), "[[-1, 0, 0, 1], [-2, -1, 1, 2], [-2, 0, 0, 2]]");
        check(fourSum(new int[]{1000000000, 1000000000, 1000000000, 1000000000}, -294967296).size(), 0);
        System.out.println("ThreeSum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
