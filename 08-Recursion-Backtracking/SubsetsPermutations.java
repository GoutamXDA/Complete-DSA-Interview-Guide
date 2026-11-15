import java.util.*;

/**
 * LC 78 Subsets — 3 ways, all O(n * 2^n):
 *   A) take/skip recursion   B) for-loop backtracking (each node is a subset)   C) bitmask 0..2^n-1
 * LC 90 Subsets II (duplicates): sort, skip a[i] == a[i-1] when i > start.
 * Subset sums: take/skip, collect sums. O(2^n)
 * LC 46 Permutations — O(n * n!):
 *   A) used[] boolean array   B) swap in place (no extra array)
 * LC 47 Permutations II: sort; skip a[i] if a[i]==a[i-1] and a[i-1] not used (avoid same-level duplicate).
 */
public class SubsetsPermutations {

    static List<List<Integer>> subsetsTakeSkip(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        takeSkip(a, 0, new ArrayList<>(), res);
        return res;
    }

    static void takeSkip(int[] a, int i, List<Integer> path, List<List<Integer>> res) {
        if (i == a.length) { res.add(new ArrayList<>(path)); return; }
        path.add(a[i]); takeSkip(a, i + 1, path, res); path.remove(path.size() - 1); // take
        takeSkip(a, i + 1, path, res);                                                 // skip
    }

    static List<List<Integer>> subsetsLoop(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        loop(a, 0, new ArrayList<>(), res);
        return res;
    }

    static void loop(int[] a, int start, List<Integer> path, List<List<Integer>> res) {
        res.add(new ArrayList<>(path));
        for (int i = start; i < a.length; i++) {
            path.add(a[i]); loop(a, i + 1, path, res); path.remove(path.size() - 1);
        }
    }

    static List<List<Integer>> subsetsBitmask(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        for (int mask = 0; mask < (1 << a.length); mask++) {
            List<Integer> s = new ArrayList<>();
            for (int b = 0; b < a.length; b++) if ((mask >> b & 1) == 1) s.add(a[b]);
            res.add(s);
        }
        return res;
    }

    static List<List<Integer>> subsetsWithDup(int[] nums) {
        int[] a = nums.clone(); Arrays.sort(a);
        List<List<Integer>> res = new ArrayList<>();
        dup(a, 0, new ArrayList<>(), res);
        return res;
    }

    static void dup(int[] a, int start, List<Integer> path, List<List<Integer>> res) {
        res.add(new ArrayList<>(path));
        for (int i = start; i < a.length; i++) {
            if (i > start && a[i] == a[i - 1]) continue;
            path.add(a[i]); dup(a, i + 1, path, res); path.remove(path.size() - 1);
        }
    }

    static List<Integer> subsetSums(int[] a) {
        List<Integer> res = new ArrayList<>();
        sums(a, 0, 0, res);
        Collections.sort(res);
        return res;
    }

    static void sums(int[] a, int i, int s, List<Integer> res) {
        if (i == a.length) { res.add(s); return; }
        sums(a, i + 1, s + a[i], res); sums(a, i + 1, s, res);
    }

    static List<List<Integer>> permuteUsed(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        used(a, new boolean[a.length], new ArrayList<>(), res);
        return res;
    }

    static void used(int[] a, boolean[] used, List<Integer> path, List<List<Integer>> res) {
        if (path.size() == a.length) { res.add(new ArrayList<>(path)); return; }
        for (int i = 0; i < a.length; i++) {
            if (used[i]) continue;
            used[i] = true; path.add(a[i]);
            used(a, used, path, res);
            used[i] = false; path.remove(path.size() - 1);
        }
    }

    static List<List<Integer>> permuteSwap(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        swapPerm(nums.clone(), 0, res);
        return res;
    }

    static void swapPerm(int[] a, int k, List<List<Integer>> res) {
        if (k == a.length) { List<Integer> p = new ArrayList<>(); for (int x : a) p.add(x); res.add(p); return; }
        for (int i = k; i < a.length; i++) {
            swap(a, k, i); swapPerm(a, k + 1, res); swap(a, k, i);
        }
    }

    static List<List<Integer>> permuteUnique(int[] nums) {
        int[] a = nums.clone(); Arrays.sort(a);
        List<List<Integer>> res = new ArrayList<>();
        unique(a, new boolean[a.length], new ArrayList<>(), res);
        return res;
    }

    static void unique(int[] a, boolean[] used, List<Integer> path, List<List<Integer>> res) {
        if (path.size() == a.length) { res.add(new ArrayList<>(path)); return; }
        for (int i = 0; i < a.length; i++) {
            if (used[i] || (i > 0 && a[i] == a[i - 1] && !used[i - 1])) continue;
            used[i] = true; path.add(a[i]);
            unique(a, used, path, res);
            used[i] = false; path.remove(path.size() - 1);
        }
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    static String norm(List<List<Integer>> r) {
        List<String> s = new ArrayList<>(); for (List<Integer> t : r) s.add(t.toString());
        Collections.sort(s); return s.toString();
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        String e = norm(subsetsTakeSkip(a));
        check(subsetsTakeSkip(a).size(), 8); check(norm(subsetsLoop(a)), e); check(norm(subsetsBitmask(a)), e);
        check(norm(subsetsWithDup(new int[]{1, 2, 2})), "[[1, 2, 2], [1, 2], [1], [2, 2], [2], []]");
        check(subsetSums(new int[]{2, 3}), List.of(0, 2, 3, 5));
        check(permuteUsed(a).size(), 6); check(norm(permuteSwap(a)), norm(permuteUsed(a)));
        check(permuteUnique(new int[]{1, 1, 2}).size(), 3);
        System.out.println("SubsetsPermutations: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
