import java.util.*;

/**
 * Leaders in an array (GFG): element greater than everything to its right.
 *   Brute: check right side for each O(n^2) | scan from right tracking max O(n)
 * LC 118 Pascal's triangle: row[j] = prev[j-1] + prev[j]. O(n^2). Single row via nCr in O(n).
 * LC 2149 Rearrange array by sign (equal counts, keep order):
 *   Brute: split into pos/neg lists then interleave O(n) + O(n) | one pass with even/odd write indices.
 * LC 59 Spiral matrix II: fill 1..n^2 with shrinking boundaries.
 * LC 289 Game of life: brute copy board O(mn) space | in place: bit0 = current, bit1 = next state.
 * LC 304 Range sum query 2D: brute sum region O(mn) per query | 2D prefix + inclusion-exclusion O(1).
 * LC 152 Maximum product subarray: brute O(n^2) | track max AND min ending here (negative flips them). O(n)
 * LC 287 Find the duplicate number (n+1 values in [1,n], read-only, O(1) space):
 *   Sort O(n log n) (modifies) | HashSet O(n) space | Floyd cycle on i -> nums[i] O(n), O(1)
 */
public class ArrayProblemsII {

    static List<Integer> leadersBrute(int[] a) {
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            boolean lead = true;
            for (int j = i + 1; j < a.length; j++) if (a[j] >= a[i]) { lead = false; break; }
            if (lead) r.add(a[i]);
        }
        return r;
    }

    static List<Integer> leaders(int[] a) {
        LinkedList<Integer> r = new LinkedList<>();
        int max = Integer.MIN_VALUE;
        for (int i = a.length - 1; i >= 0; i--) if (a[i] > max) { max = a[i]; r.addFirst(a[i]); }
        return r;
    }

    static List<List<Integer>> generatePascal(int n) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) row.add(j == 0 || j == i ? 1 : res.get(i - 1).get(j - 1) + res.get(i - 1).get(j));
            res.add(row);
        }
        return res;
    }

    /** Row r (0-indexed) in O(r): C(r, j) = C(r, j-1) * (r - j + 1) / j */
    static List<Long> pascalRow(int r) {
        List<Long> row = new ArrayList<>();
        long v = 1; row.add(v);
        for (int j = 1; j <= r; j++) { v = v * (r - j + 1) / j; row.add(v); }
        return row;
    }

    static int[] rearrangeBrute(int[] a) {
        List<Integer> pos = new ArrayList<>(), neg = new ArrayList<>();
        for (int x : a) if (x > 0) pos.add(x); else neg.add(x);
        int[] r = new int[a.length];
        for (int i = 0; i < a.length / 2; i++) { r[2 * i] = pos.get(i); r[2 * i + 1] = neg.get(i); }
        return r;
    }

    static int[] rearrangeArray(int[] a) {
        int[] r = new int[a.length];
        int p = 0, n = 1;
        for (int x : a) { if (x > 0) { r[p] = x; p += 2; } else { r[n] = x; n += 2; } }
        return r;
    }

    static int[][] generateMatrix(int n) {
        int[][] m = new int[n][n];
        int top = 0, bottom = n - 1, left = 0, right = n - 1, v = 1;
        while (v <= n * n) {
            for (int j = left; j <= right; j++) m[top][j] = v++;
            top++;
            for (int i = top; i <= bottom; i++) m[i][right] = v++;
            right--;
            for (int j = right; j >= left; j--) m[bottom][j] = v++;
            bottom--;
            for (int i = bottom; i >= top; i--) m[i][left] = v++;
            left++;
        }
        return m;
    }

    static int liveNeighbours(int[][] b, int r, int c, int mask) {
        int cnt = 0;
        for (int dr = -1; dr <= 1; dr++) for (int dc = -1; dc <= 1; dc++) {
            if (dr == 0 && dc == 0) continue;
            int nr = r + dr, nc = c + dc;
            if (nr >= 0 && nc >= 0 && nr < b.length && nc < b[0].length) cnt += b[nr][nc] & mask;
        }
        return cnt;
    }

    static void gameOfLifeCopy(int[][] b) {
        int[][] old = new int[b.length][];
        for (int i = 0; i < b.length; i++) old[i] = b[i].clone();
        for (int r = 0; r < b.length; r++) for (int c = 0; c < b[0].length; c++) {
            int n = liveNeighbours(old, r, c, 1);
            b[r][c] = (old[r][c] == 1 && (n == 2 || n == 3)) || (old[r][c] == 0 && n == 3) ? 1 : 0;
        }
    }

    static void gameOfLife(int[][] b) {
        for (int r = 0; r < b.length; r++) for (int c = 0; c < b[0].length; c++) {
            int n = liveNeighbours(b, r, c, 1);              // bit 0 still holds the original state
            boolean alive = (b[r][c] & 1) == 1;
            if ((alive && (n == 2 || n == 3)) || (!alive && n == 3)) b[r][c] |= 2;
        }
        for (int[] row : b) for (int c = 0; c < row.length; c++) row[c] >>= 1;
    }

    static class NumMatrix {
        final int[][] pre;
        NumMatrix(int[][] m) {
            pre = new int[m.length + 1][m[0].length + 1];
            for (int r = 0; r < m.length; r++)
                for (int c = 0; c < m[0].length; c++) pre[r + 1][c + 1] = m[r][c] + pre[r][c + 1] + pre[r + 1][c] - pre[r][c];
        }
        int sumRegion(int r1, int c1, int r2, int c2) {
            return pre[r2 + 1][c2 + 1] - pre[r1][c2 + 1] - pre[r2 + 1][c1] + pre[r1][c1];
        }
    }

    static int maxProductBrute(int[] a) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) { int p = 1; for (int j = i; j < a.length; j++) { p *= a[j]; best = Math.max(best, p); } }
        return best;
    }

    static int maxProduct(int[] a) {
        int max = a[0], min = a[0], best = a[0];
        for (int i = 1; i < a.length; i++) {
            int x = a[i];
            if (x < 0) { int t = max; max = min; min = t; }   // a negative turns the smallest into the largest
            max = Math.max(x, max * x);
            min = Math.min(x, min * x);
            best = Math.max(best, max);
        }
        return best;
    }

    static int findDuplicateSet(int[] a) {
        Set<Integer> s = new HashSet<>();
        for (int x : a) if (!s.add(x)) return x;
        return -1;
    }

    static int findDuplicate(int[] a) {
        int slow = a[0], fast = a[0];
        do { slow = a[slow]; fast = a[a[fast]]; } while (slow != fast);
        slow = a[0];
        while (slow != fast) { slow = a[slow]; fast = a[fast]; }
        return slow;
    }

    public static void main(String[] args) {
        int[] l = {16, 17, 4, 3, 5, 2};
        check(leadersBrute(l), List.of(17, 5, 2)); check(leaders(l), List.of(17, 5, 2));
        check(generatePascal(5).get(4), List.of(1, 4, 6, 4, 1)); check(pascalRow(4), List.of(1L, 4L, 6L, 4L, 1L));
        int[] s = {3, 1, -2, -5, 2, -4};
        check(Arrays.toString(rearrangeBrute(s)), "[3, -2, 1, -5, 2, -4]"); check(Arrays.toString(rearrangeArray(s)), "[3, -2, 1, -5, 2, -4]");
        check(Arrays.deepToString(generateMatrix(3)), "[[1, 2, 3], [8, 9, 4], [7, 6, 5]]");
        check(Arrays.deepToString(generateMatrix(1)), "[[1]]");
        int[][] g1 = {{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}}, g2 = {{0, 1, 0}, {0, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        gameOfLife(g1); gameOfLifeCopy(g2);
        String e = "[[0, 0, 0], [1, 0, 1], [0, 1, 1], [0, 1, 0]]";
        check(Arrays.deepToString(g1), e); check(Arrays.deepToString(g2), e);
        NumMatrix nm = new NumMatrix(new int[][]{{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1}, {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}});
        check(nm.sumRegion(2, 1, 4, 3), 8); check(nm.sumRegion(1, 1, 2, 2), 11); check(nm.sumRegion(1, 2, 2, 4), 12);
        for (int[] t : new int[][]{{2, 3, -2, 4}, {-2, 0, -1}, {-2, 3, -4}, {-2}})
            check(maxProduct(t), maxProductBrute(t));
        check(maxProduct(new int[]{2, 3, -2, 4}), 6);
        check(findDuplicate(new int[]{1, 3, 4, 2, 2}), 2); check(findDuplicate(new int[]{3, 1, 3, 4, 2}), 3);
        check(findDuplicateSet(new int[]{3, 3, 3, 3, 3}), 3);
        System.out.println("ArrayProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
