import java.util.*;

/**
 * LC 51 / 52 — N-Queens. Explanation: NQueens.md
 * 1. Brute force over column permutations          O(n! * n^2)
 * 2. Backtracking with upward scanning             O(n! * n)
 * 3. Backtracking with column / diagonal arrays    O(n!)
 * 4. Bitmask backtracking (count)                  O(n!) but very fast
 */
public class NQueens {

    static int permutationsCount(int n) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
        int c = 0;
        do { if (noDiagonalClash(p)) c++; } while (nextPerm(p));
        return c;
    }

    static boolean noDiagonalClash(int[] p) {
        for (int i = 0; i < p.length; i++) for (int j = i + 1; j < p.length; j++) if (Math.abs(p[i] - p[j]) == j - i) return false;
        return true;
    }

    static boolean nextPerm(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i < 0) return false;
        int j = a.length - 1;
        while (a[j] <= a[i]) j--;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        for (int l = i + 1, r = a.length - 1; l < r; l++, r--) { t = a[l]; a[l] = a[r]; a[r] = t; }
        return true;
    }

    static int scanCount(int n) { return scan(0, new int[n], n); }

    static int scan(int row, int[] col, int n) {
        if (row == n) return 1;
        int c = 0;
        for (int x = 0; x < n; x++) {
            boolean ok = true;
            for (int r = 0; r < row && ok; r++) if (col[r] == x || Math.abs(col[r] - x) == row - r) ok = false;
            if (ok) { col[row] = x; c += scan(row + 1, col, n); }
        }
        return c;
    }

    static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] b = new char[n][n];
        for (char[] row : b) Arrays.fill(row, '.');
        place(0, n, b, new boolean[n], new boolean[2 * n - 1], new boolean[2 * n - 1], res);
        return res;
    }

    static void place(int r, int n, char[][] b, boolean[] col, boolean[] d1, boolean[] d2, List<List<String>> res) {
        if (r == n) { List<String> s = new ArrayList<>(); for (char[] row : b) s.add(new String(row)); res.add(s); return; }
        for (int c = 0; c < n; c++) {
            if (col[c] || d1[r - c + n - 1] || d2[r + c]) continue;
            col[c] = d1[r - c + n - 1] = d2[r + c] = true; b[r][c] = 'Q';
            place(r + 1, n, b, col, d1, d2, res);
            col[c] = d1[r - c + n - 1] = d2[r + c] = false; b[r][c] = '.';
        }
    }

    static int totalNQueens(int n) { return bits(n, 0, 0, 0, 0); }

    static int bits(int n, int row, int cols, int d1, int d2) {
        if (row == n) return 1;
        int total = 0, free = ~(cols | d1 | d2) & ((1 << n) - 1);
        while (free != 0) {
            int bit = free & -free;
            free -= bit;
            total += bits(n, row + 1, cols | bit, (d1 | bit) << 1, (d2 | bit) >> 1);
        }
        return total;
    }

    public static void main(String[] args) {
        check(solveNQueens(4).toString(), "[[.Q.., ...Q, Q..., ..Q.], [..Q., Q..., ...Q, .Q..]]");
        int[] known = {1, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724, 2680, 14200};
        for (int n = 1; n <= 8; n++) { check(permutationsCount(n), known[n]); check(scanCount(n), known[n]); }
        for (int n = 1; n <= 12; n++) { check(totalNQueens(n), known[n]); check(solveNQueens(n).size(), known[n]); }
        System.out.println("NQueens: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
