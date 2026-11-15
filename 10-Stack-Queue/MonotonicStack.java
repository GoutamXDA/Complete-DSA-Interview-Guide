import java.util.*;

/**
 * Monotonic stack problems. Brute force for most is O(n^2) nested scanning; stack gives O(n).
 *
 * LC 496/503 Next greater (circular: iterate 2n)
 * LC 739 Daily temperatures: distance to next greater
 * LC 901 Stock span: pop while price <= current, accumulate spans
 * LC 84 Largest rectangle: for each bar, width = nextSmaller - prevSmaller - 1. One-pass stack version.
 * LC 85 Maximal rectangle: build heights row by row, apply LC 84. O(R*C)
 * LC 907 Sum of subarray minimums: a[i] is min of (i - prevLess) * (nextLessOrEqual - i) subarrays.
 * LC 735 Asteroid collision: stack of survivors.
 * LC 402 Remove K digits: keep increasing stack; pop bigger previous digits while k > 0.
 */
public class MonotonicStack {

    static int[] nextGreaterBrute(int[] a) {
        int n = a.length; int[] r = new int[n]; Arrays.fill(r, -1);
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) if (a[j] > a[i]) { r[i] = a[j]; break; }
        return r;
    }

    static int[] nextGreater(int[] a) {
        int n = a.length; int[] r = new int[n]; Arrays.fill(r, -1);
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && a[st.peek()] < a[i]) r[st.pop()] = a[i];
            st.push(i);
        }
        return r;
    }

    static int[] nextGreaterCircular(int[] a) {
        int n = a.length; int[] r = new int[n]; Arrays.fill(r, -1);
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < 2 * n; i++) {
            while (!st.isEmpty() && a[st.peek()] < a[i % n]) r[st.pop()] = a[i % n];
            if (i < n) st.push(i);
        }
        return r;
    }

    static int[] dailyTemperatures(int[] t) {
        int[] r = new int[t.length];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < t.length; i++) {
            while (!st.isEmpty() && t[st.peek()] < t[i]) { int j = st.pop(); r[j] = i - j; }
            st.push(i);
        }
        return r;
    }

    static class StockSpanner {
        Deque<int[]> st = new ArrayDeque<>(); // {price, span}
        int next(int price) {
            int span = 1;
            while (!st.isEmpty() && st.peek()[0] <= price) span += st.pop()[1];
            st.push(new int[]{price, span});
            return span;
        }
    }

    static int largestRectangleBrute(int[] h) {
        int best = 0;
        for (int i = 0; i < h.length; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = i; j < h.length; j++) { min = Math.min(min, h[j]); best = Math.max(best, min * (j - i + 1)); }
        }
        return best;
    }

    static int largestRectangleArea(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        int best = 0, n = h.length;
        for (int i = 0; i <= n; i++) {
            int cur = (i == n) ? 0 : h[i];                // sentinel 0 flushes the stack
            while (!st.isEmpty() && h[st.peek()] > cur) {
                int height = h[st.pop()];
                int left = st.isEmpty() ? -1 : st.peek(); // previous smaller
                best = Math.max(best, height * (i - left - 1)); // i is next smaller
            }
            st.push(i);
        }
        return best;
    }

    static int maximalRectangle(char[][] m) {
        if (m.length == 0) return 0;
        int[] heights = new int[m[0].length];
        int best = 0;
        for (char[] row : m) {
            for (int j = 0; j < row.length; j++) heights[j] = row[j] == '1' ? heights[j] + 1 : 0;
            best = Math.max(best, largestRectangleArea(heights));
        }
        return best;
    }

    static int sumSubarrayMins(int[] a) {
        final long MOD = 1_000_000_007L;
        int n = a.length;
        int[] left = new int[n], right = new int[n];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {                       // previous strictly less
            while (!st.isEmpty() && a[st.peek()] >= a[i]) st.pop();
            left[i] = st.isEmpty() ? i + 1 : i - st.peek();
            st.push(i);
        }
        st.clear();
        for (int i = n - 1; i >= 0; i--) {                  // next less-or-equal (avoids double counting)
            while (!st.isEmpty() && a[st.peek()] > a[i]) st.pop();
            right[i] = st.isEmpty() ? n - i : st.peek() - i;
            st.push(i);
        }
        long sum = 0;
        for (int i = 0; i < n; i++) sum = (sum + (long) a[i] * left[i] % MOD * right[i]) % MOD;
        return (int) sum;
    }

    static int[] asteroidCollision(int[] ast) {
        Deque<Integer> st = new ArrayDeque<>();
        for (int x : ast) {
            boolean alive = true;
            while (alive && x < 0 && !st.isEmpty() && st.peek() > 0) {
                if (st.peek() < -x) st.pop();
                else { if (st.peek() == -x) st.pop(); alive = false; }
            }
            if (alive) st.push(x);
        }
        int[] r = new int[st.size()];
        for (int i = r.length - 1; i >= 0; i--) r[i] = st.pop();
        return r;
    }

    static String removeKdigits(String num, int k) {
        StringBuilder st = new StringBuilder();
        for (char c : num.toCharArray()) {
            while (k > 0 && st.length() > 0 && st.charAt(st.length() - 1) > c) { st.deleteCharAt(st.length() - 1); k--; }
            st.append(c);
        }
        st.setLength(st.length() - k);
        int i = 0; while (i < st.length() - 1 && st.charAt(i) == '0') i++;
        String r = st.substring(i);
        return r.isEmpty() ? "0" : r;
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 2, 25};
        check(Arrays.toString(nextGreaterBrute(a)), "[5, 25, 25, -1]"); check(Arrays.toString(nextGreater(a)), "[5, 25, 25, -1]");
        check(Arrays.toString(nextGreaterCircular(new int[]{1, 2, 1})), "[2, -1, 2]");
        check(Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})), "[1, 1, 4, 2, 1, 1, 0, 0]");
        StockSpanner sp = new StockSpanner(); StringBuilder spans = new StringBuilder();
        for (int p : new int[]{100, 80, 60, 70, 60, 75, 85}) spans.append(sp.next(p));
        check(spans.toString(), "1112146");
        check(largestRectangleBrute(new int[]{2, 1, 5, 6, 2, 3}), 10); check(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}), 10);
        char[][] m = {"10100".toCharArray(), "10111".toCharArray(), "11111".toCharArray(), "10010".toCharArray()};
        check(maximalRectangle(m), 6);
        check(sumSubarrayMins(new int[]{3, 1, 2, 4}), 17); check(sumSubarrayMins(new int[]{11, 81, 94, 43, 3}), 444);
        check(Arrays.toString(asteroidCollision(new int[]{5, 10, -5})), "[5, 10]");
        check(Arrays.toString(asteroidCollision(new int[]{8, -8})), "[]");
        check(Arrays.toString(asteroidCollision(new int[]{10, 2, -5})), "[10]");
        check(removeKdigits("1432219", 3), "1219"); check(removeKdigits("10200", 1), "200"); check(removeKdigits("10", 2), "0");
        System.out.println("MonotonicStack: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
