import java.util.*;

/**
 * LC 407 — Trapping Rain Water II. Explanation: TrappingRainWaterII.md
 * 1. Iterative relaxation of water levels until stable    O((RC)^2) worst
 * 2. Min-heap flood from the border (minimax Dijkstra)     O(RC log RC)
 */
public class TrappingRainWaterII {

    static final int[][] D = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int relaxation(int[][] h) {
        int R = h.length, C = h[0].length;
        int[][] level = new int[R][C];
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++)
            level[r][c] = (r == 0 || c == 0 || r == R - 1 || c == C - 1) ? h[r][c] : Integer.MAX_VALUE;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int r = 1; r < R - 1; r++) for (int c = 1; c < C - 1; c++) {
                int m = Integer.MAX_VALUE;
                for (int[] d : D) m = Math.min(m, level[r + d[0]][c + d[1]]);
                int nv = Math.max(h[r][c], m);
                if (nv < level[r][c]) { level[r][c] = nv; changed = true; }
            }
        }
        int water = 0;
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) water += level[r][c] - h[r][c];
        return water;
    }

    static int trapRainWater(int[][] h) {
        int R = h.length, C = h[0].length;
        if (R < 3 || C < 3) return 0;
        boolean[][] vis = new boolean[R][C];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[2]));
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++)
            if (r == 0 || c == 0 || r == R - 1 || c == C - 1) { pq.offer(new int[]{r, c, h[r][c]}); vis[r][c] = true; }
        int water = 0;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            for (int[] d : D) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr < 0 || nc < 0 || nr >= R || nc >= C || vis[nr][nc]) continue;
                vis[nr][nc] = true;
                water += Math.max(0, cur[2] - h[nr][nc]);
                pq.offer(new int[]{nr, nc, Math.max(cur[2], h[nr][nc])});
            }
        }
        return water;
    }

    public static void main(String[] args) {
        check(trapRainWater(new int[][]{{1, 4, 3, 1, 3, 2}, {3, 2, 1, 3, 2, 4}, {2, 3, 3, 2, 3, 1}}), 4);
        check(trapRainWater(new int[][]{{3, 3, 3, 3, 3}, {3, 2, 2, 2, 3}, {3, 2, 1, 2, 3}, {3, 2, 2, 2, 3}, {3, 3, 3, 3, 3}}), 10);
        Random r = new Random(39);
        for (int t = 0; t < 1500; t++) {
            int R = r.nextInt(6) + 1, C = r.nextInt(6) + 1;
            int[][] h = new int[R][C];
            for (int[] row : h) for (int c = 0; c < C; c++) row[c] = r.nextInt(6);
            check(trapRainWater(h), relaxation(h));
        }
        System.out.println("TrappingRainWaterII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
