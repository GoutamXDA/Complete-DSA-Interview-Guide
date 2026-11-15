import java.util.*;

/**
 * LC 149 — Max Points on a Line. Explanation: MaxPointsOnALine.md
 * 1. Every pair + cross product test     O(n^3) O(1)
 * 2. Anchor + reduced-slope hash map     O(n^2 log C) O(n)   (handles duplicate points too)
 */
public class MaxPointsOnALine {

    static int brute(int[][] p) {
        int n = p.length;
        if (n <= 2) return n;
        int best = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                long dx = p[j][0] - p[i][0], dy = p[j][1] - p[i][1];
                int cnt = 0;
                for (int k = 0; k < n; k++) {
                    if (dx == 0 && dy == 0) { if (p[k][0] == p[i][0] && p[k][1] == p[i][1]) cnt++; continue; }
                    if (dx * (p[k][1] - p[i][1]) - dy * (p[k][0] - p[i][0]) == 0) cnt++;
                }
                best = Math.max(best, cnt);
            }
        return best;
    }

    static int maxPoints(int[][] p) {
        int n = p.length;
        if (n <= 2) return n;
        int best = 0;
        Map<Long, Integer> slopes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            slopes.clear();
            int dup = 0, bestHere = 0;
            for (int j = i + 1; j < n; j++) {
                int dx = p[j][0] - p[i][0], dy = p[j][1] - p[i][1];
                if (dx == 0 && dy == 0) { dup++; continue; }
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g; dy /= g;
                if (dx < 0 || (dx == 0 && dy < 0)) { dx = -dx; dy = -dy; }   // canonical direction
                long key = (long) dx * 1_000_003L + dy;
                bestHere = Math.max(bestHere, slopes.merge(key, 1, Integer::sum));
            }
            best = Math.max(best, bestHere + dup + 1);
        }
        return best;
    }

    static int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }

    public static void main(String[] args) {
        check(maxPoints(new int[][]{{1, 1}, {2, 2}, {3, 3}}), 3);
        int[][] b = {{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}};
        check(maxPoints(b), 4); check(brute(b), 4);
        check(maxPoints(new int[][]{{0, 0}, {94911151, 94911150}, {94911152, 94911151}}), 2);   // float-precision trap
        check(maxPoints(new int[][]{{1, 1}, {1, 1}, {2, 3}}), 3);                                  // duplicates
        check(maxPoints(new int[][]{{0, 0}, {0, 5}, {0, -3}, {1, 1}}), 3);                         // vertical
        Random r = new Random(3);
        for (int k = 0; k < 500; k++) {
            int[][] p = new int[r.nextInt(9) + 1][];
            for (int i = 0; i < p.length; i++) p[i] = new int[]{r.nextInt(5) - 2, r.nextInt(5) - 2};
            check(maxPoints(p), brute(p));
        }
        System.out.println("MaxPointsOnALine: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
