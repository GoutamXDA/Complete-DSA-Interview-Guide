import java.util.*;

/**
 * LC 85 — Maximal Rectangle. Explanation: MaximalRectangle.md
 * 1. Every rectangle + 2D prefix sums        O(R^2 C^2)
 * 2. Row-pair compression                    O(R^2 C)
 * 3. Histogram per row + monotonic stack     O(R C)
 * 4. DP with height / left / right arrays    O(R C)
 */
public class MaximalRectangle {

    static int brute(char[][] m) {
        int R = m.length, C = m[0].length, best = 0;
        int[][] p = new int[R + 1][C + 1];
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) p[r + 1][c + 1] = (m[r][c] - '0') + p[r][c + 1] + p[r + 1][c] - p[r][c];
        for (int r1 = 0; r1 < R; r1++) for (int c1 = 0; c1 < C; c1++)
            for (int r2 = r1; r2 < R; r2++) for (int c2 = c1; c2 < C; c2++) {
                int area = (r2 - r1 + 1) * (c2 - c1 + 1);
                if (p[r2 + 1][c2 + 1] - p[r1][c2 + 1] - p[r2 + 1][c1] + p[r1][c1] == area) best = Math.max(best, area);
            }
        return best;
    }

    static int rowPairs(char[][] m) {
        int R = m.length, C = m[0].length, best = 0;
        for (int top = 0; top < R; top++) {
            boolean[] ok = new boolean[C];
            Arrays.fill(ok, true);
            for (int bot = top; bot < R; bot++) {
                int run = 0;
                for (int c = 0; c < C; c++) {
                    ok[c] &= m[bot][c] == '1';
                    run = ok[c] ? run + 1 : 0;
                    best = Math.max(best, run * (bot - top + 1));
                }
            }
        }
        return best;
    }

    static int histogram(char[][] m) {
        int C = m[0].length, best = 0;
        int[] h = new int[C];
        for (char[] row : m) {
            for (int c = 0; c < C; c++) h[c] = row[c] == '1' ? h[c] + 1 : 0;
            best = Math.max(best, largest(h));
        }
        return best;
    }

    static int largest(int[] h) {
        Deque<Integer> st = new ArrayDeque<>();
        int best = 0;
        for (int i = 0; i <= h.length; i++) {
            int cur = i == h.length ? 0 : h[i];
            while (!st.isEmpty() && h[st.peek()] > cur) {
                int height = h[st.pop()], left = st.isEmpty() ? -1 : st.peek();
                best = Math.max(best, height * (i - left - 1));
            }
            st.push(i);
        }
        return best;
    }

    static int dpLeftRight(char[][] m) {
        int C = m[0].length, best = 0;
        int[] height = new int[C], left = new int[C], right = new int[C];
        Arrays.fill(right, C);
        for (char[] row : m) {
            int curLeft = 0, curRight = C;
            for (int c = 0; c < C; c++) height[c] = row[c] == '1' ? height[c] + 1 : 0;
            for (int c = 0; c < C; c++) {
                if (row[c] == '1') left[c] = Math.max(left[c], curLeft); else { left[c] = 0; curLeft = c + 1; }
            }
            for (int c = C - 1; c >= 0; c--) {
                if (row[c] == '1') right[c] = Math.min(right[c], curRight); else { right[c] = C; curRight = c; }
            }
            for (int c = 0; c < C; c++) best = Math.max(best, height[c] * (right[c] - left[c]));
        }
        return best;
    }

    public static void main(String[] args) {
        char[][] m = {"10100".toCharArray(), "10111".toCharArray(), "11111".toCharArray(), "10010".toCharArray()};
        check(histogram(m), 6); check(dpLeftRight(m), 6); check(rowPairs(m), 6); check(brute(m), 6);
        Random r = new Random(29);
        for (int t = 0; t < 1500; t++) {
            int R = r.nextInt(5) + 1, C = r.nextInt(5) + 1;
            char[][] g = new char[R][C];
            for (char[] row : g) for (int c = 0; c < C; c++) row[c] = r.nextInt(4) == 0 ? '0' : '1';
            int e = brute(g);
            check(rowPairs(g), e); check(histogram(g), e); check(dpLeftRight(g), e);
        }
        System.out.println("MaximalRectangle: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
