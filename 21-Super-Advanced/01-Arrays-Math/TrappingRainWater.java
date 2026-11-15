import java.util.*;

/**
 * LC 42 — Trapping Rain Water. Explanation: TrappingRainWater.md
 * 1. Brute per column             O(n^2) O(1)
 * 2. Prefix / suffix max arrays   O(n)   O(n)
 * 3. Monotonic stack (layers)     O(n)   O(n)
 * 4. Two pointers                 O(n)   O(1)
 */
public class TrappingRainWater {

    static long brute(int[] h) {
        long w = 0;
        for (int i = 0; i < h.length; i++) {
            int l = 0, r = 0;
            for (int j = 0; j <= i; j++) l = Math.max(l, h[j]);
            for (int j = i; j < h.length; j++) r = Math.max(r, h[j]);
            w += Math.min(l, r) - h[i];
        }
        return w;
    }

    static long prefixSuffix(int[] h) {
        int n = h.length;
        if (n == 0) return 0;
        int[] L = new int[n], R = new int[n];
        L[0] = h[0];
        for (int i = 1; i < n; i++) L[i] = Math.max(L[i - 1], h[i]);
        R[n - 1] = h[n - 1];
        for (int i = n - 2; i >= 0; i--) R[i] = Math.max(R[i + 1], h[i]);
        long w = 0;
        for (int i = 0; i < n; i++) w += Math.min(L[i], R[i]) - h[i];
        return w;
    }

    static long stack(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        long w = 0;
        for (int i = 0; i < h.length; i++) {
            while (!st.isEmpty() && h[i] > h[st.peek()]) {
                int bottom = st.pop();
                if (st.isEmpty()) break;                 // no left wall
                int left = st.peek();
                w += (long) (i - left - 1) * (Math.min(h[left], h[i]) - h[bottom]);
            }
            st.push(i);
        }
        return w;
    }

    static long twoPointers(int[] h) {
        int l = 0, r = h.length - 1, lm = 0, rm = 0;
        long w = 0;
        while (l < r) {
            if (h[l] < h[r]) { lm = Math.max(lm, h[l]); w += lm - h[l]; l++; }
            else { rm = Math.max(rm, h[r]); w += rm - h[r]; r--; }
        }
        return w;
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}, b = {4, 2, 0, 3, 2, 5};
        for (int[] x : new int[][]{a, b}) {
            long e = brute(x);
            check(prefixSuffix(x), e); check(stack(x), e); check(twoPointers(x), e);
        }
        check(twoPointers(a), 6L); check(twoPointers(b), 9L); check(twoPointers(new int[]{}), 0L);
        Random r = new Random(2);
        for (int k = 0; k < 2000; k++) {
            int[] h = new int[r.nextInt(15)];
            for (int i = 0; i < h.length; i++) h[i] = r.nextInt(6);
            long e = brute(h);
            check(prefixSuffix(h), e); check(stack(h), e); check(twoPointers(h), e);
        }
        System.out.println("TrappingRainWater: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
