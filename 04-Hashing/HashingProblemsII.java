import java.util.*;

/**
 * LC 36  Valid sudoku: one pass with sets per row, column and box (box id = r/3*3 + c/3). O(81)
 * LC 974 Subarray sums divisible by K: brute O(n^2) | prefix mod counts; Java % can be negative -> ((x%k)+k)%k. O(n)
 * LC 454 4Sum II: brute O(n^4) | map of a+b sums, count -(c+d). O(n^2)
 * LC 554 Brick wall: count how many rows have an edge at each position; answer = rows - max edges. O(total bricks)
 */
public class HashingProblemsII {

    static boolean isValidSudoku(char[][] b) {
        Set<String> seen = new HashSet<>();
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++) {
                char ch = b[r][c];
                if (ch == '.') continue;
                if (!seen.add(ch + "r" + r) || !seen.add(ch + "c" + c) || !seen.add(ch + "b" + (r / 3 * 3 + c / 3))) return false;
            }
        return true;
    }

    static int subarraysDivByKBrute(int[] a, int k) {
        int c = 0;
        for (int i = 0; i < a.length; i++) { int s = 0; for (int j = i; j < a.length; j++) { s += a[j]; if (s % k == 0) c++; } }
        return c;
    }

    static int subarraysDivByK(int[] a, int k) {
        int[] count = new int[k];
        count[0] = 1;
        int sum = 0, ans = 0;
        for (int x : a) {
            sum = ((sum + x) % k + k) % k;
            ans += count[sum]++;
        }
        return ans;
    }

    static int fourSumCountBrute(int[] a, int[] b, int[] c, int[] d) {
        int cnt = 0;
        for (int w : a) for (int x : b) for (int y : c) for (int z : d) if (w + x + y + z == 0) cnt++;
        return cnt;
    }

    static int fourSumCount(int[] a, int[] b, int[] c, int[] d) {
        Map<Integer, Integer> ab = new HashMap<>();
        for (int x : a) for (int y : b) ab.merge(x + y, 1, Integer::sum);
        int cnt = 0;
        for (int x : c) for (int y : d) cnt += ab.getOrDefault(-(x + y), 0);
        return cnt;
    }

    static int leastBricks(List<List<Integer>> wall) {
        Map<Integer, Integer> edges = new HashMap<>();
        int maxEdges = 0;
        for (List<Integer> row : wall) {
            int pos = 0;
            for (int i = 0; i < row.size() - 1; i++) {   // skip the wall's right edge
                pos += row.get(i);
                maxEdges = Math.max(maxEdges, edges.merge(pos, 1, Integer::sum));
            }
        }
        return wall.size() - maxEdges;
    }

    public static void main(String[] args) {
        String[] good = {"53..7....", "6..195...", ".98....6.", "8...6...3", "4..8.3..1", "7...2...6", ".6....28.", "...419..5", "....8..79"};
        char[][] b = new char[9][];
        for (int i = 0; i < 9; i++) b[i] = good[i].toCharArray();
        check(isValidSudoku(b), true);
        b[0][0] = '8'; check(isValidSudoku(b), false);
        int[] a = {4, 5, 0, -2, -3, 1};
        check(subarraysDivByKBrute(a, 5), 7); check(subarraysDivByK(a, 5), 7); check(subarraysDivByK(new int[]{-1, 2, 9}, 2), 2);
        int[] p = {1, 2}, q = {-2, -1}, r = {-1, 2}, s = {0, 2};
        check(fourSumCountBrute(p, q, r, s), 2); check(fourSumCount(p, q, r, s), 2);
        check(leastBricks(List.of(List.of(1, 2, 2, 1), List.of(3, 1, 2), List.of(1, 3, 2), List.of(2, 4), List.of(3, 1, 2), List.of(1, 3, 1, 1))), 2);
        check(leastBricks(List.of(List.of(1), List.of(1), List.of(1))), 3);
        System.out.println("HashingProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
