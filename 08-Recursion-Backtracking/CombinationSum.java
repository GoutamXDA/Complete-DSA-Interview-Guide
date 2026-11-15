import java.util.*;

/**
 * LC 39 Combination Sum (reuse allowed): recurse with i (not i+1). Prune when candidate > remaining (sorted).
 * LC 40 Combination Sum II (each once, input has dupes): sort, recurse i+1, skip same value at same level.
 * LC 216 Combination Sum III: k numbers from 1..9 summing to n.
 * LC 77 Combinations: choose k from 1..n; prune when not enough numbers left.
 * Time: exponential, bounded by the number of valid paths; pruning matters in interviews.
 */
public class CombinationSum {

    static List<List<Integer>> combinationSum(int[] cands, int target) {
        int[] c = cands.clone(); Arrays.sort(c);
        List<List<Integer>> res = new ArrayList<>();
        cs1(c, 0, target, new ArrayList<>(), res);
        return res;
    }

    static void cs1(int[] c, int start, int rem, List<Integer> path, List<List<Integer>> res) {
        if (rem == 0) { res.add(new ArrayList<>(path)); return; }
        for (int i = start; i < c.length && c[i] <= rem; i++) {
            path.add(c[i]); cs1(c, i, rem - c[i], path, res); path.remove(path.size() - 1);
        }
    }

    static List<List<Integer>> combinationSum2(int[] cands, int target) {
        int[] c = cands.clone(); Arrays.sort(c);
        List<List<Integer>> res = new ArrayList<>();
        cs2(c, 0, target, new ArrayList<>(), res);
        return res;
    }

    static void cs2(int[] c, int start, int rem, List<Integer> path, List<List<Integer>> res) {
        if (rem == 0) { res.add(new ArrayList<>(path)); return; }
        for (int i = start; i < c.length && c[i] <= rem; i++) {
            if (i > start && c[i] == c[i - 1]) continue;
            path.add(c[i]); cs2(c, i + 1, rem - c[i], path, res); path.remove(path.size() - 1);
        }
    }

    static List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> res = new ArrayList<>();
        cs3(1, k, n, new ArrayList<>(), res);
        return res;
    }

    static void cs3(int start, int k, int rem, List<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) { if (rem == 0) res.add(new ArrayList<>(path)); return; }
        for (int d = start; d <= 9 && d <= rem; d++) {
            path.add(d); cs3(d + 1, k, rem - d, path, res); path.remove(path.size() - 1);
        }
    }

    static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        comb(1, n, k, new ArrayList<>(), res);
        return res;
    }

    static void comb(int start, int n, int k, List<Integer> path, List<List<Integer>> res) {
        if (path.size() == k) { res.add(new ArrayList<>(path)); return; }
        for (int i = start; i <= n - (k - path.size()) + 1; i++) { // pruning: enough numbers left
            path.add(i); comb(i + 1, n, k, path, res); path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        check(combinationSum(new int[]{2, 3, 6, 7}, 7).toString(), "[[2, 2, 3], [7]]");
        check(combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8).toString(), "[[1, 1, 6], [1, 2, 5], [1, 7], [2, 6]]");
        check(combinationSum3(3, 9).toString(), "[[1, 2, 6], [1, 3, 5], [2, 3, 4]]");
        check(combine(4, 2).size(), 6);
        System.out.println("CombinationSum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
