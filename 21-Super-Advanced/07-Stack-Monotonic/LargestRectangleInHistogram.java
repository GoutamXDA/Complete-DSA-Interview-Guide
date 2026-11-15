import java.util.*;

/**
 * LC 84 — Largest Rectangle in Histogram. Explanation: LargestRectangleInHistogram.md
 * 1. Brute force pairs                       O(n^2)
 * 2. Divide & conquer on the minimum         O(n log n) avg, O(n^2) worst
 * 3. prevSmaller / nextSmaller arrays        O(n)
 * 4. One-pass monotonic stack + sentinel     O(n)
 */
public class LargestRectangleInHistogram {

    static long brute(int[] h) {
        long best = 0;
        for (int l = 0; l < h.length; l++) { int min = Integer.MAX_VALUE; for (int r = l; r < h.length; r++) { min = Math.min(min, h[r]); best = Math.max(best, (long) min * (r - l + 1)); } }
        return best;
    }

    static long divide(int[] h, int l, int r) {
        if (l > r) return 0;
        int m = l;
        for (int i = l; i <= r; i++) if (h[i] < h[m]) m = i;
        return Math.max((long) h[m] * (r - l + 1), Math.max(divide(h, l, m - 1), divide(h, m + 1, r)));
    }

    static long twoPass(int[] h) {
        int n = h.length;
        int[] prev = new int[n], next = new int[n];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < n; i++) { while (!st.isEmpty() && h[st.peek()] >= h[i]) st.pop(); prev[i] = st.isEmpty() ? -1 : st.peek(); st.push(i); }
        st.clear();
        for (int i = n - 1; i >= 0; i--) { while (!st.isEmpty() && h[st.peek()] >= h[i]) st.pop(); next[i] = st.isEmpty() ? n : st.peek(); st.push(i); }
        long best = 0;
        for (int i = 0; i < n; i++) best = Math.max(best, (long) h[i] * (next[i] - prev[i] - 1));
        return best;
    }

    static long largestRectangleArea(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        long best = 0;
        for (int i = 0; i <= h.length; i++) {
            int cur = i == h.length ? 0 : h[i];
            while (!st.isEmpty() && h[st.peek()] > cur) {
                int height = h[st.pop()];
                int left = st.isEmpty() ? -1 : st.peek();
                best = Math.max(best, (long) height * (i - left - 1));
            }
            st.push(i);
        }
        return best;
    }

    public static void main(String[] args) {
        check(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}), 10L); check(largestRectangleArea(new int[]{2, 4}), 4L);
        Random r = new Random(28);
        for (int t = 0; t < 3000; t++) {
            int[] h = new int[r.nextInt(14) + 1];
            for (int i = 0; i < h.length; i++) h[i] = r.nextInt(7);
            long e = brute(h);
            check(divide(h, 0, h.length - 1), e); check(twoPass(h), e); check(largestRectangleArea(h), e);
        }
        System.out.println("LargestRectangleInHistogram: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
