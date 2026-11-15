import java.util.*;

/**
 * LC 37 — Sudoku Solver. Explanation: SudokuSolver.md
 * 1. Naive backtracking with 27-cell validity scans
 * 2. Backtracking with row/col/box bitmasks (cells in order)
 * 3. Bitmasks + MRV (most constrained cell first)
 * Node counters show how much each idea prunes.
 */
public class SudokuSolver {
    static long nodes;

    // ---------- 1. Naive ----------
    static boolean naive(char[][] b) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            if (b[r][c] != '.') continue;
            for (char d = '1'; d <= '9'; d++) {
                if (!valid(b, r, c, d)) continue;
                nodes++;
                b[r][c] = d;
                if (naive(b)) return true;
                b[r][c] = '.';
            }
            return false;
        }
        return true;
    }

    static boolean valid(char[][] b, int r, int c, char d) {
        for (int i = 0; i < 9; i++) {
            if (b[r][i] == d || b[i][c] == d) return false;
            if (b[r / 3 * 3 + i / 3][c / 3 * 3 + i % 3] == d) return false;
        }
        return true;
    }

    // ---------- 2 & 3. Bitmasks ----------
    static int[] rows, cols, boxes;

    static void init(char[][] b) {
        rows = new int[9]; cols = new int[9]; boxes = new int[9];
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) if (b[r][c] != '.') toggle(r, c, b[r][c] - '1');
    }

    static void toggle(int r, int c, int d) { rows[r] ^= 1 << d; cols[c] ^= 1 << d; boxes[r / 3 * 3 + c / 3] ^= 1 << d; }

    static int candidates(int r, int c) { return ~(rows[r] | cols[c] | boxes[r / 3 * 3 + c / 3]) & 0x1FF; }

    static boolean masksInOrder(char[][] b, int pos) {
        while (pos < 81 && b[pos / 9][pos % 9] != '.') pos++;
        if (pos == 81) return true;
        int r = pos / 9, c = pos % 9;
        for (int m = candidates(r, c); m != 0; m &= m - 1) {
            int d = Integer.numberOfTrailingZeros(m);
            nodes++;
            toggle(r, c, d); b[r][c] = (char) ('1' + d);
            if (masksInOrder(b, pos + 1)) return true;
            toggle(r, c, d); b[r][c] = '.';
        }
        return false;
    }

    static boolean mrv(char[][] b) {
        int bestR = -1, bestC = -1, bestCnt = 10;
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            if (b[r][c] != '.') continue;
            int cnt = Integer.bitCount(candidates(r, c));
            if (cnt < bestCnt) { bestCnt = cnt; bestR = r; bestC = c; if (cnt == 0) return false; }   // dead end
        }
        if (bestR == -1) return true;                          // no empty cell
        for (int m = candidates(bestR, bestC); m != 0; m &= m - 1) {
            int d = Integer.numberOfTrailingZeros(m);
            nodes++;
            toggle(bestR, bestC, d); b[bestR][bestC] = (char) ('1' + d);
            if (mrv(b)) return true;
            toggle(bestR, bestC, d); b[bestR][bestC] = '.';
        }
        return false;
    }

    static void solveSudoku(char[][] b) { init(b); mrv(b); }

    static char[][] parse(String s) { char[][] b = new char[9][9]; for (int i = 0; i < 81; i++) b[i / 9][i % 9] = s.charAt(i); return b; }

    static boolean isSolved(char[][] b, char[][] puzzle) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            if (b[r][c] == '.' || (puzzle[r][c] != '.' && puzzle[r][c] != b[r][c])) return false;
            char d = b[r][c]; b[r][c] = '.';
            boolean ok = valid(b, r, c, d);
            b[r][c] = d;
            if (!ok) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String lc = "53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79";
        String hard = "8..........36......7..9.2...5...7.......457.....1...3...1....68..85...1..9....4..";   // "world's hardest" style
        for (String p : new String[]{lc, hard}) {
            char[][] puzzle = parse(p);
            char[][] a = parse(p), b = parse(p), c = parse(p);
            long n1 = 0, n2, n3;
            if (p.equals(lc)) { nodes = 0; check(naive(a), true); n1 = nodes; check(isSolved(a, puzzle), true); }
            nodes = 0; init(b); check(masksInOrder(b, 0), true); n2 = nodes; check(isSolved(b, puzzle), true);
            nodes = 0; init(c); check(mrv(c), true); n3 = nodes; check(isSolved(c, puzzle), true);
            check(n3 <= n2, true);
            System.out.println("  puzzle " + (p.equals(lc) ? "LC example" : "hard") + ": nodes naive=" + (n1 == 0 ? "skipped" : n1) + " inOrder=" + n2 + " MRV=" + n3);
        }
        char[][] board = parse(lc); solveSudoku(board);
        check(new String(board[0]), "534678912");
        System.out.println("SudokuSolver: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
