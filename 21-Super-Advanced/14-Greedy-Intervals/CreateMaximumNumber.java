import java.util.*;

/**
 * LC 321 — Create Maximum Number. Explanation: CreateMaximumNumber.md
 * 1. Brute force over all order-preserving picks (oracle, tiny inputs)
 * 2. Every split + monotonic-stack max subsequence + suffix-compare merge
 * Also shows that a naive "take a on ties" merge is wrong.
 */
public class CreateMaximumNumber {

    static int[] best;

    static int[] brute(int[] a, int[] b, int k) {
        best = null;
        dfs(a, 0, b, 0, k, new ArrayList<>());
        return best;
    }

    static void dfs(int[] a, int i, int[] b, int j, int k, List<Integer> cur) {
        if (cur.size() == k) {
            int[] c = cur.stream().mapToInt(Integer::intValue).toArray();
            if (best == null || greater(c, 0, best, 0)) best = c;
            return;
        }
        if ((a.length - i) + (b.length - j) < k - cur.size()) return;
        for (int x = i; x < a.length; x++) { cur.add(a[x]); dfs(a, x + 1, b, j, k, cur); cur.remove(cur.size() - 1); }
        for (int y = j; y < b.length; y++) { cur.add(b[y]); dfs(a, i, b, y + 1, k, cur); cur.remove(cur.size() - 1); }
    }

    static int[] maxSubsequence(int[] nums, int t) {
        int[] st = new int[t];
        int top = 0, drop = nums.length - t;
        for (int x : nums) {
            while (top > 0 && st[top - 1] < x && drop > 0) { top--; drop--; }
            if (top < t) st[top++] = x; else drop--;
        }
        return st;
    }

    /** is a[i..] lexicographically greater than b[j..] ? (a longer equal prefix wins) */
    static boolean greater(int[] a, int i, int[] b, int j) {
        while (i < a.length && j < b.length && a[i] == b[j]) { i++; j++; }
        return j == b.length || (i < a.length && a[i] > b[j]);
    }

    static int[] merge(int[] a, int[] b) {
        int[] res = new int[a.length + b.length];
        for (int i = 0, j = 0, r = 0; r < res.length; r++) res[r] = greater(a, i, b, j) ? a[i++] : b[j++];
        return res;
    }

    static int[] naiveMerge(int[] a, int[] b) {
        int[] res = new int[a.length + b.length];
        for (int i = 0, j = 0, r = 0; r < res.length; r++)
            res[r] = (j == b.length || (i < a.length && a[i] >= b[j])) ? a[i++] : b[j++];
        return res;
    }

    static int[] maxNumber(int[] a, int[] b, int k) {
        int[] ans = null;
        for (int i = Math.max(0, k - b.length); i <= Math.min(k, a.length); i++) {
            int[] c = merge(maxSubsequence(a, i), maxSubsequence(b, k - i));
            if (ans == null || greater(c, 0, ans, 0)) ans = c;
        }
        return ans;
    }

    public static void main(String[] args) {
        check(Arrays.toString(maxNumber(new int[]{3, 4, 6, 5}, new int[]{9, 1, 2, 5, 8, 3}, 5)), "[9, 8, 6, 5, 3]");
        check(Arrays.toString(maxNumber(new int[]{6, 7}, new int[]{6, 0, 4}, 5)), "[6, 7, 6, 0, 4]");
        check(Arrays.toString(maxNumber(new int[]{3, 9}, new int[]{8, 9}, 3)), "[9, 8, 9]");
        check(Arrays.toString(merge(new int[]{6, 0, 4}, new int[]{6, 7})), "[6, 7, 6, 0, 4]");
        check(Arrays.toString(naiveMerge(new int[]{6, 0, 4}, new int[]{6, 7})).equals("[6, 7, 6, 0, 4]"), false);   // naive tie rule fails
        Random r = new Random(66);
        for (int t = 0; t < 1500; t++) {
            int[] a = new int[r.nextInt(5)], b = new int[r.nextInt(5)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(4);
            for (int i = 0; i < b.length; i++) b[i] = r.nextInt(4);
            if (a.length + b.length == 0) continue;
            int k = r.nextInt(a.length + b.length) + 1;
            check(Arrays.toString(maxNumber(a, b, k)), Arrays.toString(brute(a, b, k)));
        }
        System.out.println("CreateMaximumNumber: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
