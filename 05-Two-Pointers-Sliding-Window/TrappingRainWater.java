import java.util.*;

/**
 * LC 42 — Trapping Rain Water. Water above bar i = min(maxLeft, maxRight) - h[i].
 *
 * Approach 1 Brute   : for each i scan left and right for max.         O(n^2), O(1)
 * Approach 2 Prefix  : precompute leftMax[] and rightMax[].             O(n),   O(n)
 * Approach 3 Stack   : monotonic decreasing stack, fill "layers".       O(n),   O(n)
 * Approach 4 Two pointers: move the side with the smaller max; its water is decided. O(n), O(1)
 */
public class TrappingRainWater {

    static int brute(int[] h) {
        int water = 0;
        for (int i = 0; i < h.length; i++) {
            int lm = 0, rm = 0;
            for (int j = 0; j <= i; j++) lm = Math.max(lm, h[j]);
            for (int j = i; j < h.length; j++) rm = Math.max(rm, h[j]);
            water += Math.min(lm, rm) - h[i];
        }
        return water;
    }

    static int prefixMax(int[] h) {
        int n = h.length; if (n == 0) return 0;
        int[] L = new int[n], R = new int[n];
        L[0] = h[0]; for (int i = 1; i < n; i++) L[i] = Math.max(L[i - 1], h[i]);
        R[n - 1] = h[n - 1]; for (int i = n - 2; i >= 0; i--) R[i] = Math.max(R[i + 1], h[i]);
        int w = 0; for (int i = 0; i < n; i++) w += Math.min(L[i], R[i]) - h[i];
        return w;
    }

    static int stack(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        int w = 0;
        for (int i = 0; i < h.length; i++) {
            while (!st.isEmpty() && h[i] > h[st.peek()]) {
                int bottom = st.pop();
                if (st.isEmpty()) break;
                int left = st.peek();
                int width = i - left - 1;
                int height = Math.min(h[left], h[i]) - h[bottom];
                w += width * height;
            }
            st.push(i);
        }
        return w;
    }

    static int twoPointers(int[] h) {
        int l = 0, r = h.length - 1, lm = 0, rm = 0, w = 0;
        while (l < r) {
            if (h[l] < h[r]) {
                lm = Math.max(lm, h[l]); w += lm - h[l]; l++;   // right side guarantees a wall >= lm
            } else {
                rm = Math.max(rm, h[r]); w += rm - h[r]; r--;
            }
        }
        return w;
    }

    public static void main(String[] args) {
        int[] h = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        check(brute(h), 6); check(prefixMax(h), 6); check(stack(h), 6); check(twoPointers(h), 6);
        int[] g = {4, 2, 0, 3, 2, 5};
        check(twoPointers(g), 9); check(stack(g), 9);
        System.out.println("TrappingRainWater: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
