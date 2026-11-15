/**
 * LC 11 — Container With Most Water.
 *
 * Brute  : every pair (i, j): min(h[i], h[j]) * (j - i).   O(n^2)
 * Optimal: l = 0, r = n-1. Area limited by the shorter line; moving the taller one can never help
 *          (width shrinks and height can't exceed the shorter). So move the shorter side. O(n), O(1)
 */
public class ContainerWithMostWater {

    static int brute(int[] h) {
        int best = 0;
        for (int i = 0; i < h.length; i++)
            for (int j = i + 1; j < h.length; j++) best = Math.max(best, Math.min(h[i], h[j]) * (j - i));
        return best;
    }

    static int maxArea(int[] h) {
        int l = 0, r = h.length - 1, best = 0;
        while (l < r) {
            best = Math.max(best, Math.min(h[l], h[r]) * (r - l));
            if (h[l] < h[r]) l++; else r--;
        }
        return best;
    }

    public static void main(String[] args) {
        int[] h = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        check(brute(h), 49); check(maxArea(h), 49); check(maxArea(new int[]{1, 1}), 1);
        System.out.println("ContainerWithMostWater: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
