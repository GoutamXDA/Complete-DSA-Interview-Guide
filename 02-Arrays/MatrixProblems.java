import java.util.*;

/**
 * Matrix classics.
 *
 * LC 48 Rotate image 90° clockwise
 *   Brute  : new matrix, res[j][n-1-i] = m[i][j]     O(n^2), O(n^2)
 *   Optimal: transpose, then reverse each row        O(n^2), O(1)
 *
 * LC 54 Spiral order: shrink top/bottom/left/right boundaries. O(m*n)
 *   Pitfall: re-check top<=bottom and left<=right before the 3rd and 4th edges.
 *
 * LC 73 Set matrix zeroes
 *   Brute  : copy matrix                    O(mn) space
 *   Better : row[] and col[] marker arrays  O(m+n) space
 *   Optimal: use first row/col as markers + one flag for column 0. O(1) space
 */
public class MatrixProblems {

    static int[][] rotateBrute(int[][] m) {
        int n = m.length; int[][] r = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) r[j][n - 1 - i] = m[i][j];
        return r;
    }

    static void rotate(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) { int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t; }
        for (int[] row : m)
            for (int l = 0, r = n - 1; l < r; l++, r--) { int t = row[l]; row[l] = row[r]; row[r] = t; }
    }

    static List<Integer> spiralOrder(int[][] m) {
        List<Integer> res = new ArrayList<>();
        int top = 0, bottom = m.length - 1, left = 0, right = m[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) res.add(m[top][j]);
            top++;
            for (int i = top; i <= bottom; i++) res.add(m[i][right]);
            right--;
            if (top <= bottom) { for (int j = right; j >= left; j--) res.add(m[bottom][j]); bottom--; }
            if (left <= right) { for (int i = bottom; i >= top; i--) res.add(m[i][left]); left++; }
        }
        return res;
    }

    static void setZeroesBetter(int[][] m) {
        int R = m.length, C = m[0].length;
        boolean[] row = new boolean[R], col = new boolean[C];
        for (int i = 0; i < R; i++) for (int j = 0; j < C; j++) if (m[i][j] == 0) { row[i] = true; col[j] = true; }
        for (int i = 0; i < R; i++) for (int j = 0; j < C; j++) if (row[i] || col[j]) m[i][j] = 0;
    }

    static void setZeroes(int[][] m) {
        int R = m.length, C = m[0].length;
        boolean col0 = false;
        for (int i = 0; i < R; i++) {
            if (m[i][0] == 0) col0 = true;
            for (int j = 1; j < C; j++) if (m[i][j] == 0) { m[i][0] = 0; m[0][j] = 0; }
        }
        for (int i = R - 1; i >= 0; i--) {           // bottom-up so row 0 markers are used before being zeroed
            for (int j = C - 1; j >= 1; j--) if (m[i][0] == 0 || m[0][j] == 0) m[i][j] = 0;
            if (col0) m[i][0] = 0;
        }
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        check(Arrays.deepToString(rotateBrute(a)), "[[7, 4, 1], [8, 5, 2], [9, 6, 3]]");
        rotate(a); check(Arrays.deepToString(a), "[[7, 4, 1], [8, 5, 2], [9, 6, 3]]");
        check(spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}}),
                List.of(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7));
        int[][] z = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        int[][] z2 = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        setZeroes(z); setZeroesBetter(z2);
        String exp = "[[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]";
        check(Arrays.deepToString(z), exp); check(Arrays.deepToString(z2), exp);
        System.out.println("MatrixProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
