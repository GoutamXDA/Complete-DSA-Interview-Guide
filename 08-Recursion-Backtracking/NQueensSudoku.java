import java.util.*;

/**
 * LC 51 N-Queens
 *   Brute : try every cell for each row, validate by scanning board O(n^n * n)
 *   Better: place row by row, check column + diagonals with boolean arrays in O(1). O(n!)
 *           diagonal id: r - c + n - 1 ; anti-diagonal id: r + c
 *   Bitmask variant for counting (LC 52) is the fastest.
 *
 * LC 37 Sudoku solver: for each empty cell try 1..9 valid in row/col/box. Track with boolean[9][10].
 *   Worst 9^(empty cells) but pruning makes it fast.
 *
 * Rat in a maze: DFS in lexicographic direction order D, L, R, U, mark visited.
 */
public class NQueensSudoku {

    static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        char[][] board = new char[n][n];
        for (char[] row : board) Arrays.fill(row, '.');
        place(0, n, board, new boolean[n], new boolean[2 * n - 1], new boolean[2 * n - 1], res);
        return res;
    }

    static void place(int r, int n, char[][] b, boolean[] col, boolean[] diag, boolean[] anti, List<List<String>> res) {
        if (r == n) {
            List<String> sol = new ArrayList<>();
            for (char[] row : b) sol.add(new String(row));
            res.add(sol); return;
        }
        for (int c = 0; c < n; c++) {
            if (col[c] || diag[r - c + n - 1] || anti[r + c]) continue;
            col[c] = diag[r - c + n - 1] = anti[r + c] = true; b[r][c] = 'Q';
            place(r + 1, n, b, col, diag, anti, res);
            col[c] = diag[r - c + n - 1] = anti[r + c] = false; b[r][c] = '.';
        }
    }

    /** LC 52 with bitmasks: cols, d1, d2 are occupied masks for the current row. */
    static int totalNQueens(int n) { return count(n, 0, 0, 0, 0); }

    static int count(int n, int row, int cols, int d1, int d2) {
        if (row == n) return 1;
        int total = 0, free = ~(cols | d1 | d2) & ((1 << n) - 1);
        while (free != 0) {
            int bit = free & -free;  // lowest free column
            free -= bit;
            total += count(n, row + 1, cols | bit, (d1 | bit) << 1, (d2 | bit) >> 1);
        }
        return total;
    }

    static boolean solveSudoku(char[][] b) {
        boolean[][] row = new boolean[9][10], col = new boolean[9][10], box = new boolean[9][10];
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (b[r][c] != '.') {
            int d = b[r][c] - '0'; row[r][d] = col[c][d] = box[r / 3 * 3 + c / 3][d] = true;
        }
        return sudoku(b, 0, row, col, box);
    }

    static boolean sudoku(char[][] b, int pos, boolean[][] row, boolean[][] col, boolean[][] box) {
        if (pos == 81) return true;
        int r = pos / 9, c = pos % 9, bx = r / 3 * 3 + c / 3;
        if (b[r][c] != '.') return sudoku(b, pos + 1, row, col, box);
        for (int d = 1; d <= 9; d++) {
            if (row[r][d] || col[c][d] || box[bx][d]) continue;
            row[r][d] = col[c][d] = box[bx][d] = true; b[r][c] = (char) ('0' + d);
            if (sudoku(b, pos + 1, row, col, box)) return true;
            row[r][d] = col[c][d] = box[bx][d] = false; b[r][c] = '.';
        }
        return false;
    }

    static List<String> ratInMaze(int[][] m) {
        List<String> res = new ArrayList<>();
        int n = m.length;
        if (m[0][0] == 0 || m[n - 1][n - 1] == 0) return res;
        rat(m, 0, 0, new StringBuilder(), new boolean[n][n], res);
        return res;
    }

    static final int[] DR = {1, 0, 0, -1}, DC = {0, -1, 1, 0};
    static final char[] DIR = {'D', 'L', 'R', 'U'};

    static void rat(int[][] m, int r, int c, StringBuilder path, boolean[][] vis, List<String> res) {
        int n = m.length;
        if (r == n - 1 && c == n - 1) { res.add(path.toString()); return; }
        vis[r][c] = true;
        for (int k = 0; k < 4; k++) {
            int nr = r + DR[k], nc = c + DC[k];
            if (nr >= 0 && nc >= 0 && nr < n && nc < n && m[nr][nc] == 1 && !vis[nr][nc]) {
                path.append(DIR[k]); rat(m, nr, nc, path, vis, res); path.deleteCharAt(path.length() - 1);
            }
        }
        vis[r][c] = false;
    }

    public static void main(String[] args) {
        check(solveNQueens(4).toString(), "[[.Q.., ...Q, Q..., ..Q.], [..Q., Q..., ...Q, .Q..]]");
        check(solveNQueens(8).size(), 92); check(totalNQueens(8), 92); check(totalNQueens(1), 1);
        String[] puzzle = {"53..7....", "6..195...", ".98....6.", "8...6...3", "4..8.3..1",
                "7...2...6", ".6....28.", "...419..5", "....8..79"};
        char[][] b = new char[9][];
        for (int i = 0; i < 9; i++) b[i] = puzzle[i].toCharArray();
        check(solveSudoku(b), true);
        check(new String(b[0]), "534678912");
        int[][] maze = {{1, 0, 0, 0}, {1, 1, 0, 1}, {1, 1, 0, 0}, {0, 1, 1, 1}};
        check(ratInMaze(maze), List.of("DDRDRR", "DRDDRR"));
        System.out.println("NQueensSudoku: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
