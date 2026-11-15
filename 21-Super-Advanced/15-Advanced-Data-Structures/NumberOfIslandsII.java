import java.util.*;

/**
 * LC 305 — Number of Islands II. Explanation: NumberOfIslandsII.md
 * 1. Recount with flood fill after every addition      O(k m n)
 * 2. Incremental labels, merge smaller into larger     O(k log k) amortised
 * 3. Union-Find with path halving + union by size      O(k α(mn))
 */
public class NumberOfIslandsII {

    static final int[][] D = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static List<Integer> recount(int m, int n, int[][] pos) {
        boolean[][] land = new boolean[m][n];
        List<Integer> res = new ArrayList<>();
        for (int[] p : pos) {
            land[p[0]][p[1]] = true;
            boolean[][] seen = new boolean[m][n];
            int cnt = 0;
            for (int r = 0; r < m; r++) for (int c = 0; c < n; c++) {
                if (!land[r][c] || seen[r][c]) continue;
                cnt++;
                Deque<int[]> st = new ArrayDeque<>(List.of(new int[]{r, c})); seen[r][c] = true;
                while (!st.isEmpty()) {
                    int[] x = st.pop();
                    for (int[] d : D) {
                        int nr = x[0] + d[0], nc = x[1] + d[1];
                        if (nr >= 0 && nc >= 0 && nr < m && nc < n && land[nr][nc] && !seen[nr][nc]) { seen[nr][nc] = true; st.push(new int[]{nr, nc}); }
                    }
                }
            }
            res.add(cnt);
        }
        return res;
    }

    static List<Integer> labels(int m, int n, int[][] pos) {
        int[] label = new int[m * n];
        Arrays.fill(label, -1);
        Map<Integer, List<Integer>> members = new HashMap<>();
        int nextLabel = 0;
        List<Integer> res = new ArrayList<>();
        for (int[] p : pos) {
            int id = p[0] * n + p[1];
            if (label[id] == -1) {
                int my = nextLabel++;
                label[id] = my;
                members.put(my, new ArrayList<>(List.of(id)));
                for (int[] d : D) {
                    int r = p[0] + d[0], c = p[1] + d[1];
                    if (r < 0 || c < 0 || r >= m || c >= n || label[r * n + c] == -1) continue;
                    int other = label[r * n + c], mine = label[id];
                    if (other == mine) continue;
                    int big = members.get(other).size() >= members.get(mine).size() ? other : mine, small = big == other ? mine : other;
                    for (int cell : members.get(small)) label[cell] = big;     // small-to-large relabel
                    members.get(big).addAll(members.remove(small));
                }
            }
            res.add(members.size());
        }
        return res;
    }

    static int[] parent, size;

    static int find(int x) { while (parent[x] != x) { parent[x] = parent[parent[x]]; x = parent[x]; } return x; }

    static List<Integer> numIslands2(int m, int n, int[][] positions) {
        parent = new int[m * n]; size = new int[m * n];
        Arrays.fill(parent, -1);
        List<Integer> res = new ArrayList<>();
        int count = 0;
        for (int[] p : positions) {
            int id = p[0] * n + p[1];
            if (parent[id] == -1) {
                parent[id] = id; size[id] = 1; count++;
                for (int[] d : D) {
                    int r = p[0] + d[0], c = p[1] + d[1], nid = r * n + c;
                    if (r < 0 || c < 0 || r >= m || c >= n || parent[nid] == -1) continue;
                    int a = find(id), b = find(nid);
                    if (a == b) continue;
                    if (size[a] < size[b]) { int t = a; a = b; b = t; }
                    parent[b] = a; size[a] += size[b]; count--;
                }
            }
            res.add(count);
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] pos = {{0, 0}, {0, 1}, {1, 2}, {2, 1}};
        check(numIslands2(3, 3, pos), List.of(1, 1, 2, 3)); check(labels(3, 3, pos), List.of(1, 1, 2, 3));
        check(numIslands2(3, 3, new int[][]{{0, 0}, {0, 1}, {1, 2}, {1, 2}}), List.of(1, 1, 2, 2));   // duplicate
        Random r = new Random(71);
        for (int t = 0; t < 1500; t++) {
            int m = r.nextInt(5) + 1, n = r.nextInt(5) + 1;
            int[][] p = new int[r.nextInt(m * n + 3) + 1][];
            for (int i = 0; i < p.length; i++) p[i] = new int[]{r.nextInt(m), r.nextInt(n)};
            List<Integer> e = recount(m, n, p);
            check(labels(m, n, p), e); check(numIslands2(m, n, p), e);
        }
        System.out.println("NumberOfIslandsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
